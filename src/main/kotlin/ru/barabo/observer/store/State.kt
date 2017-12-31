package ru.barabo.observer.store


/**
 * Состояния элементов
 */
enum class State(val label :String) {
    NONE("новый"),
    OK("Ok"),
    ERROR("Ошибка"),
    PROCESS("в Обработке"),
    ARCHIVE("в Архиве")
}