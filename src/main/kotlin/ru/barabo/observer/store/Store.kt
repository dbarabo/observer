package ru.barabo.observer.store

import ru.barabo.db.SessionException

interface Store<in T :Any> {
    @Throws(SessionException::class)
    fun save(item :T)

    @Throws(SessionException::class)
    fun delete(item :T)


}