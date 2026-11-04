package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PeerNode
import com.example.data.model.UserSovereignState
import com.example.data.model.VaultItem
import com.example.ui.components.NexusTopBar
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VaultScreen(
    userState: UserSovereignState,
    peerNodes: List<PeerNode>,
    vaultItems: List<VaultItem>,
    onToggleTor: () -> Unit,
    onToggleGhostMode: () -> Unit,
    onUpgradePro: () -> Unit,
    onShowFingerprintSheet: () -> Unit,
    onAddNewSecret: () -> Unit,
    onDeleteSecret: (String) -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableIntStateOf(0) } // 0 = Credentials & Notes Vault, 1 = Mesh Nodes & Relays
    var selectedCategoryFilter by remember { mutableStateOf("ALL") } // "ALL", "LOGIN", "NOTE", "SEED"
    var vaultSearchQuery by remember { mutableStateOf("") }

    val filteredVaultItems = remember(vaultItems, selectedCategoryFilter, vaultSearchQuery) {
        vaultItems.filter { item ->
            val matchCategory = selectedCategoryFilter == "ALL" || item.category.equals(selectedCategoryFilter, ignoreCase = true)
            val matchQuery = vaultSearchQuery.isBlank() ||
                    item.title.contains(vaultSearchQuery, ignoreCase = true) ||
                    item.identifier.contains(vaultSearchQuery, ignoreCase = true) ||
                    item.notes.contains(vaultSearchQuery, ignoreCase = true)
            matchCategory && matchQuery
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusObsidian)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NexusTopBar(
                title = "Security Vault",
                subtitle = "ZERO-KNOWLEDGE LOCAL ROOM DB",
                userState = userState,
                onOpenVault = {}
            )

            // Top Navigation Segmented Switch
            Surface(
                color = NexusSlateDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder)
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    // Tab 0: Zero-Knowledge Credentials Vault
                    Surface(
                        color = if (activeTab == 0) NexusCyan else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeTab = 0 }
                            .testTag("tab_credentials_vault")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (activeTab == 0) NexusObsidian else NexusTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Credentials & Notes",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeTab == 0) NexusObsidian else NexusTextSecondary
                            )
                        }
                    }

                    // Tab 1: P2P Mesh Nodes & Relays
                    Surface(
                        color = if (activeTab == 1) NexusCyan else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeTab = 1 }
                            .testTag("tab_mesh_nodes")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lan,
                                contentDescription = null,
                                tint = if (activeTab == 1) NexusObsidian else NexusTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Mesh Nodes",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeTab == 1) NexusObsidian else NexusTextSecondary
                            )
                        }
                    }
                }
            }

            // Body based on activeTab
            if (activeTab == 0) {
                // Zero-Knowledge Credentials Vault View
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Security Enclave Info Banner
                    item {
                        Surface(
                            color = NexusSlateDark,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusEmerald.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(NexusEmerald.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = NexusEmerald,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "On-Device Zero-Knowledge Vault Active",
                                        fontWeight = FontWeight.Bold,
                                        color = NexusTextPrimary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Stored locally in encrypted SQLite Room database with AES-128. Zero cloud telemetry. Plaintext never leaves RAM.",
                                        color = NexusTextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    // Search and Category Chips
                    item {
                        OutlinedTextField(
                            value = vaultSearchQuery,
                            onValueChange = { vaultSearchQuery = it },
                            placeholder = { Text("Search encrypted credentials...", color = NexusTextTertiary, fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NexusCyan, modifier = Modifier.size(18.dp)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = NexusSlateCard,
                                unfocusedContainerColor = NexusSlateCard,
                                focusedBorderColor = NexusCyan,
                                unfocusedBorderColor = NexusCardBorder,
                                focusedTextColor = NexusTextPrimary,
                                unfocusedTextColor = NexusTextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("vault_search_field"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Category filter chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val filters = listOf(
                                "ALL" to "All (${vaultItems.size})",
                                "LOGIN" to "🔑 Logins",
                                "NOTE" to "📝 Notes",
                                "SEED" to "🌱 Seeds"
                            )
                            filters.forEach { (catKey, catLabel) ->
                                val isSelected = selectedCategoryFilter == catKey
                                Surface(
                                    color = if (isSelected) NexusSlateCard else NexusObsidian,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NexusCyan else NexusCardBorder),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCategoryFilter = catKey }
                                        .testTag("filter_$catKey")
                                ) {
                                    Text(
                                        text = catLabel,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) NexusCyan else NexusTextSecondary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Empty State
                    if (filteredVaultItems.isEmpty()) {
                        item {
                            Surface(
                                color = NexusSlateDark,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(imageVector = Icons.Default.LockClock, contentDescription = null, tint = NexusTextTertiary, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "No secrets in this category",
                                        fontWeight = FontWeight.Bold,
                                        color = NexusTextPrimary,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tap '+ Add Secret' below to securely store passwords, private notes, or recovery seeds.",
                                        fontSize = 12.sp,
                                        color = NexusTextSecondary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Vault items
                    items(filteredVaultItems, key = { it.id }) { item ->
                        VaultSecretCard(
                            item = item,
                            onCopy = { textToCopy ->
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Nexus Secret", textToCopy)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied securely to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onDelete = { onDeleteSecret(item.id) }
                        )
                    }
                }
            } else {
                // Mesh Nodes & Relays View
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Identity Enclave Card
                    item {
                        Surface(
                            color = NexusSlateDark,
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "DECENTRALIZED IDENTITY (DID)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NexusTextTertiary,
                                        letterSpacing = 1.sp
                                    )
                                    Surface(
                                        color = NexusEmerald.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "ENCLAVE HARDENED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NexusEmerald,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = userState.didHandle,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = NexusCyan
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = userState.didId,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = NexusTextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    color = NexusSlateCard,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Cryptographic Fingerprint", fontSize = 10.sp, color = NexusTextTertiary)
                                            Text(userState.masterFingerprint, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = NexusTextPrimary)
                                        }
                                        OutlinedButton(
                                            onClick = onShowFingerprintSheet,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexusCyan),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text("Verify", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Security Controls Grid
                    item {
                        Text(
                            text = "DECENTRALIZED NETWORK CONTROLS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexusTextTertiary,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tor Onion Routing Toggle
                        Surface(
                            color = NexusSlateDark,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(NexusViolet.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = Icons.Default.VpnLock, contentDescription = null, tint = NexusViolet, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Multi-Hop Tor Onion Relays", fontWeight = FontWeight.Bold, color = NexusTextPrimary, fontSize = 13.sp)
                                        Text("Scrambles IP across 3 encrypted nodes", fontSize = 11.sp, color = NexusTextSecondary)
                                    }
                                }
                                Switch(
                                    checked = userState.torOnionRelayEnabled,
                                    onCheckedChange = { onToggleTor() },
                                    colors = SwitchDefaults.colors(checkedThumbColor = NexusCyan, checkedTrackColor = NexusSlateCard)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Ghost Mode (Premium Pro Feature)
                        Surface(
                            color = NexusSlateDark,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (userState.isProSubscriber) NexusEmerald else NexusCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (userState.isProSubscriber) NexusEmerald.copy(alpha = 0.2f) else NexusCardBorder),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = null, tint = if (userState.isProSubscriber) NexusEmerald else NexusTextTertiary, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Stealth Ghost Mode", fontWeight = FontWeight.Bold, color = NexusTextPrimary, fontSize = 13.sp)
                                            if (!userState.isProSubscriber) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(color = NexusAmberGold.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                                    Text("PRO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = NexusAmberGold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                                }
                                            }
                                        }
                                        Text(
                                            text = if (userState.isProSubscriber) "True zero-presence network footprint" else "Unlock with Nexus Sovereign Pro (USD)",
                                            fontSize = 11.sp,
                                            color = NexusTextSecondary
                                        )
                                    }
                                }
                                if (userState.isProSubscriber) {
                                    Switch(
                                        checked = userState.stealthGhostMode,
                                        onCheckedChange = { onToggleGhostMode() },
                                        colors = SwitchDefaults.colors(checkedThumbColor = NexusEmerald, checkedTrackColor = NexusSlateCard)
                                    )
                                } else {
                                    TextButton(onClick = onUpgradePro) {
                                        Text("Upgrade", color = NexusAmberGold, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Connected P2P Peer Relays
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ACTIVE MESH PEER RELAYS (${peerNodes.size})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexusTextTertiary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${userState.totalHopsToday} Hops Routed",
                                fontSize = 11.sp,
                                color = NexusCyan,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    items(peerNodes, key = { it.id }) { node ->
                        Surface(
                            color = NexusSlateDark,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(if (node.pingMs < 35) NexusEmerald else NexusAmberGold)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(node.name, fontWeight = FontWeight.Bold, color = NexusTextPrimary, fontSize = 13.sp)
                                        Text("${node.location} • ${node.protocol}", fontSize = 11.sp, color = NexusTextSecondary)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("${node.pingMs}ms", color = NexusCyan, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                    Text(node.ipMasked, color = NexusTextTertiary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button to Add New Secret (Visible in Credentials Vault tab)
        if (activeTab == 0) {
            ExtendedFloatingActionButton(
                onClick = onAddNewSecret,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Secret", fontWeight = FontWeight.Bold) },
                containerColor = NexusCyan,
                contentColor = NexusObsidian,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 96.dp)
                    .testTag("vault_add_fab")
            )
        }
    }
}

@Composable
fun VaultSecretCard(
    item: VaultItem,
    onCopy: (String) -> Unit,
    onDelete: () -> Unit
) {
    var isRevealed by remember { mutableStateOf(false) }

    val categoryIcon = when (item.category.uppercase()) {
        "LOGIN" -> Icons.Default.Key
        "NOTE" -> Icons.Default.Notes
        "SEED" -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.Lock
    }

    val categoryColor = when (item.category.uppercase()) {
        "LOGIN" -> NexusCyan
        "NOTE" -> NexusViolet
        "SEED" -> NexusEmerald
        else -> NexusCyan
    }

    Surface(
        color = NexusSlateDark,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("secret_card_${item.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(categoryColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = categoryIcon, contentDescription = null, tint = categoryColor, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            color = NexusTextPrimary,
                            fontSize = 14.sp
                        )
                        if (item.identifier.isNotBlank()) {
                            Text(
                                text = item.identifier,
                                fontSize = 11.sp,
                                color = NexusCyan,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // Delete Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp).testTag("delete_secret_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Secret",
                        tint = NexusTextTertiary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secret Value Box
            Surface(
                color = NexusSlateCard,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isRevealed) item.plainSecret else "••••••••••••••••",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        fontWeight = if (isRevealed) FontWeight.Medium else FontWeight.Bold,
                        color = if (isRevealed) NexusTextPrimary else NexusTextTertiary,
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Reveal toggle
                        IconButton(
                            onClick = { isRevealed = !isRevealed },
                            modifier = Modifier.size(32.dp).testTag("reveal_secret_${item.id}")
                        ) {
                            Icon(
                                imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Reveal",
                                tint = if (isRevealed) NexusCyan else NexusTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Copy button
                        IconButton(
                            onClick = { onCopy(item.plainSecret) },
                            modifier = Modifier.size(32.dp).testTag("copy_secret_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Secret",
                                tint = NexusEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Notes Preview if present
            if (item.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.notes,
                    fontSize = 11.sp,
                    color = NexusTextSecondary
                )
            }
        }
    }
}
