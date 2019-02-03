/**
 * Class used to generate an address
 */
public class Address
{
   
   private int serviceID;
   private int zone;
   private int county;
   private int unit;

    public Address(int serviceID, int zone, int county, int unit)
    {
      serviceID = this.serviceID;
      zone = this.zone;
      county = this.county;
      unit = this.unit;
    }
    public Address()
    {
       //Default constructor. Zero out address if called.
       serviceID = 0;
       zone = 0;
       county = 0;
       unit = 0;
    }
}
