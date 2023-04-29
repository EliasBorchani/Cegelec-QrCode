package com.eborchani.station.qrcode.features.generate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eborchani.station.qrcode.ui.components.rememberQrBitmap
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnsignedTypes::class)
@Composable
fun ManufacturerQrCodeScreen(
    onBack: () -> Unit,
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
                title = { Text("QR codes de programmation") },
            )
        }
    ) { innerPadding ->
        val serialQrCodeContent = remember {
            ubyteArrayOf(0x08u, 0xC6u, 0x04u, 0x08u, 0x00u, 0xF2u, 0x01u, 0x00u, 0xFEu, 0x33u)
                .toByteArray()
                .toString(StandardCharsets.ISO_8859_1)
        }
        val franceQrCodeContent = remember {
            ubyteArrayOf(0x08u, 0xC6u, 0x04u, 0x08u, 0x00u, 0xF6u, 0x01u, 0x08u, 0xFEu, 0x27u)
                .toByteArray()
                .toString(StandardCharsets.ISO_8859_1)
        }
        val blinkQrCodeContent = remember {
            ubyteArrayOf(0x08u, 0xC6u, 0x04u, 0x08u, 0x00u, 0xF2u, 0x02u, 0x00u, 0xFEu, 0x32u)
                .toByteArray()
                .toString(StandardCharsets.ISO_8859_1)
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Mode Serial :")
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = BitmapPainter(rememberQrBitmap(content = serialQrCodeContent).asImageBitmap()),
                    contentDescription = "qrCode"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Mode France :")
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = BitmapPainter(rememberQrBitmap(content = franceQrCodeContent).asImageBitmap()),
                    contentDescription = "qrCode"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Mode Clignote :")
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = BitmapPainter(rememberQrBitmap(content = blinkQrCodeContent).asImageBitmap()),
                    contentDescription = "qrCode"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
private fun ManufacturerQrCodeScreenPreview() {
    ManufacturerQrCodeScreen(
        onBack = {},
    )
}
