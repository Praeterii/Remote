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
import praeterii.remote.utils.PatternUtils.necToPulsePattern
import praeterii.remote.utils.RemoteButtonType

class MainActivity : ComponentActivity() {
    private val carrierFrequencyHz = 38000 // Common frequency

    private lateinit var irManager: ConsumerIrManager
    private var hasIrEmitter: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            irManager = getSystemService(CONSUMER_IR_SERVICE) as ConsumerIrManager
            hasIrEmitter = irManager.hasIrEmitter()
        } catch (e: Exception) {
            Log.e("MainActivity", "Could not initialize ConsumerIrManager or IR emitter not found.", e)
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
                                    Toast.makeText(baseContext, "No IR emitter found", Toast.LENGTH_SHORT).show()
                                    Log.w("TvRemoteUi", "Cannot send IR command: No IR Emitter.")
                                }
                            },
                            onNumberClick = { number ->
                                Log.d("TvRemoteUi", "Number clicked: $number")
                                if (hasIrEmitter) {
                                    sendIrCommandForNumber(number)
                                } else {
                                    Toast.makeText(baseContext, "No IR emitter found", Toast.LENGTH_SHORT).show()
                                    Log.w("TvRemoteUi", "Cannot send IR command: No IR Emitter.")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Example function to map RemoteButtonType to IR codes
    private fun sendIrCommand(buttonType: RemoteButtonType) {
        if (!hasIrEmitter) return

        val pattern: IntArray? = when (buttonType) {
            RemoteButtonType.POWER -> necToPulsePattern(0x04, 0x08)
            RemoteButtonType.VOL_UP -> necToPulsePattern(0x04, 0x02)
            RemoteButtonType.VOL_DOWN -> intArrayOf(
                9121, 4377, 685, 475, 658, 476, 656, 1610, 656, 479,
                654, 482, 651, 483, 651, 483, 651, 483, 651, 1618,
                650, 1618, 650, 506, 627, 1640, 628, 1641, 627, 1641,
                627, 1640, 628, 1641, 627, 1641, 627, 1641, 627, 506,
                627, 506, 628, 507, 627, 506, 627, 507, 627, 507,
                626, 507, 627, 507, 627, 1641, 626, 1641, 627, 1641,
                627, 1641, 627, 1641, 627, 1641, 627, 39937, 9096, 2169,
                651
            )
            RemoteButtonType.CH_UP -> intArrayOf()  // TODO
            RemoteButtonType.OK -> intArrayOf(
                9120, 4379, 683, 476, 657, 477, 655, 1612, 654, 480,
                652, 483, 651, 484, 650, 484, 650, 484, 650, 1618,
                649, 1642, 626, 485, 649, 1642, 626, 1642, 626, 1642,
                626, 1642, 626, 1642, 626, 507, 627, 507, 627, 1642,
                626, 508, 625, 508, 626, 508, 626, 1643, 625, 508,
                626, 1643, 625, 1643, 625, 508, 626, 1643, 625, 1643,
                625, 1643, 625, 508, 625, 1643, 625
            )
            RemoteButtonType.DPAD_UP -> necToPulsePattern(0x04, 0x40)
            RemoteButtonType.DPAD_DOWN -> necToPulsePattern(0x04, 0x41)
            RemoteButtonType.DPAD_LEFT -> intArrayOf(
                9117, 4381, 682, 477, 656, 478, 655, 1612, 654, 481,
                651, 508, 625, 509, 625, 509, 624, 509, 625, 1643,
                625, 1643, 625, 509, 625, 1643, 625, 1643, 625, 1643,
                625, 1643, 625, 1643, 625, 1643, 625, 1643, 625, 1643,
                625, 509, 625, 509, 625, 509, 625, 509, 624, 509,
                625, 510, 624, 509, 625, 510, 624, 1644, 623, 1644,
                624, 1644, 624, 1644, 624, 1644, 624, 39940, 9092, 2197,
                624
            )
            RemoteButtonType.DPAD_RIGHT -> intArrayOf(
                9119, 4379, 683, 477, 656, 477, 656, 1611, 656, 479,
                653, 482, 651, 484, 649, 484, 650, 484, 649, 1618,
                650, 1618, 650, 507, 627, 1641, 626, 1642, 626, 1642,
                626, 1642, 626, 1642, 626, 508, 626, 1642, 626, 1642,
                626, 508, 626, 508, 626, 508, 626, 508, 626, 508,
                626, 1642, 626, 508, 626, 508, 625, 1643, 625, 1642,
                626, 1642, 626, 1643, 625, 1643, 625, 39940, 9093, 2196,
                625
            )
            RemoteButtonType.CH_DOWN -> intArrayOf()  // TODO
            RemoteButtonType.HOME -> intArrayOf(
                9100, 4400, 686, 474, 659, 474, 659, 1608, 658, 476,
                656, 478, 655, 479, 655, 479, 655, 479, 655, 1613,
                655, 1613, 655, 479, 654, 1613, 655, 1613, 655, 1613,
                655, 1613, 655, 1613, 655, 479, 655, 479, 654, 1613,
                655, 1613, 655, 1614, 654, 1613, 655, 1614, 654, 479,
                655, 1614, 654, 1614, 653, 480, 654, 479, 654, 480,
                654, 480, 654, 480, 653, 1614, 654, 39935, 9099, 2165,
                654
            )
            RemoteButtonType.BACK -> necToPulsePattern(0x04, 0x28)
            RemoteButtonType.MUTE -> intArrayOf(
                9097, 4402, 685, 475, 659, 475, 658, 1609, 657, 477,
                656, 480, 653, 480, 654, 480, 653, 481, 653, 1615,
                653, 1614, 654, 481, 653, 1615, 653, 1615, 653, 1615,
                653, 1615, 653, 1615, 653, 1615, 653, 481, 652, 481,
                653, 1616, 652, 482, 652, 482, 652, 482, 652, 482,
                652, 482, 652, 1616, 652, 1616, 651, 482, 652, 1617,
                651, 1617, 651, 1640, 628, 1640, 628, 39937, 9097, 2167,
                652
            )
            RemoteButtonType.INPUT -> necToPulsePattern(0x04, 0x0B)
        }

        if (pattern != null) {
            try {
                Log.d("MainActivity", "Transmitting IR: Freq=$carrierFrequencyHz, Pattern=${pattern.joinToString(",")}")
                irManager.transmit(carrierFrequencyHz, pattern)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error transmitting IR signal for ${buttonType.name}", e)
                // Handle transmission error (e.g., show a message)
            }
        }
    }

    private fun sendIrCommandForNumber(number: Int) {
        if (!hasIrEmitter) return

        val pattern: IntArray? = when (number) {
            0 -> intArrayOf( /* ... pattern for number 0 ... */ )
            1 -> intArrayOf( /* ... pattern for number 1 ... */ )
            2 -> intArrayOf( /* ... pattern for number 2 ... */ )
            3 -> intArrayOf( /* ... pattern for number 3 ... */ )
            4 -> intArrayOf( /* ... pattern for number 4 ... */ )
            5 -> intArrayOf( /* ... pattern for number 5 ... */ )
            6 -> intArrayOf( /* ... pattern for number 6 ... */ )
            7 -> intArrayOf( /* ... pattern for number 7 ... */ )
            8 -> intArrayOf( /* ... pattern for number 8 ... */ )
            9 -> intArrayOf( /* ... pattern for number 9 ... */ )
            else -> {
                Log.w("MainActivity", "No IR pattern defined for number: $number")
                null
            }
        }

        if (pattern != null) {
            try {
                Log.d("MainActivity", "Transmitting IR for number $number: Freq=$carrierFrequencyHz, Pattern=${pattern.joinToString(",")}")
                irManager.transmit(carrierFrequencyHz, pattern)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error transmitting IR signal for number $number", e)
            }
        }
    }

}