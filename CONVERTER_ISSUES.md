# Python-to-C++ Converter Issues & Improvements

This document tracks issues found while using `tools/py2cpp_extractor.py` to port yt-dlp extractors from Python to C++.

## Issues Found

### 1. **`_VALID_URL = False` handling** ❌
- **Issue**: Converts literal `False` to regex pattern `R"(False)"`
- **Expected**: Recognize embed-only extractors and handle appropriately
- **Files affected**: `sharevideos.py`, `sibnet.py`
- **Workaround**: Manually replace with proper embed URL pattern

### 2. **`_EMBED_REGEX` not parsed** ❌
- **Issue**: Completely ignores `_EMBED_REGEX` attribute
- **Expected**: Convert embed regex patterns to C++ equivalents
- **Files affected**: `sharevideos.py`, `sibnet.py`
- **Workaround**: Manually extract and convert embed patterns

### 3. **Python named groups in regex** ❌
- **Issue**: Keeps Python syntax `(?P<name>...)` which C++ doesn't support
- **Expected**: Convert to standard C++ capture groups `(...)`
- **Files affected**: `unity.py`, most extractors with _VALID_URL
- **Workaround**: Manually convert `(?P<id>...)` → `(...)`

### 4. **Raw string literal quotes** ❌
- **Issue**: Generated code like `R"(...)")"` causes syntax errors
- **Expected**: Use custom delimiter like `R"delim(...)delim"` when needed
- **Files affected**: `unity.py` (data-video-id="...")
- **Workaround**: Manually change to custom delimiter

### 5. **Multiple classes per file** ⚠️
- **Issue**: Only converts first class in files with multiple extractors
- **Expected**: Detect and convert all extractor classes
- **Files affected**: `ufctv.py` (UFCTVIE, UFCArabiaIE)
- **Workaround**: Run converter multiple times or manually implement

### 6. **Template URL patterns** ❌
- **Issue**: Doesn't handle URL templates like `BaseIE._VALID_URL_TEMPL % ...`
- **Expected**: Resolve templates or leave clear TODO
- **Files affected**: `ufctv.py` (ImgGamingBaseIE template)
- **Workaround**: Manually resolve template

### 7. **`_WEBPAGE_TESTS` not recognized** ⚠️
- **Issue**: Only parses `_TESTS`, ignores `_WEBPAGE_TESTS`
- **Expected**: Parse both test formats
- **Files affected**: `sibnet.py`
- **Impact**: Test cases not generated

### 8. **Base class dependencies** ❌
- **Issue**: Doesn't check if parent class is implemented
- **Expected**: Warn about missing base classes or add to dependency list
- **Files affected**: `ufctv.py` (needs ImgGamingBaseIE)
- **Workaround**: Skip extractors with unimplemented base classes

### 9. **`_WORKING = False` flag** ℹ️
- **Issue**: Doesn't preserve or document this flag
- **Expected**: Add note in generated header/docs
- **Files affected**: `unity.py`
- **Workaround**: Manually add note to class documentation

## Conversion Success Rate

Based on 3 extractors tested:

- **Embed-only extractors** (sharevideos, sibnet): ~40% automated
  - Issues: #1, #2, #7
  - Manual work: Fix URL patterns, implement embed detection

- **Simple extractors** (unity): ~60% automated
  - Issues: #3, #4, #9
  - Manual work: Fix regex syntax, implement _real_extract logic

- **Complex extractors** (ufctv): ~20% automated
  - Issues: #5, #6, #8
  - Manual work: Resolve dependencies, handle templates, port additional classes

## Recommended Improvements

### High Priority
1. Convert `(?P<name>...)` → `(...)` in all regex patterns
2. Detect `_VALID_URL = False` and handle embed-only extractors
3. Parse and convert `_EMBED_REGEX` patterns
4. Use custom delimiters for raw strings with quotes

### Medium Priority
5. Support multiple classes per file
6. Detect and warn about missing base class dependencies
7. Parse `_WEBPAGE_TESTS` in addition to `_TESTS`

### Low Priority
8. Document `_WORKING` flag in generated comments
9. Resolve template URL patterns when possible
10. Generate more complete test cases from Python _TESTS

## Statistics

- **Files tested**: 3 extractors
- **Tests generated**: 12 test cases
- **All tests passing**: ✅ 222/222 (100%)
- **Manual fixes required per extractor**: 2-5 depending on complexity
