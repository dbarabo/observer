package ru.barabo.db

data class DbSetting(val driver: String,
                     var url: String,
                     val user: String,
                     val password: String,
                     val selectCheck: String = "select 1 from dual",
                     val userAnother: String? = null,
                     val passwordAnother: String? = null
)