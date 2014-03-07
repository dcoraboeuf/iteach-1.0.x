package net.nemerosa.iteach.service.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;

@Service
public class EnvServiceImpl implements EnvService {

    public static final String SYSTEM_ITEACH_URL = "iteach.url";
    public static final String ENV_ITEACH_URL = "ITEACH_URL";
    public static final String DEFAULT_ITEACH_URL = "http://localhost:8080/iteach";
    private final Logger logger = LoggerFactory.getLogger(EnvService.class);
    private final String profiles;
    private final String version;
    private final String baseURL;

    @Autowired
    public EnvServiceImpl(@Value("${app.version}") String version,
                          ApplicationContext ctx) {
        this.profiles = StringUtils.join(ctx.getEnvironment().getActiveProfiles(), ",");
        this.version = version;
        // Base URL
        String sysBase = System.getProperty(SYSTEM_ITEACH_URL);
        String envBase = System.getenv(ENV_ITEACH_URL);
        if (StringUtils.isNotBlank(sysBase)) {
            logger.info("[env] Using `{}` system property for base URL", SYSTEM_ITEACH_URL);
            baseURL = sysBase;
        } else if (StringUtils.isNotBlank(envBase)) {
            logger.info("[env] Using `{}` environment variable for base URL", ENV_ITEACH_URL);
            baseURL = envBase;
        } else {
            logger.warn("[env] Using default value for base URL." +
                    " This should be configured using either the `{}` system property or" +
                    " the `{}` environment variable.",
                    SYSTEM_ITEACH_URL, ENV_ITEACH_URL);
            baseURL = DEFAULT_ITEACH_URL;
        }
    }

    @Override
    public String getProfiles() {
        return profiles;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getBaseURL() {
        return baseURL;
    }

    @PostConstruct
    public void init() throws FileNotFoundException {
        logger.info("[env] With JDK:      {}", System.getProperty("java.version"));
        logger.info("[env] With profiles: {}", profiles);
        logger.info("[env] With version:  {}", version);
        logger.info("[env] With base URL: {}", baseURL);
    }
}
