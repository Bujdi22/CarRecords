package com.bujdi.carRecords.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlGenerator {
    private static String appUrl;

    public UrlGenerator(@Value("${app.url}") String appUrl) {
        UrlGenerator.appUrl = appUrl;
    }
    public static String generateUrl(String path) {
        return appUrl + path;
    }
}
