package com.cloud.pay.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextHolder implements ApplicationListener<ContextRefreshedEvent> {
  

    private static ApplicationContext applicationContext = null;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(applicationContext == null){
            applicationContext = event.getApplicationContext();
        }

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
