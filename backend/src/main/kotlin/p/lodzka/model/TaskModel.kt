package p.lodzka.model

import p.lodzka.service.objects.Task
import javax.persistence.*

@Entity
@Table(name = "tasks")
class TaskModel(
        @Id @GeneratedValue var id: Long = 0,
        var name: String? = null,
        var description: String? = null,
        @ManyToOne @JoinColumn(name = "column_id") var column: ColumnModel? = null
) {
    fun toTask(): Task {
        return Task(id, name, description)
    }
}
