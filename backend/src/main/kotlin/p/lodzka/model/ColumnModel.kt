package p.lodzka.model

class ColumnModel (
    var id: Long,
    var name: String,
    var order: Int,
    var tasks: List<TaskModel>
)
