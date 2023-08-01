package com.ianmu.wiki.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonUtil<T> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    public static String toJson(Object object) {

        ObjectMapper objectMapper = new ObjectMapper();

        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("对象转换json错误", e);
        }

        return json;
    }

    public T toObject(String json, Class<T> valueType) {

        ObjectMapper objectMapper = new ObjectMapper();

        T anyObject = null;
        try {
            anyObject = objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            LOG.error("json转换对象错误", e);
        }

        return anyObject;
    }

    public static String propertyPreFilters(Map<String, Object> dynamicProperties) {
        ObjectMapper objectMapper = new ObjectMapper();
        DynamicPropertiesWrapper wrapper = new DynamicPropertiesWrapper();
        wrapper.setDynamicProperties(dynamicProperties);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(wrapper);
        } catch (JsonProcessingException e) {
            LOG.error("过滤器对象转换json错误", e);
        }
        return json;
    }

    @Data
    static class DynamicPropertiesWrapper {
        @JsonIgnore
        private Map<String, Object> dynamicProperties;
    }
}
