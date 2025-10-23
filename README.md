# yt-dlp-cpp

A high-performance C++17 port of [yt-dlp](https://github.com/yt-dlp/yt-dlp), the feature-rich command-line video downloader.

## Project Status

**Current Phase:** Phase 1 - Foundation & Project Setup

This is an ambitious project to port yt-dlp from Python to C++ with the primary goal of performance optimization. The project is in early development.

See [PORTING_PLAN.md](PORTING_PLAN.md) for the complete development roadmap.

## Features (Planned)

- Download videos from 500+ websites (YouTube, Vimeo, Twitch, etc.)
- Multiple protocol support: HTTP, HLS, DASH, fragments
- Format selection and quality preferences
- Authentication and cookie support
- Post-processing with FFmpeg integration
- Parallel downloads for improved performance
- 2-5x faster CPU-bound operations compared to Python version

## Requirements

- C++17 compliant compiler
  - GCC 7+ (2017)
  - Clang 5+ (2017)
  - MSVC 2017+ (2017)
  - AppleClang 10+ (2018)
- CMake 3.15 or higher
- All dependencies are included in `third_party/` directory

## Dependencies

### Essential Libraries
- **libcurl** - HTTP/HTTPS networking
- **OpenSSL** - Cryptography and AES decryption
- **nlohmann/json** - Modern JSON parsing (header-only)
- **fmt** - Fast string formatting
- **spdlog** - Fast logging library
- **cxxopts** - CLI argument parsing (header-only)

### Optional Libraries
- **Boost** - Utilities and regex (better performance than std::regex)
- **PCRE2** - Alternative regex library
- **pugixml** - XML/HTML parsing
- **QuickJS** - JavaScript interpreter (for YouTube signature decryption)
- **Catch2** - Testing framework
- **FFmpeg** - Video/audio post-processing

See [LIBRARIES.md](LIBRARIES.md) for detailed dependency information.

## Building

### Quick Start

```bash
# Create build directory
mkdir build && cd build

# Configure
cmake ..

# Build
cmake --build .

# Run tests (optional)
ctest

# Install (optional)
sudo cmake --install .
```

### Build Options

```bash
# Debug build
cmake -DCMAKE_BUILD_TYPE=Debug ..

# Release build (default)
cmake -DCMAKE_BUILD_TYPE=Release ..

# Disable tests
cmake -DBUILD_TESTING=OFF ..

# Disable examples
cmake -DBUILD_EXAMPLES=OFF ..

# Enable documentation
cmake -DBUILD_DOCS=ON ..
```

### Build Types

- **Debug**: Full debug symbols, no optimization, all warnings enabled
- **Release**: Full optimization (-O3), no debug info
- **RelWithDebInfo**: Optimization with debug info
- **MinSizeRel**: Optimize for size

## Usage

Once implemented, the basic usage will be:

```bash
# Download a video
ytdlp "https://www.youtube.com/watch?v=dQw4w9WgXcQ"

# Download with specific format
ytdlp -f "bestvideo[height<=1080]+bestaudio" URL

# Extract audio only
ytdlp -x --audio-format mp3 URL

# Download playlist
ytdlp "https://www.youtube.com/playlist?list=..."
```

## Project Structure

```
yt-dlp-cpp/
├── CMakeLists.txt          # Main build configuration
├── README.md               # This file
├── PORTING_PLAN.md         # Detailed development roadmap
├── LIBRARIES.md            # Dependency documentation
├── include/
│   └── ytdlp/              # Public headers
│       ├── core/           # Core orchestration
│       ├── extractor/      # Site-specific extractors
│       ├── downloader/     # Protocol downloaders
│       ├── postprocessor/  # Post-processing
│       ├── networking/     # HTTP client, cookies
│       └── utils/          # Utility functions
├── src/                    # Implementation files (mirrors include/)
├── tests/
│   ├── unit/               # Unit tests
│   └── integration/        # Integration tests
├── examples/               # Example programs
├── docs/                   # Documentation
├── third_party/            # All dependencies
│   ├── curl/
│   ├── openssl/
│   ├── json/
│   ├── fmt/
│   ├── spdlog/
│   ├── cxxopts/
│   ├── boost/
│   ├── pcre2/
│   ├── pugixml/
│   ├── quickjs/
│   ├── Catch2/
│   └── FFmpeg/
└── reference/
    └── yt-dlp/             # Python yt-dlp source (reference)
```

## Development Roadmap

This project follows an 11-phase development plan spanning 12-24 months:

1. **Phase 1**: Foundation & Project Setup (Weeks 1-4) - **Current Phase**
2. **Phase 2**: Utility Layer (Weeks 5-8)
3. **Phase 3**: Networking Layer (Weeks 9-12)
4. **Phase 4**: Core YoutubeDL Class (Weeks 13-16)
5. **Phase 5**: Base Extractor (Weeks 17-20)
6. **Phase 6**: Downloaders (Weeks 21-24)
7. **Phase 7**: YouTube Extractor (Weeks 25-32)
8. **Phase 8**: Major Extractors (Weeks 33-40)
9. **Phase 9**: Post-processors (Weeks 41-44)
10. **Phase 10**: Remaining Extractors (Months 11-18)
11. **Phase 11**: Optimization & Polish (Months 19-24)

See [PORTING_PLAN.md](PORTING_PLAN.md) for detailed information about each phase.

## Architecture

Key design decisions:

- **C++17**: Modern features (optional, variant, filesystem, string_view)
- **Static typing**: Strongly-typed structs with optional fields
- **Exception handling**: Exceptions for rare errors, std::optional for expected failures
- **Zero-copy parsing**: Extensive use of std::string_view
- **Parallel execution**: Thread pool for concurrent operations
- **Modular design**: Plugin system for extractors and post-processors

See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) for detailed architecture documentation.

## Performance Goals

- **2-5x faster** than Python for CPU-bound tasks
- **10-30% faster** overall performance
- **<100MB memory** usage for typical downloads
- Efficient parallel fragment downloads
- Connection pooling and keep-alive

## Testing

The project uses Catch2 for unit and integration testing:

```bash
# Build and run all tests
mkdir build && cd build
cmake -DBUILD_TESTING=ON ..
cmake --build .
ctest

# Run tests with verbose output
ctest --output-on-failure

# Run specific test
./tests/unit/string_utils_test
```

Target: 80%+ code coverage

## Contributing

This project is in early development. Contributions are welcome!

### Development Setup

1. Clone the repository with all submodules
2. Install dependencies (all included in third_party/)
3. Build the project in Debug mode
4. Run tests before submitting changes
5. Follow C++ Core Guidelines

### Code Style

- Use `clang-format` for formatting
- Use `clang-tidy` for static analysis
- Follow C++17 best practices
- Write unit tests for new features
- Document public APIs

## License

This project aims to maintain compatibility with yt-dlp's licensing.

## Acknowledgments

- [yt-dlp](https://github.com/yt-dlp/yt-dlp) - The original Python project
- All the maintainers and contributors of yt-dlp
- The developers of the excellent C++ libraries used in this project

## References

- [yt-dlp Documentation](https://github.com/yt-dlp/yt-dlp#readme)
- [C++ Core Guidelines](https://isocpp.github.io/CppCoreGuidelines/)
- [Modern CMake](https://cliutils.gitlab.io/modern-cmake/)

## Contact

For questions, issues, or contributions, please open an issue on the project repository.

---

**Note**: This project is a complete rewrite from scratch and is not affiliated with the official yt-dlp project. It is an independent effort to provide a high-performance C++ alternative.
