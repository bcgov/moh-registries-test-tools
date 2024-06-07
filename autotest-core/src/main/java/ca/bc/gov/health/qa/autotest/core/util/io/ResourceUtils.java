package ca.bc.gov.health.qa.autotest.core.util.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * TODO (AZ) - doc
 */
public class ResourceUtils
{
    private ResourceUtils()
    {}

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
    public static String readResource(Class<?> resourceClass, String resourceName)
    {
        String resourceContent;
        try (InputStream inputStream = openResource(resourceClass, resourceName))
        {
            resourceContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            String msg = String.format(
                    "Failed to read resource (%s: %s).", resourceClass.getName(), resourceName);
            throw new IllegalStateException(msg, e);
        }
        return resourceContent;
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
     * @throws FileNotFoundException
     *         if the resource is not found
     */
    public static InputStream openResource(Class<?> resourceClass, String resourceName)
    throws FileNotFoundException
    {
        InputStream inputStream = resourceClass.getResourceAsStream(resourceName);
        if (inputStream == null)
        {
            String msg = String.format(
                    "Resource not found (%s: %s).", resourceClass.getName(), resourceName);
            throw new FileNotFoundException(msg);
        }
        return inputStream;
    }
}
