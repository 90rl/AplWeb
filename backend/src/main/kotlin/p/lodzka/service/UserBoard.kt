package p.lodzka.service

class UserBoard(
        id: Long,
        name: String,
        var columns: List<Column>)
    : Board(id, name)