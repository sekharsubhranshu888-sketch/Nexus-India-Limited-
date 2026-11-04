package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CirclePost
import com.example.data.model.NexusCircle
import com.example.data.model.UserSovereignState
import com.example.ui.components.NexusAvatar
import com.example.ui.components.NexusTopBar
import com.example.ui.theme.*

@Composable
fun CirclesScreen(
    circles: List<NexusCircle>,
    selectedCircle: NexusCircle?,
    circlePosts: Map<String, List<CirclePost>>,
    userState: UserSovereignState,
    onSelectCircle: (NexusCircle) -> Unit,
    onCloseCircle: () -> Unit,
    onUpvotePost: (circleId: String, postId: String) -> Unit,
    onAddPost: (circleId: String, content: String) -> Unit,
    onOpenVault: () -> Unit
) {
    if (selectedCircle != null) {
        CircleDetailScreen(
            circle = selectedCircle,
            posts = circlePosts[selectedCircle.id] ?: emptyList(),
            userState = userState,
            onBack = onCloseCircle,
            onUpvote = { postId -> onUpvotePost(selectedCircle.id, postId) },
            onAddPost = { content -> onAddPost(selectedCircle.id, content) }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NexusObsidian)
        ) {
            NexusTopBar(
                title = "Communities",
                subtitle = "P2P TOPIC GROUPS • NO DATA MINING",
                userState = userState,
                onOpenVault = onOpenVault
            )

            // Facebook Disruption Banner
            Surface(
                color = NexusSlateDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NexusCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Groups, contentDescription = null, tint = NexusCyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Uncensored P2P Groups",
                            fontWeight = FontWeight.Bold,
                            color = NexusTextPrimary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "No algorithmic outrage feeds. No shadowbans or data mining. Each Circle is governed by peer nodes.",
                            color = NexusTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(circles, key = { it.id }) { circle ->
                    CircleCard(circle = circle, onClick = { onSelectCircle(circle) })
                }
            }
        }
    }
}

@Composable
fun CircleCard(
    circle: NexusCircle,
    onClick: () -> Unit
) {
    Surface(
        color = NexusSlateDark,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("circle_card_${circle.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = NexusSlateCard,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = circle.tag,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexusCyan,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = NexusEmerald.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = circle.nodeHealth,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexusEmerald,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = circle.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NexusTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = circle.description,
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.People, contentDescription = null, tint = NexusTextTertiary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${"%,d".format(circle.memberCount)} Sovereign Peers",
                        fontSize = 12.sp,
                        color = NexusTextTertiary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Enter Circle",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexusCyan
                    )
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = NexusCyan, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun CircleDetailScreen(
    circle: NexusCircle,
    posts: List<CirclePost>,
    userState: UserSovereignState,
    onBack: () -> Unit,
    onUpvote: (postId: String) -> Unit,
    onAddPost: (content: String) -> Unit
) {
    BackHandler { onBack() }

    var newPostText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusObsidian)
    ) {
        // Circle Detail Header
        Surface(
            color = NexusSlateDark,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("circle_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NexusTextPrimary
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = circle.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NexusTextPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "${circle.tag} • ${circle.nodeHealth}",
                        fontSize = 11.sp,
                        color = NexusCyan,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Posts
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(posts, key = { it.id }) { post ->
                Surface(
                    color = NexusSlateDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                NexusAvatar(seed = post.authorHandle, size = 32)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(post.authorName, fontWeight = FontWeight.Bold, color = NexusTextPrimary, fontSize = 13.sp)
                                    Text(post.authorHandle, color = NexusCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = NexusTextPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { onUpvote(post.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (post.isUpvoted) NexusCyan.copy(alpha = 0.2f) else NexusSlateCard,
                                    contentColor = if (post.isUpvoted) NexusCyan else NexusTextSecondary
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${post.upvotes} Consensus",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Comment, contentDescription = null, tint = NexusTextTertiary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${post.repliesCount} replies",
                                    fontSize = 11.sp,
                                    color = NexusTextTertiary
                                )
                            }
                        }
                    }
                }
            }
        }

        // New post input bar
        Surface(
            color = NexusSlateDark,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newPostText,
                    onValueChange = { newPostText = it },
                    placeholder = { Text("Post to this Circle...", color = NexusTextTertiary, fontSize = 14.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NexusSlateCard,
                        unfocusedContainerColor = NexusSlateCard,
                        focusedBorderColor = NexusCyan,
                        unfocusedBorderColor = NexusCardBorder,
                        focusedTextColor = NexusTextPrimary,
                        unfocusedTextColor = NexusTextPrimary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("circle_post_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilledIconButton(
                    onClick = {
                        if (newPostText.isNotBlank()) {
                            onAddPost(newPostText)
                            newPostText = ""
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = NexusCyan,
                        contentColor = NexusObsidian
                    ),
                    modifier = Modifier.testTag("circle_post_submit")
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
