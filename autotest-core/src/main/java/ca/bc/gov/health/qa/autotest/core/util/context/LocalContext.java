package ca.bc.gov.health.qa.autotest.core.util.context;

import ca.bc.gov.health.qa.autotest.core.util.capture.Artifact;
import ca.bc.gov.health.qa.autotest.core.util.capture.ArtifactBuffer;
import ca.bc.gov.health.qa.autotest.core.util.config.Config;
import ca.bc.gov.health.qa.autotest.core.util.config.ConfigProvider;

/**
 * TODO (AZ) - doc
 *
 * <p>The {@link #get()} method returns the thread local instance of the
 * {@code LocalContext}, initializing it if necessary.
 */
public class LocalContext
{
    private static final ThreadLocal<LocalContext> THREAD_LOCAL_INSTANCE =
            ThreadLocal.withInitial(() -> new LocalContext());

    private final ArtifactBuffer artifactBuffer_;
    private final Config         config_;

    /**
     * Creates a new local context.
     */
    private LocalContext()
    {
        artifactBuffer_ = new ArtifactBuffer();
        config_         = ConfigProvider.get().getConfig();
    }

    /**
     * Adds an artifact to the artifact buffer of the thread local context.
     *
     * <p>This is a convenience method equivalent to:
     * <pre>{@code
     *   LocalContext.get().getArtifactBuffer().addArtifact(artifact);
     * }</pre>
     *
     * @param artifact
     *        the artifact to add
     *
     * @throws NullPointerException
     *         if {@code artifact} is {@code null}
     */
    public static void addArtifact(Artifact artifact)
    {
        get().getArtifactBuffer().addArtifact(artifact);
    }

    /**
     * Returns the artifact buffer of this local context.
     *
     * @return the artifact buffer of this local context
     */
    public ArtifactBuffer getArtifactBuffer()
    {
        return artifactBuffer_;
    }

    /**
     * Returns the thread local instance of the context, initializing it if necessary.
     *
     * @return the thread local instance of the context
     */
    public static LocalContext get()
    {
        return THREAD_LOCAL_INSTANCE.get();
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public Config getConfig()
    {
        return config_;
    }
}
