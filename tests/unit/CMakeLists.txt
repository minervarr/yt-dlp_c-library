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
