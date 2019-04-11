package p.lodzka.service

import org.apache.camel.Body
import org.apache.camel.Exchange
import org.apache.camel.Header
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import p.lodzka.config.APPLICATION_JSON
import p.lodzka.config.TRELLO_SERVICE
import p.lodzka.form.TaskForm
import p.lodzka.model.RegisterForm
import javax.servlet.http.HttpServletResponse.*

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

    fun deleteBoard(@Header("boardId") boardId: Long, exchange: Exchange) {
        logger.info("Getting board: {}", boardId)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_NO_CONTENT)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = null
    }

    fun addBoard(@Body form: NameForm?, exchange: Exchange) {
        logger.info("Adding board with name: {}", form?.name)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    fun addColumn(@Body form: NameForm?, @Header("boardId") boardId: Long, exchange: Exchange) {
        logger.info("Adding column with name: {} to board: {}", form?.name, boardId)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    fun deleteColumn(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long, exchange: Exchange) {
        logger.info("Deleting column: {} in board: {}", columnId, boardId)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    fun reorderColumns(@Header("boardId") boardId: Long, @Header("firstColumn") firstColumn: Long?,
                       @Header("secondColumn") secondColumn: Long?, exchange: Exchange) {
        logger.info("Reordering columns: {} and: {}", firstColumn, secondColumn)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    fun addTask(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                @Body form: TaskForm?, exchange: Exchange) {
        logger.info("Adding task to board: {} to column {}, task name: ", boardId, columnId, form?.name)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    fun deleteTask(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                   @Header("taskId") taskId: Long, exchange: Exchange) {
        logger.info("Deleting task from board: {} on column {}, taskId: ", boardId, columnId, taskId)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    fun moveTask(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                 @Header("task") task: Long?,
                 @Header("toColumn") toColumn: Long?, exchange: Exchange) {
        logger.info("Moving task: {} toColumn: {}", task, toColumn)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(Trello::class.java)
    }
}
