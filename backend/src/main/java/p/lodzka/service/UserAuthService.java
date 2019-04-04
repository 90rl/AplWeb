package p.lodzka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static p.lodzka.config.Constants.ROLE_USER;

@Service
public class UserAuthService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        //todo load user and his password from db, the password should already be hashed
        logger.info("User: {}", name);
        return new User(name, "123", Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
    }
}
