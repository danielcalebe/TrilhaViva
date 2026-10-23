package edu.ws2027.mc01.am.ui.screens

import android.Manifest
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
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

@OptIn(ExperimentalGetImage::class)
@Composable
fun Scan(modifier: Modifier = Modifier) {

  val context = LocalContext.current

  var a by remember { mutableStateOf("") }
  var hasPermission by remember {
    mutableStateOf(
      context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    )
  }

  val galleryLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    uri?.let { uri ->


      try {

        val image: InputImage = InputImage.fromFilePath(context, uri)
        val barcodeScanner = BarcodeScanning.getClient(
          BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
              Barcode.FORMAT_QR_CODE,
              Barcode.FORMAT_EAN_13,
              Barcode.FORMAT_CODE_128
            )
            .build()
        )

        val result = barcodeScanner.process(image)
          .addOnCompleteListener { data ->
            a = data.result.firstOrNull()?.rawValue.toString()
          }
          .addOnFailureListener {
            Toast.makeText(context, R.string.no_code_found_in_the_image, Toast.LENGTH_LONG).show()

          }
          .addOnCanceledListener {

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
    Text(
      text = stringResource(R.string.scan_code),
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
      ),
      modifier = Modifier.padding(16.dp)
    )


    Box(
      modifier = Modifier
        .fillMaxSize()
    ) {

      if (hasPermission) {
        ScannerView(
          modifier = Modifier.fillMaxSize(),
          onBarcodeScanned = {
            a = it
            Log.d("QR_SCAN", a.toString())
          }
        )




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
            .padding(vertical = 16.dp, horizontal = 1.dp)
        ) {
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
            Icons.Outlined.PhotoCamera,
            null,
            modifier = Modifier.scale(1.5f)
          )
          Text(
            text = stringResource(R.string.camera_permission_is_required_to_scan_codes),
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.SemiBold,
              textAlign = TextAlign.Center
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
              .padding(vertical = 12.dp, horizontal = 1.dp)
          ) {
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
      path = path1,
      color = color,
      style = Stroke(stroke, cap = StrokeCap.Round)
    )

    drawPath(
      path = path2,
      color = color,
      style = Stroke(stroke, cap = StrokeCap.Round)
    )
    drawPath(
      path = path3,
      color = color,
      style = Stroke(stroke, cap = StrokeCap.Round)
    )
    drawPath(
      path = path4,
      color = color,
      style = Stroke(stroke, cap = StrokeCap.Round)
    )
  }
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerView(modifier: Modifier = Modifier, onBarcodeScanned: (String) -> Unit) {
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
            BarcodeScannerOptions.Builder()
              .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_CODE_128
              )
              .build()
          )

          val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
              it.setAnalyzer(executor) { imageProxy ->
                processImageProxy(barcodeScanner, imageProxy, onBarcodeScanned)
              }
            }

          val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

          try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
              lifeCycleOwner,
              cameraSelector,
              preview,
              imageAnalysis
            )
          } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
          }
        },
        executor
      )

      previewView

    },
    modifier = modifier
  )
}

@ExperimentalGetImage
fun processImageProxy(
  barcodeScanner: BarcodeScanner,
  imageProxy: androidx.camera.core.ImageProxy,
  onBarcodeScanned: (String) -> Unit
) {
  imageProxy.image?.let { image ->
    val inputImage = InputImage.fromMediaImage(
      image,
      imageProxy.imageInfo.rotationDegrees
    )
    barcodeScanner.process(inputImage)
      .addOnSuccessListener { barcodes ->
        barcodes.firstOrNull()?.rawValue?.let { barcode ->
          onBarcodeScanned(barcode)
        }
      }
      .addOnFailureListener { it.printStackTrace() }
      .addOnCompleteListener { imageProxy.close() }
  } ?: imageProxy.close()
}

