package ca.bc.gov.health.qa.autotest.core.util.net;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utilities for manipulating URIs.
 */
public class UriUtils
{
    private UriUtils()
    {}

    /**
     * TODO (AZ) - doc
     *
     * @param uri
     *        ???
     *
     * @param query
     *        ???
     *
     * @return ???
     *
     * @throws IllegalArgumentException
     *         if {@code uri} already contains a query
     *
     * @throws IllegalStateException
     *         if a failure occurs while creating a new URI
     */
    public static URI getUriWithQuery(URI uri, String query)
    {
        URI uriWithQuery;
        if (uri.getQuery() == null)
        {
            try
            {
                uriWithQuery = new URI(
                        uri.getScheme(),
                        uri.getAuthority(),
                        uri.getPath(),
                        query,
                        uri.getFragment());
            }
            catch (URISyntaxException e)
            {
                throw new IllegalStateException("Failed to create URI.", e);
            }
        }
        else
        {
            throw new IllegalArgumentException("URI already contains a query.");
        }
        return uriWithQuery;
    }
}
