# Sumika v1.0.0 — 首个公开 Release

> 标签：`v1.0.0` · 版本代码：1 · 包名：`app.sumika` / `app.sumika.foss`

## 发布说明（Release Notes）

Sumika 的首个公开版本。一个真正属于你的漫画阅读器 —— 免费、开源、无广告、无追踪。

### 核心特性

- **Privacy Ledger（隐私台账）**：应用内实时记录每一个出站网络域名 + 请求计数 + 时间戳，可复制/导出 CSV。遥测在构建层硬关闭，无法用构建开关重新打开 —— 你可以自己验证。
- **端侧 AI 翻译**：基于 ML Kit 的本地 OCR + 离线翻译（日/中/韩/拉丁），气泡区域原位覆盖。首次使用需下载模型，全程不上传你的图片。FOSS 构建自动剔除该专有依赖。
- **家长模式**：PIN/生物识别锁定 + 强制仅显示 SAFE 内容源，解锁家庭阅读场景。
- **零配置迁移**：支持从 Tachiyomi/Mihon 的 `.tachibk` 备份一键导入书架。
- **50+ 语言 / RTL**：完整本地化，支持从右到左阅读。
- **离线优先**：章节下载 + 存储预算管理，无网络也能读。

### 下载哪个 APK？

| 文件 | 说明 |
|------|------|
| `sumika-arm64-v8a-release.apk` | 标准版（含端侧 AI 翻译），适用于绝大多数现代 Android 手机 |
| `sumika-foss-arm64-v8a-release.apk` | FOSS 版（无 ML Kit 专有依赖），F-Droid / IzzyOnDroid 适用 |
| 其他 ABI | x86 / x86_64 / armeabi-v7a / universal |

### 来源与许可

- 源码：https://github.com/tt7247129-sys/sumika
- 官网：https://sumikareader.pages.dev
- 许可：GPL-3.0（Fork of Mihon，保留上游版权声明，见 NOTICE）

## Release 上传清单

1. `sumika-arm64-v8a-release.apk`（标准版，arm64）
2. `sumika-*-release.apk`（其他 ABI，可选）
3. `sumika-foss-arm64-v8a-release.apk`（FOSS 版，待 FOSS 构建完成后补充）
4. `SHA256SUMS`（校验和）
