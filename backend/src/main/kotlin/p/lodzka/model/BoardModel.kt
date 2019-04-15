package p.lodzka.model

import p.lodzka.service.Column
import p.lodzka.service.UserBoard
import javax.persistence.*

@Entity
@Table(name = "boards")
class BoardModel(
        @Id @GeneratedValue var id: Long = 0,
        var name: String,
        @ManyToMany(mappedBy = "boards") var users: MutableList<UserModel> = mutableListOf(),
        @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], fetch = FetchType.EAGER) var columns: MutableList<ColumnModel> = mutableListOf()
) {
    fun toUserBoard(): UserBoard {
        val cols:List<Column>  = columns.map { colModel -> colModel.toColumn() }
        return UserBoard(id, name, cols)
    }
}