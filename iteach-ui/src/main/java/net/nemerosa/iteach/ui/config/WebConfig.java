package net.nemerosa.iteach.ui.config;

import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan("net.nemerosa.iteach")
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Uses the HTTP header for content negociation.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).favorParameter(true);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        // Plain text
        converters.add(new StringHttpMessageConverter());
        // JSON
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(ObjectMapperFactory.create());
        converters.add(converter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Setup depends on the profile, dev or prod
        registry.addResourceHandler("/app/**").addResourceLocations("/app/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
        registry.addResourceHandler("/vendor/**").addResourceLocations("/vendor/");
        registry.addResourceHandler("index.html").addResourceLocations("index.html");
    }

    // Message source
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("META-INF.messages.validation");
        return messageSource;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver o = new CommonsMultipartResolver();
        o.setMaxUploadSize(1024 * 1024); // 1024K limit
        return o;
    }

}
