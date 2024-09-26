package ca.bc.gov.health.qa.autotest.runner.util.selenium;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import ca.bc.gov.health.qa.autotest.core.util.capture.BinaryArtifact;
import ca.bc.gov.health.qa.autotest.core.util.capture.TextArtifact;
import ca.bc.gov.health.qa.autotest.core.util.context.LocalContext;
import ca.bc.gov.health.qa.autotest.core.util.text.TextUtils;
import ca.bc.gov.health.qa.autotest.runner.util.log.ExecutionLogManager;

/**
 * TODO (AZ) - doc
 */
public class SeleniumCapture
{
    private static final Logger LOG = ExecutionLogManager.getLogger();

    private SeleniumCapture()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @param selenium
     *        ???
     */
    public static void capturePageSource(SeleniumSession selenium)
    {
        LOG.info("Capturing Selenium page source ...");
        LocalContext.addArtifact(grabPageSource(selenium));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param screenshotTaker
     *        ???
     *
     * @throws UnsupportedOperationException
     *         if {@code screenshotTaker} does not support screenshot capturing
     *
     * @throws WebDriverException
     *         if screenshot capture fails
     */
    public static void captureScreenshot(TakesScreenshot screenshotTaker)
    {
        LOG.info("Capturing Selenium screenshot ...");
        LocalContext.addArtifact(grabScreenshot(screenshotTaker));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param selenium
     *        ???
     */
    public static void captureSeleniumInfo(SeleniumSession selenium)
    {
        LOG.info("Capturing Selenium info ...");
        LocalContext.addArtifact(grabSeleniumInfo(selenium));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param selenium
     *        ???
     *
     * @return ???
     */
    public static TextArtifact grabPageSource(SeleniumSession selenium)
    {
        return new TextArtifact("web-source.html", selenium.getDriver().getPageSource());
    }

    /**
     * TODO (AZ) - doc
     *
     * @param screenshotTaker
     *        ???
     *
     * @return ???
     *
     * @throws UnsupportedOperationException
     *         if {@code screenshotTaker} does not support screenshot capturing
     *
     * @throws WebDriverException
     *         if screenshot capture fails
     */
    public static BinaryArtifact grabScreenshot(TakesScreenshot screenshotTaker)
    {
        byte[] bytes = screenshotTaker.getScreenshotAs(OutputType.BYTES);
        return new BinaryArtifact("web-screenshot.png", bytes);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param selenium
     *        ???
     *
     * @return ???
     */
    public static TextArtifact grabSeleniumInfo(SeleniumSession selenium)
    {
        Map<String,String> infoMap = new LinkedHashMap<>();
        WebDriver driver = selenium.getDriver();
        infoMap.put("URL",   driver.getCurrentUrl());
        infoMap.put("Title", driver.getTitle());
        return new TextArtifact("web-info.txt", TextUtils.formatStringMap(infoMap));
    }
}
