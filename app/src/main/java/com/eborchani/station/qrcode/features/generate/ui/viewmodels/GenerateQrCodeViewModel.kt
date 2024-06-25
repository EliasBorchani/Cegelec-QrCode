package com.eborchani.station.qrcode.features.generate.ui.viewmodels

import android.webkit.URLUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eborchani.station.qrcode.features.generate.ui.screens.GenerateQrCodeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateQrCodeViewModel @Inject constructor() : ViewModel() {

    val state = MutableStateFlow(GenerateQrCodeScreenState())
    val qrCodeGenerated = MutableSharedFlow<Pair<String, String>>(replay = 0)

    fun updateSct(station: String) {
        state.value = state.value.copy(station = station)
        val hasError = !isStcOk(station)
        state.value = state.value.copy(stationError = hasError)
    }

    fun updateSide(side: GenerateQrCodeScreenState.Side) {
        state.value = state.value.copy(side = side)
    }

    fun updateSsid(ssid: String) {
        state.value = state.value.copy(wifiSsid = ssid)
    }

    fun updateWifiPassword(password: String) {
        state.value = state.value.copy(wifiPassword = password)
    }

    fun updateHost(host: String) {
        state.value = state.value.copy(host = host)
        val hasError = !URLUtil.isValidUrl(host)
        state.value = state.value.copy(hostError = hasError)
    }

    fun updateSmtpActivation(enabled: Boolean) {
        state.value = state.value.copy(smtpRecipient = if (enabled) state.value.smtpRecipient ?: "" else null)
    }

    fun updateSmtpRecipient(recipient: String) {
        state.value = state.value.copy(smtpRecipient = recipient)
    }

    fun updateSmtpHost(host: String) {
        state.value = state.value.copy(smtpHost = host)
    }

    fun updateSmtpAuth(auth: String) {
        state.value = state.value.copy(smtpAuth = auth)
    }

    fun updateSmtpPassword(password: String) {
        state.value = state.value.copy(smtpPassword = password)
    }


    fun generate() {
        if (isValid()) {
            viewModelScope.launch {
                val stationName = "${state.value.station}-${ if (state.value.side == GenerateQrCodeScreenState.Side.Left) "G" else "D"}"
                qrCodeGenerated.emit(stationName to buildQrCode())
            }
        } else {
            state.value = with(state.value) {
                copy(
                    stationError = station == null || !isStcOk(station),
                    sideError = side == null
                )
            }
        }
    }

    private fun buildQrCode(): String {
        return with(state.value) {
            "STCNUM=$station COTENUM=${if (side == GenerateQrCodeScreenState.Side.Left) "G" else "D"} SSID=${wifiSsid.orEmpty()} PASSWD=${wifiPassword.orEmpty()} ADRESSE=${host ?: "http://stationcharge.free.fr/"} SMTPHOST=$smtpHost SMTPPASS=$smtpPassword SMTPAUT=$smtpAuth SMTPDES=${smtpRecipient ?: "none"} "
        }
    }

    private fun isStcOk(stc: String): Boolean {
        return (stc.length == 2 || stc.length == 3) && stc.all { it.isDigit() }
    }

    private fun isValid(): Boolean {
        return with(state.value) {
            side != null && station != null && !stationError && (smtpRecipient == null || (smtpRecipient.isNotBlank() && smtpHost.isNotBlank() && smtpAuth.isNotBlank() && smtpPassword.isNotBlank()))
        }
    }
}
