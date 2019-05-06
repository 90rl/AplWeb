package p.lodzka.model

import javax.persistence.*

@Entity
@Table(name = "users")
@NamedEntityGraph(name = "UserModel.boards",
        attributeNodes = [NamedAttributeNode("boards")])
class UserModel(
        @Id @GeneratedValue var id: Long = 0,
        @Column(unique = true) var name: String,
        var password: String,
        var email: String,
        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(name = "user_board",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "board_id", referencedColumnName = "id")])
        var boards: MutableList<BoardModel> = mutableListOf()
)