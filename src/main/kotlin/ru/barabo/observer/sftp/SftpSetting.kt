package ru.barabo.observer.sftp

data class SftpSetting(val user: String,
                       val host: String,
                       val port: Int = 22,
                       val privateKeyPath: String? = null,
                       val passphrase: String? = null,
                       val strictHostKeyCheckingNo: Boolean = true) {
}