# Oman Prayer Times

***An app for viewing daily prayer times in Oman from the
the [Ministry of Endowments and Religious Affairs (MERA)'s website](https://www.mara.gov.om/calendar_page2.asp).
***

## API

***The github repository for the api is
available [here](https://github.com/RealUltra/oman-prayer-times-api).***

## Features

- **Prayer Times Table:** View salah, adhan, and iqamah times from the **Ministry of Endowments and
  Religious Affairs**' website.
- **Next Event Timer:** View the remaining time until the next adhan or iqamah.
- **Reminders:** Set custom reminders for the adhan or iqamah.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Design System:** Material 3
- **Build System:** Gradle
- **Minimum SDK:** 23
- **Target SDK:** 36

## Getting Started

Clone the project and open it in Android Studio.

To compile from the command line:

```powershell
.\gradlew.bat :app:compileDebugKotlin
```

To build a debug APK:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Planned

### v26.06.1

- Added: Prayer Times from MERA's website for any date.
- Added: Next Event Timer.
- Added: Prayer Times caching for offline use.

## Changelog

*Nothing to see here*
