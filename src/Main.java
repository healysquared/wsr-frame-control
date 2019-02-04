/**
 * This class controls the entire program. Can't do much without it. :~)
 */

public class Main {
    final static String VERSION = "1.0-DEV";

    public static void main(String[] args) {
        //The line of information that will be displayed when the program initially starts.
        Log.info("WeatherSTAR Jr Frame Control Program v" + VERSION +
                "\n(C) 2019 The WSRProject.\nThis project is in no way associated with IBM, The Weather Channel, or it's subsidiaries.");

        // TODO: Make a builder for OMCW?
        OMCW omcw = new OMCW(false, false, false, false, false,
                false, true, true, 1, 1);

        // TODO: Make a builder for PageAttributes?
        PageAttributes attr = new PageAttributes(false, false, false, false, false, true);

        // TODO: Make a builder for TextLine?
        TextLineAttributes textAttr = new TextLineAttributes(false, false, false, true, 0);

        // Example page built with PageBuilder
        DataFrame[] frames = new PageBuilder(1)
                .setAddress(new Address())
                .setOMCW(omcw)
                .setAttributes(attr)
                .addLine(1, "Conditions at Steamboat Springs", (byte) 0b0100, textAttr)
                .addLine(2, "Sunny")
                .addLine(3, "Temp: 199°F")
                .addLine(4, "Humidity: 100%   Dewpoint: 99°F")
                .addLine(5, "Barometric Pressure: 99.99 in.")
                .addLine(6, "Wind: SSW 199 MPH")
                .addLine(7, "Visib: 100 mi. Ceiling: 10000 ft")
                .addLine(8, "February Precipitation: 0.00 in")
                .build();

        // Just print out the frames for now
        for (DataFrame frame : frames) {
            System.out.println(frame.getFrameAsString());
        }

    }
}