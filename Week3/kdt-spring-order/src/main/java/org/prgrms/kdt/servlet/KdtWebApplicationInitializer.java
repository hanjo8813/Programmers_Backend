package org.prgrms.kdt.servlet;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zaxxer.hikari.HikariDataSource;
import org.prgrms.kdt.customer.CustomerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KdtWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger logger = LoggerFactory.getLogger(KdtWebApplicationInitializer.class);

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = "org.prgrms.kdt.customer",
            // kdt 하위(서비스계층)에서는 컨트롤러만 등록
            includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomerController.class),
            useDefaultFilters = false
    )
    static class ServletConfig implements WebMvcConfigurer, ApplicationContextAware {
        ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        // ViewResolvers -> 템플릿 엔진 설정하기
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            // jsp
            registry.jsp().viewNames("jsp/*");

            // thymeleaf
            var springResourceTemplateResolver = new SpringResourceTemplateResolver();
            springResourceTemplateResolver.setApplicationContext(applicationContext);
            springResourceTemplateResolver.setPrefix("/WEB-INF/");
            springResourceTemplateResolver.setSuffix(".html");

            var springTemplateEngine = new SpringTemplateEngine();
            springTemplateEngine.setTemplateResolver(springResourceTemplateResolver);

            var thymeleafViewResolver = new ThymeleafViewResolver();
            thymeleafViewResolver.setTemplateEngine(springTemplateEngine);
            thymeleafViewResolver.setOrder(1);
            thymeleafViewResolver.setViewNames(new String[]{"views/*"});
            registry.viewResolver(thymeleafViewResolver);
        }

        // 리소스 핸들러
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // resources 경로 요청은 "/resources/"로 매핑한다~
            registry.addResourceHandler("/resources/**")
                    .addResourceLocations("/resources/")
                    .setCachePeriod(60);          // 리소스 파일을 캐시로 지정가능
//                    .resourceChain(true)        // 추가적인 옵션 : 리소스를 압축하여 제공하기 등등
//                    .addResolver(new EncodedResourceResolver());
        }

//        // 메시지 컨버터 변경
//        @Override
//        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//            var messageConverter = new MarshallingHttpMessageConverter();
//            var xStreamMarshaller = new XStreamMarshaller();
//            // java -> xml
//            messageConverter.setMarshaller(xStreamMarshaller);
//            // xml -> java
//            messageConverter.setUnmarshaller(xStreamMarshaller);
//            // 기존 컨버터에 설정한거 추가
//            converters.add(messageConverter);
//        }

        // 메시지 컨버터 확장
        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            // xml 컨버터 추가하기
            var messageConverter = new MarshallingHttpMessageConverter();
            var xStreamMarshaller = new XStreamMarshaller();
            messageConverter.setMarshaller(xStreamMarshaller);
            messageConverter.setUnmarshaller(xStreamMarshaller);
            // 설정한거 우선순위 0으로 추가
            converters.add(0, messageConverter);

            // json 시간 출력형식 변경
            var javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
            var modules = Jackson2ObjectMapperBuilder.json().modules(javaTimeModule);
            converters.add(1, new MappingJackson2HttpMessageConverter(modules.build()));
        }
    }


    @Configuration
    @ComponentScan(basePackages = "org.prgrms.kdt.customer",
            excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomerController.class)
    )
    @EnableTransactionManagement
    static class RootConfig {
        @Bean
        public DataSource dataSource() {
            var dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/order_mgmt")
                    .username("root")
                    .password("root1234")
                    .type(HikariDataSource.class)
                    .build();
            dataSource.setMaximumPoolSize(1000);
            dataSource.setMinimumIdle(100);
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

//        @Bean
//        public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
//            return new DataSourceTransactionManager(dataSource);
//        }
    }




    @Override
    public void onStartup(ServletContext servletContext) {
        logger.info("Starting Server ...");

        // root App Context 객체 등록
        var rootApplicationContext = new AnnotationConfigWebApplicationContext();
        rootApplicationContext.register(RootConfig.class);
        // ??
        var loaderListener = new ContextLoaderListener(rootApplicationContext);
        servletContext.addListener(loaderListener);


        // Servlet App Context 등록
        var applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ServletConfig.class);
        // 등록된 Servlet App Context로 dispatcherServlet 객체 생성
        var dispatcherServlet = new DispatcherServlet(applicationContext);
        // dispatcherServlet에 이름 지어주고 Servlet으로 등록 -> 도메인에 따라 여러개를 등록 가능
        var servletRegistration = servletContext.addServlet("test", dispatcherServlet);
        servletRegistration.addMapping("/");
        // -1 일경우 servlet context는 로드되지 않다가, api 요청이 왔을때 비로소 켜진다
        servletRegistration.setLoadOnStartup(-1);   
    }


}
