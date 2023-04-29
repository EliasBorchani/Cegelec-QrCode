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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eborchani.station.qrcode.R
import com.eborchani.station.qrcode.features.generate.ui.viewmodels.GenerateQrCodeViewModel
import com.eborchani.station.qrcode.ui.components.RadioGroup

@Composable
fun GenerateQrCodeScreen(
    viewModel: GenerateQrCodeViewModel = hiltViewModel(),
    onQrCodeGenerated: (station: String, qrCode: String) -> Unit,
    onNavigateToManufacturerCodes: () -> Unit,
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(key1 = "qrCode") {
        viewModel.qrCodeGenerated.collect { (station, qrCode) ->
            onQrCodeGenerated(station, qrCode)
        }
    }
    GenerateQrCodeScreen(
        state = state.value,
        onStationChanged = viewModel::updateSct,
        onSideChanged = viewModel::updateSide,
        onWifiSsidChanged = viewModel::updateSsid,
        onWifiPasswordChanged = viewModel::updateWifiPassword,
        onHostChanged = viewModel::updateHost,
        onGenerateClicked = viewModel::generate,
        onSeeManufacturerCodesClicked = onNavigateToManufacturerCodes,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GenerateQrCodeScreen(
    state: GenerateQrCodeScreenState,
    onStationChanged: (String) -> Unit,
    onSideChanged: (GenerateQrCodeScreenState.Side) -> Unit,
    onWifiSsidChanged: (String) -> Unit,
    onWifiPasswordChanged: (String) -> Unit,
    onHostChanged: (String) -> Unit,
    onGenerateClicked: () -> Unit,
    onSeeManufacturerCodesClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Générateur QR code pour station") },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo"
                    )
                },
                actions = {
                    IconButton(onClick = onSeeManufacturerCodesClicked) {
                        Icon(Icons.Filled.List, contentDescription = "manufacturer qr codes")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Numéro de Station : STC") },
                    placeholder = { Text("XX(X)") },
                    value = state.station.orEmpty(),
                    isError = state.stationError,
                    onValueChange = onStationChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                )
                if (state.stationError) {
                    Text(text = "Le numéro de station doit comporter 2 ou 3 chiffres", color = MaterialTheme.colorScheme.error)
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    val selected = when (state.side) {
                        GenerateQrCodeScreenState.Side.Left -> 0
                        GenerateQrCodeScreenState.Side.Right -> 1
                        null -> null
                    }
                    val keyboard = LocalSoftwareKeyboardController.current
                    RadioGroup(
                        selected = selected,
                        onSelectionChanged = {
                            val side =
                                if (it == 0) GenerateQrCodeScreenState.Side.Left else GenerateQrCodeScreenState.Side.Right
                            onSideChanged(side)
                            keyboard?.hide()
                        }
                    ) {
                        item { Text("Gauche") }
                        item { Text("Droite") }
                    }
                }
                if (state.sideError) {
                    Text(text = "Il manque le côté de la station", color = MaterialTheme.colorScheme.error)
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nom du WiFi") },
                    placeholder = { Text("SSID WiFi") },
                    value = state.wifiSsid.orEmpty(),
                    onValueChange = onWifiSsidChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                )
                var passwordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Mot de passe WiFi") },
                    value = state.wifiPassword.orEmpty(),
                    onValueChange = onWifiPasswordChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.CheckCircle else Icons.Filled.Lock
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisible = !passwordVisible }){
                            Icon(imageVector  = image, description)
                        }
                    },

                )
                val keyboard = LocalSoftwareKeyboardController.current
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Adresse du serveur") },
                    placeholder = { Text("http://xxxxx.xxx") },
                    value = state.host.orEmpty(),
                    isError = state.hostError,
                    onValueChange = onHostChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboard?.hide()
                        onGenerateClicked()
                    })
                )
                if (state.hostError) {
                    Text(text = "Ceci n'est pas une adresse valide", color = MaterialTheme.colorScheme.error)
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(modifier = Modifier.height(1.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = onGenerateClicked,
                ) {
                    Text(text = "Générer", color = MaterialTheme.colorScheme.onPrimary,)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

data class GenerateQrCodeScreenState(
    val station: String? = null,
    val stationError: Boolean = false,
    val side: Side? = null,
    val sideError: Boolean = false,
    val wifiSsid: String? = null,
    val wifiPassword: String? = null,
    val host: String? = null,
    val hostError: Boolean = false,
) {
    enum class Side { Left, Right }
}

@Preview
@Composable
private fun GenerateQrCodeScreenPreview() {
    GenerateQrCodeScreen(
        state = GenerateQrCodeScreenState(),
        onStationChanged = {},
        onSideChanged = {},
        onWifiSsidChanged = {},
        onWifiPasswordChanged = {},
        onHostChanged = {},
        onGenerateClicked = {},
        onSeeManufacturerCodesClicked = {},
    )
}
