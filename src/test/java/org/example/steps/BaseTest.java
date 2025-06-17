package org.example.steps;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    // Ubah driver menjadi non-static. Setiap instance test akan punya driver sendiri.
    public static AppiumDriver driver;

    // Method ini tidak lagi static
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "emulator-5554"); // Pastikan nama emulator/device sesuai
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "com.example.eatstedi");
        caps.setCapability("appActivity", ".activity.SplashScreenActivity"); // Arahkan ke activity yang benar saat start
        caps.setCapability("noReset", "true"); // Optional: agar tidak install ulang app setiap kali tes

        // Menggunakan path relatif yang sudah kita diskusikan
        String apkPath = System.getProperty("user.dir") + "/apps/app-debug.apk"; // Ganti nama APK jika perlu
        caps.setCapability("app", apkPath);

        // Appium 2.x menggunakan path root, bukan /wd/hub
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // Method ini tidak lagi static
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}