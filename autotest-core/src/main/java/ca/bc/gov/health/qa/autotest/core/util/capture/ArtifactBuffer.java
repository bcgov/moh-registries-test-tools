package ca.bc.gov.health.qa.autotest.core.util.capture;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An artifact buffer containing artifacts added in a sequential order.
 *
 * <p>The artifact buffer has a soft size that can be set.
 * When an artifact is added and the soft size exceeded, a warning is logged.
 */
public class ArtifactBuffer
{
    /**
     * TODO (AZ) - doc
     */
    public static final int DEFAULT_SOFT_SIZE = 50;

    private static final Logger LOG = LogManager.getLogger();

    private final List<Artifact> artifactList_;
    private int softSize_ = DEFAULT_SOFT_SIZE;

    /**
     * Creates a new artifact buffer.
     */
    public ArtifactBuffer()
    {
        artifactList_ = new ArrayList<>();
    }

    /**
     * Adds an artifact to this artifact buffer.
     *
     * @param artifact
     *        the artifact to add
     *
     * @throws NullPointerException
     *         if {@code artifact} is {@code null}
     */
    public void addArtifact(Artifact artifact)
    {
        requireNonNull(artifact, "Null artifact.");
        artifactList_.add(artifact);
        if (artifactList_.size() > softSize_)
        {
            LOG.warn("Artifact buffer soft size exceeded ({}/{}).",
                    artifactList_.size(), softSize_);
        }
    }

    /**
     * Clears this artifact buffer, removing all the artifacts.
     */
    public void clear()
    {
        artifactList_.clear();
    }

    /**
     * Returns a new list containing this buffer's artifacts in the same sequential order.
     *
     * @return a new list containing this buffer's artifacts
     */
    public List<Artifact> getArtifactList()
    {
        return new ArrayList<>(artifactList_);
    }

    /**
     * Resets this artifact buffer by clearing the buffer
     * and resetting the soft size to the default value.
     */
    public void reset()
    {
        clear();
        setSoftSize(DEFAULT_SOFT_SIZE);
    }

    /**
     * Sets the soft size of this artifact buffer.
     *
     * @param size
     *        the size to set
     *
     * @throws IllegalArgumentException
     *         if {@code size} is non-positive
     */
    public void setSoftSize(int size)
    {
        if (size <= 0)
        {
            String msg = String.format("Artifact buffer soft size must be positive (%d).", size);
            throw new IllegalArgumentException(msg);
        }
        softSize_ = size;
    }
}
