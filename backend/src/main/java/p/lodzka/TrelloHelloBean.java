package p.lodzka;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import p.lodzka.model.RegisterForm;
import p.lodzka.model.TrelloBoards;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static p.lodzka.config.Constants.TRELLO_BEAN;

/**
 * A bean that returns a message when you call the {@link #saySomething()} method.
 * <p/>
 * Uses <tt>@Component("myBean")</tt> to register this bean with the name <tt>myBean</tt>
 * that we use in the Camel route to lookup this bean.
 */
@Component(TRELLO_BEAN)
public class TrelloHelloBean {

    private static final Logger logger = LoggerFactory.getLogger(TrelloHelloBean.class);

    @Value("${greeting}")
    private String say;

    public String saySomething() {
        return say;
    }

    public void register(@Body RegisterForm form, Exchange exchange){
        //todo
        logger.info("Name: {}", form.getName());
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_CREATED);
        exchange.getIn().setBody(null);
    }

    public void getBoards(Exchange exchange){
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK);
        exchange.getIn().setBody(new TrelloBoards());
    }
}
