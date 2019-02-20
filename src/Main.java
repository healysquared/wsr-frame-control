
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class controls the entire program. Can't do much without it. :~)
 */

public class Main
{
    final static String VERSION = "1.0-DEV";
    static SerialPort comPort;
    
    
    public static void main(String[] args) throws IOException 
    {
        //The line of information that will be displayed when the program initially starts.
        Log.info("WeatherSTAR Jr Frame Control Program v" + VERSION +
                "\n(C) 2019 The WSRProject.\nThis project is in no way associated with IBM, The Weather Channel, or it's subsidiaries.");

        String commPort = args[0];
        int tcpPort = Integer.parseInt(args[1]);
        Log.info("Using COM Port" + commPort + " & using tcp port" + 
                Integer.toString(tcpPort));
              
        Queue<DataFrame[]> queue = new ConcurrentLinkedQueue<>();
        
        //TODO: Make this threadsafe.
        OMCW omcw = null;
        Thread x = new Thread(new TCPDataReceiver(queue, omcw), "TCPReceiver");
        x.start();
        
        
        //Setup serial nonsense
        comPort = SerialPort.getCommPort(commPort);
        comPort.setComPortParameters(115200, 8, 1, 0);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                {
                    Log.error("Serial port failure");
                    return;
                }
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                System.out.print(new String(newData, StandardCharsets.US_ASCII));
            }
        });

        if (comPort.openPort())
        {
            Log.info("Serial port is open");
        }
        else 
        {
            Log.error("Serial port failed to open");
            return;
        }

        omcw = new OMCWBuilder()
                    .setRegionSeparatorEnabled(true)
                    .setTopSolid(true)
                    .setBottomSolid(true)
                    .setTopPage(50)
                    .setLdlPage(1)
                    .build();
        
//        DataFrame[] tod = new TODBuilder()
//                .setOMCW(omcw2)
//                .setTimeZone(5)
//                .setMonth(5)
//                .setDayOfMonth(16)
//                .setDayOfWeek(3)
//                .setHours(5)
//                .setMinutes(20)
//                .setSeconds(00)
//                .setPM(0)
//                .build();  
        while (true) { 
           if(queue.isEmpty() && omcw != null)
           {
               //Idle. Queue is empty.
           DataFrame[] idleframe = new PageBuilder(129)
                    .setOMCW(omcw)
                    .setPageAttributes(new PageAttributes(false, false, false, false, false, false))
                    .setLineAttributes(
                            new TextLineAttributes(false,true,false,true,0),
                            new TextLineAttributes(false,true,false,false,1),
                            new TextLineAttributes(false,true,false,true,2),
                            new TextLineAttributes(false,true,false,true,3),
                            new TextLineAttributes(false,true,false,true,4),
                            new TextLineAttributes(false,true,false,true,5),
                            new TextLineAttributes(false,true,false,true,6),
                            new TextLineAttributes(false,true,false,true,7)
                            )
                            .addLine(1, "", 1, 0)
                    .build(); 
                    sendFrames(idleframe);
           }
           else if(!queue.isEmpty())
           {
            sendFrames(queue.poll());
           }

        }
    }

    static void sendFrames(DataFrame[] frames) 
    {
        for (DataFrame frame : frames) 
        {
            byte[] frameBytes = frame.getFrame();
            System.out.println("Writing " + frameBytes.length + " bytes: " + frame.getFrameAsString());
            comPort.writeBytes(frameBytes, frameBytes.length, 0);
            comPort.readBytes(new byte[1], 1);
            try 
            {
                Thread.sleep(80);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println(e);
            }
        }
    }
    static void sendFrame(DataFrame frame) {
        byte[] frameBytes = frame.getFrame();
        System.out.println("Writing " + frameBytes.length + " bytes: " + frame.getFrameAsString());
        comPort.writeBytes(frameBytes, frameBytes.length, 0);
        comPort.readBytes(new byte[1], 1);
        try {
            Thread.sleep(80);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
