# ContextCam

A camera app that saves photos with automatic context — location name, notes, and timestamp — so you always remember *why* you took a photo.

## Features

- **Camera capture** with CameraX
- **Auto location tagging** — human-readable place names via reverse geocoding
- **Notes at capture** — add a quick note before snapping
- **Searchable gallery** — search by note text, location, or date
- **Offline-first** — everything stored on-device with Room DB
- **Material 3 UI**

## Download

Grab the latest APK from [Releases](../../releases).

## Tech Stack

| Layer | Library |
|---|---|
| Camera | CameraX 1.3 |
| Database | Room 2.6 |
| Location | Google Play Services Location |
| Images | Glide 4 |
| UI | Material 3, ViewBinding |
| Language | Kotlin + Coroutines |

## Build

```bash
./gradlew assembleDebug
```

## Release

Tag a version to trigger the GitHub Actions release pipeline:

```bash
git tag v1.0.0
git push origin v1.0.0
```

The workflow will build a release APK and publish it automatically.
