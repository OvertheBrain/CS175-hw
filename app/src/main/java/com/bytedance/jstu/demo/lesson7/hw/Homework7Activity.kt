package com.bytedance.jstu.demo.lesson7.hw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.bytedance.jstu.demo.R

class Homework7Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework7)
        bindActivity(R.id.btn_image, ImageDisplayActivity::class.java)
        bindActivity(R.id.btn_video, VideoPlayActivity::class.java)
    }

    private fun bindActivity(buttonId: Int, activityClass: Class<*>) {
        findViewById<View>(buttonId).setOnClickListener {
            startActivity(
                Intent(
                    this@Homework7Activity,
                    activityClass
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
