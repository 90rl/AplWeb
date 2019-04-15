package p.lodzka.model

import p.lodzka.service.objects.Column
import p.lodzka.service.objects.UserBoard
import javax.persistence.*

@Entity
@Table(name = "boards")
@NamedEntityGraph(name = "BoardModel.columns",
        attributeNodes = [NamedAttributeNode("columns")])
class BoardModel(
        @Id @GeneratedValue var id: Long = 0,
        var name: String,
        @ManyToMany(mappedBy = "boards") var users: MutableList<UserModel> = mutableListOf(),
        @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true) var columns: MutableList<ColumnModel> = mutableListOf()
) {
    fun toUserBoard(): UserBoard {
        val cols: List<Column> = columns.map { colModel -> colModel.toColumn() }
        return UserBoard(id, name, cols)
    }

    fun removeColumn(columnId: Long) {
        val columnModel = columns.first{it.id == columnId}
        val currentPosition = columnModel.position
        columns.remove(columnModel)
        columns.filter { it.position > currentPosition }.forEach { it.position -= 1 }
    }
}