package ca.bc.gov.health.qa.autotest.runner.util.selenium;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * TODO (AZ) - doc
 */
public class SeleniumSession
implements AutoCloseable, SearchContext, TakesScreenshot
{
    private final WebDriver     driver_;
    private final WebDriverWait wait_;

    private Duration waitInterval_ = Duration.ofMillis(250);
    private Duration waitTimeout_  = Duration.ofSeconds(30);

    /**
     * TODO (AZ) - doc
     *
     * @param driver
     *        ???
     *
     * @throws NullPointerException
     *         if the web driver is {@code null}
     */
    public SeleniumSession(WebDriver driver)
    {
        driver_ = requireNonNull(driver, "Null web driver.");
        wait_   = new WebDriverWait(driver_, waitTimeout_, waitInterval_);
    }

    /**
     * TODO (AZ) - doc
     */
    public void bringToFront()
    {
        driver_.switchTo().window(driver_.getWindowHandle());
    }

    /**
     * Closes this Selenium session, quitting the underlying web driver.
     */
    @Override
    public void close()
    {
        driver_.quit();
    }

    /**
     * Returns a new Chrome Selenium session.
     *
     * @return a new Chrome Selenium session
     */
    public static SeleniumSession createChromeSeleniumSession()
    {
        return createChromeSeleniumSession(false);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param enableHeadless
     *        ???
     *
     * @return ???
     */
    public static SeleniumSession createChromeSeleniumSession(boolean enableHeadless)
    {
        return new SeleniumSession(new ChromeDriver(createChromeOptions(enableHeadless)));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public WebElement click(By locator)
    {
        WebElement element = findElement(locator);
        element.click();
        return element;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @return ???
     */
    public WebElement clickByCss(String cssSelector)
    {
        return click(By.cssSelector(cssSelector));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @param value
     *        ???
     *
     * @return ???
     */
    public WebElement fillField(By locator, CharSequence value)
    {
        WebElement element = findElement(locator);
        SeleniumUtils.fillField(element, value);
        return element;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @param value
     *        ???
     *
     * @return ???
     */
    public WebElement fillFieldByCss(String cssSelector, CharSequence value)
    {
        return fillField(By.cssSelector(cssSelector), value);
    }

    /**
     * TODO (AZ) - doc
     *
     * <p>This is a convenience method equivalent to:
     * <pre>{@code
     *   getDriver().findElement(locator);
     * }</pre>
     *
     * @param locator
     *        ???
     *
     * @return ???
     *
     * @throws NoSuchElementException
     *         if no matching elements are found
     */
    @Override
    public WebElement findElement(By locator)
    {
        return getDriver().findElement(locator);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @return ???
     */
    public WebElement findElementByCss(String cssSelector)
    {
        return findElement(By.cssSelector(cssSelector));
    }

    /**
     * TODO (AZ) - doc
     *
     * <p>This is a convenience method equivalent to:
     * <pre>{@code
     *   getDriver().findElements(locator);
     * }</pre>
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    @Override
    public List<WebElement> findElements(By locator)
    {
        return getDriver().findElements(locator);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @return ???
     */
    public List<WebElement> findElementsByCss(String cssSelector)
    {
        return findElements(By.cssSelector(cssSelector));
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public WebDriver getDriver()
    {
        return driver_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     *
     * @throws UnsupportedOperationException
     *         if this session does not support JavaScript
     */
    public JavascriptExecutor getJavascriptExecutor()
    {
        JavascriptExecutor javascriptExecutor;
        if (driver_ instanceof JavascriptExecutor)
        {
            javascriptExecutor = (JavascriptExecutor) driver_;
        }
        else
        {
            throw new UnsupportedOperationException("JavaScript is not supported.");
        }
        return javascriptExecutor;
    }

    /**
     * TODO (AZ) - doc
     *
     * @throws UnsupportedOperationException
     *         if this session does not support screenshot capturing
     *
     * @throws WebDriverException
     *         if screenshot capture fails
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target)
    throws WebDriverException
    {
        X screenshot;
        if (driver_ instanceof TakesScreenshot)
        {
            screenshot = ((TakesScreenshot) driver_).getScreenshotAs(target);
        }
        else
        {
            throw new UnsupportedOperationException("Screenshot capturing is not supported.");
        }
        return screenshot;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public Duration getWaitInterval()
    {
        return waitInterval_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public Duration getWaitTimeout()
    {
        return waitTimeout_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public boolean grabElementPresent(By locator)
    {
        return SeleniumUtils.grabElementPresent(this, locator);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @return ???
     */
    public boolean grabElementPresentByCss(String cssSelector)
    {
        return grabElementPresent(By.cssSelector(cssSelector));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public boolean grabElementVisible(By locator)
    {
        return SeleniumUtils.grabElementVisible(this, locator);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @return ???
     */
    public boolean grabElementVisibleByCss(String cssSelector)
    {
        return grabElementVisible(By.cssSelector(cssSelector));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public String grabText(By locator)
    {
        return findElement(locator).getText();
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @return ???
     */
    public String grabTextByCss(String cssSelector)
    {
        return grabText(By.cssSelector(cssSelector));
    }

    /**
     * Scrolls an element's ancestor containers such that the element is
     * visible to the user.
     * <p>
     * This method invokes JavaScript {@code scrollIntoView}
     * using center vertical (block) alignment.
     *
     * @param element
     *        ???
     *
     * @throws UnsupportedOperationException
     *         if this session does not support JavaScript
     */
    public void scrollIntoView(WebElement element)
    {
        getJavascriptExecutor().executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public WebElement searchElement(By locator)
    {
        return SeleniumUtils.searchElement(this, locator);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param cssSelector
     *        ???
     *
     * @return ???
     */
    public WebElement searchElementByCss(String cssSelector)
    {
        return searchElement(By.cssSelector(cssSelector));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param waitInterval
     *        ???
     */
    public void setWaitInterval(Duration waitInterval)
    {
        waitInterval_ = waitInterval;
        wait_.pollingEvery(waitInterval_);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param waitTimeout
     *        ???
     */
    public void setWaitTimeout(Duration waitTimeout)
    {
        waitTimeout_ = waitTimeout;
        wait_.withTimeout(waitTimeout_);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param <T>
     *        ???
     *
     * @param expectedCondition
     *        ???
     *
     * @return ???
     *
     * @throws TimeoutException
     *         if the timeout expires
     */
    public <T> T waitUntil(ExpectedCondition<T> expectedCondition)
    {
        return wait_.until(expectedCondition);
    }

    /**
     * Returns new Chrome options.
     *
     * @param enableHeadless
     *        ???
     *
     * @return new Chrome options
     */
    private static ChromeOptions createChromeOptions(boolean enableHeadless)
    {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        if (enableHeadless)
        {
            options.addArguments("--headless");
        }
        return options;
    }
}
