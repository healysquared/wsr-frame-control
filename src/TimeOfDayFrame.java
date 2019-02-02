/**
 * Packet used to set the clock
 */
public class TimeOfDayFrame extends DataFrame {

    public TimeOfDayFrame(int timeZone) {
        // TODO: Should parameters be set in the constructor or should we use a builder class here?

        frame[0] = DataFrame.CLOCK_RUN_IN;
        frame[1] = DataFrame.CLOCK_RUN_IN;
        frame[2] = DataFrame.FRAMING_CODE;
        frame[3] = getHam(0); // Row number: 0
        // TODO: Fill in the rest of the packet
        frame[33] = getHam(0); // OMCW Extension
        frame[34] = getHam(0); // "
        frame[35] = getHam(0); // Unused
        frame[36] = getHam(0); // "

    }
}
