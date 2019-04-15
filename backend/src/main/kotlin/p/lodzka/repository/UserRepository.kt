package p.lodzka.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import p.lodzka.model.UserModel


@Repository
interface UserRepository : CrudRepository<UserModel, Long> {
    fun findByName(name: String): UserModel

    @EntityGraph(value = "UserModel.boards", type = EntityGraphType.LOAD)
    fun getByName(name: String): UserModel

    fun existsByBoards_Id(id: Long): Boolean
}