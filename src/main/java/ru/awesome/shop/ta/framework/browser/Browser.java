package ru.awesome.shop.ta.framework.browser;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import ru.awesome.shop.ta.framework.configuration.PropertyManager;
import ru.awesome.shop.ta.framework.logging.Log;
import ru.awesome.shop.ta.framework.ui.elements.HighlightedWebElement;
import ru.awesome.shop.ta.utils.DirectoryGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class Browser implements WrapsDriver {
    private static ThreadLocal<Browser> instance = new ThreadLocal<>();
    private final WebDriver wrappedDriver;
    private final String screenshotDirectoryPath;
    private static final String LOCATOR_NULL_MESSAGE = "LOCATOR cannot be null.";

    private Browser() {
        BrowserType browserType = BrowserType.valueOf(System.getProperty("browser",
                PropertyManager.getBrowserType().toString()));
        String executionProperty = System.getProperty("grid", "false");
        boolean isSeleniumGridEnabled = executionProperty.equals("true");
        screenshotDirectoryPath = DirectoryGenerator.create("./target/screenshots");
        Log.debug("Creating instance of WebDriver for " + browserType);
        wrappedDriver = WebDriverFactory.getWebDriver(browserType, isSeleniumGridEnabled);
        wrappedDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    public static Browser getInstance() {
        Log.debug("Getting instance of browser");
        if (instance.get() == null) {
            instance.set(new Browser());
        }
        return instance.get();
    }

    public void stop() {
        Log.debug("Stopping the browser");
        try {
            if (instance.get() != null) {
                instance.get().getWrappedDriver().quit();
            }
        } finally {
            instance.remove();
        }
    }

    public void navigate(String url) {
        Objects.requireNonNull(url, "URL cannot be null.");
        Log.debug("Navigating to " + url);
        wrappedDriver.get(url);
    }

    public void click(By locator) {
        Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
        Log.debug("Click on " + locator);
        WebElement webElement = wrappedDriver.findElement(locator);
        HighlightedWebElement highlightedWebElement = new HighlightedWebElement(wrappedDriver, webElement);
        highlightedWebElement.click();
    }

    public boolean isSelected(By locator) {
        Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
        WebElement webElement = wrappedDriver.findElement(locator);
        return webElement.isSelected();
    }

    public void select(By locator, String option) {
        Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
        Objects.requireNonNull(option, "OPTION cannot be null.");
        click(locator);
        WebElement element = wrappedDriver.findElement(locator);
        HighlightedWebElement highlightedWebElement = new HighlightedWebElement(wrappedDriver, element);
        Select dropDownList = new Select(highlightedWebElement);
        Log.debug("Selecting " + option);
        dropDownList.selectByVisibleText(option);
    }

    public String getFirstSelectedOption(By locator) {
        Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
        Log.debug("Selecting the first option from " + locator);
        WebElement element = wrappedDriver.findElement(locator);
        HighlightedWebElement highlightedWebElement = new HighlightedWebElement(wrappedDriver, element);
        Select dropDownList = new Select(highlightedWebElement);
        String selectedOptionText = dropDownList.getFirstSelectedOption().getText();
        return selectedOptionText.replace(" ", "").replace("\n", "");
    }

    public void sendKeys(By locator, CharSequence... keysToSend) {
        Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
        Objects.requireNonNull(keysToSend, "KEYS TO SEND cannot be null.");
        Log.debug("Send text " + keysToSend + " to " + locator);
        WebElement webElement = wrappedDriver.findElement(locator);
        HighlightedWebElement highlightedWebElement = new HighlightedWebElement(wrappedDriver, webElement);
        highlightedWebElement.sendKeys(keysToSend);
    }

    public void clear(By locator) {
        Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
        Log.debug("Clearing field " + locator);
        WebElement webElement = wrappedDriver.findElement(locator);
        HighlightedWebElement highlightedWebElement = new HighlightedWebElement(wrappedDriver, webElement);
        highlightedWebElement.clear();
    }

    public void reloadPage() {
        Log.debug("Reloading page " + wrappedDriver.getCurrentUrl());
        wrappedDriver.navigate().refresh();
    }

    public String getText(By locator) {
        Objects.requireNonNull(locator, LOCATOR_NULL_MESSAGE);
        Log.debug("Getting the text of WebElement located by " + locator);
        WebElement webElement = wrappedDriver.findElement(locator);
        HighlightedWebElement highlightedWebElement = new HighlightedWebElement(wrappedDriver, webElement);
        return highlightedWebElement.getText().trim();
    }

    public File takeScreenshot() {
        String screenshotPath = String.format("%s/%s.png", screenshotDirectoryPath, System.nanoTime());
        File screenshotFile = ((TakesScreenshot) wrappedDriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotFile, new File(screenshotPath));
            Log.debug("Screenshot has been saved as file: " + screenshotPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshotFile;
    }

    public String openNewTab() {
        Log.debug("Opening new tab");
        ArrayList<String> oldTabs = new ArrayList<>(wrappedDriver.getWindowHandles());
        JavascriptExecutor executor = (JavascriptExecutor) wrappedDriver;
        executor.executeScript("window.open('','_blank');");
        ArrayList<String> newTabs = new ArrayList<>(wrappedDriver.getWindowHandles());
        newTabs.removeAll(oldTabs);
        return newTabs.get(0);
    }

    public void switchTab(String windowHandle) {
        Objects.requireNonNull(windowHandle, "Window handle cannot be null.");
        Log.debug("Switching to the next tab " + windowHandle);
        wrappedDriver.switchTo().window(windowHandle);
    }

    public void switchTabByIndex(int index) {
        Objects.requireNonNull(index, "INDEX cannot be null.");
        Log.debug("Switching to the tab with index " + index);
        List<String> allTabs = new ArrayList<>(wrappedDriver.getWindowHandles());
        wrappedDriver.switchTo().window(allTabs.get(index));
    }

    public void closeTab(String windowHandle) {
        Objects.requireNonNull(windowHandle, "Window handle cannot be null.");
        Log.debug("Closing the next tab " + windowHandle);
        switchTab(windowHandle);
        wrappedDriver.close();
    }

    public String getPageTitle() {
        return wrappedDriver.getTitle();
    }

    @Override
    public WebDriver getWrappedDriver() {
        Log.debug("Getting WebDriver");
        return wrappedDriver;
    }
}
