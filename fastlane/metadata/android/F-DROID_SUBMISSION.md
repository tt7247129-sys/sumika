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
      - assembleFoss
    gradleprops:
      - dist=foss
    output: app/build/outputs/apk/foss/app-foss-universal-release.apk
    timeout: 90

AutoUpdateMode: Version
UpdateCheckMode: Tags ^v[0-9.]+
CurrentVersion: 1.0.0
CurrentVersionCode: 1
```

### 1.2 逐项说明（为什么这样写）

- **没有 `subdir`**：`settings.gradle.kts` 在仓库根目录，Gradle 项目根就是仓库根。
  写成 `subdir: app`（旧模板的错误）会让 F-Droid 在 `app/` 下找 Gradle 项目而失败。
- **`gradle: - assembleFoss`**：`foss` 是 **buildType**，不是 flavor。直接给完整任务名
  `assembleFoss` 最稳妥；F-Droid 的 flavor 拼接逻辑（`assemble<Flavor>Release`）在此不适用。
- **`gradleprops: - dist=foss`**：这是剔除 ML Kit 专有依赖的开关
  （`gradle/build-logic/.../BuildConfig.kt` 中 `includeTranslate = distribution != FOSS`）。
  缺这一行会引入 Google ML Kit 专有二进制，被 F-Droid 以 NonFreeDep 拒绝。
- **`output`**：buildType 为 `foss` 时 ABI split 产物在 `apk/foss/`，universal 版文件名带 `-foss-`。
  若实际路径不符，用 `fdroid build -l -v app.sumika` 查看后再改。
- **签名**：`foss` buildType 沿用了 `signingConfig = debug.signingConfig`，产物是已签名的。
  F-Droid 会用其自有密钥重签，通常可接受；若构建机报签名相关错误，在 metadata 中加
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

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew :app:assembleFoss -Pdist=foss
```

产物：`app/build/outputs/apk/foss/app-foss-*-release.apk`

> 注意：本机内存/磁盘紧张时该任务曾因 OOM 卡死。建议 `-Xmx3g` 并关闭其它 Gradle daemon。

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
