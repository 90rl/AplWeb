package p.lodzka.model

import javax.persistence.*

@Entity
@Table(name = "boards")
class BoardModel(
        @Id @GeneratedValue var id: Long,
        var name: String,
        @ManyToMany(mappedBy = "boards") var users: List<UserModel> = mutableListOf(),
        @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], fetch = FetchType.EAGER) var columns: List<ColumnModel> = mutableListOf()
)