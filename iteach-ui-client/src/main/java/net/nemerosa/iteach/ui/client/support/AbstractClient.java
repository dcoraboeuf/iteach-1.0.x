package net.nemerosa.iteach.ui.client.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

public abstract class AbstractClient {

    private final Logger logger = LoggerFactory.getLogger(AbstractClient.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final String url;
    private final CloseableHttpClient httpClient;

    public AbstractClient(String url) {
        this.url = url;
        this.httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .build();
    }

    protected String getUrl(String path, Object... parameters) {
        return format("%s%s", url, format(path, parameters));
    }

    protected <T> T get(Locale locale, Class<T> returnType, String path, Object... parameters) {
        return request(locale, new HttpGet(getUrl(path, parameters)), returnType);
    }

    protected <T> List<T> list(Locale locale, final Class<T> elementType, String path, Object... parameters) {
        return request(locale, new HttpGet(getUrl(path, parameters)), content -> {
            JsonNode node = mapper.readTree(content);
            if (node.isArray()) {
                return Lists.newArrayList(
                        Iterables.transform(node, input -> {
                            try {
                                return mapper.treeToValue(input, elementType);
                            } catch (IOException e) {
                                throw new ClientGeneralException(path, e);
                            }
                        })
                );
            } else {
                throw new IOException("Did not receive a JSON array");
            }
        });
    }

    protected <T> T post(Locale locale, Class<T> returnType, Object body, String path, Object... parameters) {
        HttpPost post = new HttpPost(getUrl(path, parameters));
        if (body != null) {
            setBody(body, post);
        }
        return request(locale, post, returnType);
    }

    private void setBody(Object payload, HttpEntityEnclosingRequestBase put) {
        try {
            String json = mapper.writeValueAsString(payload);
            put.setEntity(new StringEntity(json, ContentType.create("application/json", "UTF-8")));
        } catch (IOException e) {
            throw new ClientGeneralException(put, e);
        }
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
            HttpResponse response = httpClient.execute(request);
            logger.debug("[response] {}", response);
            // Entity response
            HttpEntity entity = response.getEntity();
            try {
                return responseHandler.handleResponse(request, response, entity);
            } finally {
                EntityUtils.consume(entity);
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
            } else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new ClientCannotLoginException(request);
            } else if (statusCode == HttpStatus.SC_FORBIDDEN) {
                throw new ClientForbiddenException(request);
            } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                return handleEntity(null);
            } else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
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

    @FunctionalInterface
    protected static interface ResponseParser<T> {

        T parse(String content) throws IOException;

    }

    @FunctionalInterface
    protected static interface ResponseHandler<T> {

        T handleResponse(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ParseException, IOException;

    }
}
