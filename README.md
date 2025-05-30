# Sanguispring

一款简易版的 Spring 风格 IoC 容器，用于学习和演示依赖注入原理。

------

## 项目简介

Sanguispring 是作者三桂手写的一个极简化的 Spring 风格 IoC 容器，核心功能仅包含：

- 基于 XML 配置的 Bean 加载
- 基于反射的依赖注入（属性注入）
- 单例 Bean 管理

此项目主要目的是记录学习过程，并非生产级框架。欢迎 Fork 与交流。

------

## 核心代码结构

```text
com.sangui.sanguispring.core
├── ApplicationContext.java           # 应用上下文接口
└── ClassPathXmlApplicationContext.java  # 核心实现
```

配置文件：

```xml
<!-- sanguispring.xml -->
<beans>
    <bean id="vip" class="com.sangui.bean.Vip">
        <property name="name" value="lisi"/>
        <property name="age" value="44"/>
        <property name="height" value="1.77"/>
    </bean>
    <bean id="orderDao" class="com.sangui.dao.OrderDao"/>
    <bean id="orderService" class="com.sangui.service.OrderService">
        <property name="orderDao" ref="orderDao"/>
    </bean>
</beans>
```

------

## 安装与使用

1. 克隆仓库：

   ```bash
   git clone git@github.com:WuSangui571/sanguispring.git
   ```

​	 cd sanguispring

2. 将 `sanguispring.xml` 放到类路径下（如 `src/main/resources/`）。

3. 在项目中引入包并在代码中获取 Bean：

   ```java
   ApplicationContext ctx = new ClassPathXmlApplicationContext("sanguispring.xml");
   MyBean bean = (MyBean) ctx.getBean("myBeanId");
   ```

4. 运行测试：

   ```java
   mvn test
   ```

## 功能特性

- **XML 配置**：通过简单的 `<bean>` 和 `<property>` 标签定义 Bean
- **单例管理**：所有 Bean 默认单例，存储在 `singletonMap`
- **属性注入**：支持将配置文件中的 `value` 或 `ref` 注入到 POJO 属性中，通过反射调用 `setXxx()` 方法

### 支持的简单类型注入

| 类型     | 支持方式                                                     |
| -------- | ------------------------------------------------------------ |
| 原始类型 | `boolean`、`byte`、`short`、`int`、`long`、`float`、`double`、`char` |
| 包装类型 | `Boolean`、`Byte`、`Short`、`Integer`、`Long`、`Float`、`Double`、`Character` |
| 其他     | 默认按 `String` 处理                                         |

---

## 已知问题与限制

1. **仅支持属性注入**，不支持构造函数注入（除默认无参构造）
2. **只能注入有限的简单类型及其包装类型**，不支持数组、集合、枚举等复杂类型
3. **无循环依赖检测**，若存在循环依赖会导致栈溢出或空指针
4. **无作用域管理**，所有 Bean 均为单例
5. **异常处理简单**，加载或反射异常仅打印堆栈，未抛出清晰的自定义异常
6. **不支持 Bean 后处理器、AOP 等高级功能**
7. **仅支持 XML 配置**，不支持注解或 Java 配置

---

## 待改进（TODO）

- 增加构造函数注入和多构造重载支持
- 支持 `List`、`Map`、`Set` 等集合类型注入
- 实现 Bean 生命周期回调（`InitializingBean`、`DisposableBean`）
- 增加注解配置（如 `@Component`、`@Autowired`）
- 支持作用域（`prototype`、`request`、`session`）
- 循环依赖检测与解决
- 更完善的异常与日志体系

---

## 示例

在 `SanguiTest.java` 中演示了如何加载容器并获取 Bean：

```java
ApplicationContext context = new ClassPathXmlApplicationContext("sanguispring.xml");
OrderService orderService = (OrderService) context.getBean("orderService");
System.out.println(orderService);
orderService.save();
Vip vip = (Vip) context.getBean("vip");
System.out.println(vip);
```

