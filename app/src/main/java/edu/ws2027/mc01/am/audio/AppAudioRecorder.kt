package edu.ws2027.mc01.am.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AppAudioRecorder(private val context: Context) {
  var mediaRecorder: MediaRecorder? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      MediaRecorder(context)
    } else {
      MediaRecorder()
    }

  fun start(file: File) {
    mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      MediaRecorder(context)
    } else {
      MediaRecorder()
    }.apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
      setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
      setOutputFile(file.path)
      prepare()
      start()
    }
  }

  fun stop() {
    mediaRecorder?.stop()
    mediaRecorder?.release()
    mediaRecorder = null
  }


  fun pause() {
    mediaRecorder?.pause()
  }

  fun resume() {
    mediaRecorder?.start()
  }
}