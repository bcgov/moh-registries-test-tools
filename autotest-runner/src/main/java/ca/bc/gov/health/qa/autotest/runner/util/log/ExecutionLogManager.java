package ca.bc.gov.health.qa.autotest.runner.util.log;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.util.StackLocatorUtil;

import ca.bc.gov.health.qa.autotest.core.util.context.ExecutionContext;

/**
 * TODO (AZ) - doc
 */
public class ExecutionLogManager
{
    private static volatile boolean LOG_PATH_INITIALIZED_ = false;

    /**
     * Returns a logger using the fully qualified name of the calling class as the logger name.
     *
     * <p>This method ensures that the log path is initialized.
     * Invoking this method has a side effect that ensures the run context is initialized first.
     *
     * @return the logger for the calling class
     */
    public static Logger getLogger()
    {
        if (!LOG_PATH_INITIALIZED_)
        {
            initializeLogPath();
        }
        return LogManager.getLogger(StackLocatorUtil.getCallerClass(2));
    }

    /**
     * Initializes the log path, and reconfigures the logger.
     *
     * <p>Invoking this method has a side effect that ensures the run context is initialized first.
     */
    private static synchronized void initializeLogPath()
    {
        if (!LOG_PATH_INITIALIZED_)
        {
            Path logPath = ExecutionContext.get().getExecutionPaths().getLogPath();
            System.setProperty("LOG_DIR", logPath.toString());
            LoggerContext.getContext(false).reconfigure();
            LOG_PATH_INITIALIZED_ = true;
        }
    }

    private ExecutionLogManager()
    {}
}
