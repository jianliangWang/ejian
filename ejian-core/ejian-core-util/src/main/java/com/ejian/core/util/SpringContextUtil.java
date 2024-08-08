package com.ejian.core.util;

import org.springframework.context.ApplicationContext;

/**
 * @description: 获取spring的bean
 * @author: wjl
 * @date: 2023/11/14 16:06
 * @since
 **/
public class SpringContextUtil {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static Object getBean(Class<?> className){
        return applicationContext.getBean(className);
    }
}
