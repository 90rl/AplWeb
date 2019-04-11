package p.lodzka

import org.apache.camel.CamelAuthorizationException
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import p.lodzka.config.*
import p.lodzka.model.RegisterForm
import p.lodzka.service.Boards
import p.lodzka.processor.AuthProcessor
import p.lodzka.service.UnauthorizedAccessException
import javax.servlet.http.HttpServletResponse.SC_FORBIDDEN

@Component
class TrelloRouter : RouteBuilder() {

    @Autowired
    private lateinit var authProcessor: AuthProcessor

    override fun configure() {
        restConfiguration().component("restlet").host("0.0.0.0").port(8082).bindingMode(RestBindingMode.auto)

        //logger.info(applCont.getBean("trelloBean").toString())

        rest("$V1/auth")
                .consumes(APPLICATION_JSON).produces(APPLICATION_JSON)
                .post("/register").type(RegisterForm::class.java)
                .to("bean:$TRELLO_SERVICE?method=register")

        rest(V1 + TRELLO)
                .consumes(APPLICATION_JSON).produces(APPLICATION_JSON)
                .get("/boards").outType(Boards::class.java)
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=getBoards")
                .endRest()
                .get("/boards/{boardId}")
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=getBoard")


        //yes, I know about the warning, still, this is better than copy pasting same code twice
        onException(CamelAuthorizationException::class.java, UnauthorizedAccessException::class.java)
                .handled(true)
                .setHeader(Exchange.CONTENT_TYPE, constant(APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(SC_FORBIDDEN))

    }

}
