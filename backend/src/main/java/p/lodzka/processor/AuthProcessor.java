package p.lodzka.processor;

import javax.security.auth.Subject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import p.lodzka.service.UnauthorizedAccessException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthProcessor implements Processor {

    private static final Pattern BASIC = Pattern.compile("^Basic ([a-zA-Z0-9+/]+={0,2})$");

    public void process(Exchange exchange) throws Exception {
        Optional<String> authorization = Optional.ofNullable(exchange.getIn().getHeader("Authorization", String.class));
        if (authorization.isPresent()) {
            String authString = authorization.get();
            String basic = getBasic(authString);
            String userpass = new String(Base64.decodeBase64(basic));
            String[] tokens = userpass.split(":");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(tokens[0], tokens[1]);
            Subject subject = new Subject();
            subject.getPrincipals().add(authToken);
            exchange.getIn().setHeader(Exchange.AUTHENTICATION, subject);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
    }

    private String getBasic(String authString) throws UnauthorizedAccessException {
        Matcher matcher = BASIC.matcher(authString);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
    }
}