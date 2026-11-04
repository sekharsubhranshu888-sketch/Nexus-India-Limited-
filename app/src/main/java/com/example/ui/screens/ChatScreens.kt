package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.ChatThread
import com.example.data.model.UserSovereignState
import com.example.ui.components.AudioWaveformView
import com.example.ui.components.NexusAvatar
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatListScreen(
    threads: List<ChatThread>,
    userState: UserSovereignState,
    onOpenThread: (ChatThread) -> Unit,
    onOpenVault: () -> Unit,
    onShowFingerprintSheet: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenArchived: () -> Unit,
    onOpenLockedChats: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Unread", "Favorites", "Groups"
    var showMoreMenu by remember { mutableStateOf(false) }

    val activeThreads = remember(threads) {
        threads.filter { !it.isArchived && !it.isLocked }
    }
    val archivedCount = remember(threads) {
        threads.count { it.isArchived }
    }
    val lockedCount = remember(threads) {
        threads.count { it.isLocked }
    }

    val filteredThreads = remember(activeThreads, searchQuery, selectedFilter) {
        activeThreads.filter { thread ->
            val matchQuery = searchQuery.isBlank() ||
                    thread.contactName.contains(searchQuery, ignoreCase = true) ||
                    thread.contactHandle.contains(searchQuery, ignoreCase = true) ||
                    thread.lastMessage.contains(searchQuery, ignoreCase = true)
            val matchFilter = when (selectedFilter) {
                "Unread" -> thread.unreadCount > 0
                "Groups" -> thread.contactHandle.contains("circle", ignoreCase = true) ||
                        thread.contactHandle.contains("group", ignoreCase = true) ||
                        thread.contactName.contains("Group", ignoreCase = true) ||
                        thread.contactName.contains("Mesh", ignoreCase = true)
                "Favorites" -> thread.ratchetVerified
                else -> true
            }
            matchQuery && matchFilter
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsAppBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // WhatsApp-style Top Bar
            Surface(
                color = WhatsAppSurface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Title
                        Text(
                            text = "Nexus",
                            color = WhatsAppGreen,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.testTag("whatsapp_app_title")
                        )

                        // Action Icons
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Opening Zero-Tracking Camera", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Camera",
                                    tint = WhatsAppTextPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            IconButton(
                                onClick = { isSearchActive = !isSearchActive },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = WhatsAppTextPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Box {
                                IconButton(
                                    onClick = { showMoreMenu = true },
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More",
                                        tint = WhatsAppTextPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showMoreMenu,
                                    onDismissRequest = { showMoreMenu = false },
                                    modifier = Modifier.background(WhatsAppCard)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Locked chats", color = WhatsAppTextPrimary) },
                                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = WhatsAppGreen) },
                                        onClick = {
                                            showMoreMenu = false
                                            onOpenLockedChats()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Archived chats", color = WhatsAppTextPrimary) },
                                        leadingIcon = { Icon(Icons.Default.Archive, contentDescription = null, tint = WhatsAppGreen) },
                                        onClick = {
                                            showMoreMenu = false
                                            onOpenArchived()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Zero-Knowledge Vault", color = WhatsAppTextPrimary) },
                                        leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, tint = WhatsAppGreen) },
                                        onClick = {
                                            showMoreMenu = false
                                            onOpenVault()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Safety Numbers & Keys", color = WhatsAppTextPrimary) },
                                        leadingIcon = { Icon(Icons.Default.Fingerprint, contentDescription = null, tint = WhatsAppGreen) },
                                        onClick = {
                                            showMoreMenu = false
                                            onShowFingerprintSheet()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Settings", color = WhatsAppTextPrimary) },
                                        leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null, tint = WhatsAppGreen) },
                                        onClick = {
                                            showMoreMenu = false
                                            onOpenSettings()
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Search Bar (WhatsApp capsule design)
                    if (isSearchActive || searchQuery.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search chats...", color = WhatsAppTextSecondary, fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = WhatsAppTextSecondary) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = WhatsAppTextSecondary)
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = WhatsAppSearchBg,
                                unfocusedContainerColor = WhatsAppSearchBg,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = WhatsAppTextPrimary,
                                unfocusedTextColor = WhatsAppTextPrimary
                            ),
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .padding(horizontal = 16.dp)
                                .testTag("chat_search_bar"),
                            singleLine = true
                        )
                    }

                    // WhatsApp-style Filter Chips
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("All", "Unread", "Favorites", "Groups").forEach { chip ->
                            val isSelected = selectedFilter == chip
                            Surface(
                                color = if (isSelected) WhatsAppGreen.copy(alpha = 0.2f) else WhatsAppSurface,
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) WhatsAppGreen else WhatsAppDivider
                                ),
                                modifier = Modifier
                                    .clickable { selectedFilter = chip }
                                    .testTag("filter_chip_$chip")
                            ) {
                                Text(
                                    text = chip,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) WhatsAppGreenBright else WhatsAppTextSecondary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // WhatsApp Locked Chats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenLockedChats() }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("chat_locked_row"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked chats",
                    tint = WhatsAppGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Locked chats",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = WhatsAppTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                if (lockedCount > 0) {
                    Text(
                        text = lockedCount.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhatsAppGreenBright
                    )
                }
            }
            HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

            // WhatsApp Archived Chats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenArchived() }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("chat_archived_row"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Archive,
                    contentDescription = "Archived",
                    tint = WhatsAppTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Archived",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = WhatsAppTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                if (archivedCount > 0) {
                    Text(
                        text = archivedCount.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhatsAppGreenBright
                    )
                }
            }
            HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

            // Chat list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(filteredThreads, key = { it.id }) { thread ->
                    WhatsAppChatThreadItem(
                        thread = thread,
                        onClick = { onOpenThread(thread) }
                    )
                }

                item {
                    // WhatsApp-style End-to-End Encryption Footer
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = WhatsAppTextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Your personal chats are end-to-end encrypted",
                            fontSize = 11.sp,
                            color = WhatsAppTextSecondary
                        )
                    }
                }
            }
        }

        // WhatsApp Signature Floating Action Button (bottom right)
        FloatingActionButton(
            onClick = {
                Toast.makeText(context, "Start New Encrypted Chat", Toast.LENGTH_SHORT).show()
            },
            containerColor = WhatsAppGreen,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 80.dp)
                .size(56.dp)
                .testTag("whatsapp_new_chat_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "New Chat",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun WhatsAppChatThreadItem(
    thread: ChatThread,
    onClick: () -> Unit
) {
    val timeFormatted = remember(thread.lastTimestamp) {
        val diffMins = (System.currentTimeMillis() - thread.lastTimestamp) / (1000 * 60)
        when {
            diffMins < 1 -> "Just now"
            diffMins < 60 -> "${diffMins}m"
            diffMins < 1440 -> "${diffMins / 60}h"
            else -> "Yesterday"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("chat_thread_${thread.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NexusAvatar(
            seed = thread.avatarSeed,
            size = 52,
            showOnline = thread.isOnline
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Top row: Contact Name + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = thread.contactName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WhatsAppTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = timeFormatted,
                    fontSize = 12.sp,
                    color = if (thread.unreadCount > 0) WhatsAppGreenBright else WhatsAppTextSecondary,
                    fontWeight = if (thread.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Bottom row: Read receipt status + snippet + unread badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Double blue checkmark (WhatsApp style)
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Delivered",
                        tint = WhatsAppCheckmarkBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    if (thread.ephemeralSettingSeconds > 0) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Disappearing",
                            tint = WhatsAppTextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = thread.lastMessage,
                        fontSize = 14.sp,
                        color = WhatsAppTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (thread.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(WhatsAppUnreadBadge),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = thread.unreadCount.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WhatsAppBackground
                        )
                    }
                }
            }
        }
    }
    HorizontalDivider(
        color = WhatsAppDivider,
        thickness = 0.5.dp,
        modifier = Modifier.padding(start = 82.dp)
    )
}

@Composable
fun ChatDetailScreen(
    thread: ChatThread,
    messages: List<ChatMessage>,
    userState: UserSovereignState,
    onBack: () -> Unit,
    onSendMessage: (text: String, ephemeralSeconds: Int) -> Unit,
    onSendVoiceMemo: (durationSeconds: Int) -> Unit,
    onBurnMessage: (messageId: String) -> Unit,
    onShowFingerprints: () -> Unit,
    onToggleLockChat: (() -> Unit)? = null,
    onToggleArchiveChat: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var selectedEphemeralSeconds by remember { mutableStateOf(thread.ephemeralSettingSeconds) }
    var showTimerMenu by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var isVoicePlaying by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsAppBackground)
    ) {
        // WhatsApp Chat Header
        Surface(
            color = WhatsAppSurface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("chat_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = WhatsAppTextPrimary
                    )
                }

                NexusAvatar(
                    seed = thread.avatarSeed,
                    size = 38,
                    showOnline = thread.isOnline
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onShowFingerprints() }
                ) {
                    Text(
                        text = thread.contactName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WhatsAppTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (thread.isOnline) "online" else "🔒 end-to-end encrypted",
                        fontSize = 11.sp,
                        color = if (thread.isOnline) WhatsAppGreenBright else WhatsAppTextSecondary
                    )
                }

                // WhatsApp call icons
                IconButton(
                    onClick = { Toast.makeText(context, "Starting E2EE Video Call", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Video Call",
                        tint = WhatsAppTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = { Toast.makeText(context, "Starting E2EE Voice Call", Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Voice Call",
                        tint = WhatsAppTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMoreMenu = true },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = WhatsAppTextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMoreMenu,
                        onDismissRequest = { showMoreMenu = false },
                        modifier = Modifier.background(WhatsAppCard)
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (thread.isLocked) "Unlock chat" else "Lock chat", color = WhatsAppTextPrimary) },
                            leadingIcon = { Icon(if (thread.isLocked) Icons.Default.LockOpen else Icons.Default.Lock, contentDescription = null, tint = WhatsAppGreen) },
                            onClick = {
                                showMoreMenu = false
                                onToggleLockChat?.invoke()
                                val msg = if (thread.isLocked) "Unlocked chat back to main list" else "Chat locked and hidden"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (thread.isArchived) "Unarchive chat" else "Archive chat", color = WhatsAppTextPrimary) },
                            leadingIcon = { Icon(if (thread.isArchived) Icons.Default.Unarchive else Icons.Default.Archive, contentDescription = null, tint = WhatsAppGreen) },
                            onClick = {
                                showMoreMenu = false
                                onToggleArchiveChat?.invoke()
                                val msg = if (thread.isArchived) "Chat unarchived" else "Chat archived"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("View safety numbers", color = WhatsAppTextPrimary) },
                            leadingIcon = { Icon(Icons.Default.Fingerprint, contentDescription = null, tint = WhatsAppGreen) },
                            onClick = {
                                showMoreMenu = false
                                onShowFingerprints()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Disappearing messages", color = WhatsAppTextPrimary) },
                            leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null, tint = WhatsAppGreen) },
                            onClick = {
                                showMoreMenu = false
                                showTimerMenu = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Clear chat", color = WhatsAppTextPrimary) },
                            onClick = { showMoreMenu = false }
                        )
                    }
                }
            }
        }

        // Ephemeral timer dropdown if requested
        if (showTimerMenu) {
            AlertDialog(
                onDismissRequest = { showTimerMenu = false },
                containerColor = WhatsAppSurface,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = WhatsAppGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Disappearing Messages", color = WhatsAppTextPrimary, fontSize = 18.sp)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "When turned on, new messages will disappear from this chat after the selected duration.",
                            fontSize = 13.sp,
                            color = WhatsAppTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        listOf(
                            0 to "Off (Permanent)",
                            10 to "10 seconds",
                            60 to "1 minute",
                            300 to "5 minutes",
                            86400 to "24 hours"
                        ).forEach { (seconds, label) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedEphemeralSeconds = seconds
                                        showTimerMenu = false
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedEphemeralSeconds == seconds,
                                    onClick = {
                                        selectedEphemeralSeconds = seconds
                                        showTimerMenu = false
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = WhatsAppGreen,
                                        unselectedColor = WhatsAppTextSecondary
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(label, color = WhatsAppTextPrimary, fontSize = 14.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showTimerMenu = false }) {
                        Text("Done", color = WhatsAppGreen)
                    }
                }
            )
        }

        // Vanish Mode Notification Banner
        if (selectedEphemeralSeconds > 0) {
            Surface(
                color = Color(0xFF1E2A30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = WhatsAppGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Disappearing messages are set to ${selectedEphemeralSeconds}s",
                        fontSize = 12.sp,
                        color = WhatsAppGreen
                    )
                }
            }
        }

        // Chat Canvas
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 10.dp),
            reverseLayout = false
        ) {
            // WhatsApp-style Central Security Notice
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color(0xFF182229),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.widthIn(max = 320.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFFFFD279),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Messages and calls are end-to-end encrypted. No one outside of this chat can read or listen to them.",
                                fontSize = 11.sp,
                                color = Color(0xFFFFD279),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // Date pill (WhatsApp style)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color(0xFF182229),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "TODAY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WhatsAppTextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                WhatsAppMessageBubble(
                    message = msg,
                    isPlaying = isVoicePlaying == msg.id,
                    onTogglePlay = {
                        isVoicePlaying = if (isVoicePlaying == msg.id) null else msg.id
                    },
                    onBurn = { onBurnMessage(msg.id) }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // WhatsApp Attachment Tray
        if (showAttachmentMenu) {
            Surface(
                color = WhatsAppCard,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, WhatsAppDivider),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Share Content",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhatsAppTextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        AttachmentOption(Icons.Default.Description, "Document", Color(0xFF7F66FF)) {
                            showAttachmentMenu = false
                            Toast.makeText(context, "Encrypted File Attached", Toast.LENGTH_SHORT).show()
                        }
                        AttachmentOption(Icons.Default.CameraAlt, "Camera", Color(0xFFD3396D)) {
                            showAttachmentMenu = false
                            Toast.makeText(context, "Taking Photo", Toast.LENGTH_SHORT).show()
                        }
                        AttachmentOption(Icons.Default.PhotoLibrary, "Gallery", Color(0xFFAC44CF)) {
                            showAttachmentMenu = false
                            Toast.makeText(context, "Selected Zero-EXIF Photo", Toast.LENGTH_SHORT).show()
                        }
                        AttachmentOption(Icons.Default.CurrencyRupee, "UPI Pay", WhatsAppGreen) {
                            showAttachmentMenu = false
                            Toast.makeText(context, "Open Instant UPI Cash Transfer", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // WhatsApp Signature Bottom Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Capsule Text Input Box
            Surface(
                color = WhatsAppBubbleReceived,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { Toast.makeText(context, "Emoji selector", Toast.LENGTH_SHORT).show() },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mood,
                            contentDescription = "Emoji",
                            tint = WhatsAppTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = if (selectedEphemeralSeconds > 0) "Message (${selectedEphemeralSeconds}s)..." else "Message",
                                color = WhatsAppTextSecondary,
                                fontSize = 15.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = WhatsAppTextPrimary,
                            unfocusedTextColor = WhatsAppTextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        maxLines = 4
                    )

                    IconButton(
                        onClick = { showAttachmentMenu = !showAttachmentMenu },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Attach",
                            tint = WhatsAppTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (inputText.isBlank()) {
                        IconButton(
                            onClick = { Toast.makeText(context, "Quick Photo Snap", Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                tint = WhatsAppTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // WhatsApp Round Green Send / Mic FAB
            FilledIconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText, selectedEphemeralSeconds)
                        inputText = ""
                    } else {
                        onSendVoiceMemo(5)
                        Toast.makeText(context, "Encrypted voice memo recorded (5s)", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = WhatsAppGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .size(48.dp)
                    .testTag("chat_send_btn")
            ) {
                Icon(
                    imageVector = if (inputText.isNotBlank()) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                    contentDescription = if (inputText.isNotBlank()) "Send" else "Voice Memo",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun AttachmentOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(tint),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = WhatsAppTextSecondary)
    }
}

@Composable
fun WhatsAppMessageBubble(
    message: ChatMessage,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onBurn: () -> Unit
) {
    val isMine = message.isMine
    val bubbleColor = if (isMine) WhatsAppBubbleSent else WhatsAppBubbleReceived

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isMine) 12.dp else 2.dp,
                bottomEnd = if (isMine) 2.dp else 12.dp
            ),
            modifier = Modifier.widthIn(min = 80.dp, max = 310.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                if (!isMine) {
                    Text(
                        text = message.senderName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhatsAppGreenBright,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                if (message.isAudio) {
                    AudioWaveformView(
                        durationSeconds = message.audioDurationSeconds,
                        isPlaying = isPlaying,
                        onTogglePlay = onTogglePlay
                    )
                } else {
                    Text(
                        text = message.text,
                        fontSize = 15.sp,
                        color = WhatsAppTextPrimary,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Bottom row: Time + Checkmarks
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (message.ephemeralTimerSeconds > 0) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Self-destruct",
                            tint = Color(0xFFFF8A80),
                            modifier = Modifier
                                .size(11.dp)
                                .padding(end = 2.dp)
                        )
                    }

                    val timeStr = remember(message.timestamp) {
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))
                    }
                    Text(
                        text = timeStr,
                        fontSize = 11.sp,
                        color = WhatsAppTextSecondary
                    )

                    if (isMine) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            tint = WhatsAppCheckmarkBlue,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}
