package com.varivoda.igor.tvz.financijskimanager.util


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.varivoda.igor.tvz.financijskimanager.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


private enum class Quarter(val label: Int){
    FIRST(R.string.quarter_first),
    SECOND(R.string.quarter_second),
    THIRD(R.string.quarter_third),
    FOURTH(R.string.quarter_fourth);

    fun next() = when (this) {
        FIRST -> SECOND
        SECOND -> THIRD
        THIRD -> FOURTH
        FOURTH -> FIRST
    }
}

private const val RADIUS_OFFSET_LABEL = 90

class QuarterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private var radius = 0.0f
    private var quarterValue = Quarter.FIRST
    private val pointPosition: PointF = PointF(0.0f, 0.0f)
    private val rectF = RectF()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
        color = Color.BLUE
    }

    private val paintQuarter = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
        color = ContextCompat.getColor(context,R.color.colorPrimary)
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
        color = Color.WHITE
    }
    init {
        isClickable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(width, height) / 2.0 * 0.8).toFloat()
    }

    private fun PointF.computeXYForSpeed(pos: Quarter, radius: Float) {
        val startAngle = Math.PI * (14.5 / 8.0)
        val angle = startAngle + pos.ordinal * (Math.PI / 2.23)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = Color.WHITE
        canvas?.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

        rectF[(width / 2).toFloat() - radius, (height / 2).toFloat() - radius, (width / 2).toFloat() + radius] = (height / 2).toFloat() + radius
        when(quarterValue){
            Quarter.FIRST -> {
                canvas?.drawArc(rectF, 0f, -90f, true, paintQuarter)
            }
            Quarter.SECOND -> {
                canvas?.drawArc(rectF, 0f, -90f, true, paintQuarter)
                canvas?.drawArc(rectF, 0f, 90f, true, paintQuarter)
            }
            Quarter.THIRD -> {
                canvas?.drawArc(rectF, 0f, -90f, true, paintQuarter)
                canvas?.drawArc(rectF, 0f, 180f, true, paintQuarter)
            }
            Quarter.FOURTH -> {
                canvas?.drawArc(rectF, 0f, -90f, true, paintQuarter)
                canvas?.drawArc(rectF, 0f, 270f, true, paintQuarter)
            }
        }


        val labelRadius = radius - RADIUS_OFFSET_LABEL
        val labelRadius2 = radius - RADIUS_OFFSET_LABEL - 20
        for (i in Quarter.values()) {
            if(i==Quarter.SECOND || i==Quarter.THIRD){
                pointPosition.computeXYForSpeed(i, labelRadius)
            }else{
                pointPosition.computeXYForSpeed(i, labelRadius2)
            }

            val label = resources.getString(i.label)
            canvas?.drawText(label, pointPosition.x, pointPosition.y, paintText)
        }
    }

    fun getCurrentQuarterValue(): String{
        return quarterValue.name
    }


    override fun performClick(): Boolean {
        if(super.performClick()) return true
        quarterValue = quarterValue.next()
        invalidate()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                quarterValue = quarterValue.next()
                invalidate()
                return true
            }
        }
        return false
    }

}