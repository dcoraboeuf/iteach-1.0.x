package net.nemerosa.iteach.ui.client.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.UntitledDocument;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import net.nemerosa.iteach.ui.client.UIClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static java.lang.String.format;

public abstract class AbstractClient<C extends UIClient<C>> implements UIClient<C> {

    private static final CookieStore cookieStore = new BasicCookieStore();
    private final Logger logger = LoggerFactory.getLogger(AbstractClient.class);
    private final ObjectMapper mapper = ObjectMapperFactory.create();
    private final String url;
    private final HttpHost host;
    private HttpClientContext httpContext = HttpClientContext.create();

    public AbstractClient(String url) throws MalformedURLException {
        this.url = url;
        URL u = new URL(url);
        this.host = new HttpHost(
                u.getHost(),
                u.getPort(),
                u.getProtocol()
        );
    }

    private CloseableHttpClient http() {
        return httpBuilder().build();
    }

    private HttpClientBuilder httpBuilder() {
        return HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager());
    }

    @SuppressWarnings("unchecked")
    @Override
    public C withBasicLogin(String email, String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(host),
                new UsernamePasswordCredentials(email, password)
        );

        AuthCache authCache = new BasicAuthCache();
        authCache.put(host, new BasicScheme());

        httpContext = HttpClientContext.create();
        httpContext.setCredentialsProvider(credentialsProvider);
        httpContext.setAuthCache(authCache);
        httpContext.setCookieStore(cookieStore);

        return (C) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public C anonymous() {
        httpContext = HttpClientContext.create();
        return (C) this;
    }

    @Override
    public void logout() {
        logger.debug("[client] Logout");
        // Executes the call
        get(Locale.ENGLISH, Ack.class, "/api/account/logout");
    }

    protected String getUrl(String path, Object... parameters) {
        return format("%s%s", url, format(path, parameters));
    }

    protected <T> T get(Locale locale, Class<T> returnType, String path, Object... parameters) {
        return request(locale, new HttpGet(getUrl(path, parameters)), returnType);
    }

    protected <T> T delete(Locale locale, Class<T> returnType, String path, Object... parameters) {
        return request(locale, new HttpDelete(getUrl(path, parameters)), returnType);
    }

    protected <T> T post(Locale locale, Class<T> returnType, Object body, String path, Object... parameters) {
        HttpPost post = new HttpPost(getUrl(path, parameters));
        if (body != null) {
            setBody(body, post);
        }
        return request(locale, post, returnType);
    }

    protected <T> T put(Locale locale, Class<T> returnType, Object body, String path, Object... parameters) {
        HttpPut put = new HttpPut(getUrl(path, parameters));
        if (body != null) {
            setBody(body, put);
        }
        return request(locale, put, returnType);
    }

    private void setBody(Object payload, HttpEntityEnclosingRequestBase put) {
        try {
            String json = mapper.writeValueAsString(payload);
            put.setEntity(new StringEntity(json, ContentType.create("application/json", "UTF-8")));
        } catch (IOException e) {
            throw new ClientGeneralException(put, e);
        }
    }

    protected <T> T upload(Locale locale, Class<T> returnType, String fileName, JsonNode node, String path, Object... parameters) {
        try {
            return upload(
                    locale,
                    returnType,
                    new UntitledDocument(
                            ContentType.APPLICATION_JSON.getMimeType(),
                            mapper.writeValueAsBytes(node)
                    ),
                    path, parameters
            );
        } catch (JsonProcessingException e) {
            throw new ClientException(e);
        }
    }

    protected <T> T upload(Locale locale, Class<T> returnType, UntitledDocument document, String path, Object... parameters) {
        HttpPost post = new HttpPost(getUrl(path, parameters));
        // Sets the content
        post.setEntity(
                MultipartEntityBuilder.create()
                        .addBinaryBody(
                                "file",
                                document.getContent(),
                                ContentType.create(document.getType()),
                                "file"
                        )
                        .build()
        );
        // OK
        return request(locale, post, returnType);
    }

    protected <T> T request(Locale locale, HttpRequestBase request, Class<T> returnType) {
        return request(locale, request, new SimpleTypeResponseParser<>(returnType));
    }

    protected <T> T request(Locale locale, HttpRequestBase request, final ResponseParser<T> responseParser) {
        return request(locale, request, new BaseResponseHandler<T>() {
            @Override
            protected T handleEntity(HttpEntity entity) throws ParseException, IOException {
                // Gets the content as a JSON string
                String content = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
                // Parses the response
                return responseParser.parse(content);
            }
        });
    }

    protected <T> T request(Locale locale, HttpRequestBase request, ResponseHandler<T> responseHandler) {
        logger.debug("[request] {}", request);
        request.setHeader("Accept-Language", locale != null ? locale.toString() : "en");
        // Executes the call
        try {
            try (CloseableHttpClient http = http()) {
                HttpResponse response = http.execute(host, request, httpContext);
                logger.debug("[response] {}", response);
                // Entity response
                HttpEntity entity = response.getEntity();
                try {
                    return responseHandler.handleResponse(request, response, entity);
                } finally {
                    EntityUtils.consume(entity);
                }
            }
        } catch (IOException e) {
            throw new ClientGeneralException(request, e);
        } finally {
            request.releaseConnection();
        }
    }

    protected class SimpleTypeResponseParser<T> implements ResponseParser<T> {

        private final Class<T> type;

        public SimpleTypeResponseParser(Class<T> type) {
            this.type = type;
        }

        @Override
        public T parse(String content) throws IOException {
            if (content != null) {
                return mapper.readValue(content, type);
            } else {
                return null;
            }
        }
    }

    protected static abstract class BaseResponseHandler<T> implements ResponseHandler<T> {

        @Override
        public T handleResponse(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ParseException, IOException {
            // Parses the response
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return handleEntity(entity);
            } else if (statusCode == HttpStatus.SC_BAD_REQUEST) {
                throw new ClientValidationException(getMessage(response));
            } else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new ClientCannotLoginException(request);
            } else if (statusCode == HttpStatus.SC_FORBIDDEN) {
                throw new ClientForbiddenException(request);
            } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                throw new ClientNotFoundException(getMessage(response));
            } else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                String content = getMessage(response);
                if (StringUtils.isNotBlank(content)) {
                    throw new ClientMessageException(content);
                } else {
                    // Generic error
                    throw new ClientServerException(
                            request,
                            statusCode,
                            response.getStatusLine().getReasonPhrase());
                }
            } else {
                // Generic error
                throw new ClientServerException(
                        request,
                        statusCode,
                        response.getStatusLine().getReasonPhrase());
            }
        }

        protected abstract T handleEntity(HttpEntity entity) throws ParseException, IOException;

    }

    private static String getMessage(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    @FunctionalInterface
    protected static interface ResponseParser<T> {

        T parse(String content) throws IOException;

    }

    @FunctionalInterface
    protected static interface ResponseHandler<T> {

        T handleResponse(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ParseException, IOException;

    }
}
