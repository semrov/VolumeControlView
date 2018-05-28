package com.jure.semrov.volumecontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.absoluteValue

class VolumeControlView(context: Context, attrs: AttributeSet) : View(context,attrs)
{

    interface OnVolumeChangeListener
    {
        fun onVolumeChange(volume : Int)
    }

    private var onVolumeChangeListener : OnVolumeChangeListener? = null

    private var volumeScale: Int = 5
        private set(scale) {if (scale < 5) field = 5 else if (scale > 20) field = 20 else field = scale}
    private var currentVolume: Float = 50F
        private set(volume) { if(volume < 0) field = 0F else if (volume > 100F) field = 100F else field = volume }

    private var backgroundPaint = Paint()
    private var levelPaint = Paint()
    private var textPaint = Paint()

    private val default_width = resources.getDimensionPixelSize(R.dimen.volume_control_default_width)
    private val default_height = resources.getDimensionPixelSize(R.dimen.volume_control_default_height)

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.VolumeControlView)

        //sets volume scale, valid values are anything between 5 and 20
        volumeScale = attributes.getInteger(R.styleable.VolumeControlView_volume_scale,10)

        // set current volume percentage to anything between 0 and 100
        currentVolume = attributes.getFloat(R.styleable.VolumeControlView_current_sound_level,50F)

        //set color of lines that shows current level of volume
        levelPaint.color = attributes.getColor(R.styleable.VolumeControlView_level_color, Color.BLUE)

        // sets the background color of the lines
        backgroundPaint.color = attributes.getColor(R.styleable.VolumeControlView_background_color, Color.GRAY)

        textPaint.color = Color.BLACK
        //textPaint.textSize = 30F;

        // TypedArray objects are a shared resource and must be recycled after use
        attributes.recycle()
    }

    fun setScale(scale : Int) : Int
    {
        volumeScale = scale
        invalidate()
        return volumeScale
    }

    fun getScale() : Int
    {
        return volumeScale
    }

    fun setCurrentVolume(volume : Int) : Int
    {
        currentVolume = volume.toFloat()
        invalidate()
        onVolumeChangeListener?.onVolumeChange(currentVolume.toInt())
        return currentVolume.toInt()
    }

    fun get_current_volume() : Int
    {
        return currentVolume.toInt()
    }

    fun setVolumeBackgroundColor(c : Int)
    {
        backgroundPaint.color = c
        invalidate()
    }

    fun setVolumeLevelColor(c : Int)
    {
        levelPaint.color = c
        invalidate()
    }

    fun setOnVolumeChangeListener(l : OnVolumeChangeListener)
    {
        onVolumeChangeListener = l
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingStart + paddingEnd
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        setMeasuredDimension(resolveSize(desiredWidth,widthMeasureSpec), resolveSize(desiredHeight,heightMeasureSpec))
    }

    private fun getNetWidth() : Int
    {
        return width - paddingStart - paddingEnd
    }

    private fun getNetHeight() : Int
    {
        return height - paddingTop - paddingBottom
    }

    private fun drawTile(left : Float, top : Float,right : Float,bottom : Float, paint : Paint, canvas: Canvas)
    {
        canvas.drawRect(left, top, right, bottom, paint)
    }

    override fun onDraw(canvas: Canvas)
    {
        val tileHeight = getNetHeight().toFloat() / (volumeScale * 2)
        val percentPerTile = 100.0F / volumeScale.toFloat()

        for (i in volumeScale-1 downTo 0)
        {
            val top = tileHeight*(i*2) + paddingTop
            val percentage = (volumeScale-i).toFloat() * percentPerTile
            val paint = if (percentage <= currentVolume) {levelPaint} else {backgroundPaint}
            drawTile( paddingStart.toFloat(),top, width.toFloat() - paddingEnd, top + tileHeight,paint,canvas)
        }

        val y = (tileHeight * ((volumeScale * 2))).toFloat() - tileHeight / 2 + paddingTop
        canvas.drawText("Volume set at: ${currentVolume.toInt()}%", (getNetWidth().toFloat()/3) + paddingStart,y,textPaint)
    }

    private fun calculateVolumeFromEventPos(y : Float) : Float
    {
        val tileHeight = getNetHeight().toFloat() / (volumeScale * 2)
        val percentPerTile =  100.0F / volumeScale.toFloat()
        val y = (y - paddingBottom - getNetHeight()).absoluteValue

        if(y <= getNetHeight() && y >= getNetHeight()-tileHeight)
        {
            return 100F
        }
        else if(y >= 0 && y <=  tileHeight)
        {
            return 0F
        }
        else
        {
            val largeTileHeight = tileHeight*2
            val y = y - tileHeight
            val part =  (y / largeTileHeight).toInt() + 1
            return part*percentPerTile
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent::ACTION_DOWN.get() || event.action == MotionEvent.ACTION_MOVE)
        {
            val x = event.x;
            val y = event.y
            if(x > paddingStart && x < width - paddingEnd  && y > paddingTop && y < height - paddingBottom)
            {
                currentVolume = calculateVolumeFromEventPos(y)
                invalidate()
                onVolumeChangeListener?.onVolumeChange(currentVolume.toInt())
                return true
            }
        }
        return false
    }
}