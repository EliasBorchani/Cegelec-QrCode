package com.eborchani.station.qrcode.features.generate.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eborchani.station.qrcode.ui.components.rememberQrBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeResultScreen(
    stationName: String,
    qrCode: String,
    onBack: () -> Unit,
    onSharedClicked: (Uri) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                title = { Text("QR code pour $stationName") },
            )
        }
    ) { innerPadding ->
        val qrCodeBitmap = rememberQrBitmap(content = qrCode)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = BitmapPainter(qrCodeBitmap.asImageBitmap()),
                    contentDescription = "qrCode"
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(modifier = Modifier.height(1.dp))
                Spacer(modifier = Modifier.height(8.dp))
                val scope = rememberCoroutineScope()
                val contentResolver = LocalContext.current.contentResolver
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            val compressionOutputStream = ByteArrayOutputStream()
                            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 0, compressionOutputStream)
                            val contentValues = ContentValues(1)
                            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                            if (uri != null) {
                                val outputStream = contentResolver.openOutputStream(uri)
                                if (outputStream != null) {
                                    outputStream.write(compressionOutputStream.toByteArray())
                                    outputStream.close()
                                    withContext(Dispatchers.Main) {
                                        onSharedClicked(uri)
                                    }
                                }
                            }
                        }
                    },
                ) {
                    Text(text = "Partager", color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun QrCodeResultScreenPreview() {
    QrCodeResultScreen(
        stationName = "021",
        qrCode = "faoelrfknaerôiflqnfioqef",
        onSharedClicked = {},
        onBack = {},
    )
}
