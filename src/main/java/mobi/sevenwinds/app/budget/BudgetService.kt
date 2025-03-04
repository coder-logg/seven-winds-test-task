package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorEntity
import mobi.sevenwinds.app.author.AuthorRecord
import mobi.sevenwinds.app.author.AuthorTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRecord): BudgetRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.author = body.authorId?.let{ AuthorEntity.findById(it) }
            }

            return@transaction entity.toRecord()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            addLogger(StdOutSqlLogger)
            val author = param.authorFio?.let{ AuthorEntity.find { AuthorTable.fio.lowerCase() eq it.toLowerCase() }.firstOrNull() }

            val query = BudgetTable
                .select { (BudgetTable.year eq param.year) and (BudgetTable.authorId eq author?.id) }
                .limit(param.limit, param.offset)
                .orderBy(BudgetTable.month to SortOrder.ASC, BudgetTable.amount to SortOrder.DESC)

            val total = query.count()
            val data = BudgetEntity.wrapRows(query).map { it.toYearsStatsItem() }

            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }

    private fun BudgetEntity.toYearsStatsItem() = BudgetYearStatsItem(
        year = this.year,
        month = this.month,
        amount = this.amount,
        type = this.type,
        author = this.author?.toRecord()
    )
}