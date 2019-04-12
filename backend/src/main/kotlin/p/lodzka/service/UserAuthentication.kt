package p.lodzka.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import p.lodzka.config.ROLE_USER

@Service
class UserAuthentication : UserDetailsService {

    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        logger.info("User: {}", name)

        //todo load user and his password from db, the password should already be hashed
        return User.builder().passwordEncoder { p -> bCryptPasswordEncoder.encode(p)}.username(name).password(MOCK_PASSWORD).authorities(AUTHORITIES).build()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(UserAuthentication::class.java)
        private val AUTHORITIES = listOf(SimpleGrantedAuthority(ROLE_USER))
        private const val MOCK_PASSWORD = "abc123"
    }
}
