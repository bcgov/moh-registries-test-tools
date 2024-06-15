package ca.bc.gov.health.qa.alm2xray;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.bc.gov.health.qa.autotest.core.util.net.http.SimpleHttpClient;
import ca.bc.gov.health.qa.autotest.core.util.net.http.SimpleHttpRequest;
import ca.bc.gov.health.qa.autotest.core.util.net.http.SimpleHttpRequestBuilder;
import ca.bc.gov.health.qa.autotest.core.util.net.http.SimpleHttpResponse;
import ca.bc.gov.health.qa.autotest.core.util.security.UserCredentials;

/**
 * TODO (AZ) - doc
 */
public class AlmRestClient
implements AutoCloseable
{
    private static final Logger LOG            = LogManager.getLogger();
    private static final String MASK_INDICATOR = "*".repeat(8);

    private final SimpleHttpClient client_;
    private final URI              uri_;

    /**
     * TODO (AZ) - doc
     *
     * @param uri
     *        ???
     */
    public AlmRestClient(URI uri)
    {
        uri_    = requireNonNull(uri, "Null URI.");
        client_ = new SimpleHttpClient();
        LOG.info("URL ({}).", uri_);
    }

    /**
     * TODO (AZ) - doc
     *
     * <p>
     * If already closed, invoking this method has no effect.
     */
    @Override
    public void close()
    {
        client_.close();
    }

    /**
     * TODO (AZ) - doc
     *
     * @param credentials
     *        ???
     *
     * @return ???
     *
     * @throws InterruptedException
     *         if the current thread is interrupted
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    public String login(UserCredentials credentials)
    throws InterruptedException,
           IOException
    {
        String postBody = getLoginRequestPayload(credentials);
        credentials.setPassword(MASK_INDICATOR.toCharArray());
        String maskedBody = getLoginRequestPayload(credentials);
        SimpleHttpRequest request = new SimpleHttpRequestBuilder()
                .transactionName("Login")
                .uri(uri_.resolve("authentication-point/alm-authenticate"))
                .methodPost()
                .header("Content-Type", "application/json")
                .body(postBody, maskedBody)
                .build();
        SimpleHttpResponse response = client_.send(request, true, false);
        SimpleHttpClient.generateResponseArtifacts(createAlteredHttpResponse(response), false);
        
        System.out.println(request.getUri());
        
        System.out.println("LOGIN: " + postBody); // FIXME
        System.out.println("       " + maskedBody); // FIXME
        
        // FIXME
        return null;
    }

    private static SimpleHttpResponse createAlteredHttpResponse(SimpleHttpResponse response)
    {
        Map<String,List<String>> maskedHeaderMap = new HashMap<>(response.getResponseHeaderMap());
        maskedHeaderMap.put("set-cookie", List.of(MASK_INDICATOR));
        return new SimpleHttpResponse(
                response.getUri(),
                response.getStatusCode(),
                response.getHttpVersion(),
                Collections.unmodifiableMap(maskedHeaderMap),
                response.getResponseType(),
                new byte[0],
                response.getTextResponseBody(),
                response.getTransactionTiming());
    }

    private static String getLoginRequestPayload(UserCredentials credentials)
    {
        JSONObject requestJson = new JSONObject()
                .put("alm-authentication", new JSONObject()
                        .put("user", new String(credentials.getUsername(false)))
                        .put("password",   new String(credentials.getPassword(false))));
        return requestJson.toString(2);
    }
}
