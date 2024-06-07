package ca.bc.gov.health.qa.autotest.core.util.context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ca.bc.gov.health.qa.autotest.core.util.io.FileIOUtils;


/**
 * TODO (AZ) - doc
 */
public final class ExecutionPaths
{
    private final Path eventsPath_;
    private final Path executionPath_;
    private final Path logPath_;
    private final Path reportsPath_;
    private final Path temporaryPath_;

    /**
     * Creates a new instance of execution paths.
     *
     * <p>Instances of this class are immutable and safe for use by multiple concurrent threads.
     *
     * @param executionPath
     *        the execution directory path. This directory should already exist on the file system.
     */
    private ExecutionPaths(Path executionPath)
    {
        eventsPath_    = executionPath.resolve("events");
        executionPath_ = executionPath;
        logPath_       = executionPath.resolve("log");
        reportsPath_   = executionPath.resolve("reports");
        temporaryPath_ = executionPath.resolve("tmp");
    }

    /**
     * TODO (AZ) - doc
     *
     * @param parentDirectoryName
     *        ???
     *
     * @param executionName
     *        ???
     *
     * @return ???
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public static ExecutionPaths create(String parentDirectoryName, String executionName)
    throws IOException
    {
        Path parentPath    = FileIOUtils.createSanitizedPath(null, parentDirectoryName);
        Path executionPath = FileIOUtils.createSanitizedPath(parentPath, executionName);
        Files.createDirectories(parentPath);
        Files.createDirectory  (executionPath);
        return new ExecutionPaths(executionPath);
    }

    /**
     * Returns the events directory path.
     *
     * @return the events directory path
     */
    public Path getEventPath()
    {
        return eventsPath_;
    }

    /**
     * Returns the execution directory path.
     *
     * @return the execution directory path
     */
    public Path getExecutionPath()
    {
        return executionPath_;
    }

    /**
     * Returns the log directory path.
     *
     * @return the log directory path
     */
    public Path getLogPath()
    {
        return logPath_;
    }

    /**
     * Returns the reports directory path.
     *
     * @return the reports directory path
     */
    public Path getReportsPath()
    {
        return reportsPath_;
    }

    /**
     * Returns the temporary directory path.
     *
     * @return the temporary directory path
     */
    public Path getTemporaryPath()
    {
        return temporaryPath_;
    }
}
