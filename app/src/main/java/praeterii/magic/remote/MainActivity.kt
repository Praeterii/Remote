package praeterii.magic.remote

import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import praeterii.magic.remote.ui.composables.TvRemoteUi
import praeterii.magic.remote.ui.theme.RemoteTheme
import praeterii.magic.remote.utils.LgMagicRemoteCommandMapper
import praeterii.magic.remote.utils.RemoteButtonType

class MainActivity : ComponentActivity() {
    private val carrierFrequencyHz = 38000 // Common frequency
    private val patternMapper = LgMagicRemoteCommandMapper()
    private val irManager: ConsumerIrManager? by lazy {
        try {
            getSystemService(CONSUMER_IR_SERVICE) as? ConsumerIrManager
        } catch (e: Exception) {
            Log.e("MainActivity", "Could not initialize ConsumerIrManager", e)
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemoteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TvRemoteUi(
                        modifier = Modifier.padding(innerPadding),
                        onButtonClick = ::sendIrCommand,
                        onNumberClick = ::sendIrCommand
                    )
                }
            }
        }
    }

    private fun sendIrCommand(buttonType: RemoteButtonType) {
        val pattern: IntArray = patternMapper.getPattern(buttonType = buttonType)
        transmitIr(pattern = pattern, name = buttonType.name)
    }

    private fun sendIrCommand(number: Int) {
        val pattern: IntArray = patternMapper.getPattern(numberButton = number)
        transmitIr(pattern = pattern, name = number.toString())
    }

    private fun transmitIr(pattern: IntArray, name: String) {
        try {
            irManager!!.transmit(carrierFrequencyHz, pattern)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error transmitting IR signal for $name", e)
            Toast.makeText(this, R.string.no_ir_manager_error, Toast.LENGTH_SHORT).show()
        }
    }
}
