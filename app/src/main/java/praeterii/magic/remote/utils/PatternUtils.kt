package praeterii.magic.remote.utils

object PatternUtils {
    fun necToPulsePattern(addressByte: Int = 0x04, commandByte: Int): IntArray {
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

        return pulses.toIntArray()
    }

    /**
     * Generates an IR pulse pattern for a SIRC 20-bit command.
     *
     * @param command The 7-bit command code.
     * @param deviceAddress The 5-bit device address.
     * @param extendedAddress The 8-bit extended device address.
     * @param unitTimeUs The basic time unit (T) in microseconds (typically 600 µs).
     * @return IntArray suitable for irManager.transmit(), or null if inputs are invalid.
     */
    fun getSirc20Pattern(
        command: Int,          // 0-127
        deviceAddress: Int,    // 0-31
        extendedAddress: Int,  // 0-255
        unitTimeUs: Int = 600
    ): IntArray {
        val pulses = mutableListOf<Int>()

        val startBitHighUs = 4 * unitTimeUs
        val commonLowUs = 1 * unitTimeUs
        val logic0HighUs = 1 * unitTimeUs
        val logic1HighUs = 2 * unitTimeUs

        val numCommandBits = 7
        val numDeviceAddressBits = 5
        val numExtendedAddressBits = 8

        // 1. Start Bit
        pulses.add(startBitHighUs)
        pulses.add(commonLowUs)

        // Helper function to add bits for a given data field
        fun addDataBits(data: Int, numBits: Int) {
            var tempData = data
            for (i in 0 until numBits) {
                if ((tempData and 1) == 1) { // Check LSB
                    pulses.add(logic1HighUs)
                } else {
                    pulses.add(logic0HighUs)
                }
                pulses.add(commonLowUs) // Each high pulse is followed by a low pulse
                tempData = tempData shr 1 // Shift to process next bit
            }
        }

        // 2. Command Bits (7 bits, LSB first)
        addDataBits(command, numCommandBits)

        // 3. Device Address Bits (5 bits, LSB first)
        addDataBits(deviceAddress, numDeviceAddressBits)

        // 4. Extended Address Bits (8 bits, LSB first)
        addDataBits(extendedAddress, numExtendedAddressBits)

        // The SIRC protocol specifies the command is typically repeated if the total duration
        // is less than a frame time (e.g., 45ms). For a single transmission via irManager,
        // one full sequence is usually sent. The last commonLowUs makes the pattern have an even length.

        return pulses.toIntArray()
    }
}