# Sumika — Google Play 截图规划（5 张）

> 适配 1080×1920（手机竖版）主规格，另出 9:16 平板/折叠屏组合图可复用同一文案。
> 风格：Material 3 动态取色背景，文案大字居上 1/3，真实 UI 截图居下 2/3，本地化按 11 语种出图。

| # | 画面（真实截图内容） | 主文案（EN） | 中文示例 | 要传递的点 |
|---|----------------------|--------------|----------|------------|
| 1 | 书架总览（多分类、封面网格、阅读进度条） | **Your whole library, beautifully organized** | 整座书库，井井有条 | 分类/筛选/智能排序 |
| 2 | 阅读器（webtoon 竖滑模式，缩略条+自动滚动） | **Five reading modes for every kind of comic** | 日漫、条漫、页漫，全都有 | RTL/竖滑/翻页/双栏 |
| 3 | 扩展浏览页（源列表、语言过滤） | **Hundreds of community sources via extensions** | 数百个社区漫画源，随你选择 | 扩展生态 + 迁移 .tachibk |
| 4 | Privacy Ledger 界面（网络请求日志列表） | **See exactly what your app does online** | 应用做了什么，一目了然 | 端侧网络日志、零 SDK |
| 5 | 家长模式设置（开关 + 认证弹窗） | **Family-safe reading, one switch away** | 家长模式，一键守护 | SAFE 内容过滤 + 认证锁定 |

## 出图注意事项
1. 截图必须来自真实构建（`assembleRelease` 安装后实拍），禁止装饰性假 UI；
2. Privacy Ledger 截图数据用虚构域名（example.com 等），不得含真实源域名；
3. 每张文案按 11 语种翻译（en-US/zh-CN/zh-TW/ja-JP/ko-KR/es-ES/fr-FR/de-DE/pt-BR/ru-RU/id-ID），短句 ≤40 字符优先；
4. 第 4、5 张为本产品独有卖点（Privacy Ledger / 家长模式），务必保留；
5. 商店轮播顺序即上表顺序：书架（吸引）→ 阅读器（核心）→ 源浏览（能力）→ 隐私（差异化）→ 家长模式（信任）。
