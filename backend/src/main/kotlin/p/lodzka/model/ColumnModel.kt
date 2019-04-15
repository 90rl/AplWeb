package p.lodzka.model

import p.lodzka.service.objects.Column
import p.lodzka.service.objects.Task
import javax.persistence.*

@Entity
@Table(name = "columns")
class ColumnModel(
        @Id @GeneratedValue var id: Long = 0,
        var name: String? = null,
        var position: Int = 0,
        @ManyToOne @JoinColumn(name = "board_id") var board: BoardModel? = null,
        @OneToMany(mappedBy = "column", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true) var tasks: MutableList<TaskModel> = mutableListOf()
) {
    fun toColumn(): Column {
        val tas: List<Task> = tasks.map { taskModel -> taskModel.toTask() }
        return Column(id, name, position, tas)
    }
}
