/**
 * This class contains the information necessary for Time of Day packet generation.
 */
public class tod {
    //The array.
    private byte[] todArray = new byte[36]; //Initialize an array that is capable of holding the entire TOD packet.

    //The bytes that will be used.
    final private byte b1 = (byte) 0xAA;    //Clock Run In
    final private byte b2 = (byte) 0xAA;    //Clock Run In
    final private byte b3 = (byte) 0xE4;    //Teletext Framing Code
    private byte b4 = (byte) 0x80;          //Row Number (Hammed binary 0000)

    //TODO: Everything below here that isn't already set requires the hamming function to be implemented.
    private byte b5;    //OMCW Bit 4
    private byte b6;    //OMCW Bit 6, 8
    private byte b7;    //OMCW 0
    private byte b8;    //OMCW Bit 15, 16
    private byte b9 = (byte) 0x80;    //Page Number Hi
    private byte b10 = (byte) 0x80;   //Page Number Lo
    private byte b11;   //Timezone
    private byte b12;   //Day of Week
    private byte b13;   //Month
    private byte b14;   //Day of Month Hi
    private byte b15;   //Day of Month Lo
    private byte b16;   //Hours (0-12)
    private byte b17;   //Minutes Hi
    private byte b18;   //Minutes Lo
    private byte b19;   //Seconds Hi
    private byte b20;   //Seconds Lo
    private byte b21;   //AM PM (0=AM; 1=PM)
    private byte b22;
    private byte b23;
    private byte b24;
    private byte b25;
    private byte b26;
    private byte b27;
    private byte b28;
    private byte b29;
    private byte b30;
    private byte b31;
    private byte b32;   //Checksum Hi
    private byte b33;   //Checksum Lo
    private byte b34 = (byte) 0x80;   //OMCW Extension
    private byte b35 = (byte) 0x80;   //OMCW Extension
    private byte b36 = (byte) 0x80;   //Unused
    private byte b37 = (byte) 0x80;   //Unused

    public byte[] generate() {
        //TODO
        //This function can be called to return a up to date, valid TOD packet...
        return todArray;
    }
}