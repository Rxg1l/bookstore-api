// package com.bookstore.config;

// import jakarta.servlet.*;
// import jakarta.servlet.http.HttpServletRequest;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;

// import java.io.IOException;

// @Component
// @Slf4j
// public class MultipartFilter implements Filter {

// @Override
// public void doFilter(ServletRequest request, ServletResponse response,
// FilterChain chain)
// throws IOException, ServletException {

// HttpServletRequest httpRequest = (HttpServletRequest) request;
// String contentType = httpRequest.getContentType();

// log.debug("Request Content-Type: {}", contentType);

// // Fix for multipart requests with charset
// if (contentType != null && contentType.startsWith("multipart/")) {
// // Remove charset from content-type for multipart requests
// if (contentType.contains("charset=")) {
// String fixedContentType = contentType.replaceAll(";\\s*charset=[^;]+", "");
// log.debug("Fixed Content-Type: {}", fixedContentType);

// // Wrap the request with fixed content-type
// HttpServletRequest wrappedRequest = new
// ContentTypeOverrideRequest(httpRequest, fixedContentType);
// chain.doFilter(wrappedRequest, response);
// return;
// }
// }

// chain.doFilter(request, response);
// }

// // Custom HttpServletRequest wrapper to override content-type
// private static class ContentTypeOverrideRequest extends
// jakarta.servlet.http.HttpServletRequestWrapper {
// private final String contentType;

// public ContentTypeOverrideRequest(HttpServletRequest request, String
// contentType) {
// super(request);
// this.contentType = contentType;
// }

// @Override
// public String getContentType() {
// return contentType;
// }

// @Override
// public String getHeader(String name) {
// if ("content-type".equalsIgnoreCase(name)) {
// return contentType;
// }
// return super.getHeader(name);
// }
// }
// }