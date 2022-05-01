package com.bytedance.jstu.demo.lesson8

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.ExifInterface
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import com.bytedance.jstu.demo.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Homework8Activity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var surfaceView: SurfaceView
    private  var camera: Camera? = null
    private var mediaRecorder: MediaRecorder? = null
    private lateinit var holder: SurfaceHolder
    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var recordButton: Button
    private var isRecording = false
    private var mp4Path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework8)
        surfaceView = findViewById(R.id.surfaceview)
        imageView = findViewById(R.id.iv_img)
        videoView = findViewById(R.id.videoview)
        recordButton = findViewById(R.id.bt_record)
        holder = surfaceView.holder
        findViewById<Button>(R.id.bt_image_record).setOnClickListener {
            camera?.takePicture(null, null) { data, camera ->
                val path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + File.separator + "1.jpg"
                FileOutputStream(File(path)).let {
                    it.write(data)
                    it.close()
                }
                imageView.visibility = View.VISIBLE
                videoView.visibility = View.GONE
                imageView.setImageBitmap(rotateImage(BitmapFactory.decodeFile(path), path))
                camera.startPreview()
            }
        }

        recordButton.setOnClickListener {
            if (isRecording &&  mediaRecorder != null) {
                recordButton.text = "录制"
                mediaRecorder!!.let {
                    it.setOnErrorListener(null)
                    it.setOnInfoListener(null)
                    it.setPreviewDisplay(null)
                    try {
                        it.stop()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    it.reset()
                    it.release()
                }
                mediaRecorder = null
                camera?.lock()
                videoView.visibility = View.VISIBLE
                videoView.visibility = View.GONE
                videoView.setVideoPath(mp4Path)
                videoView.start()
            } else {
                mp4Path = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.mp4"
                ).also {
                    it.parentFile?.mkdirs()
                }.absolutePath
                camera?.unlock()
                try {
                    mediaRecorder = MediaRecorder().apply {
                        setCamera(camera)
                        setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                        setVideoSource(MediaRecorder.VideoSource.CAMERA)
                        setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))
                        setOutputFile(mp4Path)
                        setPreviewDisplay(holder.surface)
                        setOrientationHint(90)
                        prepare()
                        start()
                    }
                    recordButton.text = "暂停"
                    isRecording = !isRecording
                } catch (e: IllegalStateException) {
                    releaseMediaRecorder()
                } catch (e: IOException) {
                    releaseMediaRecorder()
                }
            }
        }
        initCamera()
        holder.addCallback(this)
    }

    private fun initCamera() {
        camera = Camera.open()

        camera?.let {
            val parameters = it.parameters
            parameters.pictureFormat = ImageFormat.JPEG
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
            parameters["orientation"] = "portrait"
            parameters["rotation"] = 90
            it.parameters = parameters
            it.setDisplayOrientation(90)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            camera?.let {
                it.setPreviewDisplay(holder)
                it.startPreview()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun releaseMediaRecorder() {
        mediaRecorder?.let { mediaRecorder->
            mediaRecorder.reset() // clear recorder configuration
            mediaRecorder.release() // release the recorder object
            this.mediaRecorder = null
            camera?.lock() // lock camera for later use
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (holder.surface == null) {
            return
        }
        //停止预览效果
        camera?.stopPreview()
        //重新设置预览效果
        try {
            camera?.let {
                it.setPreviewDisplay(holder)
                it.startPreview()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.let {
            it.stopPreview()
            it.release()
        }
    }

    override fun onResume() {
        super.onResume()
        if (camera == null) {
            initCamera()
        }
        camera?.startPreview()
    }

    override fun onPause() {
        super.onPause()
        camera?.stopPreview()
    }

    companion object {
        fun rotateImage(bitmap: Bitmap, path: String): Bitmap =
            try {
                Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    Matrix().apply {
                        postRotate(
                            when (ExifInterface(path).getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL
                            )) {
                                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                                else -> 0f
                            }
                        )
                    },
                    true
                )
            } catch (e: IOException) {
                e.printStackTrace()
                bitmap
            }
    }
}
