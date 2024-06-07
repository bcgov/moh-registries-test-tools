package ca.bc.gov.health.qa.autotest.core.util.net.http;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ca.bc.gov.health.qa.autotest.core.util.timer.TimedEvent;

/**
 * A simple HTTP response.
 */
public class SimpleHttpResponse
{
    private final byte[]                   binaryResponseBody_;
    private final String                   httpVersion_;
    private final Map<String,List<String>> responseHeaderMap_;
    private final HttpResponseType         responseType_;
    private final int                      statusCode_;
    private final String                   textResponseBody_;
    private final TimedEvent               transactionTiming_;
    private final URI                      uri_;

    /**
     * TODO (AZ) - doc
     *
     * @param uri
     *        ???
     *
     * @param statusCode
     *        ???
     *
     * @param httpVersion
     *        ???
     *
     * @param responseHeaderMap
     *        an unmodifiable map of the HTTP response headers
     *
     * @param responseType
     *        ???
     *
     * @param binaryResponseBody
     *        ???
     *
     * @param textResponseBody
     *        ???
     *
     * @param transactionTiming
     *        ???
     *
     * @throws NullPointerException
     *         if a required parameter is {@code null}
     */
    public SimpleHttpResponse(
            URI                      uri,
            int                      statusCode,
            String                   httpVersion,
            Map<String,List<String>> responseHeaderMap,
            HttpResponseType         responseType,
            byte[]                   binaryResponseBody,
            String                   textResponseBody,
            TimedEvent               transactionTiming)
    {
        binaryResponseBody_ = Arrays.copyOf(
                requireNonNull(binaryResponseBody, "Null binary response body."),
                binaryResponseBody.length);
        responseHeaderMap_      = requireNonNull(responseHeaderMap, "Null response header map.");
        httpVersion_            = requireNonNull(httpVersion,       "Null HTTP version.");
        responseType_           = requireNonNull(responseType,      "Null response type.");
        statusCode_             = statusCode;
        textResponseBody_       = requireNonNull(textResponseBody,  "Null text response body.");
        transactionTiming_      = requireNonNull(transactionTiming, "Null transaction timing.");
        uri_                    = requireNonNull(uri,               "Null URI.");
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public byte[] getBinaryResponseBody()
    {
        return Arrays.copyOf(binaryResponseBody_, binaryResponseBody_.length);
    }

    /**
     * Returns an unmodifiable map of the HTTP response headers.
     *
     * @return an unmodifiable map of the HTTP response headers
     */
    public Map<String,List<String>> getResponseHeaderMap()
    {
        return responseHeaderMap_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public String getHttpVersion()
    {
        return httpVersion_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public HttpResponseType getResponseType()
    {
        return responseType_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public int getStatusCode()
    {
        return statusCode_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public String getTextResponseBody()
    {
        return textResponseBody_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public TimedEvent getTransactionTiming()
    {
        return transactionTiming_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public URI getUri()
    {
        return uri_;
    }
}
