# Afaq Browser

**Fast. Lightweight. Wide compatibility.**

A minimal native Java browser shell for legacy Android devices, targeting Android 4.4 (API 19).

## MVP 0.1
- Native address bar and navigation controls
- System WebView
- URL/search handling
- JavaScript and DOM storage
- Page progress indicator and page title
- Back, forward, reload
- Lifecycle-aware pause and teardown

## Build
GitHub Actions builds a debug APK on pushes to main or manually via Actions. APK artifact is available from the workflow run.

- Minimum SDK: API 19 (Android 4.4)
- Target SDK: 28
- Compile SDK: 34

## Limitations
Android 4.4's WebView is obsolete and cannot reliably render many modern websites, including WhatsApp Web. No remote rendering service is included. JavaScript is enabled; browse trusted websites. This MVP has not been security-audited.

## Planned
Tabs, bookmarks, history, memory-pressure management, and a carefully designed optional remote-compatibility service.
