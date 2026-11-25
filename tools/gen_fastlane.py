#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate Google Play metadata under fastlane/metadata/android/.

Hard limits enforced here (build fails if violated):
  title              <= 30 chars
  short_description  <= 80 chars
  full_description   <= 4000 chars

Run:  python3 tools/gen_fastlane.py
"""
from __future__ import annotations

import pathlib
import sys

ROOT = pathlib.Path(__file__).resolve().parent.parent
OUT = ROOT / "fastlane" / "metadata" / "android"

GH = "https://github.com/tt7247129-sys/sumika"
SITE = "sumika.app"

TITLE_MAX = 30
SHORT_MAX = 80
FULL_MAX = 4000

# locale -> (title, short_description, full_description)
DATA: dict[str, tuple[str, str, str]] = {
    "en-US": (
        "Sumika: Offline Manga Library",
        "Free open-source manga reader. No ads, no tracking, 50 languages, offline first.",
        f"""Paying a subscription to re-read chapters you already bought. Ads that hijack the page halfway through. Apps that upload your library to someone else's server.

Sumika is a free, fully open-source manga, comic and webtoon reader. No ads. No in-app purchases. No subscription. No analytics SDK. No account. Your library never leaves your phone.

WHY PEOPLE SWITCH
- Every commercial reader charges per chapter, per ticket, or per month. Sumika charges nothing, forever.
- Commercial readers can revoke your downloads when you stop paying. Sumika keeps chapters as plain files you own.
- Commercial readers ship trackers. Sumika ships a Privacy Ledger that lists every domain the app contacts, and lets you export the list so you can check it yourself.

WHAT YOU GET
- A real library: categories, filters, sorting, bulk actions, reading history, and two-way sync with MyAnimeList, AniList, Kitsu, Shikimori and Bangumi.
- A reader built for long sessions: five reading modes, page and webtoon, right-to-left and vertical, double-page spreads, custom colour filters, tap zones and volume-key paging.
- Offline first: queue downloads, cap your storage budget, auto-evict chapters you already finished, and keep everything in an open folder you can back up or move to a new phone.
- Migration without an account: encrypted local backup, or phone-to-phone transfer over your own Wi-Fi. No cloud, no sign-up, no e-mail address.
- 50 languages that follow your system language, with full right-to-left mirroring for Arabic, Hebrew, Persian and Urdu, and English as the fallback.

BUILT IN THE OPEN
Sumika is a fork of the Mihon open-source project, released under GPL-3.0. Read the code, build it yourself, or audit every network call: {GH}

Sumika is a reader, not a store. It ships with no bundled catalogue: you bring your own sources or your own files.

No ads. No subscriptions. No tracking. Just reading.
{SITE}""",
    ),
    "es-ES": (
        "Sumika: Lector manga offline",
        "Lector de manga libre y sin anuncios. 50 idiomas, sin cuenta, 100% offline.",
        f"""Pagar una suscripción para releer capítulos que ya compraste. Anuncios que te sacan de la página a mitad de lectura. Apps que suben tu biblioteca a un servidor ajeno.

Sumika es un lector de manga, cómic y webtoon gratuito y totalmente de código abierto. Sin anuncios. Sin compras dentro de la app. Sin suscripción. Sin SDK de analítica. Sin cuenta. Tu biblioteca nunca sale de tu teléfono.

POR QUÉ LA GENTE SE CAMBIA
- Las apps comerciales cobran por capítulo, por ticket o al mes. Sumika no cobra nada, nunca.
- Las apps comerciales pueden revocar tus descargas cuando dejas de pagar. Sumika guarda los capítulos como archivos que son tuyos.
- Las apps comerciales incluyen rastreadores. Sumika incluye un Registro de Privacidad que muestra cada dominio al que se conecta la app y permite exportarlo para que lo verifiques tú mismo.

QUÉ INCLUYE
- Una biblioteca de verdad: categorías, filtros, orden, acciones en lote, historial y sincronización con MyAnimeList, AniList, Kitsu, Shikimori y Bangumi.
- Un lector pensado para sesiones largas: cinco modos de lectura, página y webtoon, derecha-a-izquierda y vertical, dobles páginas, filtros de color, zonas táctiles y paso de página con los botones de volumen.
- Pensado para offline: cola de descargas, límite de almacenamiento, borrado automático de capítulos ya leídos y una carpeta abierta que puedes copiar o mover a otro teléfono.
- Migración sin cuenta: copia de seguridad cifrada o transferencia de teléfono a teléfono por tu propia Wi-Fi. Sin nube, sin registro, sin correo.
- 50 idiomas que siguen el idioma del sistema, con espejo completo de derecha a izquierda para árabe, hebreo, persa y urdu, y español incluido.

DESARROLLO ABIERTO
Sumika es un fork del proyecto de código abierto Mihon, publicado bajo licencia GPL-3.0. Lee el código, compílalo tú mismo o audita cada llamada de red: {GH}

Sumika es un lector, no una tienda. No incluye catálogo: tú aportas tus fuentes o tus propios archivos.

Sin anuncios. Sin suscripciones. Sin rastreo. Solo lectura.
{SITE}""",
    ),
    "de-DE": (
        "Sumika: Manga offline, gratis",
        "Freier Open-Source-Mangaleser. Keine Werbung, kein Tracking, 50 Sprachen.",
        f"""Ein Abo zahlen, um Kapitel noch einmal zu lesen, die du längst gekauft hast. Werbung, die dich mitten auf der Seite aus dem Lesefluss reißt. Apps, die deine Bibliothek auf fremde Server laden.

Sumika ist ein kostenloser, vollständig quelloffener Leser für Manga, Comics und Webtoons. Keine Werbung. Keine In-App-Käufe. Kein Abo. Kein Analyse-SDK. Kein Konto. Deine Bibliothek verlässt dein Telefon nie.

WARUM LEUTE WECHSELN
- Kommerzielle Reader kassieren pro Kapitel, pro Ticket oder monatlich. Sumika kostet nichts, dauerhaft.
- Kommerzielle Reader können deine Downloads sperren, sobald du nicht mehr zahlst. Sumika legt Kapitel als normale Dateien ab, die dir gehören.
- Kommerzielle Reader enthalten Tracker. Sumika hat ein Datenschutz-Journal, das jede Domain auflistet, die die App kontaktiert, und den Export erlaubt, damit du es selbst prüfen kannst.

WAS DU BEKOMMST
- Eine echte Bibliothek: Kategorien, Filter, Sortierung, Massenaktionen, Verlauf und Zwei-Wege-Sync mit MyAnimeList, AniList, Kitsu, Shikimori und Bangumi.
- Einen Reader für lange Sitzungen: fünf Lesemodi, Seite und Webtoon, rechts-nach-links und vertikal, Doppelseiten, Farbfilter, Tippzonen und Blättern per Lautstärketaste.
- Offline zuerst: Download-Warteschlange, Speicherbudget, automatisches Entfernen gelesener Kapitel und ein offener Ordner, den du sichern oder umziehen kannst.
- Umzug ohne Konto: verschlüsseltes lokales Backup oder Übertragung von Telefon zu Telefon über dein eigenes WLAN. Keine Cloud, keine Anmeldung, keine E-Mail-Adresse.
- 50 Sprachen, die deiner Systemsprache folgen, mit vollständiger Rechts-nach-links-Spiegelung für Arabisch, Hebräisch, Persisch und Urdu.

OFFEN ENTWICKELT
Sumika ist ein Fork des Open-Source-Projekts Mihon unter GPL-3.0. Lies den Code, baue die App selbst oder prüfe jeden Netzwerkaufruf: {GH}

Sumika ist ein Reader, kein Shop. Es bringt keinen Katalog mit: Du bringst deine Quellen oder deine eigenen Dateien mit.

Keine Werbung. Kein Abo. Kein Tracking. Nur Lesen.
{SITE}""",
    ),
    "fr-FR": (
        "Sumika : manga hors ligne",
        "Lecteur manga libre et sans pub. 50 langues, sans compte, 100% hors ligne.",
        f"""Payer un abonnement pour relire des chapitres déjà achetés. Des publicités qui t'éjectent de la page en pleine lecture. Des applis qui envoient ta bibliothèque sur un serveur inconnu.

Sumika est un lecteur de manga, de comics et de webtoon gratuit et entièrement open source. Sans publicité. Sans achat intégré. Sans abonnement. Sans SDK d'analyse. Sans compte. Ta bibliothèque ne quitte jamais ton téléphone.

POURQUOI ON CHANGE
- Les lecteurs commerciaux facturent au chapitre, au ticket ou au mois. Sumika ne facture rien, jamais.
- Les lecteurs commerciaux peuvent révoquer tes téléchargements dès que tu arrêtes de payer. Sumika stocke les chapitres dans des fichiers qui t'appartiennent.
- Les lecteurs commerciaux embarquent des traqueurs. Sumika embarque un Journal de confidentialité qui liste chaque domaine contacté et permet de l'exporter pour que tu vérifies toi-même.

CE QUE TU OBTIENS
- Une vraie bibliothèque : catégories, filtres, tri, actions groupées, historique et synchronisation bidirectionnelle avec MyAnimeList, AniList, Kitsu, Shikimori et Bangumi.
- Un lecteur pensé pour les longues sessions : cinq modes de lecture, page et webtoon, droite-à-gauche et vertical, doubles pages, filtres de couleur, zones tactiles et pagination par les boutons de volume.
- Hors ligne d'abord : file de téléchargement, plafond de stockage, suppression automatique des chapitres lus et un dossier ouvert que tu peux sauvegarder ou déplacer.
- Migration sans compte : sauvegarde locale chiffrée, ou transfert de téléphone à téléphone sur ton propre Wi-Fi. Pas de cloud, pas d'inscription, pas d'e-mail.
- 50 langues qui suivent la langue du système, avec miroir complet droite-à-gauche pour l'arabe, l'hébreu, le persan et l'ourdou.

DÉVELOPPÉ À CIEL OUVERT
Sumika est un fork du projet open source Mihon, publié sous GPL-3.0. Lis le code, compile-le toi-même ou audite chaque appel réseau : {GH}

Sumika est un lecteur, pas une boutique. Aucun catalogue inclus : tu apportes tes sources ou tes propres fichiers.

Sans pub. Sans abonnement. Sans traçage. Juste la lecture.
{SITE}""",
    ),
    "pt-BR": (
        "Sumika: leitor mangá offline",
        "Leitor de mangá livre e sem anúncios. 50 idiomas, sem conta, 100% offline.",
        f"""Pagar assinatura para reler capítulos que você já comprou. Anúncios que te tiram da página no meio da leitura. Apps que enviam sua biblioteca para o servidor de outra pessoa.

Sumika é um leitor de mangá, quadrinho e webtoon gratuito e totalmente open source. Sem anúncios. Sem compras no app. Sem assinatura. Sem SDK de análise. Sem conta. Sua biblioteca nunca sai do seu celular.

POR QUE AS PESSOAS TROCAM
- Leitores comerciais cobram por capítulo, por ticket ou por mês. O Sumika não cobra nada, nunca.
- Leitores comerciais podem revogar seus downloads quando você para de pagar. O Sumika guarda os capítulos em arquivos que são seus.
- Leitores comerciais trazem rastreadores. O Sumika traz um Registro de Privacidade que lista cada domínio contatado e permite exportar a lista para você conferir.

O QUE VEM INCLUÍDO
- Uma biblioteca de verdade: categorias, filtros, ordenação, ações em massa, histórico e sincronização com MyAnimeList, AniList, Kitsu, Shikimori e Bangumi.
- Um leitor para sessões longas: cinco modos de leitura, página e webtoon, da direita para a esquerda e vertical, páginas duplas, filtros de cor, zonas de toque e virar página pelos botões de volume.
- Offline primeiro: fila de downloads, limite de armazenamento, remoção automática de capítulos já lidos e uma pasta aberta que você pode copiar ou mover de celular.
- Migração sem conta: backup local criptografado ou transferência de celular para celular pelo seu próprio Wi-Fi. Sem nuvem, sem cadastro, sem e-mail.
- 50 idiomas seguindo o idioma do sistema, com espelhamento completo da direita para a esquerda para árabe, hebraico, persa e urdu.

DESENVOLVIDO ABERTAMENTE
Sumika é um fork do projeto open source Mihon, distribuído sob GPL-3.0. Leia o código, compile você mesmo ou audite cada chamada de rede: {GH}

Sumika é um leitor, não uma loja. Não inclui catálogo: você traz suas fontes ou seus próprios arquivos.

Sem anúncios. Sem assinatura. Sem rastreamento. Só leitura.
{SITE}""",
    ),
    "it-IT": (
        "Sumika: lettore manga offline",
        "Lettore manga libero e senza pubblicita. 50 lingue, nessun account.",
        f"""Pagare un abbonamento per rileggere capitoli che hai già comprato. Pubblicità che ti porta via dalla pagina a metà lettura. App che caricano la tua libreria su un server altrui.

Sumika è un lettore di manga, fumetti e webtoon gratuito e completamente open source. Nessuna pubblicità. Nessun acquisto in-app. Nessun abbonamento. Nessun SDK di analisi. Nessun account. La tua libreria non lascia mai il telefono.

PERCHE SI CAMBIA
- I lettori commerciali fanno pagare a capitolo, a ticket o al mese. Sumika non fa pagare nulla, mai.
- I lettori commerciali possono revocare i download quando smetti di pagare. Sumika salva i capitoli in file che sono tuoi.
- I lettori commerciali includono tracker. Sumika include un Registro della Privacy che elenca ogni dominio contattato e permette di esportarlo per verificarlo da solo.

COSA OTTIENI
- Una libreria vera: categorie, filtri, ordinamento, azioni multiple, cronologia e sincronizzazione con MyAnimeList, AniList, Kitsu, Shikimori e Bangumi.
- Un lettore per sessioni lunghe: cinque modalità di lettura, pagina e webtoon, destra-sinistra e verticale, doppie pagine, filtri colore, zone di tocco e cambio pagina con i tasti del volume.
- Offline prima di tutto: coda di download, limite di spazio, rimozione automatica dei capitoli già letti e una cartella aperta che puoi copiare o spostare.
- Migrazione senza account: backup locale cifrato o trasferimento da telefono a telefono sulla tua Wi-Fi. Niente cloud, niente registrazione, niente e-mail.
- 50 lingue che seguono la lingua di sistema, con specchiatura completa da destra a sinistra per arabo, ebraico, persiano e urdu.

SVILUPPATO ALLA LUCE DEL SOLE
Sumika è un fork del progetto open source Mihon, distribuito con licenza GPL-3.0. Leggi il codice, compilalo da te o controlla ogni chiamata di rete: {GH}

Sumika è un lettore, non un negozio. Non include alcun catalogo: porti le tue fonti o i tuoi file.

Niente pubblicità. Nessun abbonamento. Nessun tracciamento. Solo lettura.
{SITE}""",
    ),
    "ja-JP": (
        "Sumika：オフライン漫画ライブラリ",
        "広告なし・追跡なしの無料オープンソース漫画リーダー。50言語対応。",
        f"""買ったはずの話を読み返すためだけに、毎月お金を払っていませんか。読んでいる途中で広告に画面を奪われていませんか。自分の本棚が他人のサーバーに送られているのが不安ではありませんか。

Sumika は無料で完全オープンソースの漫画・コミック・Webtoon リーダーです。広告なし。アプリ内課金なし。サブスクなし。解析 SDK なし。アカウント登録なし。あなたの本棚が端末の外に出ることはありません。

乗り換える理由
- 商業アプリは話ごと、チケットごと、あるいは月額で課金します。Sumika は一切課金しません。
- 商業アプリは支払いを止めるとダウンロードを取り上げます。Sumika はあなたのものになる普通のファイルとして保存します。
- 商業アプリは追跡 SDK を同梱します。Sumika は「プライバシー台帳」を同梱し、通信したすべてのドメインを一覧表示し、書き出して自分で検証できるようにします。

できること
- 本物の本棚：カテゴリ、フィルタ、並び替え、一括操作、履歴、MyAnimeList・AniList・Kitsu・Shikimori・Bangumi との双方向同期。
- 長時間読書のためのリーダー：5 つの閲覧モード、ページと Webtoon、右開きと縦スクロール、見開き、カラーフィルタ、タップ領域、音量キーでのページ送り。
- オフライン優先：ダウンロードキュー、保存容量の上限、読了済みの自動削除、バックアップも移動もできる開かれたフォルダ。
- アカウント不要の移行：暗号化ローカルバックアップ、または自分の Wi-Fi 経由で端末間転送。クラウドなし、登録なし、メールアドレスなし。
- 端末の言語に追従する 50 言語。アラビア語・ヘブライ語・ペルシア語・ウルドゥー語は完全な右から左へのミラーリングに対応。

オープンに開発
Sumika はオープンソースプロジェクト Mihon のフォークで、GPL-3.0 で公開されています。コードを読み、自分でビルドし、すべての通信を監査できます：{GH}

Sumika はリーダーであり、ストアではありません。カタログは同梱しません。ソースやファイルはご自身で用意します。

広告なし。サブスクなし。追跡なし。ただ読むだけ。
{SITE}""",
    ),
    "ko-KR": (
        "Sumika: 오프라인 만화 라이브러리",
        "광고 없는 무료 오픈소스 만화 리더. 50개 언어, 계정 불필요.",
        f"""이미 산 화를 다시 보려고 매달 구독료를 내고 있나요. 읽는 도중 광고가 화면을 가로채나요. 내 서재가 남의 서버로 올라가는 게 불안한가요.

Sumika 는 무료이며 완전한 오픈소스 만화·코믹·웹툰 리더입니다. 광고 없음. 인앱 결제 없음. 구독 없음. 분석 SDK 없음. 계정 없음. 서재는 절대 기기 밖으로 나가지 않습니다.

왜 갈아타는가
- 상용 리더는 화당, 티켓당, 또는 월 단위로 과금합니다. Sumika 는 아무것도 청구하지 않습니다.
- 상용 리더는 결제를 멈추면 다운로드를 회수합니다. Sumika 는 내 것이 되는 평범한 파일로 저장합니다.
- 상용 리더는 추적기를 넣습니다. Sumika 는 앱이 접속한 모든 도메인을 보여주고 내보낼 수 있는 '프라이버시 원장'을 넣습니다. 직접 검증하세요.

포함된 것
- 진짜 서재: 카테고리, 필터, 정렬, 일괄 작업, 기록, MyAnimeList·AniList·Kitsu·Shikimori·Bangumi 양방향 동기화.
- 긴 세션을 위한 리더: 5가지 보기 모드, 페이지와 웹툰, 오른쪽에서 왼쪽, 세로 스크롤, 양면 보기, 색상 필터, 탭 영역, 볼륨 키로 페이지 넘기기.
- 오프라인 우선: 다운로드 큐, 저장 공간 한도, 읽은 화 자동 정리, 백업하거나 옮길 수 있는 열린 폴더.
- 계정 없는 이전: 암호화된 로컬 백업 또는 내 Wi-Fi 로 기기 간 전송. 클라우드 없음, 가입 없음, 이메일 없음.
- 시스템 언어를 따르는 50개 언어. 아랍어·히브리어·페르시아어·우르두어는 완전한 오른쪽에서 왼쪽 미러링을 지원합니다.

공개적으로 개발
Sumika 는 오픈소스 Mihon 프로젝트의 포크이며 GPL-3.0 으로 배포됩니다. 코드를 읽고 직접 빌드하고 모든 네트워크 호출을 감사할 수 있습니다: {GH}

Sumika 는 리더이지 스토어가 아닙니다. 카탈로그를 포함하지 않으며, 소스나 파일은 직접 준비합니다.

광고 없음. 구독 없음. 추적 없음. 오직 읽기.
{SITE}""",
    ),
    "ru-RU": (
        "Sumika: манга офлайн",
        "Бесплатный открытый читалка манги. Без рекламы, без слежки, 50 языков.",
        f"""Платить за подписку, чтобы перечитать главы, которые вы уже купили. Реклама, которая выбивает вас со страницы посреди чтения. Приложения, которые отправляют вашу библиотеку на чужой сервер.

Sumika — бесплатная и полностью открытая читалка манги, комиксов и вебтунов. Без рекламы. Без встроенных покупок. Без подписки. Без SDK аналитики. Без аккаунта. Ваша библиотека никогда не покидает телефон.

ПОЧЕМУ ПЕРЕХОДЯТ
- Коммерческие читалки берут деньги за главу, за билет или за месяц. Sumika не берёт ничего и никогда.
- Коммерческие читалки могут отозвать ваши загрузки, когда вы перестаёте платить. Sumika хранит главы как обычные файлы, которые принадлежат вам.
- Коммерческие читалки содержат трекеры. В Sumika есть «Журнал приватности», который перечисляет все домены, к которым обращается приложение, и позволяет выгрузить список для самостоятельной проверки.

ЧТО ВНУТРИ
- Настоящая библиотека: категории, фильтры, сортировка, массовые действия, история и двусторонняя синхронизация с MyAnimeList, AniList, Kitsu, Shikimori и Bangumi.
- Читалка для долгих сессий: пять режимов чтения, страница и вебтун, справа налево и вертикально, развороты, цветовые фильтры, зоны нажатия и перелистывание кнопками громкости.
- Офлайн в первую очередь: очередь загрузок, лимит хранилища, автоудаление прочитанных глав и открытая папка, которую можно скопировать или перенести.
- Переезд без аккаунта: зашифрованная локальная копия или передача с телефона на телефон по вашему Wi-Fi. Без облака, без регистрации, без почты.
- 50 языков, следующих языку системы, с полным зеркалированием справа налево для арабского, иврита, персидского и урду.

РАЗРАБОТКА ОТКРЫТА
Sumika — форк открытого проекта Mihon под лицензией GPL-3.0. Читайте код, собирайте сами или проверяйте каждый сетевой вызов: {GH}

Sumika — это читалка, а не магазин. Каталог не входит: источники или файлы вы приносите свои.

Без рекламы. Без подписки. Без слежки. Только чтение.
{SITE}""",
    ),
    "zh-CN": (
        "Sumika：离线漫画书架",
        "开源免费漫画阅读器：无广告、无追踪、50 语种、离线优先。",
        f"""为了重读已经买过的章节而继续付订阅费；读到一半被广告拽去浏览器；书架被悄悄上传到别人的服务器。

Sumika 是一款完全开源、永久免费的漫画 / 条漫阅读器。无广告、无内购、无订阅、无统计 SDK、无需注册账号。你的书架永远不会离开你的手机。

为什么换过来
- 商业阅读器按章、按券、按月收费，Sumika 一分钱都不收，永远。
- 商业阅读器一旦停付就收回你的下载。Sumika 把章节存成属于你的普通文件。
- 商业阅读器内置追踪。Sumika 内置「隐私账本」，把 App 联系过的每一个域名列出来，并支持导出让你自己核验。

包含什么
- 真正的书架：分类、筛选、排序、批量操作、阅读历史，以及与 MyAnimeList、AniList、Kitsu、Shikimori、Bangumi 的双向同步。
- 为长时间阅读而做的阅读器：五种阅读模式、翻页与条漫、右开与竖排、双页跨页、自定义色彩滤镜、点击分区、音量键翻页。
- 离线优先：下载队列、存储上限、读完自动清理，全部落在可备份、可迁移的开放目录里。
- 迁移不需要账号：加密本地备份，或用你自己的 Wi-Fi 手机对手机直传。无云端、无注册、不留邮箱。
- 50 种语言跟随系统自动切换，阿拉伯语 / 希伯来语 / 波斯语 / 乌尔都语完整 RTL 镜像，未覆盖语种回落英语。

开放开发
Sumika 是开源项目 Mihon 的分支，以 GPL-3.0 发布。你可以读代码、自己编译，或审计每一次网络请求：{GH}

Sumika 是阅读器，不是内容商店。不内置任何书源目录，书源与文件由你自己提供。

无广告。无订阅。无追踪。只管阅读。
{SITE}""",
    ),
    "ar": (
        "سوميكا: مكتبة مانغا دون اتصال",
        "قارئ مانغا مجاني ومفتوح المصدر. بلا إعلانات أو تتبع وبدعم 50 لغة.",
        f"""تدفع اشتراكًا لإعادة قراءة فصول اشتريتها من قبل. إعلانات تقطع عليك الصفحة في منتصف القراءة. تطبيقات ترفع مكتبتك إلى خادم شخص آخر.

سوميكا قارئ مانغا وقصص مصورة وويب توون مجاني ومفتوح المصدر بالكامل. بلا إعلانات. بلا مشتريات داخل التطبيق. بلا اشتراك. بلا حزمة تحليلات. بلا حساب. مكتبتك لا تغادر هاتفك أبدًا.

لماذا ينتقل الناس
- القارئات التجارية تتقاضى مقابل كل فصل أو تذكرة أو شهر. سوميكا لا تتقاضى شيئًا، إلى الأبد.
- القارئات التجارية تسحب تنزيلاتك عندما تتوقف عن الدفع. سوميكا تحفظ الفصول كملفات عادية تملكها أنت.
- القارئات التجارية تضم أدوات تتبع. سوميكا تضم «سجل الخصوصية» الذي يعرض كل نطاق يتصل به التطبيق ويسمح بتصديره لتتحقق بنفسك.

ما تحصل عليه
- مكتبة حقيقية: تصنيفات وفلاتر وترتيب وإجراءات جماعية وسجل قراءة ومزامنة ثنائية الاتجاه مع MyAnimeList وAniList وKitsu وShikimori وBangumi.
- قارئ مصمم للجلسات الطويلة: خمسة أنماط قراءة، صفحات وويب توون، من اليمين إلى اليسار وعمودي، صفحات مزدوجة، فلاتر ألوان، مناطق لمس، وتقليب بأزرار الصوت.
- دون اتصال أولًا: قائمة تنزيل، حد للمساحة، حذف تلقائي للفصول المقروءة، ومجلد مفتوح يمكنك نسخه أو نقله.
- انتقال بلا حساب: نسخة احتياطية محلية مشفرة، أو نقل من هاتف إلى هاتف عبر شبكة Wi-Fi الخاصة بك. بلا سحابة، بلا تسجيل، بلا بريد.
- 50 لغة تتبع لغة النظام، مع انعكاس كامل من اليمين إلى اليسار للعربية والعبرية والفارسية والأردية.

تطوير علني
سوميكا فرع من مشروع Mihon مفتوح المصدر، يصدر تحت رخصة GPL-3.0. اقرأ الشيفرة أو ابنِه بنفسك أو دقّق كل اتصال شبكي: {GH}

سوميكا قارئ وليس متجرًا. لا يضم كتالوجًا: أنت تجلب مصادرك أو ملفاتك.

بلا إعلانات. بلا اشتراك. بلا تتبع. قراءة فقط.
{SITE}""",
    ),
    "hi-IN": (
        "Sumika: ऑफलाइन मैंगा लाइब्रेरी",
        "मुफ़्त ओपन-सोर्स मैंगा रीडर। कोई विज्ञापन नहीं, 50 भाषाएँ, ऑफलाइन।",
        f"""जिन चैप्टर को आप पहले ही खरीद चुके हैं, उन्हें दोबारा पढ़ने के लिए सब्सक्रिप्शन देना। पढ़ते समय बीच में विज्ञापन का पेज बदल देना। ऐसे ऐप जो आपकी लाइब्रेरी किसी और के सर्वर पर भेज देते हैं।

Sumika एक मुफ़्त और पूरी तरह ओपन-सोर्स मैंगा, कॉमिक और वेबटून रीडर है। कोई विज्ञापन नहीं। कोई इन-ऐप खरीद नहीं। कोई सब्सक्रिप्शन नहीं। कोई एनालिटिक्स SDK नहीं। कोई अकाउंट नहीं। आपकी लाइब्रेरी फ़ोन से बाहर नहीं जाती।

लोग क्यों बदलते हैं
- व्यावसायिक रीडर प्रति चैप्टर, प्रति टिकट या प्रति माह शुल्क लेते हैं। Sumika कुछ भी नहीं लेता, कभी नहीं।
- व्यावसायिक रीडर भुगतान रुकते ही आपके डाउनलोड छीन लेते हैं। Sumika चैप्टर को ऐसी सामान्य फ़ाइलों में रखता है जो आपकी हैं।
- व्यावसायिक रीडर ट्रैकर भेजते हैं। Sumika में "प्राइवेसी लेजर" है जो ऐप के हर डोमेन को दिखाता है और सूची निर्यात करने देता है ताकि आप खुद जाँच सकें।

क्या मिलता है
- असली लाइब्रेरी: श्रेणियाँ, फ़िल्टर, क्रम, बल्क क्रियाएँ, इतिहास और MyAnimeList, AniList, Kitsu, Shikimori व Bangumi के साथ दो-तरफ़ा सिंक।
- लंबे सत्रों के लिए रीडर: पाँच पठन मोड, पेज और वेबटून, दाएँ-से-बाएँ और वर्टिकल, डबल पेज, रंग फ़िल्टर, टैप ज़ोन और वॉल्यूम बटन से पेज पलटना।
- पहले ऑफलाइन: डाउनलोड कतार, स्टोरेज सीमा, पढ़े गए चैप्टर की स्वतः सफ़ाई, और एक खुला फ़ोल्डर जिसे आप बैकअप या स्थानांतरित कर सकते हैं।
- बिना अकाउंट माइग्रेशन: एन्क्रिप्टेड लोकल बैकअप, या अपने ही Wi-Fi पर फ़ोन-से-फ़ोन ट्रांसफ़र। क्लाउड नहीं, साइन-अप नहीं, ईमेल नहीं।
- 50 भाषाएँ जो सिस्टम भाषा का पालन करती हैं, अरबी, हिब्रू, फ़ारसी और उर्दू के लिए पूर्ण दाएँ-से-बाएँ मिररिंग के साथ।

खुलकर विकसित
Sumika, ओपन-सोर्स प्रोजेक्ट Mihon का फ़ोर्क है और GPL-3.0 के अंतर्गत जारी होता है। कोड पढ़ें, खुद बिल्ड करें, या हर नेटवर्क कॉल की जाँच करें: {GH}

Sumika एक रीडर है, स्टोर नहीं। इसमें कोई कैटलॉग नहीं आता: स्रोत या फ़ाइलें आप लाते हैं।

कोई विज्ञापन नहीं। कोई सब्सक्रिप्शन नहीं। कोई ट्रैकिंग नहीं। बस पढ़ना।
{SITE}""",
    ),
}


def main() -> int:
    errors: list[str] = []
    for locale, (title, short, full) in DATA.items():
        if len(title) > TITLE_MAX:
            errors.append(f"{locale}: title {len(title)} > {TITLE_MAX} ({title!r})")
        if len(short) > SHORT_MAX:
            errors.append(f"{locale}: short {len(short)} > {SHORT_MAX} ({short!r})")
        if len(full) > FULL_MAX:
            errors.append(f"{locale}: full {len(full)} > {FULL_MAX}")

    if errors:
        print("VALIDATION FAILED")
        for e in errors:
            print(" -", e)
        return 1

    for locale, (title, short, full) in DATA.items():
        d = OUT / locale
        d.mkdir(parents=True, exist_ok=True)
        (d / "title.txt").write_text(title + "\n", encoding="utf-8")
        (d / "short_description.txt").write_text(short + "\n", encoding="utf-8")
        (d / "full_description.txt").write_text(full + "\n", encoding="utf-8")
        cl = d / "changelogs"
        cl.mkdir(exist_ok=True)
        (cl / "1.txt").write_text(
            "First public release: zero ads, zero tracking, Privacy Ledger, offline-first downloads, 50 languages.\n",
            encoding="utf-8",
        )
        print(f"{locale:8s} title={len(title):2d} short={len(short):2d} full={len(full):5d}")

    print(f"\nOK -> {OUT}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
