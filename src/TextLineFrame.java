public class TextLineFrame extends DataFrame {

    public TextLineFrame(int rowNumber, byte textSize, String text) {

        /*
        Prior Reference: https://patentimages.storage.googleapis.com/6d/b2/60/69fee298647dc3/US4725886.pdf
        Reference: https://patentimages.storage.googleapis.com/8d/f3/42/7f8952923cce48/US4916539.pdf
        */

        frame[0] = DataFrame.CLOCK_RUN_IN;
        frame[1] = DataFrame.CLOCK_RUN_IN;
        frame[2] = DataFrame.FRAMING_CODE;
        frame[3] = (byte) rowNumber;  // Row Number
        frame[4] = (byte) 0b0010;  // Height/Width
        // TODO: 4-36 are characters

        // Only 3 and 4 are hamming code here
        hamBytes(3, 4);
    }
}
