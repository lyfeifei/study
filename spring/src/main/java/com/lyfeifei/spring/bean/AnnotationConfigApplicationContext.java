package com.lyfeifei.spring.bean;

import com.lyfeifei.spring.annotation.Autowired;
import com.lyfeifei.spring.annotation.Component;
import com.lyfeifei.spring.annotation.ComponentScan;
import com.lyfeifei.spring.annotation.Scope;
import com.lyfeifei.spring.frameWork.BeanNameAware;
import com.lyfeifei.spring.frameWork.InitializingBean;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationConfigApplicationContext {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonObjects = new HashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public AnnotationConfigApplicationContext(Class configClass) {
        // 1、扫描
        scan(configClass);

        // 2、初始化
        instance();
    }

    public void scan(Class configClass) {
        // 获取配置类上的注解信息
        ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String path = componentScan.value();
        System.out.println("扫描路径：" + path);

        List<Class> classes = loaderClass(path);
        // 遍历class得到BeanDefinition
        for (Class clazz : classes) {
            // 判断是否含有@Component
            if (clazz.isAnnotationPresent(Component.class)) {
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setBeanClass(clazz);

                Component component = (Component) clazz.getAnnotation(Component.class);
                String beanName = component.value();

                // 判断当前类是否是BeanPostProcessor的派生类
                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                    try {
                        BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                        beanPostProcessorList.add(instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 解析scope
                if (clazz.isAnnotationPresent(Scope.class)) {
                    Scope scope = (Scope) clazz.getAnnotation(Scope.class);
                    String scopeValue = scope.value();
                    if (ScopeEnum.singleton.name().equals(scopeValue)) {
                        beanDefinition.setScope(ScopeEnum.singleton);
                    } else {
                        beanDefinition.setScope(ScopeEnum.prototype);
                    }
                } else {
                    beanDefinition.setScope(ScopeEnum.singleton);
                }

                beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }

    private List<Class> loaderClass(String path) {
        List<Class> beanClasses = new ArrayList<>();
        ClassLoader classLoader = AnnotationConfigApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path.replace(".", "/"));
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath(); // D:\Git\study\spring\target\classes\com\lyfeifei\spring\service\UserService.class
                String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                className = className.replace("\\", ".");

                // 加载class文件
                try {
                    System.out.println("加载文件：" + className);
                    Class loadClass = classLoader.loadClass(className);
                    beanClasses.add(loadClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return beanClasses;
    }

    // 从beanDefinitionMap中获取bean进行实例化
    private void instance() {
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals(ScopeEnum.singleton)) {
                Object bean = doCreateBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    // 基于BeanDefinition来创建bean
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Class definitionClass = beanDefinition.getClass();
        try {
            // 通过获取默认构造器进行实例化
            Constructor declaredConstructor = definitionClass.getDeclaredConstructor();
            Object instance = declaredConstructor.newInstance();

            // 填充属性
            Field[] fields = definitionClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String fieldName = field.getName();

                    // 对通过Autowired注入的bean进行实例化
                    Object bean = getBean(fieldName);

                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }

            // Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getBean(String beanName) {
        if (singletonObjects.containsKey(beanName)) {
            return singletonObjects.get(beanName);
        } else {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            return doCreateBean(beanName, beanDefinition);
        }
    }
}
