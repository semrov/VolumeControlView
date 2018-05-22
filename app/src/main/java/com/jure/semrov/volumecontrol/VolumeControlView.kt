package com.jure.semrov.volumecontrol

import android.content.Context
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

        // TypedArray objects are a shared resource and must be recycled after use
        attributes.recycle()
    }




}