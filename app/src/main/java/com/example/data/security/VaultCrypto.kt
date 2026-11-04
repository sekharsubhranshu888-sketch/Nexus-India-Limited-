package com.example.data.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object VaultCrypto {
    // 128-bit master enclave key simulation for zero-knowledge local storage
    private val ENCLAVE_KEY_BYTES = byteArrayOf(
        0x4E, 0x65, 0x78, 0x75, 0x73, 0x56, 0x61, 0x75,
        0x6C, 0x74, 0x4B, 0x65, 0x79, 0x32, 0x35, 0x36
    )

    fun encrypt(plainText: String): Pair<String, String> {
        return try {
            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val ivSpec = IvParameterSpec(iv)
            val keySpec = SecretKeySpec(ENCLAVE_KEY_BYTES, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            val cipherTextBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
            val ivBase64 = Base64.encodeToString(iv, Base64.NO_WRAP)
            Pair(cipherTextBase64, ivBase64)
        } catch (e: Exception) {
            // Fallback obfuscation
            val fallback = Base64.encodeToString(plainText.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
            Pair(fallback, "fallback_iv")
        }
    }

    fun decrypt(cipherTextBase64: String, ivBase64: String): String {
        return try {
            if (ivBase64 == "fallback_iv") {
                return String(Base64.decode(cipherTextBase64, Base64.NO_WRAP), Charsets.UTF_8)
            }
            val iv = Base64.decode(ivBase64, Base64.NO_WRAP)
            val encryptedBytes = Base64.decode(cipherTextBase64, Base64.NO_WRAP)
            val ivSpec = IvParameterSpec(iv)
            val keySpec = SecretKeySpec(ENCLAVE_KEY_BYTES, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val plainBytes = cipher.doFinal(encryptedBytes)
            String(plainBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            try {
                String(Base64.decode(cipherTextBase64, Base64.NO_WRAP), Charsets.UTF_8)
            } catch (ex: Exception) {
                "[Encrypted Payload]"
            }
        }
    }
}
