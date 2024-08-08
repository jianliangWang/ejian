package com.ejian.core.util;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanHelper {

    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    public static <T> T copyProperties(Object source, Class<T> target, @Nullable String ignoreProperty) {
        try {
            T t = target.getDeclaredConstructor().newInstance();
            if (ignoreProperty == null) {
                BeanUtils.copyProperties(source, t);
            } else {
                BeanUtils.copyProperties(source, t, ignoreProperty);
            }
            return t;
        } catch (Exception e) {
            logger.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            return null;
        }
    }

    public static <T> List<T> copyWithCollection(List<?> sourceList, Class<T> target) {
        try {
            return sourceList.stream().map(s -> copyProperties(s, target, null)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            return null;
        }
    }

    public static <T> Set<T> copyWithCollection(Set<?> sourceList, Class<T> target) {
        try {
            return sourceList.stream().map(s -> copyProperties(s, target, null)).collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            return null;
        }
    }

}
