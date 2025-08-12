package ca.bc.gov.health.qa.autotest.core.util.capture;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * TODO (AZ) - doc
 */
public class ThrowableArtifact
extends TextArtifact
{
    private final Throwable throwable_;

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @param throwable
     *        ???
     */
    public ThrowableArtifact(String name, Throwable throwable)
    {
        super(name, null);
        requireNonNull(throwable, "Null throwable.");
        throwable_ = throwable;
    }

    @Override
    public String renderText()
    {
        return getStackTrace(throwable_);
    }

    private static String getStackTrace(Throwable throwable)
    {
        String stackTrace;
        try (StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter, true);)
        {
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            stackTrace = stringWriter.toString();
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to retrieve stack trace.", e);
        }
        return stackTrace;
    }
}
