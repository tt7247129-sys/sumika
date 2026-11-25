# Security Policy

Sumika makes a specific promise: **no ads, no analytics, no tracking, and your
library never leaves your phone.** A vulnerability that breaks that promise is
the most serious kind of bug in this project. Thank you for helping find them.

## Reporting a vulnerability

**Please do not open a public issue for security problems.**

Use GitHub's private reporting channel:

1. Go to the [Security tab](https://github.com/tt7247129-sys/sumika/security/advisories)
2. Click **Report a vulnerability**
3. Include affected version (`Settings → About` shows it), a description, and
   reproduction steps

If you cannot use that channel, open a regular issue containing **no technical
details** and ask a maintainer to reach out privately.

### What to include

- Sumika version and Android version
- Which build: standard (`app.sumika`) or FOSS (`app.sumika.foss`)
- Steps to reproduce, or a proof-of-concept
- Impact: what an attacker gains

## What counts as a security issue

In scope:

- **Unauthorised network traffic** — any outbound request not visible in
  *Settings → Privacy Ledger*, or any telemetry/analytics/Crashlytics that
  should not exist in the build. This is the highest-severity class here.
- Leakage of reading history, library contents, credentials, or backup files
- Bypass of the **parental control** lock (PIN / biometric)
- Bypass of extension signature verification or arbitrary code execution via
  a source extension
- Path traversal or arbitrary file write through downloads, backups, or
  `.tachibk` import
- Keystore or credential exposure in logs, crash reports, or exported data

Out of scope:

- Bugs in third-party source extensions (report those to the extension author;
  extensions run in a separate process by design)
- Issues inherited from upstream [Mihon](https://github.com/mihonapp/mihon) that
  also affect Mihon — please report upstream first, then tell us so we can pull
  the fix
- The absence of a feature (e.g. "the app can load arbitrary community sources")
  — that is the intended design of a reader
- Self-XSS, physical access to an unlocked device, or rooted-device scenarios
  unless they bypass a documented protection

## Response expectations

This is a volunteer project, so these are targets, not guarantees:

| Stage | Target |
|---|---|
| Acknowledge report | 3 days |
| Initial assessment | 7 days |
| Fix for critical issues | 14 days |

We will credit you in the release notes unless you ask us not to.

## Verifying our claims yourself

You do not have to take our word for any of this:

- **Audit network behaviour**: *Settings → Privacy Ledger* lists every host the
  app has contacted, with request counts and timestamps, and exports to CSV.
- **Audit the code**: the full source is here, GPL-3.0.
- **Verify the APK**: SHA-256 checksums are published with every release and on
  [the download page](https://sumikareader.pages.dev/download).

## Supported versions

Only the latest release receives security fixes. Sumika is a rolling project;
please reproduce on the newest version before reporting.
