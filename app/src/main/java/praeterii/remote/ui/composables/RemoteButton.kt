package praeterii.remote.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home // For preview
import androidx.compose.material.icons.filled.Send // For preview
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface // For wrapping previews with a theme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview // Import Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import praeterii.remote.R // Assuming this is your project's R file for drawables

@Composable
fun RemoteButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,      // For Material Icons
    painter: Painter? = null,       // NEW: For XML Drawables or other Painters
    @DrawableRes iconResId: Int? = null, // Alternative: Pass resource ID directly
    contentDescription: String,
    onClick: () -> Unit,
    buttonSize: Dp = 60.dp,
    iconSize: Dp = 24.dp,
    iconTintColor: Color = Color.Unspecified,
    fontSize: TextUnit = 16.sp,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = BorderStroke(
        1.dp,
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    )
) {
    // Determine which painter to use
    val finalPainter: Painter? = when {
        painter != null -> painter
        iconResId != null -> painterResource(id = iconResId)
        else -> null // If icon (ImageVector) is also null, no icon will be shown by this logic
    }

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(buttonSize),
        shape = shape,
        contentPadding = PaddingValues(0.dp), // Important for icon-only buttons
        colors = colors,
        border = if ((icon != null || finalPainter != null) && text == null && shape == CircleShape) null else border // Adjusted logic for cleaner circular icon-only buttons
    ) {
        if (text != null) {
            Text(text, fontSize = fontSize)
        }

        // Precedence: explicit painter, then iconResId, then ImageVector
        when {
            finalPainter != null -> {
                Image(
                    painter = finalPainter,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize),
                    colorFilter = if (iconTintColor != Color.Unspecified) ColorFilter.tint(iconTintColor) else null
                )
            }
            icon != null -> {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize),
                    tint = if (iconTintColor != Color.Unspecified) iconTintColor else LocalContentColor.current
                )
            }
        }
    }
}

// --- Previews ---

@Preview(name = "Icon Button (Outlined)", group = "RemoteButton")
@Composable
fun RemoteButtonPreview_IconOutlined() {
    MaterialTheme { // Wrap with MaterialTheme for proper defaults
        Surface { // Surface provides a background color from the theme
            RemoteButton(
                icon = Icons.Filled.Home,
                contentDescription = "Home",
                onClick = {}
            )
        }
    }
}

@Preview(name = "Text Button (Outlined)", group = "RemoteButton")
@Composable
fun RemoteButtonPreview_TextOutlined() {
    MaterialTheme {
        Surface {
            RemoteButton(
                text = "Settings",
                contentDescription = "Settings",
                onClick = {}
            )
        }
    }
}

@Preview(name = "Painter Button (XML Icon)", group = "RemoteButton")
@Composable
fun RemoteButtonPreview_PainterXml() {
    MaterialTheme {
        Surface {
            // Make sure you have 'ic_power_on_off' or similar in your drawables
            // If not, replace R.drawable.ic_power_on_off with a valid drawable or
            // use a placeholder if the specific drawable isn't crucial for the preview's purpose.
            // For this example, let's assume it exists.
            // If R.drawable.ic_power_on_off is not available, this preview might fail to render.
            // Consider adding a simple placeholder XML vector if needed for robust previews.
            RemoteButton(
                painter = painterResource(id = R.drawable.ic_power_on_off), // Replace if needed
                contentDescription = "Power From XML",
                onClick = {},
                iconTintColor = MaterialTheme.colorScheme.primary // Example tint
            )
        }
    }
}

@Preview(name = "IconResId Button (XML Icon)", group = "RemoteButton")
@Composable
fun RemoteButtonPreview_IconResIdXml() {
    MaterialTheme {
        Surface {
            RemoteButton(
                iconResId = R.drawable.baseline_volume_off_24, // Assuming you have this
                contentDescription = "Remote From XML ResId",
                onClick = {},
                iconTintColor = Color.Green
            )
        }
    }
}


@Preview(name = "Circular Icon Button", group = "RemoteButton")
@Composable
fun RemoteButtonPreview_CircularIcon() {
    MaterialTheme {
        Surface {
            RemoteButton(
                icon = Icons.Filled.ArrowBack,
                contentDescription = "Send",
                onClick = {},
                shape = CircleShape,
                buttonSize = 72.dp,
                iconSize = 32.dp,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

@Preview(name = "Custom Themed Button", group = "RemoteButton")
@Composable
fun RemoteButtonPreview_CustomTheme() {
    MaterialTheme {
        Surface {
            RemoteButton(
                text = "Custom",
                icon = Icons.Filled.Home,
                contentDescription = "Custom Button",
                onClick = {},
                buttonSize = 120.dp,
                iconSize = 30.dp,
                fontSize = 20.sp,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                iconTintColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}
