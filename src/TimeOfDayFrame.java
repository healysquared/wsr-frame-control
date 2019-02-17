/**
 * Packet used to set the clock
 */
import java.io.*;
public class TimeOfDayFrame extends ControlFrame {

    private int timeZone;
    private int dayOfWeek;
    private int month;
    private int dayOfMonth;
    private int hours;
    private int minutes;
    private int seconds;
    private int PM;
    public TimeOfDayFrame(OMCW omcw, int timeZone, int dayOfWeek, int month,
            int dayOfMonth, int hours, int minutes, int seconds, int PM) {
        this.omcw = omcw;
        this.timeZone = timeZone;
        this.dayOfWeek = dayOfWeek;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.PM = PM;
        /*
        Prior Reference: https://patentimages.storage.googleapis.com/6d/b2/60/69fee298647dc3/US4725886.pdf
        Reference: https://patentimages.storage.googleapis.com/8d/f3/42/7f8952923cce48/US4916539.pdf
        Note: Alternate time settings seem to be unused on most units. 
        Some units could have the "Alternate Time" Switch/Flag set, in which case only alternate time will work.
        */

        frame[0] = DataFrame.CLOCK_RUN_IN;
        frame[1] = DataFrame.CLOCK_RUN_IN;
        frame[2] = DataFrame.FRAMING_CODE;
        frame[3] = 0;  // Row Number: 0
        // 4-7 OMCW
        frame[8] = 0;  // Page Number: 0 for TOD
        frame[9] = 0;  // "
        frame[10] = (byte) (5); // Timezone
        frame[11] = (byte) (7); // Day of Week
        frame[12] = (byte) (2); // Month
        frame[13] = (byte) (0) ; // Day of Month
        frame[14] = (byte) (2); // "
        frame[15] = (byte) (3); // Hours (0-12)
        frame[16] = (byte) (1); // Minutes
        frame[17] = (byte) (9); // "
        frame[18] = (byte) (0); // Seconds
        frame[19] = (byte) (0); // "
        frame[20] = (byte) (1); // AM/PM (0=AM; 1=PM)
        frame[21] = 0; // Alt. Day of Week
        frame[22] = 0; // Alt. Month
        frame[23] = 0; // Alt. Day of Month
        frame[24] = 0; // "
        frame[25] = 0; // Alt. Hours
        frame[26] = 0; // Alt. Minutes
        frame[27] = 0; // "
        frame[28] = 0; // Alt. Seconds
        frame[29] = 0; // "
        frame[30] = 0; // Alt. AM/PM
        frame[31] = 0; // Checksum
        frame[32] = 0; // "
        frame[33] = 0; // OMCW Extension
        frame[34] = 0; // "
        frame[35] = 0; // Spare
        frame[36] = 0; // Spare

        // Set bytes 4-7 for OMCW
        setOmcwBytes();

        // This packet contains a checksum, which is just indexes 10-30 summed
        byte checksum = 0;
        for (int i = 10; i <= 30; i++) {
            checksum += frame[i];
            System.out.println(i + ": " + frame[i] + ": "+ checksum);
        }
        System.out.println(checksum);
        // Split the checksum into separate nibbles
        frame[31] = (byte) ((checksum >> 4) & 0x0F); // High byte
        frame[32] = (byte) (checksum & 0x0F); // Low byte

        // Convert bytes 3-36 to hamming code
        hamBytes(3, 36);

    }
}
