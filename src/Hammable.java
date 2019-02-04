/**
 * This class is used to create hammed bytes
 */
public class Hammable
{
   /**
    * @param value The nibble to ham.
    * @return The hammed byte, or the nibble if there is no need to ham it.
    */
   byte Hammable (byte value) {
      switch (value) {
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
         default:
            return value; //Return the nibble if 'value' isn't listed in the switch statement.
      }
   }
}