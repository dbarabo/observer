package ru.barabo.observer.afina

import ru.barabo.db.DbConnection
import ru.barabo.db.DbSetting
import ru.barabo.db.SessionException

object AfinaConnect: DbConnection(
        DbSetting("oracle.jdbc.driver.OracleDriver",
                "",
                "FNSUSER",
                "nhjkm",
                "select 1 from dual") ) {


    private const val AFINA_URL = "jdbc:oracle:thin:@192.168.0.43:1521:AFINA"

    private const val TEST_URL = "jdbc:oracle:thin:@192.168.0.42:1521:AFINA"

    @Throws(SessionException::class)
    fun init(isAfinaUrl :Boolean) {
        if(!dbSetting.url.isEmpty()) {
            throw SessionException("URL already initialization")
        }

        dbSetting.url = if(isAfinaUrl) AFINA_URL else TEST_URL
    }
}
