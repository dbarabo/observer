package ru.barabo.observer.store

import ru.barabo.db.SessionException
import ru.barabo.db.TemplateQuery

abstract class StoreDb<in T :Any, G>(
        val template : TemplateQuery,
        private val storeListenerList :MutableList<StoreListener<G>> = ArrayList()) : Store<T>{

    @Throws(SessionException::class)
    override fun save(item :T) {

        template.save(item)
    }

    @Throws(SessionException::class)
    override fun delete(item :T) {

        template.deleteById(item)
    }

    fun addStoreListener(storeListener :StoreListener<G>) {
        storeListenerList.add(storeListener)

        storeListener.refreshAll(getRootElem())
    }

    protected abstract fun getRootElem() :G

    protected fun sentInfoRefreshAll() {
        storeListenerList.forEach {it.refreshAll(getRootElem() )}
    }
 }