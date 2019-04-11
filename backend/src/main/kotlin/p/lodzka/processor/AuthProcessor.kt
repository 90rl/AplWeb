package p.lodzka.processor

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import p.lodzka.service.UnauthorizedAccessException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Pattern
import javax.security.auth.Subject

@Component
class AuthProcessor : Processor {

    @Throws(Exception::class)
    override fun process(exchange: Exchange) {
        val authorization = Optional.ofNullable(exchange.getIn().getHeader("Authorization", String::class.java))
        if (authorization.isPresent) {
            val authString = authorization.get()
            val basic = getBasic(authString)
            val userpass = String(Base64.getDecoder().decode(basic), StandardCharsets.UTF_8)
            val tokens = userpass.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val authToken = UsernamePasswordAuthenticationToken(tokens[0], tokens[1])
            val subject = Subject()
            subject.principals.add(authToken)
            exchange.getIn().setHeader(Exchange.AUTHENTICATION, subject)
            SecurityContextHolder.getContext().authentication = authToken
        } else {
            throw UnauthorizedAccessException("Unauthorized access")
        }
    }

    @Throws(UnauthorizedAccessException::class)
    private fun getBasic(authString: String): String {
        val matcher = BASIC.matcher(authString)
        return if (matcher.matches()) {
            matcher.group(1)
        } else {
            throw UnauthorizedAccessException("Unauthorized access")
        }
    }

    companion object {

        private val BASIC = Pattern.compile("^Basic ([a-zA-Z0-9+/]+={0,2})$")
    }
}