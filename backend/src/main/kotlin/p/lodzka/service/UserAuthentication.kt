package p.lodzka.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import p.lodzka.config.ROLE_USER
import p.lodzka.model.UserModel
import p.lodzka.repository.UserRepository

@Service
@Transactional(readOnly = true)
class UserAuthentication : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        logger.info("Authenticating user: {}", name)
        val user: UserModel = userRepository.findByName(name)
        logger.info(user.email)
        return User.builder().username(name).password(user.password).authorities(AUTHORITIES).build()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserAuthentication::class.java)
        private val AUTHORITIES = listOf(SimpleGrantedAuthority(ROLE_USER))
    }
}
