spring-cloud-openfeign
====================

官方github地址: [https://github.com/OpenFeign/feign](https://github.com/OpenFeign/feign)

官方文档

- `Greenwich.SR2`

  [https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.1.2.RELEASE/](https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.1.2.RELEASE/)

- `SpringCloud：Finchley SR4`

  [https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.0.4.RELEASE/](https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.0.4.RELEASE/)

  

前提知识：`bootstrap context ` 引入了`spring-cloud-context` 这个包就会被创建

## OpenFeign教程	

### Feign是如何工作的

Feign提供了两个重要的注解：`@EnableFeignClients`、`@FeignClient`

- @EnableFeignClients：用来开启 Feign，可自定义扫描包地址，用于Spring Boot主类。

  推荐使用包路径配置：建议所有服务提供方的包路径前缀一致：如`cn.selinx.cloud.api.xxx.xxx`

  ```java
  /**
   * @EnableFeignClients 关于注解扫描的配置，并引入FeignClientsRegistrar
   *
   * @author Spencer Gibb
   * @author Dave Syer
   * @since 1.0
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @Documented
  @Import(FeignClientsRegistrar.class)
  public @interface EnableFeignClients {
  
  	/**
  	 * Alias for the {@link #basePackages()} attribute
  	 */
  	String[] value() default {};
  	
  	/**
  	 * 扫描@FeignClient注解包路径
  	 */
  	String[] basePackages() default {};
  
  	/**
  	 * 扫描这个指定类的所在包及其的子包
  	 */
  	Class<?>[] basePackageClasses() default {};
  
  	/**
  	 * feign clients 自定义配置类.
  	 */
  	Class<?>[] defaultConfiguration() default {};
  
  	/**
  	 * 指定@FeignClient注解类集合，如果不为空那么会禁用路径扫描
  	 */
  	Class<?>[] clients() default {};
  }
  
  ```

- @FeignClient：用来标记要用Feign拦截的请求接口，用于调用服务接口类。

  @FeignClient 相关配置关于对该接口进行代理的时候，一些实现细节的配置，比如 fallback 方法，关于404的请求是抛错误还是正常返回。有11个属性配置

  ```java
  /*
   * @author Spencer Gibb
   * @author Venil Noronha
   */
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface FeignClient {
  
  	@AliasFor("name")
  	String value() default "";
  
  	@Deprecated
  	String serviceId() default "";
  
  	@AliasFor("value")
  	String name() default "";
  	
  	String qualifier() default "";
  
  	String url() default "";
  
  	/**
  	 * Whether 404s should be decoded instead of throwing FeignExceptions
  	 */
  	boolean decode404() default false;
  	
  	Class<?>[] configuration() default {};
  	
  	Class<?> fallback() default void.class;
  
  	Class<?> fallbackFactory() default void.class;
  
  	String path() default "";
  
  	boolean primary() default true;
  
  }
  ```

### FeignClientsRegistrar 注册了哪些东西

在 Spring Context 的处理里面，`@ Import` 注解，会在解析 Configuration 的时候当做提供了其他的 bean definition 的扩展，Spring 通过调用其 registerBeanDefinitions 方法来获取其提供的 bean definition。

```java
/**
 * 注册Bean定义
 * metadata = 元注解类，如果主类上，或者是@Configuration类上
 * registry = DefaultListableBeanFactory
 */
@Override
public void registerBeanDefinitions(AnnotationMetadata metadata,
		BeanDefinitionRegistry registry) {
	// 注册默认的configuration，没有配置为空
	registerDefaultConfiguration(metadata, registry);
	// 注册FeignClient客户端
	registerFeignClients(metadata, registry);
}
```

#### registerDefaultConfiguration

将`BeanName = default.cn.selinx.cloud.consumer.config.FeignConfig.FeignClientSpecification`， `Value =GenericBeanDefinition` , 

实际类：`org.springframework.cloud.openfeign.FeignClientSpecification`

注册到`beanDefinitionMap`里面。

#### registerFeignClients

* 通过元注解`@EnableFeignClients`，找到扫描FeignClient的包路径。

- 通过`FeignClientsRegistrar#getScanner`，得到`ClassPathScanningCandidateComponentProvider`扫描器，找到 basePackage 下面所有包含了`@ FeignClient` 注解的类

- 读取类上面的 FeignClient 注解参数，得到`AnnotationMetadata`：attributesMap 和 methodMetadataSet

  ```java
  // 1.获取FeignClient.class 注解类的所有属性集合,有11个属性配置
  Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(FeignClient.class.getCanonicalName());
  ```

- 如果该注解包括了 configuration 参数，则先注册 configuration 所指定的类。这个类也是包装在 FeignClientSpecification 里面的，也就是 bean 的类型其实是 FeignClientSpecification，在 FeignClient 上指定的 configuration 类是它的一个属性。

  ```java
  // 2.根据属性集合的：value、name、serviceId值,获取服务ID
  String name = getClientName(attributes);
  // 3.根据configuration配置属性，注册ClientConfiguration
  registerClientConfiguration(registry, name,attributes.get("configuration"));
  ```

  实际注册的是: BeanName :`serviceId.FeignClientSpecification`，value=`org.springframework.cloud.openfeign.FeignClientSpecification`的Bean

- 注册该注解了 FeignClient 的接口，生成 BeanDefinition 时是以 FeignClientFactoryBean 作为对象创建的，而使用了 FeignClient 注解的接口是作为该 Bean 的一个属性，同时，对于 FeignClient 注解配置的参数，比如 fallback 等都一并作为参数放入 BeanDefinition 中。

  最终得到Bean注册信息是：`beanName = cn.selinx.cloud.api.hello.client.HelloClient` 
  
  ,`beanDefinition = class org.springframework.cloud.openfeign.FeignClientFactoryBean` 
  
  ```java
  // 4.注册FeignClientFactoryBean
  registerFeignClient(registry, annotationMetadata, attributes);
  // 具体方法
  private void registerFeignClient(BeanDefinitionRegistry registry,AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
  	String className = annotationMetadata.getClassName();
      // 生成Bean定义：FeignClientFactoryBean
  	BeanDefinitionBuilder definition = BeanDefinitionBuilder
  			.genericBeanDefinition(FeignClientFactoryBean.class);
  	validate(attributes);
  	definition.addPropertyValue("url", getUrl(attributes));
  	definition.addPropertyValue("path", getPath(attributes));
      // 根据属性集合的：value、name、serviceId值,获取服务ID
  	String name = getName(attributes);
      // 获取注解参数，赋值给definition的propertyValues
  	definition.addPropertyValue("name", name);
  	definition.addPropertyValue("type", className);
  	definition.addPropertyValue("decode404", attributes.get("decode404"));
  	definition.addPropertyValue("fallback", attributes.get("fallback"));
  	definition.addPropertyValue("fallbackFactory", attributes.get("fallbackFactory"));
      // 注入方式根据tyep类型
  	definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
  	// 别名：hello-serverFeignClient
  	String alias = name + "FeignClient";
  	AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
  
  	boolean primary = (Boolean)attributes.get("primary"); // has a default, won't be null
  
  	beanDefinition.setPrimary(primary);
  
  	String qualifier = getQualifier(attributes);
  	if (StringUtils.hasText(qualifier)) {
  		alias = qualifier;
  	}
  	// 创建BeanDefinitionHolder，并且使用别名。className = cn.selinx.cloud.api.hello.client.HelloClient 
  	BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
  			new String[] { alias });
      
  	BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
  }
  ```

总结一下，在启动时，处理了 @EnableFeignClients 注解后， registry 里面会多出一些关于 Feign 的 BeanDefinition，这些 BeanDefinition 分为两类：

- 一类是 FeignClientSpecification，包括了所有@FeignClient 上指定 configuration 以及在 @EnableFeignClients 上指定的 defaultConfiguration。前者的名字为注解的 URL 或者 value 组成，比如`hello-server`，后者的名字为 default.配置Class类名，比如 `default.cn.selinx.cloud.consumer.config.FeignConfig.FeignClientSpecification`。
- 还有一类是 FeignClientFactoryBean，它包含了所有使用了 FeignClient 注解的接口信息以及注解上面的参数。它的名字为注解的 URL 或者 value，比如 `hello-server`，跟它上面的 configuration 创建出来的 bean 定义是同一个名字。

### FeignClientFactoryBean 究竟是什么

FeignClientFactoryBean，它是一个工厂Bean类，Spring Context 创建 Bean 实例时会调用它的 getObject 方法。源码如下：实例化时的核心方法 getTarget()

```java
/**
 * @author Spencer Gibb
 * @author Venil Noronha
 * @author Eko Kurniawan Khannedy
 * @author Gregor Zurowski
 */
class FeignClientFactoryBean implements FactoryBean<Object>, InitializingBean,
		ApplicationContextAware {
	/***********************************
	 * WARNING! Nothing in this class should be @Autowired. It causes NPEs because of some lifecycle race condition.
	 ***********************************/

	private Class<?> type;

	private String name;

	private String url;

	private String path;

	private boolean decode404;

	private ApplicationContext applicationContext;

	private Class<?> fallback = void.class;

	private Class<?> fallbackFactory = void.class;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(this.name, "Name must be set");
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
	}

	protected Feign.Builder feign(FeignContext context) {
		FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
		Logger logger = loggerFactory.create(this.type);

		// @formatter:off
		Feign.Builder builder = get(context, Feign.Builder.class)
				// required values
				.logger(logger)
				.encoder(get(context, Encoder.class))
				.decoder(get(context, Decoder.class))
				.contract(get(context, Contract.class));
		// @formatter:on

		configureFeign(context, builder);

		return builder;
	}

	protected void configureFeign(FeignContext context, Feign.Builder builder) {
		FeignClientProperties properties = applicationContext.getBean(FeignClientProperties.class);
		if (properties != null) {
			if (properties.isDefaultToProperties()) {
				configureUsingConfiguration(context, builder);
				configureUsingProperties(properties.getConfig().get(properties.getDefaultConfig()), builder);
				configureUsingProperties(properties.getConfig().get(this.name), builder);
			} else {
				configureUsingProperties(properties.getConfig().get(properties.getDefaultConfig()), builder);
				configureUsingProperties(properties.getConfig().get(this.name), builder);
				configureUsingConfiguration(context, builder);
			}
		} else {
			configureUsingConfiguration(context, builder);
		}
	}

	protected void configureUsingConfiguration(FeignContext context, Feign.Builder builder) {
		Logger.Level level = getOptional(context, Logger.Level.class);
		if (level != null) {
			builder.logLevel(level);
		}
		Retryer retryer = getOptional(context, Retryer.class);
		if (retryer != null) {
			builder.retryer(retryer);
		}
		ErrorDecoder errorDecoder = getOptional(context, ErrorDecoder.class);
		if (errorDecoder != null) {
			builder.errorDecoder(errorDecoder);
		}
		Request.Options options = getOptional(context, Request.Options.class);
		if (options != null) {
			builder.options(options);
		}
		Map<String, RequestInterceptor> requestInterceptors = context.getInstances(
				this.name, RequestInterceptor.class);
		if (requestInterceptors != null) {
			builder.requestInterceptors(requestInterceptors.values());
		}

		if (decode404) {
			builder.decode404();
		}
	}

	protected void configureUsingProperties(FeignClientProperties.FeignClientConfiguration config, Feign.Builder builder) {
		if (config == null) {
			return;
		}

		if (config.getLoggerLevel() != null) {
			builder.logLevel(config.getLoggerLevel());
		}

		if (config.getConnectTimeout() != null && config.getReadTimeout() != null) {
			builder.options(new Request.Options(config.getConnectTimeout(), config.getReadTimeout()));
		}

		if (config.getRetryer() != null) {
			Retryer retryer = getOrInstantiate(config.getRetryer());
			builder.retryer(retryer);
		}

		if (config.getErrorDecoder() != null) {
			ErrorDecoder errorDecoder = getOrInstantiate(config.getErrorDecoder());
			builder.errorDecoder(errorDecoder);
		}

		if (config.getRequestInterceptors() != null && !config.getRequestInterceptors().isEmpty()) {
			// this will add request interceptor to builder, not replace existing
			for (Class<RequestInterceptor> bean : config.getRequestInterceptors()) {
				RequestInterceptor interceptor = getOrInstantiate(bean);
				builder.requestInterceptor(interceptor);
			}
		}

		if (config.getDecode404() != null) {
			if (config.getDecode404()) {
				builder.decode404();
			}
		}

		if (Objects.nonNull(config.getEncoder())) {
			builder.encoder(getOrInstantiate(config.getEncoder()));
		}

		if (Objects.nonNull(config.getDecoder())) {
			builder.decoder(getOrInstantiate(config.getDecoder()));
		}

		if (Objects.nonNull(config.getContract())) {
			builder.contract(getOrInstantiate(config.getContract()));
		}
	}

	private <T> T getOrInstantiate(Class<T> tClass) {
		try {
			return applicationContext.getBean(tClass);
		} catch (NoSuchBeanDefinitionException e) {
			return BeanUtils.instantiateClass(tClass);
		}
	}

	protected <T> T get(FeignContext context, Class<T> type) {
		T instance = context.getInstance(this.name, type);
		if (instance == null) {
			throw new IllegalStateException("No bean found of type " + type + " for "
					+ this.name);
		}
		return instance;
	}

	protected <T> T getOptional(FeignContext context, Class<T> type) {
		return context.getInstance(this.name, type);
	}
	
    // loadBalance：加载LoadBalancerFeignClient，调用HystrixTargeter的target方法        
	protected <T> T loadBalance(Feign.Builder builder, FeignContext context,
			HardCodedTarget<T> target) {
		Client client = getOptional(context, Client.class);
		if (client != null) {
			builder.client(client);
			Targeter targeter = get(context, Targeter.class);
            // 生成Bean的实例：HardCodedTarget，doGetObjectFromFactoryBean
			return targeter.target(this, builder, context, target);
		}

		throw new IllegalStateException(
				"No Feign Client for loadBalancing defined. Did you forget to include spring-cloud-starter-netflix-ribbon?");
	}
	
    // 创建 Bean 实例时会调用它的 getObject 
	@Override
	public Object getObject() throws Exception {
		return getTarget();
	}

	/**
	 * @param <T> the target type of the Feign client
	 * @return a {@link Feign} client created with the specified data and the context information
	 */
	<T> T getTarget() {
		FeignContext context = applicationContext.getBean(FeignContext.class);
		Feign.Builder builder = feign(context);

		if (!StringUtils.hasText(this.url)) {
			String url;
			if (!this.name.startsWith("http")) {
				url = "http://" + this.name;
			}
			else {
				url = this.name;
			}
			url += cleanPath();
			return (T) loadBalance(builder, context, new HardCodedTarget<>(this.type,
					this.name, url));
		}
		if (StringUtils.hasText(this.url) && !this.url.startsWith("http")) {
			this.url = "http://" + this.url;
		}
		String url = this.url + cleanPath();
		Client client = getOptional(context, Client.class);
		if (client != null) {
			if (client instanceof LoadBalancerFeignClient) {
				// not load balancing because we have a url,
				// but ribbon is on the classpath, so unwrap
				client = ((LoadBalancerFeignClient)client).getDelegate();
			}
			builder.client(client);
		}
		Targeter targeter = get(context, Targeter.class);
		return (T) targeter.target(this, builder, context, new HardCodedTarget<>(
				this.type, this.name, url));
	}

	private String cleanPath() {
		String path = this.path.trim();
		if (StringUtils.hasLength(path)) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (path.endsWith("/")) {
				path = path.substring(0, path.length() - 1);
			}
		}
		return path;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FeignClientFactoryBean that = (FeignClientFactoryBean) o;
		return Objects.equals(applicationContext, that.applicationContext) &&
				decode404 == that.decode404 &&
				Objects.equals(fallback, that.fallback) &&
				Objects.equals(fallbackFactory, that.fallbackFactory) &&
				Objects.equals(name, that.name) &&
				Objects.equals(path, that.path) &&
				Objects.equals(type, that.type) &&
				Objects.equals(url, that.url);
	}

	@Override
	public int hashCode() {
		return Objects.hash(applicationContext, decode404, fallback, fallbackFactory,
				name, path, type, url);
	}

	@Override
	public String toString() {
		return new StringBuilder("FeignClientFactoryBean{")
				.append("type=").append(type).append(", ")
				.append("name='").append(name).append("', ")
				.append("url='").append(url).append("', ")
				.append("path='").append(path).append("', ")
				.append("decode404=").append(decode404).append(", ")
				.append("applicationContext=").append(applicationContext).append(", ")
				.append("fallback=").append(fallback).append(", ")
				.append("fallbackFactory=").append(fallbackFactory)
				.append("}").toString();
	}

}

```

生成Bean定义的核心方法: Feign类的target

```java
/**
 * Feign's purpose is to ease development against http apis that feign restfulness.    In implementation, Feign is a {@link Feign#newInstance factory} for generating {@link Target  targeted} http apis.
 * 
 */
public abstract class Feign {
	public <T> T target(Target<T> target) {
	  return build().newInstance(target);
	}

	public Feign build() {
	  SynchronousMethodHandler.Factory synchronousMethodHandlerFactory =
		  new SynchronousMethodHandler.Factory(client, retryer, requestInterceptors, logger,
											   logLevel, decode404, closeAfterDecode);
	  ParseHandlersByName handlersByName =
		  new ParseHandlersByName(contract, options, encoder, decoder, queryMapEncoder,
								  errorDecoder, synchronousMethodHandlerFactory);
	  return new ReflectiveFeign(handlersByName, invocationHandlerFactory, queryMapEncoder);
	}
}	
```



## EurekaClient教程





## Ribbo教程

源码解析

- ILoadBalancer
- IRule
- IPing