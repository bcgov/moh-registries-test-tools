package ca.bc.gov.health.qa.autotest.core.util.event;

import java.time.Instant;

/**
 * An event that has a time associated with it.
 */
public interface Event
{
    /**
     * Returns the time of the event.
     *
     * @return the time of the event
     */
    public Instant getTime();
}
