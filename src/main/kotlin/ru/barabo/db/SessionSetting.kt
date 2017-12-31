package ru.barabo.db

data class SessionSetting(val isReadTransact :Boolean,
                          val transactType :TransactType = TransactType.COMMIT,
                          val idSession :Long? = null)
