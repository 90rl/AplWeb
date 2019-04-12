package p.lodzka.model

import javax.persistence.*

@Entity
@Table(name = "tasks")
class TaskModel(
        @Id @GeneratedValue var id: Long,
        var name: String? = null,
        var description: String? = null,
        @ManyToOne @JoinColumn(name = "column_id") var column: ColumnModel? = null
)
