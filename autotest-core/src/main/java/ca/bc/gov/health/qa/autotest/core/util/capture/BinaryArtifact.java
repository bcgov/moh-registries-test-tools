package ca.bc.gov.health.qa.autotest.core.util.capture;

import java.time.Instant;
import java.util.Arrays;

/**
 * An artifact that has a name, a binary content and a time associated with it.
 */
public class BinaryArtifact
extends AbstractArtifact
{
    private final byte[] bytes_;

    /**
     * Creates a new binary artifact with the specified name, binary content
     * and the current time.
     *
     * @param name
     *        the name of the artifact,
     *        or {@code null} to indicate an empty name
     *
     * @param bytes
     *        the binary content of the artifact,
     *        optional and can be {@code null}
     */
    public BinaryArtifact(String name, byte[] bytes)
    {
        this(name, bytes, null);
    }

    /**
     * Creates a new binary artifact with the specified name, binary content
     * and time.
     *
     * @param name
     *        the name of the artifact,
     *        or {@code null} to indicate an empty name
     *
     * @param bytes
     *        the binary content of the artifact,
     *        optional and can be {@code null}
     *
     * @param time
     *        the time of the artifact,
     *        or {@code null} to indicate the current time
     */
    public BinaryArtifact(String name, byte[] bytes, Instant time)
    {
        super(name, time);
        if (bytes != null)
        {
            bytes_ = Arrays.copyOf(bytes, bytes.length);
        }
        else
        {
            bytes_ = new byte[0];
        }
    }

    @Override
    public byte[] render()
    {
        return Arrays.copyOf(bytes_, bytes_.length);
    }
}
