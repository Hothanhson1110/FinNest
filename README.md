# FinNest
**Tài chính an tâm, đầu tư vững bước**

Kotlin · Jetpack Compose · Material 3 · MVVM · Hilt · Room · Coroutines · GitHub Actions.

## Features in this iteration
- Add stock holdings and non-stock assets.
- Add income and expense ledger entries.
- Dashboard totals are derived from local Room data.
- Transaction history with delete action.
- Unit tests for technical indicators and finance calculations.
- GitHub Actions runs unit tests and builds a debug APK.

## Important
- Current market prices are optional user-entered values. DNSE is not connected yet; the UI must not imply prices are live.
- Do not embed API secrets in the Android app. Use a backend proxy for private credentials.
- The app is an organizer and analytics tool, not financial advice.

## GitHub Actions
Push to `main` or `master`, then open Actions → Android CI → `FinNest-debug-apk` artifact.
The workflow installs Gradle 8.9 and uses JDK 17. No local Android Studio required.

## Local storage planning
Ledger and holdings are typically a few MB for personal use. Historical OHLC data grows much faster: 1,000 symbols × 5 years × ~250 sessions × ~80–150 bytes/row is about 100–190 MB raw before indexes and SQLite overhead. Fetch only selected symbols/ranges and prune old cache.
