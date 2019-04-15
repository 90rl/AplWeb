package p.lodzka.service

import org.apache.camel.Body
import org.apache.camel.Exchange
import org.apache.camel.Header
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import p.lodzka.config.APPLICATION_JSON
import p.lodzka.config.TRELLO_SERVICE
import p.lodzka.form.NameForm
import p.lodzka.form.RegisterForm
import p.lodzka.form.TaskForm
import p.lodzka.model.BoardModel
import p.lodzka.model.UserModel
import p.lodzka.repository.BoardRepository
import p.lodzka.repository.UserRepository
import java.util.stream.Collectors
import javax.servlet.http.HttpServletResponse.SC_CREATED
import javax.servlet.http.HttpServletResponse.SC_OK

@Service(TRELLO_SERVICE)
class Trello {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var boardRepository: BoardRepository

    @Autowired
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder


    fun register(@Body form: RegisterForm, exchange: Exchange) {
        logger.info("Name: {}", form.name)
        val password = bCryptPasswordEncoder.encode(form.password)
        val user = UserModel(name = form.name, email = form.email, password = password)
        userRepository.save(user)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_CREATED)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = null
    }

    fun getBoards(exchange: Exchange) {
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
        logger.info("Deleting board: {}", boardId)
        val name = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.getByName(name)
        user.boards.removeAll { b -> b.id == boardId }
        userRepository.save(user)

        if (userRepository.countByBoards_Id(boardId) == 0) {
            logger.info("Deleting board permanently {}", boardId)
            boardRepository.deleteById(boardId)
        }
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoards()
    }

    fun addBoard(@Body form: NameForm, exchange: Exchange) {
        logger.info("Adding board with name: {}", form.name)
        val name = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.getByName(name)
        user.boards.add(BoardModel(name = form.name))
        userRepository.save(user)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoards()
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

    fun moveColumn(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                   @Header("toColumn") toColumn: Long?, exchange: Exchange) {
        logger.info("Reordering columns: {} and: {}", columnId, toColumn)
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
                 @Header("taskId") taskId: Long?,
                 @Header("toColumn") toColumn: Long?, exchange: Exchange) {
        logger.info("Moving task: {} toColumn: {}", taskId, toColumn)
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoard(1)
    }

    private fun getUserBoards(): Boards {
        val name = SecurityContextHolder.getContext().authentication.name
        val boardModels: List<BoardModel> = boardRepository.findByUsers_Name(name)
        val boards: List<Board> = boardModels.stream().map { model -> Board(model.id, model.name) }
                .collect(Collectors.toList())
        return Boards(boards)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(Trello::class.java)
    }
}
