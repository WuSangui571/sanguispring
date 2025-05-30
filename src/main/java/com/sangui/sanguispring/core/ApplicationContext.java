package com.sangui.sanguispring.core;


/**
 * @Author: sangui
 * @CreateTime: 2025-05-29
 * @Description: 这是sanguispring的应用上下文接口，提供一个方法，就是getBean()
 * @Version: 1.0
 */
public interface ApplicationContext {
    /**
     * 根据 Bean 的名称获取 Bean 对象
     * @param name sanguispring 配置文件中的配置的 Bean 的名称
     * @return 返回单例的 Object 对象
     */
    Object getBean(String name);
    //<T>T getBean(String name,Class<T> requiredType);
}
