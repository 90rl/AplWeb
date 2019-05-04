package p.lodzka.processor

import org.apache.camel.Exchange
import org.apache.camel.Processor
import p.lodzka.config.APPLICATION_JSON
import p.lodzka.form.RegisterForm
import p.lodzka.service.objects.ErrorResponse
import javax.servlet.http.HttpServletResponse


class UserValidationProcessor : Processor {
    override fun process(exchange: Exchange) {
        val form = exchange.getIn().getBody(RegisterForm::class.java)
        if (form.password.isBlank()  || form.repeatPassword.isBlank()){
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR001" , description = "Wpisz hasło")
        }
        if(form.password != form.repeatPassword){
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR002" , description = "Powtórzone hasło nie jest takie samo")
        }
    }

}