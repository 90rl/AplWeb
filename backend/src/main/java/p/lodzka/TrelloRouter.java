package p.lodzka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class TrelloRouter extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration().component("restlet").host("0.0.0.0").port(8082);

        rest("/auth").consumes("application.json").produces("application/json")
                .get("/login").to("bean:helloBean?method=saySomething");
    }


}
