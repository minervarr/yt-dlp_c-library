package com.ytdlp.zoom;

/**
 * Native interface for yt-dlp C++ library
 */
public class YtdlpNative {

    static {
        System.loadLibrary("ytdlp-zoom");
    }

    /**
     * Initialize the native library
     * @return true if initialization was successful
     */
    public static native boolean initialize();

    /**
     * Get library version
     * @return version string
     */
    public static native String getVersion();
}
