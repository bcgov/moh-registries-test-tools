package ca.bc.gov.health.qa.autotest.core.util.io;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * TODO (AZ) - doc
 */
public class FileIOUtils
{
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("[ -.0-9A-Z_a-z]+");

    private static final int MAX_CREATE_DIRECTORY_ATTEMPTS = 100;

    private FileIOUtils()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @param parentPath
     *        ???
     *
     * @param name
     *        ???
     *
     * @return ???
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public static Path createSequentialDirectory(Path parentPath, String name)
    throws IOException
    {
        Path    path    = null;
        boolean success = false;
        for (int i = 0; i < MAX_CREATE_DIRECTORY_ATTEMPTS; i++)
        {
            path = createPath(parentPath, name + ((i == 0) ? "" : "." + i));
            try
            {
                Files.createDirectory(path);
                success = true;
                break;
            }
            catch (FileAlreadyExistsException e)
            {
                // Do nothing. Try again with incremented suffix,
                // if the maximum number of attempts had not been exhausted.
            }
        }
        if (!success)
        {
            String msg = String.format(
                    "Failed to create sequential directory (%s) " +
                    "within the maximum number of attempts (%d).",
                    path,
                    MAX_CREATE_DIRECTORY_ATTEMPTS);
            throw new FileAlreadyExistsException(msg);
        }
        return path;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param parentPath
     *        ???
     *
     * @param name
     *        ???
     *
     * @return ???
     */
    public static Path createPath(Path parentPath, String name)
    {
        Path path;
        if (parentPath != null)
        {
            path = parentPath.resolve(name);
        }
        else
        {
            path = Path.of(name);
        }
        return path.normalize();
    }

    /**
     * TODO (AZ) - doc
     *
     * @param parentPath
     *        ???
     *
     * @param name
     *        ???
     *
     * @return ???
     */
    public static Path createSanitizedPath(Path parentPath, String name)
    {
        return createPath(parentPath, sanitizeFileName(name));
    }

    /**
     * Returns a sanitized file name, preserving permitted characters only,
     * and replacing other ones with an underscore ({@code "_"}).
     *
     * @param name
     *        the file name to sanitize
     *
     * @return a sanitized file name
     *
     * @throws NullPointerException
     *         if {@code name} is {@code null}
     */
    public static String sanitizeFileName(String name)
    {
        requireNonNull(name, "Null file name.");
        String sanitizedName = name.replaceAll("[^0-9A-Za-z,.-]+", "_").replaceAll("^_+|_+$", "");
        if (   sanitizedName.isEmpty()
            || sanitizedName.equals(".")
            || sanitizedName.equals(".."))
        {
            sanitizedName = "_";
        }
        return sanitizedName;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @throws IllegalArgumentException
     *         if {@code name} is not a valid file name
     *
     * @throws NullPointerException
     *         if {@code name} is {@code null}
     */
    public static void validateFileName(String name)
    {
        requireNonNull(name, "Null file name.");
        if (   !FILE_NAME_PATTERN.matcher(name).matches()
            || name.equals(".")
            || name.equals(".."))
        {
            String msg = String.format("Invalid file name (%s).", name);
            throw new IllegalArgumentException(msg);
        }
    }
}
