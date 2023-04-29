package com.eborchani.station.qrcode.features.generate.ui.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eborchani.station.qrcode.features.generate.ui.screens.GenerateQrCodeScreen
import com.eborchani.station.qrcode.features.generate.ui.screens.ManufacturerQrCodeScreen
import com.eborchani.station.qrcode.features.generate.ui.screens.QrCodeResultScreen

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.GenerateQrCodeScreen.route,
    ) {
        composable(Destinations.GenerateQrCodeScreen.route) {
            GenerateQrCodeScreen(
                onQrCodeGenerated = { station, qrCode ->
                    navController.navigate(Destinations.QrCodeResultScreen.route(station, qrCode))
                },
                onNavigateToManufacturerCodes = { navController.navigate(Destinations.ManufacturerQrCodeScreen.route) }
            )
        }
        composable(
            route = Destinations.QrCodeResultScreen.route,
            arguments = listOf(
                navArgument(Destinations.QrCodeResultScreen.stationNameArg) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(Destinations.QrCodeResultScreen.qrCodeArg) {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) {
            val stationName = it.arguments?.getString(Destinations.QrCodeResultScreen.stationNameArg)
            val qrCode = it.arguments?.getString(Destinations.QrCodeResultScreen.qrCodeArg)
            val subject = "Partage du QR code de $stationName"
            if (stationName != null && qrCode != null) {
                QrCodeResultScreen(
                    stationName = stationName,
                    qrCode = qrCode,
                    onBack = { navController.popBackStack() },
                    onSharedClicked = { uri ->
                        val sharingIntent = Intent(Intent.ACTION_SEND)
                            .setType("image/png")
                            .putExtra(Intent.EXTRA_SUBJECT, subject)
                            .putExtra(Intent.EXTRA_STREAM, uri)
                        context.startActivity(Intent.createChooser(sharingIntent, subject))
                    }
                )
            }
        }
        composable(Destinations.ManufacturerQrCodeScreen.route) {
            ManufacturerQrCodeScreen(onBack = { navController.popBackStack() })
        }
    }
}

object Destinations {
    object GenerateQrCodeScreen {
        const val route = "GenerateQrCodeScreen"
    }

    object QrCodeResultScreen {
        private const val root = "QrCodeResultScreen"
        const val stationNameArg = "stationName"
        const val qrCodeArg = "qrCode"
        const val route = "$root?$stationNameArg={$stationNameArg}&$qrCodeArg={$qrCodeArg}"
        fun route(stationName: String, qrCode: String): String {
            return "$root?$stationNameArg=$stationName&$qrCodeArg=$qrCode"
        }
    }

    object ManufacturerQrCodeScreen {
        const val route = "ManufacturerQrCodeScreen"
    }
}
