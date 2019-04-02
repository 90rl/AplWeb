package p.lodzka;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import p.lodzka.model.RegisterForm;

import static org.apache.http.HttpStatus.SC_CREATED;

/**
 * A bean that returns a message when you call the {@link #saySomething()} method.
 * <p/>
 * Uses <tt>@Component("myBean")</tt> to register this bean with the name <tt>myBean</tt>
 * that we use in the Camel route to lookup this bean.
 */
@Component("helloBean")
public class TrelloHelloBean {

    private static final Logger logger = LoggerFactory.getLogger(TrelloHelloBean.class);

    @Value("${greeting}")
    private String say;

    public String saySomething() {
        return say;
    }

    public void saySomething(@Body RegisterForm form, Exchange exchange){
        //todo
        logger.info("Name: {}", form.getName());
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_CREATED);
        exchange.getIn().setBody(null);
    }

}
