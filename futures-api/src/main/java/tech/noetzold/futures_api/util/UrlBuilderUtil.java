package tech.noetzold.futures_api.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

public class UrlBuilderUtil {

    public static URI buildUriWithParams(String path, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);
        if (params != null) {
            params.forEach(builder::queryParam);
        }
        return builder.build(true).toUri();
    }
}
