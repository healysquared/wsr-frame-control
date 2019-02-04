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
        Hammable getHam = new Hammable();
        for (int i = start; i <= end; i++) {
            frame[i] = getHam.Hammable(frame[i]);
        }
    }
}
