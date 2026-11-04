package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CashTransaction
import com.example.data.model.PremiumProduct
import com.example.data.model.UserSovereignState
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NexusTopBar(
    title: String,
    subtitle: String? = null,
    userState: UserSovereignState,
    onOpenVault: () -> Unit,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    Surface(
        color = WhatsAppSurface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (navigationIcon != null) {
                    navigationIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = WhatsAppGreen
                        )
                        if (userState.isProSubscriber) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = WhatsAppGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "PRO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WhatsAppGreenBright,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = WhatsAppTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Security indicator pill
                Surface(
                    color = WhatsAppBubbleReceived,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .clickable { onOpenVault() }
                        .testTag("topbar_vault_indicator")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (userState.torOnionRelayEnabled) WhatsAppGreenBright else NexusRose)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${userState.connectedPeersCount} P2P",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = WhatsAppTextSecondary
                        )
                    }
                }

                if (actions != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    actions()
                }
            }
        }
    }
}

@Composable
fun NexusAvatar(
    seed: String,
    size: Int = 44,
    showOnline: Boolean = false,
    modifier: Modifier = Modifier
) {
    val gradient = remember(seed) {
        when (seed.hashCode() % 4) {
            0 -> Brush.linearGradient(listOf(NexusCyan, NexusViolet))
            1 -> Brush.linearGradient(listOf(NexusViolet, NexusRose))
            2 -> Brush.linearGradient(listOf(NexusEmerald, NexusCyan))
            else -> Brush.linearGradient(listOf(NexusAmberGold, NexusRose))
        }
    }

    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(gradient)
                .border(1.5.dp, NexusCardBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val initial = seed.replace("@", "").take(1).uppercase()
            Text(
                text = if (initial.isNotBlank()) initial else "N",
                fontWeight = FontWeight.Bold,
                fontSize = (size / 2.3).sp,
                color = Color.White
            )
        }

        if (showOnline) {
            Box(
                modifier = Modifier
                    .size((size * 0.3).dp)
                    .clip(CircleShape)
                    .background(NexusEmerald)
                    .border(1.5.dp, NexusObsidian, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun AudioWaveformView(
    durationSeconds: Int,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit
) {
    Surface(
        color = NexusSlateCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTogglePlay() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = onTogglePlay,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = NexusCyan,
                    contentColor = NexusObsidian
                ),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play voice memo"
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Simulated dynamic waveform bars
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val heights = listOf(14, 22, 30, 18, 36, 26, 12, 28, 34, 16, 24, 38, 20, 14, 28, 32, 10)
                heights.forEachIndexed { index, h ->
                    val barColor = if (index < heights.size / 2) NexusCyan else NexusTextSecondary.copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(h.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(barColor)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "0:0$durationSeconds",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                color = NexusCyan
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashCheckoutSheet(
    product: PremiumProduct,
    currencyCode: String = "INR",
    onDismiss: () -> Unit,
    onConfirmPurchase: (product: PremiumProduct, paymentMethod: String, currencyCode: String) -> Unit
) {
    val isInr = currencyCode == "INR"
    val symbol = if (isInr) "₹" else "$"
    val price = if (isInr) product.priceInr else product.priceUsd
    val billing = if (isInr) product.billingTypeInr else product.billingType

    var selectedPaymentMethod by remember(currencyCode) {
        mutableStateOf(if (isInr) "Instant UPI (GPay / PhonePe / Paytm)" else "Google Play (USD)")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = NexusSlateDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = NexusCardBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Surface(
                        color = NexusCyan.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusCyan)
                    ) {
                        Text(
                            text = product.badge,
                            color = NexusCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = NexusTextPrimary
                    )
                }

                Text(
                    text = "$symbol${"%.2f".format(price)}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = NexusEmerald
                    )
                )
            }

            Text(
                text = "${product.tagline} • $billing",
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextSecondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            HorizontalDivider(color = NexusCardBorder, modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "INCLUDED SOVEREIGN CAPABILITIES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NexusTextTertiary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            product.features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = NexusCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NexusTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isInr) "SELECT INDIAN CASH PAYMENT METHOD (INR ₹)" else "SELECT REAL CASH PAYMENT METHOD (USD $)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NexusTextTertiary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            val methods = if (isInr) {
                listOf(
                    "Instant UPI (GPay / PhonePe / Paytm)" to Icons.Default.QrCode,
                    "RuPay / Indian Debit & Credit Cards" to Icons.Default.CreditCard,
                    "Net Banking (SBI / HDFC / ICICI / Axis)" to Icons.Default.AccountBalance,
                    "Google Play Balance (₹ INR)" to Icons.Default.Payment
                )
            } else {
                listOf(
                    "Google Play (USD)" to Icons.Default.Payment,
                    "Credit Card / Debit (USD)" to Icons.Default.CreditCard,
                    "Cash Balance / Instant ACH" to Icons.Default.AccountBalanceWallet
                )
            }

            methods.forEach { (method, icon) ->
                val isSelected = selectedPaymentMethod == method
                Surface(
                    color = if (isSelected) NexusSlateCard else NexusObsidian,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) NexusCyan else NexusCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedPaymentMethod = method }
                        .testTag("payment_method_${method.take(6)}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedPaymentMethod = method },
                            colors = RadioButtonDefaults.colors(selectedColor = NexusCyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = icon, contentDescription = null, tint = if (isSelected) NexusCyan else NexusTextSecondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = method,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = NexusTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Breakdown
            Surface(
                color = NexusObsidian,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", color = NexusTextSecondary, fontSize = 12.sp)
                        Text("$symbol${"%.2f".format(price)} $currencyCode", color = NexusTextPrimary, fontSize = 12.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Decentralized Network Fee", color = NexusTextSecondary, fontSize = 12.sp)
                        Text("${symbol}0.00 (Zero Cut)", color = NexusEmerald, fontSize = 12.sp)
                    }
                    HorizontalDivider(color = NexusCardBorder, modifier = Modifier.padding(vertical = 6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Due", color = NexusTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("$symbol${"%.2f".format(price)} $currencyCode", color = NexusEmerald, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { onConfirmPurchase(product, selectedPaymentMethod, currencyCode) },
                colors = ButtonDefaults.buttonColors(containerColor = NexusCyan, contentColor = NexusObsidian),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("checkout_pay_button")
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pay $symbol${"%.2f".format(price)} $currencyCode Now",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorTipSheet(
    targetHandle: String,
    currencyCode: String = "INR",
    onDismiss: () -> Unit,
    onSendTip: (handle: String, amount: Double, currencyCode: String, paymentMethod: String) -> Unit
) {
    val isInr = currencyCode == "INR"
    val symbol = if (isInr) "₹" else "$"
    val amounts = if (isInr) listOf(49.0, 99.0, 199.0, 499.0, 999.0) else listOf(1.0, 3.0, 5.0, 10.0, 25.0)
    var selectedAmount by remember(currencyCode) { mutableStateOf(if (isInr) 99.0 else 5.0) }
    var selectedPaymentMethod by remember(currencyCode) {
        mutableStateOf(if (isInr) "Instant UPI (GPay / PhonePe)" else "Google Play (USD)")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = NexusSlateDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = NexusCardBorder) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = if (isInr) "Direct Indian Cash Tip (₹ INR)" else "Direct Cash Tip ($ USD)",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = NexusTextPrimary
            )
            Text(
                text = "Send real cash directly to $targetHandle via ${if (isInr) "UPI with 0% platform fee" else "Google Play"}. Unlike Meta's 30%-50% cuts, Nexus protocol passes 100% directly to the creator.",
                style = MaterialTheme.typography.bodyMedium,
                color = NexusTextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Text(
                text = "SELECT TIP AMOUNT ($currencyCode $symbol)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NexusTextTertiary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                amounts.forEach { amt ->
                    val isSelected = selectedAmount == amt
                    Surface(
                        color = if (isSelected) NexusCyan else NexusSlateCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NexusCyan else NexusCardBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedAmount = amt }
                            .testTag("tip_amount_${amt.toInt()}")
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$symbol${amt.toInt()}",
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NexusObsidian else NexusTextPrimary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onSendTip(targetHandle, selectedAmount, currencyCode, selectedPaymentMethod) },
                colors = ButtonDefaults.buttonColors(containerColor = NexusEmerald, contentColor = NexusObsidian),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("send_cash_tip_button")
            ) {
                Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tip $symbol${"%.2f".format(selectedAmount)} $currencyCode to $targetHandle",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun ReceiptDialog(
    transaction: CashTransaction,
    onDismiss: () -> Unit
) {
    val dateStr = remember(transaction.timestamp) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(transaction.timestamp))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NexusSlateCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NexusEmerald, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Transaction Receipt", color = NexusTextPrimary, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    color = NexusObsidian,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Receipt #", color = NexusTextSecondary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            Text(transaction.receiptId, color = NexusCyan, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Date & Time", color = NexusTextSecondary, fontSize = 12.sp)
                            Text(dateStr, color = NexusTextPrimary, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Payment Rail", color = NexusTextSecondary, fontSize = 12.sp)
                            Text(transaction.paymentMethod, color = NexusTextPrimary, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Status", color = NexusTextSecondary, fontSize = 12.sp)
                            Text(transaction.status, color = NexusEmerald, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        HorizontalDivider(color = NexusCardBorder, modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Item", color = NexusTextSecondary, fontSize = 13.sp)
                            Text(transaction.title, color = NexusTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Paid (${transaction.currencyCode})", color = NexusTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("${transaction.currencySymbol}${"%.2f".format(transaction.amount)}", color = NexusEmerald, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Receipt signed cryptographically. Stored in your zero-knowledge on-device ledger.",
                    fontSize = 11.sp,
                    color = NexusTextTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NexusCyan, contentColor = NexusObsidian)
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    )
}
