package ru.loadtest.app.LoadTest.AppCore;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static ru.loadtest.app.LoadTest.AppCore.Util.*;

public class HTTPConnection {
    public static final Logger logger = LogManager.getLogger(HTTPConnection.class.getName());

    private String baseAddress;
    private BasicCookieStore cookieStore;
    private CloseableHttpClient httpClient;
    private RequestConfig requestConfig;
    private static int TIMEOUT_MS = 60000;

    HTTPConnection(String baseAddress) {
        this.baseAddress = baseAddress;
        cookieStore = new BasicCookieStore();
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT_MS)
                .setConnectTimeout(TIMEOUT_MS)
                .setConnectionRequestTimeout(TIMEOUT_MS)
                .build();
    }

    String getHTMLPageByURL(String address) {
        return getHTTPEntityContent(appendFullPath(address));
    }

    private String getHTTPEntityContent(String address) {
        HttpGet request = new HttpGet(address);
        request.setConfig(requestConfig);
        String entityContent = "";
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            entityContent = getEntityContentFromResponse(response);
            logger.info(address);
        } catch (IOException e) {
            logger.error("\nCan't get content from " + address + "\n");
        }
        return entityContent;
    }

    private String getEntityContentFromResponse(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }

    private String appendFullPath(String address) {
        StringBuilder path = new StringBuilder();
        if (!address.contains("http") && !address.contains("mailto")) {
            path.append(baseAddress);
        }
        if (!address.isEmpty()) {
            if (address.charAt(0) != '/' && !isLinkContainProtocols(address)) {
                path.append("/");
            }
        }
        path.append(address);
        return path.toString();
    }

    String getBaseAddress() {
        return baseAddress;
    }
}