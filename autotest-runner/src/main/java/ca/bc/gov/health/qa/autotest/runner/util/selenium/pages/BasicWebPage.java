package ca.bc.gov.health.qa.autotest.runner.util.selenium.pages;

import java.net.URI;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ca.bc.gov.health.qa.autotest.runner.util.selenium.SeleniumExpectedConditions;
import ca.bc.gov.health.qa.autotest.runner.util.selenium.SeleniumSession;

/**
 * TODO (AZ) - doc
 */
public class BasicWebPage
extends    BasicWebPageFragment
implements WebPage
{
    /**
     * TODO (AZ) - doc
     */
    protected final String expectedTitle_;

    /**
     * TODO (AZ) - doc
     */
    protected final URI uri_;

    /**
     * TODO (AZ) - doc
     *
     * @param selenium
     *        ???
     *
     * @param mainLocator
     *        ???
     *
     * @throws NullPointerException
     *         if either {@code selenium} or {@code mainLocator} is {@code null}
     */
    public BasicWebPage(SeleniumSession selenium, By mainLocator)
    {
        this(selenium, mainLocator, null);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param selenium
     *        ???
     *
     * @param mainLocator
     *        ???
     *
     * @param expectedTitle
     *        ???
     *
     * @throws NullPointerException
     *         if either {@code selenium} or {@code mainLocator} is {@code null}
     */
    public BasicWebPage(SeleniumSession selenium, By mainLocator, String expectedTitle)
    {
        this(selenium, mainLocator, expectedTitle, null);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param selenium
     *        ???
     *
     * @param mainLocator
     *        ???
     *
     * @param expectedTitle
     *        ??? {@code null} permitted
     *
     * @param uri
     *        ??? {@code null} permitted
     *
     * @throws NullPointerException
     *         if either {@code selenium} or {@code mainLocator} is {@code null}
     */
    public BasicWebPage(SeleniumSession selenium, By mainLocator, String expectedTitle, URI uri)
    {
        super(selenium, mainLocator);
        expectedTitle_ = expectedTitle;
        uri_           = uri;
    }

    /**
     * TODO (AZ) - doc
     *
     * TODO (AZ) - mention {@link #waitForReady()}
     *
     * @throws UnsupportedOperationException
     *         if the page URL is not specified
     */
    @Override
    public void openWebPage()
    {
        if (uri_ != null)
        {
            selenium_.getDriver().get(uri_.toString());
            waitForReady();
        }
        else
        {
            throw new UnsupportedOperationException("Page URL is not specified.");
        }
    }

    /**
     * TODO (AZ) - doc
     */
    @Override
    public void waitForAbsent()
    {
        super.waitForAbsent();
        if (expectedTitle_ != null)
        {
            selenium_.waitUntil(
                    ExpectedConditions.not(ExpectedConditions.titleContains(expectedTitle_)));
        }
    }

    /**
     * TODO (AZ) - doc
     */
    @Override
    public void waitForReady()
    {
        selenium_.waitUntil(SeleniumExpectedConditions.pageToBeReady());
        super.waitForReady();
        if (expectedTitle_ != null)
        {
            selenium_.waitUntil(ExpectedConditions.titleContains(expectedTitle_));
        }
    }
}
