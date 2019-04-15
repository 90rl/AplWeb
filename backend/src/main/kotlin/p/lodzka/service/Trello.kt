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
import p.lodzka.model.ColumnModel
import p.lodzka.model.TaskModel
import p.lodzka.model.UserModel
import p.lodzka.repository.BoardRepository
import p.lodzka.repository.ColumnRepository
import p.lodzka.repository.TaskRepository
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
    lateinit var columnRepository: ColumnRepository

    @Autowired
    lateinit var taskRepository: TaskRepository

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
        setStandardBoardsListResponse(exchange)
    }

    fun addBoard(@Body form: NameForm, exchange: Exchange) {
        logger.info("Adding board with name: {}", form.name)
        val name = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.getByName(name)
        user.boards.add(BoardModel(name = form.name))
        userRepository.save(user)
        setStandardBoardsListResponse(exchange)
    }

    fun getBoard(@Header("boardId") boardId: Long, exchange: Exchange) {
        logger.info("Getting board: {}", boardId)
        setStandardBoardResponse(exchange, boardId)
    }

    fun deleteBoard(@Header("boardId") boardId: Long, exchange: Exchange) {
        logger.info("Deleting board: {}", boardId)
        val name = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.getByName(name)
        user.boards.removeAll { b -> b.id == boardId }
        userRepository.save(user)

        if (!userRepository.existsByBoards_Id(boardId)) {
            logger.info("Deleting board permanently {}", boardId)
            boardRepository.deleteById(boardId)
        }
        setStandardBoardsListResponse(exchange)
    }

    fun addColumn(@Body form: NameForm?, @Header("boardId") boardId: Long, exchange: Exchange) {
        logger.info("Adding column with name: {} to board: {}", form?.name, boardId)
        val boardModel = boardRepository.getById(boardId)
        val colCount = columnRepository.countByBoard_Id(boardId)
        boardModel.columns.add(ColumnModel(name = form?.name, position = colCount, board = boardModel))
        boardRepository.save(boardModel)
        setStandardBoardResponse(exchange, boardId)
    }

    fun deleteColumn(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long, exchange: Exchange) {
        logger.info("Deleting column: {} in board: {}", columnId, boardId)
        val boardModel = boardRepository.getById(boardId)
        boardModel.removeColumn(columnId)
        boardRepository.save(boardModel)
        setStandardBoardResponse(exchange, boardId)
    }

    fun moveColumn(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                   @Header("toPosition") toPosition: Long, exchange: Exchange) {
        logger.info("Reordering columns: {} and: {}", columnId, toPosition)
      /*  val colFrom = columnRepository.findById(columnId).get()
        val boardModel = boardRepository.getById(boardId)
        boardModel.columns.filter { it.position }
        val temp = colFrom.position
        colTo.position = colFrom.position
        colFrom.position = temp
        columnRepository.saveAll(mutableListOf(colFrom, colTo))*/

        //fixme zaimplementowac porpawnie
        setStandardBoardResponse(exchange, boardId)
    }

    fun addTask(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                @Body form: TaskForm?, exchange: Exchange) {
        logger.info("Adding task to board: {} to column {}, task name: ", boardId, columnId, form?.name)
        val columnModel: ColumnModel = columnRepository.findById(columnId).get()
        columnModel.tasks.add(TaskModel(name = form?.name, description = form?.description, column = columnModel))
        columnRepository.save(columnModel)
        setStandardBoardResponse(exchange, boardId)
    }

    fun deleteTask(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                   @Header("taskId") taskId: Long, exchange: Exchange) {
        logger.info("Deleting task from board: {} on column {}, taskId: ", boardId, columnId, taskId)
        val columnModel = columnRepository.findById(columnId).get()
        columnModel.tasks.removeAll { it.id == taskId }
        columnRepository.save(columnModel)
        setStandardBoardResponse(exchange, boardId)
    }

    fun moveTask(@Header("boardId") boardId: Long, @Header("columnId") columnId: Long,
                 @Header("taskId") taskId: Long,
                 @Header("toColumn") toColumn: Long, exchange: Exchange) {
        logger.info("Moving task: {} toColumn: {}", taskId, toColumn)
        val task = taskRepository.findById(taskId).get()
        val columnModelFrom = columnRepository.findById(columnId).get()
        val columnModelTo = columnRepository.findById(toColumn).get()
        task.column = columnModelTo
        columnModelTo.tasks.add(task)
        columnModelFrom.tasks.remove(task)
        columnRepository.saveAll(mutableListOf(columnModelFrom, columnModelTo))
        setStandardBoardResponse(exchange, boardId)
    }

    private fun setStandardBoardResponse(exchange: Exchange, boardId: Long) {
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getBoardById(boardId)
    }

    private fun getUserBoards(): Boards {
        val name = SecurityContextHolder.getContext().authentication.name
        val boardModels: List<BoardModel> = boardRepository.findByUsers_Name(name)
        val boards: List<Board> = boardModels.stream().map { model -> Board(model.id, model.name) }
                .collect(Collectors.toList())
        return Boards(boards)
    }

    private fun getBoardById(boardId: Long): UserBoard {
        val boardModel: BoardModel = boardRepository.getById(boardId)
        boardModel.columns.sortBy { it.position }
        return boardModel.toUserBoard()
    }

    private fun setStandardBoardsListResponse(exchange: Exchange) {
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, SC_OK)
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, APPLICATION_JSON)
        exchange.getIn().body = getUserBoards()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(Trello::class.java)
    }
}
