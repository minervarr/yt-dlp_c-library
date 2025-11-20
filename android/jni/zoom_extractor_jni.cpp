/**
 * JNI wrapper for Zoom extractor
 * Provides Java interface to download Zoom recordings
 */

#include <jni.h>
#include <string>
#include <memory>
#include <android/log.h>

#include "ytdlp/core/youtube_dl.hpp"
#include "ytdlp/extractor/zoom.hpp"
#include "ytdlp/utils/json_utils.hpp"

#define LOG_TAG "ZoomExtractorJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Helper functions
std::string jstring_to_string(JNIEnv* env, jstring jstr);
jstring string_to_jstring(JNIEnv* env, const std::string& str);

extern "C" {

/**
 * Check if URL is a valid Zoom recording URL
 */
JNIEXPORT jboolean JNICALL
Java_com_ytdlp_zoom_ZoomExtractor_isValidUrl(JNIEnv* env, jclass clazz, jstring url) {
    std::string url_str = jstring_to_string(env, url);
    LOGI("Checking if URL is valid: %s", url_str.c_str());

    try {
        bool valid = ytdlp::extractor::ZoomIE::suitable(url_str);
        LOGI("URL valid: %d", valid);
        return valid ? JNI_TRUE : JNI_FALSE;
    } catch (const std::exception& e) {
        LOGE("Error checking URL: %s", e.what());
        return JNI_FALSE;
    }
}

/**
 * Extract video ID from Zoom URL
 */
JNIEXPORT jstring JNICALL
Java_com_ytdlp_zoom_ZoomExtractor_extractVideoId(JNIEnv* env, jclass clazz, jstring url) {
    std::string url_str = jstring_to_string(env, url);
    LOGI("Extracting video ID from: %s", url_str.c_str());

    try {
        std::string video_id = ytdlp::extractor::ZoomIE::extract_id(url_str);
        LOGI("Extracted video ID: %s", video_id.c_str());
        return string_to_jstring(env, video_id);
    } catch (const std::exception& e) {
        LOGE("Error extracting video ID: %s", e.what());
        return nullptr;
    }
}

/**
 * Extract video information from Zoom URL
 * Returns JSON string with video metadata
 */
JNIEXPORT jstring JNICALL
Java_com_ytdlp_zoom_ZoomExtractor_extractInfo(
    JNIEnv* env,
    jclass clazz,
    jstring url,
    jstring cookies_file
) {
    std::string url_str = jstring_to_string(env, url);
    std::string cookies_str = jstring_to_string(env, cookies_file);

    LOGI("Extracting info from: %s", url_str.c_str());
    if (!cookies_str.empty()) {
        LOGI("Using cookies from: %s", cookies_str.c_str());
    }

    try {
        // Create YoutubeDL instance
        ytdlp::core::YoutubeDLParams params;
        params.quiet = false;
        ytdlp::core::YoutubeDL ydl(params);

        // Load cookies if provided
        if (!cookies_str.empty()) {
            ydl.load_cookies(cookies_str);
        }

        // Create Zoom extractor
        ytdlp::extractor::ZoomIE extractor(&ydl);

        // Extract video ID
        std::string video_id = ytdlp::extractor::ZoomIE::extract_id(url_str);
        LOGI("Video ID: %s", video_id.c_str());

        // Call API endpoint directly
        std::string api_url = "https://utec.zoom.us/nws/recording/1.0/play/info/" + video_id;
        LOGI("API URL: %s", api_url.c_str());

        auto& http_client = ydl.http_client();
        ytdlp::networking::Request request(api_url);
        auto response = http_client.execute(request);

        if (!response.is_success()) {
            LOGE("API request failed with status: %d", response.status());
            return nullptr;
        }

        std::string json_text = response.read_all();
        LOGI("Received %zu bytes of JSON", json_text.size());

        // Parse JSON to validate
        auto json_data = nlohmann::json::parse(json_text);

        // Extract key information
        auto result = json_data["result"];
        std::string title = ytdlp::utils::get_string(result["meet"], "topic", "Unknown");
        int duration = ytdlp::utils::get_int(result, "duration", 0);
        std::string mp4_url = ytdlp::utils::get_string(result, "viewMp4Url", "");

        LOGI("Title: %s", title.c_str());
        LOGI("Duration: %d seconds", duration);
        LOGI("Has download URL: %d", !mp4_url.empty());

        // Return full JSON
        return string_to_jstring(env, json_text);

    } catch (const std::exception& e) {
        LOGE("Error extracting info: %s", e.what());
        return nullptr;
    }
}

/**
 * Download video from Zoom URL to specified file path
 */
JNIEXPORT jboolean JNICALL
Java_com_ytdlp_zoom_ZoomExtractor_downloadVideo(
    JNIEnv* env,
    jclass clazz,
    jstring url,
    jstring output_path,
    jstring cookies_file,
    jobject progress_callback
) {
    std::string url_str = jstring_to_string(env, url);
    std::string output_str = jstring_to_string(env, output_path);
    std::string cookies_str = jstring_to_string(env, cookies_file);

    LOGI("Downloading from: %s", url_str.c_str());
    LOGI("Output to: %s", output_str.c_str());

    try {
        // Create YoutubeDL instance
        ytdlp::core::YoutubeDLParams params;
        params.quiet = false;
        ytdlp::core::YoutubeDL ydl(params);

        // Load cookies
        if (!cookies_str.empty()) {
            ydl.load_cookies(cookies_str);
        }

        // Extract video info first
        std::string video_id = ytdlp::extractor::ZoomIE::extract_id(url_str);
        std::string api_url = "https://utec.zoom.us/nws/recording/1.0/play/info/" + video_id;

        auto& http_client = ydl.http_client();
        ytdlp::networking::Request request(api_url);
        auto response = http_client.execute(request);

        if (!response.is_success()) {
            LOGE("Failed to get video info");
            return JNI_FALSE;
        }

        std::string json_text = response.read_all();
        auto json_data = nlohmann::json::parse(json_text);
        auto result = json_data["result"];
        std::string mp4_url = ytdlp::utils::get_string(result, "viewMp4Url", "");

        if (mp4_url.empty()) {
            LOGE("No MP4 URL found in response");
            return JNI_FALSE;
        }

        LOGI("Downloading from: %s", mp4_url.c_str());

        // Download the video file
        ytdlp::networking::Request download_request(mp4_url);
        auto download_response = http_client.execute(download_request);

        if (!download_response.is_success()) {
            LOGE("Download failed with status: %d", download_response.status());
            return JNI_FALSE;
        }

        // Save to file
        std::string video_data = download_response.read_all();
        LOGI("Downloaded %zu bytes", video_data.size());

        FILE* fp = fopen(output_str.c_str(), "wb");
        if (!fp) {
            LOGE("Failed to open output file: %s", output_str.c_str());
            return JNI_FALSE;
        }

        size_t written = fwrite(video_data.data(), 1, video_data.size(), fp);
        fclose(fp);

        if (written != video_data.size()) {
            LOGE("Failed to write all data");
            return JNI_FALSE;
        }

        LOGI("Successfully downloaded to: %s", output_str.c_str());
        return JNI_TRUE;

    } catch (const std::exception& e) {
        LOGE("Error downloading video: %s", e.what());
        return JNI_FALSE;
    }
}

} // extern "C"
