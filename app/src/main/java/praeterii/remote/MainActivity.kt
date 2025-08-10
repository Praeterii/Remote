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
            RemoteButtonType.POWER -> necToPulsePattern(0x04, 0x08).second
            RemoteButtonType.VOL_UP -> intArrayOf()
            RemoteButtonType.VOL_DOWN -> intArrayOf()
            RemoteButtonType.CH_UP -> intArrayOf()
            RemoteButtonType.OK -> intArrayOf()
            RemoteButtonType.DPAD_UP -> intArrayOf()
            RemoteButtonType.DPAD_DOWN -> intArrayOf()
            RemoteButtonType.DPAD_LEFT -> intArrayOf()
            RemoteButtonType.DPAD_RIGHT -> intArrayOf()
            RemoteButtonType.CH_DOWN -> intArrayOf()
            RemoteButtonType.HOME -> intArrayOf()
            RemoteButtonType.BACK -> intArrayOf()
            RemoteButtonType.MUTE -> intArrayOf()
            RemoteButtonType.SETTINGS -> intArrayOf()
            RemoteButtonType.INPUT_SOURCE -> intArrayOf()
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

    private fun necToPulsePattern(addressByte: Int, commandByte: Int): Pair<Int, IntArray> {
        val carrierFrequencyHz = 38000

        val pulses = mutableListOf<Int>()

        // NEC Timings in microseconds
        val headerMark = 9000
        val headerSpace = 4500
        val bitMark = 560 // Mark is same for 0 and 1
        val zeroSpace = 560
        val oneSpace = 1690
        val stopBitMark = 560

        // 1. Add Header
        pulses.add(headerMark)
        pulses.add(headerSpace)

        // 2. Calculate Inverted Values
        val invertedAddressByte = addressByte.inv() and 0xFF // Ensure it's an 8-bit inversion
        val invertedCommandByte = commandByte.inv() and 0xFF

        // 3. Data bytes to process (LSB of each byte first)
        val dataBytes = listOf(addressByte, invertedAddressByte, commandByte, invertedCommandByte)

        // 4. Process each byte
        for (byteValue in dataBytes) {
            for (i in 0..7) { // Iterate 8 bits, LSB first
                val bit = (byteValue shr i) and 1 // Get i-th bit (0 if LSB)
                pulses.add(bitMark)
                if (bit == 1) {
                    pulses.add(oneSpace)
                } else {
                    pulses.add(zeroSpace)
                }
            }
        }

        // 5. Add Stop Bit / Final Mark
        pulses.add(stopBitMark)

        return Pair(carrierFrequencyHz, pulses.toIntArray())
    }
}