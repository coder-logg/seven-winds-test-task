package mobi.sevenwinds.app.author

import com.fasterxml.jackson.annotation.JsonFormat
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import java.util.*


fun NormalOpenAPIRoute.author() {
    route("/author").post<Unit, AuthorRecord, AuthorCreationDto>(info("Добавить автора")) { param, body ->
        respond(AuthorService.add(body))
    }
}

data class AuthorRecord(
    val fio: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val creationDateTime: Date
)

data class AuthorCreationDto(
    val fio: String
)