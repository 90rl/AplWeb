package p.lodzka.service

class UserBoard(
        id: Long, name: String,
        var columns: MutableList<Column>)
    : Board(id, name)