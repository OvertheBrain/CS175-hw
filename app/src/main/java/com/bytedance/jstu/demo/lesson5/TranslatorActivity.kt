package com.bytedance.jstu.demo.lesson5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.bytedance.jstu.demo.R
import com.bytedance.jstu.demo.lesson5.api.Youdao
import com.bytedance.jstu.demo.lesson5.api.YoudaoFanyi
import com.bytedance.jstu.demo.lesson5.api.YoudaoRes
import com.google.gson.GsonBuilder
import okhttp3.*
import com.google.gson.JsonParser
import java.io.IOException
import java.net.URLEncoder

class TranslatorActivity : AppCompatActivity() {
    private val client = OkHttpClient.Builder().build()
    private val gson = GsonBuilder().create()
    private var button: Button? = null
    private var translation: TextView? = null
    private var input: EditText? = null

    private var result: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)
        translation = findViewById(R.id.translation)
        input = findViewById(R.id.edit)
        button = findViewById(R.id.translate)

        input?.setOnKeyListener { view, i, keyEvent -> run {
            if(keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                Translate()
                true
            } else {
                false
            }
        } }

        button?.setOnClickListener {
            Translate()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateShowTextView(text: String, append: Boolean = true) {
        if (Looper.getMainLooper() !== Looper.myLooper()) {
            // 子线程，提交到主线程中去更新 UI.
            runOnUiThread {
                updateShowTextView(text, append)
            }
        } else {
            translation?.text = if (append) translation?.text.toString() + text else text
        }
    }

    private fun Translate() {
        val q = input?.text.toString()

        client.newCall(
            Request.Builder()
                .url("https://dict.youdao.com/jsonapi?q=" + URLEncoder.encode(q, "UTF-8")).build()
        ).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.body == null) return
                try {
                    val resString = response.body?.string()
                    var res = "No data"
                    result = if (response.isSuccessful) {

                        val detect = gson.fromJson(resString, Youdao::class.java)

                        if(res === "No data" && "web_trans" in detect.meta.dicts){
                            try {
                                //我的评价：用起来甚至不如js里的JSON.Parse，下次讲这类东西的时候能讲仔细点嘛，或者作业用例设计的更有针对性点
                                val format = gson.fromJson(resString, YoudaoRes::class.java)
                                res = format.WebTrans().webTranslation[0].value
                            } catch (e:Exception) {
                                res = e.message.toString()
                            }
                        }

                        else if("fanyi" in detect.meta.dicts) {
                            try {
                                val format = gson.fromJson(resString, YoudaoFanyi::class.java)
                                res = format.fanyi.tran
                            }catch (e:Exception) {
                                res = e.message.toString()
                            }
                        }
                        else {
                            res = "No data"
                        }

                        "\n${res}"

                    } else {
                        "\n\n\nResponse fail: ${resString}, http status code: ${response.code}."
                    }
                    updateShowTextView(result!!, false)

                }catch (e: NullPointerException){
                    e.printStackTrace()
                    updateShowTextView(e.message.toString(), false)
                    Log.d("translator", "failure")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                updateShowTextView(e.message.toString(), false)
                Log.d("translator", "failure")
            }
        })
    }
}
