package net.nemerosa.iteach.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private APIBasicAuthenticationEntryPoint apiBasicAuthenticationEntryPoint;

    @Autowired
    private AuthenticationUserDetailsService<OpenIDAuthenticationToken> openIdAuthenticationUserDetailsService;

    @Autowired
    @Qualifier("openid")
    private AuthenticationFailureHandler openIdAuthenticationFailureHandler;

    /**
     * By default, all queries are accessible anonymously. Security is enforced at service level.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .openidLogin()
                    .loginProcessingUrl("/api/login/openid")
                    .authenticationUserDetailsService(openIdAuthenticationUserDetailsService)
                    .attributeExchange("https://www.google.com/.*")
                        .attribute("email")
                            .type("http://axschema.org/contact/email")
                            .required(true)
                            .and()
                        .attribute("firstname")
                            .type("http://axschema.org/namePerson/first")
                            .required(true)
                            .and()
                        .attribute("lastname")
                            .type("http://axschema.org/namePerson/last")
                            .required(true)
                            .and()
                        .and()
                    .attributeExchange(".*myopenid.com.*")
                        .attribute("email")
                            .type("http://schema.openid.net/contact/email")
                            .required(true)
                            .and()
                        .attribute("fullname")
                            .type("http://schema.openid.net/namePerson")
                            .required(true)
                            .and()
                        .and()
                    .failureHandler(openIdAuthenticationFailureHandler)
                    .and()
                .httpBasic()
                    .authenticationEntryPoint(apiBasicAuthenticationEntryPoint)
                    .realmName("iteach")
                    .and()
                .logout()
                    .logoutUrl("/api/account/logout")
                    .logoutSuccessUrl("/api/account/logged-out")
                    .and()
                //.csrf().requireCsrfProtectionMatcher(new CSRFRequestMatcher()).and()
                // FIXME CSRF protection for a stateless API?
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/api/**").permitAll()
                    .anyRequest().authenticated()
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManager);
    }

}
