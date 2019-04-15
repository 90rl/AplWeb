package p.lodzka.service.objects

class Column (
        var id: Long,
        var name: String? = null,
        var position: Int,
        var tasks: List<Task>
)