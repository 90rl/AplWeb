package p.lodzka.model

import javax.persistence.*

@Entity
@Table(name = "columns")
class ColumnModel(
        @Id @GeneratedValue var id: Long,
        var name: String? = null,
        var position: Int? = 0,
        @ManyToOne @JoinColumn(name = "board_id") var board: BoardModel? = null,
        @OneToMany(mappedBy = "column", cascade = [CascadeType.ALL], fetch = FetchType.EAGER) var tasks: List<TaskModel> = mutableListOf()
)
