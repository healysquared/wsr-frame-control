public class PageHeaderFrame extends ControlFrame {

    public PageHeaderFrame(int pageNumber, int lineCount, OMCW omcw, Address address, PageAttributes attributes) {
        this.omcw = omcw;

        /*
        Prior Reference: https://patentimages.storage.googleapis.com/6d/b2/60/69fee298647dc3/US4725886.pdf
        Reference: https://patentimages.storage.googleapis.com/8d/f3/42/7f8952923cce48/US4916539.pdf
        */

        byte[] attributeBytes = attributes.buildAttributes();

        frame[0] = DataFrame.CLOCK_RUN_IN;
        frame[1] = DataFrame.CLOCK_RUN_IN;
        frame[2] = DataFrame.FRAMING_CODE;
        frame[3] = 0;  // Row Number: 0 for page header
        // 4-7 OMCW
        frame[8] = (byte) ((pageNumber >> 4) & 0x0F);   // Page Number
        frame[9] = (byte) (pageNumber & 0x0F);          // "
        frame[10] = 0; // Address
        frame[11] = 0; // "
        frame[12] = 0; // "
        frame[13] = 0; // "
        frame[14] = 0; // "
        frame[15] = 0; // "
        frame[16] = (byte) lineCount;   // Line Count
        frame[17] = attributeBytes[0];  // Page Attributes
        frame[18] = attributeBytes[1];  // "
        frame[19] = 0; // Line 1 Attributes
        frame[20] = 0; // "
        frame[21] = 0; // Line 2 Attributes
        frame[22] = 0; // "
        frame[23] = 0; // Line 3 Attributes
        frame[24] = 0; // "
        frame[25] = 0; // Line 4 Attributes
        frame[26] = 0; // "
        frame[27] = 0; // Line 5 Attributes
        frame[28] = 0; // "
        frame[29] = 0; // Line 6 Attributes
        frame[30] = 0; // "
        frame[31] = 0; // Line 7 Attributes
        frame[32] = 0; // "
        frame[33] = 0; // OMCW Extension
        frame[34] = 0; // "
        frame[35] = 0; // Line 8/9 Attributes
        frame[36] = 0; // "

        // Set bytes 4-7 for OMCW
        setOmcwBytes();

        // TODO: Set address

        // Convert bytes 3-36 to hamming code
        hamBytes(3, 36);

    }
}
