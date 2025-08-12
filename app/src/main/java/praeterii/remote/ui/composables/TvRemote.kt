package praeterii.remote.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import praeterii.remote.R
import praeterii.remote.utils.RemoteButtonType

@Composable
fun TvRemoteUi(
    onButtonClick: (RemoteButtonType) -> Unit,
    onNumberClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Distributes space
            verticalAlignment = Alignment.CenterVertically
        ) {
            // New "Settings" Button - Example
            RemoteButton(
                painter = painterResource(id = R.drawable.ic_input),
                onClick = { onButtonClick(RemoteButtonType.INPUT) },
                contentDescription = "Input",
                iconTintColor = MaterialTheme.colorScheme.primary,

                )

            // Power Button - Aligned to the End
            RemoteButton(
                painter = painterResource(id = R.drawable.ic_power_on_off),
                onClick = { onButtonClick(RemoteButtonType.POWER) },
                buttonSize = 64.dp,
                iconSize = 32.dp,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                contentDescription = "Power",
                iconTintColor = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // D-Pad
        DPad(onDpadClick = onButtonClick)

        Spacer(modifier = Modifier.height(24.dp))

        // Volume and Channel Controls
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RemoteButton(
                    painter = painterResource(id = R.drawable.outline_volume_up_24),
                    contentDescription = "Volume Up",
                    onClick = { onButtonClick(RemoteButtonType.VOL_UP) },
                    iconTintColor = MaterialTheme.colorScheme.primary,
                )
                Text("VOL", style = MaterialTheme.typography.labelSmall)
                RemoteButton(
                    painter = painterResource(id = R.drawable.outline_volume_down_24),
                    contentDescription = "Volume Down",
                    onClick = { onButtonClick(RemoteButtonType.VOL_DOWN) },
                    iconTintColor = MaterialTheme.colorScheme.primary,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RemoteButton(
                    icon = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Channel Up",
                    onClick = { onButtonClick(RemoteButtonType.CH_UP) })
                Text("CH", style = MaterialTheme.typography.labelSmall)
                RemoteButton(
                    icon = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Channel Down",
                    onClick = { onButtonClick(RemoteButtonType.CH_DOWN) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Other Controls (Mute, Home, Back)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            RemoteButton(
                painter = painterResource(id = R.drawable.baseline_volume_off_24),
                contentDescription = "Mute",
                onClick = { onButtonClick(RemoteButtonType.MUTE) },
                iconTintColor = MaterialTheme.colorScheme.primary,
            )
            RemoteButton(
                icon = Icons.Filled.Home,
                contentDescription = "Home",
                onClick = { onButtonClick(RemoteButtonType.HOME) })
            RemoteButton(
                icon = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                onClick = { onButtonClick(RemoteButtonType.BACK) })
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Number Pad
        NumberPad(onNumberClick = onNumberClick) // Pass the dedicated number click lambda
    }
}

@Composable
fun DPad(
    onDpadClick: (RemoteButtonType) -> Unit,
    modifier: Modifier = Modifier
) { // Changed to RemoteButtonType
    Box(modifier = modifier.size(200.dp), contentAlignment = Alignment.Center) {
        // Center "OK" Button
        RemoteButton(
            text = "OK",
            onClick = { onDpadClick(RemoteButtonType.OK) }, // Use Enum
            buttonSize = 70.dp,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.Center),
            contentDescription = "OK" // Added content description
        )

        // Directional Buttons
        RemoteButton( // Up
            icon = Icons.Filled.KeyboardArrowUp,
            contentDescription = "Up",
            onClick = { onDpadClick(RemoteButtonType.DPAD_UP) }, // Use Enum
            buttonSize = 55.dp,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        RemoteButton( // Down
            icon = Icons.Filled.KeyboardArrowDown,
            contentDescription = "Down",
            onClick = { onDpadClick(RemoteButtonType.DPAD_DOWN) }, // Use Enum
            buttonSize = 55.dp,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        RemoteButton( // Left
            icon = Icons.Filled.KeyboardArrowLeft,
            contentDescription = "Left",
            onClick = { onDpadClick(RemoteButtonType.DPAD_LEFT) }, // Use Enum
            buttonSize = 55.dp,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        RemoteButton( // Right
            icon = Icons.Filled.KeyboardArrowRight,
            contentDescription = "Right",
            onClick = { onDpadClick(RemoteButtonType.DPAD_RIGHT) }, // Use Enum
            buttonSize = 55.dp,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun NumberPad(
    onNumberClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) { // Stays as (Int) -> Unit
    val numbers = (1..9).toList() + listOf(0) // Numbers 1-9 and 0

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.width(200.dp), // Adjust width as needed
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(numbers) { number ->
            RemoteButton(
                text = number.toString(),
                onClick = { onNumberClick(number) }, // Directly pass the number
                buttonSize = 50.dp,
                fontSize = 18.sp,
                contentDescription = "Number $number", // Improved content description
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TvRemoteUiPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TvRemoteUi(
                onButtonClick = { buttonType ->
                    println("Button clicked: ${buttonType.name}")
                },
                onNumberClick = { number ->
                    println("Number clicked: $number")
                }
            )
        }
    }
}
