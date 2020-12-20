package com.springboot.md.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/8/31 上午11:05
 * @Description 此处可以拿到spring上下文环境
 */
@Component
@Slf4j
public class ApplicationContexHolder implements ApplicationContextAware {

	private static final Map<String, Object> SERVICE_FACTORY = new HashMap<>();
	private static ApplicationContext applicationContext;

	private static <T> T getBean(Class<T> t) {
		return applicationContext.getBean(t);
	}

	public static <T> Map<String, T> getBeanOfType(Class<T> t) {
		return applicationContext.getBeansOfType(t);
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	@SuppressWarnings("unchecked")
	public synchronized static <T> T getService(Class<T> tClass) {
		String name = tClass.getName();
		T t = (T) SERVICE_FACTORY.get(name);
		if (null == t) {
			t = (T) SERVICE_FACTORY.get(name);
			if (null == t) {
				t = (T) ApplicationContexHolder.getBean(name);
				SERVICE_FACTORY.put(name, t);
			}
		}
		return t;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContexHolder.applicationContext = applicationContext;
	}
}
