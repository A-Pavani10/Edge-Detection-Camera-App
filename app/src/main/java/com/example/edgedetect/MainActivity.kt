package com.example.edgedetect

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.hardware.camera2.*

class MainActivity : Activity() {
    companion object {
        init { System.loadLibrary("native-lib") }
    }

    external fun nativeProcessFrame(nv21: ByteArray, width: Int, height: Int): ByteArray

    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureSession: CameraCaptureSession
    private lateinit var cameraManager: CameraManager
    private lateinit var reader: ImageReader
    private lateinit var glView: GLTextureSurfaceView
    private lateinit var tvStats: TextView

    private var lastTs = 0L
    private var frames = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        glView = findViewById(R.id.glView)
        tvStats = findViewById(R.id.tvStats)

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            finish()
        }
    }

    private fun startCamera() {
        try {
            val cameraId = cameraManager.cameraIdList[0]
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val previewSize = map!!.getOutputSizes(Surface::class.java)[0]

            reader = ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.YUV_420_888, 2)
            reader.setOnImageAvailableListener({ r ->
                val img = r.acquireLatestImage() ?: return@setOnImageAvailableListener
                val nv21 = yuv420ToNV21(img)
                img.close()

                // call native
                val out = nativeProcessFrame(nv21, previewSize.width, previewSize.height)

                // out is assumed to be RGBA bytes; create bitmap and render via GL view
                val bmp = Bitmap.createBitmap(previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888)
                bmp.copyPixelsFromBuffer(java.nio.ByteBuffer.wrap(out))

                glView.queueFrame(bmp)

                // simple FPS
                frames++
                val now = System.currentTimeMillis()
                if (now - lastTs >= 1000) {
                    runOnUiThread { tvStats.text = "FPS: $frames" }
                    frames = 0
                    lastTs = now
                }
            }, null)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) return
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    val texture = android.graphics.SurfaceTexture(10)
                    texture.setDefaultBufferSize(previewSize.width, previewSize.height)
                    val surface = Surface(texture)

                    val targets = ArrayList<Surface>()
                    targets.add(reader.surface)

                    camera.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            captureSession = session
                            val requestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                            requestBuilder.addTarget(reader.surface)
                            requestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                            session.setRepeatingRequest(requestBuilder.build(), null, null)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) { }
                    }, null)
                }

                override fun onDisconnected(camera: CameraDevice) {}
                override fun onError(camera: CameraDevice, error: Int) {}
            }, null)

        } catch (e: Exception) {
            Log.e("MainActivity", "startCamera error", e)
        }
    }

    private fun yuv420ToNV21(image: Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 2
        val nv21 = ByteArray(ySize + uvSize)

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        var rowStride = image.planes[0].rowStride
        var pos = 0
        if (rowStride == width) {
            yBuffer.get(nv21, 0, ySize)
            pos += ySize
        } else {
            val yRow = ByteArray(rowStride)
            for (i in 0 until height) {
                yBuffer.get(yRow, 0, rowStride)
                System.arraycopy(yRow, 0, nv21, pos, width)
                pos += width
            }
        }

        val chromaRowStride = image.planes[1].rowStride
        val chromaPixelStride = image.planes[1].pixelStride
        val vu = ByteArray(uvSize)
        var p = 0
        val uRow = ByteArray(chromaRowStride)
        val vRow = ByteArray(chromaRowStride)

        for (i in 0 until height / 2) {
            uBuffer.get(uRow, 0, chromaRowStride)
            vBuffer.get(vRow, 0, chromaRowStride)
            var j = 0
            while (j < width) {
                vu[p++] = vRow[j]
                vu[p++] = uRow[j]
                j += chromaPixelStride
            }
        }

        System.arraycopy(vu, 0, nv21, ySize, uvSize)
        return nv21
    }
}
