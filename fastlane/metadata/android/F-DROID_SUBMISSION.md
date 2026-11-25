# Sumika — F-Droid / IzzyOnDroid 上架说明

> 包名 `app.sumika`（标准版）/ `app.sumika.foss`（FOSS 变体）· 版本 1.0.0 (code 1) · GPL-3.0 · 仓库 `tt7247129-sys/sumika`

**重要：本文档已按项目实际 Gradle 结构校正。** 本项目**没有 product flavor**，只有 buildTypes
（`debug` / `release` / `foss` / `nightly` / `benchmark`）。任何按 "githubRelease flavor" 编写的
metadata 都会在 F-Droid 构建机上直接失败。

---

## 0. 先决条件（已满足 / 待办）

| 项 | 状态 |
|---|---|
| 源码公开 | ✅ https://github.com/tt7247129-sys/sumika |
| F-Droid 兼容许可证 | ✅ GPL-3.0（LICENSE + NOTICE） |
| 版本 tag | ✅ `v1.0.0` 已推送到 GitHub |
| 专有依赖可剔除 | ✅ `-Pdist=foss` 时 `includeTranslate=false`，ML Kit 不进依赖图 |
| 依赖可从公共仓库解析 | ✅ 已移除 `mavenLocal()`；`sh.calvin.reorderable:3.1.0` 在 Maven Central 完整可用（含 `-debug` 变体） |
| fastlane 元数据 | ✅ `fastlane/metadata/android/{en-US,zh-CN,...}` 12 种语言 |
| 提供 FOSS APK（IzzyOnDroid 需要） | ⬜ 待构建并上传 Release |

---

## 1. F-Droid 官方仓库（从源码构建，不依赖我们提供 APK）

F-Droid 会**自己在构建机上从源码编译**，因此我们不需要上传 APK。只需在
`f-droid/fdroiddata` 仓库提交 `metadata/app.sumika.yml`。

### 1.1 正确的 metadata 模板

```yaml
Categories:
  - Reading
  - Internet
License: GPL-3.0-or-later
AuthorName: Sumika contributors
SourceCode: https://github.com/tt7247129-sys/sumika
IssueTracker: https://github.com/tt7247129-sys/sumika/issues
Changelog: https://github.com/tt7247129-sys/sumika/releases
WebSite: https://sumikareader.pages.dev

Summary: Manga reader with no ads or tracking
Description: |
  Sumika is a free and open-source manga, comic and webtoon reader for
  Android. It has no ads, no subscription, no account and no tracking SDK.

  A built-in Privacy Ledger lists every network host the app contacts, with
  request counts and timestamps, exportable as CSV — so the "no tracking"
  claim can be audited instead of trusted.

  It ships no catalogue and no bundled sources: you bring your own community
  sources or your own local files. Features include offline-first downloads
  with a storage budget, encrypted local backup, phone-to-phone migration over
  your own Wi-Fi, 50+ UI languages with full RTL support, and a parental
  control mode.

RepoType: git
Repo: https://github.com/tt7247129-sys/sumika

Builds:
  - versionName: 1.0.0
    versionCode: 1
    commit: v1.0.0
    gradle:
      - assembleRelease
    gradleprops:
      - dist=foss
    output: app/build/outputs/apk/release/app-universal-release.apk
    timeout: 90

AutoUpdateMode: Version
UpdateCheckMode: Tags ^v[0-9.]+
CurrentVersion: 1.0.0
CurrentVersionCode: 1
```

### 1.2 逐项说明（为什么这样写）

- **没有 `subdir`**：`settings.gradle.kts` 在仓库根目录，Gradle 项目根就是仓库根。
  写成 `subdir: app`（旧模板的错误）会让 F-Droid 在 `app/` 下找 Gradle 项目而失败。
- **用 `assembleRelease` 而非 `assembleFoss`**：F-Droid 要求 metadata 文件名等于 APK 包名。
  本文件是 `app.sumika.yml`，因此包名必须是 `app.sumika`。
  `foss` **buildType** 带 `applicationIdSuffix = ".foss"`，产物包名是 `app.sumika.foss`，会与本
  metadata 不匹配而被拒。因此 F-Droid 侧用 `release` buildType + `dist=foss`：
  包名保持 `app.sumika`，同时剔除 ML Kit。
- **`gradleprops: - dist=foss`**：这是剔除 ML Kit 专有依赖的开关
  （`gradle/build-logic/.../BuildConfig.kt` 中 `includeTranslate = distribution != FOSS`）。
  缺这一行会引入 Google ML Kit 专有二进制，被 F-Droid 以 NonFreeDep 拒绝。
- **`output`**：`release` buildType 且 ABI split 开启时，universal 产物为
  `app/build/outputs/apk/release/app-universal-release.apk`（本地构建已确认存在）。
  注意 buildType 变体的命名规律是 `app-{abi}-{buildType}.apk`——例如 `foss` buildType 产出
  `app-arm64-v8a-foss.apk`（**没有** `-release` 段），CI 日志已实测确认。
- **签名**：`release` / `foss` buildType 都沿用 `signingConfig = debug.signingConfig`。F-Droid
  会用其自有密钥重签，通常可接受；若构建机报签名错误，加
  `prebuild: sed -i '/signingConfig = debug.signingConfig/d' app/build.gradle.kts` 去掉签名配置。

### 1.3 提交步骤

1. Fork https://gitlab.com/fdroid/fdroiddata
2. 新建 `metadata/app.sumika.yml`（用上面模板）
3. 本地校验：`fdroid rewritemeta app.sumika && fdroid build -l app.sumika`
4. 提 MR，等待审核（通常 1–4 周，首次提交可能多轮沟通）

### 1.4 已知风险

- **compileSdk 37**：项目用 `compileSdk 37` / `targetSdk 36`。F-Droid 构建机的 SDK 版本可能
  落后，需要 `androidbuild: 37` 或等待其升级；若被拒，可临时降 `compileSdk` 到构建机支持的版本。
- **JDK 版本**：项目使用 JDK 17 工具链，F-Droid 已默认支持。
- **首次构建耗时**：全量 Gradle 构建 + R8，`timeout: 90`（分钟）留足余量。

---

## 2. IzzyOnDroid（需要现成 FOSS APK，最快见效）

IzzyOnDroid 从 GitHub Release 抓取 APK，需要 Release 中存在**不含专有依赖**的 APK。

### 2.1 构建 FOSS APK

两种方式，任选：

**A. GitHub Actions（推荐，本机资源紧张时）**

手动触发 `FOSS Build` workflow（Actions 标签页 → FOSS Build → Run workflow）。
实测首次构建约 **7 分钟**，产出 5 个 ABI 的 FOSS APK，并自动附加到对应 Release：

| ABI | 大小 |
|---|---|
| arm64-v8a | 34.9 MB |
| armeabi-v7a | 29.2 MB |
| x86 | 38.1 MB |
| x86_64 | 38.0 MB |
| universal | 98.1 MB |

对比标准版（含 ML Kit 翻译）的 arm64 63 MB，FOSS 版瘦身约 **45%**。

**B. 本地构建**

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew :app:assembleFoss -Pdist=foss
```

产物命名规律为 `app-{abi}-foss.apk`（**注意没有 `-release` 段**），位于
`app/build/outputs/apk/foss/`。

### 2.2 上传与收录

```bash
gh release upload v1.0.0 app/build/outputs/apk/foss/app-foss-arm64-v8a-release.apk \
  --repo tt7247129-sys/sumika --clobber
```

- 文件名必须含 `foss`，IzzyOnDroid 据此识别 FOSS 变体
- 提交入口：https://apt.izzysoft.de/fdroid/index.php?submission=1
- 机器人通常在 1–3 天内自动收录

### 2.3 也可走 CDN

本项目已有 Cloudflare R2 CDN 托管标准版 APK。FOSS 版构建完成后同样可上传 R2，
并在 `website/download.html` 增加第二个下载按钮（"FOSS build, no ML Kit"）。

---

## 3. Google Play（延后）

按 `docs/01-双榜对标与破局方案.md` 的政策风险分析，Play 对可加载第三方内容源的阅读器有
拒绝先例。若后续上架，需发布"仅本地文件阅读"的受限变体。截图规划见 `SCREENSHOT_PLAN.md`。
