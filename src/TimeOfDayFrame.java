/**
 * Packet used to set the clock
 */
public class TimeOfDayFrame extends DataFrame {

    public TimeOfDayFrame(int timeZone) {
        // TODO: Should parameters be set in the constructor or should we use a builder class here?

        frame[0] = DataFrame.CLOCK_RUN_IN;
        frame[1] = DataFrame.CLOCK_RUN_IN;
        frame[2] = DataFrame.FRAMING_CODE;
        frame[3] = 0;  // Row number: 0
        frame[4] = 0;  // OMCW
        frame[5] = 0;  // "
        frame[6] = 0;  // "
        frame[7] = 0;  // "
        frame[8] = 0;  // Page number
        frame[9] = 0;  // "
        frame[10] = 0; // Timezone
        frame[11] = 0; // Day of Week
        frame[12] = 0; // Month
        frame[13] = 0; // Day of Month
        frame[14] = 0; // "
        frame[15] = 0; // Hours (0-12)
        frame[16] = 0; // Minutes
        frame[17] = 0; // "
        frame[18] = 0; // Seconds
        frame[19] = 0; // "
        frame[20] = 0; // AM/PM (0=AM; 1=PM)
        frame[21] = 0;
        frame[22] = 0;
        frame[23] = 0;
        frame[24] = 0;
        frame[25] = 0;
        frame[26] = 0;
        frame[27] = 0;
        frame[28] = 0;
        frame[29] = 0;
        frame[30] = 0;
        frame[31] = 0; // Checksum
        frame[32] = 0; // "
        frame[33] = 0; // OMCW Extension
        frame[34] = 0; // "
        frame[35] = 0; // Unused
        frame[36] = 0; // "

        // This packet contains a checksum, which is just indexes 3-30 summed

        int checksum = 0;
        for (int i = 3; i <= 30; i++) {
            checksum += frame[i];
        }

        // I think this is how you would split this up
        // https://stackoverflow.com/a/1735855/1385710
        frame[31] = (byte) (checksum & 0xFF);
        frame[32] = (byte) ((checksum >> 8) & 0xFF);

        // Convert bytes 3-36 to hamming
        hamBytes(3, 36);

    }
}
