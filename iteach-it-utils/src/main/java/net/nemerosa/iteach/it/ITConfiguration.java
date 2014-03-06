package net.nemerosa.iteach.it;

import net.nemerosa.iteach.common.RunProfile;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@Profile(RunProfile.TEST)
public class ITConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ITConfiguration.class);

    @Bean
    public DataSource dataSource() throws IOException {
        String dbURL = "jdbc:h2:target/db";
        log.info("Using database at {}", dbURL);
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl(dbURL);
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setDefaultAutoCommit(false);
        ds.setInitialSize(1);
        ds.setMaxActive(2);
        return ds;
    }

}
