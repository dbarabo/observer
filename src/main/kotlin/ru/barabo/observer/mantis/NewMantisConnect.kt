package ru.barabo.observer.mantis

import ru.barabo.db.DbConnection
import ru.barabo.db.DbSetting
import ru.barabo.db.Query
import ru.barabo.db.SessionException

object NewMantisConnect : DbConnection(
    DbSetting("org.firebirdsql.jdbc.FBDriver",
        "jdbc:firebirdsql://192.168.0.75:3050/c:/percoBD/BD/SCD17K.FDB?encoding=UNICODE_FSS",//"jdbc:mysql://192.168.0.26:3306/bugtracker?useUnicode=true&characterEncoding=UTF-8",
        "SYSDBA",
        "masterke",
        "select * from staff  where id_staff = 25815") ) {  //?encoding=UNICODE_FSS

    @Throws(SessionException::class)
    fun init() {
        if(!dbSetting.url.isEmpty()) {
            throw SessionException("URL already initialization")
        }
    }
}

object FbQuery : Query(NewMantisConnect) {

}