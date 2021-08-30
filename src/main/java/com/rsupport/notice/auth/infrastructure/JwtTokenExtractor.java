package com.rsupport.notice.auth.infrastructure;

import java.util.Collections;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public class JwtTokenExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_TYPE = "Bearer";
    public static final String ACCESS_TOKEN_TYPE = JwtTokenExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    protected JwtTokenExtractor() {
    }

    public static Optional<String> extractToken(HttpServletRequest request) {
        return Collections.list(request.getHeaders(AUTHORIZATION))
                .stream()
                .filter(JwtTokenExtractor::isBearerTypePrefix)
                .findFirst()
                .map(value -> getToken(request, value));
    }

    private static String getToken(HttpServletRequest request, String value) {
        request.setAttribute(ACCESS_TOKEN_TYPE, getBearerTypePrefix(value));
        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
        int commaIndex = authHeaderValue.indexOf(",");
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
    }

    private static boolean isBearerTypePrefix(String value) {
        return value.toLowerCase()
                .startsWith(BEARER_TYPE.toLowerCase());
    }

    private static String getBearerTypePrefix(String value) {
        return value.substring(0, BEARER_TYPE.length()).trim();
    }
}
