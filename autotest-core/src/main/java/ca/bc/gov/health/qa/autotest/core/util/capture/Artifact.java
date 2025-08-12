package ca.bc.gov.health.qa.autotest.core.util.capture;

import ca.bc.gov.health.qa.autotest.core.util.event.NamedEvent;

/**
 * An artifact that can be rendered into a byte array.
 */
public interface Artifact
extends NamedEvent
{
    /**
     * Renders this artifact, generating its binary representation.
     *
     * @return rendered binary representation of this artifact
     */
    public byte[] render();
}
