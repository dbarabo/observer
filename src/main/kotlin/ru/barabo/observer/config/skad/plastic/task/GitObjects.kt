package ru.barabo.observer.config.skad.plastic.task

import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.barabo.p440.out.byFolderExists
import ru.barabo.observer.config.skad.plastic.PlasticOutSide
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.io.File
import java.nio.charset.Charset
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object GitObjects : SinglePerpetual {
    //private val logger = LoggerFactory.getLogger(GitObjects::class.java)

    override fun name(): String = "Autocommit объектов"

    override fun config(): ConfigTask = PlasticOutSide

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 30

    override val accessibleData: AccessibleData = AccessibleData(workWeek = WeekAccess.WORK_ONLY,
            workTimeFrom = LocalTime.of(7, 40), workTimeTo = LocalTime.of(21, 30) )

    override fun execute(elem: Elem): State {

        val objects = AfinaQuery.select(SELECT_OBJECTS)
        if(objects.isEmpty()) return super.execute(elem)

        var isModified = false

        for (dbObject in objects) {

            isModified = isModifiedProcessData(dbObject) || isModified
        }

        if(isModified) {
            gitCommit()
        }

        return super.execute(elem)
    }

    private fun gitCommit() =  Cmd.execDos(cmdGitCommit)

    private fun isModifiedProcessData(dbObject: Array<Any?>): Boolean {
        val nameObject = dbObject[0] as String

        val typeObject = convertType(dbObject[1] as String)

        val lastDDL = dbObject[2]

        val objectId = dbObject[3]

        val file = createFile(nameObject, typeObject)

        val data = loadDataDDL(nameObject)

        val isModified = (!file.exists() || objectId == null || file.readText(Charset.defaultCharset()) != data)

        if(isModified) file.writeText(data, Charset.defaultCharset())

        updateObjectsInfo(nameObject, typeObject, lastDDL, objectId, isModified)

        return isModified
    }

    private fun convertType(type: String): String = if(type == "PACKAGE BODY") "PACKAGE" else type

    private fun createFile(nameObject: String, typeObject: String): File {

        val typeFolder =
        when (typeObject) {
        "PACKAGE" -> "package"
        "PROCEDURE", "FUNCTION" -> "func"
        else -> "table"
        }

        val suffix =  if(nameObject.indexOf("PTKB") >= 0 || nameObject.indexOf("PTCB") >= 0)"-ptkb" else ""

        val folder = "$path/$typeFolder$suffix".byFolderExists()

        return File("${folder.absolutePath}/$nameObject.sql")
    }

    private fun loadDataDDL(nameObject: String): String {

        val lines = AfinaQuery.select(SELECT_LOAD_DATA, arrayOf(nameObject) )

        return lines.joinToString("") { it[0].toString() }
    }

    private fun updateObjectsInfo(nameObject: String, typeObject: String, lastDDLTime: Any?,
                                  objectId: Any?, isModified: Boolean) {

        if(objectId == null) {
            AfinaQuery.execute(INSERT_OBJECT, arrayOf(typeObject, nameObject, lastDDLTime) )
        } else {
            val execSql = if(isModified) UPDATE_MODIFY_OBJECT else UPDATE_EQUAL_OBJECT

            AfinaQuery.execute(execSql, arrayOf(lastDDLTime, objectId) )
        }
    }

    private const val cmdGitCommit = "C: && cd C:\\store\\od && git add . && git commit -m \"autocommit\" "

    private const val path = "C:/store/od"

    private const val SELECT_OBJECTS = """
SELECT o.OBJECT_NAME, o.OBJECT_TYPE, o.LAST_DDL_TIME, p.id 
FROM ALL_OBJECTS o
left join od.ptkb_objects p on (p.owner = o.OWNER and o.OBJECT_NAME =  p.name_object)
where o.OWNER = 'OD'
  and o.OBJECT_TYPE in ('FUNCTION','PROCEDURE','PACKAGE BODY', 'PACKAGE')
   and o.LAST_DDL_TIME > coalesce(p.last_ddl, min_date)"""

    private const val UPDATE_EQUAL_OBJECT = "update od.ptkb_objects set last_ddl = ? where id = ?"

    private const val UPDATE_MODIFY_OBJECT =
            "update od.ptkb_objects set prior_ddl = last_ddl, last_ddl = ?, times = times + 1 where id = ?"

    private const val INSERT_OBJECT =
            "insert into od.ptkb_objects (id, owner, type_object, name_object, last_ddl) values (classified.nextval, 'OD', ?, ?, ?)"

    private const val SELECT_LOAD_DATA = """
SELECT text
FROM all_source 
WHERE name = ?
  and owner = 'OD'
ORDER BY TYPE, LINE"""

}

//, 'TABLE'