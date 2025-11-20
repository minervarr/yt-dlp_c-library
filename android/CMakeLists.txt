# CMakeLists.txt for Android NDK
cmake_minimum_required(VERSION 3.18.1)

project(ytdlp-zoom VERSION 1.0.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Android specific settings
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--no-undefined")

# Include directories
include_directories(
    ${CMAKE_SOURCE_DIR}/../include
    ${CMAKE_SOURCE_DIR}/jni
)

# Source files
set(YTDLP_SOURCES
    # Core
    ../src/core/youtube_dl.cpp

    # Extractors
    ../src/extractor/info_extractor.cpp
    ../src/extractor/zoom.cpp

    # Networking
    ../src/networking/curl_http_client.cpp
    ../src/networking/request.cpp
    ../src/networking/response.cpp
    ../src/networking/cookie.cpp
    ../src/networking/cookie_jar.cpp
    ../src/networking/http_header_dict.cpp

    # Utils
    ../src/utils/string_utils.cpp
    ../src/utils/json_utils.cpp
    ../src/utils/filesystem_utils.cpp
    ../src/utils/network_utils.cpp
)

# JNI wrapper sources
set(JNI_SOURCES
    jni/ytdlp_jni.cpp
    jni/zoom_extractor_jni.cpp
)

# Find required Android libraries
find_library(log-lib log)
find_library(android-lib android)

# Create shared library for Android
add_library(ytdlp-zoom SHARED
    ${YTDLP_SOURCES}
    ${JNI_SOURCES}
)

# Link libraries
target_link_libraries(ytdlp-zoom
    PRIVATE
        ${log-lib}
        ${android-lib}
        curl
        ssl
        crypto
        z
)

# Export symbols
set_target_properties(ytdlp-zoom PROPERTIES
    CXX_VISIBILITY_PRESET hidden
    VISIBILITY_INLINES_HIDDEN ON
)

# Install
install(TARGETS ytdlp-zoom
    LIBRARY DESTINATION lib/${ANDROID_ABI}
)
