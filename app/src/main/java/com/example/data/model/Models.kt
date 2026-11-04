package com.example.data.model

data class ChatMessage(
    val id: String,
    val threadId: String,
    val senderName: String,
    val senderHandle: String,
    val text: String,
    val timestamp: Long,
    val isMine: Boolean,
    val isEncrypted: Boolean = true,
    val ratchetKeyFingerprint: String = "4F8A:29C1:B76D:90EE",
    val ephemeralTimerSeconds: Int = 0, // 0 = permanent
    val expiresAt: Long = 0L,
    val isAudio: Boolean = false,
    val audioDurationSeconds: Int = 0,
    val isBurned: Boolean = false,
    val isDelivered: Boolean = true,
    val isRead: Boolean = true
)

data class ChatThread(
    val id: String,
    val contactName: String,
    val contactHandle: String,
    val avatarSeed: String,
    val lastMessage: String,
    val lastTimestamp: Long,
    val unreadCount: Int = 0,
    val isGroup: Boolean = false,
    val ratchetVerified: Boolean = true,
    val safetyFingerprint: String = "7381 9283 1049 8274 0192 4821",
    val ephemeralSettingSeconds: Int = 0, // 0 = off, 10, 60, 3600, 86400
    val isOnline: Boolean = true,
    val meshHopCount: Int = 2,
    val isArchived: Boolean = false,
    val isLocked: Boolean = false
)

data class NexusPost(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val avatarSeed: String,
    val timestamp: Long,
    val content: String,
    val mediaGradient: String = "cyan_violet", // visual media presentation
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLiked: Boolean = false,
    val antiAiWatermark: Boolean = true,
    val exifPurged: Boolean = true,
    val zkpVerified: Boolean = true,
    val meshNodesRelayed: Int = 18,
    val creatorTipAddress: String = "nexus:0x9B...3F4"
)

data class NexusStory(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val avatarSeed: String,
    val caption: String,
    val timestamp: Long,
    val hasViewed: Boolean = false,
    val styleSeed: String = "matrix"
)

data class NexusCircle(
    val id: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val category: String,
    val tag: String,
    val isEncrypted: Boolean = true,
    val nodeHealth: String = "99.98% P2P"
)

data class CirclePost(
    val id: String,
    val circleId: String,
    val authorName: String,
    val authorHandle: String,
    val content: String,
    val timestamp: Long,
    val upvotes: Int,
    val repliesCount: Int,
    val isUpvoted: Boolean = false
)

data class PeerNode(
    val id: String,
    val name: String,
    val location: String,
    val ipMasked: String,
    val pingMs: Int,
    val protocol: String,
    val isDirectRelay: Boolean
)

data class UserSovereignState(
    val didHandle: String = "@satoshi_prime",
    val didId: String = "did:nexus:z6MkuT8gA9L...7c8P",
    val kyberKey: String = "Kyber1024-QResist-f93821a",
    val masterFingerprint: String = "89A2:4E51:C09D:33B8:119F:AA82",
    val isProSubscriber: Boolean = true,
    val activeProPlan: String? = "Nexus Sovereign Pro (Lifetime Enclave Activated)",
    val activeAddons: Set<String> = setOf(
        "nexus_sovereign_monthly",
        "nexus_sovereign_yearly",
        "satellite_mesh_pass",
        "hardware_enclave_key",
        "kyber_1024",
        "tor_onion",
        "stealth_ghost",
        "hardware_enclave",
        "zk_vault",
        "anti_ai_watermark",
        "quantum_mesh"
    ),
    val cashBalance: Double = 25000.00,
    val connectedPeersCount: Int = 128,
    val totalHopsToday: Int = 4820,
    val stealthGhostMode: Boolean = true,
    val torOnionRelayEnabled: Boolean = true,
    val quantumProtectionActive: Boolean = true
)

data class PremiumProduct(
    val id: String,
    val title: String,
    val priceUsd: Double,
    val priceInr: Double,
    val billingType: String, // "per month", "per year", "one-time"
    val billingTypeInr: String = billingType,
    val badge: String,
    val tagline: String,
    val features: List<String>,
    val isFeatured: Boolean = false
)

data class CashTransaction(
    val id: String,
    val title: String,
    val amount: Double,
    val currencyCode: String = "INR",
    val currencySymbol: String = "₹",
    val timestamp: Long,
    val status: String = "Completed",
    val receiptId: String,
    val paymentMethod: String = "UPI (Google Pay / PhonePe)"
)

data class VaultItem(
    val id: String,
    val title: String,
    val category: String, // "CREDENTIAL", "NOTE", "KEY"
    val identifier: String,
    val plainSecret: String,
    val notes: String,
    val timestamp: Long
)
