cmake_minimum_required(VERSION 3.15)
project(yt-dlp-zoom-test VERSION 0.1.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Use system libraries
find_package(CURL REQUIRED)
find_package(OpenSSL REQUIRED)
find_package(fmt REQUIRED)
find_package(nlohmann_json REQUIRED)

include_directories(${CMAKE_SOURCE_DIR}/include)

# Zoom extractor test executable
add_executable(test_zoom_extractor
    tools/test_zoom_extractor.cpp
    src/core/youtube_dl.cpp
    src/extractor/zoom.cpp
    src/extractor/info_extractor.cpp
    src/networking/curl_http_client.cpp
    src/networking/request.cpp
    src/networking/response.cpp
    src/networking/cookie.cpp
    src/networking/cookie_jar.cpp
    src/networking/http_header_dict.cpp
    src/utils/string_utils.cpp
    src/utils/json_utils.cpp
    src/utils/filesystem_utils.cpp
    src/utils/network_utils.cpp
)

target_link_libraries(test_zoom_extractor
    PRIVATE
        CURL::libcurl
        OpenSSL::SSL
        OpenSSL::Crypto
        fmt::fmt
        nlohmann_json::nlohmann_json
)

# Simple standalone URL parsing test (no dependencies)
add_executable(test_zoom_url_parsing
    tools/test_zoom_url_parsing.cpp
)

# Debug tool to inspect downloaded webpage
add_executable(test_zoom_debug
    tools/test_zoom_debug.cpp
    src/core/youtube_dl.cpp
    src/networking/curl_http_client.cpp
    src/networking/request.cpp
    src/networking/response.cpp
    src/networking/cookie.cpp
    src/networking/cookie_jar.cpp
    src/networking/http_header_dict.cpp
    src/utils/string_utils.cpp
    src/utils/filesystem_utils.cpp
    src/utils/network_utils.cpp
)

target_link_libraries(test_zoom_debug
    PRIVATE
        CURL::libcurl
        OpenSSL::SSL
        OpenSSL::Crypto
        fmt::fmt
)

# Test calling Zoom API directly
add_executable(test_zoom_api
    tools/test_zoom_api.cpp
    src/core/youtube_dl.cpp
    src/networking/curl_http_client.cpp
    src/networking/request.cpp
    src/networking/response.cpp
    src/networking/cookie.cpp
    src/networking/cookie_jar.cpp
    src/networking/http_header_dict.cpp
    src/utils/string_utils.cpp
    src/utils/filesystem_utils.cpp
    src/utils/network_utils.cpp
)

target_link_libraries(test_zoom_api
    PRIVATE
        CURL::libcurl
        OpenSSL::SSL
        OpenSSL::Crypto
        fmt::fmt
        nlohmann_json::nlohmann_json
)

message(STATUS "")
message(STATUS "Build configured successfully!")
message(STATUS "")
message(STATUS "Targets:")
message(STATUS "  test_zoom_url_parsing - Standalone URL parsing test")
message(STATUS "  test_zoom_extractor   - Full Zoom extractor test")
message(STATUS "  test_zoom_debug       - Debug tool to inspect webpage")
message(STATUS "  test_zoom_api         - Test Zoom API endpoint directly")
message(STATUS "")
message(STATUS "Run: cmake --build .")
message(STATUS "")
