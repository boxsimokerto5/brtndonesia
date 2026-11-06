# Panduan Signature Key & GitHub Actions (Berita Nusantara)

Aplikasi telah dilengkapi dengan signature key (keystore) resmi yang dibuat permanen untuk menjamin bahwa **signature key tidak akan pernah berubah pada build berikutnya** (sehingga pembaruan aplikasi dapat diinstal tanpa konflik signature).

---

## 1. Informasi Signature Key Resmi

* **Nama File:** `my-upload-key.jks`
* **Alias:** `upload`
* **Password Keystore:** `gecckocreator123`
* **Password Key:** `gecckocreator123`
* **Masa Berlaku:** Hingga **24 Maret 2054 (10.000 Hari)**
* **Package Name (Application ID):** `com.beritaindonesia.gecckocreator`
* **Versi:** 1.0 (versionCode: 1)

### Fingerprint Sertifikat Resmi (Konsisten & Tetap):
* **SHA-256:** `27:8F:E5:21:84:FC:43:F3:43:0F:F2:8F:B3:33:22:50:C2:73:15:AC:06:DE:22:D5:2A:93:23:BC:C4:3E:07:7D`
* **SHA-1:** `36:2D:13:13:84:6E:1A:20:ED:34:15:D9:05:FC:B5:5A:C1:D1:07:D2`

---

## 2. Cara Kerja di GitHub Actions

File workflow berada di:
`.github/workflows/build-android.yml`

Workflow ini akan otomatis terpicu ketika:
1. Melakukan `git push` ke branch `main` atau `master`.
2. Melakukan *Pull Request*.
3. Dijalankan manual lewat tombol **Run workflow** di tab **Actions** di GitHub.

### Cara Menjaga Signature Tetap Sama:
* File `my-upload-key.jks` sudah tersimpan di *root repository* dan siap digunakan secara otomatis oleh workflow tanpa perlu setup tambahan.
* Jika Anda ingin menyimpannya di **GitHub Repository Secrets** (rekomendasi keamanan untuk private repository):
  1. Masuk ke repositori GitHub Anda -> **Settings** -> **Secrets and variables** -> **Actions**.
  2. Tambahkan secret baru:
     - `KEYSTORE_BASE64`: Isi dengan seluruh isi teks dari file `my-upload-key.base64.txt`.
     - `STORE_PASSWORD`: `gecckocreator123`
     - `KEY_PASSWORD`: `gecckocreator123`

---

## 3. Hasil Output Build di GitHub Actions

Setiap build selesai, Anda dapat langsung mengunduh hasil paket di bagian **Artifacts**:
1. **`Berita-Nusantara-Release-APK`**: File `.apk` siap pasang di smartphone pengguna.
2. **`Berita-Nusantara-Release-AAB`**: File `.aab` (*Android App Bundle*) siap diunggah ke Google Play Console.
