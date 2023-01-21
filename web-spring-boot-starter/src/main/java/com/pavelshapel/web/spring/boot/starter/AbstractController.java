//package com.pavelshapel.web.spring.boot.starter;
//
//import java.io.BufferedReader;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.lmig.grs.ssi.api.openapi.model.SimpleResponse;
//
//import lombok.SneakyThrows;
//
///**
// * An abstract class contains common methods, constants for controllers that extend it.
// *
// * @author Pavel Shapel
// */
//
//public abstract class AbstractController {
//    public static final String MESSAGE = "message";
//    public static final String SUCCESS = "success";
//    public static final String URL = "url";
//    public static final String QUERY_STRING = "queryString";
//    public static final String BODY = "body";
//
//    /**
//     * Create simple response with "success" message and current http request details
//     *
//     * @return successful simple response
//     */
//    protected SimpleResponse createSimpleSuccessResponse() {
//        Map<String, String> request = new LinkedHashMap<>();
//        putUrlToResponse(request);
//        putQueryStringToResponse(request);
//        putBodyToResponse(request);
//        return new SimpleResponse()
//                .message(SUCCESS)
//                .request(request);
//    }
//
//    private void putUrlToResponse(Map<String, String> request) {
//        getOptionalCurrentHttpRequest()
//                .map(HttpServletRequest::getRequestURL)
//                .map(StringBuffer::toString)
//                .filter(StringUtils::isNotBlank)
//                .ifPresent(url -> request.put(URL, url));
//    }
//
//    private void putQueryStringToResponse(Map<String, String> request) {
//        getOptionalCurrentHttpRequest()
//                .map(HttpServletRequest::getQueryString)
//                .filter(StringUtils::isNotBlank)
//                .ifPresent(queryString -> request.put(QUERY_STRING, queryString));
//    }
//
//    private void putBodyToResponse(Map<String, String> request) {
//        getOptionalCurrentHttpRequest()
//                .map(this::getReader)
//                .map(BufferedReader::lines)
//                .map(stream -> stream.collect(Collectors.joining(System.lineSeparator())))
//                .filter(StringUtils::isNotBlank)
//                .ifPresent(body -> request.put(BODY, body));
//    }
//
//    private Optional<HttpServletRequest> getOptionalCurrentHttpRequest() {
//        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
//                .filter(ServletRequestAttributes.class::isInstance)
//                .map(ServletRequestAttributes.class::cast)
//                .map(ServletRequestAttributes::getRequest);
//    }
//
//    @SneakyThrows
//    private BufferedReader getReader(HttpServletRequest httpServletRequest) {
//        return httpServletRequest.getReader();
//    }
//}
