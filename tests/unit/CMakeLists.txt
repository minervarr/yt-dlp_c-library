cmake_minimum_required(VERSION 3.15)

# ============================================================================
# String Utils Tests
# ============================================================================
add_executable(string_utils_test
    string_utils_test.cpp
)

target_link_libraries(string_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(string_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(string_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
)

catch_discover_tests(string_utils_test)

# ============================================================================
# Filesystem Utils Tests
# ============================================================================
add_executable(filesystem_utils_test
    filesystem_utils_test.cpp
)

target_link_libraries(filesystem_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(filesystem_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(filesystem_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
)

catch_discover_tests(filesystem_utils_test)

# ============================================================================
# JSON Utils Tests
# ============================================================================
add_executable(json_utils_test
    json_utils_test.cpp
)

target_link_libraries(json_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        nlohmann_json::nlohmann_json
        fmt::fmt
)

target_include_directories(json_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(json_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/json_utils.cpp
)

catch_discover_tests(json_utils_test)

# ============================================================================
# JS Interpreter Tests
# ============================================================================
add_executable(js_interpreter_test
    js_interpreter_test.cpp
)

target_link_libraries(js_interpreter_test
    PRIVATE
        Catch2::Catch2WithMain
        quickjs
        fmt::fmt
)

target_include_directories(js_interpreter_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(js_interpreter_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/js_interpreter.cpp
)

catch_discover_tests(js_interpreter_test)

# ============================================================================
# Parse Utils Tests
# ============================================================================
add_executable(parse_utils_test
    parse_utils_test.cpp
)

target_link_libraries(parse_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(parse_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(parse_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/parse_utils.cpp
)

catch_discover_tests(parse_utils_test)

# ============================================================================
# Date Utils Tests
# ============================================================================
add_executable(date_utils_test
    date_utils_test.cpp
)

target_link_libraries(date_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(date_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(date_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/date_utils.cpp
)

catch_discover_tests(date_utils_test)

# ============================================================================
# Encode Utils Tests
# ============================================================================
add_executable(encode_utils_test
    encode_utils_test.cpp
)

target_link_libraries(encode_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        OpenSSL::SSL
        OpenSSL::Crypto
)

target_include_directories(encode_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(encode_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/encode_utils.cpp
)

catch_discover_tests(encode_utils_test)

# ============================================================================
# Network Utils Tests
# ============================================================================
add_executable(network_utils_test
    network_utils_test.cpp
)

target_link_libraries(network_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(network_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(network_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(network_utils_test)

# ============================================================================
# Number Utils Tests
# ============================================================================
add_executable(number_utils_test
    number_utils_test.cpp
)

target_link_libraries(number_utils_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(number_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(number_utils_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/utils/number_utils.cpp
)

catch_discover_tests(number_utils_test)

# ============================================================================
# HTTP Header Dict Tests
# ============================================================================
add_executable(http_header_dict_test
    http_header_dict_test.cpp
)

target_link_libraries(http_header_dict_test
    PRIVATE
        Catch2::Catch2WithMain
)

target_include_directories(http_header_dict_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(http_header_dict_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
)

catch_discover_tests(http_header_dict_test)

# ============================================================================
# Request Tests
# ============================================================================
add_executable(request_test
    request_test.cpp
)

target_link_libraries(request_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(request_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(request_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
)

catch_discover_tests(request_test)

# ============================================================================
# Response Tests
# ============================================================================
add_executable(response_test
    response_test.cpp
)

target_link_libraries(response_test
    PRIVATE
        Catch2::Catch2WithMain
)

target_include_directories(response_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(response_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
)

catch_discover_tests(response_test)

# ============================================================================
# CurlHttpClient Tests
# ============================================================================
add_executable(curl_http_client_test
    curl_http_client_test.cpp
)

target_link_libraries(curl_http_client_test
    PRIVATE
        Catch2::Catch2WithMain
        CURL::libcurl
        fmt::fmt
)

target_include_directories(curl_http_client_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(curl_http_client_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
)

catch_discover_tests(curl_http_client_test)

# ============================================================================
# Cookie Tests
# ============================================================================
add_executable(cookie_test
    cookie_test.cpp
)

target_link_libraries(cookie_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(cookie_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(cookie_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
)

catch_discover_tests(cookie_test)

# ============================================================================
# CookieJar Tests
# ============================================================================
add_executable(cookie_jar_test
    cookie_jar_test.cpp
)

target_link_libraries(cookie_jar_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
)

target_include_directories(cookie_jar_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(cookie_jar_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
)

catch_discover_tests(cookie_jar_test)

# ============================================================================
# YoutubeDL Tests
# ============================================================================
add_executable(youtube_dl_test
    youtube_dl_test.cpp
)

target_link_libraries(youtube_dl_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        CURL::libcurl
)

target_include_directories(youtube_dl_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(youtube_dl_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(youtube_dl_test)

# ============================================================================
# InfoExtractor Tests
# ============================================================================
add_executable(info_extractor_test
    info_extractor_test.cpp
)

target_link_libraries(info_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        CURL::libcurl
)

target_include_directories(info_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(info_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(info_extractor_test)

# ============================================================================
# GenericExtractor Tests
# ============================================================================
add_executable(generic_extractor_test
    generic_extractor_test.cpp
)

target_link_libraries(generic_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        CURL::libcurl
)

target_include_directories(generic_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(generic_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/generic.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(generic_extractor_test)

# ============================================================================
# VimeoExtractor Tests
# ============================================================================
add_executable(vimeo_extractor_test
    vimeo_extractor_test.cpp
)

target_link_libraries(vimeo_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        CURL::libcurl
)

target_include_directories(vimeo_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(vimeo_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/vimeo.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(vimeo_extractor_test)

# ShareVideosEmbed Extractor Test
add_executable(share_videos_embed_extractor_test
    share_videos_embed_extractor_test.cpp
)

target_link_libraries(share_videos_embed_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(share_videos_embed_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(share_videos_embed_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/share_videos_embed.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(share_videos_embed_extractor_test)

# SibnetEmbed Extractor Test
add_executable(sibnet_embed_extractor_test
    sibnet_embed_extractor_test.cpp
)

target_link_libraries(sibnet_embed_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(sibnet_embed_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(sibnet_embed_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/sibnet_embed.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(sibnet_embed_extractor_test)

# Unity Extractor Test
add_executable(unity_extractor_test
    unity_extractor_test.cpp
)

target_link_libraries(unity_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(unity_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(unity_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/unity.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(unity_extractor_test)

# DailymotionBaseInfo Extractor Test
add_executable(dailymotion_base_info_extractor_test
    dailymotion_base_info_extractor_test.cpp
)

target_link_libraries(dailymotion_base_info_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(dailymotion_base_info_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(dailymotion_base_info_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/dailymotion_base_info.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(dailymotion_base_info_extractor_test)

# UFCTVIE Extractor Test
add_executable(ufctvie_extractor_test
    ufctvie_extractor_test.cpp
)

target_link_libraries(ufctvie_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(ufctvie_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(ufctvie_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/ufctvie.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(ufctvie_extractor_test)

# HrefLiRedirect Extractor Test
add_executable(href_li_redirect_extractor_test
    href_li_redirect_extractor_test.cpp
)

target_link_libraries(href_li_redirect_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(href_li_redirect_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(href_li_redirect_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/href_li_redirect.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(href_li_redirect_extractor_test)

# Formula1 Extractor Test
add_executable(formula1_extractor_test
    formula1_extractor_test.cpp
)

target_link_libraries(formula1_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(formula1_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(formula1_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/formula1.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(formula1_extractor_test)

# BeatBumpVideo Extractor Test
add_executable(beat_bump_video_extractor_test
    beat_bump_video_extractor_test.cpp
)

target_link_libraries(beat_bump_video_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(beat_bump_video_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(beat_bump_video_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/beat_bump_video.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(beat_bump_video_extractor_test)

# OutsideTVIE Extractor Test
add_executable(outside_tvie_extractor_test
    outside_tvie_extractor_test.cpp
)

target_link_libraries(outside_tvie_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(outside_tvie_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(outside_tvie_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/outside_tvie.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
)

catch_discover_tests(outside_tvie_extractor_test)

# YouTube Extractor Test
add_executable(youtube_extractor_test
    youtube_extractor_test.cpp
)

target_link_libraries(youtube_extractor_test
    PRIVATE
        Catch2::Catch2WithMain
        fmt::fmt
        nlohmann_json::nlohmann_json
        spdlog::spdlog
        CURL::libcurl
)

target_include_directories(youtube_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/include
)

target_sources(youtube_extractor_test
    PRIVATE
        ${CMAKE_SOURCE_DIR}/src/extractor/youtube.cpp
        ${CMAKE_SOURCE_DIR}/src/extractor/info_extractor.cpp
        ${CMAKE_SOURCE_DIR}/src/core/youtube_dl.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/cookie_jar.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/http_header_dict.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/request.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/response.cpp
        ${CMAKE_SOURCE_DIR}/src/networking/curl_http_client.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/string_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/filesystem_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/network_utils.cpp
        ${CMAKE_SOURCE_DIR}/src/utils/json_utils.cpp
)

catch_discover_tests(youtube_extractor_test)
