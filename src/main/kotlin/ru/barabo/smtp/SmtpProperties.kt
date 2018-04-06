package ru.barabo.smtp

data class SmtpProperties(val host: String,
                          val user: String,
                          val password: String,
                          val from: String,
                          var charsetSubject: String = "KOI8-R",
                          val isAuth: Boolean = true,
                          val charsetBody: String = "UTF-8") {
}