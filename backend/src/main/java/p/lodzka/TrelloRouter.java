package p.lodzka;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import p.lodzka.model.RegisterForm;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class TrelloRouter extends RouteBuilder {

    public static final String APPLICATION_JSON = "application/json";

    @Override
    public void configure() {
        restConfiguration().component("restlet").host("0.0.0.0").port(8082).bindingMode(RestBindingMode.auto);
        //@formatter:off
        rest("/auth").consumes(APPLICATION_JSON).produces(APPLICATION_JSON)
                .post("/login")
                    .to("bean:helloBean?method=saySomething")
                .post("/register").type(RegisterForm.class)
                    .to("bean:helloBean?method=saySomething");

        //@formatter:on
    }

}
