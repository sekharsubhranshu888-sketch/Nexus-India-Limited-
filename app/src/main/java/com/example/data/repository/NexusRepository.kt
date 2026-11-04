package com.example.data.repository

import android.content.Context
import com.example.data.local.NexusDatabase
import com.example.data.local.StoredChatMessage
import com.example.data.local.StoredNexusPost
import com.example.data.local.StoredCashTransaction
import com.example.data.local.StoredVaultSecret
import com.example.data.security.VaultCrypto
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class NexusRepository(context: Context) {
    private val database = NexusDatabase.getDatabase(context)
    private val dao = database.nexusDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    // In-memory state for fast reactive Compose UI combined with Room persistence
    private val _threads = MutableStateFlow<List<ChatThread>>(emptyList())
    val threads: StateFlow<List<ChatThread>> = _threads.asStateFlow()

    private val _currentMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val currentMessages: StateFlow<List<ChatMessage>> = _currentMessages.asStateFlow()

    private val _posts = MutableStateFlow<List<NexusPost>>(emptyList())
    val posts: StateFlow<List<NexusPost>> = _posts.asStateFlow()

    private val _stories = MutableStateFlow<List<NexusStory>>(emptyList())
    val stories: StateFlow<List<NexusStory>> = _stories.asStateFlow()

    private val _circles = MutableStateFlow<List<NexusCircle>>(emptyList())
    val circles: StateFlow<List<NexusCircle>> = _circles.asStateFlow()

    private val _circlePosts = MutableStateFlow<Map<String, List<CirclePost>>>(emptyMap())
    val circlePosts: StateFlow<Map<String, List<CirclePost>>> = _circlePosts.asStateFlow()

    private val _peerNodes = MutableStateFlow<List<PeerNode>>(emptyList())
    val peerNodes: StateFlow<List<PeerNode>> = _peerNodes.asStateFlow()

    private val _userState = MutableStateFlow(UserSovereignState())
    val userState: StateFlow<UserSovereignState> = _userState.asStateFlow()

    private val _transactions = MutableStateFlow<List<CashTransaction>>(emptyList())
    val transactions: StateFlow<List<CashTransaction>> = _transactions.asStateFlow()

    private val _vaultItems = MutableStateFlow<List<VaultItem>>(emptyList())
    val vaultItems: StateFlow<List<VaultItem>> = _vaultItems.asStateFlow()

    private val _selectedCurrency = MutableStateFlow("INR")
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()

    fun setCurrency(currency: String) {
        _selectedCurrency.value = currency
    }

    val premiumProducts = listOf(
        PremiumProduct(
            id = "nexus_sovereign_monthly",
            title = "Nexus Sovereign Pro",
            priceUsd = 9.99,
            priceInr = 499.00,
            billingType = "per month",
            billingTypeInr = "प्रति माह (per month)",
            badge = "QUANTUM SHIELD",
            tagline = "The pinnacle of zero-knowledge decentralized security",
            features = listOf(
                "Post-Quantum Kyber-1024 Ratchet Key Agreement",
                "Unlimited 4K Lossless Zero-Knowledge Vault Storage",
                "Decentralized DID Custom Domain (@yourname.nexus)",
                "Ghost Mode: 100% Zero-Presence Footprint",
                "Priority DHT Peer Routing across 100+ Validator Nodes",
                "Anti-Metadata EXIF & Facial Biometric Scrambler"
            ),
            isFeatured = true
        ),
        PremiumProduct(
            id = "nexus_sovereign_yearly",
            title = "Nexus Sovereign Annual",
            priceUsd = 89.99,
            priceInr = 3999.00,
            billingType = "per year (Save 25%)",
            billingTypeInr = "प्रति वर्ष (Save 33%)",
            badge = "BEST VALUE",
            tagline = "Full sovereign independence with lifetime cryptographic warranty",
            features = listOf(
                "All Monthly Pro Capabilities Included",
                "Cold-Storage Hardware Security Key (FIDO2) Enclave Bridge",
                "Multi-Persona Split Identity Engine (5 ratchets)",
                "Offline Low-Earth Orbit Satellite Mesh Relay Access",
                "Verified Sovereign Badge across Circles & Feeds"
            )
        ),
        PremiumProduct(
            id = "satellite_mesh_pass",
            title = "Orbital Mesh Uplink",
            priceUsd = 4.99,
            priceInr = 249.00,
            billingType = "per month",
            billingTypeInr = "प्रति माह (per month)",
            badge = "OFFLINE MESH",
            tagline = "Communicate when cellular towers and the internet fail",
            features = listOf(
                "P2P LoRa & Low-Earth Orbit Relays",
                "Sub-GHz Peer hopping without telco infrastructure",
                "Emergency SOS beacon broadcast with zero GPS metadata"
            )
        ),
        PremiumProduct(
            id = "hardware_enclave_key",
            title = "Hardware Enclave Enabler",
            priceUsd = 14.99,
            priceInr = 999.00,
            billingType = "one-time purchase",
            billingTypeInr = "एकमुश्त भुगतान (One-Time)",
            badge = "LIFETIME",
            tagline = "Bind your cryptographic keys to physical hardware modules",
            features = listOf(
                "Hardware Titan / YubiKey NFC Master Signing",
                "Zero-ram key derivation on hardware secure elements",
                "Permanent lifetime firmware updates"
            )
        )
    )

    init {
        seedInitialData()
        loadPersistedData()
    }

    private fun seedInitialData() {
        _threads.value = listOf(
            ChatThread(
                id = "thread_1",
                contactName = "Elena Vance (Cypher)",
                contactHandle = "@elena.nexus",
                avatarSeed = "elena",
                lastMessage = "🔐 Ratchet key rotated. Sent you the encrypted smart contract.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 4,
                unreadCount = 1,
                ratchetVerified = true,
                safetyFingerprint = "7381 9283 1049 8274 0192 4821",
                ephemeralSettingSeconds = 60,
                isOnline = true,
                meshHopCount = 1
            ),
            ChatThread(
                id = "thread_2",
                contactName = "Nexus Core Devs (Mesh DAO)",
                contactHandle = "circle:0xCoreDevs",
                avatarSeed = "dao",
                lastMessage = "Node v2.4 upgrade verified by 1,420 peer validators.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 32,
                unreadCount = 3,
                isGroup = true,
                ratchetVerified = true,
                safetyFingerprint = "9901 3841 8172 6384 1290 8472",
                ephemeralSettingSeconds = 0,
                isOnline = true,
                meshHopCount = 3
            ),
            ChatThread(
                id = "thread_3",
                contactName = "Marcus Zhao (Hardware Lab)",
                contactHandle = "@marcus.p2p",
                avatarSeed = "marcus",
                lastMessage = "Micro-wave transceiver test passed. Zero metadata recorded.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 120,
                unreadCount = 0,
                ratchetVerified = true,
                safetyFingerprint = "4412 8821 7391 0029 4819 3391",
                ephemeralSettingSeconds = 300,
                isOnline = false,
                meshHopCount = 2
            ),
            ChatThread(
                id = "thread_4",
                contactName = "Aria Sterling (Investigative)",
                contactHandle = "@aria_stealth",
                avatarSeed = "aria",
                lastMessage = "Burning our previous communication tunnel. Catch you on Node 7.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 360,
                unreadCount = 0,
                ratchetVerified = true,
                safetyFingerprint = "1102 9940 3381 2291 8847 1192",
                ephemeralSettingSeconds = 10,
                isOnline = true,
                meshHopCount = 1
            ),
            ChatThread(
                id = "thread_archived_1",
                contactName = "Julian (Cypherpunk Mirror)",
                contactHandle = "@julian.ipfs",
                avatarSeed = "julian",
                lastMessage = "Documents mirrored across 40 IPFS nodes with zero central points of failure.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 28,
                unreadCount = 0,
                ratchetVerified = true,
                isOnline = false,
                isArchived = true
            ),
            ChatThread(
                id = "thread_archived_2",
                contactName = "Hal Finney (Genesis Node)",
                contactHandle = "@hal.rpo",
                avatarSeed = "hal",
                lastMessage = "First reusable proof-of-work test completed. Keys safely cold-stored.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 72,
                unreadCount = 0,
                ratchetVerified = true,
                isOnline = false,
                isArchived = true
            ),
            ChatThread(
                id = "thread_locked_1",
                contactName = "Phantom Whisperer",
                contactHandle = "@phantom.covert",
                avatarSeed = "phantom",
                lastMessage = "Biometric lock verified. Hardware security module derivation is active.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 18,
                unreadCount = 2,
                ratchetVerified = true,
                ephemeralSettingSeconds = 60,
                isOnline = true,
                isLocked = true
            ),
            ChatThread(
                id = "thread_locked_2",
                contactName = "Confidential Swiss Enclave",
                contactHandle = "@swiss.enclave",
                avatarSeed = "swiss",
                lastMessage = "Node heartbeats encrypted under Kyber-1024 post-quantum key agreement.",
                lastTimestamp = System.currentTimeMillis() - 1000 * 60 * 55,
                unreadCount = 0,
                ratchetVerified = true,
                isOnline = true,
                isLocked = true
            )
        )

        _stories.value = listOf(
            NexusStory(
                id = "story_1",
                authorName = "Your DID",
                authorHandle = "@satoshi_prime",
                avatarSeed = "satoshi",
                caption = "Connected to 42 P2P relays. Zero ISP snooping.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 20,
                hasViewed = false,
                styleSeed = "matrix"
            ),
            NexusStory(
                id = "story_2",
                authorName = "Elena Vance",
                authorHandle = "@elena.nexus",
                avatarSeed = "elena",
                caption = "Say goodbye to big tech surveillance silos. Real E2EE.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                hasViewed = false,
                styleSeed = "cyan"
            ),
            NexusStory(
                id = "story_3",
                authorName = "Tor Onion Bridge",
                authorHandle = "@tor_mesh",
                avatarSeed = "tor",
                caption = "3,890 active onion circuit tunnels routing Nexus traffic.",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                hasViewed = false,
                styleSeed = "violet"
            ),
            NexusStory(
                id = "story_4",
                authorName = "ZeroKnowledge Lab",
                authorHandle = "@zk_research",
                avatarSeed = "zk",
                caption = "Succinct ZK-SNARK proofs for decentralized credentials live!",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 300,
                hasViewed = true,
                styleSeed = "emerald"
            )
        )

        _circles.value = listOf(
            NexusCircle(
                id = "circle_1",
                name = "Privacy Sovereignty & Anti-AdTech",
                description = "Challenging the Big Tech surveillance ad monopolies. Building self-hosted and decentralized social fabrics.",
                memberCount = 84200,
                category = "Privacy & Freedom",
                tag = "#Sovereignty",
                nodeHealth = "99.9% Active"
            ),
            NexusCircle(
                id = "circle_2",
                name = "Decentralized P2P Mesh Builders",
                description = "LoRa, Bluetooth mesh, and satellite low-bandwidth networking for unstoppable communications.",
                memberCount = 42100,
                category = "Infrastructure",
                tag = "#MeshNet",
                nodeHealth = "1,840 Nodes"
            ),
            NexusCircle(
                id = "circle_3",
                name = "Open Creators & Sovereign Tippers",
                description = "Monetize creative moments directly with supporters in cash and crypto without Meta's 30%-50% cut.",
                memberCount = 61900,
                category = "Creator Economy",
                tag = "#DirectEconomy",
                nodeHealth = "100% Peer-to-Peer"
            ),
            NexusCircle(
                id = "circle_4",
                name = "Post-Quantum Cryptography & Zero-Knowledge",
                description = "Implementing Kyber, Dilithium, and Groth16 cryptographic primitives for personal comms.",
                memberCount = 29500,
                category = "Security",
                tag = "#QuantumSafe",
                nodeHealth = "Audited"
            )
        )

        _circlePosts.value = mapOf(
            "circle_1" to listOf(
                CirclePost(
                    id = "cp_1",
                    circleId = "circle_1",
                    authorName = "Alex Rivera",
                    authorHandle = "@alex_cypher",
                    content = "Why Nexus replaces WhatsApp & Instagram in one stroke: Meta still extracts all your social graph metadata (who you talk to, when, from which IP, and your location). Nexus routes packets through encrypted P2P onion hops. True zero knowledge.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 90,
                    upvotes = 342,
                    repliesCount = 58
                ),
                CirclePost(
                    id = "cp_2",
                    circleId = "circle_1",
                    authorName = "Kavita Rao",
                    authorHandle = "@kavita_sec",
                    content = "Just tested the anti-AI scraping image filter on Nexus Stream. Uploaded photos scramble facial embeddings for Meta & third-party crawlers while keeping the photo crisp for friends. Huge win for digital rights!",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 240,
                    upvotes = 512,
                    repliesCount = 89
                )
            ),
            "circle_2" to listOf(
                CirclePost(
                    id = "cp_3",
                    circleId = "circle_2",
                    authorName = "Dave Miller",
                    authorHandle = "@dave_mesh",
                    content = "Setup 4 rooftop LoRa relays in Austin. Tested Nexus mesh messaging across 9.2 miles without cellular coverage. Packets delivered in 140ms.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                    upvotes = 620,
                    repliesCount = 74
                )
            )
        )

        _peerNodes.value = listOf(
            PeerNode("node_1", "Tokyo-Relay-Alpha", "Tokyo, JP", "192.88.*.*", 18, "Tor-Onion-v3", true),
            PeerNode("node_2", "Zurich-Crypto-Vault", "Zurich, CH", "104.22.*.*", 42, "Signal-Ratchet", true),
            PeerNode("node_3", "Reykjavik-Freedom", "Reykjavik, IS", "185.12.*.*", 56, "DHT-Kademlia", true),
            PeerNode("node_4", "Sing-P2P-Gateway", "Singapore, SG", "203.11.*.*", 29, "QUIC-P2P", false),
            PeerNode("node_5", "Frankfurt-Secure-01", "Frankfurt, DE", "91.198.*.*", 38, "Kyber-1024", true)
        )
    }

    private fun loadPersistedData() {
        scope.launch {
            // Load posts
            dao.getAllPosts().collect { storedList ->
                if (storedList.isEmpty()) {
                    // Populate initial default posts
                    val initialPosts = listOf(
                        StoredNexusPost(
                            id = "post_initial_1",
                            authorName = "Elena Vance",
                            authorHandle = "@elena.nexus",
                            content = "Sunset over the encrypted city. All EXIF metadata stripped, geolocation scrubbed, and AI watermarking embedded. Share your moments without becoming the product. 🌅🛡️",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                            mediaGradient = "sunset",
                            likesCount = 142,
                            commentsCount = 28,
                            isLiked = false,
                            antiAiWatermark = true,
                            exifPurged = true
                        ),
                        StoredNexusPost(
                            id = "post_initial_2",
                            authorName = "Nexus Protocol Foundation",
                            authorHandle = "@nexus_dao",
                            content = "Milestone achieved: 1,000,000 encrypted messages relayed today across 18,400 decentralized peer nodes without a single central server or database tracking your data! 🚀🔒",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 120,
                            mediaGradient = "neon_matrix",
                            likesCount = 890,
                            commentsCount = 114,
                            isLiked = true,
                            antiAiWatermark = true,
                            exifPurged = true
                        ),
                        StoredNexusPost(
                            id = "post_initial_3",
                            authorName = "Dr. Aris Thorne",
                            authorHandle = "@aris_crypto",
                            content = "Why does WhatsApp keep all metadata logs? Because in Web2, metadata is where the billions in ad-targeting come from. Nexus changes the game: zero metadata collection, sovereign keys stored only in your device enclave.",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 280,
                            mediaGradient = "deep_ocean",
                            likesCount = 635,
                            commentsCount = 87,
                            isLiked = false,
                            antiAiWatermark = true,
                            exifPurged = true
                        )
                    )
                    initialPosts.forEach { dao.insertPost(it) }
                } else {
                    _posts.value = storedList.map { stored ->
                        NexusPost(
                            id = stored.id,
                            authorName = stored.authorName,
                            authorHandle = stored.authorHandle,
                            avatarSeed = stored.authorHandle,
                            timestamp = stored.timestamp,
                            content = stored.content,
                            mediaGradient = stored.mediaGradient,
                            likesCount = stored.likesCount,
                            commentsCount = stored.commentsCount,
                            isLiked = stored.isLiked,
                            antiAiWatermark = stored.antiAiWatermark,
                            exifPurged = stored.exifPurged
                        )
                    }
                }
            }
        }

        // Load initial thread messages
        loadThreadMessages("thread_1")

        // Load transactions
        scope.launch {
            dao.getCashTransactions().collect { storedTxList ->
                if (storedTxList.isEmpty()) {
                    val initialTxs = listOf(
                        StoredCashTransaction(
                            id = "tx_seed_1",
                            title = "UPI Wallet Load (Instant Cash Reserve)",
                            amount = 25000.00,
                            currencyCode = "INR",
                            currencySymbol = "₹",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 60,
                            status = "Completed",
                            receiptId = "NX-UPI-99214-8842",
                            paymentMethod = "Google Pay UPI (satoshi@okaxis)"
                        ),
                        StoredCashTransaction(
                            id = "tx_seed_2",
                            title = "Nexus Sovereign Pro (Lifetime Enclave Activated)",
                            amount = 3999.00,
                            currencyCode = "INR",
                            currencySymbol = "₹",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                            status = "Paid & Activated",
                            receiptId = "NX-PAY-77192-K92A",
                            paymentMethod = "PhonePe UPI (satoshi@ybl)"
                        ),
                        StoredCashTransaction(
                            id = "tx_seed_3",
                            title = "Orbital Mesh Uplink Pass (LoRa Satellite)",
                            amount = 249.00,
                            currencyCode = "INR",
                            currencySymbol = "₹",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 360,
                            status = "Paid & Activated",
                            receiptId = "NX-PAY-66120-L81C",
                            paymentMethod = "RuPay Sovereign Card (•••• 8821)"
                        )
                    )
                    initialTxs.forEach { dao.insertCashTransaction(it) }
                } else {
                    _transactions.value = storedTxList.map { tx ->
                        CashTransaction(
                            id = tx.id,
                            title = tx.title,
                            amount = tx.amount,
                            currencyCode = tx.currencyCode,
                            currencySymbol = tx.currencySymbol,
                            timestamp = tx.timestamp,
                            status = tx.status,
                            receiptId = tx.receiptId,
                            paymentMethod = tx.paymentMethod
                        )
                    }
                }
            }
        }

        // Load zero-knowledge encrypted vault secrets
        scope.launch {
            dao.getAllVaultSecrets().collect { storedList ->
                if (storedList.isEmpty()) {
                    val initialSecrets = listOf(
                        Triple(
                            "ProtonMail Sovereign Enclave",
                            "LOGIN",
                            "satoshi.nexus@proton.me" to "Tr0jan#Latt1ce!9921"
                        ),
                        Triple(
                            "Cold Storage BIP-39 Seed",
                            "SEED",
                            "Hardware Master Key" to "witch collapse practice feed shame open despair creek road again ice leap"
                        ),
                        Triple(
                            "Emergency Dead-Man's Switch Note",
                            "NOTE",
                            "Confidential Protocol" to "Decrypt node 4 shards in Zurich vault if heartbeat exceeds 90 days."
                        )
                    )

                    initialSecrets.forEach { (title, category, payload) ->
                        val (ident, plain) = payload
                        val (cipher, iv) = VaultCrypto.encrypt(plain)
                        val stored = StoredVaultSecret(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            category = category,
                            identifier = ident,
                            cipherText = cipher,
                            iv = iv,
                            notes = "Encrypted on-device using AES-128 hardware enclave key.",
                            timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24
                        )
                        dao.insertVaultSecret(stored)
                    }
                } else {
                    _vaultItems.value = storedList.map { s ->
                        val plain = VaultCrypto.decrypt(s.cipherText, s.iv)
                        VaultItem(
                            id = s.id,
                            title = s.title,
                            category = s.category,
                            identifier = s.identifier,
                            plainSecret = plain,
                            notes = s.notes,
                            timestamp = s.timestamp
                        )
                    }
                }
            }
        }
    }

    fun loadThreadMessages(threadId: String) {
        scope.launch {
            dao.getMessagesForThread(threadId).collect { list ->
                if (list.isEmpty()) {
                    val sample = when (threadId) {
                        "thread_1" -> listOf(
                            StoredChatMessage(
                                id = "m_1",
                                threadId = "thread_1",
                                senderName = "Elena Vance",
                                senderHandle = "@elena.nexus",
                                text = "Hey Satoshi, did you audit the latest double-ratchet key exchange contract?",
                                timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
                                isMine = false,
                                isEncrypted = true,
                                ratchetKeyFingerprint = "4F8A:29C1:B76D:90EE",
                                ephemeralTimerSeconds = 60,
                                isAudio = false,
                                audioDurationSeconds = 0
                            ),
                            StoredChatMessage(
                                id = "m_2",
                                threadId = "thread_1",
                                senderName = "You",
                                senderHandle = "@satoshi_prime",
                                text = "Yes, verified! Zero server storage. Every message is re-keyed after each cryptographic exchange.",
                                timestamp = System.currentTimeMillis() - 1000 * 60 * 10,
                                isMine = true,
                                isEncrypted = true,
                                ratchetKeyFingerprint = "4F8A:29C1:B76D:90EE",
                                ephemeralTimerSeconds = 60,
                                isAudio = false,
                                audioDurationSeconds = 0
                            ),
                            StoredChatMessage(
                                id = "m_3",
                                threadId = "thread_1",
                                senderName = "Elena Vance",
                                senderHandle = "@elena.nexus",
                                text = "🔐 Ratchet key rotated. Sent you the encrypted smart contract.",
                                timestamp = System.currentTimeMillis() - 1000 * 60 * 4,
                                isMine = false,
                                isEncrypted = true,
                                ratchetKeyFingerprint = "7B11:00FA:99A3:1C34",
                                ephemeralTimerSeconds = 60,
                                isAudio = false,
                                audioDurationSeconds = 0
                            )
                        )
                        else -> listOf(
                            StoredChatMessage(
                                id = "m_default_1",
                                threadId = threadId,
                                senderName = "Nexus Node Relay",
                                senderHandle = "@system",
                                text = "Encrypted tunnel established via 3 peer hops. Fingerprint verified.",
                                timestamp = System.currentTimeMillis() - 1000 * 60 * 30,
                                isMine = false,
                                isEncrypted = true,
                                ratchetKeyFingerprint = "9A01:1120:FF32:8801",
                                ephemeralTimerSeconds = 0,
                                isAudio = false,
                                audioDurationSeconds = 0
                            )
                        )
                    }
                    sample.forEach { dao.insertMessage(it) }
                } else {
                    _currentMessages.value = list.map { s ->
                        ChatMessage(
                            id = s.id,
                            threadId = s.threadId,
                            senderName = s.senderName,
                            senderHandle = s.senderHandle,
                            text = s.text,
                            timestamp = s.timestamp,
                            isMine = s.isMine,
                            isEncrypted = s.isEncrypted,
                            ratchetKeyFingerprint = s.ratchetKeyFingerprint,
                            ephemeralTimerSeconds = s.ephemeralTimerSeconds,
                            isAudio = s.isAudio,
                            audioDurationSeconds = s.audioDurationSeconds
                        )
                    }
                }
            }
        }
    }

    fun sendMessage(
        threadId: String,
        text: String,
        isAudio: Boolean = false,
        audioDuration: Int = 0,
        ephemeralSeconds: Int = 0
    ) {
        val newMsg = StoredChatMessage(
            id = UUID.randomUUID().toString(),
            threadId = threadId,
            senderName = "You",
            senderHandle = _userState.value.didHandle,
            text = text,
            timestamp = System.currentTimeMillis(),
            isMine = true,
            isEncrypted = true,
            ratchetKeyFingerprint = if (_userState.value.quantumProtectionActive) "KYBER-1024:" + UUID.randomUUID().toString().take(8).uppercase() else "4F8A:29C1:B76D:90EE",
            ephemeralTimerSeconds = ephemeralSeconds,
            isAudio = isAudio,
            audioDurationSeconds = audioDuration
        )

        scope.launch {
            dao.insertMessage(newMsg)
            // Update thread last message
            _threads.value = _threads.value.map {
                if (it.id == threadId) {
                    it.copy(
                        lastMessage = if (isAudio) "🎤 Voice note (${audioDuration}s)" else text,
                        lastTimestamp = System.currentTimeMillis()
                    )
                } else it
            }
        }
    }

    fun deleteMessage(messageId: String) {
        scope.launch {
            dao.deleteMessage(messageId)
        }
    }

    fun toggleLikePost(postId: String) {
        val post = _posts.value.find { it.id == postId } ?: return
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
        _posts.value = _posts.value.map {
            if (it.id == postId) it.copy(isLiked = newLiked, likesCount = newCount) else it
        }
        scope.launch {
            dao.updatePostLike(postId, newCount, newLiked)
        }
    }

    fun createPost(content: String, gradient: String = "cyan_violet") {
        val newStored = StoredNexusPost(
            id = UUID.randomUUID().toString(),
            authorName = "You (Sovereign)",
            authorHandle = _userState.value.didHandle,
            content = content,
            timestamp = System.currentTimeMillis(),
            mediaGradient = gradient,
            likesCount = 0,
            commentsCount = 0,
            isLiked = false,
            antiAiWatermark = true,
            exifPurged = true
        )
        scope.launch {
            dao.insertPost(newStored)
        }
    }

    fun toggleUpvoteCirclePost(circleId: String, postId: String) {
        val currentPosts = _circlePosts.value[circleId] ?: return
        _circlePosts.value = _circlePosts.value.toMutableMap().apply {
            put(circleId, currentPosts.map { cp ->
                if (cp.id == postId) {
                    val newUpvoted = !cp.isUpvoted
                    cp.copy(
                        isUpvoted = newUpvoted,
                        upvotes = if (newUpvoted) cp.upvotes + 1 else cp.upvotes - 1
                    )
                } else cp
            })
        }
    }

    fun addCirclePost(circleId: String, content: String) {
        val newPost = CirclePost(
            id = UUID.randomUUID().toString(),
            circleId = circleId,
            authorName = "You",
            authorHandle = _userState.value.didHandle,
            content = content,
            timestamp = System.currentTimeMillis(),
            upvotes = 1,
            repliesCount = 0,
            isUpvoted = true
        )
        val currentPosts = _circlePosts.value[circleId] ?: emptyList()
        _circlePosts.value = _circlePosts.value.toMutableMap().apply {
            put(circleId, listOf(newPost) + currentPosts)
        }
    }

    fun activateAllFeatures() {
        _userState.value = _userState.value.copy(
            isProSubscriber = true,
            activeProPlan = "Nexus Sovereign Pro (Lifetime Enclave Activated)",
            activeAddons = setOf(
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
            cashBalance = if (_userState.value.cashBalance < 25000.0) 25000.00 else _userState.value.cashBalance,
            connectedPeersCount = 128,
            totalHopsToday = 4820,
            stealthGhostMode = true,
            torOnionRelayEnabled = true,
            quantumProtectionActive = true
        )
    }

    fun addCashBalance(amount: Double, paymentMethod: String = "Google Pay UPI (satoshi@okaxis)"): CashTransaction {
        val newBalance = _userState.value.cashBalance + amount
        _userState.value = _userState.value.copy(cashBalance = newBalance)
        val receiptId = "NX-UPI-" + System.currentTimeMillis().toString().takeLast(6)
        val tx = StoredCashTransaction(
            id = UUID.randomUUID().toString(),
            title = "UPI Wallet Top-up",
            amount = amount,
            currencyCode = "INR",
            currencySymbol = "₹",
            timestamp = System.currentTimeMillis(),
            status = "Completed",
            receiptId = receiptId,
            paymentMethod = paymentMethod
        )
        scope.launch {
            dao.insertCashTransaction(tx)
        }
        return CashTransaction(
            id = tx.id,
            title = tx.title,
            amount = tx.amount,
            currencyCode = tx.currencyCode,
            currencySymbol = tx.currencySymbol,
            timestamp = tx.timestamp,
            status = tx.status,
            receiptId = tx.receiptId,
            paymentMethod = tx.paymentMethod
        )
    }

    fun sendCashUpi(upiId: String, amount: Double): Pair<Boolean, String> {
        if (_userState.value.cashBalance < amount) {
            return Pair(false, "Insufficient UPI wallet balance")
        }
        val newBalance = _userState.value.cashBalance - amount
        _userState.value = _userState.value.copy(cashBalance = newBalance)
        val receiptId = "NX-SEND-" + System.currentTimeMillis().toString().takeLast(6)
        val tx = StoredCashTransaction(
            id = UUID.randomUUID().toString(),
            title = "P2P Sovereign Transfer to $upiId",
            amount = amount,
            currencyCode = "INR",
            currencySymbol = "₹",
            timestamp = System.currentTimeMillis(),
            status = "Settled (0% Fee)",
            receiptId = receiptId,
            paymentMethod = "Sovereign UPI Tunnel"
        )
        scope.launch {
            dao.insertCashTransaction(tx)
        }
        return Pair(true, receiptId)
    }

    fun toggleTorRelay() {
        _userState.value = _userState.value.copy(
            torOnionRelayEnabled = !_userState.value.torOnionRelayEnabled
        )
    }

    fun toggleGhostMode() {
        _userState.value = _userState.value.copy(
            stealthGhostMode = !_userState.value.stealthGhostMode
        )
    }

    // REAL CASH SIMULATION CHECKOUT
    fun executeRealCashPurchase(
        product: PremiumProduct,
        paymentMethod: String,
        currencyCode: String = _selectedCurrency.value
    ): CashTransaction {
        val symbol = if (currencyCode == "INR") "₹" else "$"
        val amount = if (currencyCode == "INR") product.priceInr else product.priceUsd
        val billingLabel = if (currencyCode == "INR") product.billingTypeInr else product.billingType
        val receiptId = "NX-PAY-" + System.currentTimeMillis().toString().takeLast(6) + "-" + UUID.randomUUID().toString().take(4).uppercase()
        val tx = StoredCashTransaction(
            id = UUID.randomUUID().toString(),
            title = "${product.title} ($billingLabel)",
            amount = amount,
            currencyCode = currencyCode,
            currencySymbol = symbol,
            timestamp = System.currentTimeMillis(),
            status = "Paid & Activated",
            receiptId = receiptId,
            paymentMethod = paymentMethod
        )

        scope.launch {
            dao.insertCashTransaction(tx)
        }

        // Apply perks to user state
        val updatedAddons = _userState.value.activeAddons + product.id
        val isNowPro = _userState.value.isProSubscriber || product.id.contains("sovereign")
        _userState.value = _userState.value.copy(
            isProSubscriber = isNowPro,
            activeProPlan = if (product.id.contains("sovereign")) product.title else _userState.value.activeProPlan,
            activeAddons = updatedAddons,
            quantumProtectionActive = isNowPro,
            stealthGhostMode = if (isNowPro) true else _userState.value.stealthGhostMode
        )

        return CashTransaction(
            id = tx.id,
            title = tx.title,
            amount = tx.amount,
            currencyCode = tx.currencyCode,
            currencySymbol = tx.currencySymbol,
            timestamp = tx.timestamp,
            status = tx.status,
            receiptId = tx.receiptId,
            paymentMethod = tx.paymentMethod
        )
    }

    fun sendCreatorCashTip(
        creatorHandle: String,
        amount: Double,
        currencyCode: String = _selectedCurrency.value,
        paymentMethod: String
    ): CashTransaction {
        val symbol = if (currencyCode == "INR") "₹" else "$"
        val receiptId = "TIP-NX-" + System.currentTimeMillis().toString().takeLast(6)
        val tx = StoredCashTransaction(
            id = UUID.randomUUID().toString(),
            title = "Direct Creator Cash Tip to $creatorHandle",
            amount = amount,
            currencyCode = currencyCode,
            currencySymbol = symbol,
            timestamp = System.currentTimeMillis(),
            status = "Sent (0% Fee)",
            receiptId = receiptId,
            paymentMethod = paymentMethod
        )

        scope.launch {
            dao.insertCashTransaction(tx)
        }

        return CashTransaction(
            id = tx.id,
            title = tx.title,
            amount = tx.amount,
            currencyCode = tx.currencyCode,
            currencySymbol = tx.currencySymbol,
            timestamp = tx.timestamp,
            status = tx.status,
            receiptId = tx.receiptId,
            paymentMethod = tx.paymentMethod
        )
    }

    fun addVaultSecret(
        title: String,
        category: String,
        identifier: String,
        plainSecret: String,
        notes: String = ""
    ) {
        val (cipher, iv) = VaultCrypto.encrypt(plainSecret)
        val stored = StoredVaultSecret(
            id = UUID.randomUUID().toString(),
            title = title,
            category = category,
            identifier = identifier,
            cipherText = cipher,
            iv = iv,
            notes = notes.ifBlank { "Encrypted with on-device AES enclave key" },
            timestamp = System.currentTimeMillis()
        )
        scope.launch {
            dao.insertVaultSecret(stored)
        }
    }

    fun deleteVaultSecret(id: String) {
        scope.launch {
            dao.deleteVaultSecret(id)
        }
    }

    fun toggleArchiveThread(threadId: String) {
        _threads.value = _threads.value.map {
            if (it.id == threadId) it.copy(isArchived = !it.isArchived) else it
        }
    }

    fun toggleLockThread(threadId: String) {
        _threads.value = _threads.value.map {
            if (it.id == threadId) it.copy(isLocked = !it.isLocked) else it
        }
    }

    fun archiveThread(threadId: String) {
        _threads.value = _threads.value.map {
            if (it.id == threadId) it.copy(isArchived = true) else it
        }
    }

    fun unarchiveThread(threadId: String) {
        _threads.value = _threads.value.map {
            if (it.id == threadId) it.copy(isArchived = false) else it
        }
    }

    fun lockThread(threadId: String) {
        _threads.value = _threads.value.map {
            if (it.id == threadId) it.copy(isLocked = true, isArchived = false) else it
        }
    }

    fun unlockThread(threadId: String) {
        _threads.value = _threads.value.map {
            if (it.id == threadId) it.copy(isLocked = false) else it
        }
    }
}
