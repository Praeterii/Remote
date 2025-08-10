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

    // Numbers (can be handled differently if you need the specific number)
    // For simplicity, we can have a generic one if the action is the same,
    // or you could have NUM_0, NUM_1, etc. if needed.
    // Let's assume for now you want to pass the number separately for number pad.
    // If you need distinct enum entries for each number, you can add:
    // NUM_0, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9

    // Placeholder for other buttons you might add
    SETTINGS,
    INPUT_SOURCE,
    // Add more as needed
}
