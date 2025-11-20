package com.ytdlp.zoom.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ytdlp.zoom.ZoomDownloader
import com.ytdlp.zoom.ZoomExtractor
import kotlinx.coroutines.launch
import java.io.File

/**
 * Example Activity showing how to use the Zoom downloader
 */
class MainActivity : AppCompatActivity() {

    private lateinit var downloader: ZoomDownloader
    private val TAG = "MainActivity"

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize downloader
        downloader = ZoomDownloader(this)

        // Check permissions
        checkPermissions()

        // Example 1: Check if URL is valid
        exampleCheckUrl()

        // Example 2: Get video information
        exampleGetVideoInfo()

        // Example 3: Download video
        // exampleDownloadVideo()
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    /**
     * Example 1: Check if a URL is a valid Zoom recording
     */
    private fun exampleCheckUrl() {
        val testUrl = "https://utec.zoom.us/rec/play/k-O3Gvpp31NECNZswOUC0bEdaC7GUA6tH4jzeIUSTsF"

        val isValid = downloader.isValidUrl(testUrl)
        Log.i(TAG, "URL is valid: $isValid")

        if (isValid) {
            val videoId = downloader.extractVideoId(testUrl)
            Log.i(TAG, "Video ID: $videoId")
        }
    }

    /**
     * Example 2: Get video information
     */
    private fun exampleGetVideoInfo() {
        val url = "https://utec.zoom.us/rec/play/k-O3Gvpp31NECNZswOUC0bEdaC7GUA6tH4jzeIUSTsF-CIyVouzeKvgYczjOWKfuvRZ6JRQuxeV34Cs.5pPB_1cFFPVdPX2Z"

        // Optional: Path to cookies file
        val cookiesFile = File(getExternalFilesDir(null), "cookies.txt")

        lifecycleScope.launch {
            try {
                Log.i(TAG, "Getting video info...")

                val videoInfo = downloader.getVideoInfo(url, cookiesFile)

                if (videoInfo != null) {
                    Log.i(TAG, "Title: ${videoInfo.title}")
                    Log.i(TAG, "Duration: ${videoInfo.formatDuration()}")
                    Log.i(TAG, "File size: ${videoInfo.fileSize} MB")
                    Log.i(TAG, "Has transcript: ${videoInfo.hasTranscript}")

                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Video: ${videoInfo.title}\nDuration: ${videoInfo.formatDuration()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Log.e(TAG, "Failed to get video info")
                    showError("Failed to get video information")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting video info", e)
                showError("Error: ${e.message}")
            }
        }
    }

    /**
     * Example 3: Download video
     */
    private fun exampleDownloadVideo() {
        val url = "https://utec.zoom.us/rec/play/k-O3Gvpp31NECNZswOUC0bEdaC7GUA6tH4jzeIUSTsF-CIyVouzeKvgYczjOWKfuvRZ6JRQuxeV34Cs.5pPB_1cFFPVdPX2Z"

        // Output file in Downloads folder
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(downloadsDir, "zoom_recording.mp4")

        // Optional: Cookies file
        val cookiesFile = File(getExternalFilesDir(null), "cookies.txt")

        lifecycleScope.launch {
            try {
                Log.i(TAG, "Starting download...")
                showMessage("Starting download...")

                val result = downloader.downloadVideo(
                    url = url,
                    outputFile = outputFile,
                    cookiesFile = if (cookiesFile.exists()) cookiesFile else null,
                    onProgress = { progress ->
                        Log.d(TAG, "Download progress: ${progress * 100}%")
                    }
                )

                if (result.isSuccess) {
                    val file = result.getOrNull()
                    Log.i(TAG, "Download complete: ${file?.absolutePath}")
                    showMessage("Download complete!\nSaved to: ${file?.absolutePath}")
                } else {
                    val error = result.exceptionOrNull()
                    Log.e(TAG, "Download failed", error)
                    showError("Download failed: ${error?.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading video", e)
                showError("Error: ${e.message}")
            }
        }
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showError(message: String) {
        runOnUiThread {
            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
        }
    }
}
