package com.example.data.model

data class TopicItem(
    val name: String,
    val category: String,
    val iconEmoji: String,
    val isHot: Boolean = false,
    val searchKeyword: String = name
)

object TopicCatalog {

    val categories = listOf(
        "Semua",
        "🔥 Tren",
        "⚽ Olahraga",
        "🏙️ Kota",
        "💰 Ekonomi",
        "📱 Teknologi",
        "🎭 Hiburan",
        "⚖️ Nasional"
    )

    val allTopics = listOf(
        // 🔥 Tren & Karir
        TopicItem("PPPK", "🔥 Tren", "📋", isHot = true, searchKeyword = "PPPK"),
        TopicItem("CPNS 2026", "🔥 Tren", "🎓", isHot = true, searchKeyword = "CPNS"),
        TopicItem("Gaji ASN", "🔥 Tren", "💵", isHot = true, searchKeyword = "Gaji ASN"),
        TopicItem("IKN Nusantara", "🔥 Tren", "🏛️", isHot = true, searchKeyword = "IKN"),
        TopicItem("Pilkada Serentak", "🔥 Tren", "🗳️", isHot = false, searchKeyword = "Pilkada"),
        TopicItem("Bansos", "🔥 Tren", "📦", isHot = false, searchKeyword = "Bansos"),
        TopicItem("Kenaikan PPN", "🔥 Tren", "📊", isHot = true, searchKeyword = "PPN"),
        TopicItem("Kartu Prakerja", "🔥 Tren", "💳", isHot = false, searchKeyword = "Prakerja"),
        TopicItem("BPJS Kesehatan", "🔥 Tren", "🏥", isHot = false, searchKeyword = "BPJS"),
        TopicItem("Subsidi BBM", "🔥 Tren", "⛽", isHot = false, searchKeyword = "Subsidi BBM"),
        TopicItem("Tapera", "🔥 Tren", "🏠", isHot = false, searchKeyword = "Tapera"),
        TopicItem("KJP Plus", "🔥 Tren", "🎒", isHot = false, searchKeyword = "KJP"),
        TopicItem("Paspor Baru", "🔥 Tren", "🛂", isHot = false, searchKeyword = "Paspor"),
        TopicItem("Mudik Lebaran", "🔥 Tren", "🚆", isHot = false, searchKeyword = "Mudik"),

        // ⚽ Olahraga & Sepak Bola
        TopicItem("Sepakbola", "⚽ Olahraga", "⚽", isHot = true, searchKeyword = "Sepakbola"),
        TopicItem("Timnas Indonesia", "⚽ Olahraga", "🇮🇩", isHot = true, searchKeyword = "Timnas"),
        TopicItem("Liga 1", "⚽ Olahraga", "🏆", isHot = false, searchKeyword = "Liga 1"),
        TopicItem("Shin Tae-yong", "⚽ Olahraga", "🧢", isHot = true, searchKeyword = "Shin Tae-yong"),
        TopicItem("Liga Champions", "⚽ Olahraga", "🌟", isHot = true, searchKeyword = "Liga Champions"),
        TopicItem("Premier League", "⚽ Olahraga", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", isHot = false, searchKeyword = "Premier League"),
        TopicItem("Real Madrid", "⚽ Olahraga", "👑", isHot = false, searchKeyword = "Real Madrid"),
        TopicItem("Manchester United", "⚽ Olahraga", "🔴", isHot = false, searchKeyword = "Manchester United"),
        TopicItem("Bulu Tangkis", "⚽ Olahraga", "🏸", isHot = true, searchKeyword = "Bulu Tangkis"),
        TopicItem("All England", "⚽ Olahraga", "🏸", isHot = false, searchKeyword = "All England"),
        TopicItem("MotoGP", "⚽ Olahraga", "🏍️", isHot = true, searchKeyword = "MotoGP"),
        TopicItem("Formula 1", "⚽ Olahraga", "🏎️", isHot = false, searchKeyword = "Formula 1"),
        TopicItem("Voli Proliga", "⚽ Olahraga", "🏐", isHot = false, searchKeyword = "Proliga"),
        TopicItem("PSSI", "⚽ Olahraga", "🦅", isHot = false, searchKeyword = "PSSI"),
        TopicItem("Piala Dunia", "⚽ Olahraga", "🌍", isHot = false, searchKeyword = "Piala Dunia"),
        TopicItem("NBA", "⚽ Olahraga", "🏀", isHot = false, searchKeyword = "NBA"),

        // 🏙️ Kota & Daerah
        TopicItem("Jakarta", "🏙️ Kota", "🏙️", isHot = true, searchKeyword = "Jakarta"),
        TopicItem("Surabaya", "🏙️ Kota", "🦈", isHot = true, searchKeyword = "Surabaya"),
        TopicItem("Bandung", "🏙️ Kota", "⛰️", isHot = false, searchKeyword = "Bandung"),
        TopicItem("Medan", "🏙️ Kota", "🌴", isHot = false, searchKeyword = "Medan"),
        TopicItem("Semarang", "🏙️ Kota", "⛵", isHot = false, searchKeyword = "Semarang"),
        TopicItem("Makassar", "🏙️ Kota", "⚓", isHot = false, searchKeyword = "Makassar"),
        TopicItem("Bali", "🏙️ Kota", "🏝️", isHot = true, searchKeyword = "Bali"),
        TopicItem("Yogyakarta", "🏙️ Kota", "🏯", isHot = true, searchKeyword = "Yogyakarta"),
        TopicItem("Solo", "🏙️ Kota", "🏘️", isHot = false, searchKeyword = "Solo"),
        TopicItem("Malang", "🏙️ Kota", "🍏", isHot = false, searchKeyword = "Malang"),
        TopicItem("Palembang", "🏙️ Kota", "🌉", isHot = false, searchKeyword = "Palembang"),
        TopicItem("Balikpapan", "🏙️ Kota", "🛢️", isHot = false, searchKeyword = "Balikpapan"),
        TopicItem("Batam", "🏙️ Kota", "🛳️", isHot = false, searchKeyword = "Batam"),
        TopicItem("Aceh", "🏙️ Kota", "🕌", isHot = false, searchKeyword = "Aceh"),
        TopicItem("Papua", "🏙️ Kota", "🌄", isHot = false, searchKeyword = "Papua"),
        TopicItem("Jawa Timur", "🏙️ Kota", "🗺️", isHot = false, searchKeyword = "Jawa Timur"),
        TopicItem("Jawa Barat", "🏙️ Kota", "🗺️", isHot = false, searchKeyword = "Jawa Barat"),
        TopicItem("Jawa Tengah", "🏙️ Kota", "🗺️", isHot = false, searchKeyword = "Jawa Tengah"),

        // 💰 Ekonomi & Keuangan
        TopicItem("IHSG", "💰 Ekonomi", "📈", isHot = true, searchKeyword = "IHSG"),
        TopicItem("Rupiah", "💰 Ekonomi", "💵", isHot = true, searchKeyword = "Rupiah"),
        TopicItem("Harga Emas", "💰 Ekonomi", "🥇", isHot = true, searchKeyword = "Emas"),
        TopicItem("Crypto & Bitcoin", "💰 Ekonomi", "🪙", isHot = true, searchKeyword = "Crypto"),
        TopicItem("Saham IDX", "💰 Ekonomi", "📊", isHot = false, searchKeyword = "Saham"),
        TopicItem("Bunga BI", "💰 Ekonomi", "🏦", isHot = false, searchKeyword = "Bank Indonesia"),
        TopicItem("BBM Pertamina", "💰 Ekonomi", "⛽", isHot = true, searchKeyword = "Pertamina"),
        TopicItem("UMKM", "💰 Ekonomi", "🏪", isHot = false, searchKeyword = "UMKM"),
        TopicItem("Pajak", "💰 Ekonomi", "🧾", isHot = false, searchKeyword = "Pajak"),
        TopicItem("Investasi", "💰 Ekonomi", "💼", isHot = false, searchKeyword = "Investasi"),
        TopicItem("Perbankan", "💰 Ekonomi", "🏧", isHot = false, searchKeyword = "Bank"),
        TopicItem("Inflasi", "💰 Ekonomi", "📉", isHot = false, searchKeyword = "Inflasi"),
        TopicItem("OJK", "💰 Ekonomi", "🛡️", isHot = false, searchKeyword = "OJK"),

        // 📱 Teknologi & Gadget
        TopicItem("Kecerdasan Buatan (AI)", "📱 Teknologi", "🤖", isHot = true, searchKeyword = "AI"),
        TopicItem("Gadget & Ponsel", "📱 Teknologi", "📱", isHot = true, searchKeyword = "Gadget"),
        TopicItem("iPhone", "📱 Teknologi", "🍏", isHot = true, searchKeyword = "iPhone"),
        TopicItem("Samsung Galaxy", "📱 Teknologi", "📲", isHot = false, searchKeyword = "Samsung"),
        TopicItem("Mobil Listrik", "📱 Teknologi", "🚗", isHot = true, searchKeyword = "Mobil Listrik"),
        TopicItem("Motor Listrik", "📱 Teknologi", "🛵", isHot = false, searchKeyword = "Motor Listrik"),
        TopicItem("Game Online", "📱 Teknologi", "🎮", isHot = false, searchKeyword = "Game"),
        TopicItem("Cyber Security", "📱 Teknologi", "🔐", isHot = false, searchKeyword = "Cyber"),
        TopicItem("Starlink", "📱 Teknologi", "🛰️", isHot = false, searchKeyword = "Starlink"),
        TopicItem("Telkomsel", "📱 Teknologi", "📶", isHot = false, searchKeyword = "Telkomsel"),
        TopicItem("Kominfo", "📱 Teknologi", "📡", isHot = false, searchKeyword = "Kominfo"),
        TopicItem("Startup", "📱 Teknologi", "🚀", isHot = false, searchKeyword = "Startup"),

        // 🎭 Hiburan & Gaya Hidup
        TopicItem("Film Indonesia", "🎭 Hiburan", "🎬", isHot = true, searchKeyword = "Film"),
        TopicItem("Bioskop XXI", "🎭 Hiburan", "🍿", isHot = false, searchKeyword = "Bioskop"),
        TopicItem("K-Pop & Drakor", "🎭 Hiburan", "🎵", isHot = true, searchKeyword = "K-Pop"),
        TopicItem("Konser Musik", "🎭 Hiburan", "🎤", isHot = false, searchKeyword = "Konser"),
        TopicItem("Kuliner Nusantara", "🎭 Hiburan", "🍜", isHot = true, searchKeyword = "Kuliner"),
        TopicItem("Wisata", "🎭 Hiburan", "✈️", isHot = false, searchKeyword = "Wisata"),
        TopicItem("Kesehatan", "🎭 Hiburan", "🩺", isHot = false, searchKeyword = "Kesehatan"),
        TopicItem("Otomotif", "🎭 Hiburan", "🏎️", isHot = false, searchKeyword = "Otomotif"),
        TopicItem("Viral Medsos", "🎭 Hiburan", "🔥", isHot = true, searchKeyword = "Viral"),

        // ⚖️ Nasional & Hukum
        TopicItem("KPK", "⚖️ Nasional", "⚖️", isHot = true, searchKeyword = "KPK"),
        TopicItem("Kejaksaan Agung", "⚖️ Nasional", "🏛️", isHot = false, searchKeyword = "Kejaksaan"),
        TopicItem("Mahkamah Konstitusi", "⚖️ Nasional", "📜", isHot = false, searchKeyword = "MK"),
        TopicItem("Polri", "⚖️ Nasional", "👮", isHot = false, searchKeyword = "Polri"),
        TopicItem("TNI", "⚖️ Nasional", "🎖️", isHot = false, searchKeyword = "TNI"),
        TopicItem("BMKG Cuaca", "⚖️ Nasional", "🌦️", isHot = true, searchKeyword = "BMKG"),
        TopicItem("Gempa Bumi", "⚖️ Nasional", "🌋", isHot = true, searchKeyword = "Gempa"),
        TopicItem("Kementerian Keuangan", "⚖️ Nasional", "🏦", isHot = false, searchKeyword = "Kemenkeu"),
        TopicItem("Kemendikbud", "⚖️ Nasional", "📚", isHot = false, searchKeyword = "Kemendikbud")
    )
}
