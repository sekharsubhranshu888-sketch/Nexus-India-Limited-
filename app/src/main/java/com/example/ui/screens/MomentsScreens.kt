package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NexusPost
import com.example.data.model.NexusStory
import com.example.data.model.UserSovereignState
import com.example.ui.components.NexusAvatar
import com.example.ui.components.NexusTopBar
import com.example.ui.theme.*

@Composable
fun MomentsScreen(
    posts: List<NexusPost>,
    stories: List<NexusStory>,
    userState: UserSovereignState,
    onOpenStory: (NexusStory) -> Unit,
    onLikePost: (postId: String) -> Unit,
    onTipCreator: (handle: String) -> Unit,
    onOpenCreatePost: () -> Unit,
    onOpenVault: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusObsidian)
    ) {
        NexusTopBar(
            title = "Updates",
            subtitle = "STATUS • ZERO-EXIF CHANNELS",
            userState = userState,
            onOpenVault = onOpenVault,
            actions = {
                IconButton(
                    onClick = onOpenCreatePost,
                    modifier = Modifier.testTag("action_new_moment")
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "New Status",
                        tint = WhatsAppTextPrimary
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // WhatsApp Status Header
            item {
                Text(
                    text = "Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhatsAppTextPrimary,
                    modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 4.dp)
                )
            }

            // Stories tray (WhatsApp Status style)
            item {
                StoriesTray(
                    stories = stories,
                    onStoryClick = onOpenStory,
                    onAddStory = onOpenCreatePost
                )
            }

            // Anti-Surveillance Explainer Card
            item {
                Surface(
                    color = NexusSlateDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexusViolet.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = NexusViolet,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Zero Surveillance Visual Feed",
                                fontWeight = FontWeight.Bold,
                                color = NexusTextPrimary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Chronological order only. Meta algorithms cannot manipulate your feed, inject targeted ads, or scrape biometric data.",
                                color = NexusTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Feed Posts
            items(posts, key = { it.id }) { post ->
                NexusPostCard(
                    post = post,
                    onLike = { onLikePost(post.id) },
                    onTip = { onTipCreator(post.authorHandle) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun StoriesTray(
    stories: List<NexusStory>,
    onStoryClick: (NexusStory) -> Unit,
    onAddStory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // WhatsApp My Status item
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onAddStory() }
                        .testTag("add_story_btn")
                ) {
                    Box(
                        modifier = Modifier.size(62.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        NexusAvatar(seed = "@me", size = 56)
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(WhatsAppGreen)
                                .border(1.5.dp, WhatsAppBackground, CircleShape)
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add status",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "My Status",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = WhatsAppTextPrimary
                    )
                }
            }

            items(stories, key = { it.id }) { story ->
                val ringColor = if (!story.hasViewed) WhatsAppGreen else WhatsAppDivider

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onStoryClick(story) }
                        .testTag("story_item_${story.id}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, ringColor, CircleShape)
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        NexusAvatar(seed = story.avatarSeed, size = 52)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = story.authorName.take(8),
                        fontSize = 12.sp,
                        color = if (!story.hasViewed) WhatsAppTextPrimary else WhatsAppTextSecondary,
                        fontWeight = if (!story.hasViewed) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun NexusPostCard(
    post: NexusPost,
    onLike: () -> Unit,
    onTip: () -> Unit
) {
    Surface(
        color = NexusSlateDark,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("post_card_${post.id}")
    ) {
        Column {
            // Post Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NexusAvatar(seed = post.avatarSeed, size = 42)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.authorName,
                                fontWeight = FontWeight.Bold,
                                color = NexusTextPrimary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Identity",
                                tint = NexusCyan,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Text(
                            text = post.authorHandle,
                            fontSize = 11.sp,
                            color = NexusCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Privacy Pill
                Surface(
                    color = NexusSlateCard,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusEmerald.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = NexusEmerald,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "NO-AI SCRAPE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexusEmerald,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Post Content Text
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextPrimary,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Post Media Representation (Futuristic Encrypted Visual Frame)
            val brush = when (post.mediaGradient) {
                "sunset" -> Brush.verticalGradient(listOf(Color(0xFF8B2500), Color(0xFF2C1033), Color(0xFF0F172A)))
                "neon_matrix" -> Brush.verticalGradient(listOf(Color(0xFF003B22), Color(0xFF04202C), Color(0xFF080B10)))
                "deep_ocean" -> Brush.verticalGradient(listOf(Color(0xFF0D253F), Color(0xFF1E1B4B), Color(0xFF080B10)))
                else -> Brush.verticalGradient(listOf(Color(0xFF2E1065), Color(0xFF0C4A6E), Color(0xFF080B10)))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(brush),
                contentAlignment = Alignment.Center
            ) {
                // Cryptographic Watermark & Geometric Overlay
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = NexusCyan.copy(alpha = 0.8f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "EXIF METADATA STRIPPED",
                        color = NexusCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Anti-Facial Recognition Noise Injected",
                        color = NexusTextSecondary,
                        fontSize = 10.sp
                    )
                }

                // Anti-AI Scraping Watermark in corner
                Surface(
                    color = NexusObsidian.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "🛡️ CRAWLER DEFENSE v4.2",
                        color = NexusEmerald,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            // Post Actions & Creator Cash Tip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Like button
                    IconButton(
                        onClick = onLike,
                        modifier = Modifier.testTag("like_post_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) NexusRose else NexusTextSecondary
                        )
                    }
                    Text(
                        text = post.likesCount.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NexusTextPrimary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Comments button
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comments",
                            tint = NexusTextSecondary
                        )
                    }
                    Text(
                        text = post.commentsCount.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NexusTextPrimary
                    )
                }

                // Direct Cash Tip Button (Disrupting Meta's lack of direct creator monetization)
                Button(
                    onClick = onTip,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NexusEmerald.copy(alpha = 0.2f),
                        contentColor = NexusEmerald
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusEmerald),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("tip_creator_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolunteerActivism,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cash Tip",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StoryViewerDialog(
    story: NexusStory,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NexusSlateDark,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NexusAvatar(seed = story.avatarSeed, size = 36)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(story.authorName, fontWeight = FontWeight.Bold, color = NexusTextPrimary, fontSize = 14.sp)
                        Text(story.authorHandle, color = NexusCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
                Surface(
                    color = NexusRose.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "⏱️ 24H VANISH",
                        color = NexusRose,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(NexusVioletDim, NexusObsidian, NexusCyanDim))
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = NexusCyan, modifier = Modifier.size(54.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = story.caption,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Zero View Tracking", color = NexusEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("Anonymous P2P Hop", color = NexusTextTertiary, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NexusCyan, contentColor = NexusObsidian)
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit,
    onSubmit: (content: String, gradient: String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var selectedGradient by remember { mutableStateOf("cyan_violet") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NexusSlateDark,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = NexusCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Encrypted Moment", fontWeight = FontWeight.Bold, color = NexusTextPrimary)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("What's happening on your decentralized node?", color = NexusTextTertiary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NexusSlateCard,
                        unfocusedContainerColor = NexusSlateCard,
                        focusedBorderColor = NexusCyan,
                        unfocusedBorderColor = NexusCardBorder,
                        focusedTextColor = NexusTextPrimary,
                        unfocusedTextColor = NexusTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .testTag("create_post_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "SELECT VISUAL PRIVACY PALETTE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexusTextTertiary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                val palettes = listOf(
                    "cyan_violet" to "Neon Cyber",
                    "sunset" to "Sunset Cypher",
                    "neon_matrix" to "Matrix Green",
                    "deep_ocean" to "Deep Void"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    palettes.forEach { (key, label) ->
                        val isSelected = selectedGradient == key
                        Surface(
                            color = if (isSelected) NexusCyan else NexusSlateCard,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NexusCyan else NexusCardBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedGradient = key }
                        ) {
                            Text(
                                text = label,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NexusObsidian else NexusTextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = NexusObsidian,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NexusEmerald, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Automatic EXIF metadata purge enabled", fontSize = 11.sp, color = NexusTextSecondary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NexusEmerald, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Anti-AI crawler watermark embedded", fontSize = 11.sp, color = NexusTextSecondary)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(text, selectedGradient) },
                colors = ButtonDefaults.buttonColors(containerColor = NexusCyan, contentColor = NexusObsidian),
                modifier = Modifier.testTag("submit_post_btn")
            ) {
                Text("Publish to Mesh", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = NexusTextSecondary)
            }
        }
    )
}
