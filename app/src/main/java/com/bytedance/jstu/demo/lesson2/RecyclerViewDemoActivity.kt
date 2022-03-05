package com.bytedance.jstu.demo.lesson2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.jstu.demo.R
import java.io.IOException

class RecyclerViewDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_demo)

        val rv = findViewById<RecyclerView>(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)

        val adapter = SearchItemAdapter(this)
        //val data = (1..100).map { "这是第${it}行" }
        val data = listOf<String>("1.两数之和","2.两数相加","3.无重复字符的最长子串","4.寻找两个正序数组的中位数",
            "5.最长回文子串")
        val difficulty = listOf<String>("简单","中等","中等","困难","中等")
        val solution = listOf<String>("16403","8587","9190","4681","4909")
        val pass = listOf<String>("52.4%","41.4%","38.5%","41.1%","36.3%")
        adapter.setContentList(data, difficulty, solution, pass)
        rv.adapter = adapter

        val et = findViewById<EditText>(R.id.words_et)
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                adapter.setFilter(p0.toString())
            }
        })

    }

    fun getInfo(): List<String>? {
        var str: String? = ""
        try {
            val info = application.assets.open("detail")
            val len: Int = info.available()
            val buffer = ByteArray(len)
            info.read(buffer)
            str = String(buffer)
            info.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return str?.split("\n")
    }
}
