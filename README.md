# Sumika

[![Release](https://img.shields.io/github/v/release/tt7247129-sys/sumika?style=flat-square)](https://github.com/tt7247129-sys/sumika/releases)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-blue.svg?style=flat-square)](https://www.gnu.org/licenses/gpl-3.0.html)
[![Fork of Mihon](https://img.shields.io/badge/fork%20of-Mihon-orange.svg?style=flat-square)](https://github.com/mihonapp/mihon)
[![Languages](https://img.shields.io/badge/languages-50-green.svg?style=flat-square)](#-languages-50)
[![Privacy](https://img.shields.io/badge/tracking-none-success.svg?style=flat-square)](https://sumikareader.pages.dev/privacy)

**A free, open-source manga, comic and webtoon reader for Android.**
No ads. No in-app purchases. No subscription. No analytics SDK. No account. Your library never leaves your phone.

Website: [sumikareader.pages.dev](https://sumikareader.pages.dev) · Privacy policy: [sumikareader.pages.dev/privacy](https://sumikareader.pages.dev/privacy)

## Why Sumika exists

| | Commercial readers (MANGA Plus, K MANGA, Manga Bird…) | Sumika |
|---|---|---|
| Price | Pay per chapter / ticket / month | Free forever |
| Your downloads | Can be revoked when you stop paying | Plain files on your storage, you own them |
| Tracking SDKs | Common | **None** — auditable, see below |
| Account required | Usually yes | Never |
| Languages | 1–10 | 50, follows system language |
| Open source | No | Yes, GPL-3.0 |

**The Privacy Ledger** — Sumika's signature feature and our answer to opaque trackers. A built-in screen under *Settings → Privacy Ledger* lists every network host the app has contacted, with the count of requests and the last-seen timestamp. One tap exports the whole ledger as CSV so anyone can verify the app phones home to no one.

## Features

- **A real library** — categories, filters, sorting, bulk actions, reading history, and tracking sync with MyAnimeList, AniList, Kitsu, Shikimori and Bangumi.
- **A reader built for long sessions** — 5 reading modes, page & webtoon, right-to-left and vertical, double-page spreads, custom color filters, tap zones, volume-key paging.
- **Offline first** — download queue, storage budget cap, auto-eviction of finished chapters, everything in an open folder you can back up or move.
- **Migration without an account** — encrypted local backup, or phone-to-phone transfer over your own Wi-Fi.
- **50 languages** — full right-to-left mirroring for Arabic, Hebrew, Persian and Urdu.

## Languages (50)

العربية · български · català · čeština · dansk · Deutsch · Ελληνικά · English · español · فارسی · suomi · français · हिंदी · hrvatski · magyar · Indonesia · italiano · עברית · 日本語 · 한국어 · polski · português · română · русский · српски · svenska · ไทย · Türkçe · українська · Tiếng Việt · 简体中文 · 繁體中文 … and 18 more.

## Download

| Channel | Link |
|---|---|
| Official website (APK) | [sumikareader.pages.dev/download](https://sumikareader.pages.dev/download) |
| GitHub Releases | [github.com/tt7247129-sys/sumika/releases](https://github.com/tt7247129-sys/sumika/releases) |
| F-Droid | *submission in progress* |
| IzzyOnDroid | *submission in progress* |
| Google Play | *planned* |

Sumika is a reader, not a content store. It ships with **no bundled catalogue** — you bring your own sources or your own local files.

## Building from source

Requirements: JDK 17, Android SDK (compileSdk 37). Then:

```bash
./gradlew :app:assembleRelease -Pdist=github
```

## Credits & licensing

Sumika is a fork of [Mihon](https://github.com/mihonapp/mihon), which continues the lineage of [Tachiyomi](https://github.com/tachiyomiorg). All credit for the core engine goes to those teams.

This project is licensed under the **GNU GPL-3.0**. See [LICENSE](LICENSE) and [NOTICE](NOTICE) for upstream attributions.

## Contributing

Bug reports, translations and PRs are welcome. Please read the upstream [Mihon contributing guide](https://github.com/mihonapp/mihon/blob/main/.github/CONTRIBUTING.md) — most of it applies here.

## Tags

`android` `manga` `manga-reader` `open-source` `privacy` `offline` `webtoon` `comic` `kotlin` `jetpack-compose`
