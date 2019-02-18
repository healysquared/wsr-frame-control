
import com.google.gson.*;
import java.net.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.*;

public class TCPDataReceiver implements Runnable
{

    private Queue<DataFrame[]> queue;
    private OMCW omcw;

    public TCPDataReceiver(Queue<DataFrame[]> queue, OMCW omcw)
    {
        this.queue = queue;
        this.omcw = omcw;
    }

    @Override
    public void run()
    {
        String data;
        ServerSocket serverSocket = null;
        try
        {

            serverSocket = new ServerSocket(6868);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("Big brother is listening...");
        while (true)
        {
            try
            {
                //Thread is blocked until accept new connection
                Socket socket = serverSocket.accept();
                //Especially for read timeouts, in case comm failure.
                socket.setSoTimeout(1000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                data = reader.readLine();
                System.out.println("Received: " + data);
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(data).getAsJsonObject();
                String frameType = rootObj.getAsJsonPrimitive("frameType").getAsString();
                
                JsonObject jomcw = rootObj.getAsJsonObject("OMCW");
                OMCW locomcw = null;
                
                if(jomcw != null)
                {
                    locomcw = new OMCWBuilder()
                            .setRegionSeparatorEnabled(jomcw.getAsJsonPrimitive("regionSeparator").getAsBoolean())
                            .setTopSolid(jomcw.getAsJsonPrimitive("topSolid").getAsBoolean())
                            .setBottomSolid(jomcw.getAsJsonPrimitive("bottomSolid").getAsBoolean())
                            .setTopPage(jomcw.getAsJsonPrimitive("topPage").getAsInt())
                            .setLdlPage(jomcw.getAsJsonPrimitive("ldlPage").getAsInt())
                            .build();
                }
                else
                {
                    throw new NullPointerException("No OMCW in JSON!");
                }
                
                //Process page json
                if( frameType.equals("Page"))
                {
                    PageBuilder page = new PageBuilder(50);
                    page.setOMCW(locomcw);
                    page.setAttributes(
                            new PageAttributes(false, false, false, false, false, false),
                            new TextLineAttributes(false,true,false,true,0),
                            new TextLineAttributes(false,true,false,false,1),
                            new TextLineAttributes(false,true,false,true,2),
                            new TextLineAttributes(false,true,false,true,3),
                            new TextLineAttributes(false,true,false,true,4),
                            new TextLineAttributes(false,true,false,true,5),
                            new TextLineAttributes(false,true,false,true,6),
                            new TextLineAttributes(false,true,false,true,7)
                            );
                    
                    JsonObject pageData = rootObj.getAsJsonObject("pageData");
                    int lineCount = pageData.getAsJsonPrimitive("lineCount").getAsInt();

                    System.out.println("frameType:" + frameType + " lineCount:" + lineCount);
                    
                    //Iterate through lines
                    JsonArray lines = pageData.getAsJsonArray("lines");
                    for (int i = 0; i < lineCount; i++)
                    {
                        JsonObject row = lines.get(i).getAsJsonObject();
                        int rowNumber = row.getAsJsonPrimitive("rowNumber").getAsInt();
                        String textData = row.getAsJsonPrimitive("textData").getAsString();
                        int height = row.getAsJsonPrimitive("height").getAsInt();
                        int width = row.getAsJsonPrimitive("width").getAsInt();
                        page.addLine(rowNumber, textData, height, width);
                        System.out.println("Row:" + rowNumber);
                    }
                    queue.offer(page.build());
                }
                else if(frameType.equals("tod"))
                {
                    ;
                }
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
