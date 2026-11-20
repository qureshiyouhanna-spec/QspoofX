# QSpoof X BGMI Edition v2.0
For Realme 9 Pro+ (RMX3392), Android 14.

## Included
- BGMI India launcher shortcut (`com.pubg.imobile`)
- Root availability check
- Device/build information
- Read-only thermal service diagnostic
- 90 FPS target information and safety notes
- GitHub Actions cloud build

## Cloud build from a phone
1. Extract this ZIP.
2. Upload the extracted folder CONTENTS to the root of your GitHub repository, preserving `.github/workflows/android.yml`.
3. Commit to `main`.
4. Open Actions → Build QSpoof X BGMI APK → Run workflow.
5. Download artifact `QSpoofX-BGMI-v2-debug-apk`.

## Limitations
This build does not force 90 FPS, modify BGMI files, bypass anti-cheat, spoof device identity, or disable thermal protections. BGMI's own device/game-version support determines available FPS options. Live FPS/GPU telemetry and reversible tuning are not included yet. Thermal data varies by firmware.
