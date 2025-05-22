package com.ddd.api.config.web;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

final class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
            WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> defaultAttributes = super.getErrorAttributes(webRequest, options);

        Map<String, Object> customAttributes = new LinkedHashMap<>();

        customAttributes.put("type", "about:blank");
        customAttributes.put("title", defaultAttributes.get("error"));
        customAttributes.put("status", defaultAttributes.get("status"));
        customAttributes.put("detail", defaultAttributes.get("message"));
        customAttributes.put("instance", defaultAttributes.get("path"));

        return customAttributes;
    }
}
