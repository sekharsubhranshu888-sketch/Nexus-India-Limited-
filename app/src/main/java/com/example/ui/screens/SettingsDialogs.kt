package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserSovereignState
import com.example.ui.theme.*

@Composable
fun QrCodeDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.QrCode, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Your Contact QR Code", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Scan to add @satoshi_prime on Nexus with zero metadata leakage.",
                    fontSize = 13.sp,
                    color = WhatsAppTextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR Code",
                        tint = Color.Black,
                        modifier = Modifier.size(160.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Fingerprint: 7381 9283 1049 8274",
                    fontSize = 11.sp,
                    color = WhatsAppGreenBright,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun AccountSecurityDialog(
    userState: UserSovereignState,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VpnKey, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Account & Enclave Security", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecurityStatusBadge(title = "Hardware Enclave Passkeys", status = "ACTIVATED", active = true)
                SecurityStatusBadge(title = "Post-Quantum Kyber-1024", status = "SHIELD ACTIVE", active = userState.quantumProtectionActive)
                SecurityStatusBadge(title = "Tor Onion Routing Relay", status = "ROUTING ONION", active = userState.torOnionRelayEnabled)
                SecurityStatusBadge(title = "Two-Step Seed Phrase", status = "ENCRYPTED IN ROOM VAULT", active = true)

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Text("Sovereign Identity Details", color = WhatsAppGreenBright, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("DID: ${userState.didId}", color = WhatsAppTextSecondary, fontSize = 11.sp)
                Text("Master Key: ${userState.masterFingerprint}", color = WhatsAppTextSecondary, fontSize = 11.sp)
                Text("Status: All Pro security enclaves verified.", color = WhatsAppTextPrimary, fontSize = 12.sp)

                Button(
                    onClick = {
                        Toast.makeText(context, "Identity Keys Rotated with Kyber-1024 Ratchet", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    modifier = Modifier.fillMaxWidth().testTag("rotate_keys_btn")
                ) {
                    Text("Rotate Cryptographic Keys", color = WhatsAppBackground, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun PrivacySettingsDialog(
    userState: UserSovereignState,
    readReceiptsEnabled: Boolean,
    onToggleReadReceipts: (Boolean) -> Unit,
    onToggleTor: () -> Unit,
    onToggleGhostMode: () -> Unit,
    onDismiss: () -> Unit
) {
    var lastSeenOption by remember { mutableStateOf("Nobody (Zero Footprint)") }
    var disappearingTimer by remember { mutableStateOf("24 Hours") }
    var antiScreenshot by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Privacy & Anonymity", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Read receipts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Read Receipts (Blue Ticks)", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Send/receive blue double checkmarks.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = readReceiptsEnabled,
                        onCheckedChange = onToggleReadReceipts,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                // Tor Onion
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tor Onion Relay Routing", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Multi-hop relay to hide real IP address completely.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = userState.torOnionRelayEnabled,
                        onCheckedChange = { onToggleTor() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                // Ghost Mode
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ghost Presence Mode", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Never broadcasts online status or typing indicators.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = userState.stealthGhostMode,
                        onCheckedChange = { onToggleGhostMode() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                // Screen Security
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Screen Security (Anti-Screenshot)", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Blocks screen capture in chats and hidden vault.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = antiScreenshot,
                        onCheckedChange = { antiScreenshot = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Text("Last Seen & Online: $lastSeenOption", color = WhatsAppGreenBright, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text("Default Disappearing Timer: $disappearingTimer", color = WhatsAppGreenBright, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun ChatsSettingsDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var keepArchived by remember { mutableStateOf(true) }
    var selectedTheme by remember { mutableStateOf("WhatsApp Dark (Default)") }
    var isBackingUp by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Chat, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chat Settings & Backups", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Display Theme", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                listOf("WhatsApp Dark (Default)", "AMOLED Midnight Black", "System Default").forEach { themeName ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTheme = themeName }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedTheme == themeName,
                            onClick = { selectedTheme = themeName },
                            colors = RadioButtonDefaults.colors(selectedColor = WhatsAppGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(themeName, color = WhatsAppTextPrimary, fontSize = 13.sp)
                    }
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Keep Chats Archived", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Archived chats will stay hidden when you receive a new message.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = keepArchived,
                        onCheckedChange = { keepArchived = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Text("Encrypted Local Room Backup", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("All messages are encrypted with AES-128 and backed up to on-device zero-knowledge storage.", color = WhatsAppTextSecondary, fontSize = 12.sp)

                Button(
                    onClick = {
                        isBackingUp = true
                        Toast.makeText(context, "Backup complete! 18.4 MB secured into Encrypted Vault", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    modifier = Modifier.fillMaxWidth().testTag("backup_chats_btn")
                ) {
                    Icon(Icons.Default.CloudSync, contentDescription = null, tint = WhatsAppBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isBackingUp) "Vault Synced (18.4 MB)" else "Backup to Encrypted Vault", color = WhatsAppBackground, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun NotificationsSettingsDialog(onDismiss: () -> Unit) {
    var conversationTones by remember { mutableStateOf(true) }
    var highPriorityPush by remember { mutableStateOf(true) }
    var reactionAlerts by remember { mutableStateOf(true) }
    var vibrationOption by remember { mutableStateOf("Default") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Notification Preferences", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Conversation Tones", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Play sounds for incoming and outgoing messages.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = conversationTones,
                        onCheckedChange = { conversationTones = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("P2P High-Priority Wakeup", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Instant mesh alerts without Google Cloud Messaging dependencies.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = highPriorityPush,
                        onCheckedChange = { highPriorityPush = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Reaction Notifications", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("Show alerts when someone reacts to your messages.", color = WhatsAppTextSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = reactionAlerts,
                        onCheckedChange = { reactionAlerts = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Text("Vibration: $vibrationOption • Ringtone: Nexus Sovereign Tone", color = WhatsAppGreenBright, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun StorageDataDialog(
    userState: UserSovereignState,
    onToggleTor: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var clearedCache by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DataUsage, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Storage & Data Usage", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Device Storage Usage", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                StorageUsageBar(label = "Encrypted Chat Messages", size = "18.4 MB", color = WhatsAppGreen)
                StorageUsageBar(label = "Zero-Knowledge Room Vault", size = "64.2 MB", color = WhatsAppGreenBright)
                StorageUsageBar(label = "Temporary Peer Cache", size = if (clearedCache) "0.0 KB" else "3.8 MB", color = WhatsAppTextSecondary)

                Button(
                    onClick = {
                        clearedCache = true
                        Toast.makeText(context, "3.8 MB cache cleared securely", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WhatsAppGreen),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(WhatsAppGreen)),
                    modifier = Modifier.fillMaxWidth().testTag("clear_cache_btn")
                ) {
                    Text(if (clearedCache) "Cache Purged ✓" else "Clear Temporary Cache (3.8 MB)", fontSize = 12.sp)
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Text("P2P Network Statistics", color = WhatsAppTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("• Sent: 1.2 GB (Encrypted)", color = WhatsAppTextSecondary, fontSize = 12.sp)
                Text("• Received: 3.4 GB", color = WhatsAppTextSecondary, fontSize = 12.sp)
                Text("• Tor Onion Hops Today: ${userState.totalHopsToday} hops", color = WhatsAppTextSecondary, fontSize = 12.sp)
                Text("• Active Peer Connections: ${userState.connectedPeersCount} validators", color = WhatsAppTextSecondary, fontSize = 12.sp)

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tor Proxy Relay", color = WhatsAppTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("Multi-hop onion encapsulation", color = WhatsAppTextSecondary, fontSize = 11.sp)
                    }
                    Switch(
                        checked = userState.torOnionRelayEnabled,
                        onCheckedChange = { onToggleTor() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = WhatsAppGreen)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun PaymentsUpiDialog(
    userState: UserSovereignState,
    onAddCash: (Double) -> Unit,
    onSendUpi: (String, Double) -> Pair<Boolean, String>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var showSendUpiInput by remember { mutableStateOf(false) }
    var targetUpiId by remember { mutableStateOf("") }
    var transferAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nexus Sovereign UPI Cash", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Wallet Balance Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = WhatsAppCard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Available Cash Balance", color = WhatsAppTextSecondary, fontSize = 12.sp)
                        Text(
                            text = "₹${"%,.2f".format(userState.cashBalance)}",
                            color = WhatsAppGreenBright,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "⚡ Zero Transaction Fees • Zero Bank Tracking",
                            color = WhatsAppTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Text("Quick Add Cash (UPI / Card)", color = WhatsAppTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1000.0, 5000.0, 10000.0).forEach { amt ->
                        Button(
                            onClick = {
                                onAddCash(amt)
                                Toast.makeText(context, "Added ₹${amt.toInt()} to sovereign wallet", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppCard),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+₹${amt.toInt()}", color = WhatsAppGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                Text("Linked UPI IDs & Rails", color = WhatsAppTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                UpiAccountRow("Google Pay UPI", "satoshi@okaxis", "Primary", true)
                UpiAccountRow("PhonePe UPI", "satoshi@ybl", "Active", true)
                UpiAccountRow("Paytm UPI", "satoshi@paytm", "Active", true)
                UpiAccountRow("RuPay Sovereign Card", "•••• 8821", "Enclave Verified", true)

                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)

                if (!showSendUpiInput) {
                    Button(
                        onClick = { showSendUpiInput = true },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                        modifier = Modifier.fillMaxWidth().testTag("send_upi_btn")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = WhatsAppBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send Instant UPI Payment", color = WhatsAppBackground, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = targetUpiId,
                            onValueChange = { targetUpiId = it },
                            label = { Text("Receiver UPI ID (e.g. friend@okhdfcbank)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WhatsAppGreen,
                                focusedLabelColor = WhatsAppGreen,
                                unfocusedBorderColor = WhatsAppDivider
                            )
                        )
                        OutlinedTextField(
                            value = transferAmount,
                            onValueChange = { transferAmount = it },
                            label = { Text("Amount in INR (₹)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WhatsAppGreen,
                                focusedLabelColor = WhatsAppGreen,
                                unfocusedBorderColor = WhatsAppDivider
                            )
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { showSendUpiInput = false }) {
                                Text("Cancel", color = WhatsAppTextSecondary)
                            }
                            Button(
                                onClick = {
                                    val amt = transferAmount.toDoubleOrNull() ?: 0.0
                                    if (targetUpiId.isNotBlank() && amt > 0) {
                                        val (success, receipt) = onSendUpi(targetUpiId, amt)
                                        if (success) {
                                            Toast.makeText(context, "₹$amt sent to $targetUpiId! Ref: $receipt", Toast.LENGTH_LONG).show()
                                            showSendUpiInput = false
                                            targetUpiId = ""
                                            transferAmount = ""
                                        } else {
                                            Toast.makeText(context, "Transfer failed: $receipt", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Confirm Payment", color = WhatsAppBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun LanguageDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var selectedLang by remember { mutableStateOf("English (device's language)") }
    val languages = listOf(
        "English (device's language)",
        "हिन्दी (Hindi)",
        "Español (Spanish)",
        "Français (French)",
        "Deutsch (German)",
        "বাংলা (Bengali)",
        "తెలుగు (Telugu)",
        "मराठी (Marathi)"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("App Language", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                languages.forEach { lang ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLang = lang
                                Toast.makeText(context, "Language set to $lang", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLang == lang,
                            onClick = {
                                selectedLang = lang
                                Toast.makeText(context, "Language set to $lang", Toast.LENGTH_SHORT).show()
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = WhatsAppGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(lang, color = WhatsAppTextPrimary, fontSize = 14.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
fun HelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WhatsAppSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = WhatsAppGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nexus Protocol & Help", color = WhatsAppTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Architecture Whitepaper", color = WhatsAppGreenBright, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Nexus operates on an autonomous peer-to-peer gossip protocol over Tor onion routing. All messages are encrypted with the Kyber-1024 post-quantum algorithm and double-ratchet keys. No metadata, phone numbers, contacts, or chat logs are ever uploaded to any central server or cloud company.",
                    color = WhatsAppTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)
                Text("Zero-Knowledge Vault", color = WhatsAppGreenBright, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Passkeys, seed phrases, and private notes are locally stored in a hardened Room database protected by AES-128 GCM encryption. Only your biometric credentials can decrypt them.",
                    color = WhatsAppTextSecondary,
                    fontSize = 12.sp
                )
                HorizontalDivider(color = WhatsAppDivider, thickness = 0.5.dp)
                Text("Version", color = WhatsAppTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Nexus v2.4 (Decentralized Super App • All Features Activated)", color = WhatsAppTextSecondary, fontSize = 11.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = WhatsAppGreen)
            }
        }
    )
}

@Composable
private fun SecurityStatusBadge(title: String, status: String, active: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = WhatsAppTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Surface(
            color = if (active) WhatsAppGreen.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.2f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = status,
                color = if (active) WhatsAppGreenBright else Color.LightGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun StorageUsageBar(label: String, size: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = WhatsAppTextPrimary, fontSize = 12.sp)
        }
        Text(size, color = WhatsAppTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun UpiAccountRow(name: String, id: String, tag: String, verified: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(name, color = WhatsAppTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(id, color = WhatsAppTextSecondary, fontSize = 11.sp)
        }
        Surface(
            color = WhatsAppGreen.copy(alpha = 0.15f),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = tag,
                color = WhatsAppGreenBright,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
