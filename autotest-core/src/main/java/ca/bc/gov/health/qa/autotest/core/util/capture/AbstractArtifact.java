package ca.bc.gov.health.qa.autotest.core.util.capture;

import java.time.Instant;

import ca.bc.gov.health.qa.autotest.core.util.event.BasicNamedEvent;

/**
 * An artifact that has a name and a time associated with it,
 * and can can be rendered into a binary representation.
 */
public abstract class AbstractArtifact
extends BasicNamedEvent
implements Artifact
{
    /**
     * Creates a new artifact with the specified name and time.
     *
     * @param name
     *        the name of the artifact,
     *        or {@code null} to indicate an empty name
     *
     * @param time
     *        the time of the artifact,
     *        or {@code null} to indicate the current time
     */
    protected AbstractArtifact(String name, Instant time)
    {
        super(name, time);
    }
}
