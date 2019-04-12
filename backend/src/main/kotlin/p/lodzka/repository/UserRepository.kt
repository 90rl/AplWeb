package p.lodzka.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import p.lodzka.model.UserModel

@Repository
interface UserRepository : CrudRepository<UserModel, Long>