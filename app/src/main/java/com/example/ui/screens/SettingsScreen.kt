package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserSovereignState
import com.example.ui.components.NexusAvatar
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    userState: UserSovereignState,
    onBack: () -> Unit,
    onOpenVault: () -> Unit,
    onOpenLockedChats: () -> Unit,
    onToggleTor: () -> Unit,
    onToggleGhostMode: () -> Unit,
    onActivateAllFeatures: () -> Unit = {},
    onAddCashBalance: (Double) -> Unit = {},
    onSendCashUpi: (String, Double) -> Pair<Boolean, String> = { _, _ -> Pair(true, "CONFIRMED") },
    onOpenStore: () -> Unit = {}
) {
    val context = LocalContext.current
    var readReceiptsEnabled by remember { mutableStateOf(true) }

    // Dialog state triggers
    var showQrDialog by remember { mutableStateOf(false) }
    var showAccountDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showChatsDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showStorageDialog by remember { mutableStateOf(false) }
    var showPaymentsDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatsAppBackground)
            .statusBarsPadding()
    ) {
        // WhatsApp Settings Top Bar
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = WhatsAppTextPrimary
                    )
                }
                Text(
                    text = "Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = WhatsAppTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                // Master "All Active" pill indicator
                Surface(
                    color = WhatsAppGreen.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .clickable {
                            onActivateAllFeatures()
                            Toast.makeText(context, "All Sovereign & Pro features are fully activated!", Toast.LENGTH_SHORT).show()
                        }
                        .padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(WhatsAppGreenBright)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ALL ACTIVE",
                            color = WhatsAppGreenBright,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            // Master Activation Status Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = WhatsAppCard),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(WhatsAppGreen.copy(alpha = 0.4f))
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = WhatsAppGreenBright,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "All Pro Features Activated",
                                    color = WhatsAppTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                color = WhatsAppGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "LIFETIME PRO",
                                    color = WhatsAppGreenBright,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🛡️ Kyber-1024 Quantum Shield • 🧅 Tor Onion Multi-Hop • 👻 Stealth Ghost Mode • 🔐 Zero-Knowledge Vault • ⚡ ₹25,000 UPI Cash",
                            color = WhatsAppTextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    onActivateAllFeatures()
                                    Toast.makeText(context, "All sovereign modules re-synchronized and active!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("reverify_features_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = WhatsAppBackground,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Features Active ✓", color = WhatsAppBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = onOpenStore,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = WhatsAppGreen),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(WhatsAppGreen.copy(alpha = 0.6f))
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(0.9f)
                            ) {
                                Text("Store Perks", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Profile Card (WhatsApp style)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showQrDialog = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NexusAvatar(seed = "@satoshi_prime", size = 60, showOnline = true)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sovereign Prime",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WhatsAppTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "🔒 E2EE active • Zero Meta tracking",
                            fontSize = 13.sp,
                            color = WhatsAppTextSecondary,
                            maxLines = 1
                        )
                    }
                    IconButton(onClick = { showQrDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QR Code",
                            tint = WhatsAppGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)
            }

            // Settings Items List
            item {
                SettingsRow(
                    icon = Icons.Default.VpnKey,
                    title = "Account",
                    subtitle = "Security notifications, passkeys, change number",
                    onClick = { showAccountDialog = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Lock,
                    title = "Privacy",
                    subtitle = "Last seen, read receipts, chat lock, vanishing timers",
                    onClick = { showPrivacyDialog = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Shield,
                    title = "Zero-Knowledge Security Vault",
                    subtitle = "AES-128 encrypted local Room storage for credentials & seed phrases",
                    onClick = onOpenVault
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.LockClock,
                    title = "Locked Chats",
                    subtitle = "Biometrically protected & hidden confidential conversations",
                    onClick = onOpenLockedChats
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Chat,
                    title = "Chats",
                    subtitle = "Theme, wallpapers, keep chats archived, chat backup",
                    onClick = { showChatsDialog = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Message, group, sovereign channel & call tones",
                    onClick = { showNotificationsDialog = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.DataUsage,
                    title = "Storage and data",
                    subtitle = if (userState.torOnionRelayEnabled) "Tor Onion Relay ACTIVE • 0 Bytes leaked" else "P2P network usage, proxy settings",
                    onClick = { showStorageDialog = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.CurrencyRupee,
                    title = "Payments",
                    subtitle = "UPI (Google Pay, PhonePe, Paytm, RuPay) • ₹${"%,.2f".format(userState.cashBalance)} balance",
                    onClick = { showPaymentsDialog = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.Language,
                    title = "App language",
                    subtitle = "English (device's language) / हिन्दी (Hindi)",
                    onClick = { showLanguageDialog = true }
                )
            }

            item {
                SettingsRow(
                    icon = Icons.Default.HelpOutline,
                    title = "Help",
                    subtitle = "Help center, privacy whitepaper, terms & licenses",
                    onClick = { showHelpDialog = true }
                )
            }

            // WhatsApp-style Footer
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "from",
                        fontSize = 11.sp,
                        color = WhatsAppTextSecondary
                    )
                    Text(
                        text = "NEXUS SOVEREIGN NETWORK",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = WhatsAppGreen
                    )
                    Text(
                        text = "Zero Metadata • Peer-to-Peer • Double-Ratchet E2EE",
                        fontSize = 10.sp,
                        color = WhatsAppTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }

    // Dialogs
    if (showQrDialog) {
        QrCodeDialog(onDismiss = { showQrDialog = false })
    }

    if (showAccountDialog) {
        AccountSecurityDialog(
            userState = userState,
            onDismiss = { showAccountDialog = false }
        )
    }

    if (showPrivacyDialog) {
        PrivacySettingsDialog(
            userState = userState,
            readReceiptsEnabled = readReceiptsEnabled,
            onToggleReadReceipts = { readReceiptsEnabled = it },
            onToggleTor = onToggleTor,
            onToggleGhostMode = onToggleGhostMode,
            onDismiss = { showPrivacyDialog = false }
        )
    }

    if (showChatsDialog) {
        ChatsSettingsDialog(onDismiss = { showChatsDialog = false })
    }

    if (showNotificationsDialog) {
        NotificationsSettingsDialog(onDismiss = { showNotificationsDialog = false })
    }

    if (showStorageDialog) {
        StorageDataDialog(
            userState = userState,
            onToggleTor = onToggleTor,
            onDismiss = { showStorageDialog = false }
        )
    }

    if (showPaymentsDialog) {
        PaymentsUpiDialog(
            userState = userState,
            onAddCash = onAddCashBalance,
            onSendUpi = onSendCashUpi,
            onDismiss = { showPaymentsDialog = false }
        )
    }

    if (showLanguageDialog) {
        LanguageDialog(onDismiss = { showLanguageDialog = false })
    }

    if (showHelpDialog) {
        HelpDialog(onDismiss = { showHelpDialog = false })
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = WhatsAppTextSecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = WhatsAppTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = WhatsAppTextSecondary,
                lineHeight = 17.sp
            )
        }
    }
}
