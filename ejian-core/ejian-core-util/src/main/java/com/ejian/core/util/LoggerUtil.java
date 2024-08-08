package com.ejian.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class LoggerUtil {

    private final static Map<String, Logger> loggerMap = new HashMap<>();

    private static Logger getLogger(String className) {
        Logger logger = null;
        if (loggerMap.containsKey(className)) {
            logger = loggerMap.get(className);
        } else {
            try {
                logger = LogManager.getLogger(Class.forName(className));
                loggerMap.put(className, logger);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    /**
     * 获取最开始的调用者所在类
     *
     * @return
     */
    private static String getClassName() {
        Throwable th = new Throwable();
        StackTraceElement[] stes = th.getStackTrace();
        StackTraceElement ste = stes[2];
        return ste.getClassName();
    }

    public void trace(Object message) {
        String className = getClassName();
        getLogger(className).trace(message);
    }

    public static void debug(Object message) {
        String className = getClassName();
        getLogger(className).debug(message);
    }

    public static void info(Object message) {
        String className = getClassName();
        getLogger(className).info(message);
    }

    public static void warn(Object message) {
        String className = getClassName();
        getLogger(className).warn(message);
    }

    public static void error(Object message) {
        String className = getClassName();
        getLogger(className).error(message);
    }

    public static void error(String msg,Object obj) {
        String className = getClassName();
        getLogger(className).error(msg,obj);
    }

    public static void fatal(Object message) {
        String className = getClassName();
        getLogger(className).fatal(message);
    }
}
