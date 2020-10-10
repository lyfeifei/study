package com.lyfeifei.spring.frameWork;

public interface BeanPostProcessor {

    public void postProcessBeforeInitialization(String beanName, Object bean);
    public void postProcessAfterInitialization(String beanName, Object bean);
}
