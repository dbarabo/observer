package ru.barabo.observer.afina

import ru.barabo.db.DbConnection
import ru.barabo.db.DbSetting
import ru.barabo.db.SessionException
import ru.barabo.observer.crypto.MasterKey

object AfinaConnect : DbConnection(
        DbSetting("oracle.jdbc.driver.OracleDriver",
                "",
                MasterKey.value("AFINA_USER"),
                MasterKey.value("AFINA_PSWD"),
                "select 1 from dual",
                MasterKey.value("ANOTHER_USER"),
                MasterKey.value("ANOTHER_PSWD")
        ) ) {


    private val AFINA_URL = MasterKey.value("AFINA_URL")

    private const val TEST_URL = "jdbc:oracle:thin:@192.168.0.42:1521:AFINA" //"jdbc:oracle:thin:@192.168.0.180:1521:AFINA"

    @Throws(SessionException::class)
    fun init(isAfinaUrl :Boolean) {
        if(dbSetting.url.isNotEmpty()) {
            throw SessionException("URL already initialization")
        }

        dbSetting.url = if(isAfinaUrl) AFINA_URL else TEST_URL
    }
}
