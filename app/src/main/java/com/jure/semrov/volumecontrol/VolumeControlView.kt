package com.jure.semrov.volumecontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by Jure on 22.5.2018.
 */
class VolumeControlView(context: Context, attrs: AttributeSet?) : View(context,attrs)
{
    var volumeScale: Int = 10
        set(scale) {if (scale < 5) field = 5 else if (scale > 20) field = 20 else field = scale}
    var currentVolume: Int = 50
        set(volume) { if (volume < 0) field = 0 else if (volume > 100) field = 100 else field = volume }

    var backgroundPaint = Paint()
    var levelPaint = Paint()
    var textPaint = Paint()

    private val default_width = resources.getDimensionPixelSize(R.dimen.volume_control_default_width)
    private val default_height = resources.getDimensionPixelSize(R.dimen.volume_control_default_height)

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.VolumeControlView)

        //sets volume scale, valid values are anything between 5 and 20
        volumeScale = attributes.getInteger(R.styleable.VolumeControlView_volume_scale,10)

        // set current volume percentage to anything between 0 and 100
        currentVolume = attributes.getInteger(R.styleable.VolumeControlView_current_sound_level,50)

        //set color of lines that shows current level of volume
        levelPaint.color = attributes.getColor(R.styleable.VolumeControlView_level_color, Color.BLUE)

        // sets the background color of the lines
        backgroundPaint.color = attributes.getColor(R.styleable.VolumeControlView_background_color, Color.GRAY)

        textPaint.color = Color.BLACK
        textPaint.textSize = 30F;

        // TypedArray objects are a shared resource and must be recycled after use
        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode)
        {
            View.MeasureSpec.EXACTLY -> widthSize
            View.MeasureSpec.AT_MOST -> default_width
            View.MeasureSpec.UNSPECIFIED -> default_width
            else -> default_width
        }

        val height = when (heightMode)
        {
            View.MeasureSpec.EXACTLY -> heightSize
            View.MeasureSpec.AT_MOST -> default_height
            View.MeasureSpec.UNSPECIFIED -> default_height
            else -> default_height
        }

        setMeasuredDimension(width,height)
    }

    private fun drawTile(left : Float, top : Float,right : Float,bottom : Float, paint : Paint, canvas: Canvas)
    {
        canvas.drawRect(left, top, right, bottom, paint)
    }

    override fun onDraw(canvas: Canvas)
    {
        val tileHeight = height / (volumeScale * 2)
        for (i in 0..(volumeScale-1))
        {
            val top = (tileHeight*(i*2)).toFloat()
            drawTile(0F,top, width.toFloat(), top + tileHeight,backgroundPaint,canvas)
        }

        val y = (tileHeight * ((volumeScale * 2))).toFloat()
        canvas.drawText("Volume set at: " + currentVolume + "%", (width / 4).toFloat(),y,textPaint)
    }
}