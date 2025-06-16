@echo off
echo Setting up Android SDK environment variables...

REM Replace this path with your actual Android SDK installation path
REM Common locations:
REM - C:\Users\%USERNAME%\AppData\Local\Android\Sdk
REM - C:\Android\Sdk
REM - C:\Program Files\Android\Sdk

set ANDROID_SDK_PATH=C:\Users\%USERNAME%\AppData\Local\Android\Sdk

echo Setting ANDROID_HOME to %ANDROID_SDK_PATH%
setx ANDROID_HOME "%ANDROID_SDK_PATH%"

echo Setting ANDROID_SDK_ROOT to %ANDROID_SDK_PATH%
setx ANDROID_SDK_ROOT "%ANDROID_SDK_PATH%"

echo Adding Android tools to PATH...
setx PATH "%PATH%;%ANDROID_SDK_PATH%\platform-tools;%ANDROID_SDK_PATH%\tools;%ANDROID_SDK_PATH%\tools\bin"

echo.
echo Environment variables have been set. Please restart your IDE and command prompt.
echo.
echo To verify the setup, run these commands in a new command prompt:
echo   echo %%ANDROID_HOME%%
echo   echo %%ANDROID_SDK_ROOT%%
echo   adb version
echo.
pause