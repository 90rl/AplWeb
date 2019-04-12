package p.lodzka.model

import javax.persistence.*

@Entity
@Table(name = "users")
class UserModel(
        @Id @GeneratedValue var id: Long = 0,
        var name: String? = null,
        var password: String,
        var email: String,
        @ManyToMany
        @JoinTable(name = "user_board",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "board_id", referencedColumnName = "id")])
        var boards: List<BoardModel> = mutableListOf()
)