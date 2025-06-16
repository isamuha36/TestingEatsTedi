package org.example.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        // 'glue' harus menunjuk ke package lengkap tempat step definitions berada
        glue = "org.example.steps",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html", // Menghasilkan laporan HTML yang lebih baik
                "json:target/cucumber-reports/Cucumber.json",
                "junit:target/cucumber-reports/Cucumber.xml"
        },
        // Anda bisa memilih tag yang ingin dijalankan, misal @auth atau @menu
        tags = "@auth or @menu"
)
public class TestRunner {
}