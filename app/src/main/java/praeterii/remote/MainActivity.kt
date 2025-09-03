package praeterii.remote

import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import praeterii.remote.ui.composables.TvRemoteUi
import praeterii.remote.ui.theme.RemoteTheme
import praeterii.remote.utils.LgMagicRemoteCommandMapper
import praeterii.remote.utils.RemoteButtonType

class MainActivity : ComponentActivity() {
    private val carrierFrequencyHz = 38000 // Common frequency
    private val patternMapper = LgMagicRemoteCommandMapper()
    private lateinit var irManager: ConsumerIrManager
    private var hasIrEmitter: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            irManager = getSystemService(CONSUMER_IR_SERVICE) as ConsumerIrManager
            hasIrEmitter = irManager.hasIrEmitter()
        } catch (e: Exception) {
            Log.e(
                "MainActivity",
                "Could not initialize ConsumerIrManager or IR emitter not found.",
                e
            )
            hasIrEmitter = false
        }

        enableEdgeToEdge()
        setContent {
            RemoteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        TvRemoteUi(
                            onButtonClick = ::sendIrCommand,
                            onNumberClick = ::sendIrCommandForNumber
                        )
                    }
                }
            }
        }
    }

    private fun sendIrCommand(buttonType: RemoteButtonType) {
        val pattern: IntArray = patternMapper.getPattern(buttonType)
        try {
            irManager.transmit(carrierFrequencyHz, pattern)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error transmitting IR signal for ${buttonType.name}", e)
        }
    }

    private fun sendIrCommandForNumber(number: Int) {
        val pattern: IntArray = patternMapper.getPattern(numberButton = number)
        try {
            irManager.transmit(carrierFrequencyHz, pattern)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error transmitting IR signal for number $number", e)
        }
    }
}
