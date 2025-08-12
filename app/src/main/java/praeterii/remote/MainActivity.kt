package praeterii.remote

import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
                            onButtonClick = { buttonType ->
                                Log.d("TvRemoteUi", "Button clicked: ${buttonType.name}")
                                if (hasIrEmitter) {
                                    sendIrCommand(buttonType)
                                } else {
                                    Toast.makeText(
                                        baseContext,
                                        "No IR emitter found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.w("TvRemoteUi", "Cannot send IR command: No IR Emitter.")
                                }
                            },
                            onNumberClick = { number ->
                                Log.d("TvRemoteUi", "Number clicked: $number")
                                if (hasIrEmitter) {
                                    sendIrCommandForNumber(number)
                                } else {
                                    Toast.makeText(
                                        baseContext,
                                        "No IR emitter found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.w("TvRemoteUi", "Cannot send IR command: No IR Emitter.")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun sendIrCommand(buttonType: RemoteButtonType) {
        val pattern: IntArray = patternMapper.getPattern(buttonType)

        try {
            Log.d(
                "MainActivity",
                "Transmitting IR: Freq=$carrierFrequencyHz, Pattern=${pattern.joinToString(",")}"
            )
            irManager.transmit(carrierFrequencyHz, pattern)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error transmitting IR signal for ${buttonType.name}", e)
        }
    }

    private fun sendIrCommandForNumber(number: Int) {
        if (!hasIrEmitter) return

        val pattern: IntArray? = when (number) {
            0 -> intArrayOf( /* ... pattern for number 0 ... */)
            1 -> intArrayOf( /* ... pattern for number 1 ... */)
            2 -> intArrayOf( /* ... pattern for number 2 ... */)
            3 -> intArrayOf( /* ... pattern for number 3 ... */)
            4 -> intArrayOf( /* ... pattern for number 4 ... */)
            5 -> intArrayOf( /* ... pattern for number 5 ... */)
            6 -> intArrayOf( /* ... pattern for number 6 ... */)
            7 -> intArrayOf( /* ... pattern for number 7 ... */)
            8 -> intArrayOf( /* ... pattern for number 8 ... */)
            9 -> intArrayOf( /* ... pattern for number 9 ... */)
            else -> {
                Log.w("MainActivity", "No IR pattern defined for number: $number")
                null
            }
        }

        if (pattern != null) {
            try {
                Log.d(
                    "MainActivity",
                    "Transmitting IR for number $number: Freq=$carrierFrequencyHz, Pattern=${
                        pattern.joinToString(",")
                    }"
                )
                irManager.transmit(carrierFrequencyHz, pattern)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error transmitting IR signal for number $number", e)
            }
        }
    }
}
