package p.lodzka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import p.lodzka.model.RegisterForm;

/**
 * A bean that returns a message when you call the {@link #saySomething()} method.
 * <p/>
 * Uses <tt>@Component("myBean")</tt> to register this bean with the name <tt>myBean</tt>
 * that we use in the Camel route to lookup this bean.
 */
@Component("helloBean")
public class TrelloHelloBean {

    @Value("${greeting}")
    private String say;

    public String saySomething() {
        return say;
    }

    public void saySomething(RegisterForm form){
        //todo
        return;
    }

}
