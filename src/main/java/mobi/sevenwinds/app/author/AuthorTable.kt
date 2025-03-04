package mobi.sevenwinds.app.author

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

object AuthorTable: IntIdTable("author") {
    val fio = varchar("fio", 255)
    val creationDate = datetime("creation_date_time").default(DateTime.now())
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var fio by AuthorTable.fio
    var creationDateTime by AuthorTable.creationDate

    fun toRecord(): AuthorRecord {
        return AuthorRecord(fio, creationDateTime.toDate())
    }
}
