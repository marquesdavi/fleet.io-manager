package com.api.manager.fleet.util.uri;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

public class UriUtil {

    public static URI buildUri(String basePath, Map<String, String> pathParams, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(basePath);

        if (pathParams != null) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                builder.pathSegment(entry.getValue());
            }
        }

        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        return builder.build().toUri();
    }

    public static URI buildUriWithId(String basePath, Long id) {
        return UriComponentsBuilder.fromPath(basePath)
                .pathSegment(String.valueOf(id))
                .build()
                .toUri();
    }
}

