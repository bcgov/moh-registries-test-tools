package ca.bc.gov.health.qa.autotest.core.util.timer;

import java.time.Duration;

/**
 * TODO (AZ) - doc
 */
public class Delay
{
    private Delay()
    {}

    /**
     * TODO (AZ) - doc
     * <p>
     * This method is identical to {@code Thread.sleep}, except that it throws
     * an unchecked exception instead of {@code InterruptedException}.
     *
     * @param duration
     *        ???
     *
     * @throws IllegalStateException
     *         if the current thread is interrupted
     */
    public static void delay(Duration duration)
    {
        try
        {
            Thread.sleep(duration);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException("Delay interrupted.", e);
        }
    }

    /**
     * TODO (AZ) - doc
     * <p>
     * This method is identical to {@code Thread.sleep}, except that it throws
     * an unchecked exception instead of {@code InterruptedException}.
     *
     * @param millis
     *        ???
     *
     * @throws IllegalStateException
     *         if the current thread is interrupted
     */
    public static void delay(long millis)
    {
        delay(Duration.ofMillis(millis));
    }
}
