package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.IndonesianRed
import com.example.ui.theme.NusantaraBlueActive

/**
 * Dialog Kebijakan Privasi (Privacy Policy)
 */
@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .testTag("privacy_policy_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = NusantaraBlueActive.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = NusantaraBlueActive,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Kebijakan Privasi",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Terakhir diperbarui: November 2026",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_privacy_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    PolicySection(
                        title = "1. Pendahuluan",
                        body = "Selamat datang di Berita Indonesia. Kami menghargai privasi Anda dan berkomitmen untuk melindungi informasi yang Anda gunakan saat menikmati layanan agregator berita ini. Kebijakan ini menjelaskan bagaimana data dikelola sesuai prinsip privasi dan kepatuhan Google Play."
                    )

                    PolicySection(
                        title = "2. Data yang Dikumpulkan & Penyimpanan Lokal",
                        body = "Aplikasi Berita Indonesia beroperasi dengan prinsip minimasi data (data minimization):\n" +
                                "• Preferensi Pengguna: Penanda buku (bookmarks) dan pilihan tema (terang/gelap) disimpan secara lokal di memori perangkat Anda menggunakan database Room terenkripsi bawaan Android.\n" +
                                "• Data Pribadi: Kami tidak mewajibkan registrasi, tidak meminta nama, email, nomor telepon, kontak, maupun lokasi presisi Anda."
                    )

                    PolicySection(
                        title = "3. Layanan Iklan Pihak Ketiga (TopOn & Meta)",
                        body = "Untuk mendukung operasional aplikasi secara gratis, aplikasi ini mengintegrasikan SDK periklanan dari TopOn Mediation dan Meta Audience Network (Facebook Ads):\n" +
                                "• Jaringan iklan dapat memproses data teknis non-pribadi seperti ID Iklan Google (GAID), jenis perangkat, resolusi layar, dan alamat IP untuk penayangan iklan yang relevan serta pencegahan penipuan klik (fraud prevention).\n" +
                                "• Anda dapat mengatur atau mereset preferensi ID Iklan Anda kapan saja melalui Pengaturan Android > Google > Iklan."
                    )

                    PolicySection(
                        title = "4. Pembacaan Artikel via Chrome Custom Tabs",
                        body = "Ketika Anda memilih untuk membaca artikel lengkap, tautan dibuka menggunakan teknologi Chrome Custom Tabs resmi milik Android langsung ke situs web resmi penerbit (seperti Detik, Kompas, CNN, Antara, CNBC, dll). Segala kuki atau interaksi pada situs penerbit tersebut tunduk pada kebijakan privasi masing-masing situs web penerbit bersangkutan."
                    )

                    PolicySection(
                        title = "5. Keamanan Data",
                        body = "Kami menerapkan langkah-langkah keamanan teknis standar industri untuk melindungi integritas aplikasi dan mencegah akses yang tidak sah ke data lokal di perangkat Anda."
                    )

                    PolicySection(
                        title = "6. Hubungi Pengembang",
                        body = "Jika Anda memiliki pertanyaan mengenai Kebijakan Privasi ini, silakan hubungi pengembang melalui halaman dukungan di Google Play Store."
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NusantaraBlueActive),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("accept_privacy_button")
                ) {
                    Text("Saya Mengerti", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Dialog Syarat & Ketentuan (Terms of Service)
 */
@Composable
fun TermsOfServiceDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .testTag("terms_of_service_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = IndonesianRed.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = IndonesianRed,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Syarat & Ketentuan",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Ketentuan Penggunaan Layanan",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_terms_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    PolicySection(
                        title = "1. Penerimaan Ketentuan",
                        body = "Dengan mengunduh, memasang, atau menggunakan aplikasi Berita Indonesia, Anda menyatakan telah membaca, memahami, dan menyetujui seluruh Syarat dan Ketentuan ini. Jika Anda tidak menyetujui salah satu poin, harap tidak menggunakan aplikasi ini."
                    )

                    PolicySection(
                        title = "2. Hak Cipta & Sumber Konten Berita",
                        body = "• Berita Indonesia adalah aplikasi agregator pembaca feed berita yang mengumpulkan ringkasan dan cuplikan berita dari RSS publik resmi milik portal berita Indonesia terpercaya (antara lain Detikcom, Kompas, CNN Indonesia, Antara News, CNBC Indonesia, Republika, Tempo, Sindonews, dll).\n" +
                                "• Hak cipta seluruh naskah, foto jurnalistik, video, dan merek dagang tetap sepenuhnya menjadi milik eksklusif masing-masing penerbit berita terkait.\n" +
                                "• Aplikasi ini merujuk dan menghubungkan pembaca langsung ke sumber penerbit asli melalui Chrome Custom Tabs resmi demi menghormati hak cipta dan jurnalisme pers."
                    )

                    PolicySection(
                        title = "3. Penggunaan yang Diperbolehkan",
                        body = "Anda setuju untuk menggunakan aplikasi hanya untuk tujuan yang sah, membaca informasi untuk kepentingan pribadi non-komersial, dan tidak melakukan rekayasa balik (reverse engineering) atau merusak integritas sistem aplikasi."
                    )

                    PolicySection(
                        title = "4. Batasan Tanggung Jawab",
                        body = "Aplikasi Berita Indonesia disediakan 'sebagaimana adanya' (as is). Kami berupaya menjaga ketersediaan layanan dan akurasi sindikasi, namun kami tidak bertanggung jawab atas opini, perubahan isi artikel, atau kesalahan data editorial yang diterbitkan oleh masing-masing portal media eksternal."
                    )

                    PolicySection(
                        title = "5. Perubahan Layanan",
                        body = "Pengembang berhak memperbarui, memodifikasi, atau menyempurnakan fitur aplikasi sewaktu-waktu demi meningkatkan kenyamanan pembaca di tanah air."
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndonesianRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("accept_terms_button")
                ) {
                    Text("Saya Setuju", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Dialog Tentang Aplikasi (About App)
 */
@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit,
    onShareApp: () -> Unit,
    onRateApp: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .testTag("about_app_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button top right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_about_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                // App Icon Logo
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(80.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon_logo),
                        contentDescription = "Logo Berita Indonesia",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Berita Indonesia",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Versi 1.0.0 (Build 2026)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = NusantaraBlueActive.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "Portal Berita Tercepat & Terpercaya Tanah Air",
                        style = MaterialTheme.typography.labelSmall,
                        color = NusantaraBlueActive,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Aplikasi pembaca berita pintar Indonesia yang mengumpulkan informasi teraktual dari berbagai portal terkemuka nasional secara real-time, disajikan dengan performa instan Chrome Custom Tabs dan antarmuka modern.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Developer & Support Info
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AboutDetailRow(icon = Icons.Default.Person, label = "Pengembang", value = "Geccko Creator")
                        AboutDetailRow(icon = Icons.Default.Public, label = "Paket ID", value = "com.beritaindonesia.gecckocreator")
                        AboutDetailRow(icon = Icons.Default.Layers, label = "Teknologi", value = "Android Jetpack Compose & CCT")
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action buttons: Bagikan & Beri Nilai
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onShareApp,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_share_app_button")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Bagikan", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onRateApp,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NusantaraBlueActive),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_rate_app_button")
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Beri Nilai", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutDetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PolicySection(
    title: String,
    body: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
        )
    }
}
