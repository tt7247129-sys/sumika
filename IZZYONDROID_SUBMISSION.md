# IzzyOnDroid App Inclusion Request — Sumika

> Destination: https://codeberg.org/IzzyOnDroid/repodata/issues/new
> Template: "App Inclusion Request" (labels: `app-request`, `needs/llm-check`)
> Suggested title: `[AppRequest] Sumika`

**IMPORTANT — read before submitting:** the template requires a mandatory
"Assistance Level" answer about AI tooling. IzzyOnDroid policy rejects apps
whose code is predominantly AI-generated ("Dominant"), and defines "Vibe-coded
apps will be rejected". This draft deliberately leaves that one field for the
human developer to answer truthfully. Do not submit with a false answer.

---

## Guidelines

- [x] I am the developer of the app.
- [x] The app complies with the App Inclusion Policy.
      - GPL-3.0-or-later, source public.
      - No trackers: telemetry is hard-disabled at the build level (`BuildConfig.kt`).
      - No self-updater in the FOSS build (removed deliberately — see below).
      - No proprietary components in the FOSS build (`-Pdist=foss` removes ML Kit).
- [x] The app is not already listed in the repo or issue tracker.
- [x] The Fastlane folder is available in the app's repo
      (`fastlane/metadata/android/`, 14 locales, icon + 4 screenshots each).

## App meta-data

**Link to the source code**
https://github.com/tt7247129-sys/sumika

**Link to app in another app store**
Not listed yet. Official download page: https://sumikareader.pages.dev/download

**License used**
GPL-3.0-or-later

**Categories**
Reading, Internet

**Summary**
A free, open-source manga, comic and webtoon reader for Android. No ads, no
tracking, no account — with a built-in Privacy Ledger that lists every network
host the app contacts.

**Description**
Sumika is a reader, not a content store. It ships with no catalogue and no
bundled sources: you bring your own community sources or your own local CBZ/ZIP
files. It supports RTL/LTR, continuous webtoon and paged reading, offline-first
chapter downloads with a storage budget, encrypted backups, phone-to-phone
migration, 70+ UI languages with full RTL support, and an optional parental
control mode.

The Privacy Ledger records every domain the app contacts — with request counts
and timestamps, exportable to CSV — so the "no tracking" claim can be audited
rather than trusted. In the FOSS build, telemetry is disabled at build time and
cannot be re-enabled, and the in-app self-updater is removed so that the APK
you install is the APK that was reviewed.

Sumika is a fork of Mihon (which continues Tachiyomi). Upstream attribution is
in `NOTICE` and the README, and the app name, icon and descriptions are distinct
from upstream.

## Technical instructions

**Build instructions**

```bash
git clone https://github.com/tt7247129-sys/sumika.git
cd sumika
export JAVA_HOME=/path/to/jdk-21   # .github/.java-version = 21
./gradlew :app:assembleFoss -Pdist=foss
```

Requirements: JDK 21 (the Metro compiler plugin requires 21+), Android SDK with
compileSdk 37. Output: `app/build/outputs/apk/foss/app-{abi}-foss.apk`.

The `foss` buildType appends `.foss` to the application id and `dist=foss` sets
`includeTranslate=false`, removing the proprietary ML Kit dependency. There is
no `githubRelease` flavor — do not use one.

## AI Tools Usage — TO BE ANSWERED BY THE DEVELOPER

**Assistance Level** — *choose one truthfully:*
- [ ] None – No "AI" tools were used
- [ ] Minimal – Occasional suggestions or lookups
- [ ] Moderate – Used for specific tasks or modules
- [ ] Substantial – Used throughout development
- [ ] Dominant – Most code or content was "AI"-generated

**AI Tool(s)**
WorkBuddy (Claude) was used during packaging/release engineering and documentation.

**What did the tools help with, and how?**
Examples from this project's history: diagnosing a JVM-version mismatch in CI,
writing Fastlane/F-Droid metadata, generating release notes and documentation,
and automating build/Release verification. The Android application code is an
upstream fork of Mihon (GPL-3.0) with a comparatively small set of local changes.

**AI Accountability** — *confirm only what is true:*
- [ ] The human developer(s) reviewed and edited all "AI"-generated outputs
- [ ] The human developer(s) ran manual tests and manually verified all changes

## Further Notices

**APK size exception.** The arm64-v8a FOSS APK is 33.2 MB, slightly over the
30 MB rule-of-thumb (understood to be an exception-capable guideline). The
armeabi-v7a build is 27.8 MB and within the limit. The size comes from the
reader's core rendering path (`ca.mpreg:webgpuviewer`, ~5.9 MB) and multi-format
image decoding; debug symbols were already stripped. We are happy to provide
only the smaller ABI if preferred, or to discuss further size reduction.

**Reproducibility.** Full source and tagged release (`v1.0.0`) are public. CI
builds are automated via GitHub Actions (`.github/workflows/foss-build.yml`),
with signed APKs attached to the release.
