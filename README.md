# Tip Splitter

One-screen Jetpack Compose app: enter a bill, pick a tip, split it between people.

## Tests

```bash
./gradlew test
```

Outputs:

| What | Where |
| --- | --- |
| JUnit XML reports | `app/build/test-results/testDebugUnitTest/TEST-*.xml` |
| HTML test report | `app/build/reports/tests/testDebugUnitTest/index.html` |
| Screenshots (Roborazzi) | `app/build/outputs/roborazzi/*.png` |

- `TipCalculatorTest`: plain JVM unit tests for the maths.
- `TipSplitterScreenshotTest`: renders the Compose screen with Robolectric (no emulator), interacts with it, and saves PNGs.

`roborazzi.test.record=true` in `gradle.properties` makes every test run write screenshots.
To use them as golden images instead, remove that line, run `./gradlew recordRoborazziDebug` once, then `./gradlew verifyRoborazziDebug`.

## Running on a Mac

### Android Studio (easiest)

1. `brew install --cask android-studio`
2. Open this folder. Studio brings its own JDK and installs the SDK on first launch.
3. Device Manager, create a Pixel device with an arm64 system image (API 35 or 36).
4. Press Run.

### Command line only

```bash
brew install --cask temurin@17 android-commandlinetools
export ANDROID_HOME="$(brew --prefix)/share/android-commandlinetools"
sdkmanager "platform-tools" "emulator" "platforms;android-36" "system-images;android-36;google_apis;arm64-v8a"
avdmanager create avd -n pixel -k "system-images;android-36;google_apis;arm64-v8a" -d pixel_7
$ANDROID_HOME/emulator/emulator -avd pixel &
./gradlew installDebug
$ANDROID_HOME/platform-tools/adb shell am start -n com.example.tipsplitter/.MainActivity
```
