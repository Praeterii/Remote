package praeterii.remote.utils

enum class RemoteButtonType {
    // Power
    POWER,

    // D-Pad
    DPAD_UP,
    DPAD_DOWN,
    DPAD_LEFT,
    DPAD_RIGHT,
    OK,

    // Volume
    VOL_UP,
    VOL_DOWN,

    // Channel
    CH_UP,
    CH_DOWN,

    // Common Controls
    HOME,
    BACK,
    MUTE, // Or use a more descriptive name like TOGGLE_MUTE
}
