package com.ejian.core.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapObjectUtil {

    /**
     * Map 转换为 对象
     *
     * @param source 源
     * @param target 目标对象
     * @param <T>    类型
     * @return
     * @throws Exception
     */
    public static <T> T mapToObj(Map<String, Object> source, Class<T> target) throws Exception {
        Field[] fields = target.getDeclaredFields();
        T o = target.getDeclaredConstructor().newInstance();
        for (Field field : fields) {
            Object val;
            if ((val = source.get(field.getName())) != null) {
                field.setAccessible(true);
                field.set(o, val);
            }
        }
        return o;
    }

    /**
     * map 转 object
     * @param source 对象
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objToMap(Object source) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (source == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if(propertyDescriptor.getName().startsWith("min") || propertyDescriptor.getName().startsWith("max")){
                continue;
            }
            String key = propertyDescriptor.getName().replaceAll("([A-Z])", "_$1").toLowerCase();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = propertyDescriptor.getReadMethod();
            Object value = getter != null ? getter.invoke(source) : null;
            map.put(key, value);
        }
        return map;
    }

}
