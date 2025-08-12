package ca.bc.gov.health.qa.autotest.runner.util.selenium;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import ca.bc.gov.health.qa.autotest.runner.util.log.ExecutionLogManager;

/**
 * TODO (AZ) - doc
 */
public class SeleniumExpectedConditions
{
    private static final Logger LOG = ExecutionLogManager.getLogger();

    private SeleniumExpectedConditions()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public static ExpectedCondition<Boolean> absenceOfElementLocated(final By locator)
    {
        return new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                return driver.findElements(locator).isEmpty();
            }

            @Override
            public String toString()
            {
                return "absence of element located by: " + locator;
            }
        };
    }

    /**
     * TODO (AZ) - doc
     *
     * @param parentLocator
     *        ???
     *
     * @param childLocator
     *        ???
     *
     * @return ???
     */
    public static ExpectedCondition<Boolean> invisibilityOfNestedElementLocated(
            By parentLocator, By childLocator)
    {
        return new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                WebElement element = driver.findElement(parentLocator).findElement(childLocator);
                return !element.isDisplayed();
            }

            @Override
            public String toString()
            {
                return String.format(
                        "invisibility of elements located by  %s -> %s",
                        parentLocator,
                        childLocator);
            }
        };
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @param className
     *        ???
     *
     * @return ???
     */
    public static ExpectedCondition<WebElement> presenceOfElementLocatedWithoutClass(
            final By locator, final String className)
    {
        return new ExpectedCondition<WebElement>()
        {
            @Override
            public WebElement apply(WebDriver driver)
            {
                WebElement element = SeleniumUtils.searchElement(driver, locator);
                if (element != null)
                {
                    try
                    {
                        if (SeleniumUtils.grabElementClassSet(element).contains(className))
                        {
                            element = null;
                        }
                    }
                    catch (StaleElementReferenceException e)
                    {
                        // Handle the element being refreshed by catching this exception
                        // and reporting the condition as not satisfied (yet).
                        element = null;
                        LOG.warn("Element refreshed ({}).", locator);
                    }
                }
                return element;
            }

            @Override
            public String toString()
            {
                return "class absence (" + className + ") in element located (" + locator + ")";
            }
        };
    }

    /**
     * TODO (AZ) - doc
     *
     * @param locator
     *        ???
     *
     * @param className
     *        ???
     *
     * @return ???
     */
    public static ExpectedCondition<WebElement> presenceOfElementLocatedWithClass(
            final By locator, final String className)
    {
        return new ExpectedCondition<WebElement>()
        {
            @Override
            public WebElement apply(WebDriver driver)
            {
                WebElement element = SeleniumUtils.searchElement(driver, locator);
                if (element != null)
                {
                    try
                    {
                        if (!SeleniumUtils.grabElementClassSet(element).contains(className))
                        {
                            element = null;
                        }
                    }
                    catch (StaleElementReferenceException e)
                    {
                        // Handle the element being refreshed by catching this exception
                        // and reporting the condition as not satisfied (yet).
                        element = null;
                        LOG.warn("Element refreshed ({}).", locator);
                    }
                }
                return element;
            }

            @Override
            public String toString()
            {
                return "class presence (" + className + ") in element located (" + locator + ")";
            }
        };
    }

    /**
     * An expectation for checking that the page has loaded and is ready
     * (i.e. the document ready state to be complete).
     *
     * @return an expected condition, which when satisfied returns
     *         {@code true} when the page has loaded and is ready
     */
    public static ExpectedCondition<Boolean> pageToBeReady()
    {
        return new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                return jsExecutor.executeScript("return document.readyState").equals("complete");
            }

            @Override
            public String toString()
            {
                return "page to be ready";
            }
        };
    }

    /**
     * TODO (AZ) - doc
     *
     * @param parentLocator
     *        ???
     *
     * @param childLocator
     *        ???
     *
     * @return ???
     */
    public static ExpectedCondition<Boolean> visibilityOfNestedElementLocated(
            By parentLocator, By childLocator)
    {
        return new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                WebElement element = driver.findElement(parentLocator).findElement(childLocator);
                return element.isDisplayed();
            }

            @Override
            public String toString()
            {
                return String.format(
                        "visibility of elements located by  %s -> %s",
                        parentLocator,
                        childLocator);
            }
        };
    }
}
