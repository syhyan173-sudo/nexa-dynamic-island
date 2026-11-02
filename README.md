# Nexa Island — Dynamic Island untuk Android & MIUI 13

[![Android Build (Debug)](https://img.shields.io/badge/CI-Android%20Build-purple.svg)](#h-langkah-5--menjalankan-github-actions-debug-build)
[![Platform](https://img.shields.io/badge/Platform-Android%2012%20(API%2031%2B)-06B6D4.svg)](#a-pengenalan-project)
[![Target](https://img.shields.io/badge/Target-Xiaomi%20Redmi%20Note%209%20%7C%20MIUI%2013-10B981.svg)](#q-panduan-redmi-note-9-miui-13)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](#t-roadmap-dan-license)

**Nexa Island** adalah aplikasi Android native yang menghadirkan pengalaman interaktif *Dynamic Island* modern dengan desain **Dark Frosted Glassmorphism**, arsitektur modular (*Clean Architecture + MVVM*), efisiensi konsumsi daya, dan sistem otomatisasi build cloud menggunakan **GitHub Actions**.

Dokumentasi ini adalah panduan langkah-demi-langkah dari nol bagi pengguna untuk mengubah source code menjadi file APK (Debug maupun Signed Release) langsung dari cloud tanpa perlu memasang Android Studio di komputer lokal.

---

## DAFTAR ISI

- [A. Pengenalan Project](#a-pengenalan-project)
- [B. Persyaratan](#b-persyaratan)
- [C. Struktur Folder](#c-struktur-folder)
- [D. LANGKAH 1 — Membuat Repository GitHub](#d-langkah-1--membuat-repository-github)
- [E. LANGKAH 2 — Mengunggah Source Code](#e-langkah-2--mengunggah-source-code)
- [F. LANGKAH 3 — Memastikan Source Code Siap](#f-langkah-3--memastikan-source-code-siap)
- [G. LANGKAH 4 — Push Source Code ke GitHub](#g-langkah-4--push-source-code-ke-github)
- [H. LANGKAH 5 — Menjalankan GitHub Actions Debug Build](#h-langkah-5--menjalankan-github-actions-debug-build)
- [I. LANGKAH 6 — Mengunduh APK Debug](#i-langkah-6--mengunduh-apk-debug)
- [J. LANGKAH 7 — Membuat Android Keystore untuk Release](#j-langkah-7--membuat-android-keystore-untuk-release)
- [K. LANGKAH 8 — Mengatur GitHub Secrets](#k-langkah-8--mengatur-github-secrets)
- [L. LANGKAH 9 — Menjalankan Release Build](#l-langkah-9--menjalankan-release-build)
- [M. LANGKAH 10 — Mengunduh APK Release](#m-langkah-10--mengunduh-apk-release)
- [N. LANGKAH 11 — Membuat GitHub Release](#n-langkah-11--membuat-github-release)
- [O. LANGKAH 12 — Update Aplikasi dan Build Versi Baru](#o-langkah-12--update-aplikasi-dan-build-versi-baru)
- [P. Troubleshooting](#p-troubleshooting)
- [Q. Panduan Redmi Note 9 & MIUI 13](#q-panduan-redmi-note-9--miui-13)
- [R. Development Guide](#r-development-guide)
- [S. Known Limitations](#s-known-limitations)
- [T. Roadmap dan License](#t-roadmap-dan-license)

---

## A. PENGENALAN PROJECT

Nexa Island dirancang khusus untuk membawa pengalaman multitasking visual elegan ke smartphone Android tanpa memerlukan akses root.

### Fitur Utama:
1. **Dynamic Floating Island Overlay**: Tiga mode status adaptif (**Compact**, **Expanded**, dan **Hidden**) menggunakan `WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY`.
2. **Music & Media Integration**: Mendeteksi pemutaran Spotify, YouTube Music, dan pemutar musik bawaan melalui Android MediaSession. Dilengkapi dengan progress tracking, kontrol play/pause/prev/next, dan visualizer gelombang audio compact.
3. **Smart Notification Bridge**: Intersepsi notifikasi pesan masuk dari WhatsApp, Telegram, Instagram, Discord, dan SMS via `NotificationListenerService` dengan opsi privasi.
4. **Hardware & Battery Alerts**: Animasi pengisian daya real-time, indikator persentase baterai, serta peringatan baterai lemah (*Low Battery Warning*).
5. **Gaming Mode HUD**: Deteksi aplikasi game otomatis menggunakan `UsageStatsManager` dengan layout compact adaptif berukuran minim agar tidak mengganggu area bermain.
6. **Activity & Calls Indicator**: Peringatan panggilan masuk (telepon/WhatsApp) dengan tombol jawab atau tolak cepat, serta integrasi timer.
7. **Event Priority Manager**: Mengatur prioritas tampilan cerdas:
   - *Incoming Call* (Prioritas 100)
   - *Navigation* (Prioritas 90)
   - *Notification* (Prioritas 80)
   - *Music* (Prioritas 60)
   - *Battery Alert* (Prioritas 50)
   - *System Status* (Prioritas 20)

### Teknologi yang Digunakan:
- **Bahasa**: Kotlin 1.9+
- **UI Framework**: Jetpack Compose (BOM 2024.04.00) & Material 3
- **Dependency Injection**: Hilt (Dagger)
- **Local Storage**: Jetpack DataStore Preferences
- **Asynchronous**: Kotlin Coroutines, StateFlow, & SharedFlow
- **Physics**: Spring Animation (`dampingRatio: 0.82f`, `stiffness: 350f`)
- **CI/CD Cloud Build**: GitHub Actions (Ubuntu 22.04 LTS, Temurin JDK 17)

### Target Perangkat:
- Target utama: **Xiaomi Redmi Note 9**
- Sistem Operasi: **Android 12 (API 31)**
- Antarmuka: **MIUI 13**
- Orientasi: Portrait & Landscape
- Kondisi: **Tanpa Root (Non-Root)**

---

## B. PERSYARATAN

Untuk membangun dan menggunakan Nexa Island, Anda hanya membutuhkan:
1. Akun GitHub aktif (gratis).
2. Browser web modern (Chrome, Edge, Firefox, atau Safari).
3. Source code project Nexa Island (dapat diunduh langsung dalam bentuk file ZIP melalui tombol **Export Project ZIP** di web preview ini).
4. Koneksi internet stabil.
5. Perangkat Android fisik (misalnya Xiaomi Redmi Note 9 Android 12) untuk menginstal dan menjalankan file APK hasil build.

> **Catatan Penting**: Anda **TIDAK PERLU** menginstal Android Studio, Java, atau Android SDK di komputer lokal Anda. Seluruh proses kompilasi kode menjadi APK dilakukan secara gratis dan otomatis di server GitHub Actions!

---

## C. STRUKTUR FOLDER

Proyek ini menggunakan **Gradle Multi-Module Architecture** profesional:

```text
NexaIsland/
├── app/                        # Modul aplikasi utama & entry point
│   ├── src/main/
│   │   ├── java/com/nexa/island/
│   │   │   ├── NexaApplication.kt       # Inisialisasi Hilt & Notification Channels
│   │   │   ├── MainActivity.kt          # UI Host & aktivasi Overlay Service
│   │   │   ├── service/                 # IslandOverlayService (WindowManager engine)
│   │   │   └── receiver/                # BootReceiver (Autostart setelah reboot)
│   │   ├── res/                         # Strings, Vector Icons, Themes
│   │   └── AndroidManifest.xml          # Deklarasi izin & komponen Android
│   └── build.gradle.kts
│
├── core/
│   ├── common/                 # Base Dispatchers, Result wrappers, Extensions
│   ├── design/                 # Theme, Glassmorphism components, Spring physics
│   ├── datastore/              # DataStore Preferences untuk setting island
│   ├── permissions/            # Permission Manager & MIUI 13 navigation intents
│   └── overlay/                # WindowManager layout params & touch safety
│
├── domain/                     # Model data, Priority Enums, & Repository Interface
│
├── data/                       # Implementasi Repository, Battery, & Notification Listeners
│
├── feature/
│   ├── dashboard/              # Tampilan Dashboard pengaturan & Live Preview
│   └── island/                 # Composable Dynamic Island (Compact & Expanded)
│
├── .github/
│   └── workflows/
│       ├── android-build.yml   # Workflow otomatis kompilasi APK Debug
│       └── android-release.yml # Workflow otomatis kompilasi APK Release (Signed)
│
├── gradle/
│   ├── wrapper/
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml      # Version Catalog untuk manajemen versi dependensi
│
├── build.gradle.kts            # Root Gradle configuration
├── settings.gradle.kts         # Pendaftaran seluruh modul proyek
├── gradle.properties           # Pengaturan memori Gradle & caching
├── gradlew                     # Script Gradle wrapper untuk Linux/macOS
├── gradlew.bat                 # Script Gradle wrapper untuk Windows
├── README.md                   # Dokumentasi operasional ini
└── .gitignore                  # Proteksi keamanan agar keystore tidak ter-upload
```

---

## D. LANGKAH 1 — MEMBUAT REPOSITORY GITHUB

1. Buka browser dan kunjungi [https://github.com/](https://github.com/).
2. Masuk (*Sign In*) ke akun GitHub Anda.
3. Di sudut kanan atas atau di halaman depan, klik tombol hijau **New** (atau klik tanda **+** lalu pilih **New repository**).
4. Pada kolom **Repository name**, masukkan nama:
   ```text
   NexaIsland
   ```
5. Pilih opsi visibilitas:
   - **Public**: Dapat dilihat publik (gratis unlimited build menit pada GitHub Actions).
   - **Private**: Hanya dapat dilihat oleh Anda sendiri (kuota build akun gratis 2000 menit/bulan).
6. Pada bagian *Initialize this repository with*:
   - **JANGAN** centang *Add a README file* (karena file README.md sudah tersedia dalam source code ini).
   - Biarkan *.gitignore* dan *license* dalam status **None**.
7. Klik tombol hijau **Create repository**.
8. Halaman repository kosong Anda sekarang telah siap menerima file source code.

---

## E. LANGKAH 2 — MENGUNGGAH SOURCE CODE

Anda dapat memilih salah satu dari dua metode di bawah ini:

### Metode A: Upload Melalui Browser GitHub (Paling Mudah)
1. Unduh source code project dari web preview ini dengan mengklik tombol **Export Project ZIP** di panel atas.
2. Ekstrak file ZIP hasil unduhan di komputer Anda hingga menjadi sebuah folder proyek.
3. Buka halaman repository GitHub yang baru saja Anda buat di Langkah 1.
4. Pada bagian petunjuk repositori kosong, klik tautan **uploading an existing file** (atau tombol **Add file** -> **Upload files**).
5. Buka folder proyek yang telah diekstrak, tandai (*select all*) seluruh file dan folder di dalamnya (pastikan folder `.github`, `app`, `core`, `domain`, `data`, `feature`, `gradle`, `build.gradle.kts`, `settings.gradle.kts`, `gradlew`, dll ikut terpilih), lalu seret (*drag and drop*) ke area browser GitHub.
6. Tunggu browser selesai mengunggah seluruh berkas.
7. Di bagian bawah pada kolom *Commit changes*, masukkan pesan:
   ```text
   Initial commit: Nexa Island native Android source
   ```
8. Pastikan opsi **Commit directly to the main branch** aktif, kemudian klik tombol hijau **Commit changes**.

### Metode B: Menggunakan GitHub Desktop
1. Unduh dan pasang aplikasi [GitHub Desktop](https://desktop.github.com/).
2. Buka GitHub Desktop dan login dengan akun GitHub Anda.
3. Pilih menu **File** -> **Clone Repository...**, pilih repositori `NexaIsland` yang sudah dibuat, dan tentukan lokasi folder di laptop/PC Anda.
4. Salin seluruh isi folder source code Nexa Island ke dalam folder hasil clone tersebut.
5. GitHub Desktop akan mendeteksi seluruh perubahan berkas secara otomatis.
6. Pada kotak commit di kiri bawah, ketik pesan `Initial commit: Nexa Island native project`, lalu klik **Commit to main**.
7. Klik tombol **Push origin** di bilah atas untuk mengunggah berkas ke GitHub.

---

## F. LANGKAH 3 — MEMASTIKAN SOURCE CODE SIAP

Sebelum menjalankan proses build, pastikan daftar berikut sudah terunggah dengan benar di repositori GitHub Anda:

- [x] File `settings.gradle.kts` berada di direktori root.
- [x] File `build.gradle.kts` berada di direktori root.
- [x] File `gradle.properties` berada di direktori root.
- [x] File `gradlew` dan `gradlew.bat` berada di direktori root.
- [x] Folder `gradle/wrapper/gradle-wrapper.properties` dan `gradle/libs.versions.toml` tersedia.
- [x] Modul-modul proyek (`app/`, `core/`, `domain/`, `data/`, `feature/`) memiliki struktur lengkap.
- [x] File alur kerja GitHub Actions `.github/workflows/android-build.yml` dan `.github/workflows/android-release.yml` berada di tempatnya.
- [x] Tidak ada file keystore atau file berharga yang tertinggal (aman berkat `.gitignore`).

---

## G. LANGKAH 4 — PUSH SOURCE CODE KE GITHUB

Bagi pengguna GitHub Desktop atau Terminal Git:
- **Upload**: Menaruh berkas ke direktori kerja lokal.
- **Commit**: Mengunci versi perubahan di riwayat lokal.
- **Push**: Mengirimkan riwayat commit lokal ke server repositori GitHub.

Saat proses push selesai dan masuk ke branch `main`, GitHub Actions akan otomatis mendeteksi pemicu event push pada alur kerja build!

---

## H. LANGKAH 5 — MENJALANKAN GITHUB ACTIONS DEBUG BUILD

Untuk menguji kompilasi dan mendapatkan APK Debug secara instan:

1. Buka repositori `NexaIsland` Anda di browser GitHub.
2. Klik tab **Actions** di bilah menu atas repositori.
3. Di panel sebelah kiri, klik alur kerja bernama **Android Build (Debug)**.
4. Jika proses belum otomatis berjalan, klik tombol **Run workflow** di sebelah kanan atas:
   - Pastikan pilihan branch adalah `main`.
   - Klik tombol hijau **Run workflow**.
5. Muat ulang halaman, Anda akan melihat sebuah proses baru dengan status:
   - 🟡 **Queued** (menunggu server runner siap)
   - 🟡 **In progress** (sedang mengunduh dependensi Gradle & mengkompilasi APK)
6. Klik pada nama eksekusi tersebut untuk melihat log kompilasi secara langsung.
7. Setelah sekitar 2-4 menit, proses akan selesai dengan tanda centang hijau 🟢 **Success**.

---

## I. LANGKAH 6 — MENGUNDUH APK DEBUG

1. Buka kembali halaman detail run workflow yang berstatus 🟢 **Success** tersebut.
2. Gulir ke bagian paling bawah ke subjudul **Artifacts**.
3. Anda akan melihat berkas artefak bernama:
   ```text
   NexaIsland-Debug
   ```
4. Klik pada nama `NexaIsland-Debug` untuk mengunduh file `.zip`.
5. Ekstrak file zip tersebut di komputer atau langsung di ponsel Anda.
6. Di dalamnya terdapat file `app-debug.apk`.
7. Pindahkan berkas tersebut ke ponsel Xiaomi Redmi Note 9 Anda dan pasang (*Install*).

---

## J. LANGKAH 7 — MEMBUAT ANDROID KEYSTORE UNTUK RELEASE

Untuk menghasilkan file APK Release yang dapat dipasang secara resmi, Anda membutuhkan sertifikat penandatanganan (*Keystore*).

> **PERINGATAN KEAMANAN**:
> - Jangan pernah mengunggah file `.jks` ke repositori publik!
> - Jangan pernah membagikan password keystore kepada siapapun!
> - Simpan cadangan file keystore di tempat aman (Google Drive pribadi/Flashdisk). Jika keystore hilang, Anda tidak akan bisa memperbarui aplikasi yang sama di masa depan.

### Metode: Menggunakan Perintah `keytool` (Command Prompt / Terminal)
Buka Terminal atau Command Prompt (CMD) di komputer Anda, lalu jalankan perintah berikut dalam satu baris:

```bash
keytool -genkey -v -keystore nexaisland-release.jks -alias nexaisland -keyalg RSA -keysize 2048 -validity 10000
```

Anda akan diminta memasukkan informasi:
1. *Keystore password*: Buat password yang kuat (contoh: `NexaSecretPass2026`).
2. Pertanyaan identitas (Nama, Unit Organisasi, Negara contoh: `ID`).
3. Tekan `y` untuk konfirmasi.

File baru bernama `nexaisland-release.jks` akan tercipta di direktori kerja Anda.

---

## K. LANGKAH 8 — MENGATUR GITHUB SECRETS

Agar server cloud GitHub Actions dapat menandatangani APK tanpa mengekspos kredensial Anda, kita menyimpan sertifikat dalam bentuk GitHub Encrypted Secrets.

### 1. Ubah File Keystore Menjadi Teks Base64

**Di Windows (menggunakan PowerShell):**
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("nexaisland-release.jks")) | Set-Clipboard
```
*(Hasil teks Base64 panjang akan langsung tersalin ke clipboard Anda).*

**Di Linux atau macOS (menggunakan Terminal):**
```bash
base64 -w 0 nexaisland-release.jks
# Pada beberapa macOS: base64 -b 0 nexaisland-release.jks atau cukup: base64 nexaisland-release.jks
```

### 2. Daftarkan Secrets di GitHub Repository
1. Buka repositori `NexaIsland` di GitHub.
2. Klik tab **Settings** di pojok kanan atas.
3. Di bilah menu kiri, pilih **Secrets and variables** -> klik **Actions**.
4. Klik tombol **New repository secret** untuk masing-masing 4 secret berikut:

| Secret Name | Isi Nilai (Value) |
|---|---|
| `ANDROID_KEYSTORE_BASE64` | Tempelkan (*paste*) seluruh teks Base64 panjang hasil konversi di atas |
| `ANDROID_KEYSTORE_PASSWORD` | Masukkan password keystore Anda (contoh: `NexaSecretPass2026`) |
| `ANDROID_KEY_ALIAS` | Masukkan nama alias: `nexaisland` |
| `ANDROID_KEY_PASSWORD` | Masukkan password key Anda (sama dengan password keystore) |

---

## L. LANGKAH 9 — MENJALANKAN RELEASE BUILD

1. Buka kembali tab **Actions** di repositori GitHub Anda.
2. Di kolom sebelah kiri, pilih alur kerja **Android Build (Release Signed)**.
3. Klik tombol **Run workflow** di sebelah kanan.
4. Pilih branch `main`, lalu klik tombol hijau **Run workflow**.
5. Alur kerja akan melakukan validasi keberadaan Secrets, mendekode keystore secara aman ke memori lingkungan build, menjalankan `./gradlew assembleRelease`, membersihkan file keystore sementara, dan mengemas APK yang telah ditandatangani secara kriptografis.

---

## M. LANGKAH 10 — MENGUNDUH APK RELEASE

1. Klik hasil run yang sukses pada alur kerja **Android Build (Release Signed)**.
2. Di bagian bawah (**Artifacts**), klik:
   ```text
   NexaIsland-Release
   ```
3. Unduh dan ekstrak file `.zip`.
4. Anda akan mendapatkan file APK Release resmi (contoh: `app-release.apk`).
5. Kirim file ini ke Xiaomi Redmi Note 9 Anda dan pasang!

---

## N. LANGKAH 11 — MEMBUAT GITHUB RELEASE

Setelah Anda mengunduh APK Release, Anda dapat menerbitkannya sebagai rilis resmi:
1. Buka repositori Anda di GitHub.
2. Pada panel samping kanan, klik **Releases** (atau tautan **Create a new release**).
3. Klik **Draft a new release**.
4. Klik tombol **Choose a tag**, ketik tag versi baru misalnya `v1.0.0`, lalu klik **Create new tag**.
5. Isi kolom judul rilis: `Nexa Island v1.0.0 - Production Release`.
6. Tuliskan deskripsi ringkas fitur baru.
7. Pada bagian kotak lampiran file *Attach binaries by dropping them here or selecting them*, seret dan letakkan file `app-release.apk` yang telah Anda unduh.
8. Klik tombol hijau **Publish release**.

Sekarang pengguna atau teman Anda dapat mengunduh APK langsung dari halaman rilis GitHub Anda!

---

## O. LANGKAH 12 — UPDATE APLIKASI DAN BUILD VERSI BARU

Jika Anda ingin memperbarui tampilan atau menambah fitur baru di kemudian hari:
1. Edit file Kotlin atau konfigurasi di proyek Anda.
2. Buka file `app/build.gradle.kts`.
3. Naikkan nilai versi aplikasi:
   - Ubah `versionCode = 1` menjadi `versionCode = 2`
   - Ubah `versionName = "1.0.0"` menjadi `versionName = "1.1.0"`
4. Lakukan commit dan push perubahan ke GitHub:
   ```bash
   git add .
   git commit -m "Update: improve dynamic visualizer and add battery alerts"
   git push origin main
   ```
5. Jalankan kembali workflow **Android Build (Release Signed)** di tab Actions.
6. Unduh APK Release baru dan instal di ponsel tanpa perlu menghapus aplikasi versi lama terlebih dahulu (data dan pengaturan tersimpan utuh).

---

## P. TROUBLESHOOTING

| Gejala Masalah | Penyebab Utama | Solusi Perbaikan |
|---|---|---|
| **Tab Actions tidak menampilkan workflow** | File YAML belum berada di folder `.github/workflows/` | Pastikan struktur folder persis `.github/workflows/android-build.yml`. Periksa apakah folder `.github` tidak sengaja terlewat saat upload browser. |
| **Error: ANDROID_KEYSTORE_BASE64 is missing** | Secret belum ditambahkan di menu Settings repositori | Buka *Settings* -> *Secrets and variables* -> *Actions* dan buat 4 nama secrets sesuai tabel Langkah 8. |
| **Decoded keystore file is empty / corrupted** | Hasil copy-paste Base64 terpotong atau terdapat spasi ekstra | Ulangi perintah Base64 di PowerShell/Terminal, pastikan seluruh string tersalin utuh tanpa jeda baris. |
| **Overlay tidak muncul di layar** | Izin "Display over other apps" belum aktif | Buka menu *Settings* -> *Apps* -> *Manage Apps* -> *Nexa Island* -> aktifkan *Display pop-up windows while running in the background* / *Display over other apps*. |
| **MIUI mematikan aplikasi setelah beberapa menit** | Optimasi baterai agresif bawaan MIUI | Buka menu *Battery Saver* di Info Aplikasi Nexa Island, ubah menjadi **No restrictions**. |
| **Notifikasi chat tidak terbaca** | Izin Notification Listener belum diberikan | Masuk ke dashboard Nexa Island dan klik **Grant** pada baris Notification Listener. |

---

## Q. PANDUAN REDMI NOTE 9 & MIUI 13

Untuk memastikan performa mulus 60 FPS dan stabilitas latar belakang di MIUI 13:

1. **Izinkan Tampil di Atas Aplikasi Lain (*Display over other apps*)**:
   - Buka *Settings (Pengaturan)* -> *Apps (Aplikasi)* -> *Manage Apps (Kelola Aplikasi)*.
   - Temukan **Nexa Island**.
   - Cari opsi **Other permissions (Izin lainnya)** -> Aktifkan centang hijau pada **Display pop-up window**.
2. **Aktifkan Autostart (Mulai Otomatis)**:
   - Pada halaman info aplikasi yang sama, aktifkan tombol sakelar **Autostart**.
3. **Pengaturan Penghemat Baterai MIUI (*Battery Saver*)**:
   - Pilih menu **Battery saver** -> ubah dari *MIUI Battery saver (recommended)* menjadi **No restrictions (Tidak ada pembatasan)**.
4. **Kunci Aplikasi di Recent Apps**:
   - Buka tampilan *Recent Apps* (Multitasking).
   - Tekan dan tahan kartu aplikasi Nexa Island, lalu klik ikon **Gembok (Lock)** agar MIUI tidak membersihkan proses saat pembersihan RAM sistem.

---

## R. DEVELOPMENT GUIDE

### Menambahkan Fitur / Modul Baru:
1. Daftarkan modul baru pada `settings.gradle.kts`:
   ```kotlin
   include(":feature:customfeature")
   ```
2. Buat file `feature/customfeature/build.gradle.kts` dengan plugin `android.library`, `kotlin.android`, dan `kotlin.compose`.
3. Terapkan prinsip Clean Architecture:
   - Simpan entitas data murni di modul `:domain`.
   - Simpan event collector di modul `:data`.
   - Implementasikan UI Composable di modul `:feature:xxx`.
4. Berikan anotasi `@HiltViewModel` pada ViewModel dan `@Inject` pada use case atau repository.

---

## S. KNOWN LIMITATIONS

1. **Android 12 Untrusted Touch Blocking**:
   Android 12 memperkenalkan keamanan baru yang memblokir overlay transparan layar penuh agar tidak dapat melakukan *tapjacking*. Nexa Island mematuhi pedoman resmi dengan hanya mengalokasikan area interaksi fisik selebar island itu sendiri.
2. **MediaSession App Compatibility**:
   Aplikasi pemutar musik pihak ketiga harus mengimplementasikan standar resmi `android.media.session.MediaSession` agar metadata lagu dan kendali pemutaran dapat diakses.
3. **MIUI Game Turbo**:
   Pada beberapa versi MIUI, fitur Game Turbo dapat menyembunyikan overlay pihak ketiga selama mode game kompetitif aktif.

---

## T. ROADMAP DAN LICENSE

### Roadmap:
- [x] Multi-Module Gradle Architecture & Version Catalog
- [x] Dark Frosted Glassmorphism UI System
- [x] MediaSession integration dengan visualizer gelombang audio
- [x] Bridge notifikasi WhatsApp, Telegram, dan sistem
- [x] Indikator status pengisian daya baterai
- [x] Workflow otomatis GitHub Actions CI/CD (Debug & Release)
- [ ] Integrasi sensor gesture proximity kamera depan
- [ ] Tema aksen dinamis Material You (Monet dynamic colors)

### License
Didistribusikan di bawah lisensi **Apache License 2.0**. Dibuat dengan dedikasi untuk komunitas open-source Android.
