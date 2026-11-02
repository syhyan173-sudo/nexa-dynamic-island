import React from 'react';
import { Sliders, Palette, Zap, Sparkles, Check } from 'lucide-react';

interface IslandCustomizerProps {
  islandWidth: number;
  setIslandWidth: (w: number) => void;
  islandHeight: number;
  setIslandHeight: (h: number) => void;
  cornerRadius: number;
  setCornerRadius: (r: number) => void;
  accentColor: string;
  setAccentColor: (c: string) => void;
  isMusicEnabled: boolean;
  setIsMusicEnabled: (v: boolean) => void;
  isNotifEnabled: boolean;
  setIsNotifEnabled: (v: boolean) => void;
  isGamingEnabled: boolean;
  setIsGamingEnabled: (v: boolean) => void;
}

export const IslandCustomizer: React.FC<IslandCustomizerProps> = ({
  islandWidth,
  setIslandWidth,
  islandHeight,
  setIslandHeight,
  cornerRadius,
  setCornerRadius,
  accentColor,
  setAccentColor,
  isMusicEnabled,
  setIsMusicEnabled,
  isNotifEnabled,
  setIsNotifEnabled,
  isGamingEnabled,
  setIsGamingEnabled,
}) => {
  const colorOptions = [
    { name: 'Nexa Purple', hex: '#A855F7' },
    { name: 'Cyan Glow', hex: '#06B6D4' },
    { name: 'Emerald Wave', hex: '#10B981' },
    { name: 'Rose Neon', hex: '#F43F5E' },
    { name: 'Amber Sunset', hex: '#F59E0B' },
  ];

  return (
    <div className="bg-[#0f172a]/90 backdrop-blur-xl rounded-3xl border border-white/10 p-6 shadow-2xl space-y-6">
      <div className="flex items-center justify-between pb-3 border-b border-white/10">
        <div>
          <h3 className="text-base font-bold text-white flex items-center gap-2">
            <Sliders className="w-4 h-4 text-purple-400" />
            Island Dimensions & Geometry
          </h3>
          <p className="text-xs text-gray-400">
            Real-time parameter tuning applied to Compose layout params
          </p>
        </div>
      </div>

      {/* Sliders */}
      <div className="space-y-4">
        {/* Width */}
        <div className="space-y-1.5">
          <div className="flex justify-between text-xs">
            <span className="text-gray-300">Compact Width</span>
            <span className="font-mono text-purple-400 font-bold">{islandWidth} dp</span>
          </div>
          <input
            type="range"
            min={150}
            max={260}
            value={islandWidth}
            onChange={(e) => setIslandWidth(Number(e.target.value))}
            className="w-full h-1.5 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-purple-500"
          />
        </div>

        {/* Height */}
        <div className="space-y-1.5">
          <div className="flex justify-between text-xs">
            <span className="text-gray-300">Compact Height</span>
            <span className="font-mono text-cyan-400 font-bold">{islandHeight} dp</span>
          </div>
          <input
            type="range"
            min={32}
            max={50}
            value={islandHeight}
            onChange={(e) => setIslandHeight(Number(e.target.value))}
            className="w-full h-1.5 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-cyan-500"
          />
        </div>

        {/* Corner Radius */}
        <div className="space-y-1.5">
          <div className="flex justify-between text-xs">
            <span className="text-gray-300">Corner Radius</span>
            <span className="font-mono text-emerald-400 font-bold">{cornerRadius} dp</span>
          </div>
          <input
            type="range"
            min={14}
            max={28}
            value={cornerRadius}
            onChange={(e) => setCornerRadius(Number(e.target.value))}
            className="w-full h-1.5 bg-gray-700 rounded-lg appearance-none cursor-pointer accent-emerald-500"
          />
        </div>
      </div>

      {/* Accent Color Palette */}
      <div className="space-y-2 pt-2 border-t border-white/5">
        <label className="text-xs font-semibold text-gray-300 flex items-center gap-1.5">
          <Palette className="w-3.5 h-3.5 text-purple-400" />
          Dark Glass Accent Color
        </label>
        <div className="flex items-center gap-2">
          {colorOptions.map((c) => (
            <button
              key={c.hex}
              onClick={() => setAccentColor(c.hex)}
              className="group relative w-8 h-8 rounded-full flex items-center justify-center transition-all transform hover:scale-110"
              style={{ backgroundColor: c.hex }}
              title={c.name}
            >
              {accentColor === c.hex && <Check className="w-4 h-4 text-white drop-shadow" />}
            </button>
          ))}
        </div>
      </div>

      {/* Feature Toggles */}
      <div className="space-y-2.5 pt-3 border-t border-white/5">
        <label className="text-xs font-semibold text-gray-300">Feature Activation (DataStore)</label>
        
        <div className="space-y-2">
          <div className="flex items-center justify-between p-2 rounded-xl bg-white/5 border border-white/5">
            <span className="text-xs text-gray-300">MediaSession Music Integration</span>
            <input
              type="checkbox"
              checked={isMusicEnabled}
              onChange={(e) => setIsMusicEnabled(e.target.checked)}
              className="w-4 h-4 rounded text-purple-600 focus:ring-0 bg-gray-800 border-gray-600 cursor-pointer"
            />
          </div>

          <div className="flex items-center justify-between p-2 rounded-xl bg-white/5 border border-white/5">
            <span className="text-xs text-gray-300">Notification Bridge Listener</span>
            <input
              type="checkbox"
              checked={isNotifEnabled}
              onChange={(e) => setIsNotifEnabled(e.target.checked)}
              className="w-4 h-4 rounded text-purple-600 focus:ring-0 bg-gray-800 border-gray-600 cursor-pointer"
            />
          </div>

          <div className="flex items-center justify-between p-2 rounded-xl bg-white/5 border border-white/5">
            <span className="text-xs text-gray-300">Gaming Mode (UsageStats)</span>
            <input
              type="checkbox"
              checked={isGamingEnabled}
              onChange={(e) => setIsGamingEnabled(e.target.checked)}
              className="w-4 h-4 rounded text-purple-600 focus:ring-0 bg-gray-800 border-gray-600 cursor-pointer"
            />
          </div>
        </div>
      </div>
    </div>
  );
};
