package com.ytdlp.zoom

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File

/**
 * Kotlin wrapper for Zoom video downloader
 * Provides coroutine-based API for downloading Zoom recordings
 */
class ZoomDownloader(private val context: Context) {

    companion object {
        private const val TAG = "ZoomDownloader"

        init {
            System.loadLibrary("ytdlp-zoom")
        }
    }

    /**
     * Check if URL is a valid Zoom recording
     */
    fun isValidUrl(url: String): Boolean {
        return ZoomExtractor.isValidUrl(url)
    }

    /**
     * Get video information (suspend function for coroutines)
     */
    suspend fun getVideoInfo(url: String, cookiesFile: File? = null): VideoInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val cookiesPath = cookiesFile?.absolutePath
                ZoomExtractor.getVideoInfo(url, cookiesPath)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting video info", e)
                null
            }
        }
    }

    /**
     * Download video (suspend function for coroutines)
     */
    suspend fun downloadVideo(
        url: String,
        outputFile: File,
        cookiesFile: File? = null,
        onProgress: ((Float) -> Unit)? = null
    ): Result<File> {
        return withContext(Dispatchers.IO) {
            try {
                val cookiesPath = cookiesFile?.absolutePath

                // Create output directory if needed
                outputFile.parentFile?.mkdirs()

                Log.i(TAG, "Starting download to: ${outputFile.absolutePath}")

                val success = ZoomExtractor.downloadVideo(
                    url,
                    outputFile.absolutePath,
                    cookiesPath,
                    null // TODO: Implement progress callback
                )

                if (success) {
                    Log.i(TAG, "Download completed: ${outputFile.absolutePath}")
                    Result.success(outputFile)
                } else {
                    Log.e(TAG, "Download failed")
                    Result.failure(Exception("Download failed"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading video", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Extract video ID from URL
     */
    fun extractVideoId(url: String): String? {
        return ZoomExtractor.extractVideoId(url)
    }
}

/**
 * Data class for video information (Kotlin wrapper)
 */
data class VideoInfo(
    val videoId: String,
    val title: String,
    val duration: Int,
    val downloadUrl: String,
    val chapterUrl: String?,
    val fileSize: Long,
    val meetingTopic: String,
    val meetingStartTime: String?,
    val hasTranscript: Boolean
) {
    companion object {
        fun fromJavaVideoInfo(javaInfo: ZoomExtractor.VideoInfo): VideoInfo {
            return VideoInfo(
                videoId = javaInfo.videoId ?: "",
                title = javaInfo.title ?: "",
                duration = javaInfo.duration,
                downloadUrl = javaInfo.downloadUrl ?: "",
                chapterUrl = javaInfo.chapterUrl,
                fileSize = javaInfo.fileSize,
                meetingTopic = javaInfo.meetingTopic ?: "",
                meetingStartTime = javaInfo.meetingStartTime,
                hasTranscript = javaInfo.hasTranscript
            )
        }
    }

    /**
     * Format duration as HH:MM:SS
     */
    fun formatDuration(): String {
        val hours = duration / 3600
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
