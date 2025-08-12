package ca.bc.gov.health.qa.autotest.core.util.capture;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import ca.bc.gov.health.qa.autotest.core.util.text.TextUtils;

/**
 * An artifact that has a name, a text content and a time associated with it.
 *
 * <p>TODO (AZ) - doc normalize lines and encoding to bytes using charset
 */
public class TextArtifact
extends AbstractArtifact
{
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String  LINE_SEPARATOR  = TextUtils.CRLF;

    private Charset      charset_;
    private CharSequence text_;

    /**
     * Creates a new text artifact.
     *
     * @param name
     *        the name of the artifact,
     *        or {@code null} to indicate an empty name
     *
     * @param text
     *        the text content of the artifact,
     *        optional and can be {@code null}
     */
    public TextArtifact(String name, CharSequence text)
    {
        this(name, text, null, null);
    }

    /**
     * Creates a new text artifact.
     *
     * @param name
     *        the name of the artifact,
     *        or {@code null} to indicate an empty name
     *
     * @param text
     *        the text content of the artifact,
     *        optional and can be {@code null}
     *
     * @param charset
     *        the charset to use for encoding the artifact's text content
     *        into a binary representation, or {@code null} to indicate UTF-8
     *
     * @param time
     *        the time of the artifact,
     *        or {@code null} to indicate the current time
     */
    public TextArtifact(String name, CharSequence text, Charset charset, Instant time)
    {
        super(name, time);
        charset_ = charset;
        text_    = text;
    }

    /**
     * TODO (AZ) - doc
     *           - invokes {@link #renderText()}
     *           - normalize lines, converting endings to the specific line separator
     *           - text converted/encoded to bytes using the specified charset (or UTF-8)
     */
    @Override
    public byte[] render()
    {
        String  text           = renderText();
        String  normalizedText = TextUtils.normalizeLines(text, LINE_SEPARATOR);
        Charset charset        = (charset_ != null) ? charset_ : DEFAULT_CHARSET;
        return normalizedText.getBytes(charset);
    }

    /**
     * Renders this artifact, generating its text representation.
     *
     * @return rendered text representation of this artifact
     */
    public String renderText()
    {
        return (text_ != null) ? text_.toString() : "";
    }
}
