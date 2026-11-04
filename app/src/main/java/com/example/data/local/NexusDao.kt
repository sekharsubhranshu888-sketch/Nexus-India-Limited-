package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NexusDao {
    @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY timestamp ASC")
    fun getMessagesForThread(threadId: String): Flow<List<StoredChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: StoredChatMessage)

    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String)

    @Query("SELECT * FROM nexus_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<StoredNexusPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: StoredNexusPost)

    @Query("UPDATE nexus_posts SET likesCount = :likesCount, isLiked = :isLiked WHERE id = :postId")
    suspend fun updatePostLike(postId: String, likesCount: Int, isLiked: Boolean)

    @Query("SELECT * FROM cash_transactions ORDER BY timestamp DESC")
    fun getCashTransactions(): Flow<List<StoredCashTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashTransaction(transaction: StoredCashTransaction)

    @Query("SELECT * FROM vault_secrets ORDER BY timestamp DESC")
    fun getAllVaultSecrets(): Flow<List<StoredVaultSecret>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaultSecret(secret: StoredVaultSecret)

    @Query("DELETE FROM vault_secrets WHERE id = :id")
    suspend fun deleteVaultSecret(id: String)
}
