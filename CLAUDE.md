# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**yt-dlp-cpp** is a C++17 port of [yt-dlp](https://github.com/yt-dlp/yt-dlp), a Python video downloader supporting 500+ websites. This is a multi-year effort to port ~250,000 lines of Python code to C++ for performance optimization (2-5x faster CPU-bound operations).

**Current Status:** Phase 2 (Utility Layer) - String, filesystem, and JSON utilities implemented and tested
**C++ Standard:** C++17 (chosen for `std::optional`, `std::filesystem`, `std::string_view`, better debuggability)
**Estimated Timeline:** 12-24 months for complete port

## Build Commands

### Standard Build Workflow
```bash
# From project root
mkdir -p build && cd build
cmake ..
cmake --build .
```

### Running Tests
```bash
# All tests (from build directory)
ctest

# With verbose output
ctest --output-on-failure

# Run specific test executable
./tests/unit/string_utils_test
./tests/unit/filesystem_utils_test
./tests/unit/json_utils_test

# Run specific test case with Catch2
./tests/unit/string_utils_test "url_encode encodes special characters"
./tests/unit/filesystem_utils_test "[utils][filesystem]"
```

### Build Targets
```bash
# Build specific target
cmake --build . --target string_utils_test
cmake --build . --target filesystem_utils_test
cmake --build . --target json_utils_test

# Clean rebuild of specific target
cmake --build . --target filesystem_utils_test --clean-first

# Build library verification test
cmake --build . --target ytdlp-lib-test
```

### Build Configurations
```bash
# Debug build (recommended for development)
cmake -DCMAKE_BUILD_TYPE=Debug ..

# Release build
cmake -DCMAKE_BUILD_TYPE=Release ..

# Disable tests
cmake -DBUILD_TESTING=OFF ..
```

## Architecture

### Porting Strategy
This is a **gradual port following Python's architecture**, not a rewrite. The development follows an 11-phase plan (see `PORTING_PLAN.md`):

1. ✅ **Phase 1**: Foundation & Library Setup (Complete)
2. 🔄 **Phase 2**: Utility Layer (In Progress - string/filesystem/JSON complete)
3. ⏳ **Phase 3**: Networking Layer (HTTP client, cookies)
4. ⏳ **Phase 4**: Core YoutubeDL Class
5. ⏳ **Phase 5**: Base Extractor
6. ⏳ **Phase 6**: Downloaders (HTTP, HLS, DASH)
7. ⏳ **Phase 7**: YouTube Extractor
8-11. ⏳ Additional extractors and optimization

### Code Organization
```
include/ytdlp/        # Public API headers
├── core/             # Core orchestration (future)
├── extractor/        # Site-specific extractors (future)
├── downloader/       # Protocol downloaders (future)
├── networking/       # HTTP client, cookies (future)
└── utils/            # Utility functions (CURRENT)
    ├── string_utils.hpp     # String manipulation (URL encoding, HTML escaping, sanitization)
    ├── filesystem_utils.hpp # File/path operations (cross-platform)
    └── json_utils.hpp       # Safe JSON access wrappers

src/                  # Implementation (mirrors include/)
tests/unit/           # Catch2 unit tests
third_party/          # 12 vendored dependencies
```

### Namespace Structure
All code lives in `namespace ytdlp`. Submodules use nested namespaces:
- `ytdlp::utils` - Utility functions
- `ytdlp::core` - Core classes (future)
- `ytdlp::extractor` - Extractors (future)
- `ytdlp::downloader` - Downloaders (future)

### Key Design Decisions

**C++17 Features Used:**
- `std::optional<T>` for values that may not exist (better debuggability than raw pointers)
- `std::string_view` for zero-copy string operations (critical for parsing)
- `std::filesystem` for cross-platform path handling
- `std::variant<T...>` for type-safe unions (future use)

**Error Handling:**
- `std::optional` for expected failures (missing file, invalid JSON)
- Exceptions for rare/unrecoverable errors
- Return `false` for simple boolean operations

**String Handling:**
- Use `std::string_view` for function parameters (zero-copy)
- Return `std::string` from functions (RVO optimization)
- Extensive use of `fmt` library for formatting (safer than iostream)

## Critical Implementation Details

### Function Name Collisions with POSIX

**IMPORTANT:** When implementing utilities, be aware of POSIX function collisions.

Example issue encountered with `basename()`:
```cpp
// ❌ WRONG - auto deduces to POSIX basename() which returns char*
auto result = basename("/path/to/file.txt");

// ✅ CORRECT - explicit namespace or type
std::string result = ytdlp::utils::basename("/path/to/file.txt");
```

**Solution:** Always use explicit namespace qualification in tests when function names might collide with POSIX/system functions:
- `basename()` → use `ytdlp::utils::basename()`
- `dirname()` → use `ytdlp::utils::dirname()`

### String Utilities (`ytdlp::utils`)
40+ functions for string manipulation matching Python yt-dlp behavior:
- **URL encoding:** `url_encode()`, `url_decode()`, `compat_urllib_parse_unquote()`
- **HTML:** `escape_html()`, `unescape_html()`, `strip_html_tags()`
- **Filename sanitization:** `sanitize_filename()` with platform-specific rules
- **String operations:** `strip()`, `starts_with()`, `ends_with()`, `replace_all()`

### Filesystem Utilities (`ytdlp::utils`)
60+ functions for cross-platform file operations:
- **Path manipulation:** `join_path()`, `dirname()`, `basename()`, `expand_path()` (supports `~` and `$VAR`)
- **File I/O:** `read_file()`, `write_file()`, `read_binary_file()`
- **Platform utilities:** `get_home_directory()`, `get_temp_directory()`, `to_native_path()`
- **Special filenames:** `subtitles_filename()`, `temp_filename()`

### JSON Utilities (`ytdlp::utils`)
30+ functions wrapping `nlohmann/json` with Python-like API:
- **Safe access:** `get_string(j, "key", "default")`, `get_int()`, `get_bool()`, `get_array()`
- **Optional access:** `get_string_opt(j, "key")` returns `std::optional<std::string>`
- **Path navigation:** `get_at_path(j, "/user/address/city")` using JSON Pointer (RFC 6901)
- **Validation:** `has_required_keys()`, `validate_structure()`
- **Merging:** `merge()` (recursive), `update()` (shallow)

## Testing

### Test Framework
Uses **Catch2 v3** with BDD-style test organization:
```cpp
TEST_CASE("descriptive test name", "[tag1][tag2]") {
    SECTION("subsection description") {
        REQUIRE(actual == expected);
    }
}
```

### Test Coverage
Currently **56 tests, 100% passing**:
- `string_utils_test.cpp`: 24 tests (URL encoding, HTML escaping, filename sanitization)
- `filesystem_utils_test.cpp`: 19 tests (path operations, file I/O, platform utilities)
- `json_utils_test.cpp`: 18 tests (safe access, validation, merging)

### Writing New Tests
Follow the pattern in existing test files:
1. Use descriptive TEST_CASE names
2. Use SECTION for test variations
3. Test both success and failure cases
4. Include edge cases (empty strings, nullopt, missing keys)

## Dependencies

All 12 dependencies are vendored in `third_party/`:

### Essential Libraries
- **libcurl** - HTTP/HTTPS networking
- **OpenSSL** - Cryptography, AES decryption
- **nlohmann/json** - JSON parsing (header-only)
- **fmt** - Fast string formatting
- **spdlog** - Fast logging (built on fmt)
- **cxxopts** - CLI argument parsing (header-only)

### Optional Libraries
- **Boost** - Regex (faster than std::regex), utilities
- **PCRE2** - Alternative regex with JIT compilation
- **pugixml** - XML/HTML parsing
- **QuickJS** - JavaScript interpreter (for YouTube signature decryption)
- **Catch2** - Testing framework
- **FFmpeg** - Video post-processing (external process)

See `LIBRARIES.md` for detailed dependency documentation.

## Common Patterns

### Implementing a New Utility Function

1. **Add declaration** to `include/ytdlp/utils/XXX_utils.hpp`
2. **Add implementation** to `src/utils/XXX_utils.cpp`
3. **Add test** to `tests/unit/XXX_utils_test.cpp`
4. **Build and test:**
   ```bash
   cd build
   cmake --build . --target XXX_utils_test
   ./tests/unit/XXX_utils_test
   ctest
   ```

### Example: Adding a String Utility
```cpp
// include/ytdlp/utils/string_utils.hpp
namespace ytdlp::utils {
    std::string my_function(std::string_view input);  // Note: string_view param
}

// src/utils/string_utils.cpp
std::string my_function(std::string_view input) {
    std::string result;
    // implementation
    return result;  // RVO optimization
}

// tests/unit/string_utils_test.cpp
TEST_CASE("my_function does something", "[utils][string]") {
    REQUIRE(ytdlp::utils::my_function("input") == "expected");
}
```

## Build System Notes

### CMake Structure
- Main `CMakeLists.txt` configures all 12 libraries
- `tests/unit/CMakeLists.txt` defines test executables
- Each test links required dependencies (Catch2, fmt, etc.)
- Test sources include corresponding utility .cpp files directly

### Adding a New Test Executable
Edit `tests/unit/CMakeLists.txt`:
```cmake
add_executable(new_test new_test.cpp)
target_link_libraries(new_test PRIVATE Catch2::Catch2WithMain fmt::fmt)
target_include_directories(new_test PRIVATE ${CMAKE_SOURCE_DIR}/include)
target_sources(new_test PRIVATE ${CMAKE_SOURCE_DIR}/src/utils/new_utils.cpp)
catch_discover_tests(new_test)
```

### Library Configuration
Libraries are configured with minimal options to reduce build time:
- Tests disabled for third-party libraries (`BUILD_TESTING=OFF`)
- Only essential Boost components built (`regex;system;filesystem`)
- Static linking preferred (`BUILD_SHARED_LIBS=OFF`)

## Reference Materials

- **Python source:** `reference/yt-dlp/` (not yet populated in this build)
- **Porting plan:** `PORTING_PLAN.md` - Complete 11-phase roadmap
- **Architecture:** `ARCHITECTURE.md` - Design decisions and patterns
- **Dependencies:** `LIBRARIES.md` - Detailed library documentation

## Next Steps (Phase 2 Continuation)

The following utilities still need implementation to complete Phase 2:
1. **Network utilities** - User-agent strings, proxy handling
2. **Date/time utilities** - Parse various date formats
3. **Number utilities** - Parse duration strings, file sizes
4. **Data structure utilities** - Traverse nested dicts safely
5. **Encoding utilities** - Character encoding detection/conversion

After Phase 2, move to Phase 3 (Networking Layer) - HTTP client implementation.
