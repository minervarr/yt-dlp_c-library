cmake_minimum_required(VERSION 3.15)
project(ytdlp-cpp VERSION 0.1.0 LANGUAGES CXX)

# ============================================================================
# Project Configuration
# ============================================================================
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Build type
if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Release)
endif()

# Compiler flags
set(CMAKE_CXX_FLAGS_DEBUG "-g -O0 -Wall -Wextra -Wpedantic")
set(CMAKE_CXX_FLAGS_RELEASE "-O3 -DNDEBUG")

# ============================================================================
# Third-party Dependencies
# ============================================================================

message(STATUS "")
message(STATUS "========================================")
message(STATUS "  Configuring Third-Party Libraries")
message(STATUS "========================================")

# Add third_party directory to module path
list(APPEND CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/third_party")

# CURL - HTTP/HTTPS networking
message(STATUS "Looking for CURL...")
find_package(CURL)
if(CURL_FOUND)
    message(STATUS "  ✓ Found system CURL: ${CURL_VERSION_STRING}")
else()
    message(STATUS "  → Building CURL from third_party/")
    set(BUILD_CURL_EXE OFF CACHE BOOL "" FORCE)
    set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)
    set(CURL_STATICLIB ON CACHE BOOL "" FORCE)
    set(BUILD_TESTING OFF CACHE BOOL "" FORCE)
    add_subdirectory(third_party/curl EXCLUDE_FROM_ALL)
endif()

# OpenSSL - Cryptography
message(STATUS "Looking for OpenSSL...")
find_package(OpenSSL)
if(OPENSSL_FOUND)
    message(STATUS "  ✓ Found system OpenSSL: ${OPENSSL_VERSION}")
else()
    message(STATUS "  → OpenSSL not found in system")
    message(STATUS "  → Note: OpenSSL from third_party/ requires manual build")
    message(STATUS "  → Using system OpenSSL is recommended")
    # OpenSSL doesn't have CMakeLists.txt, needs manual build
    # For now, we'll require system OpenSSL
    find_package(OpenSSL REQUIRED)
endif()

# nlohmann/json - JSON parsing (header-only)
message(STATUS "Configuring nlohmann/json...")
set(JSON_BuildTests OFF CACHE INTERNAL "")
set(JSON_Install OFF CACHE INTERNAL "")
add_subdirectory(third_party/json EXCLUDE_FROM_ALL)
message(STATUS "  ✓ nlohmann/json configured")

# fmt - String formatting
message(STATUS "Configuring fmt...")
set(FMT_INSTALL OFF CACHE INTERNAL "")
add_subdirectory(third_party/fmt EXCLUDE_FROM_ALL)
message(STATUS "  ✓ fmt configured")

# spdlog - Logging
message(STATUS "Configuring spdlog...")
set(SPDLOG_INSTALL OFF CACHE INTERNAL "")
set(SPDLOG_FMT_EXTERNAL ON CACHE INTERNAL "")
add_subdirectory(third_party/spdlog EXCLUDE_FROM_ALL)
message(STATUS "  ✓ spdlog configured")

# cxxopts - CLI argument parsing (header-only)
message(STATUS "Configuring cxxopts...")
set(CXXOPTS_BUILD_EXAMPLES OFF CACHE INTERNAL "")
set(CXXOPTS_BUILD_TESTS OFF CACHE INTERNAL "")
add_subdirectory(third_party/cxxopts EXCLUDE_FROM_ALL)
message(STATUS "  ✓ cxxopts configured")

# Boost - Utilities and Regex
message(STATUS "Configuring Boost...")
# Boost is huge, we'll configure it with minimal components
set(BOOST_INCLUDE_LIBRARIES "regex;system;filesystem" CACHE INTERNAL "")
set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)
add_subdirectory(third_party/boost EXCLUDE_FROM_ALL)
message(STATUS "  ✓ Boost configured")

# PCRE2 - Alternative regex library
message(STATUS "Configuring PCRE2...")
set(PCRE2_BUILD_TESTS OFF CACHE INTERNAL "")
set(PCRE2_BUILD_PCRE2GREP OFF CACHE INTERNAL "")
add_subdirectory(third_party/pcre2 EXCLUDE_FROM_ALL)
message(STATUS "  ✓ PCRE2 configured")

# pugixml - XML/HTML parsing
message(STATUS "Configuring pugixml...")
set(PUGIXML_BUILD_TESTS OFF CACHE INTERNAL "")
add_subdirectory(third_party/pugixml EXCLUDE_FROM_ALL)
message(STATUS "  ✓ pugixml configured")

# QuickJS - JavaScript interpreter
# QuickJS doesn't have CMakeLists.txt, we'll add it later when needed
message(STATUS "QuickJS...")
message(STATUS "  → Skipped (no CMakeLists.txt, will integrate manually later)")

# FFmpeg - External tool, not built
message(STATUS "FFmpeg...")
message(STATUS "  → External tool (will be called as process)")

# Catch2 - Testing framework
message(STATUS "Configuring Catch2...")
set(CATCH_INSTALL_DOCS OFF CACHE INTERNAL "")
set(CATCH_INSTALL_EXTRAS OFF CACHE INTERNAL "")
add_subdirectory(third_party/Catch2 EXCLUDE_FROM_ALL)
message(STATUS "  ✓ Catch2 configured")

message(STATUS "========================================")
message(STATUS "")

# ============================================================================
# Test Executable - Verify Library Linking
# ============================================================================

message(STATUS "")
message(STATUS "Creating library verification test...")

# Create a simple test to verify all libraries can be linked
add_executable(ytdlp-lib-test
    src/lib_test.cpp
)

# Link all configured libraries to verify they work
target_link_libraries(ytdlp-lib-test
    PRIVATE
        CURL::libcurl
        OpenSSL::SSL
        OpenSSL::Crypto
        nlohmann_json::nlohmann_json
        fmt::fmt
        spdlog::spdlog
        cxxopts::cxxopts
        pugixml
        Boost::regex
        Boost::system
        Boost::filesystem
        pcre2-8
)

message(STATUS "  ✓ Test executable configured")
message(STATUS "")

# ============================================================================
# Unit Tests
# ============================================================================

option(BUILD_TESTING "Build unit tests" ON)
if(BUILD_TESTING)
    message(STATUS "Configuring unit tests...")
    enable_testing()
    add_subdirectory(tests)
    message(STATUS "  ✓ Unit tests configured")
    message(STATUS "")
endif()

# ============================================================================
# Summary
# ============================================================================

message(STATUS "")
message(STATUS "========================================")
message(STATUS "  yt-dlp C++ Configuration Summary")
message(STATUS "========================================")
message(STATUS "  Version:        ${PROJECT_VERSION}")
message(STATUS "  Build type:     ${CMAKE_BUILD_TYPE}")
message(STATUS "  C++ Standard:   ${CMAKE_CXX_STANDARD}")
message(STATUS "  Compiler:       ${CMAKE_CXX_COMPILER_ID} ${CMAKE_CXX_COMPILER_VERSION}")
message(STATUS "")
message(STATUS "  Libraries Configured:")
message(STATUS "    ✓ CURL")
message(STATUS "    ✓ OpenSSL")
message(STATUS "    ✓ nlohmann/json")
message(STATUS "    ✓ fmt")
message(STATUS "    ✓ spdlog")
message(STATUS "    ✓ cxxopts")
message(STATUS "    ✓ Boost (regex, system, filesystem)")
message(STATUS "    ✓ PCRE2")
message(STATUS "    ✓ pugixml")
message(STATUS "    ✓ Catch2")
message(STATUS "")
message(STATUS "  Build Target:")
message(STATUS "    ytdlp-lib-test - Library verification test")
message(STATUS "")
message(STATUS "  Next: Run 'cmake --build .' to build")
message(STATUS "========================================")
message(STATUS "")
