package ru.barabo.observer.store

import org.slf4j.LoggerFactory
import ru.barabo.db.ConverterValue
import ru.barabo.db.annotation.*
import ru.barabo.observer.config.task.ActionTask
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.reflect.full.isSubclassOf


@TableName("ALLDATA")
@SelectQuery("select * from ALLDATA where BASE = ? and (CURRENT_DATE in (DATE(CREATED), DATE(EXECUTED)) or STATE in (0, 2) ) order by TASK, STATE")
data class Elem (

        @ColumnName("ID")
        @SequenceName("VALUES (NEXT VALUE FOR ALLDATA_ID)")
        @ColumnType(java.sql.Types.INTEGER)
        var id :Int? = null,

        @ColumnName("ID_ELEM")
        @ColumnType(java.sql.Types.BIGINT)
        var idElem :Long? = null,

        @ColumnName("NAME")
        @ColumnType(java.sql.Types.VARCHAR)
        var name :String = "",

        @ColumnName("STATE")
        @ColumnType(java.sql.Types.INTEGER)
        var state :State = State.NONE,

        @ColumnName("TASK")
        @ColumnType(java.sql.Types.VARCHAR)
        var task : ActionTask? = null,

        @ColumnName("PATH")
        @ColumnType(java.sql.Types.VARCHAR)
        var path :String = "",

        @ColumnName("CREATED")
        @ColumnType(java.sql.Types.TIMESTAMP)
        var created :LocalDateTime = LocalDateTime.now(),

        @ColumnName("EXECUTED")
        @ColumnType(java.sql.Types.TIMESTAMP)
        var executed :LocalDateTime? = null,

        @ColumnName("ERROR")
        @ColumnType(java.sql.Types.VARCHAR)
        var error :String? = null,

        @ColumnName("TARGET")
        @ColumnType(java.sql.Types.VARCHAR)
        var target :String? = null,

        @ColumnName("BASE")
        @ColumnType(java.sql.Types.INTEGER)
        var base :Int = if(TaskMapper.isAfinaBase()) 1 else 0)

       : ConverterValue, ParamsSelect {

        private val logger = LoggerFactory.getLogger(Elem::class.java)

        override fun selectParams() :Array<Any?>? =  Array(1, {if(TaskMapper.isAfinaBase()) 1 else 0 })

        override fun convertToBase(value :Any) :Any {
                logger.info("value=$value")
                logger.info("class=${value.javaClass.canonicalName}")

                return when (value) {
                        is Enum<*>  -> value.ordinal
                        is LocalDateTime -> Date.from(value.atZone(ZoneId.systemDefault()).toInstant())
                        else -> value.javaClass.canonicalName
                }
        }

        @Suppress("UNCHECKED_CAST")
        override fun convertFromBase(value :Any, javaType :Class<*>) :Any? {

                logger.info("convertFromBase value=$value javaType= $javaType" )
                when {
                        javaType.kotlin.isSubclassOf(ActionTask::class) -> {

                                return TaskMapper.objectByClass(value as String)
                        }

                        Enum::class.java.isAssignableFrom(javaType) -> {
                                val valNumber = (value as Number).toInt()

                                val enums = javaType.enumConstants as Array<out Enum<*>>

                                return enums[valNumber]
                        }
                        else -> {
                                //logger.error("convertFromBase else")
                                return value
                        }
                }
        }

        constructor(file : File, actionTask :ActionTask, waitExecuted :Duration?, targetIt :String? = null) : this() {

                idElem = file.lastModified()

                name = file.name

                path = file.parent

                task = actionTask

                executed = if(waitExecuted == null) null else created.plusSeconds(waitExecuted.seconds)

                target = targetIt
        }


        constructor (idElem: Number?, name :String?, actionTask :ActionTask, waitExecuted :Duration?): this() {

            this.idElem = idElem?.toLong()

            this.name = name?.let { it } ?:""

            task = actionTask

            executed = if(waitExecuted == null) null else created.plusSeconds(waitExecuted.seconds)
        }

        fun getFile() :File = File(path + "/" + name)


        fun isFindByIdName(idElem :Long?, name :String, isDuplicateName :Boolean) :Boolean {

            val result=  this.name == name && (!isDuplicateName || (this.idElem == idElem))

            return result
        }
}

