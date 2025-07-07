//package com.example.DEMO_INTEGRATION.Utils.Http;
//
//import com.example.DEMO_INTEGRATION.DTOs.HttpDTO.HttpResponse;
//import com.example.DEMO_INTEGRATION.Utils.Logger.LoggerUtil;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//public class HttpUtils {
//    private final LoggerUtil loggerUtil;
//    private final RestTemplate restTemplate;
//    private static final String HEADER_PREFIX = "header_";
//
//    public HttpUtils() {
//        this.loggerUtil = LoggerUtil.getLogger(HttpUtils.class);
//        this.restTemplate = new RestTemplate(); // Assuming SSL is properly configured
//    }
//
//    public HttpResponse request(String url, HttpMethod httpMethod, String contentType, Map<String, String> headersContent, Map<String, String> bodyContent) {
//        if (url == null || url.trim().isEmpty()) {
//            loggerUtil.log(LoggerUtil.LogLevel.ERROR, "The URL cannot be null or empty.", null, null);
//            return new HttpResponse(null, "The URL cannot be null or empty.");
//        }
//
//        try {
//            // Set headers
//            HttpHeaders headers = new HttpHeaders();
//            if (headersContent != null) {
//                headersContent.forEach((key, value) -> {
//                    if (key.startsWith(HEADER_PREFIX)) {
//                        headers.add(key.substring(HEADER_PREFIX.length()), value);
//                    }
//                });
//            }
//
//            // Prepare body content
//            final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//            final StringBuilder curlContent = new StringBuilder();
//
//            if (bodyContent != null && !httpMethod.equals(HttpMethod.GET)) {
//                bodyContent.forEach((key, value) -> {
//                    body.add(key, value);
//                    if (curlContent.length() > 0) {
//                        curlContent.append("&");
//                    }
//                    curlContent.append(key).append("=").append(value);
//                });
//            }
//
//            // Set Content-Type based on contentType parameter (only for non-GET requests)
//            if (!httpMethod.equals(HttpMethod.GET)) {
//                if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
//                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                } else if ("multipart/form-data".equalsIgnoreCase(contentType)) {
//                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//                } else {
//                    headers.setContentType(MediaType.APPLICATION_JSON);
//                    if (body.isEmpty() && bodyContent != null && bodyContent.containsKey("data")) {
//                        curlContent.setLength(0); // Clear existing content
//                        curlContent.append(bodyContent.get("data")); // Modify existing StringBuilder
//                    }
//                }
//            }
//
//            // Create request entity
//               HttpEntity<?> request;
//            if (httpMethod.equals(HttpMethod.GET) || headers.getContentType() == null) {
//                request = new HttpEntity<>(headers);
//            } else if (headers.getContentType().equals(MediaType.APPLICATION_JSON) && body.isEmpty()) {
//                String jsonBody = bodyContent != null ? bodyContent.getOrDefault("data", "") : "";
//                request = new HttpEntity<>(jsonBody, headers);
//            } else {
//                request = new HttpEntity<>(body, headers);
//            }
////            // If POST + application/json + bodyContent null --> send empty body ""
////            if (httpMethod.equals(HttpMethod.POST) &&
////                    "application/json".equalsIgnoreCase(contentType) &&
////                    (bodyContent == null || bodyContent.isEmpty())) {
////
////                request = new HttpEntity<>("", headers);  // <-- send empty body
////
////            } else if (httpMethod.equals(HttpMethod.GET) || headers.getContentType() == null) {
////
////                request = new HttpEntity<>(headers);
////
////            } else if (headers.getContentType().equals(MediaType.APPLICATION_JSON) && body.isEmpty()) {
////
////                String jsonBody = bodyContent != null ? bodyContent.getOrDefault("data", "") : "";
////                request = new HttpEntity<>(jsonBody, headers);
////
////            } else {
////
////                request = new HttpEntity<>(body, headers);
////            }
//
//            // Log equivalent curl command
//            String headerString = headers.entrySet().stream()
//                    .flatMap(entry -> entry.getValue().stream().map(value -> String.format("-H \"%s: %s\"", entry.getKey(), value)))
//                    .collect(Collectors.joining(" "));
//            String curlCommand = String.format("curl -X %s \"%s\" %s %s", httpMethod, url, headerString,
//                    httpMethod.equals(HttpMethod.GET) || curlContent.length() == 0 ? "" : "-d '" + curlContent + "'");
//            loggerUtil.log(LoggerUtil.LogLevel.INFO, "Equivalent cURL command: %s", new Object[]{curlCommand}, null);
//
//            // Make HTTP request
//            ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, request, String.class);
//
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return new HttpResponse(response.getBody(), null);
//            } else if (response.getStatusCode().value() == 404) {
//                String errorMessage = String.format("Error 404: %s not found.", url);
//                loggerUtil.log(LoggerUtil.LogLevel.ERROR, errorMessage, null, null);
//                return new HttpResponse(null, errorMessage);
//            } else if (response.getStatusCode().value() == 503) {
//                String errorMessage = "Error 503: Service temporarily unavailable.";
//                loggerUtil.log(LoggerUtil.LogLevel.ERROR, errorMessage, null, null);
//                return new HttpResponse(null, errorMessage);
//            } else {
//                String errorMessage = String.format("Error: %d - %s", response.getStatusCodeValue(), getReasonPhrase((HttpStatus) response.getStatusCode()));
//                loggerUtil.log(LoggerUtil.LogLevel.ERROR, errorMessage, null, null);
//                return new HttpResponse(null, errorMessage);
//            }
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            String errorMessage = String.format("%s Request Error: %d - %s", httpMethod, e.getStatusCode().value(), getReasonPhrase((HttpStatus) e.getStatusCode()));
//            loggerUtil.log(LoggerUtil.LogLevel.ERROR, errorMessage, e,null, null);
//            return new HttpResponse(null, errorMessage);
//        } catch (Exception e) {
//            String errorMessage = String.format("Unexpected Error: %s", e.getMessage());
//            loggerUtil.log(LoggerUtil.LogLevel.ERROR, errorMessage, e,null, null);
//            return new HttpResponse(null, errorMessage);
//        }
//    }
//
//    private String getReasonPhrase(HttpStatus status) {
//        return switch (status) {
//            case OK -> "OK";
//            case CREATED -> "Created";
//            case ACCEPTED -> "Accepted";
//            case BAD_REQUEST -> "Bad Request";
//            case UNAUTHORIZED -> "Unauthorized";
//            case FORBIDDEN -> "Forbidden";
//            case NOT_FOUND -> "Not Found";
//            case TOO_MANY_REQUESTS -> "Too Many Requests";
//            case INTERNAL_SERVER_ERROR -> "Internal Server Error";
//            case SERVICE_UNAVAILABLE -> "Service Unavailable";
//            case GATEWAY_TIMEOUT -> "Gateway Timeout";
//            default -> "Unknown Error (Status: " + status.value() + ")";
//        };
//    }
//}