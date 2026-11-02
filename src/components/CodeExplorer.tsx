import React, { useState } from 'react';
import { ANDROID_PROJECT_FILES, ProjectFile } from '../data/projectFiles';
import { FileCode, Copy, Check, Folder, ChevronRight, Download } from 'lucide-react';

interface CodeExplorerProps {
  onExportZip: () => void;
}

export const CodeExplorer: React.FC<CodeExplorerProps> = ({ onExportZip }) => {
  const [selectedFile, setSelectedFile] = useState<ProjectFile>(ANDROID_PROJECT_FILES[0]);
  const [copied, setCopied] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<string>('all');

  const categories = [
    { id: 'all', label: 'All Files' },
    { id: 'config', label: 'Gradle Config' },
    { id: 'workflow', label: 'GitHub Workflows' },
    { id: 'app', label: 'App Module' },
    { id: 'core', label: 'Core Modules' },
    { id: 'domain', label: 'Domain' },
    { id: 'docs', label: 'Documentation' },
  ];

  const filteredFiles = ANDROID_PROJECT_FILES.filter(
    (f) => selectedCategory === 'all' || f.category === selectedCategory
  );

  const handleCopy = () => {
    navigator.clipboard.writeText(selectedFile.content);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="bg-[#0f172a]/90 backdrop-blur-xl rounded-3xl border border-white/10 p-6 shadow-2xl flex flex-col h-[740px]">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between pb-4 border-b border-white/10 gap-3">
        <div>
          <h3 className="text-lg font-bold text-white flex items-center gap-2">
            <Folder className="w-5 h-5 text-purple-400" />
            Android Multi-Module Project Structure
          </h3>
          <p className="text-xs text-gray-400 mt-0.5">
            100% production-ready Gradle Kotlin DSL & Clean Architecture source code
          </p>
        </div>

        <button
          onClick={onExportZip}
          className="flex items-center gap-2 px-4 py-2 rounded-xl bg-gradient-to-r from-purple-600 to-cyan-600 hover:from-purple-500 hover:to-cyan-500 text-white font-semibold text-xs shadow-lg shadow-purple-600/30 transition transform hover:scale-[1.02]"
        >
          <Download className="w-4 h-4" />
          Export Project (ZIP)
        </button>
      </div>

      {/* Category Pills */}
      <div className="flex items-center gap-2 py-3 overflow-x-auto no-scrollbar border-b border-white/5">
        {categories.map((c) => (
          <button
            key={c.id}
            onClick={() => setSelectedCategory(c.id)}
            className={`px-3 py-1 rounded-lg text-xs font-medium whitespace-nowrap transition ${
              selectedCategory === c.id
                ? 'bg-purple-600 text-white'
                : 'bg-white/5 text-gray-400 hover:text-white hover:bg-white/10'
            }`}
          >
            {c.label}
          </button>
        ))}
      </div>

      {/* Content Area: Left file list, Right code preview */}
      <div className="grid grid-cols-1 md:grid-cols-12 gap-4 flex-1 min-h-0 pt-4">
        {/* File Tree List */}
        <div className="md:col-span-4 overflow-y-auto space-y-1.5 pr-2">
          {filteredFiles.map((file) => {
            const isSelected = selectedFile.path === file.path;
            return (
              <button
                key={file.path}
                onClick={() => setSelectedFile(file)}
                className={`w-full text-left px-3 py-2.5 rounded-xl text-xs flex items-center justify-between transition group ${
                  isSelected
                    ? 'bg-purple-600/20 text-purple-300 border border-purple-500/40 shadow-sm'
                    : 'bg-white/5 text-gray-300 border border-transparent hover:bg-white/10 hover:text-white'
                }`}
              >
                <div className="flex items-center gap-2 truncate">
                  <FileCode
                    className={`w-4 h-4 shrink-0 ${
                      isSelected ? 'text-purple-400' : 'text-gray-400 group-hover:text-cyan-400'
                    }`}
                  />
                  <span className="truncate font-mono">{file.path}</span>
                </div>
                <ChevronRight
                  className={`w-3.5 h-3.5 shrink-0 transition ${
                    isSelected ? 'opacity-100 text-purple-400' : 'opacity-0 group-hover:opacity-60'
                  }`}
                />
              </button>
            );
          })}
        </div>

        {/* Code Previewer */}
        <div className="md:col-span-8 flex flex-col bg-[#0b0f19] rounded-2xl border border-white/10 overflow-hidden min-h-0">
          {/* File Tab Bar */}
          <div className="px-4 py-2.5 bg-black/40 border-b border-white/10 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="w-2.5 h-2.5 rounded-full bg-rose-500/80 inline-block" />
              <span className="w-2.5 h-2.5 rounded-full bg-amber-500/80 inline-block" />
              <span className="w-2.5 h-2.5 rounded-full bg-emerald-500/80 inline-block" />
              <span className="font-mono text-xs text-gray-300 ml-2 font-medium">
                {selectedFile.path}
              </span>
            </div>

            <button
              onClick={handleCopy}
              className="flex items-center gap-1.5 px-3 py-1 rounded-lg bg-white/10 hover:bg-white/20 text-gray-300 text-xs transition"
            >
              {copied ? (
                <>
                  <Check className="w-3.5 h-3.5 text-emerald-400" />
                  <span className="text-emerald-400">Copied!</span>
                </>
              ) : (
                <>
                  <Copy className="w-3.5 h-3.5" />
                  <span>Copy Code</span>
                </>
              )}
            </button>
          </div>

          {/* Syntax / Code content */}
          <div className="flex-1 overflow-auto p-4 font-mono text-xs text-gray-300 leading-relaxed bg-[#06090e]">
            <pre className="whitespace-pre overflow-x-auto">{selectedFile.content}</pre>
          </div>
        </div>
      </div>
    </div>
  );
};
