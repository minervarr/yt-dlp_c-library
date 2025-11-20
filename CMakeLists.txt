cmake_minimum_required(VERSION 3.15)
project(yt-dlp-zoom-test VERSION 0.1.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Simple standalone URL parsing test (no dependencies)
add_executable(test_zoom_url_parsing
    tools/test_zoom_url_parsing.cpp
)

message(STATUS "")
message(STATUS "Build configured successfully!")
message(STATUS "Run: cmake --build .")
message(STATUS "Then: ./test_zoom_url_parsing")
message(STATUS "")
