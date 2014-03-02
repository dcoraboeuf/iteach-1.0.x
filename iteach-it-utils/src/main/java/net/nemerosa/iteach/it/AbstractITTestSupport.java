package net.nemerosa.iteach.it;

import net.nemerosa.iteach.common.RunProfile;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        loader = AnnotationConfigContextLoader.class,
        classes = AbstractITTestSupport.AbstractIntegrationTestConfiguration.class)
@ActiveProfiles(profiles = {RunProfile.TEST})
public abstract class AbstractITTestSupport extends AbstractJUnit4SpringContextTests {

    @Configuration
    @Profile(RunProfile.TEST)
    @ComponentScan("net.nemerosa.iteach")
    public static class AbstractIntegrationTestConfiguration {
    }
}
