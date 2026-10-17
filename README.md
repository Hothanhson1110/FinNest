# FinNest
**Tài chính an tâm, đầu tư vững bước**

Version: **0.4.0**

Kotlin · Jetpack Compose · Material 3 · MVVM · Hilt · Room · Coroutines · GitHub Actions.

## Features in this iteration (0.4.0)
- Add stock holdings and non-stock assets.
- Add income and expense ledger entries.
- Dashboard totals are derived from local Room data.
- Transaction history with delete action.
- Technical analysis engine: SMA, EMA, RSI and MACD with edge-case unit tests.
- Read-only market screen and secure backend contract retained from v0.3.0.
- Unit tests for finance calculations and market repository.
- GitHub Actions runs unit tests and builds a debug APK.

## Important
- v0.3.0 adds a read-only market screen and a Retrofit client for the FinNest backend contract `GET /api/v1/market/quotes?symbols=FPT,HPG,VCB`.
- This is **not yet a direct DNSE connection**. Configure a backend you control, which authenticates to DNSE and returns `{ "data": [{ "symbol": "FPT", "price": 123000, "change": 1000, "changePercent": 0.82, "currency": "VND", "updatedAt": "2026-10-17T09:00:00+07:00" }] }`.
- Set the non-secret HTTPS base URL in `gradle.properties`: `MARKET_API_BASE_URL=https://your-backend.example/`. Do not commit private tokens or DNSE credentials.
- The backend must validate symbols, enforce rate limits, authenticate its clients as appropriate, and never expose DNSE API Key/Secret. Do not use plain HTTP in production.
- Until the backend URL is configured, the app clearly reports that market data is not connected; it does not display fabricated live prices.
- The app is an organizer and analytics tool, not financial advice.

## GitHub Actions
Push to `main` or `master`, then open Actions → Android CI → `FinNest-debug-apk` artifact.
The workflow installs Gradle 8.9 and uses JDK 17. No local Android Studio required.

## Local storage planning
Ledger and holdings are typically a few MB for personal use. Historical OHLC data grows much faster: 1,000 symbols × 5 years × ~250 sessions × ~80–150 bytes/row is about 100–190 MB raw before indexes and SQLite overhead. Fetch only selected symbols/ranges and prune old cache.

## v0.4.0 technical-analysis notes
- Indicator inputs are close prices ordered oldest to newest.
- SMA/EMA/RSI/MACD calculations are local pure functions; they do not invent or fetch market prices.
- Live DNSE quotes still require a separately deployed HTTPS backend. The Android app must not contain DNSE API credentials.
- MACD uses EMA(12), EMA(26), and EMA(9) signal by default. Indicator outputs are informational and are not investment advice.


## Build note
This source archive is intended to be built by the included GitHub Actions workflow,
which installs Gradle 8.9 and JDK 17. A Gradle Wrapper is not included in this archive.
Do not add DNSE API keys or secrets to the Android app; live market data requires a
separately deployed HTTPS backend.
