import java.nio.charset.StandardCharsets;

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
        frame[4] = textSize;  // Height/Width

        // fill the array with spaces
        for (int i = 5; i < frame.length; i++) {
            frame[i] = 0x7F;
        }

        // Convert the string to odd-parity ASCII and append it to the frame
        byte[] textBytes = text.getBytes(StandardCharsets.US_ASCII);
        int startIndex = 5;
        for (byte i : textBytes) {
            if (startIndex >= frame.length) continue; // Text lines have a max of 32 characters
            frame[startIndex] = calculateOddParity(i);
            startIndex++;
        }

        // Only 3 and 4 are hamming code here
        hamBytes(3, 4);
    }

    byte calculateOddParity(String input) {
        return calculateOddParity(input.getBytes(StandardCharsets.US_ASCII)[0]);
    }

    byte calculateOddParity(byte input) {

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
