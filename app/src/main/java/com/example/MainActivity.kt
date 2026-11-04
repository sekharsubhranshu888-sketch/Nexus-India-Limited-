package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NexusScreen
import com.example.ui.viewmodel.NexusViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NexusSuperApp()
            }
        }
    }
}

data class NavItem(
    val screen: NexusScreen,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun NexusSuperApp(viewModel: NexusViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedThread by viewModel.selectedThread.collectAsStateWithLifecycle()
    val selectedCircle by viewModel.selectedCircle.collectAsStateWithLifecycle()
    val selectedStory by viewModel.selectedStory.collectAsStateWithLifecycle()
    val checkoutProduct by viewModel.checkoutProduct.collectAsStateWithLifecycle()
    val creatorTipTarget by viewModel.creatorTipTarget.collectAsStateWithLifecycle()
    val recentReceipt by viewModel.recentReceipt.collectAsStateWithLifecycle()
    val showCreatePostDialog by viewModel.showCreatePostDialog.collectAsStateWithLifecycle()
    val showFingerprintSheet by viewModel.showKeyFingerprintSheet.collectAsStateWithLifecycle()
    val showAddSecretDialog by viewModel.showAddSecretDialog.collectAsStateWithLifecycle()

    val threads by viewModel.threads.collectAsStateWithLifecycle()
    val currentMessages by viewModel.currentMessages.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val circles by viewModel.circles.collectAsStateWithLifecycle()
    val circlePosts by viewModel.circlePosts.collectAsStateWithLifecycle()
    val peerNodes by viewModel.peerNodes.collectAsStateWithLifecycle()
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val selectedCurrency by viewModel.selectedCurrency.collectAsStateWithLifecycle()
    val vaultItems by viewModel.vaultItems.collectAsStateWithLifecycle()
    val products = viewModel.premiumProducts

    val totalUnreadChats = remember(threads) { threads.sumOf { it.unreadCount } }

    val navItems = listOf(
        NavItem(NexusScreen.CHATS, "Chats", Icons.Default.Chat, Icons.Outlined.Chat, "nav_chat"),
        NavItem(NexusScreen.MOMENTS, "Updates", Icons.Default.DonutLarge, Icons.Outlined.DonutLarge, "nav_moments"),
        NavItem(NexusScreen.CIRCLES, "Communities", Icons.Default.Groups, Icons.Outlined.Groups, "nav_circles"),
        NavItem(NexusScreen.VAULT, "Vault", Icons.Default.Lock, Icons.Outlined.Lock, "nav_vault"),
        NavItem(NexusScreen.STORE, "Store", Icons.Default.Storefront, Icons.Outlined.Storefront, "nav_store")
    )

    // Handle Hardware Back Button
    BackHandler(enabled = selectedThread != null || selectedCircle != null || currentScreen == NexusScreen.SETTINGS || currentScreen == NexusScreen.ARCHIVED || currentScreen == NexusScreen.LOCKED_CHATS) {
        if (selectedThread != null) {
            viewModel.closeThread()
        } else if (selectedCircle != null) {
            viewModel.closeCircle()
        } else if (currentScreen == NexusScreen.SETTINGS || currentScreen == NexusScreen.ARCHIVED || currentScreen == NexusScreen.LOCKED_CHATS) {
            viewModel.navigateTo(NexusScreen.CHATS)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsAppBackground),
        containerColor = WhatsAppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // Show bottom bar only on primary tabs, not in chat thread or sub-screens
            if (selectedThread == null && currentScreen != NexusScreen.SETTINGS && currentScreen != NexusScreen.ARCHIVED && currentScreen != NexusScreen.LOCKED_CHATS) {
                NavigationBar(
                    containerColor = WhatsAppSurface,
                    contentColor = WhatsAppTextPrimary,
                    tonalElevation = 6.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("nexus_bottom_nav")
                ) {
                    navItems.forEach { item ->
                        val selected = currentScreen == item.screen
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.navigateTo(item.screen) },
                            icon = {
                                if (item.screen == NexusScreen.CHATS && totalUnreadChats > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = WhatsAppUnreadBadge,
                                                contentColor = WhatsAppBackground
                                            ) {
                                                Text(
                                                    text = totalUnreadChats.toString(),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = WhatsAppGreenBright,
                                indicatorColor = WhatsAppGreen.copy(alpha = 0.22f),
                                unselectedIconColor = WhatsAppTextSecondary,
                                selectedTextColor = WhatsAppGreenBright,
                                unselectedTextColor = WhatsAppTextSecondary
                            ),
                            modifier = Modifier.testTag(item.tag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main content based on navigation state
            if (selectedThread != null) {
                ChatDetailScreen(
                    thread = selectedThread!!,
                    messages = currentMessages,
                    userState = userState,
                    onBack = { viewModel.closeThread() },
                    onSendMessage = { text, ephemeral -> viewModel.sendMessage(text, ephemeral) },
                    onSendVoiceMemo = { duration -> viewModel.sendVoiceMemo(duration) },
                    onBurnMessage = { msgId -> viewModel.burnMessage(msgId) },
                    onShowFingerprints = { viewModel.setKeyFingerprintSheet(true) },
                    onToggleLockChat = { selectedThread?.let { viewModel.toggleLockThread(it.id) } },
                    onToggleArchiveChat = { selectedThread?.let { viewModel.toggleArchiveThread(it.id) } }
                )
            } else {
                when (currentScreen) {
                    NexusScreen.CHATS -> {
                        ChatListScreen(
                            threads = threads,
                            userState = userState,
                            onOpenThread = { thread -> viewModel.openThread(thread) },
                            onOpenVault = { viewModel.navigateTo(NexusScreen.VAULT) },
                            onShowFingerprintSheet = { viewModel.setKeyFingerprintSheet(true) },
                            onOpenSettings = { viewModel.openSettings() },
                            onOpenArchived = { viewModel.openArchived() },
                            onOpenLockedChats = { viewModel.openLockedChats() }
                        )
                    }
                    NexusScreen.MOMENTS -> {
                        MomentsScreen(
                            posts = posts,
                            stories = stories,
                            userState = userState,
                            onOpenStory = { story -> viewModel.openStory(story) },
                            onLikePost = { postId -> viewModel.toggleLikePost(postId) },
                            onTipCreator = { handle -> viewModel.openCreatorTip(handle) },
                            onOpenCreatePost = { viewModel.setCreatePostDialog(true) },
                            onOpenVault = { viewModel.navigateTo(NexusScreen.VAULT) }
                        )
                    }
                    NexusScreen.CIRCLES -> {
                        CirclesScreen(
                            circles = circles,
                            selectedCircle = selectedCircle,
                            circlePosts = circlePosts,
                            userState = userState,
                            onSelectCircle = { circle -> viewModel.openCircle(circle) },
                            onCloseCircle = { viewModel.closeCircle() },
                            onUpvotePost = { circleId, postId -> viewModel.toggleUpvoteCirclePost(circleId, postId) },
                            onAddPost = { circleId, content -> viewModel.addCirclePost(circleId, content) },
                            onOpenVault = { viewModel.navigateTo(NexusScreen.VAULT) }
                        )
                    }
                    NexusScreen.VAULT -> {
                        VaultScreen(
                            userState = userState,
                            peerNodes = peerNodes,
                            vaultItems = vaultItems,
                            onToggleTor = { viewModel.toggleTorRelay() },
                            onToggleGhostMode = { viewModel.toggleGhostMode() },
                            onUpgradePro = { viewModel.navigateTo(NexusScreen.STORE) },
                            onShowFingerprintSheet = { viewModel.setKeyFingerprintSheet(true) },
                            onAddNewSecret = { viewModel.setAddSecretDialog(true) },
                            onDeleteSecret = { secretId -> viewModel.deleteVaultSecret(secretId) }
                        )
                    }
                    NexusScreen.STORE -> {
                        PremiumStoreScreen(
                            userState = userState,
                            products = products,
                            transactions = transactions,
                            selectedCurrency = selectedCurrency,
                            onCurrencyChange = { curr -> viewModel.setCurrency(curr) },
                            onSelectProduct = { prod -> viewModel.openCheckout(prod) },
                            onViewReceipt = { tx -> viewModel.showReceipt(tx) },
                            onTipCreator = { handle -> viewModel.openCreatorTip(handle) },
                            onOpenVault = { viewModel.navigateTo(NexusScreen.VAULT) }
                        )
                    }
                    NexusScreen.SETTINGS -> {
                        SettingsScreen(
                            userState = userState,
                            onBack = { viewModel.navigateTo(NexusScreen.CHATS) },
                            onOpenVault = { viewModel.navigateTo(NexusScreen.VAULT) },
                            onOpenLockedChats = { viewModel.openLockedChats() },
                            onToggleTor = { viewModel.toggleTorRelay() },
                            onToggleGhostMode = { viewModel.toggleGhostMode() },
                            onActivateAllFeatures = { viewModel.activateAllFeatures() },
                            onAddCashBalance = { amount -> viewModel.addCashBalance(amount) },
                            onSendCashUpi = { upiId, amount -> viewModel.sendCashUpi(upiId, amount) },
                            onOpenStore = { viewModel.navigateTo(NexusScreen.STORE) }
                        )
                    }
                    NexusScreen.ARCHIVED -> {
                        ArchivedChatsScreen(
                            archivedThreads = threads.filter { it.isArchived },
                            onBack = { viewModel.navigateTo(NexusScreen.CHATS) },
                            onOpenThread = { thread -> viewModel.openThread(thread) },
                            onUnarchiveThread = { threadId -> viewModel.unarchiveThread(threadId) }
                        )
                    }
                    NexusScreen.LOCKED_CHATS -> {
                        LockedChatsScreen(
                            lockedThreads = threads.filter { it.isLocked },
                            allThreads = threads,
                            onBack = { viewModel.navigateTo(NexusScreen.CHATS) },
                            onOpenThread = { thread -> viewModel.openThread(thread) },
                            onUnlockThread = { threadId -> viewModel.unlockThread(threadId) },
                            onLockThread = { threadId -> viewModel.lockThread(threadId) }
                        )
                    }
                }
            }

            // Dialogs & Sheets
            if (showFingerprintSheet) {
                FingerprintSheet(
                    userState = userState,
                    onDismiss = { viewModel.setKeyFingerprintSheet(false) }
                )
            }

            if (selectedStory != null) {
                StoryViewerDialog(
                    story = selectedStory!!,
                    onDismiss = { viewModel.closeStory() }
                )
            }

            if (showCreatePostDialog) {
                CreatePostDialog(
                    onDismiss = { viewModel.setCreatePostDialog(false) },
                    onSubmit = { content, gradient ->
                        viewModel.createPost(content, gradient)
                    }
                )
            }

            if (checkoutProduct != null) {
                CashCheckoutSheet(
                    product = checkoutProduct!!,
                    currencyCode = selectedCurrency,
                    onDismiss = { viewModel.closeCheckout() },
                    onConfirmPurchase = { product, method, currency ->
                        viewModel.executeRealCashPurchase(product, method, currency)
                    }
                )
            }

            if (creatorTipTarget != null) {
                CreatorTipSheet(
                    targetHandle = creatorTipTarget!!,
                    currencyCode = selectedCurrency,
                    onDismiss = { viewModel.closeCreatorTip() },
                    onSendTip = { handle, amount, currency, method ->
                        viewModel.sendCreatorCashTip(handle, amount, currency, method)
                    }
                )
            }

            if (recentReceipt != null) {
                ReceiptDialog(
                    transaction = recentReceipt!!,
                    onDismiss = { viewModel.closeReceipt() }
                )
            }

            if (showAddSecretDialog) {
                AddSecretDialog(
                    onDismiss = { viewModel.setAddSecretDialog(false) },
                    onSave = { title, category, identifier, secret, notes ->
                        viewModel.addVaultSecret(title, category, identifier, secret, notes)
                    }
                )
            }
        }
    }
}
