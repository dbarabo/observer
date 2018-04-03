package ru.barabo.smtp

import ru.barabo.observer.store.Shift

class MailProperties(val protocol: String, val host: String, val port: Int,
                     val hostSmtp: String,
                     userEncoding: String, passwordEncoding: String) {

    val user: String = userEncoding
    get() = Shift.decrypt(field)

    val password: String = passwordEncoding
    get() = Shift.decrypt(field)
}