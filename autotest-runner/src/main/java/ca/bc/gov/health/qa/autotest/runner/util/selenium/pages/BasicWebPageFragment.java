package ca.bc.gov.health.qa.autotest.runner.util.selenium.pages;

import static java.util.Objects.requireNonNull;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ca.bc.gov.health.qa.autotest.runner.util.selenium.SeleniumExpectedConditions;
import ca.bc.gov.health.qa.autotest.runner.util.selenium.SeleniumSession;

/**
 * TODO (AZ) - doc
 */
public class BasicWebPageFragment
implements WebPageFragment
{
    /**
     * TODO (AZ) - doc
     */
    protected final SeleniumSession selenium_;

    /**
     * TODO (AZ) - doc
     */
    protected final By mainLocator_;

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
     *         if either {@code driver} or {@code mainLocator} is {@code null}
     */
    public BasicWebPageFragment(SeleniumSession selenium, By mainLocator)
    {
        selenium_    = requireNonNull(selenium, "Null Selenium session.");
        mainLocator_ = requireNonNull(mainLocator, "Null main locator.");
    }

    /**
     * TODO (AZ) - doc
     */
    @Override
    public void waitForAbsent()
    {
        selenium_.waitUntil(SeleniumExpectedConditions.absenceOfElementLocated(mainLocator_));
    }

    /**
     * TODO (AZ) - doc
     */
    @Override
    public void waitForInvisible()
    {
        selenium_.waitUntil(ExpectedConditions.invisibilityOfElementLocated(mainLocator_));
    }

    /**
     * TODO (AZ) - doc
     */
    @Override
    public void waitForReady()
    {
        selenium_.waitUntil(ExpectedConditions.visibilityOfElementLocated(mainLocator_));
    }
}
