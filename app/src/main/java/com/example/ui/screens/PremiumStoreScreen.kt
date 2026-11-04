package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CashTransaction
import com.example.data.model.PremiumProduct
import com.example.data.model.UserSovereignState
import com.example.ui.components.NexusTopBar
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PremiumStoreScreen(
    userState: UserSovereignState,
    products: List<PremiumProduct>,
    transactions: List<CashTransaction>,
    selectedCurrency: String = "INR",
    onCurrencyChange: (String) -> Unit,
    onSelectProduct: (PremiumProduct) -> Unit,
    onViewReceipt: (CashTransaction) -> Unit,
    onTipCreator: (handle: String) -> Unit,
    onOpenVault: () -> Unit
) {
    val isInr = selectedCurrency == "INR"
    val symbol = if (isInr) "₹" else "$"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusObsidian)
    ) {
        NexusTopBar(
            title = "Payments & Store",
            subtitle = "UPI & CASH RAILS • NO AD-TRACKING",
            userState = userState,
            onOpenVault = onOpenVault
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Currency Selector Bar (Indian Rupee INR & USD)
            item {
                Surface(
                    color = NexusSlateDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "STORE CURRENCY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexusTextTertiary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = if (isInr) "UPI & RuPay Enabled" else "Global USD Rails",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NexusEmerald
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // INR Button
                            Surface(
                                color = if (isInr) NexusCyan else NexusSlateCard,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isInr) NexusCyan else NexusCardBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onCurrencyChange("INR") }
                                    .testTag("currency_inr_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "🇮🇳 Indian Rupee (₹ INR)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isInr) NexusObsidian else NexusTextPrimary
                                    )
                                }
                            }

                            // USD Button
                            Surface(
                                color = if (!isInr) NexusCyan else NexusSlateCard,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (!isInr) NexusCyan else NexusCardBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onCurrencyChange("USD") }
                                    .testTag("currency_usd_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "🇺🇸 US Dollar ($ USD)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (!isInr) NexusObsidian else NexusTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Anti-Meta Monetization Manifesto Card
            item {
                Surface(
                    color = NexusSlateDark,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = null,
                                tint = NexusCyan,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Why Real Cash Instead of Free-with-Ads?",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NexusTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Meta offers 'free' apps by harvesting your personal chats, photos, location, and social contacts to build psychological advertising profiles. Nexus charges transparent cash fees for high-performance privacy infrastructure so you remain the sovereign customer, NEVER the product.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NexusTextSecondary
                        )
                    }
                }
            }

            // Current Subscription Status
            item {
                Surface(
                    color = if (userState.isProSubscriber) NexusEmerald.copy(alpha = 0.12f) else NexusSlateDark,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (userState.isProSubscriber) NexusEmerald else NexusCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "CURRENT MEMBERSHIP TIER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userState.isProSubscriber) NexusEmerald else NexusTextTertiary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (userState.isProSubscriber) "Nexus Sovereign Pro (Active)" else "Nexus Community (Free Tier)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NexusTextPrimary
                            )
                            Text(
                                text = if (userState.isProSubscriber) "Kyber-1024 Post-Quantum Active" else "Basic E2EE double-ratchet enabled",
                                fontSize = 11.sp,
                                color = NexusTextSecondary
                            )
                        }

                        if (userState.isProSubscriber) {
                            Surface(
                                color = NexusEmerald,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NexusObsidian, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("PAID", color = NexusObsidian, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Products Header
            item {
                Text(
                    text = if (isInr) "SOVEREIGN REAL CASH TIERS (INDIAN RUPEES ₹)" else "SOVEREIGN REAL CASH TIERS (USD $)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexusTextTertiary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // Product Cards
            items(products, key = { it.id }) { product ->
                PremiumProductCard(
                    product = product,
                    currencyCode = selectedCurrency,
                    isActive = userState.activeAddons.contains(product.id) || (product.id.contains("sovereign") && userState.isProSubscriber),
                    onSelect = { onSelectProduct(product) }
                )
            }

            // Direct Creator Cash Support
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = NexusSlateDark,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NexusEmerald.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, tint = NexusEmerald, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isInr) "Direct Creator Cash Support (0% Cut via UPI)" else "Direct Creator Cash Support (0% Meta Cut)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = NexusTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isInr)
                                "Send direct Indian Rupee tips to creators via UPI (Google Pay, PhonePe, Paytm, BHIM) or RuPay cards with zero platform deduction."
                            else
                                "Send real cash tips to privacy advocates and open-source researchers directly via Google Play or Cash ACH with 0% platform extraction.",
                            fontSize = 12.sp,
                            color = NexusTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onTipCreator("@elena.nexus") },
                                colors = ButtonDefaults.buttonColors(containerColor = NexusEmerald, contentColor = NexusObsidian),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tip_elena_btn")
                            ) {
                                Text(
                                    text = if (isInr) "Tip @elena (₹ INR)" else "Tip @elena.nexus",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { onTipCreator("@marcus.p2p") },
                                colors = ButtonDefaults.buttonColors(containerColor = NexusSlateCard, contentColor = NexusCyan),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NexusCyan),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tip_marcus_btn")
                            ) {
                                Text(
                                    text = if (isInr) "Tip @marcus (₹ INR)" else "Tip @marcus.p2p",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Past Cash Transactions & Receipts
            if (transactions.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ITEMIZED DIGITAL CASH RECEIPTS (${transactions.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexusTextTertiary,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                items(transactions, key = { it.id }) { tx ->
                    Surface(
                        color = NexusSlateDark,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NexusCardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewReceipt(tx) }
                            .testTag("receipt_${tx.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = NexusCyan, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(tx.title, fontWeight = FontWeight.Bold, color = NexusTextPrimary, fontSize = 13.sp, maxLines = 1)
                                    val dateStr = remember(tx.timestamp) {
                                        SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date(tx.timestamp))
                                    }
                                    Text("$dateStr • ${tx.paymentMethod}", fontSize = 10.sp, color = NexusTextSecondary, fontFamily = FontFamily.Monospace)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("${tx.currencySymbol}${"%.2f".format(tx.amount)} ${tx.currencyCode}", fontWeight = FontWeight.Bold, color = NexusEmerald, fontSize = 13.sp)
                                Text("View Receipt", color = NexusCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumProductCard(
    product: PremiumProduct,
    currencyCode: String = "INR",
    isActive: Boolean,
    onSelect: () -> Unit
) {
    val isInr = currencyCode == "INR"
    val symbol = if (isInr) "₹" else "$"
    val price = if (isInr) product.priceInr else product.priceUsd
    val billing = if (isInr) product.billingTypeInr else product.billingType
    val borderColor = if (isActive) NexusEmerald else if (product.isFeatured) NexusCyan else NexusCardBorder

    Surface(
        color = NexusSlateDark,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(if (product.isFeatured || isActive) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (isActive) NexusEmerald.copy(alpha = 0.2f) else NexusCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isActive) NexusEmerald else NexusCyan)
                ) {
                    Text(
                        text = if (isActive) "ACTIVATED" else product.badge,
                        color = if (isActive) NexusEmerald else NexusCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$symbol${"%.2f".format(price)}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = NexusEmerald
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currencyCode,
                        fontSize = 11.sp,
                        color = NexusTextSecondary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NexusTextPrimary
            )

            Text(
                text = "$billing • ${product.tagline}",
                style = MaterialTheme.typography.bodySmall,
                color = NexusTextSecondary
            )

            HorizontalDivider(color = NexusCardBorder, modifier = Modifier.padding(vertical = 10.dp))

            product.features.forEach { feat ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NexusCyan, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = feat, fontSize = 12.sp, color = NexusTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) NexusSlateCard else NexusCyan,
                    contentColor = if (isActive) NexusEmerald else NexusObsidian
                ),
                shape = RoundedCornerShape(10.dp),
                border = if (isActive) androidx.compose.foundation.BorderStroke(1.dp, NexusEmerald) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("buy_btn_${product.id}")
            ) {
                if (isActive) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Current Plan Active", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                } else {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Subscribe for $symbol${"%.2f".format(price)} $currencyCode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
