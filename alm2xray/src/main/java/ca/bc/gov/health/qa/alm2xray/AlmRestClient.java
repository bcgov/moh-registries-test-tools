package ca.bc.gov.health.qa.alm2xray;

import static java.util.Objects.requireNonNull;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.bc.gov.health.qa.autotest.core.util.net.http.SimpleHttpRequest;
import ca.bc.gov.health.qa.autotest.core.util.net.http.SimpleHttpRequestBuilder;
import ca.bc.gov.health.qa.autotest.core.util.security.UserCredentials;

/**
 * TODO (AZ) - doc
 */
public class AlmRestClient
{
    private static final Logger LOG              = LogManager.getLogger();
    private static final String MASK_INDICATOR   = "*".repeat(8);

    private final URI uri_;

    /**
     * TODO (AZ) - doc
     *
     * @param uri
     *        ???
     */
    public AlmRestClient(URI uri)
    {
        uri_ = requireNonNull(uri, "Null URI.");
        // FIXME - LOG uri
    }

    /**
     * TODO (AZ) - doc
     *
     * @param credentials
     *        ???
     *
     * @return ???
     */
    public String login(UserCredentials credentials)
    {
        LOG.info ("LOGIN TEST I"); // FIXME
        LOG.warn ("LOGIN TEST W"); // FIXME
        LOG.error("LOGIN TEST E"); // FIXME
        LOG.fatal("LOGIN TEST F"); // FIXME

        JSONObject requestJson = new JSONObject()
                .put("clientId", new String(credentials.getUsername()))
                .put("secret",   new String(credentials.getPassword()));
        String postBody = requestJson.toString();
        requestJson.put("secret", MASK_INDICATOR);
        String maskedBody = requestJson.toString();

        SimpleHttpRequest request = new SimpleHttpRequestBuilder()
                .uri(uri_.resolve("rest/oauth2/login"))
                .methodPost()
                .header("Content-Type", "application/json")
                .body(postBody, maskedBody)
                .build();
        
        System.out.println(request.getUri());
        
        System.out.println("LOGIN: " + postBody); // FIXME
        System.out.println("       " + maskedBody); // FIXME
        
        // FIXME
        return null;
    }
}
