package ca.bc.gov.health.qa.autotest.runner.util.testng;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.IClassListener;
import org.testng.IConfigurationListener;
import org.testng.IExecutionListener;
import org.testng.ITestClass;
import org.testng.ITestListener;
import org.testng.ITestResult;

import ca.bc.gov.health.qa.autotest.core.util.capture.Artifact;
import ca.bc.gov.health.qa.autotest.core.util.capture.CaptureEvent;
import ca.bc.gov.health.qa.autotest.core.util.capture.TextArtifact;
import ca.bc.gov.health.qa.autotest.core.util.capture.ThrowableArtifact;
import ca.bc.gov.health.qa.autotest.core.util.context.ExecutionContext;
import ca.bc.gov.health.qa.autotest.core.util.context.LocalContext;
import ca.bc.gov.health.qa.autotest.core.util.io.ProjectInfo;
import ca.bc.gov.health.qa.autotest.core.util.text.TextUtils;
import ca.bc.gov.health.qa.autotest.runner.util.log.ExecutionLogManager;
import ca.bc.gov.health.qa.autotest.runner.util.selenium.SeleniumCapture;
import ca.bc.gov.health.qa.autotest.runner.util.selenium.SeleniumContext;
import ca.bc.gov.health.qa.autotest.runner.util.selenium.SeleniumSession;

/**
 * TODO (AZ) - doc
 */
public class TestListener
implements IClassListener, IConfigurationListener, IExecutionListener, ITestListener
{
    private static final String EVENT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS z";

    private static final Logger LOG = ExecutionLogManager.getLogger();

    private static final Pattern WHITESPACE_SEQUENCE_PATTERN = Pattern.compile("\\s+");

    /**
     * TODO (AZ) - doc
     */
    public TestListener()
    {}

    @Override
    public void beforeConfiguration(ITestResult result)
    {
        resetArtifactBuffer();
    }

    @Override
    public void onBeforeClass(ITestClass testClass)
    {
        LOG.info("Starting test class ({}).", testClass.getName());
    }

    @Override
    public void onConfigurationFailure(ITestResult result)
    {
        handleFailure(result, true);
    }

    @Override
    public void onConfigurationSkip(ITestResult result)
    {
        handleSkip(result, true);
    }

    @Override
    public void onExecutionStart()
    {
        ProjectInfo coreProjectInfo = ProjectInfo.getCoreProjectInfo();
        LOG.info("{} {}", coreProjectInfo.getName(), coreProjectInfo.getVersion());
        LOG.info("TEST EXECUTION START ({})", ExecutionContext.get().getExecutionName());
    }

    @Override
    public void onExecutionFinish()
    {
        LOG.info("TEST EXECUTION FINISH ({})", ExecutionContext.get().getExecutionName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        handleFailure(result, false);
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        handleFailure(result, false);
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        handleSkip(result, false);
    }

    @Override
    public void onTestStart(ITestResult result)
    {
        resetArtifactBuffer();
        LOG.info("TEST START ({}:{})",
                getTestName(result), formatParameters(result.getParameters()));
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        LOG.info("TEST PASS ({}:{})",
                getTestName(result), formatParameters(result.getParameters()));
    }

    private Artifact createInfoArtifact(
            ITestResult result, boolean inConfig, Instant eventTime, int artifactCount)
    {
        String timestamp = DateTimeFormatter
                .ofPattern(EVENT_TIME_PATTERN)
                .withZone(ZoneId.systemDefault())
                .format(eventTime);
        Map<String,String> infoMap = new LinkedHashMap<>();
        infoMap.put("Name",       result.getInstanceName());
        infoMap.put("Method",     result.getName());
        infoMap.put("Parameters", formatParameters(result.getParameters()));
        infoMap.put("Type",       inConfig ? "Configuration" : "Test");
        infoMap.put("Time",       timestamp);
        infoMap.put("Thread ",    Thread.currentThread().getName());
        infoMap.put("Artifacts",  Integer.toString(artifactCount));
        return new TextArtifact("info.txt", TextUtils.formatStringMap(infoMap));
    }

    private String formatParameters(Object[] parameters)
    {
        StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < parameters.length; i++)
        {
            if (i > 0)
            {
                builder.append(", ");
            }
            Object parameter = parameters[i];
            if (parameter != null)
            {
                String value = WHITESPACE_SEQUENCE_PATTERN
                        .matcher(parameter.toString().trim())
                        .replaceAll(" ");
                builder.append(value);
            }
            else
            {
                builder.append("null");
            }
        }
        return builder.append(")").toString();
    }

    private String getTestName(ITestResult result)
    {
        return result.getInstanceName() + ":" + result.getName();
    }

    private void handleFailure(ITestResult result, boolean inConfig)
    {
        Instant eventTime = Instant.now();

        // Log the failure.
        Throwable throwable = result.getThrowable();
        if (throwable == null)
        {
            throwable = new IllegalStateException("Failure without a throwable.");
        }
        LOG.error("{} FAIL ({})", inConfig ? "CONFIG" : "TEST", getTestName(result), throwable);

        // Capture failure artifacts.
        try
        {
            CaptureEvent artifactEvent = CaptureEvent.createCaptureEvent(
                    ExecutionContext.get().getExecutionPaths().getEventPath(),
                    (inConfig ? "config-" : "test-") + "failure",
                    eventTime);
            List<Artifact> artifactList = LocalContext.get().getArtifactBuffer().getArtifactList();
            artifactEvent.addSequentialArtifacts(artifactList);
            artifactEvent.addSummaryArtifact(new ThrowableArtifact("throwable.txt", throwable));
            artifactEvent.addSummaryArtifact(createInfoArtifact(
                    result,
                    inConfig,
                    eventTime,
                    artifactList.size()));
            SeleniumSession selenium = SeleniumContext.get().getSeleniumSession();
            if (selenium != null)
            {
                try
                {
                    artifactEvent.addSummaryArtifact(SeleniumCapture.grabSeleniumInfo(selenium));
                    artifactEvent.addSummaryArtifact(SeleniumCapture.grabPageSource(selenium));
                    artifactEvent.addSummaryArtifact(SeleniumCapture.grabScreenshot(selenium));
                }
                catch (UnsupportedOperationException | WebDriverException e)
                {
                    // NOTE: Allow capturing of artifacts to complete,
                    //       regardless of failures in capturing of Selenium artifacts.
                    LOG.error("Failed to capture Selenium artifacts.", e);
                    artifactEvent.addSummaryArtifact(
                            new ThrowableArtifact("web-capture-failure.txt", e));
                }
            }
            artifactEvent.renderArtifacts();
            LOG.error("Failure event captured ({}).", artifactEvent.getPath());
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Unable to capture failure event.", e);
        }
    }

    private void handleSkip(ITestResult result, boolean inConfig)
    {
        LOG.warn("{} SKIP ({})", inConfig ? "CONFIG" : "TEST", getTestName(result));
    }

    private void resetArtifactBuffer()
    {
        LocalContext.get().getArtifactBuffer().reset();
    }
}
