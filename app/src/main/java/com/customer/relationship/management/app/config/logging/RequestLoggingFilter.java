package com.customer.relationship.management.app.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            String requestBody = getContent(wrappedRequest.getContentAsByteArray());
            String responseBody = getContent(wrappedResponse.getContentAsByteArray());

            logger.info("\n--- HTTP REQUEST ---\n" +
                            "Method: {}\n" +
                            "URI: {}\n" +
                            "Query: {}\n" +
                            "Headers: {}\n" +
                            "Body: {}\n",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    getHeaders(request),
                    requestBody);

            logger.info("\n--- HTTP RESPONSE ---\n" +
                            "Status: {}\n" +
                            "Duration: {}ms\n" +
                            "Body: {}\n",
                    response.getStatus(),
                    duration,
                    responseBody);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String getContent(byte[] content) {
        try {
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "[unreadable content]";
        }
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> headers.append(name)
                        .append(": ")
                        .append(request.getHeader(name))
                        .append("; "));
        return headers.toString();
    }
}
