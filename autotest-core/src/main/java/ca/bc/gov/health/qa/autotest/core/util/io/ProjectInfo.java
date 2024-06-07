package ca.bc.gov.health.qa.autotest.core.util.io;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

/**
 * Project information.
 *
 * <p>Instances of this class are immutable and safe for use by multiple concurrent threads.
 */
public final class ProjectInfo
{
    private static final ProjectInfo CORE_PROJECT_INFO =
            ProjectInfo.create(MethodHandles.lookup().lookupClass(), "maven-project.properties");

    private final String artifactId_;
    private final String groupId_;
    private final String name_;
    private final String version_;

    /**
     * Creates a new instance of project information.
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
    public ProjectInfo(String groupId, String artifactId, String name, String version)
    {
        artifactId_ = requireNonNull(artifactId, "Null artifactId.");
        groupId_    = requireNonNull(groupId,    "Null groupId.");
        name_       = requireNonNull(name,       "Null name.");
        version_    = requireNonNull(version,    "Null version.");
    }

    /**
     * Creates a new instance of project information from properties.
     *
     * <p>The properties supplied are required to contain mappings for the following keys:
     * <ul>
     *   <li>{@code groupId}
     *   <li>{@code artifactId}
     *   <li>{@code name}
     *   <li>{@code version}
     * </ul>
     *
     * @param properties
     *        the properties to create the project information from
     *
     * @return a new instance of project information
     */
    public static ProjectInfo create(Properties properties)
    {
        return new ProjectInfo(
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
        return ProjectInfo.create(properties);
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
     * Returns project info of the core project (i.e. containing this utility).
     *
     * @return project info of the core project
     */
    public static ProjectInfo getCoreProjectInfo()
    {
        return CORE_PROJECT_INFO;
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
}
