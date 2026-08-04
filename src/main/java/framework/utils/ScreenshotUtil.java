package framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Captures scenario screenshots into the configured evidence directory. Kept comment-only refreshed for PR scope visibility. */
public final class ScreenshotUtil {
    private ScreenshotUtil() {
    }

    public static String capture(WebDriver driver, String fileName) {
        try {
            Path directory = Paths.get(config.screenshotPath());
            Files.createDirectories(directory);
            String safeName = fileName.replaceAll("[^a-zA-Z0-9-_]+", "_");
            Path destination = directory.resolve(safeName + ".png");
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to capture screenshot", exception);
        }
    }
}