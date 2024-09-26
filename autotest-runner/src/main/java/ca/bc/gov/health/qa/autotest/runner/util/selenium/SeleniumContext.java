package ca.bc.gov.health.qa.autotest.runner.util.selenium;

/**
 * TODO (AZ) - doc
 *
 * <p>The {@link #get()} method returns the thread local instance of the
 * {@code SeleniumContext}, initializing it if necessary.
 */
public class SeleniumContext
{
    private static final ThreadLocal<SeleniumContext> THREAD_LOCAL_INSTANCE =
            ThreadLocal.withInitial(() -> new SeleniumContext());

    private SeleniumSession seleniumSession_ = null;

    /**
     * Creates a new Selenium context.
     */
    private SeleniumContext()
    {}

    /**
     * Returns the thread local instance of the context, initializing it if necessary.
     *
     * @return the thread local instance of the context
     */
    public static SeleniumContext get()
    {
        return THREAD_LOCAL_INSTANCE.get();
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public SeleniumSession getSeleniumSession()
    {
        return seleniumSession_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param seleniumSession
     *        ???
     */
    public void setSeleniumSession(SeleniumSession seleniumSession)
    {
        seleniumSession_ = seleniumSession;
    }
}
