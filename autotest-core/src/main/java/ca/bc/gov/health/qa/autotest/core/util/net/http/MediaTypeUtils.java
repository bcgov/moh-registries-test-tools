package ca.bc.gov.health.qa.autotest.core.util.net.http;

import java.util.Locale;
import java.util.Map;

/**
 * Media type utilities.
 */
public class MediaTypeUtils
{
    private static final Map<String,String> MEDIA_TYPE_EXTENSION_MAP = Map.ofEntries(
            Map.entry("application/json",      ".json"),
            Map.entry("application/fhir+json", ".json"));

    private MediaTypeUtils()
    {}

    /**
     * Returns a file extension associated with a given media type.
     *
     * @param mediaType
     *        the media type whose associated file extension to return
     *
     * @return the file extension associated with {@code mediaType},
     *         or {@code null} if the content type is not found
     */
    public static String getExtensionForMediaType(String mediaType)
    {
        String extension;
        if (mediaType != null)
        {
            String rawMediaType = mediaType.split("\s*;\s*", 2)[0];
            extension = MEDIA_TYPE_EXTENSION_MAP.get(rawMediaType.toLowerCase(Locale.ROOT));
        }
        else
        {
            extension = null;
        }
        return extension;
    }
}
