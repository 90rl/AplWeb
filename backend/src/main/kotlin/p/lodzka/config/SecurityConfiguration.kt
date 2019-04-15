package p.lodzka.config

import org.apache.camel.component.spring.security.SpringSecurityAccessPolicy
import org.apache.camel.component.spring.security.SpringSecurityAuthorizationPolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.vote.AffirmativeBased
import org.springframework.security.access.vote.RoleVoter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var  userDetailsService: UserDetailsService


    @Throws(Exception::class)
    fun registerAuthentication(auth: AuthenticationManagerBuilder){
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.userDetailsService(userDetailsService).authorizeRequests()
                .antMatchers("/trello/**").hasRole("user")
                .and().httpBasic().and().rememberMe()

    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun accessDecisionManager(): AccessDecisionManager {
        val decisionVoters = ArrayList<AccessDecisionVoter<*>>()
        decisionVoters.add(RoleVoter())
        return AffirmativeBased(decisionVoters)
    }

    @Bean(name = [USER])
    @Throws(Exception::class)
    fun springPolicy(@Autowired accessMgr: AccessDecisionManager): SpringSecurityAuthorizationPolicy {
        val policy = SpringSecurityAuthorizationPolicy()
        policy.authenticationManager = authenticationManagerBean()
        policy.accessDecisionManager = accessMgr
        policy.springSecurityAccessPolicy = SpringSecurityAccessPolicy(ROLE_USER)
        return policy
    }
}
