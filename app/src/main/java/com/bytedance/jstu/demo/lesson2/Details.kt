package com.bytedance.jstu.demo.lesson2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bytedance.jstu.demo.R

class Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val info = intent.extras?.getString("info")
        val textView = findViewById<TextView>(R.id.details)
        textView.text = info
    }
}
