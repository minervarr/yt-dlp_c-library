# Download Timeout Fix - Implementation Summary

## Problem Identified

From LAST_CHAT.md, the issue was:
1. ✅ Format selection working - correctly selects format 401 (AV1 2160p)
2. ❌ Download timing out - "CURL error: Timeout was reached" after 60 seconds
3. Fallback to 360p progressive format which downloaded successfully

## Root Causes

1. **Hard 60-second timeout**: `CurlHttpClient::Config::timeout = 60` killed any download taking longer than 60 seconds
2. **Memory-inefficient download**: Loaded entire file into memory before writing to disk
3. **No progress reporting**: User couldn't see download progress
4. **Duplicate timeout setting**: `YoutubeDLParams::socket_timeout = 60` was overriding the config

## Solution Implemented

### 1. Fixed Timeout Strategy ✅

**Changes to `include/ytdlp/networking/curl_http_client.hpp`**:
- Changed `timeout` default from 60 to 0 (no hard timeout)
- Added `low_speed_limit = 1000` bytes/sec (stall detection threshold)
- Added `low_speed_time = 30` seconds (stall detection window)

**Rationale**: Match Python yt-dlp behavior - no hard timeout, but abort if speed drops below 1KB/s for 30 seconds.

**Changes to `src/networking/curl_http_client.cpp`**:
- Added `CURLOPT_LOW_SPEED_LIMIT` and `CURLOPT_LOW_SPEED_TIME` configuration
- Keeps `CURLOPT_CONNECTTIMEOUT` for connection establishment (10s)

**Changes to `include/ytdlp/core/youtube_dl.hpp`**:
- Changed `socket_timeout` default from 60 to 0
- Added `low_speed_limit` and `low_speed_time` parameters

**Changes to `src/core/youtube_dl.cpp`**:
- Pass low-speed configuration from params to HTTP client config

### 2. Implemented Streaming Download ✅

**New method in `CurlHttpClient`**:
```cpp
bool download_to_file(
    const std::string& url,
    const std::string& output_path,
    const std::map<std::string, std::string>& headers,
    std::function<void(int64_t, int64_t)> progress_callback
);
```

**Implementation**:
- Uses `CURLOPT_WRITEFUNCTION` to stream data directly to file
- Writes data in chunks (8KB blocks) as received from server
- No memory buffering - efficient for large files
- Progress callback using `CURLOPT_XFERINFOFUNCTION`

### 3. Added Progress Reporting ✅

**Updated `src/main.cpp` download_file()**:
- Real-time progress display: `[download] 45.3% of 1.03GB at 8.2MB/s ETA 00:07`
- Updates every 500ms to avoid spam
- Shows percentage, total size, download speed, and ETA

## Expected Results

✅ **Timeout Fix**: Large 4K downloads (1GB+) should complete without timing out
✅ **Efficient Memory**: Only uses ~8KB buffer instead of loading entire file
✅ **Progress Feedback**: User sees real-time download progress
✅ **Stall Detection**: Aborts if download stalls (< 1KB/s for 30 seconds)

## Blocking Issue - YouTube Extractor Crash

❌ **CRITICAL**: The YouTube extractor crashes before any download starts

**Crash Location**: During video info extraction at "Downloading webpage to extract player URL"
- File: `src/extractor/youtube.cpp:536`
- Function: `_download_player()` calling `downloader()->http_client()`
- Error: Segmentation fault (core dumped)

**Timeline**:
- Crash exists from first commit (381e00f)
- Not related to download timeout fixes
- Prevents testing of download improvements

**Impact**: Cannot test download timeout fix until extractor crash is resolved.

## Files Modified

1. `include/ytdlp/networking/curl_http_client.hpp` - Timeout config, streaming download declaration
2. `src/networking/curl_http_client.cpp` - Low-speed limits, streaming download implementation
3. `include/ytdlp/core/youtube_dl.hpp` - Timeout parameters
4. `src/core/youtube_dl.cpp` - Pass timeout params to HTTP client
5. `src/main.cpp` - Progress reporting in download_file()

## Next Steps

1. **Fix YouTube extractor crash** (blocking all testing)
2. Test large file download with real YouTube video
3. Verify timeout settings work correctly
4. Confirm high-quality format (401) downloads successfully

## Test Commands

Once extractor is fixed:
```bash
./yt-dlp-cpp -u "https://www.youtube.com/watch?v=STBSUasnJ5s"
```

Expected: Download format 401 (AV1 2160p, ~1GB) with progress display and no timeout.
