package p.lodzka.config;

import org.apache.camel.component.spring.security.SpringSecurityAccessPolicy;
import org.apache.camel.component.spring.security.SpringSecurityAuthorizationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

import static p.lodzka.config.Constants.ROLE_USER;
import static p.lodzka.config.Constants.USER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /*@Autowired
    public void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }*/

    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService).authorizeRequests()
                .antMatchers("/trello/**").hasRole("user")
                .and().httpBasic().and().rememberMe();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new RoleVoter());
        return new AffirmativeBased(decisionVoters);
    }

    @Bean(name = USER)
    public SpringSecurityAuthorizationPolicy springPolicy(@Autowired AccessDecisionManager accessMgr) throws Exception {
        SpringSecurityAuthorizationPolicy policy = new SpringSecurityAuthorizationPolicy();
        policy.setAuthenticationManager(authenticationManagerBean());
        policy.setAccessDecisionManager(accessMgr);
        policy.setSpringSecurityAccessPolicy(new SpringSecurityAccessPolicy(ROLE_USER));
        return policy;
    }
}
