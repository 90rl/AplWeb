package p.lodzka.model

class BoardModel(
        var id: Long? = null,
        var name: String,
        var columns: List<ColumnModel>
)