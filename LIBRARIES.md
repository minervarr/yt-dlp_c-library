# Third-Party Libraries

This document provides detailed information about all dependencies used in the yt-dlp-cpp project.

## Overview

All dependencies are included in the `third_party/` directory, making the project self-contained and easy to build without external dependency management.

## Essential Libraries

### 1. libcurl - HTTP/HTTPS Networking

**Location:** `third_party/curl/`
**Purpose:** HTTP/HTTPS networking, SSL/TLS support, cookie handling
**License:** MIT/X derivate license
**Website:** https://curl.se/libcurl/

**Usage in yt-dlp-cpp:**
- All HTTP/HTTPS requests to video hosting websites
- Cookie management and persistence
- SSL/TLS certificate verification
- Proxy support (HTTP, SOCKS4/5)
- Connection reuse and keep-alive
- Redirect following
- Range requests for resumable downloads

**Key Features:**
- Multi-protocol support (HTTP, HTTPS, FTP, etc.)
- SSL backends (OpenSSL, mbedTLS, etc.)
- Asynchronous multi-handle interface for concurrent requests
- Cookie jar functionality
- Custom headers and request methods

---

### 2. OpenSSL - Cryptography

**Location:** `third_party/openssl/`
**Purpose:** Cryptographic operations, AES decryption, SSL/TLS
**License:** Apache License 2.0
**Website:** https://www.openssl.org/

**Usage in yt-dlp-cpp:**
- AES-128 decryption for encrypted HLS streams
- SSL/TLS for secure HTTPS connections (via libcurl)
- Hash functions (MD5, SHA-1, SHA-256) for integrity checks
- Random number generation
- Certificate verification

**Key Features:**
- Industry-standard cryptography library
- Hardware acceleration support (AES-NI)
- Extensive algorithm support
- FIPS 140-2 validation available

---

### 3. nlohmann/json - JSON Parsing

**Location:** `third_party/json/`
**Purpose:** Modern C++ JSON parsing and serialization
**License:** MIT
**Website:** https://github.com/nlohmann/json

**Usage in yt-dlp-cpp:**
- Parse JSON responses from video site APIs
- Parse video metadata and format information
- Parse player configuration
- Configuration file handling
- Export metadata to JSON files

**Key Features:**
- Header-only library (easy integration)
- Intuitive syntax (similar to Python's json module)
- STL-like interface
- Automatic type conversion
- JSON Pointer (RFC 6901) support
- JSON Patch (RFC 6902) support

**Example:**
```cpp
json video_info = json::parse(response_body);
std::string title = video_info["title"];
int duration = video_info["duration"];
```

---

### 4. fmt - String Formatting

**Location:** `third_party/fmt/`
**Purpose:** Fast, safe string formatting
**License:** MIT
**Website:** https://github.com/fmtlib/fmt

**Usage in yt-dlp-cpp:**
- URL construction and formatting
- Log message formatting
- Error message generation
- Template string processing
- Format specifiers for video quality, size, etc.

**Key Features:**
- Much faster than std::stringstream
- Python-like format string syntax
- Type-safe formatting
- Compile-time format string checking
- Will be std::format in C++20
- Small binary size

**Example:**
```cpp
std::string url = fmt::format("https://example.com/video?id={}&quality={}",
                               video_id, quality);
```

---

### 5. spdlog - Fast Logging

**Location:** `third_party/spdlog/`
**Purpose:** Fast, header-only logging library
**License:** MIT
**Website:** https://github.com/gabime/spdlog

**Usage in yt-dlp-cpp:**
- Debug logging during development
- Info/warning/error messages to user
- Verbose output mode
- Network request logging
- Performance profiling logs

**Key Features:**
- Very fast (lock-free, async logging)
- Multiple sinks (console, file, rotating files)
- Custom formatting patterns
- Severity levels (trace, debug, info, warn, error, critical)
- Thread-safe
- Built on top of fmt library

**Example:**
```cpp
spdlog::info("Downloading video: {}", title);
spdlog::debug("Format selected: {} ({})", format_id, quality);
spdlog::error("Failed to download: {}", error_msg);
```

---

### 6. cxxopts - CLI Argument Parsing

**Location:** `third_party/cxxopts/`
**Purpose:** Command-line option parsing
**License:** MIT
**Website:** https://github.com/jarro2783/cxxopts

**Usage in yt-dlp-cpp:**
- Parse command-line arguments (URLs, options, flags)
- Generate help text
- Option validation
- Support for yt-dlp's extensive CLI options

**Key Features:**
- Header-only library
- Lightweight and easy to use
- Automatic help generation
- Supports short and long options
- Type-safe option handling
- Default values and required options

**Example:**
```cpp
cxxopts::Options options("ytdlp", "A C++ video downloader");
options.add_options()
    ("u,url", "Video URL", cxxopts::value<std::string>())
    ("f,format", "Format selection", cxxopts::value<std::string>())
    ("o,output", "Output template", cxxopts::value<std::string>());
```

---

## Recommended Libraries

### 7. Boost - Utilities and Regex

**Location:** `third_party/boost/`
**Purpose:** General-purpose C++ utilities, high-performance regex
**License:** Boost Software License
**Website:** https://www.boost.org/

**Usage in yt-dlp-cpp:**
- **Boost.Regex:** Pattern matching (faster than std::regex)
- **Boost.Filesystem:** Cross-platform filesystem operations (C++17 alternative)
- **Boost.Algorithm:** String algorithms
- **Boost.Optional:** Optional values (C++17 alternative)

**Key Features:**
- Industry-standard C++ libraries
- Peer-reviewed and well-tested
- Extensive documentation
- Used by millions of projects

**Why Boost.Regex over std::regex:**
- std::regex is known to be slow in many implementations
- Boost.Regex is typically 2-10x faster
- Better error messages
- More features (recursive patterns, etc.)

---

### 8. PCRE2 - Perl Compatible Regular Expressions

**Location:** `third_party/pcre2/`
**Purpose:** Alternative high-performance regex library
**License:** BSD-like
**Website:** https://www.pcre.org/

**Usage in yt-dlp-cpp:**
- Alternative to Boost.Regex
- Pattern matching in HTML/JavaScript code
- URL pattern matching
- Format string parsing

**Key Features:**
- Very fast (comparable to Boost.Regex)
- Perl-compatible syntax
- JIT compilation for extreme performance
- Unicode support
- Widely used (Python, PHP, nginx use PCRE)

**Performance:**
- Often the fastest regex library available
- JIT compilation can make simple patterns 10x faster

---

### 9. pugixml - XML/HTML Parsing

**Location:** `third_party/pugixml/`
**Purpose:** Lightweight XML/HTML DOM parser
**License:** MIT
**Website:** https://pugixml.org/

**Usage in yt-dlp-cpp:**
- Parse HTML pages to extract video metadata
- Parse XML responses (DASH MPD files, RSS feeds)
- Extract Open Graph tags
- Parse JSON-LD embedded in HTML

**Key Features:**
- Fast and memory-efficient
- Simple and intuitive API
- XPath 1.0 support
- Header-only option available
- Tolerant HTML parsing
- Small footprint

**Example:**
```cpp
pugi::xml_document doc;
doc.load_string(html_content.c_str());
auto title_node = doc.select_node("//meta[@property='og:title']/@content");
std::string title = title_node.attribute().value();
```

---

### 10. QuickJS - JavaScript Interpreter

**Location:** `third_party/quickjs/`
**Purpose:** Small, embeddable JavaScript engine
**License:** MIT
**Website:** https://bellard.org/quickjs/

**Usage in yt-dlp-cpp:**
- **Critical for YouTube:** Decrypt signature parameters
- Execute JavaScript code from video pages
- Parse obfuscated JavaScript
- Run anti-bot challenge scripts

**Key Features:**
- Small and fast
- ES2020 support
- Easy to embed
- Low memory footprint
- Can run in sandboxed environment

**Why needed:**
YouTube and other sites use JavaScript to encrypt/obfuscate video URLs. The signature must be decrypted using JavaScript code extracted from the page.

**Alternative:** Port Python's `jsinterp.py` to pure C++ (more complex but no dependency)

---

### 11. Catch2 - Testing Framework

**Location:** `third_party/Catch2/`
**Purpose:** Modern C++ unit testing framework
**License:** BSL-1.0
**Website:** https://github.com/catchorg/Catch2

**Usage in yt-dlp-cpp:**
- Unit tests for all components
- Integration tests for extractors
- Regression tests
- Performance benchmarks

**Key Features:**
- Header-only (Catch2 v2) or compiled (Catch2 v3)
- BDD-style test writing
- Rich assertion macros
- Automatic test registration
- Integrated benchmarking
- JUnit XML output for CI

**Example:**
```cpp
TEST_CASE("URL encoding works correctly", "[utils]") {
    REQUIRE(url_encode("hello world") == "hello%20world");
    REQUIRE(url_encode("foo&bar=baz") == "foo%26bar%3Dbaz");
}
```

---

### 12. FFmpeg - Video/Audio Processing

**Location:** `third_party/FFmpeg/`
**Purpose:** Multimedia framework for post-processing
**License:** LGPL 2.1+ or GPL 2+ (depending on configuration)
**Website:** https://ffmpeg.org/

**Usage in yt-dlp-cpp:**
- Merge video and audio streams
- Convert video formats
- Extract audio from video
- Embed thumbnails and metadata
- Fix video/audio issues
- Re-encode if necessary

**Key Features:**
- Industry-standard multimedia framework
- Supports hundreds of formats and codecs
- Hardware acceleration (NVENC, QSV, etc.)
- Extensive filtering capabilities
- Cross-platform

**Note:** yt-dlp-cpp spawns FFmpeg as an external process, similar to Python yt-dlp.

---

## Library Comparison Matrix

| Library | Size | Type | Build Time | C++ Standard | Key Advantage |
|---------|------|------|------------|--------------|---------------|
| libcurl | Large | Compiled | Slow | C | Industry standard, feature-rich |
| OpenSSL | Very Large | Compiled | Very Slow | C | Industry standard, hardware accel |
| nlohmann/json | Small | Header-only | Fast | C++11 | Easy to use, STL-like |
| fmt | Medium | Header/Compiled | Fast | C++11 | Fast, safe, modern |
| spdlog | Small | Header-only | Fast | C++11 | Very fast, async logging |
| cxxopts | Tiny | Header-only | Instant | C++11 | Simple, lightweight |
| Boost | Huge | Mixed | Very Slow | C++11+ | Comprehensive, well-tested |
| PCRE2 | Medium | Compiled | Medium | C | Fastest regex, JIT support |
| pugixml | Small | Compiled | Fast | C++11 | Fast, simple API |
| QuickJS | Medium | Compiled | Medium | C99 | Small, embeddable, ES2020 |
| Catch2 | Medium | Header/Compiled | Fast | C++11 | Modern, easy to use |
| FFmpeg | Huge | External | N/A | C | Industry standard |

---

## Build Considerations

### Compile Times

**Fast builds (header-only or small):**
- nlohmann/json
- cxxopts
- spdlog
- fmt (mostly header-only)
- pugixml

**Slow builds (large, compiled):**
- Boost (especially if building all libraries)
- OpenSSL (very large, complex build system)
- libcurl (moderate)
- PCRE2 (moderate)
- QuickJS (moderate)

**Recommendation:** Use precompiled versions or system libraries for OpenSSL and libcurl in production builds.

### Binary Size

**Small footprint:**
- fmt, spdlog, cxxopts, pugixml, QuickJS

**Large footprint:**
- Boost (if using many components)
- OpenSSL (large library)
- libcurl (moderate)

**Recommendation:** Link statically only what you need. Most libraries support selective linking.

---

## Alternative Libraries (Not Used)

### Why not these alternatives?

| Alternative | Not Used Because |
|-------------|------------------|
| **cpp-httplib** | Less feature-rich than libcurl, limited production use |
| **Boost.Beast** | More complex API, requires Boost.Asio learning curve |
| **RapidJSON** | Less intuitive API than nlohmann/json |
| **Google Test** | Catch2 is simpler and more modern |
| **tinyxml2** | pugixml has better API and XPath support |
| **Duktape** | QuickJS is faster and more standards-compliant |
| **V8** | Too large and complex for our needs |

---

## Version Requirements

| Library | Minimum Version | Recommended Version |
|---------|----------------|---------------------|
| CMake | 3.15 | 3.20+ |
| GCC | 7.0 | 11+ |
| Clang | 5.0 | 14+ |
| MSVC | 2017 | 2022 |
| libcurl | 7.50 | 8.0+ |
| OpenSSL | 1.1.0 | 3.0+ |
| nlohmann/json | 3.0 | 3.11+ |
| fmt | 7.0 | 10.0+ |
| spdlog | 1.4 | 1.12+ |
| cxxopts | 2.2 | 3.0+ |
| Boost | 1.66 | 1.80+ |
| PCRE2 | 10.30 | 10.42+ |
| pugixml | 1.10 | 1.13+ |
| QuickJS | 2020-11-08 | Latest |
| Catch2 | 2.0 | 3.0+ |

---

## License Compatibility

All libraries used are permissively licensed (MIT, BSD, Apache, Boost) or LGPL (FFmpeg), making them suitable for both open-source and commercial use.

**Summary:**
- **MIT:** nlohmann/json, fmt, spdlog, cxxopts, pugixml, QuickJS
- **MIT/X:** libcurl
- **Apache 2.0:** OpenSSL
- **Boost License:** Boost
- **BSD:** PCRE2
- **BSL-1.0:** Catch2
- **LGPL/GPL:** FFmpeg (external process, not linked)

---

## Future Considerations

### Libraries to Potentially Add

1. **Brotli** - Compression support for modern web content
2. **zstd** - Fast compression for archives
3. **libarchive** - Extract videos from archives
4. **Protobuf** - Parse some binary formats
5. **Google Benchmark** - Performance benchmarking (more advanced than Catch2)

### System Library vs Bundled

For production deployments, consider using system-provided versions of:
- OpenSSL (security updates)
- libcurl (security updates)
- FFmpeg (system integration)

For development, the bundled versions are convenient.

---

## Updating Libraries

To update a library in `third_party/`:

1. Navigate to the library directory
2. If it's a git submodule: `git pull origin main`
3. If it's a copy: Download new version and replace
4. Test build: `cmake --build build`
5. Run tests: `ctest`
6. Update version in this document

---

## Summary

This project includes **12 essential third-party libraries** totaling approximately:
- **Source code:** ~500MB
- **Compiled libraries:** ~100-200MB
- **Build time (first build):** 30-60 minutes
- **Incremental builds:** <5 minutes

All libraries are mature, well-maintained, and widely used in production environments.
