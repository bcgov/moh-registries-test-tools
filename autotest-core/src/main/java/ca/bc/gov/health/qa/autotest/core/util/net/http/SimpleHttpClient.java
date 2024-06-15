package ca.bc.gov.health.qa.autotest.core.util.net.http;

import static java.util.Objects.requireNonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.bc.gov.health.qa.autotest.core.util.capture.BinaryArtifact;
import ca.bc.gov.health.qa.autotest.core.util.capture.TextArtifact;
import ca.bc.gov.health.qa.autotest.core.util.context.LocalContext;
import ca.bc.gov.health.qa.autotest.core.util.io.FileIOUtils;
import ca.bc.gov.health.qa.autotest.core.util.timer.EventTimer;
import ca.bc.gov.health.qa.autotest.core.util.timer.TimedEvent;

/**
 * A simple HTTP client.
 */
public class SimpleHttpClient
implements AutoCloseable
{
    private static final Logger LOG = LogManager.getLogger();

    private HttpClient client_;

    /**
     * Creates a new simple HTTP client.
     */
    public SimpleHttpClient()
    {
        this(true);
    }

    /**
     * Creates a new simple HTTP client.
     *
     * @param enableCertificateValidation
     *        ???
     *
     * TODO (AZ) - doc the parameter
     */
    public SimpleHttpClient(boolean enableCertificateValidation)
    {
        this(enableCertificateValidation, null, null);
    }

    /**
     * Creates a new simple HTTP client.
     *
     * @param enableCertificateValidation
     *        ???
     *
     * @param keyStorePath
     *        ???
     *
     * @param keyStorePassword
     *        ???
     *
     * TODO (AZ) - doc the parameters
     */
    public SimpleHttpClient(
            boolean enableCertificateValidation, Path keyStorePath, char[] keyStorePassword)
    {
        SSLContext sslContext =
                createSSLContext(enableCertificateValidation, keyStorePath, keyStorePassword);
        client_ = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL)
                .sslContext(sslContext)
                .build();
    }

    /**
     * TODO (AZ) - doc
     *
     * <p>If already closed, invoking this method has no effect.
     */
    @Override
    public void close()
    {
        client_.close();
    }

    /**
     * TODO (AZ) - doc
     *
     * @param request
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
    public SimpleHttpResponse send(SimpleHttpRequest request)
    throws InterruptedException,
           IOException
    {
        return send(request, true, true);
    }

    /**
     * TODO (AZ) - doc
     *
     * @param request
     *        ???
     *
     * @param captureRequest
     *        ???
     *
     * @param captureResponse
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
    public SimpleHttpResponse send(
            SimpleHttpRequest request, boolean captureRequest, boolean captureResponse)
    throws InterruptedException,
           IOException
    {
        // Create request artifact.
        if (captureRequest)
        {
            SimpleHttpRequest maskedRequest = request.getMaskedRequest();
            if (maskedRequest == null)
            {
                maskedRequest = request;
            }
            boolean isMaskedRequestBodyOriginal =
                    maskedRequest.getRequestBody().equals(request.getRequestBody());
            generateRequestArtifact(maskedRequest, isMaskedRequestBodyOriginal);
        }

        // Send the HTTP request.
        HttpResponse<?>  httpResponse;
        byte[]           binaryBody   = new byte[0];
        HttpResponseType responseType = request.getResponseType();
        String           textBody     = "";
        EventTimer       timer        = new EventTimer(request.getTransactionName());
        TimedEvent       transactionTiming;
        switch (responseType)
        {
            case BINARY:
            {
                timer.start();
                HttpResponse<byte[]> binaryHttpResponse =
                        client_.send(request.getHttpRequest(), BodyHandlers.ofByteArray());
                transactionTiming = timer.measure();
                binaryBody   = binaryHttpResponse.body();
                httpResponse = binaryHttpResponse;
                break;
            }

            case TEXT:
            {
                timer.start();
                HttpResponse<String> textHttpResponse =
                        client_.send(request.getHttpRequest(), BodyHandlers.ofString());
                transactionTiming = timer.measure();
                textBody     = textHttpResponse.body();
                httpResponse = textHttpResponse;
                break;
            }

            default:
                String msg = String.format("Unsupported response type (%s).", responseType);
                throw new IllegalStateException(msg);
        }
        SimpleHttpResponse response = new SimpleHttpResponse(
                httpResponse.uri(),
                httpResponse.statusCode(),
                httpResponse.version().toString(),
                httpResponse.headers().map(),
                responseType,
                binaryBody,
                textBody,
                transactionTiming);

        // Create response artifacts.
        if (captureResponse)
        {
            generateResponseArtifacts(response, true);
        }

        return response;
    }

    /**
     * TODO (AZ) - doc
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
    public static void generateRequestArtifact(
            SimpleHttpRequest request, boolean isRequestBodyOriginal)
    {
        requireNonNull(request, "Null request.");
        String name = formatArtifactName(request.getTransactionName(), "request.txt");
        LocalContext.addArtifact(
                new SimpleHttpRequestArtifact(name, request, isRequestBodyOriginal));
    }

    /**
     * TODO (AZ) - doc
     *
     * @param response
     *        ???
     *
     * @param isResponseBodyOriginal
     *        ???
     *
     * @throws NullPointerException
     *         if {@code response} is {@code null}
     */
    public static void generateResponseArtifacts(
            SimpleHttpResponse response, boolean isResponseBodyOriginal)
    {
        requireNonNull(response, "Null response.");
        TimedEvent transactionTiming = response.getTransactionTiming();
        String     transactionName   = transactionTiming.getName();
        Instant    time              = transactionTiming.getEndTime();
        String     name              = formatArtifactName(transactionName, "response.txt");
        LocalContext.addArtifact(
                new SimpleHttpResponseArtifact(name, response, isResponseBodyOriginal));

        name = formatArtifactName(transactionName, "response-content" + detectExtension(response));
        switch(response.getResponseType())
        {
            case BINARY:
                LocalContext.addArtifact(
                        new BinaryArtifact(name, response.getBinaryResponseBody(), time));
                break;

            case TEXT:
                LocalContext.addArtifact(
                        new TextArtifact(name, response.getTextResponseBody(), null, time));
                break;
        }
    }

    /**
     * TODO (AZ) - doc
     *
     * @param transactionName
     *        the transaction name to format.
     *
     * @param suffix
     *        ???
     *
     * @return formatted transaction name
     */
    public static String formatArtifactName(String transactionName, String suffix)
    {
        String name =
                FileIOUtils.sanitizeFileName(transactionName) +
                "-" +
                FileIOUtils.sanitizeFileName(suffix);
        return name.toLowerCase(Locale.ROOT).replace("_", "-");
    }

    /**
     * TODO (AZ) - doc
     *
     * @return ???
     */
    private static TrustManager createNonrestrictiveTrustManager()
    {
        // https://medium.com/javarevisited/java-http-client-invalid-certificate-93673415fdec
        TrustManager trustManager = new X509ExtendedTrustManager()
        {
            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain, String authType)
            {}

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain, String authType)
            {}

            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain, String authType, Socket socket)
            {}

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain, String authType, Socket socket)
            {}

            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain, String authType, SSLEngine engine)
            {}

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain, String authType, SSLEngine engine)
            {}
        };
        return trustManager;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param enableCertificateValidation
     *        ???
     *
     * @param keyStorePath
     *        ???
     *
     * @param keyStorePassword
     *        ???
     *
     * @return ???
     */
    private static SSLContext createSSLContext(
            boolean enableCertificateValidation, Path keyStorePath, char[] keyStorePassword)
    {
        SSLContext sslContext;
        try
        {
            TrustManager[] trustManagers;
            if (enableCertificateValidation)
            {
                trustManagers = null;
            }
            else
            {
                trustManagers = new TrustManager[]{createNonrestrictiveTrustManager()};
            }

            KeyManager[] keyManagers;
            if (keyStorePath == null)
            {
                keyManagers = null;
            }
            else
            {
                KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePassword);
                KeyManagerFactory keyManagerFactory =
                        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, keyStorePassword);
                keyManagers = keyManagerFactory.getKeyManagers();
            }

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, null);
        }
        catch (GeneralSecurityException | IOException e)
        {
            throw new IllegalStateException("Failed to create SSL context.", e);
        }
        return sslContext;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param path
     *        ???
     *
     * @param password
     *        ???
     *
     * @return ???
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    private static KeyStore loadKeyStore(Path path, char[] password)
    throws IOException
    {
        requireNonNull(path,     "Null key store path.");
        requireNonNull(password, "Null key store password.");
        KeyStore keyStore;
        try (FileInputStream fis = new FileInputStream(path.toFile()))
        {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(fis, password);
        }
        catch (GeneralSecurityException e)
        {
            throw new IllegalStateException("Failed to load keystore.", e);
        }
        return keyStore;
    }

    /**
     * TODO (AZ) - doc
     *
     * @param response
     *        ???
     *
     * @return ???
     */
    private static String detectExtension(SimpleHttpResponse response)
    {
        List<String> contentTypeList = response.getResponseHeaderMap().get("content-type");
        String contentType = null;
        if (contentTypeList != null && contentTypeList.size() > 0)
        {
            contentType = contentTypeList.get(0);
        }
        String extension = MediaTypeUtils.getExtensionForMediaType(contentType);
        if (extension == null)
        {
            LOG.warn("File extension not found for content type ({}).", contentType);
            if (response.getResponseType() == HttpResponseType.TEXT)
            {
                extension = ".txt";
            }
            else
            {
                extension = ".bin";
            }
        }
        return extension;
    }
}
