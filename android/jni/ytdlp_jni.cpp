/**
 * JNI wrapper for yt-dlp C++ library
 * Main initialization and utility functions
 */

#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "YtdlpJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Helper function to convert Java string to C++ string
std::string jstring_to_string(JNIEnv* env, jstring jstr) {
    if (!jstr) {
        return "";
    }

    const char* chars = env->GetStringUTFChars(jstr, nullptr);
    std::string result(chars);
    env->ReleaseStringUTFChars(jstr, chars);
    return result;
}

// Helper function to convert C++ string to Java string
jstring string_to_jstring(JNIEnv* env, const std::string& str) {
    return env->NewStringUTF(str.c_str());
}

extern "C" {

/**
 * Initialize the library
 */
JNIEXPORT jboolean JNICALL
Java_com_ytdlp_zoom_YtdlpNative_initialize(JNIEnv* env, jclass clazz) {
    LOGI("YtdlpNative initialized");
    return JNI_TRUE;
}

/**
 * Get library version
 */
JNIEXPORT jstring JNICALL
Java_com_ytdlp_zoom_YtdlpNative_getVersion(JNIEnv* env, jclass clazz) {
    return string_to_jstring(env, "1.0.0");
}

} // extern "C"
