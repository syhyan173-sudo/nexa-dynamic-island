import React, { useState } from 'react';
import JSZip from 'jszip';
import confetti from 'canvas-confetti';
import { ANDROID_PROJECT_FILES } from '../data/projectFiles';
import { Download, CheckCircle, Loader2, Sparkles, FolderArchive } from 'lucide-react';

interface ZipExporterProps {
  onSuccess?: () => void;
}

export const exportAndroidProjectZip = async (
  onProgress?: (progress: number) => void
): Promise<void> => {
  const zip = new JSZip();

  // Populate all files into zip structure
  ANDROID_PROJECT_FILES.forEach((file) => {
    zip.file(file.path, file.content);
  });

  // Generate binary zip
  const blob = await zip.generateAsync({ type: 'blob' }, (metadata) => {
    if (onProgress) {
      onProgress(metadata.percent);
    }
  });

  // Trigger browser download
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'NexaIsland-Android-Source.zip';
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
};

export const ZipExporterButton: React.FC<ZipExporterProps> = ({ onSuccess }) => {
  const [isExporting, setIsExporting] = useState(false);
  const [progress, setProgress] = useState(0);

  const handleDownload = async () => {
    try {
      setIsExporting(true);
      setProgress(10);

      await exportAndroidProjectZip((p) => setProgress(Math.round(p)));

      // Trigger celebration confetti
      confetti({
        particleCount: 80,
        spread: 60,
        origin: { y: 0.6 },
      });

      if (onSuccess) onSuccess();
    } catch (e) {
      console.error('Failed to export zip', e);
    } finally {
      setTimeout(() => {
        setIsExporting(false);
        setProgress(0);
      }, 1500);
    }
  };

  return (
    <button
      onClick={handleDownload}
      disabled={isExporting}
      className="flex items-center gap-2 px-5 py-2.5 rounded-2xl bg-gradient-to-r from-purple-600 via-indigo-600 to-cyan-500 hover:from-purple-500 hover:to-cyan-400 text-white font-bold text-xs shadow-xl shadow-purple-600/30 transition transform hover:scale-105 active:scale-95 disabled:opacity-75"
    >
      {isExporting ? (
        <>
          <Loader2 className="w-4 h-4 animate-spin" />
          <span>Packaging Project ({progress}%)...</span>
        </>
      ) : (
        <>
          <FolderArchive className="w-4 h-4" />
          <span>Export Complete Project (ZIP)</span>
          <Sparkles className="w-3.5 h-3.5 text-yellow-300 animate-pulse" />
        </>
      )}
    </button>
  );
};
