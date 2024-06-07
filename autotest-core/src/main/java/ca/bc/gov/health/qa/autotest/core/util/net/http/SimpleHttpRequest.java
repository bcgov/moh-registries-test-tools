package ca.bc.gov.health.qa.autotest.core.util.net.http;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

/**
 * A simple HTTP request.
 */
public class SimpleHttpRequest
{
    private final HttpRequest       httpRequest_;
    private final SimpleHttpRequest maskedRequest_;
    private final String            requestBody_;
    private final HttpResponseType  responseType_;
    private final String            transactionName_;

    /**
     * TODO (AZ) - doc
     *
     * @param transactionName
     *        ???
     *
     * @param httpRequest
     *        ???
     *
     * @param requestBody
     *        ???
     *
     * @param responseType
     *        ???
     *
     * @param maskedRequest
     *        the request to use for captured request artifact,
     *        optional and can be {@code null}
     *
     * @throws NullPointerException
     *         if a required parameter is {@code null}
     */
    SimpleHttpRequest(
            String            transactionName,
            HttpRequest       httpRequest,
            String            requestBody,
            HttpResponseType  responseType,
            SimpleHttpRequest maskedRequest)
    {
        requestBody_           = requireNonNull(requestBody,  "Null request body.");
        httpRequest_           = requireNonNull(httpRequest,  "Null HTTP request.");
        responseType_          = requireNonNull(responseType, "Null response type.");
        maskedRequest_         = maskedRequest;
        transactionName_       = requireNonNull(transactionName, "Null transaction name.");
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public SimpleHttpRequest getMaskedRequest()
    {
        return maskedRequest_;
    }

    /**
     * Returns the body.
     *
     * @return the body
     */
    public String getRequestBody()
    {
        return requestBody_;
    }

    /**
     * Returns an unmodifiable map of the HTTP request headers.
     *
     * @return an unmodifiable map of the HTTP request headers
     */
    public Map<String,List<String>> getRequestHeaderMap()
    {
        return httpRequest_.headers().map();
    }

    /**
     * Returns the HTTP request method.
     *
     * @return the HTTP request method
     */
    public String getRequestMethod()
    {
        return httpRequest_.method();
    }

    /**
     * Returns the HTTP response type.
     *
     * @return the HTTP response type
     */
    public HttpResponseType getResponseType()
    {
        return responseType_;
    }

    /**
     * Returns the transaction name.
     *
     * @return the transaction name
     */
    public String getTransactionName()
    {
        return transactionName_;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    public URI getUri()
    {
        return httpRequest_.uri();
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    HttpRequest getHttpRequest()
    {
        return httpRequest_;
    }
}
