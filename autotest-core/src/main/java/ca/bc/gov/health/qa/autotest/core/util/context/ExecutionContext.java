package ca.bc.gov.health.qa.autotest.core.util.context;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * TODO (AZ) - doc
 * ?? - initializes execution path, etc.
 *
 * * TODO (AZ) - add a note that execution context should not reference the logger.
 *
 * <p>The {@link #get()} method returns the singleton instance of the
 * {@code ExecutionContext}, initializing it if necessary.
 */
public class ExecutionContext
{
    private static final String EXECUTION_NAME_PATTERN    = "'execution'-yyyy.MM.dd-HH.mm.ss";
    private static final String EXECUTIONS_DIRECTORY_NAME = "executions";

    private static class SingletonHolder
    {
        private static final ExecutionContext INSTANCE;
        static
        {
            try
            {
                INSTANCE = ExecutionContext.create();
            }
            catch (IOException e)
            {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private final String         executionName_;
    private final ExecutionPaths executionPaths_;
    private final Instant        executionStartTime_;

    /**
     * Creates a new execution context.
     *
     * @param executionName
     *        the name of the execution
     *
     * @param executionStartTime
     *        the execution start time instant
     *
     * @param executionPaths
     *        the execution paths
     */
    private ExecutionContext(
            String executionName, Instant executionStartTime, ExecutionPaths executionPaths)
    {
        executionName_      = executionName;
        executionPaths_     = executionPaths;
        executionStartTime_ = executionStartTime;
    }

    /**
     * Creates a new initialized instance of the execution context.
     *
     * @return a new initialized instance of the execution context
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    private static ExecutionContext create()
    throws IOException
    {
        Instant executionStartTime = Instant.now();
        String executionName = DateTimeFormatter
                .ofPattern(EXECUTION_NAME_PATTERN)
                .withZone(ZoneId.systemDefault())
                .format(executionStartTime);
        ExecutionPaths executionPaths =
                ExecutionPaths.create(EXECUTIONS_DIRECTORY_NAME, executionName);
        return new ExecutionContext(executionName, executionStartTime, executionPaths);
    }

    /**
     * Returns the singleton instance of the execution context, initializing it if necessary.
     *
     * @return the singleton instance of the execution context
     */
    public static ExecutionContext get()
    {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Returns the name of this execution.
     *
     * @return the name of this execution
     */
    public String getExecutionName()
    {
        return executionName_;
    }

    /**
     * Returns the execution paths.
     *
     * @return the execution paths
     */
    public ExecutionPaths getExecutionPaths()
    {
        return executionPaths_;
    }

    /**
     * Returns the execution start time instant.
     *
     * @return the execution start time instant
     */
    public Instant getExecutionStartTime()
    {
        return executionStartTime_;
    }
}
