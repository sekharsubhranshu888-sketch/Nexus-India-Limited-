package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.security.SecureRandom

@Composable
fun AddSecretDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, category: String, identifier: String, secret: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("LOGIN") } // "LOGIN", "NOTE", "SEED"
    var identifier by remember { mutableStateOf("") }
    var secret by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showSecretPlain by remember { mutableStateOf(true) }

    fun generateRandomPassword() {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*+?"
        val rand = SecureRandom()
        val generated = (1..18).map { chars[rand.nextInt(chars.length)] }.joinToString("")
        secret = generated
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NexusSlateDark,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = NexusCyan, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Zero-Knowledge Vault", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = NexusTextPrimary)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "SELECT SECRET TYPE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexusTextTertiary,
                    letterSpacing = 1.sp
                )

                // Category selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val categories = listOf(
                        "LOGIN" to "🔑 Login",
                        "NOTE" to "📝 Note",
                        "SEED" to "🌱 Seed"
                    )
                    categories.forEach { (catKey, catLabel) ->
                        val isSelected = selectedCategory == catKey
                        Surface(
                            color = if (isSelected) NexusCyan else NexusSlateCard,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NexusCyan else NexusCardBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCategory = catKey }
                                .testTag("cat_select_$catKey")
                        ) {
                            Text(
                                text = catLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NexusObsidian else NexusTextPrimary,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title / Service Name", color = NexusTextSecondary, fontSize = 12.sp) },
                    placeholder = { Text(if (selectedCategory == "LOGIN") "e.g. ProtonMail, GitHub" else if (selectedCategory == "NOTE") "e.g. Master Recovery Note" else "e.g. Hardware Seed Phrase", color = NexusTextTertiary, fontSize = 12.sp) },
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
                        .testTag("vault_input_title"),
                    singleLine = true
                )

                // Identifier
                OutlinedTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    label = { Text(if (selectedCategory == "LOGIN") "Username / Email" else if (selectedCategory == "NOTE") "Subject / Header" else "Key Label", color = NexusTextSecondary, fontSize = 12.sp) },
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
                        .testTag("vault_input_identifier"),
                    singleLine = true
                )

                // Secret payload
                OutlinedTextField(
                    value = secret,
                    onValueChange = { secret = it },
                    label = { Text(if (selectedCategory == "LOGIN") "Password / Secret" else if (selectedCategory == "NOTE") "Confidential Note Text" else "12/24 Word Seed Phrase", color = NexusTextSecondary, fontSize = 12.sp) },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (selectedCategory == "LOGIN") {
                                IconButton(onClick = { generateRandomPassword() }) {
                                    Icon(imageVector = Icons.Default.Casino, contentDescription = "Generate Password", tint = NexusCyan, modifier = Modifier.size(18.dp))
                                }
                            }
                            IconButton(onClick = { showSecretPlain = !showSecretPlain }) {
                                Icon(
                                    imageVector = if (showSecretPlain) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Visibility",
                                    tint = NexusTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
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
                        .testTag("vault_input_secret"),
                    maxLines = 4
                )

                // Optional notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Optional Notes", color = NexusTextSecondary, fontSize = 12.sp) },
                    placeholder = { Text("e.g. 2FA backup codes or URL", color = NexusTextTertiary, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NexusSlateCard,
                        unfocusedContainerColor = NexusSlateCard,
                        focusedBorderColor = NexusCyan,
                        unfocusedBorderColor = NexusCardBorder,
                        focusedTextColor = NexusTextPrimary,
                        unfocusedTextColor = NexusTextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Surface(
                    color = NexusObsidian,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.EnhancedEncryption, contentDescription = null, tint = NexusEmerald, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Secret is encrypted with AES-128 on device before saving to Room DB.",
                            fontSize = 10.sp,
                            color = NexusTextSecondary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && secret.isNotBlank()) {
                        onSave(title, selectedCategory, identifier, secret, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NexusCyan, contentColor = NexusObsidian),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("vault_save_btn")
            ) {
                Text("Encrypt & Save", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = NexusTextSecondary)
            }
        }
    )
}
