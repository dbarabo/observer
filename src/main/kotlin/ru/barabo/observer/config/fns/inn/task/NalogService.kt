package ru.barabo.observer.config.fns.inn.task

import com.google.gson.Gson
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import java.net.URLEncoder

import org.apache.http.Header
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHeader
import org.apache.http.util.EntityUtils
import java.sql.Timestamp
import java.text.SimpleDateFormat

class NalogService {

    fun getInnByPerson(person: Person): Pair<Int, String> {

        val httpClient = HttpClients.createDefault()

        val headers = getHeaders()

        val request = HttpPost("https://service.nalog.ru/inn-new-proc.json").apply {
            setHeaders( headers )
        }

        val json =
            "c=find&captcha=&captchaToken=&fam=${person.surname.encodeValue()}&nam=${person.firstName.encodeValue()}&otch=${person.secondName.encodeValue()}&bdate=${person.birthday.formatRus()}&doctype=21&docno=${person.docNumber.encodeValue()}&docdt=${person.docDate.formatRus()}"

        request.entity = StringEntity(json, ContentType.APPLICATION_JSON)

        httpClient.use {
            val response = httpClient.execute(request)

            val result = EntityUtils.toString(response.entity)

            if(response.statusLine.statusCode != 200) {
                return Pair(response.statusLine.statusCode, result)
            }

            val fnsResult = Gson().fromJson(result, FnsResultRequest::class.java)

            if( fnsResult.requestId.isEmpty() ) {
                return Pair(500, "fnsResult.requestId is null!!!")
            }

            val entityNext = StringEntity("c=get&requestId=${fnsResult.requestId}", ContentType.APPLICATION_JSON)

            request.setHeaders(headers)

            request.entity = entityNext

            val responseNext = httpClient.execute(request)

            val resultNext = EntityUtils.toString(responseNext.entity)

            if(responseNext.statusLine.statusCode != 200) {
                return Pair(responseNext.statusLine.statusCode, resultNext)
            }

            val fnsResultInn = Gson().fromJson(resultNext, FnsResultInn::class.java)

            return Pair(200, fnsResultInn.inn?:"")
        }
    }

    private fun getHeaders(): Array<Header> {

        return arrayOf(
            BasicHeader("Accept", "application/json, text/javascript, */*; q=0.01"),

            BasicHeader("Accept-Encoding", "gzip, deflate, br, zstd"),
            BasicHeader("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7"),
            BasicHeader("Connection", "keep-alive"),
            BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"),
            BasicHeader(
                "Cookie",
                "_ym_uid=1775092061584582981; _ym_d=1791260897; uniI18nLang=RUS; JSESSIONID=477E0E35F92FD41CC363F15EF929F64F; upd_inn=3A392E2F44EC4AD7D31FA43044EC11FA3DB5AEE019A906FAF59CA482E4E9FD2CEA6DF2D25652AAE7D6E17DE680CDE956; _ym_isad=2; _ym_visorc=b; mdd=0"
            ),
            BasicHeader("Host", "service.nalog.ru"),
            BasicHeader("Origin", "https://service.nalog.ru"),
            BasicHeader("Referer", "https://service.nalog.ru/inn.do"),

            BasicHeader("Sec-Fetch-Dest", "empty"),
            BasicHeader("Sec-Fetch-Mode", "cors"),
            BasicHeader("Sec-Fetch-Site", "same-origin"),
            BasicHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:157.0) Gecko/20100101 Firefox/157.0"
            ),
            BasicHeader("X-Requested-With", "XMLHttpRequest")
        )
    }
}

private val dateFormatRus = SimpleDateFormat("dd.MM.yyyy")

class Person(val birthday: Timestamp, val docDate: Timestamp, val idClient: Number? = null,
    val surname: String = "",
    val firstName: String = "",
    val secondName: String = "",
    val docNumber: String = ""
    )

fun String.encodeValue(): String = URLEncoder.encode(this, "UTF-8")

fun Timestamp.formatRus(): String = dateFormatRus.format(this)

fun personFromRow(row: Array<Any?>): Person? {

    val line = row[0] as String

    val number = row[1] as String

    val out = row[2] as? Timestamp

    val lastname = row[3] as? String

    val firstname = row[4] as? String

    val secondname = if(row[5] == null) "" else row[5] as String

    val birthday = row[6] as? Timestamp

    val clientId = row[7] as Number

    if(out == null ||
        lastname == null ||
        firstname == null ||
        birthday == null) return null

    return Person(birthday, out, clientId,  lastname,
        firstname, secondname, "${line.substring(0..1)} ${line.substring(2)} $number")
}


class FnsResultRequest() {
    var requestId: String = ""
    var token1: String = ""
    var captchaRequired: Boolean = false
}

class FnsResultInn() {
    var id: String = ""
    var state: Int = 0
    var inn: String? = null
    var entityId: Long = 0
    var result: String = ""
}