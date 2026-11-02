import React, { useState } from 'react';
import {
  GitBranch,
  KeyRound,
  ShieldCheck,
  Download,
  Terminal,
  Copy,
  Check,
  ExternalLink,
  ChevronRight,
  Sparkles,
  Smartphone
} from 'lucide-react';

export const GitHubGuideModal: React.FC = () => {
  const [activeStep, setActiveStep] = useState(1);
  const [copiedKey, setCopiedKey] = useState<string | null>(null);

  const copyToClipboard = (text: string, key: string) => {
    navigator.clipboard.writeText(text);
    setCopiedKey(key);
    setTimeout(() => setCopiedKey(null), 2000);
  };

  const steps = [
    {
      num: 1,
      title: 'Buat Repository GitHub',
      subtitle: 'Membuat tempat penampungan kode di GitHub',
      content: (
        <div className="space-y-3 text-xs text-gray-300">
          <p>
            1. Buka browser dan login ke{' '}
            <a
              href="https://github.com/new"
              target="_blank"
              rel="noreferrer"
              className="text-cyan-400 underline inline-flex items-center gap-1"
            >
              github.com/new <ExternalLink className="w-3 h-3" />
            </a>
            .
          </p>
          <p>
            2. Isi <strong>Repository name</strong> dengan: <code className="bg-purple-900/40 text-purple-300 px-2 py-0.5 rounded font-mono">NexaIsland</code>.
          </p>
          <p>3. Pilih <strong>Public</strong> (rekomendasi kuota build tak terbatas) atau <strong>Private</strong>.</p>
          <p>4. <strong>JANGAN</strong> centang "Add a README file" atau .gitignore (sudah disediakan lengkap dalam project).</p>
          <p>5. Klik tombol hijau <strong>Create repository</strong>.</p>
        </div>
      ),
    },
    {
      num: 2,
      title: 'Unggah Source Code',
      subtitle: 'Upload berkas proyek ke repository',
      content: (
        <div className="space-y-3 text-xs text-gray-300">
          <p>1. Klik tombol <strong>Export Project (ZIP)</strong> di aplikasi ini untuk mengunduh seluruh source code.</p>
          <p>2. Ekstrak file ZIP di komputer Anda.</p>
          <p>
            3. Pada halaman repositori GitHub Anda, klik <strong>uploading an existing file</strong>.
          </p>
          <p>
            4. Drag & drop seluruh isi folder (pastikan folder <code className="text-cyan-300 font-mono">.github</code>, <code className="text-cyan-300 font-mono">app</code>, <code className="text-cyan-300 font-mono">gradle</code>, dan file konfigurasi ikut terunggah).
          </p>
          <p>5. Tulis commit message: <code className="bg-black/40 text-gray-200 px-2 py-0.5 rounded">Initial commit: Nexa Island native project</code>.</p>
          <p>6. Klik tombol hijau <strong>Commit changes</strong>.</p>
        </div>
      ),
    },
    {
      num: 3,
      title: 'Kompilasi APK Debug Otomatis',
      subtitle: 'Menjalankan GitHub Actions Debug workflow',
      content: (
        <div className="space-y-3 text-xs text-gray-300">
          <p>1. Di repositori Anda, klik tab <strong>Actions</strong> di menu atas.</p>
          <p>2. Pilih alur kerja <strong>Android Build (Debug)</strong> di panel sebelah kiri.</p>
          <p>3. Klik tombol <strong>Run workflow</strong> pada branch <code className="font-mono text-purple-300">main</code>.</p>
          <p>4. Tunggu 2-3 menit hingga proses menampilkan status centang hijau 🟢 <strong>Success</strong>.</p>
          <p>
            5. Klik run tersebut, gulir ke bawah ke bagian <strong>Artifacts</strong>, lalu unduh{' '}
            <strong className="text-emerald-400">NexaIsland-Debug</strong> (file ZIP berisi <code className="font-mono">app-debug.apk</code>).
          </p>
        </div>
      ),
    },
    {
      num: 4,
      title: 'Buat Keystore & GitHub Secrets (Release Signed)',
      subtitle: 'Menyiapkan sertifikat tanda tangan digital untuk rilis resmi',
      content: (
        <div className="space-y-4 text-xs text-gray-300">
          <div>
            <span className="font-semibold text-white block mb-1">Perintah Keytool (Jalankan di Terminal / CMD):</span>
            <div className="relative bg-[#06090e] p-2.5 rounded-xl border border-white/10 font-mono text-[11px] text-cyan-300 flex items-center justify-between">
              <span className="overflow-x-auto whitespace-pre pr-2">
                keytool -genkey -v -keystore nexaisland-release.jks -alias nexaisland -keyalg RSA -keysize 2048 -validity 10000
              </span>
              <button
                onClick={() =>
                  copyToClipboard(
                    'keytool -genkey -v -keystore nexaisland-release.jks -alias nexaisland -keyalg RSA -keysize 2048 -validity 10000',
                    'keytool'
                  )
                }
                className="shrink-0 p-1.5 rounded-lg bg-white/10 hover:bg-white/20 text-gray-300 transition"
              >
                {copiedKey === 'keytool' ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
              </button>
            </div>
          </div>

          <div>
            <span className="font-semibold text-white block mb-1">Ubah Keystore Jadi Base64 (Windows PowerShell):</span>
            <div className="relative bg-[#06090e] p-2.5 rounded-xl border border-white/10 font-mono text-[11px] text-purple-300 flex items-center justify-between">
              <span className="overflow-x-auto whitespace-pre pr-2">
                [Convert]::ToBase64String([IO.File]::ReadAllBytes("nexaisland-release.jks")) | Set-Clipboard
              </span>
              <button
                onClick={() =>
                  copyToClipboard(
                    '[Convert]::ToBase64String([IO.File]::ReadAllBytes("nexaisland-release.jks")) | Set-Clipboard',
                    'powershell'
                  )
                }
                className="shrink-0 p-1.5 rounded-lg bg-white/10 hover:bg-white/20 text-gray-300 transition"
              >
                {copiedKey === 'powershell' ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
              </button>
            </div>
          </div>

          <div>
            <span className="font-semibold text-white block mb-1">Daftarkan 4 Secret di GitHub (Settings &gt; Secrets and variables &gt; Actions):</span>
            <ul className="list-disc list-inside space-y-1 text-gray-400">
              <li><strong className="text-gray-200">ANDROID_KEYSTORE_BASE64</strong>: Paste hasil Base64 panjang.</li>
              <li><strong className="text-gray-200">ANDROID_KEYSTORE_PASSWORD</strong>: Password yang Anda buat.</li>
              <li><strong className="text-gray-200">ANDROID_KEY_ALIAS</strong>: <code className="text-cyan-300">nexaisland</code></li>
              <li><strong className="text-gray-200">ANDROID_KEY_PASSWORD</strong>: Password alias yang Anda buat.</li>
            </ul>
          </div>
        </div>
      ),
    },
    {
      num: 5,
      title: 'Build APK Release & Publikasi GitHub Release',
      subtitle: 'Menghasilkan APK Release yang ditandatangani dan dipublikasikan',
      content: (
        <div className="space-y-3 text-xs text-gray-300">
          <p>1. Buka tab <strong>Actions</strong>, pilih workflow <strong>Android Build (Release Signed)</strong>.</p>
          <p>2. Klik <strong>Run workflow</strong> pada branch <code className="font-mono text-purple-300">main</code>.</p>
          <p>
            3. Saat selesai, unduh artifact bernama <strong className="text-cyan-400">NexaIsland-Release</strong> yang berisi berkas{' '}
            <code className="font-mono">app-release.apk</code>.
          </p>
          <p>4. Buka menu <strong>Releases</strong> di repository Anda, klik <strong>Create a new release</strong>.</p>
          <p>5. Buat tag <code className="font-mono text-purple-300">v1.0.0</code>, lampirkan file <code className="font-mono">app-release.apk</code>, lalu klik <strong>Publish release</strong>!</p>
        </div>
      ),
    },
    {
      num: 6,
      title: 'Optimasi Xiaomi Redmi Note 9 (MIUI 13)',
      subtitle: 'Pengaturan latar belakang agar overlay selalu aktif',
      content: (
        <div className="space-y-3 text-xs text-gray-300">
          <div className="p-3 rounded-xl bg-amber-500/10 border border-amber-500/30 text-amber-200">
            ⚠️ <strong>Perhatian MIUI 13:</strong> MIUI memiliki manajemen baterai agresif yang dapat menutup aplikasi background jika izin tidak diatur.
          </div>
          <p>
            1. <strong>Display over other apps:</strong> Buka <em>Settings &gt; Apps &gt; Manage Apps &gt; Nexa Island &gt; Other permissions &gt; Display pop-up window</em> (Aktifkan).
          </p>
          <p>
            2. <strong>Autostart:</strong> Pada info aplikasi Nexa Island, aktifkan tombol sakelar <strong>Autostart</strong>.
          </p>
          <p>
            3. <strong>Battery Saver:</strong> Ubah dari <em>MIUI Battery Saver</em> menjadi <strong>No restrictions</strong>.
          </p>
          <p>
            4. <strong>Lock in Recent Apps:</strong> Buka multitasking, tahan kartu Nexa Island, lalu klik ikon <strong>Gembok (Lock)</strong>.
          </p>
        </div>
      ),
    },
  ];

  return (
    <div className="bg-[#0f172a]/95 backdrop-blur-2xl rounded-3xl border border-white/10 p-6 shadow-2xl">
      <div className="flex items-center justify-between pb-4 border-b border-white/10">
        <div>
          <h3 className="text-lg font-bold text-white flex items-center gap-2">
            <GitBranch className="w-5 h-5 text-cyan-400" />
            Panduan Lengkap: Dari Nol Hingga APK Release
          </h3>
          <p className="text-xs text-gray-400 mt-0.5">
            Ikuti 6 tahap mudah berikut untuk mengompilasi APK langsung dari GitHub Actions
          </p>
        </div>
      </div>

      {/* Step Numbers Indicator */}
      <div className="grid grid-cols-6 gap-2 py-4">
        {steps.map((s) => (
          <button
            key={s.num}
            onClick={() => setActiveStep(s.num)}
            className={`py-2 px-1 rounded-xl text-center transition flex flex-col items-center gap-1 border ${
              activeStep === s.num
                ? 'bg-purple-600/30 border-purple-500 text-white shadow-lg shadow-purple-600/20'
                : 'bg-white/5 border-white/5 text-gray-400 hover:bg-white/10 hover:text-white'
            }`}
          >
            <span
              className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold ${
                activeStep === s.num ? 'bg-purple-500 text-white' : 'bg-gray-800 text-gray-400'
              }`}
            >
              {s.num}
            </span>
            <span className="text-[10px] font-medium truncate max-w-full hidden sm:block">
              {s.title.split(' ')[0]}
            </span>
          </button>
        ))}
      </div>

      {/* Step Content Card */}
      <div className="bg-[#0b0f19] rounded-2xl border border-white/10 p-5 mt-2 min-h-[280px]">
        <div className="flex items-center justify-between pb-3 mb-3 border-b border-white/5">
          <div>
            <h4 className="text-sm font-bold text-white flex items-center gap-2">
              <span className="w-5 h-5 rounded-md bg-purple-500/20 text-purple-400 flex items-center justify-center text-xs">
                {activeStep}
              </span>
              {steps[activeStep - 1].title}
            </h4>
            <p className="text-[11px] text-gray-400">{steps[activeStep - 1].subtitle}</p>
          </div>
        </div>

        {steps[activeStep - 1].content}
      </div>

      {/* Navigation Buttons */}
      <div className="flex items-center justify-between pt-4 mt-2">
        <button
          disabled={activeStep === 1}
          onClick={() => setActiveStep((prev) => Math.max(1, prev - 1))}
          className="px-4 py-2 rounded-xl text-xs font-medium text-gray-400 hover:text-white disabled:opacity-30 disabled:hover:text-gray-400 transition"
        >
          ← Sebelumnya
        </button>

        <span className="text-xs text-gray-500">
          Langkah {activeStep} dari {steps.length}
        </span>

        <button
          disabled={activeStep === steps.length}
          onClick={() => setActiveStep((prev) => Math.min(steps.length, prev + 1))}
          className="px-4 py-2 rounded-xl bg-purple-600 hover:bg-purple-500 text-white text-xs font-semibold disabled:opacity-30 transition"
        >
          Selanjutnya →
        </button>
      </div>
    </div>
  );
};
