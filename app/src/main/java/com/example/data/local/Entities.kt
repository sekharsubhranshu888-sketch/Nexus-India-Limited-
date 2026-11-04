package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class StoredChatMessage(
    @PrimaryKey val id: String,
    val threadId: String,
    val senderName: String,
    val senderHandle: String,
    val text: String,
    val timestamp: Long,
    val isMine: Boolean,
    val isEncrypted: Boolean,
    val ratchetKeyFingerprint: String,
    val ephemeralTimerSeconds: Int,
    val isAudio: Boolean,
    val audioDurationSeconds: Int
)

@Entity(tableName = "nexus_posts")
data class StoredNexusPost(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorHandle: String,
    val content: String,
    val timestamp: Long,
    val mediaGradient: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean,
    val antiAiWatermark: Boolean,
    val exifPurged: Boolean
)

@Entity(tableName = "cash_transactions")
data class StoredCashTransaction(
    @PrimaryKey val id: String,
    val title: String,
    val amount: Double,
    val currencyCode: String = "INR",
    val currencySymbol: String = "₹",
    val timestamp: Long,
    val status: String,
    val receiptId: String,
    val paymentMethod: String
)

@Entity(tableName = "vault_secrets")
data class StoredVaultSecret(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "CREDENTIAL", "NOTE", "KEY"
    val identifier: String, // username/email/key
    val cipherText: String, // Encrypted Base64 payload
    val iv: String,
    val notes: String,
    val timestamp: Long
)
