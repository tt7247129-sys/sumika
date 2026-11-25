# Sumika — F-Droid / IzzyOnDroid 上架说明

> 包名 `app.sumika` · 版本 1.0.0 (code 1) · GPL-3.0 · 仓库 `tt7247129-sys/sumika`

## 1. IzzyOnDroid（先做，最快见效）

IzzyOnDroid 从 GitHub Release 自动抓取，无需提 PR。需满足：

- [x] 源码公开：`https://github.com/tt7247129-sys/sumika`
- [x] 使用 F-Droid 兼容许可证：GPL-3.0（LICENSE 文件）
- [x] Release 提供 APK，且**不带专有依赖**（需提供 `foss` 变体）
- [x] fastlane 元数据完整（`fastlane/metadata/android/en-US/`）
- [ ] 在 Release 中上传 `*-foss-*.apk`（关键！当前 APK 含 ML Kit 专有二进制，不符合 FOSS）

**操作**：构建 `:app:assembleGithubRelease -Pdist=foss`，将 `app-foss-arm64-v8a-release.apk` 上传至 Release，IzzyOnDroid 机器人会在 1-3 天内自动收录。

申请入口：https://apt.izzysoft.de/fdroid/index.php?submission=1

## 2. F-Droid 官方仓库（较慢，需人工审核）

F-Droid 采用"从源码构建"模式，需在 `f-droid/fdroiddata` 提 PR 添加 `metadata/app.sumika.yml`。

### 提交步骤

1. Fork https://gitlab.com/fdroid/fdroiddata
2. 在 `metadata/` 下新建 `app.sumika.yml`（见下方模板）
3. 用 `fdroid build -l app.sumika` 本地验证
4. 提交 MR，等待审核（通常 1-4 周）

### metadata/app.sumika.yml 模板

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
Summary: Free, open-source manga reader with no ads or tracking
Description: |
  Sumika is a free and open-source manga, comic and webtoon reader for Android.
  It has no ads, no subscription, no account and no tracking SDK. It includes
  a Privacy Ledger that shows every domain the app contacts, offline-first
  downloads, 50+ languages and a parental control mode.
RepoType: git
Repo: https://github.com/tt7247129-sys/sumika
Builds:
  - versionName: 1.0.0
    versionCode: 1
    commit: v1.0.0
    subdir: app
    gradle:
      - githubRelease
    output: app/build/outputs/apk/githubRelease/app-githubRelease-universal-release-unsigned.apk
    dist: foss
    timeout: 60
AutoUpdateMode: Version v%v
UpdateCheckMode: Tags ^v[0-9.]+
CurrentVersion: 1.0.0
CurrentVersionCode: 1
```

### 注意事项

- F-Droid 要求**从源码可复现构建**，CI 中 `dist=foss` 变体已剔除 ML Kit 专有依赖，符合要求；
- 需创建 `v1.0.0` tag 指向首个正式提交；
- `gradle: - githubRelease` 对应 `app/build.gradle.kts` 中的 product flavor；
- 若构建产物路径不符，用 `fdroid build -l -v app.sumika` 查看实际输出路径并调整；
- 官方审核严格，首次提交可能需要多轮沟通。

## 3. Google Play（延后）

按 `docs/01-双榜对标与破局方案.md` 的政策风险分析，Play 对可加载第三方内容源的阅读器有拒绝先例。若后续上架，需发布"仅本地文件阅读"的受限变体。截图规划见 `SCREENSHOT_PLAN.md`。
