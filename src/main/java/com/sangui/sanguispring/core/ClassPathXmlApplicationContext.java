package com.sangui.sanguispring.core;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sangui
 * @CreateTime: 2025-05-29
 * @Description:
 * @Version: 1.0
 */
public class ClassPathXmlApplicationContext implements ApplicationContext{
    private static Logger log = LoggerFactory.getLogger(ClassPathXmlApplicationContext.class);
    private Map<String,Object> singletonMap = new HashMap<>();

    /**
     * 构造方法，解析 xml 文件 ，然后初始化所有的 Bean 对象
     * @param configLocation sanguispring配置文件的路径
     */
    public ClassPathXmlApplicationContext(String configLocation) {
        try {
            SAXReader saxReader = new SAXReader();
            InputStream inputStream = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream(configLocation);
            Document document = saxReader.read(inputStream);
            Element beans = document.getRootElement();
            List<Element> beanList = beans.elements("bean");
            for (Element bean : beanList) {
                String id = bean.attributeValue("id");
                String clazzStr = bean.attributeValue("class");
                log.info("bean id:{}, class:{}", id, clazzStr);
                Class<?> clazz = Class.forName(clazzStr);
                Object o = clazz.getDeclaredConstructor().newInstance();
                // 曝光
                singletonMap.put(id, o);
                log.info(singletonMap.toString());
            }
            for (Element bean : beanList) {
                String id = bean.attributeValue("id");
                String clazzStr = bean.attributeValue("class");
                Class<?> clazz = Class.forName(clazzStr);
                Object o = singletonMap.get(id);
                List<Element> propertyList = bean.elements("property");
                for (Element property : propertyList) {
                    String propertyName = property.attributeValue("name");
                    Field field = clazz.getDeclaredField(propertyName);
                    log.info("property name:{}", propertyName);
                    String setMethodName = "set" + propertyName.toUpperCase().charAt(0) + propertyName.substring(1);
                    Method setMethod = clazz.getDeclaredMethod(setMethodName,field.getType());

                    String value = property.attributeValue("value");
                    if (value != null) {
                        String propertyTypeSimpleName = field.getType().getSimpleName();
                        switch (propertyTypeSimpleName) {
                            case "boolean":
                                setMethod.invoke(o,Boolean.parseBoolean(value));
                                break;
                            case "Boolean":
                                setMethod.invoke(o,Boolean.valueOf(value));
                                break;
                            case "byte":
                                setMethod.invoke(o,Byte.parseByte(value));
                                break;
                            case "Byte":
                                setMethod.invoke(o,Byte.valueOf(value));
                                break;
                            case "short":
                                setMethod.invoke(o,Short.parseShort(value));
                                break;
                            case "Short":
                                setMethod.invoke(o,Short.valueOf(value));
                                break;
                            case "int":
                                setMethod.invoke(o,Integer.parseInt(value));
                                break;
                            case "Integer":
                                setMethod.invoke(o,Integer.valueOf(value));
                                break;
                            case "long":
                                setMethod.invoke(o,Long.parseLong(value));
                                break;
                            case "Long":
                                setMethod.invoke(o,Long.valueOf(value));
                                break;
                            case "float":
                                setMethod.invoke(o,Float.parseFloat(value));
                                break;
                            case "Float":
                                setMethod.invoke(o,Float.valueOf(value));
                                break;
                            case "double":
                                setMethod.invoke(o,Double.parseDouble(value));
                                break;
                            case "Double":
                                setMethod.invoke(o,Double.valueOf(value));
                                break;
                            case "char":
                                setMethod.invoke(o,value.charAt(0));
                                break;
                            case "Character":
                                setMethod.invoke(o,Character.valueOf(value.charAt(0)));
                                break;
                            default:
                                setMethod.invoke(o,value);
                        }
                    }
                    String ref = property.attributeValue("ref");
                    if (ref != null) {
                        setMethod.invoke(o,singletonMap.get(ref));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String name) {
        return singletonMap.get(name);
    }
}
