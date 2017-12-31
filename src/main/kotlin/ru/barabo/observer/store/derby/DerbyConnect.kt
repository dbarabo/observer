package ru.barabo.observer.store.derby

import ru.barabo.db.DbConnection
import ru.barabo.db.DbSetting

object DerbyConnect : DbConnection(
        DbSetting("org.apache.derby.jdbc.EmbeddedDriver",
                "jdbc:derby:derbyDB;create=true",
                "observ",
                "troll",
                "select ID from ALLDATA where ID = 1") ) {

    private val TBL_ALLDATA = """
        create table ALLDATA (
        ID integer not null,
        ID_ELEM bigint not null,
        NAME varchar(300) not null,
        CREATED TIMESTAMP not null,
        EXECUTED TIMESTAMP,
        STATE integer not null default 0,
        TASK varchar(300) not null,
        PATH varchar(500),
        ERROR varchar(500),
        TARGET varchar(500),
        BASE integer not null default 0,
        primary key(ID) )"""

    private val SEQ_ALLDATA = "CREATE SEQUENCE ALLDATA_ID"

    init {
        checkCreateStructure()
    }

    private fun checkCreateStructure() {

        val session = addSession(false)

        if(!checkBase(session)) {
            session.execute(TBL_ALLDATA)

            session.execute(SEQ_ALLDATA)
        }
        session.isFree = true
    }
}