package ca.bc.gov.health.qa.alm2xray;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.bc.gov.health.qa.alm2xray.test.TestCase;
import ca.bc.gov.health.qa.alm2xray.test.TestStep;
import ca.bc.gov.health.qa.autotest.core.util.data.CsvUtils;
import ca.bc.gov.health.qa.autotest.core.util.data.TableUtils;
import ca.bc.gov.health.qa.autotest.core.util.io.ProjectInfo;
import ca.bc.gov.health.qa.autotest.core.util.security.UserCredentials;

/**
 * TODO (AZ) - doc
 */
public class Alm2XrayConverter
{
    private static final Logger LOG = LogManager.getLogger();

    private static final ProjectInfo PROJECT_INFO =
            ProjectInfo.create(Alm2XrayConverter.class, "maven-project.properties");

    private final AlmRestClient almRestClient_;

    /**
     * TODO (AZ) - doc
     */
    public Alm2XrayConverter()
    {
        this(null);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param uri
     *        ???
     */
    public Alm2XrayConverter(URI uri)
    {
        LOG.info("{} {}", PROJECT_INFO.getName(), PROJECT_INFO.getVersion());
        if (uri != null)
        {
            almRestClient_ = new AlmRestClient(uri);
        }
        else
        {
            almRestClient_ = null;
        }
    }

    /**
     * TODO (AZ) - doc
     *
     * @param testCaseCollection
     *        ???
     *
     * @return ???
     */
    public List<List<String>> convertToXray(Collection<TestCase> testCaseCollection)
    {
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("TCID", "Summary", "Description", "Action", "Result"));
        int testCaseNumber = 0;
        for (TestCase testCase : testCaseCollection)
        {
            testCaseNumber++;
            int testStepNumber = 0;
            for (TestStep testStep : testCase.getTestStepList())
            {
                testStepNumber++;
                data.add(List.of(
                        "" + testCaseNumber,
                        testStepNumber == 1 ? testCase.getName() : "",
                        testStepNumber == 1 ? testCase.getDescription() : "",
                        testStep.getAction(),
                        testStep.getExpectedResult()));
            }
        }
        return TableUtils.validateTabularData(data);
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public static ProjectInfo getProjectInfo()
    {
        return PROJECT_INFO;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param outputPath
     *        ???
     *
     * @param forceOverwrite
     *        ???
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public void output(Path outputPath, boolean forceOverwrite)
    throws IOException
    {
        // FIXME - POC
        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList.add(TestCase
                .builder()
                .name("POC-1")
                .description("This is P.O.C. #1")
                .step("Foo 1.1", "Bar 1.1")
                .build());
        testCaseList.add(TestCase
                .builder()
                .name("POC-2")
                .description("This is P.O.C. #2")
                .step(new TestStep("Foo 2.1", "Bar 2.1"))
                .step("Foo 2.2", "Bar 2.2")
                .build());
        testCaseList.add(TestCase
                .builder()
                .name("POC-3")
                .description("This is P.O.C. #3")
                .step("Foo 3.1", "Bar 3.1")
                .step("Foo 3.2", "Bar 3.2")
                .step("Foo 3.3", "Bar 3.3")
                .build());
        CsvUtils.writeCsvFile(convertToXray(testCaseList), outputPath, forceOverwrite);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param domainName
     *        ???
     *
     * @param projectName
     *        ???
     *
     * @param credentials
     *        ???
     *
     * @throws InterruptedException
     *         if the current thread is interrupted
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public void verifyAlmProjectAccess(
            String domainName, String projectName, UserCredentials credentials)
    throws InterruptedException,
           IOException
    {
        // FIXME
        almRestClient_.login(credentials);
        
        
    }
}
