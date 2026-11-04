package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatThread
import com.example.ui.components.NexusAvatar
import com.example.ui.theme.*

@Composable
fun LockedChatsScreen(
    lockedThreads: List<ChatThread>,
    allThreads: List<ChatThread>,
    onBack: () -> Unit,
    onOpenThread: (ChatThread) -> Unit,
    onUnlockThread: (String) -> Unit,
    onLockThread: (String) -> Unit
) {
    val context = LocalContext.current
    var isAuthenticated by remember { mutableStateOf(false) }
    var showLockChatDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsAppBackground)
            .statusBarsPadding()
    ) {
        // WhatsApp Locked Chats Top Bar
        Surface(
            color = WhatsAppSurface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("locked_chats_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = WhatsAppTextPrimary
                    )
                }
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = WhatsAppGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Locked chats",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhatsAppTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                if (isAuthenticated) {
                    IconButton(onClick = { showLockChatDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Lock another chat",
                            tint = WhatsAppGreen
                        )
                    }
                }
            }
        }

        if (!isAuthenticated) {
            // Biometric / PIN Lock Screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(WhatsAppGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Biometric Lock",
                            tint = WhatsAppGreen,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Chat Lock Active",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhatsAppTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Use your fingerprint, face, or device PIN to view confidential locked chats.",
                        fontSize = 14.sp,
                        color = WhatsAppTextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            isAuthenticated = true
                            Toast.makeText(context, "Biometric authentication verified", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp)
                            .testTag("unlock_biometric_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Unlock with Fingerprint", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            isAuthenticated = true
                            Toast.makeText(context, "Device PIN accepted", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = WhatsAppTextPrimary),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Use Device PIN")
                    }
                }
            }
        } else {
            // Authenticated Locked Chats List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                // Privacy Info Banner
                item {
                    Surface(
                        color = WhatsAppSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = WhatsAppGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Locked chats are hidden from your main chat list. Notifications do not reveal sender or content. End-to-end encryption remains active.",
                                fontSize = 12.sp,
                                color = WhatsAppTextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                    HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)
                }

                if (lockedThreads.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(WhatsAppSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = WhatsAppTextSecondary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No locked chats",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WhatsAppTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tap '+ Lock a Chat' to move confidential conversations here.",
                                    fontSize = 13.sp,
                                    color = WhatsAppTextSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(lockedThreads, key = { it.id }) { thread ->
                        LockedChatItem(
                            thread = thread,
                            onClick = { onOpenThread(thread) },
                            onUnlock = {
                                onUnlockThread(thread.id)
                                Toast.makeText(context, "Unlocked ${thread.contactName} back to main chats", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog to pick a chat to lock
    if (showLockChatDialog) {
        val unlockableThreads = allThreads.filter { !it.isLocked }
        AlertDialog(
            onDismissRequest = { showLockChatDialog = false },
            containerColor = WhatsAppSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = WhatsAppGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lock a Chat", color = WhatsAppTextPrimary, fontSize = 18.sp)
                }
            },
            text = {
                if (unlockableThreads.isEmpty()) {
                    Text("All your active chats are already locked.", color = WhatsAppTextSecondary)
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                        items(unlockableThreads) { thread ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onLockThread(thread.id)
                                        showLockChatDialog = false
                                        Toast.makeText(context, "Locked ${thread.contactName}", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NexusAvatar(seed = thread.avatarSeed, size = 40)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(thread.contactName, color = WhatsAppTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(thread.contactHandle, color = WhatsAppTextSecondary, fontSize = 11.sp)
                                }
                                Icon(Icons.Default.Lock, contentDescription = "Lock", tint = WhatsAppGreen, modifier = Modifier.size(18.dp))
                            }
                            HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLockChatDialog = false }) {
                    Text("Cancel", color = WhatsAppGreen)
                }
            }
        )
    }
}

@Composable
fun LockedChatItem(
    thread: ChatThread,
    onClick: () -> Unit,
    onUnlock: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            NexusAvatar(
                seed = thread.avatarSeed,
                size = 52,
                showOnline = thread.isOnline
            )
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(WhatsAppGreen)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.White,
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
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
                    text = "🔒 Locked",
                    fontSize = 11.sp,
                    color = WhatsAppGreenBright,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = thread.lastMessage,
                    fontSize = 13.sp,
                    color = WhatsAppTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onUnlock,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LockOpen,
                        contentDescription = "Unlock to Main Chats",
                        tint = WhatsAppTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
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
