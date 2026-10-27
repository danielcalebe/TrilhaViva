@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package edu.ws2027.mc01.am.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import edu.ws2027.mc01.am.R
import edu.ws2027.mc01.am.data.Repository
import edu.ws2027.mc01.am.data.ScanResult
import java.util.Locale

@SuppressLint("LocalContextResourcesRead")
@OptIn(
  ExperimentalGetImage::class, ExperimentalMaterial3Api::class,
  ExperimentalMaterial3Api::class
)
@Composable
fun Scan(modifier: Modifier = Modifier, repository: Repository) {

  val context = LocalContext.current


  var scanResult by remember { mutableStateOf<ScanResult?>(null) }

  var shouldScan by remember { mutableStateOf(false) }

  var a by remember { mutableStateOf("") }
  var hasPermission by remember {
    mutableStateOf(
      context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    )
  }


  LaunchedEffect(hasPermission) {
    if (hasPermission) {
      shouldScan = true
    }
  }

  val galleryLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    uri?.let { uri ->

      try {
        val image: InputImage = InputImage.fromFilePath(context, uri)
        val barcodeScanner = BarcodeScanning.getClient(
          BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_QR_CODE, Barcode.FORMAT_EAN_13, Barcode.FORMAT_CODE_128
          ).build()
        )

        barcodeScanner.process(image).addOnSuccessListener { barcodes ->
          barcodes?.firstOrNull()?.let { barcode ->
            if (shouldScan) {
              val format = when (barcode.format) {
                Barcode.FORMAT_EAN_13 -> "EAN-13"
                Barcode.FORMAT_CODE_128 -> "CODE-128"
                Barcode.FORMAT_QR_CODE -> "QR Code"
                else -> "Unknown"
              }
              val type = when {
                barcode.rawValue?.contains("TV-") == true -> if (repository.getTrails()
                    .find { it.id == barcode.rawValue } != null
                ) R.string.trail else R.string.trail_not_found

                barcode.rawValue?.startsWith("https://") == true || barcode.rawValue?.startsWith("http://") == true -> R.string.link
                barcode.format == Barcode.FORMAT_EAN_13 || barcode.format == Barcode.FORMAT_CODE_128 -> R.string.barcode
                else -> R.string.text
              }
              scanResult = ScanResult(
                format = format,
                content = barcode?.rawValue ?: "",
                type = type
              )


              shouldScan = false
            }
          } ?:

          Toast.makeText(context, R.string.no_code_found_in_the_image, Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
          Toast.makeText(context, R.string.no_code_found_in_the_image, Toast.LENGTH_LONG).show()

        }.addOnCanceledListener {
          Toast.makeText(context, R.string.no_code_found_in_the_image, Toast.LENGTH_LONG).show()
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }


    }
  }


  val permissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted -> hasPermission = isGranted }
  LaunchedEffect(Unit) {
    permissionLauncher.launch(Manifest.permission.CAMERA)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {

    AnimatedVisibility(
      visible = !shouldScan && scanResult != null,
      enter = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(300, easing = LinearEasing)
      ),
      exit = slideOutVertically(
        targetOffsetY = { -it },
        animationSpec = tween(300, easing = LinearEasing)
      ),
    ) {
      ModalBottomSheet(
        onDismissRequest = {
          shouldScan = true
          scanResult = null
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ) {
        scanResult?.let { result ->
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {

            Text(
              text = stringResource(R.string.scan_result),
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
              ),
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {


              Text(
                stringResource(R.string.format),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
              Text(
                result.format,
                style = MaterialTheme.typography.bodyLarge.copy(
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold
                )
              )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {


              Text(
                stringResource(R.string.content),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
              )
              Text(
                result.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable(
                  onClick = {
                    if (result.type == R.string.link) {
                      val intent = Intent(Intent.ACTION_VIEW).apply {
                        setData(Uri.parse(result.content))
                      }
                      context.startActivity(intent)
                    }
                  }
                )
              )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {


              Text(
                stringResource(R.string.type),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
              Text(
                stringResource(result.type),
                style = MaterialTheme.typography.bodyLarge.copy(
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold
                )
              )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f))


            if (result.type == R.string.trail) {
              val trail = repository.getTrails().find { it.id == result.content }

              trail?.let { trail ->
                Row(
                  modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  val drawableId = remember(trail.imageName) {
                    context.resources.getIdentifier(
                      trail.imageName.removeSuffix(".png").removeSuffix(".jpg"),
                      "drawable",
                      context.packageName
                    )
                  }
                  Image(
                    painter = painterResource(drawableId),
                    contentDescription = null,
                    modifier = Modifier
                      .size(80.dp)
                      .clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                  )


                  Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                      text = if (Locale.getDefault().language == "pt-BR") trail.namePt else trail.nameEn,
                      style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                    )



                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Text(
                        text = trail.difficulty,
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontWeight = FontWeight.Normal,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier
                          .clip(CircleShape)
                          .background(MaterialTheme.colorScheme.surfaceVariant)
                          .padding(vertical = 4.dp, horizontal = 12.dp)
                      )


                      Text(
                        text = "${trail.length} km",
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontWeight = FontWeight.Normal,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier
                          .clip(CircleShape)
                          .background(MaterialTheme.colorScheme.surfaceVariant)
                          .padding(vertical = 4.dp, horizontal = 12.dp)
                      )
                    }
                  }
                }
              }
            }

            Box(
              modifier = Modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                  shouldScan = true
                  scanResult = null
                }
                .padding(vertical = 16.dp, horizontal = 1.dp)) {
              Text(
                text = stringResource(R.string.scan_again),
                color = MaterialTheme.colorScheme.surface,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

      }
    }

    Text(
      text = stringResource(R.string.scan_code), style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      ), modifier = Modifier.padding(16.dp)
    )


    Box(
      modifier = Modifier.fillMaxSize()
    ) {

      if (hasPermission) {
        ScannerView(
          modifier = Modifier.fillMaxSize(),
          onBarcodeScanned = { barcode ->
            if (shouldScan) {
              val format = when (barcode.format) {
                Barcode.FORMAT_EAN_13 -> "EAN-13"
                Barcode.FORMAT_CODE_128 -> "CODE-128"
                Barcode.FORMAT_QR_CODE -> "QR Code"
                else -> "Unknown"
              }
              val type = when {
                barcode.rawValue?.contains("TV-") == true -> if (repository.getTrails()
                    .find { it.id == barcode.rawValue } != null
                ) R.string.trail else R.string.trail_not_found

                barcode.rawValue?.contains("https://") == true || barcode.rawValue?.contains("http://") == true -> R.string.link
                barcode.format == Barcode.FORMAT_EAN_13 || barcode.format == Barcode.FORMAT_CODE_128 -> R.string.barcode
                else -> R.string.text
              }
              scanResult = ScanResult(
                format = format,
                content = barcode?.rawValue ?: "",
                type = type
              )


              shouldScan = false
            }
          },
        )
//        ScannerView(
//          modifier = Modifier.fillMaxSize(),
//          onBarcodeScanned = {
//
//            if (shouldScan) {
//              shouldScan = false
//
//              val format = when (barcode.format) {
//                Barcode.FORMAT_EAN_13 -> "EAN-13"
//                Barcode.FORMAT_CODE_128 -> "CODE-128"
//                Barcode.FORMAT_QR_CODE -> "QR Code"
//                else -> "Unknown"
//              }
//              val type = when {
//                barcode.rawValue?.contains("TV-") == true -> if ()
//
//                else -> ""
//              }
//            }
//          }
//              Log . d ("QR_SCAN", a.toString())
//
//      },
//      shouldScan = shouldScan
//      )


        Column(
          modifier = Modifier.align(Alignment.Center),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {

          ScanFrame()

          Text(
            text = stringResource(R.string.align_the_code_within_the_frame),
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center,
            ),
            modifier = Modifier.padding(top = 12.dp)
          )

        }

        Box(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
              galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            .padding(vertical = 16.dp, horizontal = 1.dp)) {
          Text(
            text = stringResource(R.string.pick_from_gallery),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
          )
        }

        Text(a, fontSize = 30.sp, fontWeight = FontWeight.Bold)
      } else {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
          Icon(
            Icons.Outlined.PhotoCamera, null, modifier = Modifier.scale(1.5f)
          )
          Text(
            text = stringResource(R.string.camera_permission_is_required_to_scan_codes),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface
          )

          Box(
            modifier = Modifier
              .padding(12.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.primary)
              .clickable {
                permissionLauncher.launch(Manifest.permission.CAMERA)
                if (!hasPermission) {
                  val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                  context.startActivity(intent.apply {
                    data = Uri.fromParts("package", context.packageName, null)
                  })
                }

              }
              .padding(vertical = 12.dp, horizontal = 1.dp)) {
            Text(
              text = stringResource(R.string.grant_permission),
              color = MaterialTheme.colorScheme.surface,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}


@Composable
fun ScanFrame(
  modifier: Modifier = Modifier,
  color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondary,
  segmentSize: Float = 90f,
  squareSize: Dp = 200.dp,
  stroke: Float = 15f
) {
  Canvas(
    modifier = modifier.size(squareSize)
  ) {

    val path1 = Path().apply {
      moveTo(segmentSize, 0f)
      lineTo(0f, 0f)
      lineTo(0f, segmentSize)
    }

    val path2 = Path().apply {
      moveTo(size.width - segmentSize, 0f)
      lineTo(size.width, 0f)
      lineTo(size.width, segmentSize)
    }

    val path3 = Path().apply {
      moveTo(0f, size.height - segmentSize)
      lineTo(0f, size.height)
      lineTo(segmentSize, size.height)
    }


    val path4 = Path().apply {
      moveTo(size.width - segmentSize, size.height)
      lineTo(size.width, size.height)
      lineTo(size.width, size.height - segmentSize)
    }

    drawPath(
      path = path1, color = color, style = Stroke(stroke, cap = StrokeCap.Round)
    )

    drawPath(
      path = path2, color = color, style = Stroke(stroke, cap = StrokeCap.Round)
    )
    drawPath(
      path = path3, color = color, style = Stroke(stroke, cap = StrokeCap.Round)
    )
    drawPath(
      path = path4, color = color, style = Stroke(stroke, cap = StrokeCap.Round)
    )
  }
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerView(
  modifier: Modifier = Modifier,
  onBarcodeScanned: (Barcode) -> Unit,
) {
  val context = LocalContext.current
  val lifeCycleOwner = LocalLifecycleOwner.current
  val cameraProviderFuture = remember {
    ProcessCameraProvider.getInstance(context)
  }



  AndroidView(
    factory = { context ->
      val previewView = PreviewView(context)
      val executor = ContextCompat.getMainExecutor(context)

      cameraProviderFuture.addListener(
        {
          val cameraProvider = cameraProviderFuture.get()
          val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
          }

          val barcodeScanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().setBarcodeFormats(
              Barcode.FORMAT_QR_CODE, Barcode.FORMAT_EAN_13, Barcode.FORMAT_CODE_128
            ).build()
          )

          val imageAnalysis =
            ImageAnalysis.Builder()
              .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
              .build().also {
                it.setAnalyzer(executor) { imageProxy ->
                  processImageProxy(
                    barcodeScanner,
                    imageProxy,
                    { barcode -> onBarcodeScanned(barcode); Log.d("bar-bar", barcode.toString()) }
                  )
                }
              }

          val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

          try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
              lifeCycleOwner, cameraSelector, preview, imageAnalysis
            )
          } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
          }
        }, executor
      )

      previewView

    }, modifier = modifier
  )
}

@ExperimentalGetImage
fun processImageProxy(
  barcodeScanner: BarcodeScanner,
  imageProxy: androidx.camera.core.ImageProxy,
  onBarcodeScanned: (Barcode) -> Unit
) {

  imageProxy.image?.let { image ->
    val inputImage = InputImage.fromMediaImage(
      image, imageProxy.imageInfo.rotationDegrees
    )
    barcodeScanner.process(inputImage).addOnSuccessListener { barcodes ->

      barcodes.firstOrNull()?.let { barcode ->
        Log.d("bar-bar", barcode.toString())
        onBarcodeScanned(barcode)
      }
    }.addOnFailureListener { it.printStackTrace() }.addOnCompleteListener { imageProxy.close() }
  } ?: imageProxy.close()
}

