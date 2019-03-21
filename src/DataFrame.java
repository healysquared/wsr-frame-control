/**
 * Base packet class, all packet types should extend this
 */
public class DataFrame {

    static final byte CLOCK_RUN_IN = (byte) 0xAA;
    static final byte FRAMING_CODE = (byte) 0xE4;

    byte[] frame = new byte[37];

    public byte[] getFrame()
    {
        //TODO: Should this be moved?
        //Set odd parity on all bytes in frame.
        byte[] frameOddParity = new byte[37];
        for(int i = 3; i < 37; i++)
        {
            frameOddParity[i] = calculateOddParity(frame[i]);
        }
        
        return frameOddParity;
    }

    /**
     * @return A string of HEX values for debugging purposes
     */
    public String getFrameAsString() {
        StringBuilder out = new StringBuilder();
        for (byte i : frame) {
            out.append(String.format("%02X ", i & 0xFF));
        }
        return out.toString();
    }

    /**
     * Convert an index range of the frame to hamming code bytes
     *
     * @param start Start index
     * @param end   Ending index
     */
    public void hamBytes(int start, int end) {
        Hammable getHam = new Hammable();
        for (int i = start; i <= end; i++) {
            frame[i] = getHam.Hammable(frame[i]);
        }
    }
    byte calculateOddParity(byte input) {

        // Degrees symbol is mapped to a different location
        if (input == 0x3F) {
            input = 0x5C;
        }

        // We need to find the sum of all the 1 bits in this byte
        int count = 0;
        byte shifted = input;

        for (int i = 0; i <= 8; i++) {
            if ((shifted & 1) == 1) {
                count++;
            }
            shifted = (byte) (shifted >> 1); // shift at the end so we can count the first bit
        }

        if (count % 2 == 1) {
            // Even number of 1 bits, set the first bit to 0
            input &= ~(1 << 7);
        } else {
            // Odd number of 1 bits, set the first bit to 1
            input |= 1 << 7;
        }

        return input;
    }
}
