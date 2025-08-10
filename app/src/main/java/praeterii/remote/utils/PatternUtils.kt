package praeterii.remote.utils

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
     * Generates an IR pulse pattern for the SIRC protocol.
     *
     * @param command The command value (e.g., 0-127 for 7-bit commands).
     * @param address The address value (e.g., 0-8191 for 13-bit SIRC-20 address).
     * @param totalDataBits The total number of data bits (command + address).
     *                      Common values: 12 (SIRC-12), 15 (SIRC-15), 20 (SIRC-20).
     * @param numCommandBits The number of bits used for the command (typically 7).
     * @return Pair of Carrier Frequency (Hz) and the IntArray pulse pattern.
     */
    fun sircToPulsePattern(
        command: Int,
        address: Int,
        totalDataBits: Int, // e.g., 12, 15, or 20
        numCommandBits: Int = 7 // Standard SIRC uses 7 bits for command
    ): Pair<Int, IntArray> {
        val carrierFrequencyHz = 40000 // Common for SIRC
        val pulses = mutableListOf<Int>()

        // SIRC Timings in microseconds (T = 600µs)
        val unitTimeUs = 600
        val startMarkUs = 4 * unitTimeUs // 2400 µs
        val startSpaceUs = 1 * unitTimeUs  // 600 µs
        val oneMarkUs = 2 * unitTimeUs   // 1200 µs
        val oneSpaceUs = 1 * unitTimeUs  // 600 µs
        val zeroMarkUs = 1 * unitTimeUs  // 600 µs
        val zeroSpaceUs = 1 * unitTimeUs // 600 µs

        // 1. Add Start Bit
        pulses.add(startMarkUs)
        pulses.add(startSpaceUs)

        // 2. Add Command Bits (LSB first)
        for (i in 0 until numCommandBits) {
            val bit = (command shr i) and 1
            if (bit == 1) {
                pulses.add(oneMarkUs)
                pulses.add(oneSpaceUs)
            } else {
                pulses.add(zeroMarkUs)
                pulses.add(zeroSpaceUs)
            }
        }

        // 3. Add Address Bits (LSB first)
        val numAddressBits = totalDataBits - numCommandBits
        if (numAddressBits < 0) {
            throw IllegalArgumentException("totalDataBits cannot be less than numCommandBits")
        }
        for (i in 0 until numAddressBits) {
            val bit = (address shr i) and 1
            if (bit == 1) {
                pulses.add(oneMarkUs)
                pulses.add(oneSpaceUs)
            } else {
                pulses.add(zeroMarkUs)
                pulses.add(zeroSpaceUs)
            }
        }

        return Pair(carrierFrequencyHz, pulses.toIntArray())
    }

    // Example Usage for SIRC-20 (7 command bits, 13 address bits):
    fun main() { // Or inside your Android Composable/ViewModel
        val tvPowerCommand = 0x15 // Example Sony TV Power command (21 decimal) - Verify this!
        val tvAddress = 0x0001    // Example Sony TV Address (1 decimal) - Verify this!

        // For SIRC-20 (7 command bits + 13 address bits = 20 total data bits)
        val (frequencySirc20, patternSirc20) = sircToPulsePattern(
            command = tvPowerCommand,
            address = tvAddress,
            totalDataBits = 20, // 7 for command + 13 for address
            numCommandBits = 7
        )
        println("SIRC-20:")
        println("  Carrier Frequency: $frequencySirc20 Hz")
        println("  Pulse Pattern: ${patternSirc20.joinToString(",")}")
        println("  Pattern Length (elements): ${patternSirc20.size}") // Should be 2 (start) + 2*20 (data) = 42

        // For SIRC-12 (7 command bits + 5 address bits = 12 total data bits)
        // val someDeviceCommand = 0x0A
        // val someDeviceAddressSirc12 = 0x03 // Max 0x1F for 5 bits
        // val (frequencySirc12, patternSirc12) = sircToPulsePattern(
        //     command = someDeviceCommand,
        //     address = someDeviceAddressSirc12,
        //     totalDataBits = 12,
        //     numCommandBits = 7
        // )
        // println("\nSIRC-12:")
        // println("  Carrier Frequency: $frequencySirc12 Hz")
        // println("  Pulse Pattern: ${patternSirc12.joinToString(",")}")
        // println("  Pattern Length (elements): ${patternSirc12.size}") // Should be 2 (start) + 2*12 (data) = 26


        // Now you can use 'frequency' and 'pattern' with irManager.transmit()
        // In Android:
        // if (hasIrEmitter) {
        //     try {
        //         irManager.transmit(frequencySirc20, patternSirc20)
        //         // SIRC often requires sending the command multiple times
        //         // Thread.sleep(40) // Example delay, adjust based on typical SIRC inter-message gap
        //         // irManager.transmit(frequencySirc20, patternSirc20)
        //         // Thread.sleep(40)
        //         // irManager.transmit(frequencySirc20, patternSirc20)
        //     } catch (e: Exception) {
        //         Log.e("IRTransmit", "Error transmitting SIRC", e)
        //     }
        // }
    }
}