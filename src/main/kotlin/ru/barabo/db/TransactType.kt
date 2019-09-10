package ru.barabo.db

enum class TransactType {
    COMMIT,
    ROLLBACK,
    NO_ACTION,
    SET_SAVEPOINT_BEFORE,
    ROLLBACK_SAVEPOINT,
    COMMIT_MONOPOLY,
    MICRO_ROLLBACK
}