package com.bytedance.jstu.demo.lesson7.hw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.jstu.demo.R

class ImageDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        val pictures: List<String> = listOf(
            "https://bkimg.cdn.bcebos.com/pic/377adab44aed2e73939247808301a18b87d6fa27?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5/format,f_auto",
            "https://bkimg.cdn.bcebos.com/pic/d31b0ef41bd5ad6e58471b8c86cb39dbb6fd3c1d?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2U4MA==,g_7,xp_5,yp_5/format,f_auto",
            "https://bkimg.cdn.bcebos.com/pic/faf2b2119313b07e5673a3a508d7912396dd8c55?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5/format,f_auto",
            "https://i0.hdslb.com/bfs/article/24bd6960b2794a1d1aec477b6c4325cf656da4c7.gif",
        )

        findViewById<ViewPager2>(R.id.imagePage).adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = pictures.size
            override fun createFragment(pos: Int): Fragment = ImageFragment.newInstance(pictures[pos])
        }
    }
}
