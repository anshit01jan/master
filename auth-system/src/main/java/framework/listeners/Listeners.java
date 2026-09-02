package framework.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import framework.drivers.DriverManager;
import framework.reports.ExtentManager;
import framework.utils.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Listeners implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        Logger.info("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        Logger.info("Test Suite Finished: " + context.getName());
        ExtentManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testDescription = result.getMethod().getDescription() != null ?
                                 result.getMethod().getDescription() : testName;

        ExtentManager.startTest(testName, testDescription);
        Logger.info("Test Started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Logger.info("Test Passed: " + testName);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test Passed: " + testName);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Logger.error("Test Failed: " + testName);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, "Test Failed: " + testName);
            test.log(Status.FAIL, result.getThrowable());

            String screenshotPath = captureScreenshot(testName);
            if (screenshotPath != null) {
                try {
                    test.addScreenCaptureFromPath(screenshotPath, "Screenshot on Failure");
                } catch (Exception e) {
                    Logger.error("Failed to attach screenshot: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Logger.warn("Test Skipped: " + testName);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test Skipped: " + testName);
            test.log(Status.SKIP, result.getThrowable());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Logger.warn("Test Failed Within Success Percentage: " + result.getMethod().getMethodName());
    }

    private String captureScreenshot(String testName) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            Logger.warn("Driver is null, cannot capture screenshot");
            return null;
        }

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotName = testName + "_" + timestamp + ".png";
            String screenshotDir = "target/screenshots";

            File dir = new File(screenshotDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String screenshotPath = screenshotDir + "/" + screenshotName;

            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);

            Files.copy(source.toPath(), destination.toPath());

            Logger.info("Screenshot captured: " + screenshotPath);
            return screenshotPath;

        } catch (IOException e) {
            Logger.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
