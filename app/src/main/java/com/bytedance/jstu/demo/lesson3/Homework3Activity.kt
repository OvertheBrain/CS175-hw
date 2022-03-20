package com.bytedance.jstu.demo.lesson3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bytedance.jstu.demo.R
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar

class Homework3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homework3)

        val like = findViewById<ImageButton>(R.id.like)
        val coin = findViewById<ImageView>(R.id.coin)
        val star = findViewById<ImageView>(R.id.star)
        var cir1 = findViewById<ProgressBar>(R.id.cir1)
        var cir2 = findViewById<ProgressBar>(R.id.cir2)

        cir1.setVisibility(View.INVISIBLE);
        cir2.setVisibility(View.INVISIBLE);
        //点击点赞一键三连
        like.setOnClickListener {
            AnimatorSet().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    playTogether(
                        ValueAnimator.ofFloat(0f, 1f).apply {
                            duration = 1000
                            cir1.setVisibility(View.VISIBLE);
                            cir2.setVisibility(View.VISIBLE);
                            addUpdateListener {
                                val value= it.animatedValue as Float
                                // 三次多项式创造先变小后变大再复原的效果。
                                val buttonScale = 1f - value * (value - 1f) * (value - .5f) * 9f
                                coin.scaleX = buttonScale
                                coin.scaleY = buttonScale
                                star.scaleX = buttonScale
                                star.scaleY = buttonScale
                            }
                        },
                        ObjectAnimator.ofArgb(like, "ColorFilter",
                            Color.rgb(97, 102, 109),
                            Color.rgb(228, 0, 127),
                        ).setDuration(500),
                        ObjectAnimator.ofArgb(coin, "ColorFilter",
                            Color.rgb(97, 102, 109),
                            Color.rgb(228, 0, 127),
                        ).setDuration(500),
                        ObjectAnimator.ofArgb(star, "ColorFilter",
                            Color.rgb(97, 102, 109),
                            Color.rgb(228, 0, 127),
                        ).setDuration(500),
                    )
                }
            }.start()
        }
    }
}
