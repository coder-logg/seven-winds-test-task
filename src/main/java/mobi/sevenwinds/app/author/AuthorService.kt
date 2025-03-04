package mobi.sevenwinds.app.author

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

object AuthorService {
    suspend fun add(body: AuthorCreationDto): AuthorRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = AuthorEntity.new {
                this.fio = body.fio
            }

            return@transaction entity.toRecord()
        }
    }
}