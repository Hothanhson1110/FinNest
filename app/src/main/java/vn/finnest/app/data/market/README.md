# DNSE integration notes

Before wiring the Retrofit interface:
1. Read the official docs: https://developers.dnse.com.vn/
2. Verify the current Market Data endpoint path, response JSON schema, allowed rate limits, WebSocket availability, and authentication requirements.
3. Keep API Key/Secret off-device whenever the API uses private credentials. Prefer a backend proxy with secrets stored in server-side environment variables.
4. Use HTTPS, bounded timeouts, retry/backoff, and explicit stale-data indicators.
5. Do not auto-place trades in the initial release. Signals should be informational and manually reviewed.
