package com.eborchani.station.qrcode

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var keepScreenOn = true
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            keepScreenOn
        }
        lifecycleScope.launch {
            delay(2000L)
            keepScreenOn = false
            viewModel.log()
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    fun log() {
        useCase.log("test log()")
    }
}

class UseCase @Inject constructor() {

    fun log(content: String) {
        Log.d(this::class.java.name, content)
    }
}
