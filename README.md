# Amphibians

Small Android app (Kotlin + Jetpack Compose) that loads and displays a list of amphibians. This README explains project structure, implemented features, and how each feature works.

## Project overview

- Language: Kotlin
- UI: Jetpack Compose (Material3)
- Architecture: simple DI via an `AppContainer` in a custom `Application` class, `ViewModel` for UI state, repository for data access.
- Networking / Images: HTTP client (repository) + Coil for image loading.
- Minimum files referenced: `AmphibiansApplication`, `AppContainer`, `AmphibiansViewModel`, repository (`amphibiansRepository`), composables under `userinterface` (`AmphibiansApp`, `LoadingScreen`, `ErrorScreen`, `AmphibiansList`, `AmphibianCard`), `MainActivity`, and `AndroidManifest.xml` (with `android:name` set).

## Features implemented

1. App-level dependency container
2. Custom `Application` class
3. Repository for fetching amphibian data
4. `AmphibiansViewModel` with factory and UI state management
5. Compose UI with multiple states: Loading, Error, Success (list)
6. Image loading with Coil
7. Network permission in `AndroidManifest.xml`
8. Activity using `viewModels` delegate and composable content

## How each feature works (details)

### 1) App-level dependency container
- File: `app/src/main/java/com/example/amphibians/di/AppContainer.kt`
- Purpose: hold app-wide dependencies (e.g., `amphibiansRepository`, network client).
- How it works: `AmphibiansApplication` creates a single `AppContainer` instance on startup. Other components (Activity/ViewModel factory) access dependencies through `application as AmphibiansApplication` -> `appContainer`.

### 2) Custom `Application` class
- File: `app/src/main/java/com/example/amphibians/AmphibiansApplication.kt`
- Purpose: initialize `AppContainer` early in app lifecycle.
- How it works: overrides `onCreate()` and assigns `appContainer = AppContainer()`. Manifest must include `android:name=".AmphibiansApplication"` inside the `<application>` tag so the system uses it.

### 3) Repository (data layer)
- Typical location: `app/src/main/java/com/example/amphibians/data/...`
- Purpose: fetch amphibian list (from network or local).
- How it works: exposes a suspend function (or flow) that returns data or throws on error. `AppContainer` provides a repository instance that the ViewModel uses.

### 4) `AmphibiansViewModel`
- File: `userinterface/AmphibiansViewModel.kt`
- Purpose: orchestrate data loading and expose UI state to Composables.
- Key parts:
    - `AmphibiansUiState` (sealed): `Loading`, `Success(List<Amphibian>)`, `Error(String?)`.
    - `provideFactory(repository)` static helper: returns a `ViewModelProvider.Factory` so `viewModels { ... }` can create the ViewModel with repository dependency.
    - `getAmphibians()` (or init block): launches coroutine, sets `uiState` to `Loading`, calls repository, sets `Success` or `Error` accordingly. Exposes `uiState` as `State`/`LiveData` observed by Compose.

### 5) Compose UI (userinterface)
- Files: `AmphibiansApp`, `LoadingScreen`, `ErrorScreen`, `AmphibiansList`, `AmphibianCard`
- How it works:
    - `AmphibiansApp(uiState, onRetry)` is the single entry composable used in `MainActivity.setContent`. It `when`-switches on `uiState`:
        - `Loading` -> shows `LoadingScreen` with a `CircularProgressIndicator`.
        - `Error` -> shows `ErrorScreen` with message + `Retry` button that calls `onRetry`.
        - `Success` -> shows `AmphibiansList(amphibians)`.
    - `AmphibiansList` uses `LazyColumn` to display `AmphibianCard` for each item, handling spacing and performance.
    - `AmphibianCard` renders title, type, image (via Coil), and description using Material typography and padding.

### 6) Image loading with Coil
- How it works: `AsyncImage` or `rememberImagePainter` from Coil is used inside `AmphibianCard` to load remote image URIs asynchronously. Coil handles caching, placeholders, and error images.

### 7) Android manifest and permissions
- File: `app/src/main/AndroidManifest.xml`
- Required entries:
    - `<uses-permission android:name="android.permission.INTERNET" />` for network access.
    - `<application android:name=".AmphibiansApplication" ...>` so the custom application initializes DI container.

### 8) `MainActivity`
- File: `app/src/main/java/com/example/amphibians/MainActivity.kt`
- How it works:
    - Uses `private val viewModel: AmphibiansViewModel by viewModels { ... }` with a factory from `AmphibiansViewModel.provideFactory(app.appContainer.amphibiansRepository)`.
    - Calls `setContent` to host Compose UI. Provides `viewModel.uiState` and `onRetry = { viewModel.getAmphibians() }` to `AmphibiansApp`.
    - Ensures lifecycle-aware ViewModel ownership and proper dependency injection from `AppContainer`.

## Build & run

1. Open the project in Android Studio (Android Studio Otter).
2. Ensure Gradle sync completes. If new classes were added, run *File > Sync Project with Gradle Files*.
3. Build: *Build > Rebuild Project*.
4. Run on emulator / device.

## Troubleshooting

- Unresolved symbol for `AppContainer` or `AmphibiansApplication`: ensure file exists at `app/src/main/java/com/example/amphibians/di/AppContainer.kt` and package matches `com.example.amphibians.di`. Then sync & rebuild.
- UI shows nothing: confirm `MainActivity.setContent` uses `AmphibiansApp` and the ViewModel is loading data; check logs for network errors.
- Image fails: verify INTERNET permission and that image URLs are valid.

## Files to check / edit
- `app/src/main/java/com/example/amphibians/AmphibiansApplication.kt`
- `app/src/main/java/com/example/amphibians/di/AppContainer.kt`
- `app/src/main/java/com/example/amphibians/userinterface/AmphibiansViewModel.kt`
- `app/src/main/java/com/example/amphibians/userinterface/AmphibiansScreen.kt` (composables)
- `app/src/main/java/com/example/amphibians/MainActivity.kt`
- `app/src/main/AndroidManifest.xml`

## Contributing
- Add features in the `di` container, expose them via `AmphibiansApplication`, and update `ViewModel` factories accordingly. Keep UI logic in composables and business logic in repository/ViewModel.

## License
- Add your preferred license file to the repository.
