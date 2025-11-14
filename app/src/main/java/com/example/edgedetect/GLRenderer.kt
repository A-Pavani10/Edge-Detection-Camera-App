package com.example.edgedetect

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class GLTextureSurfaceView(context: Context, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {
    private val renderer: GLRenderer

    constructor(context: Context) : this(context, null)

    init {
        setEGLContextClientVersion(2)
        renderer = GLRenderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun queueFrame(bmp: Bitmap) {
        renderer.updateBitmap(bmp)
        requestRender()
    }
}
