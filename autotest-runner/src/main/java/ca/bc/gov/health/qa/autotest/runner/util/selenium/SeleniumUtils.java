package ca.bc.gov.health.qa.autotest.runner.util.selenium;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * TODO (AZ) - doc
 */
public class SeleniumUtils
{
    private SeleniumUtils()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @param element
     *        ???
     *
     * @param value
     *        ???
     */
    public static void fillField(WebElement element, CharSequence value)
    {
        element.clear();
        element.sendKeys(value);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param element
     *        ???
     *
     * @return ???
     */
    public static Set<String> grabElementClassSet(WebElement element)
    {
        Set<String> elementClassSet = new HashSet<>();
        String className = element.getDomProperty("className");
        if (className != null)
        {
            for(String classNameItem : className.split("\\s+"))
            {
                elementClassSet.add(classNameItem);
            }
        }
        return elementClassSet;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param context
     *        ???
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public static boolean grabElementPresent(SearchContext context, By locator)
    {
        return !context.findElements(locator).isEmpty();
    }

    /**
     * TODO (AZ) - doc
     *
     * @param context
     *        ???
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public static boolean grabElementVisible(SearchContext context, By locator)
    {
        boolean elementDisplayed;
        WebElement element = searchElement(context, locator);
        if (element != null)
        {
            elementDisplayed = element.isDisplayed();
        }
        else
        {
            elementDisplayed = false;
        }
        return elementDisplayed;
    }

    /**
     * TODO (AZ) - doc
     *           - searches for the first element in a specified search context.
     *
     * @param context
     *        ???
     *
     * @param locator
     *        ???
     *
     * @return ???
     */
    public static WebElement searchElement(SearchContext context, By locator)
    {
        List<WebElement> elementList = context.findElements(locator);
        WebElement element;
        if (!elementList.isEmpty())
        {
            element = elementList.get(0);
        }
        else
        {
            element = null;
        }
        return element;
    }

}
