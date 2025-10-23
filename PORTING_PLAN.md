# yt-dlp Python to C++ Porting Plan

**Date:** 2025-10-23
**Target:** Full port of yt-dlp from Python to C++
**Primary Goal:** Performance optimization
**C++ Standard:** C++17 (recommended over C++14)

---

## Executive Summary

yt-dlp is a massive project with:
- **1,182 Python files** (~250,000 lines of code)
- **1,026 site extractors** (YouTube, Vimeo, Twitch, etc.)
- Complex systems: networking, cookies, authentication, streaming protocols, post-processing, JavaScript interpretation

**Estimated effort:** 5,000-10,000 hours for a complete port by experienced developers
**Timeline:** 12-24 months with dedicated team

---

## C++ Version Recommendation: C++17 (NOT C++14)

### Why C++17 is Superior

#### Performance Benefits
- **`std::string_view`** - Zero-copy string operations (critical for parsing thousands of HTML pages)
- **Parallel STL algorithms** - Built-in multi-threading for batch operations
- **`if constexpr`** - Compile-time optimizations, eliminates runtime branches
- **Better move semantics** - More efficient resource management
- **Structured bindings** - Cleaner code with less overhead

#### Debuggability (Your Key Requirement)
- **`std::optional<T>`** - Debugger clearly shows "has_value" vs raw pointers (nullptr = bug or intentional?)
- **`std::variant<T...>`** - Debugger shows which type is active (safer than unions)
- **`std::filesystem::path`** - Better debug representations than C strings
- **Structured bindings** - Variables have meaningful names in stack traces
  ```cpp
  // C++17: Clear in debugger
  auto [title, url, duration] = video_info;

  // C++14: Need to inspect tuple indices
  auto info = std::make_tuple(title, url, duration);
  ```
- **Better compiler error messages** - Modern compilers give clearer C++17 diagnostics

#### Critical Features You'll Need
- **`std::filesystem`** - yt-dlp does extensive file/path operations (300+ filesystem calls)
- **`std::optional`** - Handle missing video formats, metadata, optional fields
- **`std::variant`** - Represent different video format types safely
- **`std::any`** - Dynamic configuration values (mirrors Python's dynamic typing)
- **`std::string_view`** - Parse JSON, HTML, regex matches without copying

#### C++14 Pain Points You'd Face
- ❌ No filesystem library (need boost or manual platform-specific code)
- ❌ No `optional` (forced to use pointers = harder to debug, more nullptrs)
- ❌ No `variant` (use unions = unsafe, hard to debug, no type safety)
- ❌ Verbose template metaprogramming code
- ❌ More boilerplate code overall

#### Compiler Support (All Mature)
- GCC 7+ (2017)
- Clang 5+ (2017)
- MSVC 2017+ (2017)
- AppleClang 10+ (2018)

**Verdict:** C++17 is the clear choice. It's mature, widely supported, and provides significant advantages for this project.

---

## Project Architecture Overview

### Current Python Structure
```
yt_dlp/
├── YoutubeDL.py          # Core orchestration class (4,441 lines)
├── options.py            # CLI options parsing (1,962 lines)
├── __init__.py           # Entry point (1,090 lines)
├── extractor/            # 1,026 site-specific extractors
│   ├── common.py         # Base InfoExtractor class
│   ├── youtube.py        # Largest extractor (~15k lines)
│   └── ...               # 1,025 other extractors
├── downloader/           # Protocol downloaders
│   ├── http.py           # HTTP/HTTPS
│   ├── hls.py            # HLS streaming
│   ├── dash.py           # DASH streaming
│   ├── fragment.py       # Fragment-based protocols
│   └── ...
├── postprocessor/        # Post-download processing
│   ├── ffmpeg.py         # FFmpeg integration
│   ├── embedthumbnail.py
│   └── ...
├── networking/           # HTTP client, request handling
├── utils/                # Utility functions
├── cookies.py            # Cookie management (1,700 lines)
├── jsinterp.py           # JavaScript interpreter (1,200 lines)
└── aes.py                # AES decryption
```

### Proposed C++ Structure
```
yt-dlp-cpp/
├── CMakeLists.txt
├── include/
│   └── ytdlp/
│       ├── core/
│       │   ├── youtube_dl.hpp
│       │   ├── options.hpp
│       │   └── info_dict.hpp
│       ├── extractor/
│       │   ├── info_extractor.hpp
│       │   ├── youtube_extractor.hpp
│       │   └── ...
│       ├── downloader/
│       │   ├── downloader_base.hpp
│       │   ├── http_downloader.hpp
│       │   └── ...
│       ├── postprocessor/
│       │   └── ffmpeg_pp.hpp
│       ├── networking/
│       │   ├── http_client.hpp
│       │   └── request.hpp
│       └── utils/
│           ├── string_utils.hpp
│           ├── filesystem_utils.hpp
│           └── json_utils.hpp
├── src/
│   └── [mirrors include structure]
├── tests/
│   └── [unit tests with Catch2/GoogleTest]
├── examples/
│   └── simple_download.cpp
└── third_party/
    └── [dependencies via conan/vcpkg]
```

---

## Core Dependencies

### Essential Libraries
1. **libcurl** - HTTP/HTTPS networking, SSL/TLS support
2. **OpenSSL** - Cryptography, AES decryption for encrypted streams
3. **nlohmann/json** - Modern JSON parsing (header-only)
4. **fmt** - Fast string formatting (will be std::format in C++20)
5. **spdlog** - Fast logging library (built on fmt)
6. **cxxopts** - CLI argument parsing (header-only)

### Optional but Recommended
7. **Boost.Regex** or **PCRE2** - Regex performance (std::regex is slow)
8. **Boost.Beast** - HTTP if you want more control than libcurl
9. **cpp-httplib** - Simple HTTP client alternative (header-only)
10. **pugixml** or **tinyxml2** - XML/HTML parsing
11. **QuickJS** or **Duktape** - JavaScript interpreter (for YouTube signature decryption)

### Development/Testing
12. **Catch2** or **GoogleTest** - Unit testing
13. **Google Benchmark** - Performance benchmarking
14. **Valgrind** / **AddressSanitizer** - Memory leak detection
15. **clang-tidy** - Static analysis

---

## Phased Development Plan

### Phase 1: Foundation & Project Setup (Weeks 1-4)

**Goals:**
- Establish build system
- Set up dependency management
- Create project skeleton
- CI/CD pipeline

**Tasks:**
1. Create CMakeLists.txt with C++17 standard
2. Set up conan or vcpkg for dependency management
3. Integrate testing framework (Catch2 recommended)
4. Set up CI (GitHub Actions or GitLab CI)
5. Create core directory structure
6. Write project documentation (README, CONTRIBUTING)
7. Set up code formatting (clang-format) and linting (clang-tidy)

**Deliverables:**
- Buildable project skeleton
- Basic test infrastructure
- CI pipeline
- Developer documentation

---

### Phase 2: Utility Layer (Weeks 5-8)

**Goals:**
- Port utility functions (foundation for everything else)
- Establish coding patterns and conventions

**Port from `utils/`:**
1. **String utilities**
   - URL encoding/decoding
   - HTML unescaping
   - Sanitization functions
   - Format string helpers

2. **Filesystem utilities**
   - Path sanitization
   - Directory creation
   - File locking
   - Temp file management

3. **Parsing utilities**
   - Date/time parsing
   - Duration parsing
   - Filesize parsing
   - Number extraction

4. **Data structures**
   - OrderedSet
   - LazyList
   - Namespace (config dict)

**Challenges:**
- Python's dynamic typing vs C++ static typing
- Decide on error handling strategy (exceptions vs std::expected)

**Deliverables:**
- Comprehensive utility library with 90%+ test coverage
- Coding standards document
- Error handling guidelines

---

### Phase 3: Networking Layer (Weeks 9-12)

**Goals:**
- HTTP client with retry logic, redirects, cookies
- Request/Response abstractions

**Port from `networking/`:**
1. **Request class** - URL, headers, data, timeout
2. **Response class** - Status, headers, body, redirects
3. **HTTP client** - Built on libcurl
4. **Cookie management** - Load from browser, netrc support
5. **Proxy support** - HTTP, SOCKS4/5
6. **Impersonation** - Browser fingerprinting (curl-impersonate)

**Port from `cookies.py`:**
- Chrome/Firefox/Safari cookie extraction
- Cookie jar serialization

**Challenges:**
- libcurl C API wrapping in modern C++
- Thread-safe cookie jar
- SSL certificate handling

**Deliverables:**
- HTTP client library
- Cookie management system
- Integration tests with real websites

---

### Phase 4: Core YoutubeDL Class (Weeks 13-16)

**Goals:**
- Main orchestration class
- Configuration management
- Plugin system

**Port from `YoutubeDL.py` (4,441 lines):**
1. **Configuration**
   - Options structure
   - Default values
   - Validation

2. **Plugin system**
   - Extractor registration
   - Lazy loading
   - Override mechanism

3. **Core methods**
   - `extract_info()` - Main entry point
   - `process_ie_result()` - Handle extractor results
   - `process_video_result()` - Handle video info
   - `download()` - Orchestrate download

4. **Progress reporting**
   - Download progress hooks
   - Status messages
   - Error reporting

**Port from `options.py`:**
- CLI option definitions
- Option parsing with cxxopts

**Challenges:**
- Python's duck typing vs C++ interfaces
- Complex state management
- Callback/hook system design

**Deliverables:**
- Core YoutubeDL class with basic functionality
- Options system
- Plugin loading mechanism

---

### Phase 5: Base Extractor (Weeks 17-20)

**Goals:**
- InfoExtractor base class
- Common extraction patterns
- Regex/JSON/HTML helpers

**Port from `extractor/common.py`:**
1. **InfoExtractor base class**
   - `extract()` method
   - `_download_webpage()`
   - `_search_regex()`
   - `_json_ld()` - JSON-LD metadata
   - `_og_search()` - Open Graph tags

2. **Helper methods** (100+ utility methods)
   - HTML parsing
   - Regex extraction
   - Format extraction
   - Subtitle extraction
   - Playlist handling

3. **Format handling**
   - Format sorting
   - Quality selection
   - Protocol detection

**Challenges:**
- Regex performance (Python re vs C++ std::regex)
- HTML parsing (need library like pugixml)
- JavaScript execution for anti-bot measures

**Deliverables:**
- InfoExtractor base class
- Extraction helper library
- Generic extractor (handles common video embeds)

---

### Phase 6: Downloaders (Weeks 21-24)

**Goals:**
- Download protocols
- Resume capability
- Progress tracking

**Port from `downloader/`:**
1. **FileDownloader base** (`common.py`)
   - Progress reporting
   - Rate limiting
   - Resume support

2. **HttpFD** (`http.py`)
   - Range requests
   - Chunked download
   - Connection retry

3. **FragmentFD** (`fragment.py`)
   - Multi-fragment handling
   - Concurrent downloads
   - Assembly

4. **HlsFD** (`hls.py`)
   - M3U8 parsing
   - AES-128 decryption
   - Byte range support

5. **DashFD** (`dash.py`)
   - MPD parsing
   - Segment download

6. **ExternalFD** (`external.py`)
   - ffmpeg spawning
   - aria2c integration
   - Progress parsing

**Challenges:**
- Multi-threaded fragment downloads
- Efficient buffer management
- AES decryption performance
- Process spawning and IPC

**Deliverables:**
- Complete downloader subsystem
- Download resume functionality
- Performance benchmarks vs Python version

---

### Phase 7: YouTube Extractor (Weeks 25-32)

**Goals:**
- Most complex and important extractor
- JavaScript signature decryption
- Proof of concept for other extractors

**Port from `extractor/youtube.py` (~15,000 lines):**
1. **YoutubeBaseInfoExtractor**
   - Authentication
   - API handling
   - Player config extraction

2. **YoutubeIE** (single video)
   - Format extraction
   - Signature decryption
   - Adaptive formats
   - Live streams

3. **JavaScript interpreter** (`jsinterp.py`)
   - Parse JS functions
   - Execute signature decryption
   - Handle obfuscation

4. **Other YouTube extractors**
   - YoutubePlaylistIE
   - YoutubeChannelIE
   - YoutubeSearchIE
   - YoutubeLiveIE

**Challenges:**
- YouTube's anti-bot measures
- JavaScript execution (need JS engine or pure C++ interpreter)
- Frequent YouTube changes (need maintainability)
- OAuth authentication flow

**Deliverables:**
- Working YouTube extractor
- JS interpreter for signature decryption
- Comprehensive tests (YouTube changes frequently)

---

### Phase 8: Major Extractors (Weeks 33-40)

**Goals:**
- Port 10-20 popular extractors
- Refine extractor plugin system

**Priority extractors by popularity:**
1. Generic (handles many sites)
2. Vimeo
3. Twitch
4. Twitter/X
5. Facebook
6. Instagram
7. TikTok
8. Reddit
9. Dailymotion
10. SoundCloud
11. Pornhub (adult content, but high traffic)
12. BBC iPlayer
13. Crunchyroll

**Deliverables:**
- 15+ working extractors
- Extractor testing framework
- Performance comparison vs Python

---

### Phase 9: Post-processors (Weeks 41-44)

**Goals:**
- FFmpeg integration
- Metadata embedding
- Post-download processing

**Port from `postprocessor/`:**
1. **FFmpegPostProcessor** (`ffmpeg.py`)
   - FFmpeg detection
   - Process spawning
   - Progress parsing

2. **FFmpegMergerPP**
   - Merge video + audio

3. **FFmpegVideoConvertorPP**
   - Format conversion

4. **FFmpegFixup*** (various fixups)
   - Duration, timestamp, stretched video fixes

5. **EmbedThumbnailPP**
   - Thumbnail embedding

6. **SponsorBlockPP**
   - SponsorBlock API integration
   - Chapter modification

**Challenges:**
- FFmpeg process management
- Cross-platform FFmpeg location
- Binary output parsing

**Deliverables:**
- Post-processor subsystem
- FFmpeg integration
- Metadata handling

---

### Phase 10: Remaining Extractors (Months 11-18)

**Goals:**
- Port remaining ~1,000 extractors
- Automate where possible

**Strategy:**
1. Group extractors by similarity
2. Create templates/generators for common patterns
3. Port in batches of 50-100
4. Automate testing with known URLs
5. Community contribution system

**Challenges:**
- Massive amount of code
- Many extractors may be broken/outdated
- Need to decide what to port vs deprecate

**Deliverables:**
- 500+ extractors (prioritize by usage)
- Deprecation list for unused extractors
- Extractor development guide

---

### Phase 11: Optimization & Polish (Months 19-24)

**Goals:**
- Performance optimization
- Memory leak detection
- Production hardening

**Tasks:**
1. **Performance profiling**
   - Perf/gprof analysis
   - Identify hotspots
   - Optimize critical paths

2. **Memory optimization**
   - Valgrind analysis
   - AddressSanitizer
   - Fix leaks

3. **Concurrency**
   - Parallel downloads
   - Thread pool for extractors
   - Lock-free data structures

4. **Testing**
   - 80%+ code coverage
   - Integration tests
   - Regression tests

5. **Documentation**
   - API documentation (Doxygen)
   - User guide
   - Developer guide

6. **Packaging**
   - Static/dynamic libraries
   - CLI binary
   - Package managers (apt, brew, vcpkg)

**Deliverables:**
- Production-ready library
- Comprehensive documentation
- Performance benchmarks
- Binary distributions

---

## Technical Challenges & Solutions

### 1. Python's Dynamic Typing → C++ Static Typing

**Challenge:** Python code like:
```python
info = {
    'title': 'Video',
    'formats': [...],
    'duration': 120
}
```

**Solution:** Use strongly-typed structs with optional fields:
```cpp
struct InfoDict {
    std::string title;
    std::vector<Format> formats;
    std::optional<int> duration;
    std::optional<std::string> description;
    // ... 50+ fields
};
```

Alternative: Use `std::unordered_map<std::string, std::any>` for maximum flexibility (less type-safe, harder to debug).

### 2. Regex Performance

**Challenge:** Python's `re` module is fast (C-based). C++ `std::regex` is notoriously slow.

**Solution:** Use Boost.Regex or PCRE2 for better performance. Profile to confirm.

### 3. JavaScript Execution

**Challenge:** YouTube (and others) require JavaScript execution for signature decryption.

**Solutions:**
- **Option A:** Embed JS engine (QuickJS, Duktape) - adds dependency
- **Option B:** Port `jsinterp.py` to C++ - complex but no dependency
- **Option C:** Call external Node.js - slow but simple

Recommendation: Start with Option B (pure C++), fall back to Option A if too complex.

### 4. Error Handling Strategy

**Challenge:** Python uses exceptions heavily. C++ can use exceptions or error codes.

**Options:**
- **Exceptions:** Simpler code, but performance overhead
- **`std::expected<T, Error>`** (C++23, or use library) - Zero-overhead error handling
- **Error codes + std::optional** - Manual but explicit

**Recommendation:** Use exceptions for rare errors, `std::optional` for expected failures (e.g., missing metadata).

### 5. Maintainability

**Challenge:** Websites change frequently, breaking extractors.

**Solution:**
- Comprehensive test suite with real URLs
- CI that runs tests daily
- Easy extractor update process
- Community contribution system

---

## Performance Optimization Strategies

### 1. Zero-Copy Parsing
- Use `std::string_view` for parsing
- Avoid string copies during JSON/HTML parsing
- Memory-mapped file I/O for large files

### 2. Parallel Execution
- Download fragments concurrently
- Parse multiple extractors in parallel (playlist)
- Use thread pool (not thread-per-task)

### 3. Connection Pooling
- Reuse HTTP connections (libcurl multi handle)
- DNS caching
- Keep-alive connections

### 4. Smart Caching
- Cache webpage responses (short TTL)
- Cache regex compilation
- Cache JSON parsing results

### 5. Profiling Points
- HTTP request time
- Regex matching time
- JSON parsing time
- Format sorting time
- Disk I/O time

**Target:** 2-5x faster than Python version for CPU-bound tasks, 10-30% faster overall.

---

## Testing Strategy

### Unit Tests
- Every utility function
- Every extractor method
- Mocked HTTP responses
- Target: 80%+ coverage

### Integration Tests
- Real website downloads (may be flaky)
- Format selection logic
- End-to-end download pipeline
- Use VCR-style HTTP recording

### Performance Tests
- Benchmark vs Python version
- Memory usage comparison
- Concurrency scaling
- Regression detection

### Continuous Testing
- Run tests on every commit
- Daily tests against real websites
- Notify on extractor breakage

---

## Risk Assessment

### High Risks
1. **Scope creep** - Project is enormous, easy to underestimate
2. **Website changes** - Extractors break frequently, need maintenance
3. **JavaScript execution** - Complex to replicate Python's jsinterp
4. **Team availability** - Requires sustained effort over 12-24 months

### Medium Risks
1. **Dependency management** - C++ dependencies can be painful
2. **Cross-platform issues** - Windows/Linux/macOS differences
3. **Performance expectations** - May not be 10x faster as hoped
4. **Community adoption** - Python version is well-established

### Mitigation Strategies
- Start small (MVP with YouTube only)
- Regular milestones and demos
- Automated testing from day 1
- Clear documentation
- Open source early, get community feedback

---

## Success Metrics

### Phase-by-Phase
- **Phase 1-2:** Build system works, utility tests pass
- **Phase 3:** Can make HTTP requests and handle cookies
- **Phase 4:** Can load and configure extractors
- **Phase 5-6:** Can download simple videos
- **Phase 7:** YouTube downloading works
- **Phase 8:** 15+ sites supported
- **Phase 9-10:** Feature parity with Python version
- **Phase 11:** Performance improvements demonstrated

### Final Success Criteria
- ✅ Downloads from 500+ sites
- ✅ 2-5x faster than Python for CPU-bound tasks
- ✅ Memory usage < 100MB for typical downloads
- ✅ 80%+ test coverage
- ✅ Production-ready stability
- ✅ Comprehensive documentation

---

## Getting Started (First Steps)

### Week 1 Tasks
1. Create `CMakeLists.txt`:
   ```cmake
   cmake_minimum_required(VERSION 3.15)
   project(ytdlp-cpp VERSION 0.1.0 LANGUAGES CXX)

   set(CMAKE_CXX_STANDARD 17)
   set(CMAKE_CXX_STANDARD_REQUIRED ON)
   set(CMAKE_CXX_EXTENSIONS OFF)

   # Dependencies
   find_package(CURL REQUIRED)
   find_package(OpenSSL REQUIRED)
   # ... more dependencies

   # Library
   add_library(ytdlp
       src/utils/string_utils.cpp
       # ... more sources
   )

   # Executable
   add_executable(ytdlp-cli
       src/main.cpp
   )
   target_link_libraries(ytdlp-cli PRIVATE ytdlp)

   # Tests
   enable_testing()
   add_subdirectory(tests)
   ```

2. Set up directory structure:
   ```bash
   mkdir -p include/ytdlp/{core,extractor,downloader,postprocessor,networking,utils}
   mkdir -p src/{core,extractor,downloader,postprocessor,networking,utils}
   mkdir -p tests/{unit,integration}
   mkdir -p examples
   ```

3. Create first utility function:
   ```cpp
   // include/ytdlp/utils/string_utils.hpp
   #pragma once
   #include <string>
   #include <string_view>

   namespace ytdlp::utils {
       std::string url_encode(std::string_view str);
       std::string url_decode(std::string_view str);
       std::string sanitize_filename(std::string_view filename);
   }
   ```

4. Write first test:
   ```cpp
   // tests/unit/string_utils_test.cpp
   #include <catch2/catch_test_macros.hpp>
   #include "ytdlp/utils/string_utils.hpp"

   TEST_CASE("url_encode works", "[utils]") {
       REQUIRE(ytdlp::utils::url_encode("hello world") == "hello%20world");
   }
   ```

5. Document decisions in `docs/ARCHITECTURE.md`

---

## Resources & References

### C++ Best Practices
- [C++ Core Guidelines](https://isocpp.github.io/CppCoreGuidelines/)
- [Modern CMake](https://cliutils.gitlab.io/modern-cmake/)
- [Awesome Modern C++](https://github.com/rigtorp/awesome-modern-cpp)

### Similar Projects
- [youtube-dl C port](https://github.com/search?q=youtube-dl+c%2B%2B) (various abandoned attempts)
- [annie](https://github.com/iawia002/annie) (Go, similar project)

### Python yt-dlp Resources
- [yt-dlp GitHub](https://github.com/yt-dlp/yt-dlp)
- [yt-dlp Documentation](https://github.com/yt-dlp/yt-dlp#readme)
- [Developer Guide](https://github.com/yt-dlp/yt-dlp/blob/master/CONTRIBUTING.md)

---

## Conclusion

This is an **ambitious, multi-year project**. Success requires:
- Realistic timeline expectations (12-24 months minimum)
- Sustained development effort
- Strong C++ expertise
- Commitment to testing and maintenance
- Community building

**Recommendation:** Start with an MVP (YouTube-only) to validate the approach, then gradually expand. This allows you to:
- Prove performance gains early
- Refine architecture before scaling
- Get community feedback
- Maintain motivation with working product

**Next Step:** Execute Phase 1 (Foundation) and Phase 2 (Utilities) to establish solid groundwork before tackling complex extractors.

Good luck! This will be a challenging but rewarding project. 🚀
