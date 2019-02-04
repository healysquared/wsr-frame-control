/**
 * Class used to create an OMCW
 */
public class OMCW {
    //Declare variables for the integer representation of the constructors parameters.
    private int localProgram;
    private int localPreroll;
    private int auxAudio;
    private int wxWarning;
    private int radar;
    private int regionSeparator;
    private int topSolid;
    private int botSolid;

    private byte[] omcwBytes = new byte[6];
    //These will be the bytes that we're going to be manipulating. Cast them to the frame.
    private int b4, b5, b6, b7, b8, b9 = 0b0000; //Set these to 0. We'll be manipulating these with bitwise operators.

    /**
     * Constructor will generate a OMCW packet using the paramaters passed to it.
     *
     * @param localProgram    True enables local program. False disables.
     * @param localPreroll    True enables local program. False disables.
     * @param auxAudio        True enables an alternate audio stream. False disables.
     * @param wxWarning       True will close the 'WX WARNING' relay. False opens the relay.
     * @param radar           True will enable the local video feed for radar. False disables.
     * @param regionSeparator True will disable the LDL separator. False enables.
     * @param topSolid        True enables the background. False disables.
     * @param botSolid        True enables the background. False disables.
     * @param topPageNum      The page to display in the upper region.
     * @param ldlPage         THe page to display in the lower region.
     */
    public OMCW(boolean localProgram, boolean localPreroll, boolean auxAudio, boolean wxWarning,
                boolean radar, boolean regionSeparator, boolean topSolid, boolean botSolid, int topPageNum,
                int ldlPage) {
        //Byte 4
        //We're shifting these bits to the left, then using bitwise or to properly setup the nibbles.
        //If parameter is true, set variable to 1. Else, set it to 0. Then shift to the left by x for bitwise operations.
        //((parameter) ? 1 : 0) << x;
        this.localProgram = ((localProgram) ? 1 : 0) << 3;
        this.localPreroll = ((localPreroll) ? 1 : 0) << 2;
        this.auxAudio = ((auxAudio) ? 1 : 0) << 1;
        this.wxWarning = ((wxWarning) ? 1 : 0); //No need to shift as it's already at position 0.
        this.b4 = this.localProgram | this.localPreroll | this.auxAudio | this.wxWarning; //Generate the nibble.
        this.b4 = (byte) this.b4;

        //Byte 5
        this.radar = ((radar) ? 1 : 0) << 3;
        this.regionSeparator = ((regionSeparator) ? 1 : 0) << 2;
        this.topSolid = ((topSolid) ? 1 : 0) << 1;
        this.botSolid = ((botSolid) ? 1 : 0); //No need to shift as it's already at position 0.
        this.b5 = this.radar | this.regionSeparator | this.topSolid | this.botSolid; //Generate the nibble.
        this.b5 = (byte) this.b4;

        //Byte 6 (Top Page Number - Most significant four bits. No need for a topPageNumMSB variable.)
        this.b6 = (byte) (topPageNum & 0x3C); //0x3C = 111100... we're only wanting the upper four bits.

        //Byte 7 (Top Page Number - Least significant two bits AND Bottom Page Number)
        this.b7 = (byte) (((topPageNum & 0x03) << 2) | (ldlPage & 0x03)); //Get the two least significant bits of topPageNumber, shift by 2, then place LDL page number in the two least significant bits to create the nibble.

        //Byte 8
        //TODO: Is this a part of the OMCW...? Or should it be in a different class?
        this.b8 = (byte) (topPageNum & 0xF0); //Top Page Number MSB

        //Byte 9
        //TODO: Is this a part of the OMCW...? Or shold it be in a different class?
        this.b9 = (byte) (topPageNum & 0xF); //Low Page Number LSB

        //Assign the newly created OMCW bytes to the frame!
        omcwBytes[0] = (byte) this.b4;  // OMCW
        omcwBytes[1] = (byte) this.b5;  // "
        omcwBytes[2] = (byte) this.b6;  // "
        omcwBytes[3] = (byte) this.b7;  // "
        omcwBytes[4] = (byte) this.b8;  // Page number: 0 for TOD
        omcwBytes[5] = (byte) this.b9;  // "

    }

    public byte[] getOmcwBytes() {
        return omcwBytes;
    }
}
