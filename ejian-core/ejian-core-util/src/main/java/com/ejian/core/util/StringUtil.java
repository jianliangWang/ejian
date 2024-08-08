package com.ejian.core.util;

import java.util.Objects;

public class StringUtil {

    /**
     * 判断参数是否为空，如果为空返回默认值
     * @param properties 参数
     * @param defaultValue 默认值
     * @return 返回值
     */
    public static String getValue(String properties, String defaultValue) {

        if (Objects.nonNull(properties) && !properties.isBlank()) {
            return properties;
        }
        return defaultValue;
    }

    /**
     * 字符串是否为空，如果为空返回true
     * @param str 待验证的字符串
     * @return
     */
    public static boolean isBlank(String str){
        return Objects.isNull(str) || str.isBlank() || str.equalsIgnoreCase("null");
    }

    /**
     * 字符串是否不为空，如果不为空返回true
     * @param str 待验证的字符串
     * @return
     */
    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }
}
