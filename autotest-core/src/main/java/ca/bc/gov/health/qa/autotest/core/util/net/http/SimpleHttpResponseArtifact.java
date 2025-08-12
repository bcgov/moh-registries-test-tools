package ca.bc.gov.health.qa.autotest.core.util.net.http;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.bc.gov.health.qa.autotest.core.util.capture.TextArtifact;
import ca.bc.gov.health.qa.autotest.core.util.text.TextUtils;
import ca.bc.gov.health.qa.autotest.core.util.timer.TimedEvent;

/**
 * TODO (AZ) - doc
 */
public class SimpleHttpResponseArtifact
extends TextArtifact
{
    private static final String EVENT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS z";
    private static final String SECTION_SEPARATOR  = "-".repeat(80) + "\n";

    private final boolean            isResponseOriginal_;
    private final SimpleHttpResponse response_;

    /**
     * TODO (AZ) - doc
     *
     * @param name
     *        ???
     *
     * @param response
     *        ???
     *
     * @param isResponseOriginal
     *        ???
     *
     * @throws NullPointerException
     *         if {@code response} is {@code null}
     */
    public SimpleHttpResponseArtifact(
            String             name,
            SimpleHttpResponse response,
            boolean            isResponseOriginal)
    {
        super(name, null, null, response.getTransactionTiming().getEndTime());
        isResponseOriginal_ = isResponseOriginal;
        response_           = response;
    }

    @Override
    public String renderText()
    {
        TimedEvent transactionTiming = response_.getTransactionTiming();
        String timestamp = DateTimeFormatter
                .ofPattern(EVENT_TIME_PATTERN)
                .withZone(ZoneId.systemDefault())
                .format(transactionTiming.getEndTime());
        String duration = "" + transactionTiming.getDuration().toMillis() + " ms";

        HttpResponseType responseType = response_.getResponseType();
        String note = null;
        String responseContent;
        switch (responseType)
        {
            case BINARY:
                note = "Binary response content is not included.";
                responseContent = "";
                break;

            case TEXT:
                if (!isResponseOriginal_)
                {
                    note = "An altered version of the response is included.";
                }
                responseContent = response_.getTextResponseBody();
                break;

            default:
                String msg = String.format("Unknown HTTP response type (%s).", responseType);
                throw new IllegalStateException(msg);
        }

        List<List<String>> info = new ArrayList<>();
        info.add(Arrays.asList("Name",     ":", transactionTiming.getName()));
        info.add(Arrays.asList("URL",      ":", response_.getUri().toString()));
        info.add(Arrays.asList("Version",  ":", response_.getHttpVersion()));
        info.add(Arrays.asList("Status",   ":", String.valueOf(response_.getStatusCode())));
        info.add(Arrays.asList("Time",     ":", timestamp));
        info.add(Arrays.asList("Duration", ":", duration));
        info.add(Arrays.asList("Type",     ":", responseType.toString()));
        if (note != null)
        {
            info.add(Arrays.asList("Note", ":", note));
        }

        String text = new StringBuilder()
                .append(SECTION_SEPARATOR)
                .append("HTTP RESPONSE\n")
                .append(TextUtils.formatTable(info))
                .append(SECTION_SEPARATOR)
                .append("RESPONSE HEADERS\n")
                .append(TextUtils.formatHeaders(response_.getResponseHeaderMap()))
                .append(SECTION_SEPARATOR)
                .append(responseContent)
                .toString();
        return text;
    }
}
