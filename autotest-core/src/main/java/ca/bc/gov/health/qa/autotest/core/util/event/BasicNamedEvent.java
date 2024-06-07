package ca.bc.gov.health.qa.autotest.core.util.event;

import java.time.Instant;

/**
 * An event that has a name and a time associated with it.
 */
public class BasicNamedEvent
implements NamedEvent
{
    private final String  name_;
    private final Instant time_;

    /**
     * Creates a new event with the specified name and the current time.
     *
     * @param name
     *        the name of the event,
     *        or {@code null} to indicate an empty name
     */
    public BasicNamedEvent(String name)
    {
        this(name, null);
    }

    /**
     * Creates a new event with the specified name and time.
     *
     * @param name
     *        the name of the event,
     *        or {@code null} to indicate an empty name
     *
     * @param time
     *        the time of the event,
     *        or {@code null} to indicate the current time
     */
    public BasicNamedEvent(String name, Instant time)
    {
        name_ = (name != null) ? name : "";
        time_ = (time != null) ? time : Instant.now();
    }

    @Override
    public String getName()
    {
        return name_;
    }

    @Override
    public Instant getTime()
    {
        return time_;
    }
}
