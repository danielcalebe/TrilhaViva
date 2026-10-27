package edu.ws2027.mc01.am.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build

class AppAudioPlayer(private val context: Context) {
  val mediaPlayer: MediaPlayer =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      MediaPlayer(context)
    } else {
      MediaPlayer()
    }

  fun start(uri: Uri) {
    mediaPlayer.setDataSource(context, uri)
    mediaPlayer.prepare()
    mediaPlayer.start()
  }

  fun stop() {
    mediaPlayer.stop()
    mediaPlayer.release()
  }
}