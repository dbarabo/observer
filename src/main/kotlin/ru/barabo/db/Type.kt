package ru.barabo.db

import org.slf4j.LoggerFactory

enum class Type(val clazz: Class<*>, val sqlType: Int, val converter :(x :Any)->Any?) {

    BYTE(Byte::class.javaObjectType, java.sql.Types.TINYINT, { x -> (x as Number).toByte()} ),
    SMALLINT(Short::class.javaObjectType, java.sql.Types.SMALLINT, { x -> (x as Number).toShort()} ),
    INT(Int::class.javaObjectType, java.sql.Types.INTEGER, { x -> (x as Number).toInt()} ),
    LONG(Long::class.javaObjectType, java.sql.Types.BIGINT, { x -> (x as Number).toLong()} ),
    STRING(String::class.javaObjectType, java.sql.Types.VARCHAR, { x -> x.toString()} ),
    DECIMAL(Double::class.javaObjectType, java.sql.Types.DECIMAL, { x -> (x as Number).toDouble()} ),
    DATE(java.time.LocalDateTime::class.javaObjectType, java.sql.Types.TIMESTAMP, { x -> (x as java.util.Date).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()} );

    companion object {
        private val logger = LoggerFactory.getLogger(Type::class.java)

        private val HASH_CLASS_TYPES = Type.values().map { it -> Pair(it.clazz, it.sqlType) }.toMap()

        fun getSqlTypeByClass(clazz: Class<*>) :Int = HASH_CLASS_TYPES[clazz] ?:-1

        private val HASH_TYPES_CLASS = Type.values().map { it -> Pair(it.sqlType, it.clazz) }.toMap()

        private fun errorNotFoundClassType(typeSql :Int) = "For type $typeSql Class not found"

        @Throws(SessionException::class)
        fun getClassBySqlType(typeSql:Int) :Class<*> {
            logger.info("typeSql=$typeSql")

            return HASH_TYPES_CLASS[typeSql] ?: throw SessionException(errorNotFoundClassType(typeSql))
        }

        private val HASH_CLASS_CONVERTER = Type.values().map { it -> Pair(it.clazz, it.converter) }.toMap()

        fun convertValueToJavaTypeByClass(value :Any, clazz :Class<*>) :Any? = HASH_CLASS_CONVERTER[clazz]?.invoke(value)

        fun isConverterExists(clazz :Class<*>) :Boolean {
            return HASH_CLASS_CONVERTER[clazz] != null
        }

        fun isNumberType(type :Int) :Boolean {
            return when (type) {
                java.sql.Types.INTEGER,
                java.sql.Types.SMALLINT,
                java.sql.Types.BIT,
                java.sql.Types.BIGINT,
                java.sql.Types.FLOAT,
                java.sql.Types.REAL,
                java.sql.Types.DOUBLE,
                java.sql.Types.NUMERIC,
                java.sql.Types.DECIMAL -> true
                else -> false
            }
        }

        fun isDateType(type :Int) :Boolean {
            return when (type) {
                java.sql.Types.DATE,
                java.sql.Types.TIME,
                java.sql.Types.TIMESTAMP -> true
                else -> false
            }
        }

        fun isStringType(type :Int) :Boolean {
            return when (type) {
                java.sql.Types.VARCHAR,
                java.sql.Types.CHAR -> true
                else -> false
            }
        }
    }
}

