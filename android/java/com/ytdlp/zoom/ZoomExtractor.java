package com.ytdlp.zoom;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Zoom recording extractor
 * Downloads videos from Zoom recordings
 */
public class ZoomExtractor {

    private static final String TAG = "ZoomExtractor";

    static {
        System.loadLibrary("ytdlp-zoom");
    }

    /**
     * Check if URL is a valid Zoom recording URL
     * @param url Zoom recording URL
     * @return true if URL is valid
     */
    public static native boolean isValidUrl(String url);

    /**
     * Extract video ID from Zoom URL
     * @param url Zoom recording URL
     * @return video ID or null if invalid
     */
    public static native String extractVideoId(String url);

    /**
     * Extract video information from Zoom URL
     * @param url Zoom recording URL
     * @param cookiesFile Path to Netscape cookies file (can be null)
     * @return JSON string with video metadata or null on error
     */
    public static native String extractInfo(String url, String cookiesFile);

    /**
     * Download video from Zoom URL
     * @param url Zoom recording URL
     * @param outputPath Path where to save the video file
     * @param cookiesFile Path to Netscape cookies file (can be null)
     * @param progressCallback Callback for download progress (currently unused)
     * @return true if download was successful
     */
    public static native boolean downloadVideo(
        String url,
        String outputPath,
        String cookiesFile,
        Object progressCallback
    );

    /**
     * Convenience method to get video info as VideoInfo object
     * @param url Zoom recording URL
     * @param cookiesFile Path to cookies file (can be null)
     * @return VideoInfo object or null on error
     */
    public static VideoInfo getVideoInfo(String url, String cookiesFile) {
        String jsonStr = extractInfo(url, cookiesFile);
        if (jsonStr == null) {
            Log.e(TAG, "Failed to extract video info");
            return null;
        }

        try {
            JSONObject json = new JSONObject(jsonStr);
            return VideoInfo.fromJson(json);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON", e);
            return null;
        }
    }

    /**
     * Convenience method to download video
     * @param url Zoom recording URL
     * @param outputPath Path where to save the video
     * @param cookiesFile Path to cookies file (can be null)
     * @return true if download was successful
     */
    public static boolean download(String url, String outputPath, String cookiesFile) {
        return downloadVideo(url, outputPath, cookiesFile, null);
    }

    /**
     * Video information container
     */
    public static class VideoInfo {
        public String videoId;
        public String title;
        public int duration;
        public String downloadUrl;
        public String chapterUrl;
        public long fileSize;
        public String meetingTopic;
        public String meetingStartTime;
        public boolean hasTranscript;

        public static VideoInfo fromJson(JSONObject json) throws JSONException {
            VideoInfo info = new VideoInfo();

            JSONObject result = json.getJSONObject("result");

            // Basic info
            info.duration = result.optInt("duration", 0);
            info.downloadUrl = result.optString("viewMp4Url", "");
            info.chapterUrl = result.optString("chapterUrl", "");
            info.hasTranscript = result.optBoolean("hasTranscript", false);

            // Meeting info
            if (result.has("meet")) {
                JSONObject meet = result.getJSONObject("meet");
                info.meetingTopic = meet.optString("topic", "");
                info.meetingStartTime = meet.optString("meetingStartTimeStr", "");
                info.title = info.meetingTopic;
            }

            // Recording info
            if (result.has("recording")) {
                JSONObject recording = result.getJSONObject("recording");
                info.videoId = recording.optString("id", "");
                String fileSizeStr = recording.optString("fileSizeInMB", "0 MB");
                // Parse "55 MB" -> 55
                info.fileSize = parseSizeInMB(fileSizeStr);
            }

            return info;
        }

        private static long parseSizeInMB(String sizeStr) {
            try {
                String[] parts = sizeStr.split(" ");
                if (parts.length > 0) {
                    return Long.parseLong(parts[0]);
                }
            } catch (NumberFormatException e) {
                // Ignore
            }
            return 0;
        }

        @Override
        public String toString() {
            return "VideoInfo{" +
                    "videoId='" + videoId + '\'' +
                    ", title='" + title + '\'' +
                    ", duration=" + duration +
                    ", fileSize=" + fileSize + "MB" +
                    ", hasTranscript=" + hasTranscript +
                    '}';
        }
    }
}
