package p.lodzka.model

import javax.persistence.*

@Entity
@Table(name = "boards")
@NamedEntityGraph(name = "BoardModel.users",
        attributeNodes = [NamedAttributeNode("users")])
class BoardModel(
        @Id @GeneratedValue var id: Long = 0,
        var name: String,
        @ManyToMany(mappedBy = "boards") var users: MutableList<UserModel> = mutableListOf(),
        @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], fetch = FetchType.EAGER) var columns: List<ColumnModel> = mutableListOf()
)