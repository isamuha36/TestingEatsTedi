# Android SDK Environment Setup

## Problem
The error "Neither ANDROID_HOME nor ANDROID_SDK_ROOT environment variable was exported" occurs because Appium cannot locate your Android SDK installation.

## Solution

### Step 1: Find Your Android SDK Location
Common Android SDK installation paths:
- `C:\Users\[USERNAME]\AppData\Local\Android\Sdk` (Android Studio default)
- `C:\Android\Sdk`
- `C:\Program Files\Android\Sdk`

### Step 2: Set Environment Variables

#### Option A: Using the Batch Script (Recommended)
1. Run `setup-android-env.bat` as Administrator
2. Edit the script to use your actual Android SDK path
3. Restart your IDE and command prompt

#### Option B: Manual Setup
1. Open System Properties → Advanced → Environment Variables
2. Add these system variables:
   - `ANDROID_HOME` = `C:\Users\[USERNAME]\AppData\Local\Android\Sdk`
   - `ANDROID_SDK_ROOT` = `C:\Users\[USERNAME]\AppData\Local\Android\Sdk`
3. Add to PATH:
   - `%ANDROID_HOME%\platform-tools`
   - `%ANDROID_HOME%\tools`
   - `%ANDROID_HOME%\tools\bin`

### Step 3: Verify Setup
Open a new command prompt and run:
```cmd
echo %ANDROID_HOME%
echo %ANDROID_SDK_ROOT%
adb version
```

### Step 4: Additional Requirements
Make sure you have:
1. **Android Studio** installed with SDK
2. **Android Emulator** running (or physical device connected)
3. **Appium Server** running on port 4723
4. **USB Debugging** enabled (for physical devices)

### Step 5: Start Appium Server
```cmd
appium
```
Or if using Appium Desktop, start it from the application.

### Step 6: Verify Device Connection
```cmd
adb devices
```
Should show your emulator or connected device.

## Troubleshooting

### If you still get errors:
1. **Restart your computer** after setting environment variables
2. **Check Android SDK Manager** - ensure you have:
   - Android SDK Platform-Tools
   - Android SDK Build-Tools
   - Android Emulator
   - System Images for your target Android version

3. **Verify emulator is running**:
   ```cmd
   emulator -list-avds
   emulator -avd [AVD_NAME]
   ```

4. **Check Appium Doctor**:
   ```cmd
   npm install -g appium-doctor
   appium-doctor --android
   ```

### Common Issues:
- **Path contains spaces**: Use quotes around paths
- **Multiple Android SDK installations**: Ensure variables point to the correct one
- **Permissions**: Run command prompt as Administrator when setting variables
- **IDE cache**: Restart IntelliJ IDEA after setting environment variables

## Test Configuration Notes
Your current test configuration in `BaseTest.java` looks correct:
- Device: `emulator-5554`
- App package: `com.example.eatstedi`
- Automation: `UiAutomator2`

Make sure your emulator is running and accessible via `adb devices` before running tests.