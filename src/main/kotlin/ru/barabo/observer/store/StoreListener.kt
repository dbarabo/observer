package ru.barabo.observer.store


interface StoreListener {

    fun refreshAll(rootElem :GroupElem)
}