package ca.bc.gov.health.qa.autotest.core.util.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * TODO (AZ) - doc
 */
public class PropertyUtils
{
    private PropertyUtils()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @param filePath
     *        ???
     *
     * @return ???
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public static Properties loadProperties(Path filePath)
    throws IOException
    {
        Properties properties = new Properties();
        try (InputStream inStream = new FileInputStream(filePath.toFile()))
        {
            properties.load(inStream);
        }
        return properties;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param filePath
     *        ???
     *
     * @return ???
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public static Map<String,String> loadPropertyMap(Path filePath)
    throws IOException
    {
        Map<String,String> propertyMap = new HashMap<>();
        Properties properties = loadProperties(filePath);
        for (Entry<Object,Object> entry : properties.entrySet())
        {
            propertyMap.put((String) entry.getKey(), (String) entry.getValue());
        }
        return propertyMap;
    }
}
