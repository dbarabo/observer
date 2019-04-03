package ru.barabo.observer.mantis

import ru.barabo.db.DbConnection
import ru.barabo.db.DbSetting
import ru.barabo.db.SessionException
import ru.barabo.observer.crypto.MasterKey

object MantisConnect : DbConnection(
        DbSetting("com.mysql.jdbc.Driver",
                "jdbc:mysql://192.168.0.26:3306/bugtracker?useUnicode=true&characterEncoding=UTF-8", //MasterKey.value("MANTIS_URL"),
                MasterKey.value("MANTIS_USER"),
                MasterKey.value("MANTIS_PSWD"),
                "select 1") ) {

    @Throws(SessionException::class)
    fun init() {
        if(!dbSetting.url.isEmpty()) {
            throw SessionException("URL already initialization")
        }
    }
}