package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

/**
 * Splash Screen Berita Indonesia dengan latar belakang animasi Bendera Merah Putih berkibar
 * secara dinamis, menampilkan logo resmi, dan transisi halus ke halaman utama.
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Navigasi otomatis setelah 2.6 detik
    LaunchedEffect(Unit) {
        delay(2600)
        onSplashFinished()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "flag_wave")

    // Fase gelombang kibaran bendera
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    // Animasi skala halus logo (efek bernapas)
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    // Animasi fade in konten
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        contentVisible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 650, easing = EaseOutCubic),
        label = "content_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("splash_screen_root")
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // Sentuhan layar untuk langsung lewati splash screen jika diinginkan
                onSplashFinished()
            }
    ) {
        // Latar Belakang Bendera Merah Putih Berkibar Dinamis
        WavingIndonesianFlagCanvas(
            phase = wavePhase,
            modifier = Modifier.fillMaxSize()
        )

        // Vignette & overlay lembut untuk memastikan keterbacaan teks dan logo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.55f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Konten Tengah
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .graphicsLayer(alpha = contentAlpha)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Logo Berita Indonesia dengan Efek Shimmer & Bayangan Elegan
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 16.dp,
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = Color.Red.copy(alpha = 0.4f),
                        spotColor = Color.Black.copy(alpha = 0.5f)
                    )
                    .testTag("splash_logo_card")
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon_logo),
                        contentDescription = "Logo Berita Indonesia",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(28.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Judul Aplikasi
            Text(
                text = "BERITA INDONESIA",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    fontSize = 24.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tagline / Subtitle
            Text(
                text = "Portal Berita Terpercaya & Terlengkap Nusantara",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                ),
                color = Color.White.copy(alpha = 0.88f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Portal Badges
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val portals = listOf("Detik", "Kompas", "CNN", "Antara", "CNBC")
                portals.forEachIndexed { index, portal ->
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = portal,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Indikator Loading Bawah
            CircularProgressIndicator(
                strokeWidth = 2.5.dp,
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Merah Putih Mengabarkan",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Canvas kustom yang merender kain bendera Merah Putih berkibar menggunakan fungsi
 * sinus bertingkat dengan pencahayaan dan bayangan realistis.
 */
@Composable
private fun WavingIndonesianFlagCanvas(
    phase: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val steps = 80
        val stepWidth = width / steps
        val halfHeight = height / 2f

        // Garis pemisah merah putih bergelombang
        val wavePathRed = Path()
        val wavePathWhite = Path()

        // Titik awal merah (seluruh bagian atas sampai garis tengah bergelombang)
        wavePathRed.moveTo(0f, 0f)
        wavePathRed.lineTo(width, 0f)

        // Hitung garis tengah bergelombang dari kanan ke kiri
        for (i in steps downTo 0) {
            val x = i * stepWidth
            val normalizedX = x / width

            // Kombinasi gelombang primer dan sekunder untuk efek kain alami
            val waveOffset = (
                sin((normalizedX * 2.5f * PI + phase).toFloat()) * 28f +
                sin((normalizedX * 5.0f * PI - phase * 0.7f).toFloat()) * 12f
            )
            val y = halfHeight + waveOffset
            wavePathRed.lineTo(x, y)
        }
        wavePathRed.close()

        // Gambar Bagian Putih (seluruh layar latar bawah)
        drawRect(
            color = Color(0xFFF8F9FA),
            size = size
        )

        // Gambar Bagian Merah (kombinasi warna bendera merah Indonesia)
        drawPath(
            path = wavePathRed,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF9E0B0E), // Merah tua atas
                    Color(0xFFBA0C12), // Merah bendera resmi
                    Color(0xFFD6181E)  // Merah terang batas tengah
                ),
                startY = 0f,
                endY = halfHeight
            )
        )

        // Gambar Efek Lipatan Cahaya & Bayangan Kain Berkibar (Shading & Ripples)
        drawWaveShadows(width = width, height = height, steps = steps, stepWidth = stepWidth, phase = phase)
    }
}

/**
 * Menambahkan bayangan lipatan dinamis untuk menciptakan dimensi 3D bendera berkibar
 */
private fun DrawScope.drawWaveShadows(
    width: Float,
    height: Float,
    steps: Int,
    stepWidth: Float,
    phase: Float
) {
    for (i in 0 until steps) {
        val x = i * stepWidth
        val normalizedX = x / width

        // Turunan gelombang untuk mendeteksi lereng gelombang (pencahayaan vs bayangan)
        val slope = sin((normalizedX * 2.5f * PI + phase).toFloat())

        val shadowAlpha = (slope * 0.14f).coerceIn(-0.10f, 0.16f)

        if (shadowAlpha > 0f) {
            // Bayangan gelap di lembah gelombang
            drawRect(
                color = Color.Black.copy(alpha = shadowAlpha),
                topLeft = Offset(x, 0f),
                size = androidx.compose.ui.geometry.Size(stepWidth + 1f, height)
            )
        } else if (shadowAlpha < 0f) {
            // Kilauan cahaya di puncak gelombang
            drawRect(
                color = Color.White.copy(alpha = -shadowAlpha * 0.8f),
                topLeft = Offset(x, 0f),
                size = androidx.compose.ui.geometry.Size(stepWidth + 1f, height)
            )
        }
    }
}
