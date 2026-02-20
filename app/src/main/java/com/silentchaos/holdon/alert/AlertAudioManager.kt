package com.silentchaos.holdon.alert

import android.content.Context
import android.database.ContentObserver
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.provider.Settings

class AlertAudioManager(
    private val context: Context,
) {

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var mediaPlayer: MediaPlayer? = null
    private var focusRequest: AudioFocusRequest? = null

    private var lockedVolume: Int = 0
    private var isVolumeLocked = false
    private val volumeHandler = Handler(Looper.getMainLooper())


    private val volumeObserver = object : ContentObserver(
        Handler(Looper.getMainLooper())
    ) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)

            if (!isVolumeLocked) return

            val current =
                audioManager.getStreamVolume(AudioManager.STREAM_ALARM)

            if (current < lockedVolume) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_SYSTEM,
                    lockedVolume,
                    0
                )
            }
        }
    }


    private val volumeCheckRunnable = object : Runnable {
        override fun run() {
            if (isVolumeLocked) {
                val current =
                    audioManager.getStreamVolume(AudioManager.STREAM_ALARM)

                if (current < lockedVolume) {
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_ALARM,
                        lockedVolume,
                        0
                    )
                }

                volumeHandler.postDelayed(this, 300)
            }
        }
    }
    fun start(soundResId: Int) {

        lockedVolume =
            audioManager.getStreamVolume(AudioManager.STREAM_ALARM)

        isVolumeLocked = true

        volumeHandler.post(volumeCheckRunnable)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        focusRequest = AudioFocusRequest.Builder(
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
        )
            .setAudioAttributes(attributes)
            .build()

        if (audioManager.requestAudioFocus(focusRequest!!)
            != AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        ) return

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(attributes)
            setDataSource(
                context,
                android.net.Uri.parse("android.resource://${context.packageName}/$soundResId")
            )
            isLooping = true
            prepare()
            start()
        }
    }



    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        focusRequest?.let {
            audioManager.abandonAudioFocusRequest(it)
        }
        isVolumeLocked = false

        context.contentResolver.unregisterContentObserver(volumeObserver)
        volumeHandler.removeCallbacks(volumeCheckRunnable)
    }
}
