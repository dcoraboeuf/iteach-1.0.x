package net.nemerosa.iteach.dao.config;

import net.nemerosa.iteach.common.RunProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@Profile({RunProfile.ACCEPTANCE, RunProfile.DEV, RunProfile.PROD})
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws IllegalArgumentException, NamingException {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setExpectedType(DataSource.class);
        factory.setJndiName("java:comp/env/jdbc/iteach");
        factory.afterPropertiesSet();
        DataSource dataSource = (DataSource) factory.getObject();
        return dataSource;
    }

}
