package com.bytedance.jstu.demo.lesson7.hw

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.bytedance.jstu.demo.R

class VideoPlayActivity : AppCompatActivity() {
    private lateinit var fragmentVideoContainer: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        fragmentVideoContainer = findViewById<FragmentContainerView>(R.id.fragment_video_container)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.println(Log.INFO,"on_cc",newConfig.orientation.toString())
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == ORIENTATION_LANDSCAPE) {

            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    //全屏的状态栏显示设置
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)

            supportActionBar?.hide()
        }else{
            supportFragmentManager.commit {
                replace<VideoPlayerFragment>(R.id.fragment_video_container)
            }
            window.decorView.systemUiVisibility = 0

            supportActionBar?.show()
        }
    }
}
