package p.lodzka.service

fun getUserBoards(): Boards {
    val boards = Boards()
    val board1 = Board(1, "Tablica1")
    val board2 = Board(2, "Tablica2")
    val board3 = Board(3, "Tablica3")
    boards.boards = listOf(board1, board2, board3)
    return boards
}

fun getUserBoard(id: Long): UserBoard {
    val firstTask = Task(1, "Zadanie1", "Opis zadania")
    val secondTask = Task(1, "Zadanie2", "Opis zadania")
    val tasks = mutableListOf(firstTask, secondTask)

    val firstColumn = Column(1,"kolumna1", 0, tasks)
    val secondColumn = Column(2,"kolumna2", 1, emptyList())
    val thirdColumn = Column(3,"kolumna3", 2, emptyList())
    val columns = mutableListOf(firstColumn, secondColumn, thirdColumn)

    return UserBoard(id, "Tablica1",  columns)
}