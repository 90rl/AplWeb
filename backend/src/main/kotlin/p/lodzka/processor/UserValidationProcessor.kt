package p.lodzka.processor

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import p.lodzka.config.APPLICATION_JSON
import p.lodzka.form.RegisterForm
import p.lodzka.repository.UserRepository
import p.lodzka.service.objects.ErrorResponse
import javax.servlet.http.HttpServletResponse


@Component
class UserValidationProcessor : Processor {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun process(exchange: Exchange) {
        val form = exchange.getIn().getBody(RegisterForm::class.java)

        if (StringUtils.isBlank(form.name)) {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR001", description = "Wpisz nazwę użytkownika")
        } else if (StringUtils.isBlank(form.email)) {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR002", description = "Wpisz email")
        } else if (StringUtils.isBlank(form.password) || StringUtils.isBlank(form.repeatPassword)) {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR003", description = "Wpisz hasło")
        } else if (form.password != form.repeatPassword) {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR004", description = "Powtórzone hasło nie jest takie samo")
        } else if (userRepository.existsByName(form.name!!)) {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR005", description = "Użytkownik już istnieje")
        } else if (userRepository.existsByEmail(form.email!!)) {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR006", description = "Ten adres email już istnieje w bazie danych")
        }
    }
}