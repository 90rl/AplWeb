package p.lodzka.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import p.lodzka.model.UserModel
import javax.persistence.NamedAttributeNode
import javax.persistence.NamedEntityGraph


@Repository
/*@NamedEntityGraph(name = "UserModel.boards",
        attributeNodes = [NamedAttributeNode("boards")])*/
interface UserRepository : CrudRepository<UserModel, Long> {
    fun findByName(name: String): UserModel

/*    @EntityGraph(value = "UserModel.boards", type = EntityGraphType.LOAD)
    fun findByName(name: String): UserModel*/
}