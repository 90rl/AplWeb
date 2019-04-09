package p.lodzka;

import org.apache.camel.CamelAuthorizationException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import p.lodzka.model.RegisterForm;
import p.lodzka.model.TrelloBoards;
import p.lodzka.processor.AuthProcessor;
import p.lodzka.service.UnauthorizedAccessException;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static p.lodzka.config.Constants.*;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class TrelloRouter extends RouteBuilder {

    @Autowired
    private AuthProcessor authProcessor;

    @Override
    public void configure() {
        restConfiguration().component("restlet").host("0.0.0.0").port(8082).bindingMode(RestBindingMode.auto);
        //@formatter:off
        rest(V1 + "/auth").consumes(APPLICATION_JSON).produces(APPLICATION_JSON)
                .post("/register").type(RegisterForm.class)
                    .to("bean:" + TRELLO_BEAN  + "?method=register");

        rest(V1 + TRELLO)
                .consumes(APPLICATION_JSON).produces(APPLICATION_JSON)
                .get("/boards").outType(TrelloBoards.class)
                .route().process(authProcessor).policy(USER)
                .to("bean:" + TRELLO_BEAN  + "?method=getBoards");


        //yes, I know about the warning, still, this is better than copy pasting same code twice
        onException(CamelAuthorizationException.class, UnauthorizedAccessException.class)
                .handled(true)
                .setHeader(Exchange.CONTENT_TYPE, constant(APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(SC_FORBIDDEN));

        //@formatter:on
    }

}
