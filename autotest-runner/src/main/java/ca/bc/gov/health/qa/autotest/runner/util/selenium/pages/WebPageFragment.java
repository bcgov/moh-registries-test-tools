package ca.bc.gov.health.qa.autotest.runner.util.selenium.pages;

/**
 * Represents a fragment of a web page.
 */
public interface WebPageFragment
{
    /**
     * Waits until this page fragment is absent.
     */
    public void waitForAbsent();

    /**
     * Waits until this page fragment is no longer visible.
     */
    public void waitForInvisible();

    /**
     * Waits until this page fragment is ready.
     */
    public void waitForReady();
}
