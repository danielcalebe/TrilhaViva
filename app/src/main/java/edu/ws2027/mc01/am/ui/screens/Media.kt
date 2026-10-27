package edu.ws2027.mc01.am.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.material3.Player
import coil3.compose.AsyncImage
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.audio.AppAudioPlayer
import edu.ws2027.mc01.am.audio.AppAudioRecorder
import edu.ws2027.mc01.am.data.AudioGuide
import edu.ws2027.mc01.am.data.AudioPlayerPhase
import edu.ws2027.mc01.am.data.AudioPlayerState
import edu.ws2027.mc01.am.data.Repository
import edu.ws2027.mc01.am.data.Video
import edu.ws2027.mc01.am.data.VideoPlayerPhase
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun Media(
  modifier: Modifier = Modifier,
  repository: Repository,
  appAudioPlayer: AppAudioPlayer,
  appAudioRecorder: AppAudioRecorder
) {

  val context = LocalContext.current
  val photosString = stringResource(R.string.photos)
  val audioString = stringResource(R.string.audio)
  val videoString = stringResource(R.string.video)

  val sections = remember { listOf(photosString, audioString, videoString) }
  var section by remember { mutableStateOf(photosString) }


  var files by remember { mutableStateOf(context.cacheDir.listFiles()) }
  fun updateFiles() {
    files = context.cacheDir.listFiles()
  }

  val countFiles by remember(files) {
    mutableStateOf(
      files.count {
        it.name.contains("IMAGE_") || it.name.contains("Gravação")
      })
  }

  var totalSize by remember(files) {
    mutableStateOf(files.filter {
      it.name.contains("IMAGE_") || it.name.contains("Gravação")
    }.sumOf {
      it.length().toDouble() / 100000
    })
  }

//  LaunchedEffect(files) {
//    totalSize = files.filter {
//      it.name.contains("IMAGE_") || it.name.contains("AUDIO_")
//    }.sumOf {
//      it.length().toDouble() / 100000
//    }
//  }


  Column(
    modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = stringResource(R.string.media), style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      )
    )
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(4.dp)
    ) {
      sections.forEach {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
              if (section == it) MaterialTheme.colorScheme.primary
              else Color.Transparent
            )
            .clickable(onClick = { section = it })
            .padding(12.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = it, style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = if (section == it) FontWeight.Bold else FontWeight.Normal,
              color = if (section == it) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
            )
          )
        }
      }
    }



    when (section) {
      photosString -> {
        val images by remember(files) { mutableStateOf(files.filter { it.name.contains("IMAGE_") }) }

        var zoomImage by remember { mutableStateOf<Uri?>(null) }
        val cameraLauncher = rememberLauncherForActivityResult(
          ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
          bitmap?.let {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val fileName = "IMAGE_${System.currentTimeMillis()}.png"
            val newFile = File(context.cacheDir, fileName)
            newFile.writeBytes(stream.toByteArray())
            updateFiles()
          }
        }

        val galleryLauncher = rememberLauncherForActivityResult(
          ActivityResultContracts.PickVisualMedia()
        ) { uri ->
          uri?.let { uri ->
            val bytes = context.contentResolver.openInputStream(uri).use { it?.readBytes() }
            bytes?.let {
              val fileName = "IMAGE_${System.currentTimeMillis()}.png"
              val newFile = File(context.cacheDir, fileName)
              newFile.writeBytes(bytes)
              updateFiles()
            }
          }
        }

        AnimatedVisibility(zoomImage != null) {
          androidx.compose.ui.window.Dialog(
            onDismissRequest = {},
          ) {
            Box(
              modifier = Modifier.fillMaxSize()
            ) {

              IconButton(
                onClick = { zoomImage = null },
                modifier = Modifier
                  .padding(top = 12.dp)
                  .align(Alignment.TopEnd),
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface)
              ) {
                Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.onSurface)
              }
              AsyncImage(
                model = zoomImage,
                contentDescription = null,
                modifier = Modifier
                  .align(Alignment.Center)
                  .clip(RoundedCornerShape(12.dp))
                  .fillMaxWidth(),
              )
            }

          }
        }
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Button(
            onClick = {
              cameraLauncher.launch()
            },
            colors = ButtonDefaults.buttonColors(

            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              stringResource(R.string.take_photo),
              style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
          }

          Button(
            onClick = { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              stringResource(R.string.choose_from_gallery),
              style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
              )
            )
          }

          Text(
            "${stringResource(R.string.files)}: $countFiles • ${stringResource(R.string.size)}: ${
              "%.2f".format(
                totalSize
              )
            } MB", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
          )
          Spacer(Modifier.height(8.dp))
          Text(
            stringResource(R.string.temporary_photos),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
              fontSize = 20.sp
            )
          )

          if (images.isNotEmpty()) {
            LazyVerticalGrid(
              modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.83f),

              columns = GridCells.Fixed(3),
              verticalArrangement = Arrangement.spacedBy(12.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              items(images) {
                AsyncImage(
                  model = it.toUri(),
                  contentDescription = null,
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable(onClick = { zoomImage = it.toUri() }),
                  contentScale = ContentScale.Crop
                )

              }
            }
          } else {
            Text(
              stringResource(R.string.no_temporary_photos),
            )
          }

          Row(modifier = Modifier.fillMaxWidth()) {
            var showClearCacheDialog by remember { mutableStateOf(false) }
            if (showClearCacheDialog) {
              AlertDialog(onDismissRequest = { showClearCacheDialog = false }, title = {
                Text(stringResource(R.string.clear_cache))
              }, text = {
                Text(stringResource(R.string.clear_all_temporary_files))
              }, confirmButton = {
                Button(onClick = {
                  context.cacheDir.listFiles { it.name.contains("IMAGE_") }.forEach {
                    it.delete()
                  }
                  context.cacheDir.listFiles { it.name.contains("Gravação") }.forEach {
                    it.delete()
                  }
                  updateFiles()
                  showClearCacheDialog = false
                }) {
                  Text(stringResource(R.string.yes), style = MaterialTheme.typography.bodyLarge)
                }
              }, dismissButton = {
                OutlinedButton(
                  onClick = { showClearCacheDialog = false }) {
                  Text(stringResource(R.string.no), style = MaterialTheme.typography.bodyLarge)
                }
              })
            }
            Box(
              modifier = Modifier

                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                  showClearCacheDialog = true
                }
                .padding(vertical = 16.dp, horizontal = 1.dp)) {
              Text(
                text = stringResource(R.string.clear_cache),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }


      }

      audioString -> {

        fun getDuration(uri: Uri): Long {
          val retriever = MediaMetadataRetriever().apply {
            setDataSource(context, uri)
          }
          val duration =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
              ?: 0L
          return duration
        }

        var state by remember { mutableStateOf(AudioPlayerState()) }

        val guides = remember { repository.getAudioGuides() }


        LaunchedEffect(state.phase, state.currentAudio) {
          if (state.phase == AudioPlayerPhase.PLAYING) {
            val guide = state.currentAudio
            guide?.let {
              val file = File(context.cacheDir, guide.file)
              val duration = getDuration(file.toUri())
              var lastTime = System.currentTimeMillis()

              while (
                state.phase == AudioPlayerPhase.PLAYING
                && state.progressMillis <= duration
              ) {
                val now = System.currentTimeMillis()
                val dt = now - lastTime
                lastTime = now
                state = state.copy(progressMillis = state.progressMillis + dt)
                delay(16.milliseconds)
              }

            }
          }
        }





        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            stringResource(R.string.audio_guide), style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
              fontSize = 20.sp
            )
          )


          LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(guides) { guide ->
              val bytes = context.assets.open("audio/${guide.file}").use { it.readBytes() }
              val file = File(context.cacheDir, guide.file)
              file.writeBytes(bytes)
              val duration = getDuration(uri = file.toUri())


              AudioCard(
                title = if (context.resources.configuration.locale.language == "pt-BR") guide.titlePt else guide.titleEn,
                durationMillis = duration,
                progressMillis = state.progressMillis,
                onClick = {
                  when (state.phase) {
                    AudioPlayerPhase.IDLE -> {
                      appAudioPlayer.start(uri = file.toUri())
                      state = state.copy(phase = AudioPlayerPhase.PLAYING, currentAudio = guide)
                    }

                    AudioPlayerPhase.PAUSED -> {
                      if (state.currentAudio == guide) {
                        appAudioPlayer.resume()
                        state = state.copy(phase = AudioPlayerPhase.PLAYING)
                      } else {
                        appAudioPlayer.stop()
                        appAudioPlayer.start(file.toUri())
                        state = state.copy(
                          phase = AudioPlayerPhase.PLAYING,
                          currentAudio = guide,
                          progressMillis = 0,
                          durationMillis = getDuration(file.toUri())
                        )
                      }
                    }

                    AudioPlayerPhase.PLAYING -> {
                      if (state.currentAudio == guide) {
                        appAudioPlayer.pause()
                        state = state.copy(phase = AudioPlayerPhase.PAUSED)
                      } else {
                        appAudioPlayer.stop()
                        appAudioPlayer.start(uri = file.toUri())
                        state = state.copy(
                          phase = AudioPlayerPhase.PLAYING,
                          currentAudio = guide,
                          progressMillis = 0,
                          durationMillis = getDuration(file.toUri())
                        )
                      }
                    }
                  }

                },
                onSeekMillis = {
                  state = state.copy(progressMillis = it)
                  appAudioPlayer.seekTo(state.progressMillis)

                },
                state = state,
                guide
              )

            }
          }


          Spacer(Modifier.height(8.dp))


          Text(
            stringResource(R.string.voice_recorder),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
              fontSize = 20.sp
            )
          )

          var isRecording by remember { mutableStateOf(false) }

          DisposableEffect(Unit) {
            onDispose {
              if (isRecording) {
                appAudioRecorder.stop()
                isRecording = false
              }
            }
          }
          var recordingMillis by remember { mutableStateOf(0L) }
          var hasMicPermission by remember {
            mutableStateOf(
              context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            )
          }
          val micPermission = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
          ) { isGranted -> hasMicPermission = isGranted }

          LaunchedEffect(isRecording) {
            if (isRecording) {

              if (state.phase == AudioPlayerPhase.PLAYING) {
                appAudioPlayer.stop()
                state = state.copy(
                  phase = AudioPlayerPhase.IDLE,
                  currentAudio = null,
                  durationMillis = 0,
                  progressMillis = 0
                )
              }

              val count = files.count { it.name.contains("Gravação") }
              val file = File(context.cacheDir, "Gravação ${count + 1}.mp3")
              appAudioRecorder.start(file)
              var lastTime = System.currentTimeMillis()

              while (recordingMillis < 30_000L && isRecording) {
                val now = System.currentTimeMillis()
                val dt = now - lastTime

                lastTime = now
                recordingMillis += dt
                delay(16.milliseconds)
              }
              isRecording = false
              updateFiles()
            }
          }

          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "00:${if (recordingMillis / 1000 < 10) "0${recordingMillis / 1000}" else recordingMillis / 1000}",
              style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
              )
            )
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
              if (isRecording) {
                Box(
                  Modifier
                    .clip(CircleShape)
                    .size(10.dp)
                    .background(MaterialTheme.colorScheme.error)
                )
                Text(
                  text = stringResource(R.string.recording),
                  style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              } else {
                Text(
                  text = stringResource(R.string.ready),
                  style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
            }

          }
          Box(
            modifier = Modifier
              .padding(12.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.primary)
              .clickable {
                if (!hasMicPermission) {
                  micPermission.launch(Manifest.permission.RECORD_AUDIO)
                } else {
                  if (!isRecording) {
                    isRecording = true
                  } else {
                    appAudioRecorder.stop()
                    recordingMillis = 0L
                    isRecording = false
                    updateFiles()
                  }
                }
              }
              .padding(vertical = 16.dp, horizontal = 1.dp)) {
            Text(
              text = if (!isRecording) stringResource(R.string.record) else stringResource(R.string.stop),
              color = MaterialTheme.colorScheme.surface,
              fontWeight = FontWeight.SemiBold
            )
          }


          val recordings by remember(files) { mutableStateOf(files.filter { it.name.contains("Gravação") }) }

          LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(recordings) { recording ->
              val duration = getDuration(uri = recording.toUri())


              val recordingGuide = AudioGuide(
                durationSec = getDuration(recording.toUri()).toInt() / 1000.toInt(),
                file = recording.name,
                id = "",
                titleEn = recording.nameWithoutExtension,
                titlePt = recording.nameWithoutExtension
              )

              AudioCard(
                title = recording.nameWithoutExtension,
                durationMillis = duration,
                progressMillis = state.progressMillis,
                onClick = {
                  when (state.phase) {
                    AudioPlayerPhase.IDLE -> {
                      appAudioPlayer.start(uri = recording.toUri())
                      state = state.copy(
                        phase = AudioPlayerPhase.PLAYING,
                        currentAudio = recordingGuide
                      )
                    }

                    AudioPlayerPhase.PAUSED -> {
                      if (state.currentAudio == recordingGuide) {
                        appAudioPlayer.resume()
                        state = state.copy(phase = AudioPlayerPhase.PLAYING)
                      } else {
                        appAudioPlayer.stop()
                        appAudioPlayer.start(recording.toUri())
                        state = state.copy(
                          phase = AudioPlayerPhase.PLAYING,
                          currentAudio = recordingGuide,
                          progressMillis = 0,
                          durationMillis = getDuration(recording.toUri())
                        )
                      }
                    }

                    AudioPlayerPhase.PLAYING -> {
                      if (state.currentAudio == recordingGuide) {
                        appAudioPlayer.pause()
                        state = state.copy(phase = AudioPlayerPhase.PAUSED)
                      } else {
                        appAudioPlayer.stop()
                        appAudioPlayer.start(uri = recording.toUri())
                        state = state.copy(
                          phase = AudioPlayerPhase.PLAYING,
                          currentAudio = recordingGuide,
                          progressMillis = 0,
                          durationMillis = getDuration(recording.toUri())
                        )
                      }
                    }
                  }

                },
                onSeekMillis = {
                  state = state.copy(progressMillis = it)
                  appAudioPlayer.seekTo(state.progressMillis)

                },
                state = state,
                audioGuide = recordingGuide
              )

            }
          }


        }
      }

      videoString -> {

        val videos = remember { repository.getVideos() }


        LaunchedEffect(Unit) {
          videos.forEach { video ->
            val bytes = context.assets.open("video/${video.file}").use { it.readBytes() }
            val file = File(context.cacheDir, video.file)
            file.writeBytes(bytes)

            val bytesThumb = context.assets.open("video/${video.thumbnail}").use { it.readBytes() }
            val fileThumb = File(context.cacheDir, video.thumbnail)
            fileThumb.writeBytes(bytesThumb)

          }
        }

//          onDispose {
//            context.cacheDir.listFiles { it.name.contains("trail_") || it.name.contains("thumb_") }
//              .forEach { it.delete() }
//          }
        VideoPlayer(videos)

      }
    }

  }
}

@SuppressLint("NonObservableLocale")
@Composable
fun VideoPlayer(videos: List<Video>) {


  val context = LocalContext.current
  var exoPlayer = remember { ExoPlayer.Builder(context).build() }
  var state by remember { mutableStateOf(VideoPlayerPhase.IDLE) }
  var currentVideo by remember { mutableStateOf(videos[0]) }

  var isMuted by remember { mutableStateOf(exoPlayer.isDeviceMuted) }

  fun getThumbUri(video: Video) = File(context.cacheDir, video.thumbnail).toUri()
  fun getVideoUri(video: Video) = File(context.cacheDir, video.file).toUri()

  fun getDuration(video: Video): Long {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, getVideoUri(video))
    return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
      ?: 0L
  }

  var duration by remember(currentVideo) { mutableStateOf(getDuration(currentVideo)) }
  var progress by remember(currentVideo) { mutableStateOf(0L) }


  DisposableEffect(Unit) {
    onDispose {
      if (state == VideoPlayerPhase.PLAYING) {
        exoPlayer.stop()
        exoPlayer.release()
      }
    }
  }
  LaunchedEffect(state) {
    if (state == VideoPlayerPhase.PLAYING) {
      var lastFrame = System.currentTimeMillis()
      while (progress <= duration) {
        val now = System.currentTimeMillis()
        val dt = now - lastFrame
        lastFrame = now
        progress += dt
        delay(16.milliseconds)
      }
//      exoPlayer.stop()
//      exoPlayer.release()
      state = VideoPlayerPhase.IDLE

    }

  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .aspectRatio(16f / 9f)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      contentAlignment = Alignment.Center
    ) {

      if (state == VideoPlayerPhase.IDLE) {

        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          AsyncImage(
            model = getThumbUri(currentVideo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
          )
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.onSurface.copy(0.5f))
              .clickable(onClick = {

                progress = 0L
                state = VideoPlayerPhase.PLAYING

                exoPlayer.setMediaItem(MediaItem.fromUri(getVideoUri(currentVideo)))
                exoPlayer.prepare()
                exoPlayer.play()

              }),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.PlayArrow, null, tint = MaterialTheme.colorScheme.surface)
          }
        }
      } else {

        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Player(exoPlayer, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.onSurface.copy(0.5f))
              .clickable(
                onClick = {
                  when (state) {
                    VideoPlayerPhase.IDLE -> {
                      progress = 0L
                      state = VideoPlayerPhase.PLAYING

                      exoPlayer.setMediaItem(MediaItem.fromUri(getVideoUri(currentVideo)))
                      exoPlayer.prepare()
                      exoPlayer.play()
                    }

                    VideoPlayerPhase.PLAYING -> {

                      state = VideoPlayerPhase.PAUSED
                      exoPlayer.pause()
                    }

                    VideoPlayerPhase.PAUSED -> {
                      state = VideoPlayerPhase.PLAYING
                      exoPlayer.play()
                    }
                  }
                }
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              if (state == VideoPlayerPhase.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
              null,
              tint = MaterialTheme.colorScheme.surface
            )
          }

        }
      }
    }


    Column(

      modifier = Modifier.padding(top = 24.dp)
    ) {

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = if (Locale.getDefault().language == "pt-BR") currentVideo.titlePt else currentVideo.titleEn,
          style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
          color = MaterialTheme.colorScheme.onSurface,
        )


        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = {
              isMuted = !isMuted
              if (isMuted) exoPlayer.mute() else exoPlayer.unmute()

            })
            .padding(8.dp), contentAlignment = Alignment.Center
        ) {
          Icon(if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp, null)
        }
      }
      @OptIn(ExperimentalMaterial3Api::class)
      Slider(
        value = progress.toFloat(),
        onValueChange = { value ->
//          onSeekMillis(value.toLong())
          progress = value.toLong()
          exoPlayer.seekTo(progress)
        },
        valueRange = 0f..duration.toFloat(),
        track = {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(12.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surface)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth(progress / duration.toFloat())
                .height(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
            )
          }
        },
        thumb = {

        }
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "00:${if (progress / 1000 < 10) "0${progress / 1000}" else progress / 1000}",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 12.sp
        )

        Text(
          text = "00:${if (duration / 1000 < 10) "0${duration / 1000}" else duration / 1000}",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 12.sp
        )
      }
    }



    Spacer(Modifier.height(12.dp))
    Text(
      text = stringResource(R.string.video),
      style = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
      )
    )
    LazyColumn(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      items(videos) {
        VideoItem(
          video = it,
          onPlay = {
            when (state) {
              VideoPlayerPhase.IDLE -> {
                progress = 0L
                state = VideoPlayerPhase.IDLE
                currentVideo = it
              }

              VideoPlayerPhase.PLAYING -> {
                exoPlayer.stop()
                progress = 0L
                state = VideoPlayerPhase.IDLE
                currentVideo = it
              }

              VideoPlayerPhase.PAUSED -> {
                exoPlayer.stop()
                progress = 0L
                state = VideoPlayerPhase.IDLE
                currentVideo = it
              }
            }

          },
          isSelected = currentVideo == it,
          thumb = getThumbUri(it),
          duration = getDuration(it)
        )

      }

    }
  }

}

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun VideoItem(video: Video, onPlay: () -> Unit, isSelected: Boolean, thumb: Uri, duration: Long) {
  Row(
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .fillMaxWidth()
      .background(
        if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent
      )
      .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      AsyncImage(
        model = thumb,
        null,
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .height(50.dp)
          .aspectRatio(16f / 9f),
        contentScale = ContentScale.Crop
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(if (LocalContext.current.resources.configuration.locale.language == "pt-BR") video.titlePt else video.titleEn)
        Text(
          text = "00:${if (duration / 1000 < 10) "0${duration / 1000}" else duration / 1000}",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 12.sp
        )
      }
    }

    IconButton(
      onClick = onPlay
    ) {
      Icon(Icons.Default.PlayArrow, null)
    }
  }
}

@Composable
fun AudioCard(
  title: String,
  durationMillis: Long,
  progressMillis: Long,
  onClick: () -> Unit,
  onSeekMillis: (Long) -> Unit,
  state: AudioPlayerState,
  audioGuide: AudioGuide
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {

    Column(
      modifier = Modifier.fillMaxWidth(0.8f),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
      )
      if (!(state.currentAudio == audioGuide && (state.phase == AudioPlayerPhase.PLAYING || state.phase == AudioPlayerPhase.PAUSED))) {
        Text(
          text = "00:${if (durationMillis / 1000 < 10) "0${durationMillis / 1000}" else durationMillis / 1000}",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 12.sp
        )
      } else {
        Column() {
          @OptIn(ExperimentalMaterial3Api::class)
          Slider(
            value = progressMillis.toFloat(),
            onValueChange = { value ->
              onSeekMillis(value.toLong())
            },
            valueRange = 0f..durationMillis.toFloat(),
            track = {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(12.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.surface)
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth(progressMillis / durationMillis.toFloat())
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                )
              }
            },
            thumb = {

            }
          )

          Text(
            text = "00:${if (progressMillis / 1000 < 10) "0${progressMillis / 1000}" else progressMillis / 1000}/00:${if (durationMillis / 1000 < 10) "0${durationMillis / 1000}" else durationMillis / 1000}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
          )
        }
      }

    }

    Box(
      modifier = Modifier
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primary)
        .clickable(onClick = onClick)
        .padding(8.dp)
    ) {
      Icon(
        if (state.currentAudio == audioGuide && state.phase == AudioPlayerPhase.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
        null,
        tint = MaterialTheme.colorScheme.surface
      )
    }

  }
}