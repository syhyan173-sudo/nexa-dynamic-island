import React, { useState, useEffect } from 'react';
import {
  Play,
  Pause,
  SkipForward,
  SkipBack,
  Volume2,
  BatteryCharging,
  Wifi,
  PhoneCall,
  PhoneOff,
  Bell,
  Gamepad2,
  X,
  Maximize2,
  Minimize2,
  MessageSquare,
  Sparkles
} from 'lucide-react';

export type EventType = 'idle' | 'music' | 'notification' | 'battery' | 'call' | 'gaming';

interface PhoneSimulatorProps {
  islandWidth: number;
  islandHeight: number;
  cornerRadius: number;
  accentColor: string;
  isExpanded: boolean;
  onToggleExpand: () => void;
  activeEvent: EventType;
  setActiveEvent: (event: EventType) => void;
}

export const PhoneSimulator: React.FC<PhoneSimulatorProps> = ({
  islandWidth,
  islandHeight,
  cornerRadius,
  accentColor,
  isExpanded,
  onToggleExpand,
  activeEvent,
  setActiveEvent,
}) => {
  const [isPlaying, setIsPlaying] = useState(true);
  const [musicProgress, setMusicProgress] = useState(38);
  const [cameraPosition, setCameraPosition] = useState<'center' | 'left'>('center');
  const [batteryLevel, setBatteryLevel] = useState(84);
  const [isCharging, setIsCharging] = useState(true);

  // Music progress timer simulation
  useEffect(() => {
    if (!isPlaying) return;
    const interval = setInterval(() => {
      setMusicProgress((prev) => (prev >= 100 ? 0 : prev + 1));
    }, 1000);
    return () => clearInterval(interval);
  }, [isPlaying]);

  return (
    <div className="flex flex-col items-center">
      {/* Phone Hardware Shell (Redmi Note 9 profile) */}
      <div className="relative w-[340px] h-[680px] bg-[#0c1017] rounded-[48px] border-[6px] border-[#222a36] shadow-2xl shadow-purple-950/30 overflow-hidden flex flex-col select-none">
        
        {/* Subtle screen reflection gradient */}
        <div className="absolute inset-0 pointer-events-none bg-gradient-to-tr from-transparent via-white/[0.02] to-transparent z-40" />

        {/* Status Bar */}
        <div className="relative w-full h-8 px-6 pt-2 flex items-center justify-between text-[11px] font-semibold text-gray-300 z-30">
          <span>09:41</span>
          
          {/* Punch-hole camera */}
          <div
            className={`absolute top-2 w-3.5 h-3.5 bg-black rounded-full border border-gray-900 shadow-inner z-50 transition-all duration-300 ${
              cameraPosition === 'center' ? 'left-1/2 -translate-x-1/2' : 'left-8'
            }`}
          />

          <div className="flex items-center gap-1.5 text-gray-400">
            <Wifi className="w-3.5 h-3.5" />
            <span className="text-[10px]">5G</span>
            <div className="flex items-center gap-0.5">
              <span className="text-[10px]">{batteryLevel}%</span>
              {isCharging && <BatteryCharging className="w-3.5 h-3.5 text-emerald-400" />}
            </div>
          </div>
        </div>

        {/* Dynamic Island Floating Overlay Container */}
        <div className="relative w-full flex justify-center items-start pt-1 z-30 min-h-[140px]">
          <div
            onClick={onToggleExpand}
            style={{
              width: isExpanded ? '92%' : `${islandWidth}px`,
              minHeight: isExpanded ? '150px' : `${islandHeight}px`,
              borderRadius: isExpanded ? '30px' : `${cornerRadius}px`,
              borderColor: `${accentColor}33`,
            }}
            className="cursor-pointer transition-all duration-300 ease-[cubic-bezier(0.25,1,0.5,1)] bg-[#0f172a]/90 backdrop-blur-xl border shadow-xl shadow-black/60 text-white overflow-hidden p-2 flex flex-col justify-center relative hover:border-white/30"
          >
            {/* Specular highlight rim */}
            <div className="absolute top-0 inset-x-0 h-[1px] bg-gradient-to-r from-transparent via-white/20 to-transparent" />

            {/* COMPACT MODE */}
            {!isExpanded && (
              <div className="flex items-center justify-between px-2 w-full h-full text-xs">
                {activeEvent === 'music' && (
                  <>
                    <div className="flex items-center gap-2 overflow-hidden">
                      <div
                        className="w-5 h-5 rounded-full flex items-center justify-center shrink-0"
                        style={{ backgroundColor: `${accentColor}40` }}
                      >
                        <Volume2 className="w-3 h-3" style={{ color: accentColor }} />
                      </div>
                      <span className="truncate font-medium text-[11px] max-w-[90px]">
                        Starboy
                      </span>
                    </div>

                    {/* Animated Equalizer Waves */}
                    <div className="flex items-end gap-[2px] h-3">
                      <span
                        className={`w-[2px] rounded-full bg-cyan-400 transition-all ${
                          isPlaying ? 'animate-bounce h-3.5' : 'h-1.5'
                        }`}
                      />
                      <span
                        className={`w-[2px] rounded-full transition-all ${
                          isPlaying ? 'animate-pulse h-2' : 'h-1'
                        }`}
                        style={{ backgroundColor: accentColor }}
                      />
                      <span
                        className={`w-[2px] rounded-full bg-cyan-400 transition-all ${
                          isPlaying ? 'animate-bounce h-3' : 'h-2'
                        }`}
                      />
                    </div>
                  </>
                )}

                {activeEvent === 'notification' && (
                  <>
                    <div className="flex items-center gap-1.5">
                      <div className="w-5 h-5 rounded-full bg-emerald-500/20 flex items-center justify-center">
                        <MessageSquare className="w-3 h-3 text-emerald-400" />
                      </div>
                      <span className="font-semibold text-emerald-300 text-[11px]">WhatsApp</span>
                    </div>
                    <span className="text-gray-400 text-[11px] truncate max-w-[90px]">
                      Sarah: Hey! 🔥
                    </span>
                  </>
                )}

                {activeEvent === 'battery' && (
                  <>
                    <div className="flex items-center gap-1.5">
                      <BatteryCharging className="w-3.5 h-3.5 text-emerald-400" />
                      <span className="text-gray-300 text-[11px] font-medium">Charging</span>
                    </div>
                    <span className="text-emerald-400 font-bold text-xs">{batteryLevel}%</span>
                  </>
                )}

                {activeEvent === 'call' && (
                  <>
                    <div className="flex items-center gap-1.5">
                      <div className="w-4 h-4 rounded-full bg-emerald-500 flex items-center justify-center animate-pulse">
                        <PhoneCall className="w-2.5 h-2.5 text-white" />
                      </div>
                      <span className="font-medium text-xs text-white">Alex Rivera</span>
                    </div>
                    <span className="text-emerald-400 text-[11px]">Incoming...</span>
                  </>
                )}

                {activeEvent === 'gaming' && (
                  <>
                    <div className="flex items-center gap-1.5">
                      <Gamepad2 className="w-3.5 h-3.5 text-cyan-400" />
                      <span className="text-xs font-semibold text-white">CODM</span>
                    </div>
                    <span className="text-cyan-400 text-[11px] font-mono font-bold">59.8 FPS</span>
                  </>
                )}

                {activeEvent === 'idle' && (
                  <>
                    <div
                      className="w-2 h-2 rounded-full"
                      style={{ backgroundColor: accentColor }}
                    />
                    <span className="text-[11px] text-gray-400 tracking-wider font-medium">
                      NEXA ISLAND
                    </span>
                    <div className="w-2 h-2 rounded-full bg-cyan-400" />
                  </>
                )}
              </div>
            )}

            {/* EXPANDED MODE */}
            {isExpanded && (
              <div
                className="w-full h-full flex flex-col justify-between p-2 text-xs"
                onClick={(e) => e.stopPropagation()}
              >
                {/* Header Row */}
                <div className="flex items-center justify-between pb-1">
                  <div className="flex items-center gap-1.5">
                    <Sparkles className="w-3.5 h-3.5" style={{ color: accentColor }} />
                    <span className="text-[10px] uppercase font-bold tracking-wider text-gray-400">
                      {activeEvent === 'music'
                        ? 'Now Playing • Spotify'
                        : activeEvent === 'notification'
                        ? 'Incoming Notification'
                        : activeEvent === 'call'
                        ? 'Incoming Audio Call'
                        : activeEvent === 'gaming'
                        ? 'MIUI Game Turbo Active'
                        : 'System Status'}
                    </span>
                  </div>
                  <button
                    onClick={onToggleExpand}
                    className="p-1 rounded-full hover:bg-white/10 text-gray-400 hover:text-white transition-colors"
                  >
                    <Minimize2 className="w-3.5 h-3.5" />
                  </button>
                </div>

                {/* Music Details */}
                {activeEvent === 'music' && (
                  <div className="space-y-2 mt-1">
                    <div className="flex items-center gap-3">
                      <div
                        className="w-11 h-11 rounded-xl flex items-center justify-center shrink-0 shadow-md"
                        style={{ backgroundColor: `${accentColor}30` }}
                      >
                        <Volume2 className="w-6 h-6" style={{ color: accentColor }} />
                      </div>
                      <div className="overflow-hidden">
                        <h4 className="font-bold text-white text-sm truncate">Starboy</h4>
                        <p className="text-gray-400 text-xs truncate">The Weeknd, Daft Punk</p>
                      </div>
                    </div>

                    {/* Scrubber */}
                    <div className="space-y-1">
                      <div className="w-full bg-white/10 h-1.5 rounded-full overflow-hidden">
                        <div
                          className="h-full rounded-full transition-all"
                          style={{
                            width: `${musicProgress}%`,
                            backgroundColor: accentColor,
                          }}
                        />
                      </div>
                      <div className="flex justify-between text-[10px] text-gray-400 font-mono">
                        <span>1:28</span>
                        <span>3:50</span>
                      </div>
                    </div>

                    {/* Playback Controls */}
                    <div className="flex items-center justify-center gap-4 pt-1">
                      <button className="text-gray-400 hover:text-white transition">
                        <SkipBack className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => setIsPlaying(!isPlaying)}
                        className="w-8 h-8 rounded-full flex items-center justify-center text-white shadow-lg transition transform hover:scale-105"
                        style={{ backgroundColor: accentColor }}
                      >
                        {isPlaying ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4 ml-0.5" />}
                      </button>
                      <button className="text-gray-400 hover:text-white transition">
                        <SkipForward className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                )}

                {/* Notification Details */}
                {activeEvent === 'notification' && (
                  <div className="space-y-2 mt-1">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <div className="w-7 h-7 rounded-full bg-emerald-500/20 flex items-center justify-center">
                          <MessageSquare className="w-4 h-4 text-emerald-400" />
                        </div>
                        <div>
                          <div className="font-bold text-white text-xs">Sarah Jenkins</div>
                          <div className="text-[10px] text-emerald-400">WhatsApp • Just now</div>
                        </div>
                      </div>
                    </div>
                    <p className="text-gray-300 text-xs leading-relaxed bg-black/20 p-2 rounded-lg border border-white/5">
                      "Hey! Did you check out the new Nexa Island update? It looks amazing on MIUI 13! 🔥"
                    </p>
                    <div className="flex justify-end gap-2 pt-1">
                      <button
                        onClick={onToggleExpand}
                        className="px-3 py-1 rounded-lg bg-white/10 text-[11px] text-gray-300 hover:bg-white/20 transition"
                      >
                        Dismiss
                      </button>
                      <button
                        className="px-3 py-1 rounded-lg text-[11px] font-semibold text-white transition"
                        style={{ backgroundColor: accentColor }}
                      >
                        Reply
                      </button>
                    </div>
                  </div>
                )}

                {/* Call Details */}
                {activeEvent === 'call' && (
                  <div className="space-y-3 mt-1 text-center">
                    <div>
                      <h4 className="font-bold text-white text-sm">Alex Rivera</h4>
                      <p className="text-gray-400 text-xs">+62 812-3456-7890</p>
                    </div>
                    <div className="flex justify-center gap-6 pt-1">
                      <button
                        onClick={() => setActiveEvent('idle')}
                        className="w-10 h-10 rounded-full bg-rose-500 hover:bg-rose-600 flex items-center justify-center text-white shadow-lg transition"
                      >
                        <PhoneOff className="w-5 h-5" />
                      </button>
                      <button
                        onClick={() => setActiveEvent('idle')}
                        className="w-10 h-10 rounded-full bg-emerald-500 hover:bg-emerald-600 flex items-center justify-center text-white shadow-lg transition"
                      >
                        <PhoneCall className="w-5 h-5" />
                      </button>
                    </div>
                  </div>
                )}

                {/* Battery Details */}
                {activeEvent === 'battery' && (
                  <div className="space-y-2 mt-1">
                    <div className="flex items-center justify-between">
                      <span className="text-gray-300 font-medium">Turbo Fast Charge</span>
                      <span className="text-emerald-400 font-bold font-mono text-sm">{batteryLevel}%</span>
                    </div>
                    <div className="w-full bg-white/10 h-2 rounded-full overflow-hidden">
                      <div className="h-full bg-emerald-400 rounded-full transition-all" style={{ width: `${batteryLevel}%` }} />
                    </div>
                    <div className="flex justify-between text-[11px] text-gray-400 pt-1">
                      <span>Temp: 31.2°C</span>
                      <span>33W Xiaomi Fast Charge</span>
                    </div>
                  </div>
                )}

                {/* Gaming HUD Details */}
                {activeEvent === 'gaming' && (
                  <div className="space-y-2 mt-1">
                    <div className="flex items-center justify-between text-xs">
                      <span className="text-gray-300 font-medium">Call of Duty Mobile</span>
                      <span className="text-cyan-400 font-bold font-mono">59.8 FPS</span>
                    </div>
                    <div className="grid grid-cols-2 gap-2 text-[10px]">
                      <div className="p-1.5 rounded-lg bg-white/5 border border-white/5 text-gray-300">
                        GPU: 42% Load
                      </div>
                      <div className="p-1.5 rounded-lg bg-white/5 border border-white/5 text-gray-300">
                        Memory: 3.1 GB Free
                      </div>
                    </div>
                  </div>
                )}

                {/* Idle Expanded */}
                {activeEvent === 'idle' && (
                  <div className="py-2 text-center text-gray-400 text-xs">
                    <p>Nexa Island Engine is running in background.</p>
                    <p className="text-[10px] text-gray-500 mt-1">Listening for MediaSession & Notifications</p>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>

        {/* Simulated Phone Wallpaper & App Icons */}
        <div className="flex-1 px-5 pt-8 flex flex-col justify-between pb-8">
          {/* Widget Area */}
          <div className="bg-white/5 backdrop-blur-md rounded-2xl p-4 border border-white/10 shadow-lg text-white">
            <div className="text-xs text-gray-400">Wednesday, Nov 2</div>
            <div className="text-2xl font-light mt-0.5">27°C Sunny</div>
            <div className="text-[11px] text-purple-300 mt-1">Xiaomi Redmi Note 9 • MIUI 13</div>
          </div>

          {/* Quick Event Simulation Triggers inside phone screen */}
          <div className="space-y-2">
            <div className="text-[10px] uppercase font-bold tracking-wider text-gray-500 text-center">
              Tap Event To Simulate
            </div>
            <div className="grid grid-cols-3 gap-2">
              <button
                onClick={() => { setActiveEvent('music'); }}
                className={`py-2 px-1 rounded-xl text-[10px] font-medium border transition-all ${
                  activeEvent === 'music'
                    ? 'bg-purple-600/30 border-purple-500 text-white'
                    : 'bg-white/5 border-white/10 text-gray-400 hover:text-white'
                }`}
              >
                🎵 Music
              </button>
              <button
                onClick={() => { setActiveEvent('notification'); }}
                className={`py-2 px-1 rounded-xl text-[10px] font-medium border transition-all ${
                  activeEvent === 'notification'
                    ? 'bg-cyan-600/30 border-cyan-500 text-white'
                    : 'bg-white/5 border-white/10 text-gray-400 hover:text-white'
                }`}
              >
                💬 Chat Notif
              </button>
              <button
                onClick={() => { setActiveEvent('battery'); }}
                className={`py-2 px-1 rounded-xl text-[10px] font-medium border transition-all ${
                  activeEvent === 'battery'
                    ? 'bg-emerald-600/30 border-emerald-500 text-white'
                    : 'bg-white/5 border-white/10 text-gray-400 hover:text-white'
                }`}
              >
                ⚡ Battery
              </button>
              <button
                onClick={() => { setActiveEvent('call'); }}
                className={`py-2 px-1 rounded-xl text-[10px] font-medium border transition-all ${
                  activeEvent === 'call'
                    ? 'bg-rose-600/30 border-rose-500 text-white'
                    : 'bg-white/5 border-white/10 text-gray-400 hover:text-white'
                }`}
              >
                📞 Phone Call
              </button>
              <button
                onClick={() => { setActiveEvent('gaming'); }}
                className={`py-2 px-1 rounded-xl text-[10px] font-medium border transition-all ${
                  activeEvent === 'gaming'
                    ? 'bg-amber-600/30 border-amber-500 text-white'
                    : 'bg-white/5 border-white/10 text-gray-400 hover:text-white'
                }`}
              >
                🎮 Gaming
              </button>
              <button
                onClick={() => { setActiveEvent('idle'); }}
                className={`py-2 px-1 rounded-xl text-[10px] font-medium border transition-all ${
                  activeEvent === 'idle'
                    ? 'bg-gray-700/40 border-gray-500 text-white'
                    : 'bg-white/5 border-white/10 text-gray-400 hover:text-white'
                }`}
              >
                🔄 Reset Idle
              </button>
            </div>
          </div>

          {/* Android Navigation Bar */}
          <div className="w-28 h-1 bg-gray-500/60 rounded-full mx-auto" />
        </div>
      </div>

      {/* Simulator Controls below phone */}
      <div className="flex items-center gap-3 mt-4 text-xs text-gray-400">
        <span>Punch-hole camera:</span>
        <button
          onClick={() => setCameraPosition('center')}
          className={`px-2.5 py-1 rounded-md text-[11px] border transition ${
            cameraPosition === 'center'
              ? 'bg-purple-600 text-white border-purple-500'
              : 'bg-gray-800 text-gray-400 border-gray-700'
          }`}
        >
          Center
        </button>
        <button
          onClick={() => setCameraPosition('left')}
          className={`px-2.5 py-1 rounded-md text-[11px] border transition ${
            cameraPosition === 'left'
              ? 'bg-purple-600 text-white border-purple-500'
              : 'bg-gray-800 text-gray-400 border-gray-700'
          }`}
        >
          Top-Left (Redmi Note 9)
        </button>
      </div>
    </div>
  );
};
