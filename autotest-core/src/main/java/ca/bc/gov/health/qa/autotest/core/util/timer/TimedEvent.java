package ca.bc.gov.health.qa.autotest.core.util.timer;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;

import ca.bc.gov.health.qa.autotest.core.util.event.NamedEvent;

/**
 * TODO (AZ) - doc
 *
 * <p>TODO (AZ) - add a note that duration may not be equal to the difference (endTime - time).
 *
 * <p>Instances of this class are immutable and safe for use by multiple concurrent threads.
 */
public final class TimedEvent
implements NamedEvent
{
    private final Duration duration_;
    private final Instant  endTime_;
    private final String   name_;
    private final Instant  time_;
    private final String   toString_;

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @param duration
     *        ???
     *
     * @param time
     *        ???
     *
     * @param endTime
     *        ???
     *
     * @throws IllegalArgumentException
     *         if @{endTime} is before @{time} or {@code duration} is negative
     *
     * @throws NullPointerException
     *         if a {@code null} parameter is supplied
     */
    public TimedEvent(String name, Duration duration, Instant time, Instant endTime)
    {
        requireNonNull(duration, "Null duration.");
        requireNonNull(endTime,  "Null end time.");
        requireNonNull(name,     "Null name.");
        requireNonNull(time,     "Null start time.");
        if (duration.isNegative())
        {
            String msg = String.format("Negative duration (%s).", duration);
            throw new IllegalArgumentException(msg);
        }
        if (endTime.isBefore(time))
        {
            String msg = String.format("End time (%s) is before start time (%s).", endTime, time);
            throw new IllegalArgumentException(msg);
        }
        duration_ = duration;
        endTime_  = endTime;
        name_     = name;
        time_     = time;
        toString_ = "[" + name + ", " + duration + ", " + time + " - " + endTime + "]";
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public Duration getDuration()
    {
        return duration_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public Instant getEndTime()
    {
        return endTime_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    @Override
    public String getName()
    {
        return name_;
    }


    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    @Override
    public Instant getTime()
    {
        return time_;
    }

    /**
     * Returns a string representation of this timing.
     *
     * @return a string representation of this timing
     */
    @Override
    public String toString()
    {
        return toString_;
    }
}