package com.bytedance.jstu.demo.lesson4.homework

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withRotation
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min

/**
 *  author : neo
 *  time   : 2021/10/25
 *  desc   :
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var quarterScalePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.WHITE
        strokeWidth = 10f
        strokeCap=Paint.Cap.ROUND
    }
    private var minScalePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.WHITE
        strokeWidth = 5f
        strokeCap=Paint.Cap.ROUND
    }

    private var hourHandPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.WHITE
        strokeWidth = 10f
        strokeCap=Paint.Cap.ROUND
    }

    private var minHandPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.BLUE
        strokeWidth = 6f
        strokeCap=Paint.Cap.ROUND
    }
    private var secHandPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.BLACK
        strokeWidth = 3f
        strokeCap=Paint.Cap.ROUND
    }
    private var NumPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 72f
        color = Color.WHITE
        typeface = Typeface.SERIF
        strokeCap=Paint.Cap.ROUND
    }

    private var ClockFramePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(128, 255, 255, 255)
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }
    private var DigitalPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.WHITE
        strokeWidth = 3f
        textSize= 80f
        typeface= Typeface.DEFAULT
        textAlign=Paint.Align.CENTER
        strokeCap=Paint.Cap.ROUND
    }

    private var tmpRec = Rect()
    var CurValue: Float = 0f

    private var cal: Calendar = Calendar.getInstance()
    private var curHourOfDay=cal.get(Calendar.HOUR_OF_DAY).toFloat()
    private var curHour= cal.get(Calendar.HOUR).toFloat()
    private var curMinute= cal.get(Calendar.MINUTE).toFloat()
    private var curSecond= cal.get(Calendar.SECOND).toFloat()

    init{
        Thread {
            while (true){
                Thread.sleep(1000)

                if(CurValue == 120 * 3600f)
                    CurValue= 0f;
                CurValue++;

                cal = Calendar.getInstance()
                curHourOfDay=cal.get(Calendar.HOUR_OF_DAY).toFloat()
                curHour = cal.get(Calendar.HOUR).toFloat()
                curMinute = cal.get(Calendar.MINUTE).toFloat()
                curSecond = cal.get(Calendar.SECOND).toFloat()

                postInvalidate()
            }
        }.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        canvas.translate(width / 2f, height / 2f)
        val r = min(width, height) / 2f
        canvas.drawCircle(0f, 0f, r - 1.5f, ClockFramePaint)
        fun drawClock(str: String, angle: Float) {
            NumPaint.getTextBounds(str, 0, str.length, tmpRec)
            canvas.withRotation(angle) {
                drawText(str, -tmpRec.width() / 2f, -r + tmpRec.height() + 48f, NumPaint)
                drawLine(0f, -r, 0f, -r + 32f, quarterScalePaint)
                (6..24 step 6).forEach {
                    withRotation(it.toFloat()) {
                        drawLine(0f, -r, 0f, -r + 24f, minScalePaint)
                    }
                }
            }
        }

        drawClock("12", 0f)
        drawClock("1", 30f)
        drawClock("2", 60f)
        drawClock("3", 90f)
        drawClock("4", 120f)
        drawClock("5", 150f)
        drawClock("6", 180f)
        drawClock("7", 210f)
        drawClock("8", 240f)
        drawClock("9", 270f)
        drawClock("10", 300f)
        drawClock("11", 330f)

        canvas.withRotation(CurValue / 120) {
            drawLine(0f, 0f, 0f, -r / 2, hourHandPaint)
        }
        canvas.withRotation(CurValue.mod(3600f) / 10) {
            drawLine(0f, 0f, 0f, -2*r / 3, minHandPaint)
        }
        canvas.withRotation(CurValue.mod(60f) * 6) {
            drawLine(0f, 0f, 0f, -r * .75f, secHandPaint)
        }

        val DigitNumber="${curHourOfDay.toInt()} : ${curMinute.toInt()} : ${curSecond.toInt()}"

        canvas.drawText( DigitNumber, (width/2).toFloat(), (height*3/4).toFloat(), DigitalPaint)
    }
}
