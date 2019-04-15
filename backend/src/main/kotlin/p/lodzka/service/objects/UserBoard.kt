package p.lodzka.service.objects

class UserBoard(
        id: Long,
        name: String,
        var columns: List<Column>)
    : Board(id, name)