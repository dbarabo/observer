package ru.barabo.observer.store


interface StoreListener<G> {

    fun refreshAll(rootElem :G)
}