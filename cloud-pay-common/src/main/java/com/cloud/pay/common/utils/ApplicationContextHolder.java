package com.cloud.pay.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 工具类
 * @author wangy
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(ApplicationContextAware.class);


    private static ApplicationContext applicationContext;

    @SuppressWarnings("all")
    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        if (ApplicationContextHolder.applicationContext != null) {
            throw new IllegalStateException(
                    "ApplicationContextHolder already holded 'applicationContext'.");
        }
        ApplicationContextHolder.applicationContext = context;
        log.info("holded applicationContext,displayName:" + applicationContext.getDisplayName());
    }

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null)
            throw new IllegalStateException(
                    "'applicationContext' property is null,ApplicationContextHolder not yet init.");
        return applicationContext;
    }

    public static <T> T getBean(String beanName) throws BeansException {
        try {
            return (T) getApplicationContext().getBean(beanName);
        } catch (BeansException e) {
            throw new IllegalStateException(
                    "BeansException.cause by:" + e.getMessage());
        }
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (BeansException e) {
            throw new IllegalStateException(
                    "BeansException.cause by:" + e.getMessage());
        }
    }

    /**
     * 判断beanName是否存在
     *
     * @param beanName
     * @return
     */
    public static boolean containsBean(String beanName) {
        return getApplicationContext().containsBean(beanName);
    }

    public static void cleanHolder() {
        applicationContext = null;
    }
}