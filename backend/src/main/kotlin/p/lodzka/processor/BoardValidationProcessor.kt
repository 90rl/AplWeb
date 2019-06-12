package p.lodzka.processor

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import p.lodzka.config.APPLICATION_JSON
import p.lodzka.form.NameForm
import p.lodzka.service.objects.ErrorResponse
import javax.servlet.http.HttpServletResponse

@Component
class BoardValidationProcessor : Processor {

    override fun process(exchange: Exchange) {
        val form = exchange.getIn().getBody(NameForm::class.java)

        if (StringUtils.isBlank(form.name)) {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpServletResponse.SC_BAD_REQUEST)
            exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
            exchange.getIn().body = ErrorResponse(errorId = "UVP_TR010", description = "Nazwa tablicy nie moze byc pusta")
        }
    }
}