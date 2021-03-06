package ru.awesome.shop.ta.framework.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ru.awesome.shop.ta.framework.browser.Browser;
import ru.awesome.shop.ta.framework.logging.Log;
import ru.awesome.shop.ta.utils.ReportPortalManager;

import java.io.File;
import java.util.Objects;

public class TestListener implements ITestListener {
    private static final String MESSAGE = "iTestResult cannot be null.";

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Objects.requireNonNull(iTestResult, MESSAGE);
        Log.info(String.format("Test method %s STARTED.", iTestResult.getMethod().getDescription()));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        Objects.requireNonNull(iTestResult, MESSAGE);
        Log.info(String.format("Test method %s SUCCESSFULLY PASSED", iTestResult.getMethod().getDescription()));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Objects.requireNonNull(iTestResult, MESSAGE);
        Log.info(String.format("Test method %s FAILED.", iTestResult.getMethod().getDescription()));
        File screenshot = Browser.getInstance().takeScreenshot();
        ReportPortalManager.post(screenshot);
        Log.info(String.format("Screenshot %s was saved", screenshot.getName()));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Objects.requireNonNull(iTestResult, MESSAGE);
        Log.info(String.format("Test method %s SKIPPED.", iTestResult.getMethod().getDescription()));
        File screenshot = Browser.getInstance().takeScreenshot();
        ReportPortalManager.post(screenshot);
        Log.info(String.format("Screenshot %s was saved", screenshot.getName()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Objects.requireNonNull(iTestResult, MESSAGE);
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        Objects.requireNonNull(iTestContext, "iTestContext cannot be null.");
        Log.info(String.format("Test %s STARTED.", iTestContext.getName()));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        Objects.requireNonNull(iTestContext, "iTestContext cannot be null.");
        Log.info(String.format("Test %s FINISHED.", iTestContext.getName()));
    }
}
