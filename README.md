# FinNest

**Tài chính an tâm, đầu tư vững bước**

Android app scaffold built with Kotlin, Jetpack Compose, Material 3, MVVM-ready ViewModel, Hilt, Room, Retrofit, Coroutines and GitHub Actions.

## Current starter scope
- Branded launch/dashboard screen with portfolio and personal-finance overview.
- Local Room database for holdings and transactions.
- Basic technical indicators (SMA, RSI) as pure Kotlin functions.
- GitHub Actions builds a debug APK and runs unit tests.
- DNSE integration is intentionally an adapter placeholder until API credentials, permitted scopes, and exact endpoint/auth flow are configured.

## Security — read before adding DNSE credentials
Do **not** hardcode DNSE API Key or API Secret into Kotlin, `BuildConfig`, assets, Git history, or an APK. Anything shipped in an Android client can be extracted. GitHub Actions Secrets are useful for CI but do not make a secret safe if it is compiled into the app. For private/authenticated DNSE calls, prefer a small backend proxy that stores the secret server-side and returns only required market data. Use Android Keystore-backed encryption for local secrets and never log tokens.

## Build
Open this repository in GitHub and wait for **Actions → Android CI → Build APK**. Download `FinNest-debug-apk` from the completed workflow's artifacts. A debug APK is for personal testing only.

## Termux workflow
```bash
pkg update
pkg install git gh
git clone https://github.com/Hothanhson1110/FinNest.git
cd FinNest
```
You do not need to compile Android locally in Termux; GitHub Actions does that. To upload this starter, extract the ZIP and copy its contents into the cloned repo, then `git add`, `git commit`, and `git push`. Inspect existing repository files first; do not overwrite unrelated work.

## Local storage estimate
Initial database is typically under 1 MB for personal ledger and holdings. Rough planning:
- 10,000 ledger/holding records: usually ~2–10 MB depending on fields and indexes.
- Daily OHLC history: 1,000 symbols × 5 years × ~250 sessions × ~80–150 bytes/row ≈ 100–190 MB raw; indexes and SQLite overhead may raise this to a few hundred MB. This is intentionally **not** downloaded wholesale.
- Suggested policy: cache watchlist/portfolio prices briefly; retain user ledger locally; fetch OHLC only for requested symbols/ranges; cap cache size and prune old bars. Exact size depends on schema, metadata and SQLite page overhead.
