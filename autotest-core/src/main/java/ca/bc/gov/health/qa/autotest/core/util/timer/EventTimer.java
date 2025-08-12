package ca.bc.gov.health.qa.autotest.core.util.timer;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;

/**
 * TODO (AZ) - doc
 *
 * <p>TODO (AZ) - add a note that duration may not be equal to the difference (endTime - time).
 */
public class EventTimer
{
    private String  name_;
    private long    startNanos_;
    private Instant startTime_;

    /**
     * TODO (AZ) - doc
     */
    public EventTimer()
    {
        this("");
    }

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @throws NullPointerException
     *         if {@code name} is {@code null}
     */
    public EventTimer(String name)
    {
        internalSetName(name);
        internalStart();
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public TimedEvent measure()
    {
        long     endNanos = System.nanoTime();
        Instant  endTime  = Instant.now();
        Duration duration = Duration.ofNanos(endNanos - startNanos_);
        return new TimedEvent(name_, duration, startTime_, endTime);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @throws NullPointerException
     *         if {@code name} is {@code null}
     */
    public void setName(String name)
    {
        internalSetName(name);
    }

    /**
     * TODO (AZ) - doc
     */
    public void start()
    {
        internalStart();
    }

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @throws NullPointerException
     *         if {@code name} is {@code null}
     */
    private void internalSetName(String name)
    {
        name_ = requireNonNull(name);
    }

    /**
     * TODO (AZ) - doc
     */
    private void internalStart()
    {
        startTime_  = Instant.now();
        startNanos_ = System.nanoTime();
    }
}
