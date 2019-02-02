/**
 * Base packet class, all packet types should extend this
 */
public class DataFrame {

    static final byte CLOCK_RUN_IN = (byte) 0xAA;
    static final byte FRAMING_CODE = (byte) 0xE4;

    byte[] frame = new byte[36];

    public byte[] getFrame() {
        return frame;
    }

    /**
     * Convert an index range of the frame to hamming code bytes
     *
     * @param start Start index
     * @param end   Ending index
     */
    public void hamBytes(int start, int end) {
        for (int i = start; i <= end; i++) {
            frame[i] = getHam(frame[i]);
        }
    }

    static byte getHam(int input) {

        switch (input) {
            case 0:
                return (byte) 0x80;
            case 1:
                return (byte) 0x31;
            case 2:
                return (byte) 0x52;
            case 3:
                return (byte) 0xE3;
            case 4:
                return (byte) 0x64;
            case 5:
                return (byte) 0xD5;
            case 6:
                return (byte) 0xB6;
            case 7:
                return (byte) 0x7;
            case 8:
                return (byte) 0xF8;
            case 9:
                return (byte) 0x49;
            case 10:
                return (byte) 0x2A;
            case 11:
                return (byte) 0x9B;
            case 12:
                return (byte) 0x1C;
            case 13:
                return (byte) 0xAD;
            case 14:
                return (byte) 0xCE;
            case 15:
                return (byte) 0x7F;
        }

        return (byte) 0x80;
    }

}
