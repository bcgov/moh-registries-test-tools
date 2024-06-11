package ca.bc.gov.health.qa.autotest.core.util.io;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

/**
 * Project information.
 *
 * <p>Instances of this class are immutable and safe for use by multiple concurrent threads.
 */
public final class ProjectInfo
{
    private static class CoreProjectInfoSingletonHolder
    {
        private static final ProjectInfo INSTANCE = ProjectInfo.create(
                MethodHandles.lookup().lookupClass(), "maven-project.properties");
    }

    private final Path   basePath_;
    private final String artifactId_;
    private final String groupId_;
    private final String name_;
    private final String version_;

    /**
     * Creates a new instance of project information.
     *
     * @param basePath
     *        project base path
     *
     * @param groupId
     *        project group identifier
     *
     * @param artifactId
     *        project artifact identifier
     *
     * @param name
     *        project name
     *
     * @param version
     *        project version
     *
     * @throws NullPointerException
     *         if any of the parameters is {@code null}
     */
    public ProjectInfo(
            Path basePath, String groupId, String artifactId, String name, String version)
    {
        basePath_   = requireNonNull(basePath,   "Null base path.");
        artifactId_ = requireNonNull(artifactId, "Null artifactId.");
        groupId_    = requireNonNull(groupId,    "Null groupId.");
        name_       = requireNonNull(name,       "Null name.");
        version_    = requireNonNull(version,    "Null version.");
    }

    /**
     * Creates a new instance of project information from base path and properties.
     *
     * <p>The properties supplied are required to contain mappings for the following keys:
     * <ul>
     *   <li>{@code groupId}
     *   <li>{@code artifactId}
     *   <li>{@code name}
     *   <li>{@code version}
     * </ul>
     *
     * @param basePath
     *        project base path
     *
     * @param properties
     *        the properties to create the project information from
     *
     * @return a new instance of project information
     */
    public static ProjectInfo create(Path basePath, Properties properties)
    {
        return new ProjectInfo(
                basePath,
                properties.getProperty("groupId"),
                properties.getProperty("artifactId"),
                properties.getProperty("name"),
                properties.getProperty("version"));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param resourceClass
     *        ???
     *
     * @param resourceName
     *        ???
     *
     * @return ???
     *
     * @throws IllegalStateException
     *         ???
     */
    public static ProjectInfo create(Class<?> resourceClass, String resourceName)
    {
        Path basePath = lookupBasePath(resourceClass);
        Properties properties = new Properties();
        try (InputStream inputStream = ResourceUtils.openResource(resourceClass, resourceName))
        {
            properties.load(inputStream);
        }
        catch (IOException e)
        {
            String msg = String.format(
                    "Failed to read project info resource (%s: %s).",
                    resourceClass.getName(),
                    resourceName);
            throw new IllegalStateException(msg, e);
        }
        return ProjectInfo.create(basePath, properties);
    }

    /**
     * Returns the project artifact identifier.
     *
     * @return the project artifact identifier
     */
    public String getArtifactId()
    {
        return artifactId_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public Path getBasePath()
    {
        return basePath_;
    }

    /**
     * Returns project info of the core project (i.e. containing this utility).
     *
     * @return project info of the core project
     */
    public static ProjectInfo getCoreProjectInfo()
    {
        return CoreProjectInfoSingletonHolder.INSTANCE;
    }

    /**
     * Returns the project group identifier.
     *
     * @return the project group identifier
     */
    public String getGroupId()
    {
        return groupId_;
    }

    /**
     * Returns the project name.
     *
     * @return the project name
     */
    public String getName()
    {
        return name_;
    }

    /**
     * Returns the project version.
     *
     * @return the project version
     */
    public String getVersion()
    {
        return version_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param referenceClass
     *        ???
     *
     * @return ???
     */
    private static Path lookupBasePath(Class<?> referenceClass)
    {
        Path basePath;
        try
        {
            Path path = Paths.get(
                    referenceClass.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (path.toString().toLowerCase(Locale.ROOT).endsWith(".jar"))
            {
                path = path.getParent();
            }
            basePath = path.getParent();
        }
        catch (URISyntaxException e)
        {
            throw new IllegalStateException("Failed to determine base path.", e);
        }
        return basePath;
    }
}
