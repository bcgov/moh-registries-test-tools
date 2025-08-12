package ca.bc.gov.health.qa.autotest.core.util.net.http;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import ca.bc.gov.health.qa.autotest.core.util.net.UriUtils;
import ca.bc.gov.health.qa.autotest.core.util.text.TextUtils;

/**
 * A builder for simple HTTP requests.
 */
public class SimpleHttpRequestBuilder
{
    private static final String   DEFAULT_REQUEST_METHOD = "GET";
    private static final Duration DEFAULT_TIMEOUT        = Duration.ofSeconds(30);

    private static final Pattern FIELD_NAME_PATTERN =
            Pattern.compile("[!#$%&'*+.^_`|~0-9A-Za-z-]+");
    private static final Pattern HEADER_VALUE_PATTERN =
            Pattern.compile("[\\x20-\\x7E]*");
    private static final Pattern QUERY_PARAMETER_PATTERN =
            Pattern.compile("[\\x20-\\x7E&&[^&+=?]]*");

    private final Map<String,List<String>> maskedRequestHeaderMap_;
    private final Map<String,String>       queryParameterMap_;
    private final Map<String,List<String>> requestHeaderMap_;

    private String           maskedRequestBody_;
    private String           requestBody_;
    private String           requestMethod_;
    private HttpResponseType responseType_;
    private Duration         timeout_;
    private String           transactionName_;
    private URI              uri_;

    /**
     * TODO (AZ) - doc
     */
    public SimpleHttpRequestBuilder()
    {
        // Initialize final fields.
        maskedRequestHeaderMap_ = new HashMap<>();
        queryParameterMap_      = new LinkedHashMap<>();
        requestHeaderMap_       = new HashMap<>();

        // Initialize default values.
        maskedRequestBody_ = "";
        requestBody_       = "";
        requestMethod_     = DEFAULT_REQUEST_METHOD;
        responseType_      = HttpResponseType.TEXT;
        timeout_           = DEFAULT_TIMEOUT;
        transactionName_   = "";
    }

    /**
     * Sets the HTTP request body.
     *
     * @param requestBody
     *        the request body to set
     *
     * @return this builder
     *
     * @throws NullPointerException
     *         if {@code requestBody} is {@code null}
     */
    public SimpleHttpRequestBuilder body(String requestBody)
    {
        return body(requestBody, requestBody);
    }

    /**
     * Sets the HTTP request body.
     *
     * @param requestBody
     *        the request body
     *
     * @param maskedRequestBody
     *        the request body to use for captured request artifact
     *
     * @return this builder
     *
     * @throws NullPointerException
     *         if either {@code requestBody} or {@code maskedRequestBody} is {@code null}
     */
    public SimpleHttpRequestBuilder body(String requestBody, String maskedRequestBody)
    {
        requestBody_       = requireNonNull(requestBody,       "Null HTTP request body.");
        maskedRequestBody_ = requireNonNull(maskedRequestBody, "Null masked HTTP request body.");
        return this;
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     *
     * @throws IllegalStateException
     *         if ???
     */
    public SimpleHttpRequest build()
    {
        SimpleHttpRequest simpleHttpRequest;
        try
        {
            HttpRequest httpRequest       = buildHttpRequest(true);
            HttpRequest maskedHttpRequest = buildHttpRequest(false);
            SimpleHttpRequest maskedSimpleHttpRequest = new SimpleHttpRequest(
                    transactionName_,
                    maskedHttpRequest,
                    maskedRequestBody_,
                    responseType_,
                    null);
            simpleHttpRequest = new SimpleHttpRequest(
                    transactionName_,
                    httpRequest,
                    requestBody_,
                    responseType_,
                    maskedSimpleHttpRequest);
        }
        catch (IllegalStateException e)
        {
            throw new IllegalStateException("Failed to build SimpleHttpRequest.", e);
        }
        return simpleHttpRequest;
    }

    /**
     * TODO (AZ) - doc
     *           - name converted to lower case
     *
     * @param name
     *        ???
     *
     * @param value
     *        ???
     *
     * @return this builder
     *
     * @throws IllegalArgumentException
     *         if either {@code name} or {@code value} is not valid
     *
     * @throws NullPointerException
     *         if either {@code name} or {@code value} is {@code null}
     */
    public SimpleHttpRequestBuilder header(String name, String value)
    {
        return header(name, value, false);
    }

    /**
     * TODO (AZ) - doc
     *           - name converted to lower case
     *
     * @param name
     *        ???
     *
     * @param value
     *        ???
     *
     * @param enableValueMask
     *        ???
     *
     * @return this builder
     *
     * @throws IllegalArgumentException
     *         if either {@code name} or {@code value} is not valid
     *
     * @throws NullPointerException
     *         if either {@code name} or {@code value} is {@code null}
     */
    public SimpleHttpRequestBuilder header(
            String name, String value, boolean enableValueMask)
    {
        validateHeader(name, value);
        String lowerCaseName = name.toLowerCase(Locale.ROOT);
        String maskedValue   = !enableValueMask ? value : TextUtils.MASK_INDICATOR;
        addRequestHeader(requestHeaderMap_,       lowerCaseName, value);
        addRequestHeader(maskedRequestHeaderMap_, lowerCaseName, maskedValue);
        return this;
    }

    /**
     * Sets the HTTP request method.
     *
     * @param requestMethod
     *        the HTTP request method to set
     *
     * @return this builder
     *
     * @throws IllegalArgumentException
     *         if {@code requestMethod} is not valid
     *
     * @throws NullPointerException
     *         if {@code requestMethod} is {@code null}
     */
    public SimpleHttpRequestBuilder method(String requestMethod)
    {
        validateRequestMethod(requestMethod);
        requestMethod_ = requestMethod;
        return this;
    }

    /**
     * Sets the HTTP DELETE request method.
     *
     * <p>The invocation is equivalent to:
     * <pre>{@code
     *   method("DELETE");
     * }</pre>
     *
     * @return this builder
     *
     * @see #method(String)
     */
    public SimpleHttpRequestBuilder methodDelete()
    {
        method("DELETE");
        return this;
    }

    /**
     * Sets the HTTP GET request method.
     *
     * <p>The invocation is equivalent to:
     * <pre>{@code
     *   method("GET");
     * }</pre>
     *
     * @return this builder
     *
     * @see #method(String)
     */
    public SimpleHttpRequestBuilder methodGet()
    {
        method("GET");
        return this;
    }

    /**
     * Sets the HTTP PATCH request method.
     *
     * <p>The invocation is equivalent to:
     * <pre>{@code
     *   method("PATCH");
     * }</pre>
     *
     * @return this builder
     *
     * @see #method(String)
     */
    public SimpleHttpRequestBuilder methodPatch()
    {
        method("PATCH");
        return this;
    }

    /**
     * Sets the HTTP POST request method.
     *
     * <p>The invocation is equivalent to:
     * <pre>{@code
     *   method("POST");
     * }</pre>
     *
     * @return this builder
     *
     * @see #method(String)
     */
    public SimpleHttpRequestBuilder methodPost()
    {
        method("POST");
        return this;
    }

    /**
     * Sets the HTTP PUT request method.
     *
     * <p>The invocation is equivalent to:
     * <pre>{@code
     *   method("PUT");
     * }</pre>}
     *
     * @return this builder
     *
     * @see #method(String)
     */
    public SimpleHttpRequestBuilder methodPut()
    {
        method("PUT");
        return this;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        query parameter name
     *
     * @param value
     *        query parameter value
     *
     * @return this builder
     *
     * @throws IllegalArgumentException
     *         if either {@code name} or {@code value} is not valid
     *
     * @throws NullPointerException
     *         if either {@code name} or {@code value} is {@code null}
     */
    public SimpleHttpRequestBuilder queryParameter(String name, String value)
    {
        validateQueryParameter(name, value);
        if (queryParameterMap_.containsKey(name)) {
            String msg = String.format("Duplicate query parameter name (%s).", name);
            throw new IllegalArgumentException(msg);
        }
        queryParameterMap_.put(name, value);
        return this;
    }

    /**
     * Sets the HTTP response type.
     *
     * @param responseType
     *        the HTTP response type
     *
     * @return this builder
     *
     * @throws NullPointerException
     *         if {@code responseType} is {@code null}
     */
    public SimpleHttpRequestBuilder responseType(HttpResponseType responseType)
    {
        requireNonNull(responseType, "Null HTTP response type.");
        responseType_ = responseType;
        return this;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param timeout
     *        ???
     *
     * @return this builder
     *
     * @throws IllegalArgumentException
     *         if the timeout is non-positive
     *
     * @throws NullPointerException
     *         if {@code timeout} is {@code null}
     */
    public SimpleHttpRequestBuilder timeout(Duration timeout)
    {
        requireNonNull(timeout, "Null timeout.");
        if (!timeout.isPositive())
        {
            String msg = String.format("Non-positive timeout (%s).", timeout);
            throw new IllegalArgumentException(msg);
        }
        timeout_ = timeout;
        return this;
    }

    /**
     * Sets the transaction name.
     *
     * @param name
     *        the transaction name
     *
     * @return this builder
     *
     * @throws NullPointerException
     *         if {@code name} is {@code null}
     */
    public SimpleHttpRequestBuilder transactionName(String name)
    {
        requireNonNull(name, "Null transaction name.");
        transactionName_ = name;
        return this;
    }

    /**
     * Sets the request URI.
     *
     * @param uri
     *        the request URI
     *
     * @return this builder
     *
     * @throws NullPointerException
     *         if the specified URI is {@code null}
     */
    public SimpleHttpRequestBuilder uri(URI uri)
    {
        requireNonNull(uri, "Null URI.");
        uri_ = uri;
        return this;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param headerMap
     *        ???
     *
     * @param name
     *        ???
     *
     * @param value
     *        ???
     */
    private static void addRequestHeader(
            Map<String,List<String>> headerMap, String name, String value)
    {
        if (!headerMap.containsKey(name))
        {
            headerMap.put(name, new ArrayList<>());
        }
        headerMap.get(name).add(value);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param masked
     *        ???
     *
     * @return ???
     */
    private HttpRequest buildHttpRequest(boolean original)
    {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .timeout(timeout_)
                .uri(getUriWithQuery());

        Map<String,List<String>> requestHeaderMap =
                original ? requestHeaderMap_ : maskedRequestHeaderMap_;
        for (Entry<String,List<String>> headerEntry : requestHeaderMap.entrySet())
        {
            String name = headerEntry.getKey();
            for (String value : headerEntry.getValue())
            {
                httpRequestBuilder.header(name, value);
            }
        }

        String requestBody = original ? requestBody_ : maskedRequestBody_;
        httpRequestBuilder.method(requestMethod_, BodyPublishers.ofString(requestBody));

        return httpRequestBuilder.build();
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    private String getQuery()
    {
        StringBuilder queryBuilder = new StringBuilder();
        for (Entry<String,String> entry : queryParameterMap_.entrySet())
        {
            String name  = entry.getKey();
            String value = entry.getValue();
            if (!queryBuilder.isEmpty())
            {
                queryBuilder.append('&');
            }
            queryBuilder.append(name).append("=").append(value);
        }
        return queryBuilder.toString();
    }

    /**
     * TODO (AZ) - doc
     */
    private URI getUriWithQuery()
    {
        if (uri_ == null)
        {
            String msg = "HTTP request URI is not specified.";
            throw new IllegalStateException(msg);
        }
        URI uriWithQuery;
        if (!queryParameterMap_.isEmpty())
        {
            uriWithQuery = UriUtils.getUriWithQuery(uri_, getQuery());
        }
        else
        {
            uriWithQuery = uri_;
        }
        return uriWithQuery;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @param value
     *        ???
     *
     * @throws IllegalArgumentException
     *         if either {@code name} or {@code value} is not valid
     *
     * @throws NullPointerException
     *         if either {@code name} or {@code value} is {@code null}
     */
    private static void validateHeader(String name, String value)
    {
        requireNonNull(name,  "Null header name.");
        requireNonNull(value, "Null header value.");
        if (name.isEmpty())
        {
            throw new IllegalArgumentException("Empty header name.");
        }
        if (!FIELD_NAME_PATTERN.matcher(name).matches())
        {
            String msg = String.format("Invalid header name (%s).", name);
            throw new IllegalArgumentException(msg);
        }
        if (!HEADER_VALUE_PATTERN.matcher(value).matches())
        {
            String msg = String.format("Invalid header value (%s).", value);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Validates query parameter name and value.
     *
     * @param name
     *        query parameter name
     *
     * @param value
     *        query parameter value
     *
     * @throws IllegalArgumentException
     *         if either {@code name} or {@code value} is not valid
     *
     * @throws NullPointerException
     *         if either {@code name} or {@code value} is {@code null}
     */
    private static void validateQueryParameter(String name, String value)
    {
        requireNonNull(name,  "Null query parameter name.");
        requireNonNull(value, "Null query parameter value.");
        if (name.isEmpty())
        {
            throw new IllegalArgumentException("Empty query parameter name.");
        }
        if (!QUERY_PARAMETER_PATTERN.matcher(name).matches())
        {
            String msg = String.format("Invalid query parameter name (%s).", name);
            throw new IllegalArgumentException(msg);
        }
        if (!QUERY_PARAMETER_PATTERN.matcher(value).matches())
        {
            String msg = String.format("Invalid query parameter value (%s).", value);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * TODO (AZ) - doc
     *
     * @param requestMethod
     *        ???
     *
     * @throws IllegalArgumentException
     *         if {@code requestMethod} is not valid
     *
     * @throws NullPointerException
     *         if {@code requestMethod} is {@code null}
     */
    private static void validateRequestMethod(String requestMethod)
    {
        requireNonNull(requestMethod, "Null HTTP request method.");
        if (requestMethod.isEmpty())
        {
            throw new IllegalArgumentException("Empty HTTP request method.");
        }
        if (!FIELD_NAME_PATTERN.matcher(requestMethod).matches())
        {
            String msg = String.format("Invalid HTTP request method (%s).", requestMethod);
            throw new IllegalArgumentException(msg);
        }
    }
}
