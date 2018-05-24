package com.jure.semrov.volumecontrol

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), VolumeControlView.OnVolumeChangeListener {

    private lateinit var audioManager : AudioManager
    private lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        volumeControl.setOnVolumeChangeListener(this)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        setVolume(volumeControl.get_current_volume())

        btn_volume.setOnClickListener{_ ->
            var volume = et_volume.text.toString().toInt()
            volume = volumeControl.setCurrentVolume(volume)
            et_volume.setText(volume.toString())
        }

        btn_lines.setOnClickListener{_ ->
            var lines = et_lines.text.toString().toInt()
            lines = volumeControl.setScale(lines)
            et_lines.setText(lines.toString())
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.braincandy)
        //mediaPlayer.setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.isLooping = true
    }

    private fun setVolume(volume : Int)
    {
        et_volume.setText(volume.toString())
        val volume = volume / 100F
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,(volume*maxVolume).toInt(),0)
    }

    override fun onVolumeChange(volume: Int) {
        setVolume(volume)
    }

    override fun onPause() {
        mediaPlayer.pause()
        super.onPause()
    }

    override fun onResume() {
        mediaPlayer.start()
        super.onResume()
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
        super.onDestroy()
    }
}
