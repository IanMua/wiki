package com.ianmu.wiki.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CopyUtil {

    /**
     * 单对象复制
     *
     * @param source 目标对象
     * @param clazz  结果类
     * @param <T>    any
     * @return 结果对象
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }

        T obj = null;
        try {
            obj = clazz.getConstructor().newInstance();

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(source, obj);
        return obj;
    }

    public static <K, T> List<T> copyList(List<K> sourceList, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sourceList)) {
            for (Object o : sourceList) {
                T obj = copy(o, clazz);
                results.add(obj);
            }
        }
        return results;
    }
}
