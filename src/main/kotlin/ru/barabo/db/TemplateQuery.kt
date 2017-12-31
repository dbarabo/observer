package ru.barabo.db

import org.slf4j.LoggerFactory
import ru.barabo.db.annotation.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaType

open class TemplateQuery (private val query :Query) {

    private val logger = LoggerFactory.getLogger(TemplateQuery::class.java)

    companion object {

        private fun errorNotFoundAnnotationSelectQuery(className :String?) = "Annotation @SelectQuery not found for class $className"

        private fun errorNotFoundAnnotationTableName(className :String?) = "Annotation @TableName not found for class $className"

        private fun errorNotFoundAnnotationColumnName(className :String?) = "Annotation @ColumnName not found for class $className"

        private fun errorNotFoundAnnotationSequenceName(className :String?) = "Annotation @SequenceName not found for class $className"

        private fun errorValueType(typeSql :Int, value :Any) = "value $value is not type $typeSql"

        private val ERROR_NULL_VALUE_TYPE = "Value and Type value is null"

        private fun deleteTemplate(table :String) = "delete from $table where id = ?"

        private fun errorSequenceReturnNull(sequence :String) = "Sequence expression return NULL $sequence"

        private val ID_COLUMN = "ID"
    }

    @Throws(SessionException::class)
    fun <T> select(row :Class<T>, callBack :(priorRow :T?, row :T)->Unit) {
        val selectQuery =  getSelect(row)

        var item :T? = null

        var priorItem :T?  = null

        val propertyByColumn = getPropertyByColumn(row)

        val params = if(ParamsSelect::class.java.isAssignableFrom(row)) {
            (row.newInstance() as ParamsSelect).selectParams() } else null

        query.select(selectQuery, params, SessionSetting(true)) lambda@ {
            isNewRow :Boolean, value :Any?, column :String? ->

            if(isNewRow) {
                val newItem = row.newInstance()

                if(item != null) {
                    val itemCopy = item
                    callBack(priorItem, itemCopy!!)
                }

                priorItem = item
                item = newItem

                return@lambda
            }

            value ?: return@lambda

            val member = propertyByColumn[column] ?: return@lambda

            logger.info("javaType=${member.returnType.javaType}")

            logger.info("value=$value")

            val javaValue =  valueToJava(item as Any, value, member.returnType.javaType as Class<*>)

            javaValue ?: return@lambda

            logger.info("javaValue=$javaValue")

            member.setter.call(item, javaValue)
          }

        if(item != null) {
            callBack(priorItem, item!!)
        }
    }

    private fun getPropertyByColumn(row :Class<*>) :Map<String, KMutableProperty<*>> {
        val propertyByColumn = HashMap<String, KMutableProperty<*>>()

        for (member in row.kotlin.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()) {
            val columnName =member.findAnnotation<ColumnName>()?.name?.toUpperCase()?:continue

            propertyByColumn.put(columnName, member)
        }

        return propertyByColumn
    }

    @Throws(SessionException::class)
    fun save(item :Any) {

        val idField = getFieldData(item, ID_COLUMN)

        if(idField.second is Class<*>) {

            setSequenceValue(item)

            insert(item)

        } else {
            updateById(item)
        }
    }

    @Throws(SessionException::class)
    fun deleteById(item :Any) {

        val tableName = getTableName(item)

        val idField = getFieldData(item, ID_COLUMN)

        query.execute(deleteTemplate(tableName), Array(1, {idField.second}))
    }


    @Throws(SessionException::class)
    private fun getSelect(row :Class<*>) :String = row.kotlin.findAnnotation<SelectQuery>()?.name
            ?: throw SessionException(errorNotFoundAnnotationSelectQuery(row.simpleName))

    @Throws(SessionException::class)
    private fun insert (item :Any) {

        val tableName = getTableName(item)

        val fieldsData = getFieldsData(item)

        insert(tableName, fieldsData)
    }

    @Throws(SessionException::class)
    private fun updateById(item :Any) {
        val tableName = getTableName(item)

        val fieldsData = getFieldsData(item)

        val idField = fieldsData.firstOrNull { it.first.equals(ID_COLUMN, true) }
                ?: throw SessionException(errorNotFoundAnnotationColumnName(item::class.simpleName))

        val updateFields = fieldsData.filter{!it.first.equals(ID_COLUMN, true)}

        val updateColumns = updateFields.joinToString(" = ?, ",  "", " = ?"){it.first}

        val params = updateFields.map { it.second }.toMutableList()
        params.add(idField.second)

        val updateQuery = updateTemplate(tableName, updateColumns, idField.first)

        query.execute(updateQuery, params.toTypedArray())
    }

    private fun setSequenceValue(item :Any) {
        for (member in item::class.declaredMembers) {

            val annotationName = member.findAnnotation<SequenceName>()

            if(annotationName?.name != null){

                val valueSequence = getNextSequenceValue(annotationName.name)

                (member as KMutableProperty<*>).setter.call(item,
                        Type.convertValueToJavaTypeByClass(valueSequence, member.returnType.javaType as Class<*>))
                return
            }
        }
        throw SessionException(errorNotFoundAnnotationSequenceName(item::class.simpleName))
    }

    @Throws(SessionException::class)
    private fun getNextSequenceValue(sequenceExpression: String) :Any {
        return query.selectValue(sequenceExpression)
                ?: throw SessionException(errorSequenceReturnNull(sequenceExpression))
    }

    private fun updateTemplate(table :String, valueColumns :String, idColumn :String) = "update $table set $valueColumns where $idColumn = ?"


    private fun getInsertQuery(table :String, fields :List<FieldData>) :String {

        val columnNames = fields.joinToString(", ") {it.first}

        val questions = fields.joinToString(", ") { _ -> "?" }

        return "insert into $table ( $columnNames ) values ( $questions )"
    }

    @Throws(SessionException::class)
    private fun getTableName(item :Any) :String = item::class.findAnnotation<TableName>()?.name
            ?: throw SessionException(errorNotFoundAnnotationTableName(item::class.simpleName))


    @Throws(SessionException::class)
    private fun insert(table :String, fields :List<FieldData>) {

        val queryInsert= getInsertQuery(table, fields)

        val params :Array<Any?>? = fields.map { it.second }.toTypedArray()

        query.execute(queryInsert, params)
    }

    @Throws(SessionException::class)
    private fun getFieldData(item :Any, findColumn :String) :FieldData {
        for (member in item::class.declaredMemberProperties) {
            val annotationName =member.findAnnotation<ColumnName>()

            val annotationType =member.findAnnotation<ColumnType>()

            if(annotationName?.name != null && (findColumn.equals(annotationName.name, true))) {

                return FieldData(annotationName.name, valueToSql(item, member.call(item), annotationType?.type) )
            }
        }
        throw SessionException(errorNotFoundAnnotationColumnName(item::class.simpleName))
    }

    /**
     * из аннотаций вытаскиваем данные для sql
     */
    @Throws(SessionException::class)
    private fun getFieldsData(item :Any) :ArrayList<FieldData> {

        val fieldsData = ArrayList<FieldData>()

        for (member in item::class.declaredMemberProperties) {
            val annotationName =member.findAnnotation<ColumnName>()

            val annotationType =member.findAnnotation<ColumnType>()

            if(annotationName?.name != null) {

                fieldsData.add(FieldData(annotationName.name, valueToSql(item, member.call(item), annotationType?.type) ))
            }
        }

        if(fieldsData.size == 0) throw SessionException(errorNotFoundAnnotationColumnName(item::class.simpleName))

        return fieldsData
    }

    @Throws(SessionException::class)
    private fun valueToJava(item :Any, value :Any, javaType :Class<*>) :Any? {

        if(Type.isConverterExists(javaType)) {
            return Type.convertValueToJavaTypeByClass(value, javaType)
        }

        if(item is ConverterValue) {
            return item.convertFromBase(value, javaType)
        }

        return value
    }

    /**
     * преобразует значение value к типу type
     * Если value == null => return Class.Type
     */
    @Throws(SessionException::class)
    private fun valueToSql(item :Any, value :Any?, type :Int?) :Any {

        if(value != null && type == null) {
            return value
        }

        if(value == null && type != null) {
            return Type.getClassBySqlType(type)
        }

        if(value == null || type == null) {
            throw SessionException(ERROR_NULL_VALUE_TYPE)
        }

        if((value is Number) && Type.isNumberType(type)) {
            return value
        }

        if((value is Date) && Type.isDateType(type)) {
            return value
        }

        if(value is String && Type.isStringType(type) ) {
            return value
        }

        if(item is ConverterValue) {
            return item.convertToBase(value)
        }

        throw SessionException(errorValueType(type, value) )
    }

}

typealias FieldData = Pair<String, Any>