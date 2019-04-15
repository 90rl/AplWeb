package p.lodzka.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import p.lodzka.model.BoardModel
import p.lodzka.model.ColumnModel
import p.lodzka.model.TaskModel
import p.lodzka.model.UserModel


@Repository
interface UserRepository : CrudRepository<UserModel, Long> {
    fun findByName(name: String): UserModel

    @EntityGraph(value = "UserModel.boards", type = EntityGraphType.LOAD)
    fun getByName(name: String): UserModel

    fun existsByBoards_Id(id: Long): Boolean
}

@Repository
interface BoardRepository : CrudRepository<BoardModel, Long> {

    fun findByUsers_Name(name: String): List<BoardModel>

    @EntityGraph(value = "BoardModel.columns", type = EntityGraphType.LOAD)
    fun getById(id: Long): BoardModel

}

@Repository
interface ColumnRepository : CrudRepository<ColumnModel, Long> {
    fun countByBoard_Id(id: Long): Int
}

@Repository
interface TaskRepository : CrudRepository<TaskModel, Long>