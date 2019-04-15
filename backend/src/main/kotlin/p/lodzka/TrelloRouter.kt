package p.lodzka

import org.apache.camel.CamelAuthorizationException
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import org.apache.camel.model.rest.RestParamType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import p.lodzka.config.*
import p.lodzka.form.TaskForm
import p.lodzka.form.RegisterForm
import p.lodzka.processor.AuthProcessor
import p.lodzka.form.NameForm
import p.lodzka.service.InvalidArgumentException
import p.lodzka.service.UnauthorizedAccessException
import javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST
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
                .get("/boards")
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=getBoards")
                .endRest()

                .post("/boards").type(NameForm::class.java)
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=addBoard")
                .endRest()

                .get("/boards/{boardId}")
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=getBoard")
                .endRest()

                .delete("/boards/{boardId}")
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=deleteBoard")
                .endRest()

                .post("/boards/{boardId}/columns").type(NameForm::class.java)
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=addColumn")
                .endRest()

                .delete("/boards/{boardId}/columns/{columnId}")
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=deleteColumn")
                .endRest()

                .post("/boards/{boardId}/columns/{columnId}/tasks").type(TaskForm::class.java)
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=addTask")
                .endRest()

                .delete("/boards/{boardId}/columns/{columnId}/tasks/{taskId}")
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=deleteTask")
                .endRest()

                .post("/boards/{boardId}/columns/{columnId}/move")
                .param().name("toPosition").type(RestParamType.query).endParam()
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=moveColumn")
                .endRest()

                .post("/boards/{boardId}/columns/{columnId}/tasks/{taskId}/move")
                .param().name("toColumn").type(RestParamType.query).endParam()
                .route().process(authProcessor).policy(USER)
                .to("bean:$TRELLO_SERVICE?method=moveTask")
                .endRest()

        onException(CamelAuthorizationException::class.java, UnauthorizedAccessException::class.java)
                .handled(true)
                .setHeader(Exchange.CONTENT_TYPE, constant(APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(SC_FORBIDDEN))
                .setBody(simple("${null}"))

        onException(InvalidArgumentException::class.java)
                .handled(true)
                .setHeader(Exchange.CONTENT_TYPE, constant(APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(SC_BAD_REQUEST))
                .setBody(simple("${null}"))
    }

}
