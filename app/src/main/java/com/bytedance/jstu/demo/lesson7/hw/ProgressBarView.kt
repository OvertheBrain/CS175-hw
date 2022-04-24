package com.bytedance.jstu.demo.lesson7.hw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ProgressBarView(context: Context,attrs: AttributeSet) : View(context,attrs)  {

    public var progress: Float=1F

    private val paintBar= Paint()

    init {
        paintBar.isAntiAlias = true
        paintBar.style = Paint.Style.STROKE
        paintBar.color = Color.parseColor("#000000")
        paintBar.strokeWidth = 10F
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawLine(
                0F,height/2F,
                progress * width,height/2F,
                paintBar
            )
        }
    }


}
