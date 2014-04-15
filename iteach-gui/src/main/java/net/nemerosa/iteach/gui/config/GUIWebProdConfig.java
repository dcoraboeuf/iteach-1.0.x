package net.nemerosa.iteach.gui.config;

import net.nemerosa.iteach.common.RunProfile;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Resource management for the static resources.
 *
 * @see net.nemerosa.iteach.gui.config.GUIWebDevConfig
 */
@Configuration
@EnableWebMvc
@ComponentScan("net.nemerosa.iteach")
@Profile({RunProfile.ACCEPTANCE, RunProfile.PROD})
public class GUIWebProdConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
        registry.addResourceHandler("index.html").addResourceLocations("index.html");
    }

}
