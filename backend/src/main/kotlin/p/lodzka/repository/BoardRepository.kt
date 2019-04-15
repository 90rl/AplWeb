package p.lodzka.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import p.lodzka.model.BoardModel

@Repository
interface BoardRepository : CrudRepository<BoardModel, Long> {

    fun findByUsers_Name(name: String): List<BoardModel>

}