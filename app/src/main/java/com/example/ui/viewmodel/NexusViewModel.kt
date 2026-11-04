package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.NexusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NexusScreen {
    CHATS,
    MOMENTS,
    CIRCLES,
    VAULT,
    STORE,
    SETTINGS,
    ARCHIVED,
    LOCKED_CHATS
}

class NexusViewModel(application: Application) : AndroidViewModel(application) {
    val repository = NexusRepository(application)

    private val _currentScreen = MutableStateFlow(NexusScreen.CHATS)
    val currentScreen: StateFlow<NexusScreen> = _currentScreen.asStateFlow()

    private val _selectedThread = MutableStateFlow<ChatThread?>(null)
    val selectedThread: StateFlow<ChatThread?> = _selectedThread.asStateFlow()

    private val _selectedCircle = MutableStateFlow<NexusCircle?>(null)
    val selectedCircle: StateFlow<NexusCircle?> = _selectedCircle.asStateFlow()

    private val _selectedStory = MutableStateFlow<NexusStory?>(null)
    val selectedStory: StateFlow<NexusStory?> = _selectedStory.asStateFlow()

    // Dialog & sheet states
    private val _checkoutProduct = MutableStateFlow<PremiumProduct?>(null)
    val checkoutProduct: StateFlow<PremiumProduct?> = _checkoutProduct.asStateFlow()

    private val _creatorTipTarget = MutableStateFlow<String?>(null)
    val creatorTipTarget: StateFlow<String?> = _creatorTipTarget.asStateFlow()

    private val _recentReceipt = MutableStateFlow<CashTransaction?>(null)
    val recentReceipt: StateFlow<CashTransaction?> = _recentReceipt.asStateFlow()

    private val _showCreatePostDialog = MutableStateFlow(false)
    val showCreatePostDialog: StateFlow<Boolean> = _showCreatePostDialog.asStateFlow()

    private val _showKeyFingerprintSheet = MutableStateFlow(false)
    val showKeyFingerprintSheet: StateFlow<Boolean> = _showKeyFingerprintSheet.asStateFlow()

    // Pass-through flows from repository
    val threads = repository.threads
    val currentMessages = repository.currentMessages
    val posts = repository.posts
    val stories = repository.stories
    val circles = repository.circles
    val circlePosts = repository.circlePosts
    val peerNodes = repository.peerNodes
    val userState = repository.userState
    val transactions = repository.transactions
    val premiumProducts = repository.premiumProducts
    val selectedCurrency = repository.selectedCurrency
    val vaultItems = repository.vaultItems

    private val _showAddSecretDialog = MutableStateFlow(false)
    val showAddSecretDialog: StateFlow<Boolean> = _showAddSecretDialog.asStateFlow()

    fun setAddSecretDialog(show: Boolean) {
        _showAddSecretDialog.value = show
    }

    fun setCurrency(currency: String) {
        repository.setCurrency(currency)
    }

    fun navigateTo(screen: NexusScreen) {
        _currentScreen.value = screen
        _selectedThread.value = null
        _selectedCircle.value = null
    }

    fun openThread(thread: ChatThread) {
        _selectedThread.value = thread
        repository.loadThreadMessages(thread.id)
    }

    fun closeThread() {
        _selectedThread.value = null
    }

    fun openCircle(circle: NexusCircle) {
        _selectedCircle.value = circle
    }

    fun closeCircle() {
        _selectedCircle.value = null
    }

    fun openStory(story: NexusStory) {
        _selectedStory.value = story
    }

    fun closeStory() {
        _selectedStory.value = null
    }

    fun openCheckout(product: PremiumProduct) {
        _checkoutProduct.value = product
    }

    fun closeCheckout() {
        _checkoutProduct.value = null
    }

    fun openCreatorTip(handle: String) {
        _creatorTipTarget.value = handle
    }

    fun closeCreatorTip() {
        _creatorTipTarget.value = null
    }

    fun closeReceipt() {
        _recentReceipt.value = null
    }

    fun showReceipt(transaction: CashTransaction) {
        _recentReceipt.value = transaction
    }

    fun setCreatePostDialog(show: Boolean) {
        _showCreatePostDialog.value = show
    }

    fun setKeyFingerprintSheet(show: Boolean) {
        _showKeyFingerprintSheet.value = show
    }

    fun sendMessage(text: String, ephemeralSeconds: Int = 0) {
        val threadId = _selectedThread.value?.id ?: return
        if (text.isBlank()) return
        repository.sendMessage(threadId, text, isAudio = false, ephemeralSeconds = ephemeralSeconds)
    }

    fun sendVoiceMemo(durationSeconds: Int = 6) {
        val threadId = _selectedThread.value?.id ?: return
        repository.sendMessage(threadId, "Encrypted voice memo", isAudio = true, audioDuration = durationSeconds)
    }

    fun burnMessage(messageId: String) {
        repository.deleteMessage(messageId)
    }

    fun toggleLikePost(postId: String) {
        repository.toggleLikePost(postId)
    }

    fun createPost(content: String, gradient: String) {
        if (content.isBlank()) return
        repository.createPost(content, gradient)
        _showCreatePostDialog.value = false
    }

    fun toggleUpvoteCirclePost(circleId: String, postId: String) {
        repository.toggleUpvoteCirclePost(circleId, postId)
    }

    fun addCirclePost(circleId: String, content: String) {
        if (content.isBlank()) return
        repository.addCirclePost(circleId, content)
    }

    fun activateAllFeatures() {
        repository.activateAllFeatures()
    }

    fun addCashBalance(amount: Double, paymentMethod: String = "Google Pay UPI (satoshi@okaxis)") {
        val receipt = repository.addCashBalance(amount, paymentMethod)
        _recentReceipt.value = receipt
    }

    fun sendCashUpi(upiId: String, amount: Double): Pair<Boolean, String> {
        return repository.sendCashUpi(upiId, amount)
    }

    fun toggleTorRelay() {
        repository.toggleTorRelay()
    }

    fun toggleGhostMode() {
        repository.toggleGhostMode()
    }

    fun executeRealCashPurchase(product: PremiumProduct, paymentMethod: String, currencyCode: String = selectedCurrency.value) {
        val receipt = repository.executeRealCashPurchase(product, paymentMethod, currencyCode)
        _checkoutProduct.value = null
        _recentReceipt.value = receipt
    }

    fun sendCreatorCashTip(handle: String, amount: Double, currencyCode: String = selectedCurrency.value, paymentMethod: String) {
        val receipt = repository.sendCreatorCashTip(handle, amount, currencyCode, paymentMethod)
        _creatorTipTarget.value = null
        _recentReceipt.value = receipt
    }

    fun addVaultSecret(title: String, category: String, identifier: String, plainSecret: String, notes: String = "") {
        if (title.isBlank() || plainSecret.isBlank()) return
        repository.addVaultSecret(title, category, identifier, plainSecret, notes)
        _showAddSecretDialog.value = false
    }

    fun deleteVaultSecret(id: String) {
        repository.deleteVaultSecret(id)
    }

    fun openSettings() {
        _currentScreen.value = NexusScreen.SETTINGS
    }

    fun openArchived() {
        _currentScreen.value = NexusScreen.ARCHIVED
    }

    fun openLockedChats() {
        _currentScreen.value = NexusScreen.LOCKED_CHATS
    }

    fun archiveThread(threadId: String) {
        repository.archiveThread(threadId)
    }

    fun unarchiveThread(threadId: String) {
        repository.unarchiveThread(threadId)
    }

    fun lockThread(threadId: String) {
        repository.lockThread(threadId)
    }

    fun unlockThread(threadId: String) {
        repository.unlockThread(threadId)
    }

    fun toggleArchiveThread(threadId: String) {
        repository.toggleArchiveThread(threadId)
    }

    fun toggleLockThread(threadId: String) {
        repository.toggleLockThread(threadId)
    }
}
