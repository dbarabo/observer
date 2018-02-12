package ru.barabo.cmd

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Shift {

    private val secureRandom = SecureRandom()

  //  private val DEF_IV = "rf;lsq".toByteArray().


    /**
     * Generates an IV. The IV is always 128 bit long.
     */
    fun generateIv(): ByteArray {
        val result = ByteArray(128 / 8)
        secureRandom.nextBytes(result)
        return result
    }

    /**
     * Generates a key with [sizeInBits] bits.
     */
    fun generateKey(sizeInBits: Int): ByteArray {
        val result = ByteArray(sizeInBits / 8)
        secureRandom.nextBytes(result)
        return result
    }

    /**
     * Encrypts the given [plaintext] with the given [key] under AES CBC with PKCS5 padding.
     *
     * This method generates a random IV.
     *
     * @return Ciphertext and IV
     */
    fun encryptCbc(plaintext: ByteArray, key: ByteArray): Ciphertext {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key, "AES")

        val iv = generateIv()
        val ivSpec = IvParameterSpec(iv)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val ciphertext = cipher.doFinal(plaintext)

        return Ciphertext(ciphertext, iv)
    }

    /**
     * Decrypts the given [ciphertext] using the given [key] under AES CBC with PKCS5 padding.
     *
     * @return Plaintext
     */
    fun decryptCbc(ciphertext: Ciphertext, key: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(ciphertext.iv)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

        val plaintext = cipher.doFinal(ciphertext.ciphertext)
        return plaintext
    }

}

class Ciphertext(val ciphertext: ByteArray, val iv: ByteArray)