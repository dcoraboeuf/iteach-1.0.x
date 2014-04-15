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
 * @see net.nemerosa.iteach.gui.config.GUIWebProdConfig
 */
@Configuration
@EnableWebMvc
@ComponentScan("net.nemerosa.iteach")
@Profile({RunProfile.DEV, RunProfile.TEST})
public class GUIWebDevConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**").addResourceLocations("/app/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
        registry.addResourceHandler("/vendor/**").addResourceLocations("/vendor/");
        registry.addResourceHandler("index.html").addResourceLocations("index.html");
    }

}
