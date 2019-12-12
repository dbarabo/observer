package ru.barabo.observer.sftp

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import java.io.Closeable
import java.io.File

class Sftp private constructor(private val setting: SftpSetting) : Closeable {

    private val jsch = JSch()

    private lateinit var channel: ChannelSftp

    companion object {
        fun openSftp(setting: SftpSetting): Sftp = Sftp(setting).apply {  openSftp() }
    }

    fun lsFileOnly(folder: String): List<ChannelSftp.LsEntry> {

        val files = channel.ls(folder) // /OUT

        if(files.isEmpty() ) return emptyList()

        return files.filterIsInstance<ChannelSftp.LsEntry>().filter { it.attrs?.isDir == false }
    }

    fun downloadFiles(remoteDirectory: String, ftpFiles: List<ChannelSftp.LsEntry>, localDirectory: File) {

        if(ftpFiles.isEmpty()) return

        for(entry in ftpFiles) {
            channel.get("$remoteDirectory/${entry.filename}", "${localDirectory.absolutePath}/${entry.filename}")

            channel.rm("$remoteDirectory/${entry.filename}")
        }
    }

    @Throws(JSchException::class)
    private fun openSftp(): Sftp {
        close()

        setting.privateKeyPath?.let { jsch.addIdentity(it, setting.passphrase) }
        val session = jsch.getSession(setting.user, setting.host, setting.port)

        if(setting.strictHostKeyCheckingNo) {
            session.setConfig("StrictHostKeyChecking", "no")
        }
        session.connect()

        channel = session.openChannel("sftp") as ChannelSftp
        channel.connect()

        return this
    }

    override fun close() {
        if(::channel.isInitialized) {
            channel.closeChanel()
        }
    }
}

private fun Channel.closeChanel() {
    disconnect()
    session?.disconnect()
}