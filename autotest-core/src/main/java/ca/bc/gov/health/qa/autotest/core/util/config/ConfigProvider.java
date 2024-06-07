package ca.bc.gov.health.qa.autotest.core.util.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.bc.gov.health.qa.autotest.core.util.io.FileIOUtils;
import ca.bc.gov.health.qa.autotest.core.util.io.PropertyUtils;
import ca.bc.gov.health.qa.autotest.core.util.text.TextUtils;


/**
 * TODO (AZ) - doc
 */
public class ConfigProvider
{
    private static final Path   CONFIG_DIR            = Path.of("config");
    private static final String CONFIG_FILE_NAME      = "config.properties";
    private static final String CONFIG_FILES_PROPERTY = "config.files";
    private static final Logger LOG                   = LogManager.getLogger();

    private static class SingletonHolder
    {
        private static final ConfigProvider INSTANCE = new ConfigProvider();
    }

    private volatile Config config_;

    private ConfigProvider()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public static ConfigProvider get()
    {
        return SingletonHolder.INSTANCE;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     *
     * @throws IllegalStateException
     *         ???
     */
    public Config getConfig()
    {
        if (config_ == null)
        {
            ensureConfigExists();
        }
        return config_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @throws IllegalStateException
     *         ???
     */
    private synchronized void ensureConfigExists()
    {
        if (config_ == null)
        {
            Map<String,String> configMap = loadConfigFile(CONFIG_DIR, CONFIG_FILE_NAME);
            String configFileNamesStr = configMap.getOrDefault(CONFIG_FILES_PROPERTY, "").trim();
            if (!configFileNamesStr.isEmpty())
            {
                String[] configFileNames = configFileNamesStr.split("\\s*,\\s*");
                for (String configFileName : configFileNames)
                {
                    if (!configFileName.isEmpty())
                    {
                        configMap.putAll(loadConfigFile(CONFIG_DIR, configFileName));
                    }
                }
            }
            config_ = new Config(configMap);
            LOG.info("Configuration:\n{}",
                     TextUtils.formatStringMap(new TreeMap<>(config_.getConfigMap())));
        }
    }

    private Map<String,String> loadConfigFile(Path configDir, String configFileName)
    {
        FileIOUtils.validateFileName(configFileName);
        Path configPath = configDir.resolve(configFileName);
        Map<String,String> propertyMap;
        try
        {
            LOG.info("Loading configuration file ({}) ...", configPath);
            propertyMap = PropertyUtils.loadPropertyMap(configPath);
        }
        catch (IOException e)
        {
            String msg = String.format("Failed to load configuration file (%s).", configPath);
            throw new IllegalStateException(msg, e);
        }
        return propertyMap;
    }
}
