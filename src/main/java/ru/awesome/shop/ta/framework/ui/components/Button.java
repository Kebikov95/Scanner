package ru.awesome.shop.ta.framework.ui.components;

import ru.awesome.shop.ta.framework.browser.Browser;
import org.openqa.selenium.By;

import java.util.Objects;

public class Button extends CommonPageElement {

    public Button(By locator) {
        Objects.requireNonNull(locator, "Locator can not be null");
        this.locator = locator;
    }

    public void click() {
        waitForPageElementToBeClickable(locator);
        Browser.getInstance().click(locator);
    }

    public String getText() {
        waitForPageElementVisibilityLocated(locator);
        return Browser.getInstance().getText(locator);
    }

    @Override
    public String toString() {
        String buttonName = getText();
        return String.format("Button \"%s\"", buttonName);
    }
}
