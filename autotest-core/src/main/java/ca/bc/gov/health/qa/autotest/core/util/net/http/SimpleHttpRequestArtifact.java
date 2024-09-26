package ca.bc.gov.health.qa.autotest.core.util.net.http;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.bc.gov.health.qa.autotest.core.util.capture.TextArtifact;
import ca.bc.gov.health.qa.autotest.core.util.text.TextUtils;

/**
 * TODO (AZ) - doc
 */
public class SimpleHttpRequestArtifact
extends TextArtifact
{
    private static final String SECTION_SEPARATOR = "-".repeat(80) + "\n";

    private final boolean           isRequestBodyOriginal_;
    private final SimpleHttpRequest request_;

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @param request
     *        ???
     *
     * @param isRequestBodyOriginal
     *        ???
     *
     * @throws NullPointerException
     *         if {@code request} is {@code null}
     */
    public SimpleHttpRequestArtifact(
            String            name,
            SimpleHttpRequest request,
            boolean           isRequestBodyOriginal)
    {
        super(name, null);
        requireNonNull(request, "Null request.");
        isRequestBodyOriginal_ = isRequestBodyOriginal;
        request_               = request;
    }

    @Override
    public String renderText()
    {
        String requestMethod   = request_.getRequestMethod();
        String transactionName = request_.getTransactionName();

        List<List<String>> info = new ArrayList<>();
        info.add(Arrays.asList("Name",   ":", transactionName));
        info.add(Arrays.asList("Method", ":", requestMethod));
        info.add(Arrays.asList("URL",    ":", request_.getUri().toString()));
        if (!isRequestBodyOriginal_)
        {
            info.add(Arrays.asList(
                    "Note", ":", "An altered version of the request payload is included."));
        }

        String text = new StringBuilder()
                .append(SECTION_SEPARATOR)
                .append("HTTP REQUEST\n")
                .append(TextUtils.formatTable(info))
                .append(SECTION_SEPARATOR)
                .append("REQUEST HEADERS\n")
                .append(TextUtils.formatHeaders(request_.getRequestHeaderMap()))
                .append(SECTION_SEPARATOR)
                .append(request_.getRequestBody())
                .toString();
        return text;
    }
}
