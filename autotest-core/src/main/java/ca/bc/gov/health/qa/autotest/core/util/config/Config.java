package ca.bc.gov.health.qa.autotest.core.util.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO (AZ) - doc
 */
public class Config
{
    private final Map<String,String> configMap_;

    /**
     * TODO (AZ) - doc
     */
    public Config()
    {
        this(Map.of());
    }

    /**
     * TODO (AZ) - doc
     *
     * @param configMap
     *        ???
     */
    public Config(Map<String,String> configMap)
    {
        configMap_ = Collections.unmodifiableMap(new HashMap<>(configMap));
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public Map<String,String> getConfigMap()
    {
        return configMap_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param key
     *        ???
     *
     * @return ???
     *
     * @throws IllegalArgumentException
     *         ???
     */
    public String get(String key)
    {
        String value = get(key, null);
        if (value == null)
        {
            String msg = String.format("Configuration parameter not found (%s).", key);
            throw new IllegalArgumentException(msg);
        }
        return value;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param key
     *        ???
     *
     * @param defaultValue
     *        ???
     *
     * @return ???
     */
    public String get(String key, String defaultValue)
    {
        String value = configMap_.get(key);
        if (value != null)
        {
            value = value.trim();
        }
        else
        {
            value = defaultValue;
        }
        return value;
    }
}
