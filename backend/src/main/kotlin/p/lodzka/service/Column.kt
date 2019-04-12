package p.lodzka.service

class Column (
        var id: Long,
        var name: String? = null,
        var position: Int,
        var tasks: List<Task>
)