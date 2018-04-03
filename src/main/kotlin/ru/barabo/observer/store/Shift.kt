package ru.barabo.observer.store

import org.jasypt.util.text.BasicTextEncryptor

object Shift {

    private val textEncryptor = BasicTextEncryptor()

    init {
        textEncryptor.setPassword(TaskMapper.masterPswd)
    }

    fun encrypt(text: String) = textEncryptor.encrypt(text)

    fun decrypt(encryptText: String) = textEncryptor.decrypt(encryptText)
}