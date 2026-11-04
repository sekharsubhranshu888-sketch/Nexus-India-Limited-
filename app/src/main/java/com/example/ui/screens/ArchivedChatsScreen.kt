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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatThread
import com.example.ui.components.NexusAvatar
import com.example.ui.theme.*

@Composable
fun ArchivedChatsScreen(
    archivedThreads: List<ChatThread>,
    onBack: () -> Unit,
    onOpenThread: (ChatThread) -> Unit,
    onUnarchiveThread: (String) -> Unit
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsAppBackground)
            .statusBarsPadding()
    ) {
        // WhatsApp Archived Top Bar
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("archived_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = WhatsAppTextPrimary
                    )
                }
                Text(
                    text = "Archived",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhatsAppTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = WhatsAppTextPrimary
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(WhatsAppCard)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Archive settings", color = WhatsAppTextPrimary) },
                            onClick = {
                                showMenu = false
                                Toast.makeText(context, "Keep chats archived: ON (Privacy Preserved)", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // WhatsApp explanation banner
        Surface(
            color = WhatsAppSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = WhatsAppTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "These chats stay archived when new messages are received.",
                    fontSize = 12.sp,
                    color = WhatsAppTextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
        HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

        if (archivedThreads.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(WhatsAppSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Archive,
                            contentDescription = null,
                            tint = WhatsAppTextSecondary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No archived chats",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WhatsAppTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Swipe left or tap more in any chat to archive it here.",
                        fontSize = 13.sp,
                        color = WhatsAppTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                items(archivedThreads, key = { it.id }) { thread ->
                    ArchivedChatItem(
                        thread = thread,
                        onClick = { onOpenThread(thread) },
                        onUnarchive = {
                            onUnarchiveThread(thread.id)
                            Toast.makeText(context, "Unarchived ${thread.contactName}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ArchivedChatItem(
    thread: ChatThread,
    onClick: () -> Unit,
    onUnarchive: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NexusAvatar(
            seed = thread.avatarSeed,
            size = 52,
            showOnline = thread.isOnline
        )

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
                    text = "Archived",
                    fontSize = 11.sp,
                    color = WhatsAppTextSecondary
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
                    onClick = onUnarchive,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Unarchive,
                        contentDescription = "Unarchive",
                        tint = WhatsAppGreen,
                        modifier = Modifier.size(20.dp)
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
