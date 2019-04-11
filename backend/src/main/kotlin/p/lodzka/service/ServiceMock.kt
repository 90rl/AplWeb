package p.lodzka.service

import p.lodzka.model.BoardModel
import p.lodzka.model.ColumnModel
import p.lodzka.model.TaskModel

fun getUserBoards(): Boards {
    val boards = Boards()
    val board1 = Board(1, "Tablica1")
    val board2 = Board(2, "Tablica2")
    val board3 = Board(3, "Tablica3")
    boards.boards = listOf(board1, board2, board3)
    return boards
}

fun getUserBoard(id: Long): BoardModel {
    val firstTask = TaskModel(1, "Zadanie1", "Opis zadania")
    val secondTask = TaskModel(1, "Zadanie2", "Opis zadania")
    val tasks = mutableListOf(firstTask, secondTask)

    val firstColumn = ColumnModel(1,"kolumna1", 0, tasks)
    val secondColumn = ColumnModel(2,"kolumna2", 1, emptyList())
    val thirdColumn = ColumnModel(3,"kolumna3", 2, emptyList())
    val columns = mutableListOf(firstColumn, secondColumn, thirdColumn)

    return BoardModel(id, "Tablica1",  columns)
}