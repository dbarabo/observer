package ru.barabo.observer.afina

import ru.barabo.db.DbConnection
import ru.barabo.db.DbSetting
import ru.barabo.db.Query
import ru.barabo.observer.crypto.MasterKey
import java.sql.Clob

private object TestConnect : DbConnection(
    DbSetting("oracle.jdbc.driver.OracleDriver",
        "",
        MasterKey.value("AFINA_USER"),
        MasterKey.value("AFINA_PSWD"),
        "select 1 from dual",
        MasterKey.value("ANOTHER_USER"),
        MasterKey.value("ANOTHER_PSWD")
    ) ) {

    private const val TEST_URL = "jdbc:oracle:thin:@192.168.0.180:1521:AFINA" //"jdbc:oracle:thin:@192.168.0.42:1521:AFINA"

    fun init() {
        if(dbSetting.url.isEmpty()) {
            dbSetting.url = TEST_URL
        }
    }
}

private object TestQuery : Query(TestConnect)

internal fun modifyTestScript(currentFileName: String, nextFileName: String): String {

    TestConnect.init()

    val curLine = "'$currentFileName',"

    val nextLine = "'$nextFileName',"

    TestQuery.execute(EXEC_ADD_FILE, arrayOf(SCRIPT_FILE, curLine, nextLine))

    return getTextFileBackup()
}

internal fun getTextFileBackup(): String {
    TestConnect.init()

    val textClob = TestQuery.selectValue(SELECT_CLOB_DATA_FILE, arrayOf(SCRIPT_FILE)) as Clob

    return textClob.clobToString()
}

private const val SCRIPT_FILE = "/opt/oracle/af.sql"

private const val EXEC_ADD_FILE = "{ call OD.ADD_LINE_TO_FILE(?, ?, ?) }"

private const val SELECT_CLOB_DATA_FILE = "select od.READ_CLOBJ( ? ) from dual"
