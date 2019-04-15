package p.lodzka.model

import p.lodzka.service.Column
import p.lodzka.service.Task
import javax.persistence.*

@Entity
@Table(name = "columns")
class ColumnModel(
        @Id @GeneratedValue var id: Long,
        var name: String? = null,
        var position: Int = 0,
        @ManyToOne @JoinColumn(name = "board_id") var board: BoardModel? = null,
        @OneToMany(mappedBy = "column", cascade = [CascadeType.ALL], fetch = FetchType.EAGER) var tasks: MutableList<TaskModel> = mutableListOf()
) {
    fun toColumn(): Column {
        val tas: List<Task> = tasks.map { taskModel -> taskModel.toTask() }
        return Column(id, name, position, tas)
    }
}
