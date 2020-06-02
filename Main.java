
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main
{
    final static String VERSION = "MASTER";
    static SerialPort comPort;
    
    
    public static void main(String[] args) throws IOException 
    {
        //The line of information that will be displayed when the program initially starts.
        Log.info("WSR Frame Control v" + VERSION);

        String commPort = args[0];
        int tcpPort = Integer.parseInt(args[1]);
        
        //Delay in milliseconds.
        int delay = Integer.parseInt(args[2]);
        
        if(delay == null)
           delay = 20;
        Log.info("Using COM Port" + commPort + " & using tcp port" + 
                Integer.toString(tcpPort));
              
        Queue<DataFrame[]> queue = new ConcurrentLinkedQueue<>();
        
        //TODO: Make this threadsafe.
        OMCWBuilder omcw = new OMCWBuilder()
                    .setRegionSeparatorEnabled(true)
                    .setTopSolid(true)
                    .setBottomSolid(true)
                    .setTopPage(50)
                    .setLdlPage(1);
        
        Thread x = new Thread(new TCPDataReceiver(queue, omcw), "TCPReceiver");
        x.start();
        
        
        //Setup serial nonsense
        comPort = SerialPort.getCommPort(commPort);
        comPort.setComPortParameters(115200, 8, 1, 0);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 5, 0);
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
        
        while (true) { 
           if(queue.isEmpty())
           {
               DataFrame[] idleframe = new TODBuilder()
                .setOMCW(omcw.build())
                    .setTimeZone(2)
                    .setMonth(1)
                    .setDayOfMonth(1)
                    .setDayOfWeek(1)
                    .setHours(1)
                    .setMinutes(1)
                    .setSeconds(1)
                    .setPM(1)
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
                Thread.sleep(delay);
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
            Thread.sleep(delau);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
