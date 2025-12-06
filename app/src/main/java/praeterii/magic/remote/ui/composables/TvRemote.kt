package praeterii.magic.remote.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import praeterii.magic.remote.ui.theme.RemoteTheme
import praeterii.magic.remote.utils.RemoteButtonType
import praeterii.magic.remote.R

@Composable
fun TvRemoteUi(
    modifier: Modifier = Modifier,
    onButtonClick: (RemoteButtonType) -> Unit,
    onNumberClick: (Int) -> Unit
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        TvRemoteUiLandscape(
            modifier = modifier,
            onButtonClick = onButtonClick,
            onNumberClick = onNumberClick
        )
    } else {
        TvRemoteUiPortrait(
            modifier = modifier,
            onButtonClick = onButtonClick,
            onNumberClick = onNumberClick
        )
    }
}

@Composable
private fun TvRemoteUiPortrait(
    modifier: Modifier = Modifier,
    onButtonClick: (RemoteButtonType) -> Unit,
    onNumberClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SystemControls(onButtonClick = onButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        DPad(onDpadClick = onButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        VolumeAndChannelControls(onButtonClick = onButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        OtherControls(onButtonClick = onButtonClick)
        Spacer(modifier = Modifier.height(8.dp))
        // Number and apps buttons
        Row(verticalAlignment = Alignment.CenterVertically) {
            NumberPad(onNumberClick = onNumberClick)
            Spacer(modifier = Modifier.width(24.dp))
            AppButtons(onButtonClick = onButtonClick)
        }
    }
}

@Composable
private fun TvRemoteUiLandscape(
    modifier: Modifier = Modifier,
    onButtonClick: (RemoteButtonType) -> Unit,
    onNumberClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column: DPad + System
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SystemControls(onButtonClick = onButtonClick)
            Spacer(modifier = Modifier.height(24.dp))
            DPad(onDpadClick = onButtonClick)
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Right Column: Vol/Ch, Other, Numbers/Apps
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            VolumeAndChannelControls(onButtonClick = onButtonClick)
            Spacer(modifier = Modifier.height(16.dp))
            OtherControls(onButtonClick = onButtonClick)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberPad(onNumberClick = onNumberClick)
                Spacer(modifier = Modifier.width(24.dp))
                AppButtons(onButtonClick = onButtonClick)
            }
        }
    }
}

@Composable
private fun OtherControls(
    onButtonClick: (RemoteButtonType) -> Unit
) {
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
}

@Composable
private fun VolumeAndChannelControls(
    onButtonClick: (RemoteButtonType) -> Unit
) {
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
}

@Composable
private fun SystemControls(
    onButtonClick: (RemoteButtonType) -> Unit,
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
}

@Composable
private fun DPad(
    onDpadClick: (RemoteButtonType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(200.dp), contentAlignment = Alignment.Center) {
        RemoteButton(
            text = "OK",
            onClick = { onDpadClick(RemoteButtonType.OK) }, // Use Enum
            buttonSize = 70.dp,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.Center),
            contentDescription = "OK" // Added content description
        )
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
private fun NumberPad(
    onNumberClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val numberRows = (1..9).chunked(3) + listOf(listOf(null, 0, null))

    Column(
        modifier = modifier.width(200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        numberRows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { number ->
                    if (number != null) {
                        RemoteButton(
                            text = number.toString(),
                            onClick = { onNumberClick(number) },
                            buttonSize = 50.dp,
                            fontSize = 18.sp,
                            contentDescription = "Number $number",
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun AppButtons(
    onButtonClick: (RemoteButtonType) -> Unit
) {
    Column {
        RemoteButton(
            text = "Disney",
            contentDescription = "Disney",
            buttonSize = 65.dp,
            onClick = { onButtonClick(RemoteButtonType.DISNEY) },
        )
        Spacer(modifier = Modifier.height(12.dp))
        RemoteButton(
            text = "Prime",
            contentDescription = "Prime",
            buttonSize = 65.dp,
            onClick = { onButtonClick(RemoteButtonType.AMAZON) },
        )
        Spacer(modifier = Modifier.height(12.dp))
        RemoteButton(
            text = "Netflix",
            contentDescription = "Netflix",
            buttonSize = 65.dp,
            onClick = { onButtonClick(RemoteButtonType.NETFLIX) },
        )
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Small phone", heightDp = 600)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Landscape", device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun TvRemoteUiPreview() {
    RemoteTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TvRemoteUi(
                onButtonClick = {},
                onNumberClick = {}
            )
        }
    }
}
