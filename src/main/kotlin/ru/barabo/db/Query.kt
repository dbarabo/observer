package ru.barabo.db

import oracle.jdbc.OracleCallableStatement
import oracle.jdbc.OracleTypes
import org.slf4j.LoggerFactory
import java.sql.*
import java.util.concurrent.atomic.AtomicLong

open class Query (private val dbConnection :DbConnection) {

    companion object {
        private val logger = LoggerFactory.getLogger(Query::class.java)

        private const val ERROR_STATEMENT_NULL = "statement is null"

        private const val ERROR_RESULTSET_NULL = "ResultSet is null"
    }

    private var uniqueSession : AtomicLong = AtomicLong(1L)

    fun uniqueSession() :SessionSetting =
            SessionSetting(false,  TransactType.NO_ACTION, uniqueSession.incrementAndGet())

    @Throws(SessionException::class)
    fun selectValue(query :String, params :Array<Any?>? = null,
                    sessionSetting : SessionSetting = SessionSetting(true)) :Any? {

       val list = select(query, params, sessionSetting)

       return if(list.isEmpty() || list[0].isEmpty()) null else list[0][0]
    }

    @Throws(SessionException::class)
    fun select(query :String, params :Array<Any?>? = null,
                       sessionSetting : SessionSetting = SessionSetting(true) ) :List<Array<Any?>> {

        logger.info("select=" + query)

        val (session, statement, resultSet) = prepareSelect(query, params, sessionSetting)

        val tableData = try {
            fetchData(resultSet)
        }catch (e : SQLException) {
            logger.error("query=$query")
            params?.forEach { logger.error(it?.toString()) }
            logger.error("fetch", e)
            closeQueryData(session, TransactType.ROLLBACK, statement, resultSet)
            throw SessionException(e.message as String)
        }

        closeQueryData(session, sessionSetting.transactType, statement, resultSet)

        return tableData
    }

    @Throws(SessionException::class)
    fun selectCursor(query :String, params :Array<Any?>? = null,
                     sessionSetting : SessionSetting = SessionSetting(true)):List<Array<Any?>> {


        val session = dbConnection.getSession(sessionSetting)

        val request = prepareSelectCursor(session, query, params, sessionSetting)

        val tableData = try {
            fetchData(request.resultSetCursor!!)
        }catch (e : SQLException) {

            logger.error("query=$query")
            params?.forEach { logger.error(it?.toString()) }
            logger.error("fetch", e)
            closeQueryData(session, TransactType.ROLLBACK, request.statement, request.resultSetCursor)

            throw SessionException(e.message?:"")
        }

        closeQueryData(session, sessionSetting.transactType, request.statement, request.resultSetCursor)

        return tableData
    }

    @Throws(SessionException::class)
    fun select(query :String, params :Array<Any?>? = null,
               sessionSetting : SessionSetting = SessionSetting(true),
               callBack :(isNewRow :Boolean, value :Any?, column :String?)->Unit) {

        logger.info("select=" + query)

        val (session, statement, resultSet) = prepareSelect(query, params, sessionSetting)

        try {
            fetchData(resultSet, callBack)
        }catch (e : SQLException) {
            logger.error("fetch", e)
            closeQueryData(session, TransactType.ROLLBACK, statement, resultSet)
            throw SessionException(e.message as String)
        }

        closeQueryData(session, sessionSetting.transactType, statement, resultSet)
    }

    fun commitFree(sessionSetting : SessionSetting = SessionSetting(false)) {

        val session = dbConnection.getSession(sessionSetting)

        closeQueryData(session, TransactType.COMMIT)
    }

    fun rollbackFree(sessionSetting : SessionSetting = SessionSetting(false)) {

        val session = dbConnection.getSession(sessionSetting)

        closeQueryData(session, TransactType.ROLLBACK)
    }

    private fun prepareExecute(session :Session, query :String, params :Array<Any?>?,
                               outParamTypes :IntArray?) :QueryRequest {

        return try {
            if(outParamTypes?.size?:0 == 0)
                QueryRequest(query, params, session.session.prepareStatement(query)?.setParams(params))
            else
                QueryRequest(query, params, session.session.prepareCall(query)?.setParams(outParamTypes as IntArray, params))

        } catch (e : SQLException) {
            logger.error("QUERY=$query")
            params?.forEach { logger.error(it?.toString()) }
            logger.error("outParamTypes.size=${outParamTypes?.size}")
            outParamTypes?.forEach { logger.error(it.toString()) }

            logger.error("prepareCall", e)

            if(dbConnection.isRestartSessionException(session, false, e.message?:"")) {
                return prepareExecute(session, query, params, outParamTypes)
            }

            closeQueryData(session, TransactType.ROLLBACK)
            throw SessionException(e.message as String)
        }
    }

    private fun executePrepared(session :Session, queryRequest :QueryRequest, outParamTypes :IntArray?) :List<Any?>? {

        val result = if(outParamTypes?.size?:0 == 0) null else ArrayList<Any?>()

        val statement = queryRequest.statement

        try {
            statement?.execute()

            if(statement is CallableStatement) {
                for(index in outParamTypes!!.indices) {
                    result?.add(statement.getObject(index + 1))
                }
            }
        } catch (e : SQLException) {
            logger.error("query=${queryRequest.query}")
            logger.error("outParamTypes.size=${outParamTypes?.size}")
            outParamTypes?.forEach { logger.error(it.toString()) }

            logger.error("execute Call", e)

            if(dbConnection.isRestartSessionException(session, false, e.message?:"")) {

                try { statement?.close() } catch (e :SQLException) {}

                val newQueryRequest =
                        prepareExecute(session, queryRequest.query, queryRequest.params, outParamTypes)
                queryRequest.statement = newQueryRequest.statement

                return executePrepared(session, queryRequest, outParamTypes)
            }

            closeQueryData(session, TransactType.ROLLBACK, statement)
            throw SessionException(e.message as String)
        }

        return result
    }

    @Throws(SessionException::class)
    fun execute(query :String, params :Array<Any?>? = null,
                       sessionSetting : SessionSetting = SessionSetting(false),
                       outParamTypes :IntArray? = null) :List<Any?>? {

        logger.info("!!!!!!!!!!!!!!!!!$query")

        params?.forEach { logger.info(it.toString()) }

        val session = dbConnection.getSession(sessionSetting)

        val queryRequest = prepareExecute(session, query, params, outParamTypes)

        val resultList = executePrepared(session, queryRequest, outParamTypes)

        closeQueryData(session, sessionSetting.transactType, queryRequest.statement)

        return resultList
    }

    @Throws(SessionException::class)
    private fun prepareSelectCursor(session :Session, query :String, params :Array<Any?>?, sessionSetting : SessionSetting) :QueryRequest {

        val statement = try {
            session.session.prepareCall(query)
                    ?.setParams(intArrayOf(OracleTypes.CURSOR), params) as OracleCallableStatement

        } catch (e: SQLException) {

            logger.error("query=$query")
            params?.forEach { logger.error(it?.toString()) }

            logger.error("prepareSelectCursor", e)

            closeQueryData(session, TransactType.ROLLBACK)
            throw SessionException(e.message?:"")
        }

        val resultSet = try {

            statement.execute()

            statement.getCursor(1)

        } catch (e: SQLException) {
            logger.error("executeCursor", e)

            if(dbConnection.isRestartSessionException(session, sessionSetting.isReadTransact, e.message?:"")) {
                return prepareSelectCursor(session, query, params, sessionSetting)
            }
            closeQueryData(session, TransactType.ROLLBACK, statement)
            throw SessionException(e.message as String)
        }

        return QueryRequest(query, params, statement, resultSet)
    }

    @Throws(SessionException::class)
    private fun prepareSelect(query :String, params :Array<Any?>?, sessionSetting : SessionSetting)
            : Triple<Session, Statement, ResultSet>{
        val session = dbConnection.getSession(sessionSetting)

        val statement = try {
            session.session.prepareStatement(query)?.setParams(params)?: throw SessionException(ERROR_STATEMENT_NULL)
        } catch (e : SQLException) {
            logger.error("query=$query")
            params?.forEach { logger.error(it?.toString()) }
            logger.error("prepareStatement", e)

            if(dbConnection.isRestartSessionException(session, sessionSetting.isReadTransact, e.message?:"")) {
                return prepareSelect(query, params, sessionSetting)
            }

            closeQueryData(session, TransactType.ROLLBACK)
            throw SessionException(e.message?:"")
        }

        val resultSet = try {
            statement.executeQuery() ?: throw SessionException(ERROR_RESULTSET_NULL)
        } catch (e : SQLException) {
            logger.error("executeQuery", e)

            if(dbConnection.isRestartSessionException(session, sessionSetting.isReadTransact, e.message?:"")) {
                return prepareSelect(query, params, sessionSetting)
            }

            closeQueryData(session, TransactType.ROLLBACK, statement)
            throw SessionException(e.message as String)
        }

        return Triple(session, statement, resultSet)
    }

    @Throws(SessionException::class)
    private fun fetchData(resultSet : ResultSet, callBack :(isNewRow :Boolean, value :Any?, column :String?)->Unit) {

        val columns = Array(resultSet.metaData.columnCount, {""})

        for (index in 1 .. resultSet.metaData.columnCount) {
            columns[index - 1] = resultSet.metaData.getColumnName(index)?.toUpperCase()!!
        }

        while(resultSet.next()) {
            callBack(true, null, null)

            for (index in 1 .. columns.size) {
                callBack(false, resultSet.getObject(index), columns[index - 1])
            }
        }
    }

    @Throws(SessionException::class)
    private fun fetchData(resultSet : ResultSet) :List<Array<Any?>> {

        val data = ArrayList<Array<Any?>>()

        while(resultSet.next()) {

            val row = Array<Any?>(resultSet.metaData.columnCount, {null})

            for (index in 1 .. resultSet.metaData.columnCount) {
                row[index - 1] = resultSet.getObject(index)
            }
            data.add(row)
        }

        return data
    }

    private fun closeQueryData(session :Session, transactType :TransactType = TransactType.ROLLBACK, statement :Statement? = null, resultSet :ResultSet? = null) {

        //logger.error("closeQueryData ${session.session}")

        try {
            resultSet?.close()

            statement?.close()

            processCommit(session, transactType)

            session.isFree = true

            if(transactType == TransactType.ROLLBACK || transactType == TransactType.COMMIT) {
                //logger.error("FREE SESSION ${session.session}")
                session.idSession = null
            }
         } catch (e :SQLException) {
            logger.error("closeQueryData", e)
        }
    }

    @Throws(SQLException::class)
    private fun processCommit(session :Session, transactType :TransactType) {
        when (transactType) {
            TransactType.ROLLBACK -> {
                session.session.rollback()
                logger.error("session is ROLLBACK session.idSession=${session.session}")
            }
            TransactType.COMMIT -> {
                session.session.commit()
                //logger.error("session is COMMIT session.idSession=${session.session}")
            }
            else -> {
                //logger.error("session is NOTHING_ session.idSession=${session.session}")
            }
        }
    }
}

@Throws(SQLException::class)
fun PreparedStatement.setParams(inParams :Array<Any?>? = null, shiftOutParams :Int = 0) :PreparedStatement {

    if(inParams == null) {
        return this
    }

    for (index in 0 until inParams.size) {

        if (inParams[index] is Class<*>) {

            this.setNull(index + 1 + shiftOutParams, Type.getSqlTypeByClass(inParams[index] as Class<*>))
        } else {
            //LoggerFactory.getLogger(Query::class.java).error("PARAMS: index=$index  inParams=${inParams[index]}")

            this.setObject(index + 1 + shiftOutParams, inParams[index])
        }
    }
    return this
}

@Throws(SQLException::class)
fun CallableStatement.setParams(outParamTypes :IntArray, inParams :Array<Any?>? = null) :CallableStatement {

    for(index in outParamTypes.indices) {
        this.registerOutParameter(index + 1, outParamTypes[index])
    }
    return setParams(inParams, outParamTypes.size) as CallableStatement
}

private class QueryRequest(val query :String,
                        val params :Array<Any?>?,
                        var statement :PreparedStatement?,
                        var resultSetCursor :ResultSet? = null)





