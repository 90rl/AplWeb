package p.lodzka.service

import org.apache.camel.Body
import org.apache.camel.Exchange
import org.apache.camel.Header
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import p.lodzka.config.APPLICATION_JSON
import p.lodzka.config.TRELLO_SERVICE
import p.lodzka.model.RegisterForm
import javax.servlet.http.HttpServletResponse.SC_CREATED
import javax.servlet.http.HttpServletResponse.SC_OK

@Service(TRELLO_SERVICE)
class Trello {

    @Value("\${greeting}")
    private val say: String? = null

    fun saySomething(): String? {
        return say
    }

    fun register(@Body form: RegisterForm, exchange: Exchange) {
        //todo
        logger.info("Name: {}", form.name)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_CREATED)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = null
    }

    fun getBoards(exchange: Exchange) {
        //todo get logged user -> get his boards
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoards()
    }

    fun getBoard(@Header("boardId") boardId: Long, exchange: Exchange) {
        logger.info("Getting board: {}", boardId)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(boardId)
    }

    fun addBoard(@Body form: AddBoardForm, exchange: Exchange) {
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(Trello::class.java)
    }
}
