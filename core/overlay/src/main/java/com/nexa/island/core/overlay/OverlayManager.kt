package com.nexa.island.core.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var activeView: View? = null
    private var isAttached: Boolean = false

    fun createLayoutParams(
        widthPx: Int,
        heightPx: Int,
        offsetYPx: Int
    ): WindowManager.LayoutParams {
        val type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

        val flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

        return WindowManager.LayoutParams(
            widthPx,
            heightPx,
            type,
            flags,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = offsetYPx
        }
    }

    @Synchronized
    fun attachOverlay(view: View, params: WindowManager.LayoutParams) {
        if (isAttached && activeView != null) {
            updateOverlay(params)
            return
        }
        try {
            windowManager.addView(view, params)
            activeView = view
            isAttached = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun updateOverlay(params: WindowManager.LayoutParams) {
        activeView?.let { view ->
            if (isAttached) {
                try {
                    windowManager.updateViewLayout(view, params)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Synchronized
    fun detachOverlay() {
        activeView?.let { view ->
            if (isAttached) {
                try {
                    windowManager.removeViewImmediate(view)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    activeView = null
                    isAttached = false
                }
            }
        }
    }

    fun isOverlayAttached(): Boolean = isAttached
}
