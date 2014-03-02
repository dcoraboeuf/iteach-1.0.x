package net.nemerosa.iteach.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class TemplateConfiguration {

    @Bean
    public FreeMarkerConfigurationFactoryBean templateConfiguration() {
        FreeMarkerConfigurationFactoryBean o = new FreeMarkerConfigurationFactoryBean();
        o.setTemplateLoaderPath("classpath:META-INF/templates/");
        return o;
    }

}
