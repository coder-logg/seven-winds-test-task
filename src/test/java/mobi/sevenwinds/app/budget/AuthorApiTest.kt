package mobi.sevenwinds.app.budget

import io.restassured.RestAssured
import mobi.sevenwinds.app.author.AuthorCreationDto
import mobi.sevenwinds.app.author.AuthorEntity
import mobi.sevenwinds.app.author.AuthorRecord
import mobi.sevenwinds.app.author.AuthorTable
import mobi.sevenwinds.common.ServerTest
import mobi.sevenwinds.common.jsonBody
import mobi.sevenwinds.common.toResponse
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthorApiTest : ServerTest() {

    @BeforeEach
    internal fun setUp() {
        transaction { AuthorTable.deleteAll() }
    }

    @Test
    fun testAuthorCreation() {
        val author = AuthorCreationDto("Nikto Niktoevich")
        RestAssured.given()
            .jsonBody(author)
            .post("/author")
            .toResponse<AuthorRecord>().let {
                Assert.assertEquals(it.fio, author.fio)
            }

        val updatedRowsCount = transaction { AuthorEntity.find { AuthorTable.fio eq author.fio }.count() }
        Assert.assertEquals(updatedRowsCount, 1)
    }

}