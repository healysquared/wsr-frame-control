import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.nio.charset.StandardCharsets;

/**
 * This class controls the entire program. Can't do much without it. :~)
 */

public class Main {
    final static String VERSION = "1.0-DEV";

    public static void main(String[] args) {
        //The line of information that will be displayed when the program initially starts.
        Log.info("WeatherSTAR Jr Frame Control Program v" + VERSION +
                "\n(C) 2019 The WSRProject.\nThis project is in no way associated with IBM, The Weather Channel, or it's subsidiaries.");

        SerialPort comPort = SerialPort.getCommPort("COM5");
        comPort.setComPortParameters(115200, 8, 1, 0);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                System.out.print(new String(newData, StandardCharsets.US_ASCII));
            }
        });

        if (comPort.openPort()) {
            Log.info("Serial port is open");
        } else {
            Log.error("Serial port failed to open");
            return;
        }

        // TODO: Make a builder for OMCW?
        OMCW omcw = new OMCW(false, false, false, false, false,
                true, true, true, 1, 2);

        // TODO: Make a builder for PageAttributes?
        PageAttributes attr = new PageAttributes(false, false, false, false, false, true);

        // TODO: Make a builder for TextLine?
        TextLineAttributes textAttr = new TextLineAttributes(false, false, false, true, 0);

        // Test sending an OMCW within a header, this works and shows page 1 and the local crawl
        while (true) {
            PageHeaderFrame frame = new PageHeaderFrame(1, 1, omcw, new Address(), attr);
            byte[] frameBytes = frame.getFrame();
            System.out.println("Writing " + frameBytes.length + " bytes: " + frame.getFrameAsString());
            comPort.writeBytes(frameBytes, frameBytes.length, 0);
            comPort.readBytes(new byte[1], 1);
            try {
                Thread.sleep(80);
            } catch (Exception e) {
            }
        }

        /*
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

        for (DataFrame frame : frames) {
            byte[] frameBytes = frame.getFrame();
            System.out.println("Writing " + frameBytes.length + " bytes: " + frame.getFrameAsString());
            comPort.writeBytes(frameBytes, frameBytes.length, 0);
            comPort.readBytes(new byte[1], 1);
            try {
                System.in.read();
            } catch (Exception e) {
            }
        }
        */
    }
}