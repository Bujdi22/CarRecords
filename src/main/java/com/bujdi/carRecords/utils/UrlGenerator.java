package com.bujdi.carRecords.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlGenerator {
    private static String appUrl;
    private static String serverUrl;

    public UrlGenerator(@Value("${app.url}") String appUrl, @Value("${server.url}") String serverUrl) {
        UrlGenerator.appUrl = appUrl;
        UrlGenerator.serverUrl = serverUrl;
    }
    public static String generateUrl(String path) {
        return appUrl + path;
    }
    public static String generateServerUrl(String path) {
        return serverUrl + path;
    }
}
