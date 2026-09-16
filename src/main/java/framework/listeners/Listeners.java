package framework.listeners;

import framework.reports.ExtentManager;
import framework.utils.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Logs high-level TestNG lifecycle events for the automation suite. Kept comment-only refreshed for PR scope visibility. */
public class Listeners implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        Logger.info("Test execution started: " + context.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Logger.info("Test passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Logger.error("Test failed: " + result.getName(), result.getThrowable());
        if (ExtentManager.getTest() != null && result.getThrowable() != null) {
            ExtentManager.getTest().fail(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        Logger.info("Test execution finished: " + context.getName());
    }
}