● Looking at the output, I see the issue! Our code is selecting format 313 (2160p) but timing out, while Python yt-dlp successfully downloads format
  401 (1.73GB). We need to fix our format selection logic to prefer formats that actually work.

  Let me investigate format 401 and update the format selection:

● Bash(yt-dlp --list-formats "https://www.youtube.com/watch?v=STBSUasnJ5s" 2>&1 | grep -E "^(ID|401|313)" | head -10) timeout: 20s
  ⎿  ID      EXT   RESOLUTION FPS CH |   FILESIZE    TBR PROTO | VCODEC           VBR ACODEC      ABR ASR MORE INFO
     313     webm  3840x2160   30    |    1.99GiB 13283k https | vp9           13283k video only          2160p, webm_dash
     401     mp4   3840x2160   30    |    1.00GiB  6696k https | av01.0.12M.08  6696k video only          2160p, mp4_dash

● FOUND IT!

  Both formats are 2160p, but:
  - Format 313 (VP9/webm): 1.99GB, 13283k bitrate → TIMES OUT ❌
  - Format 401 (AV1/mp4): 1.00GB, 6696k bitrate → WORKS ✅

  Python yt-dlp prefers format 401 because it's smaller and more reliable. Our code is picking 313. Let me fix the format selection logic:

● Read(/mnt/sharedDrive/code_programs/languages/C++/libraries/yt-dlp-c++/src/main.cpp)
  ⎿  Read 200 lines

