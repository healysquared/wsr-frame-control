
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
                data =  reader.lines().collect(Collectors.joining());
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
                    omcw = locomcw;
                }
                else
                {
                    throw new NullPointerException("No OMCW in JSON!");
                }
                
                //Process page json
                if(frameType.equals("Page"))
                {
                    JsonObject pageData = rootObj.getAsJsonObject("pageData");
                    JsonObject pageAttr = pageData.getAsJsonObject("pageAttributes");
                    JsonArray lineAttr = pageData.getAsJsonArray("lineAttributes");
                    
                    PageBuilder page = new PageBuilder(50);
                    page.setOMCW(locomcw);
                    page.setAttributes(
                            new PageAttributes(pageAttr.getAsJsonPrimitive("freeze").getAsBoolean(), 
                                    pageAttr.getAsJsonPrimitive("advisory").getAsBoolean(),
                                    pageAttr.getAsJsonPrimitive("warning").getAsBoolean(),
                                    pageAttr.getAsJsonPrimitive("flip").getAsBoolean(),
                                    pageAttr.getAsJsonPrimitive("roll").getAsBoolean(), 
                                    pageAttr.getAsJsonPrimitive("chain").getAsBoolean()),
                            new TextLineAttributes(lineAttr.get(0).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(0).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(0).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(0).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(0).getAsJsonObject().getAsJsonPrimitive("color").getAsInt()),
                            new TextLineAttributes(lineAttr.get(1).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(1).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(1).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(1).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(1).getAsJsonObject().getAsJsonPrimitive("color").getAsInt()),
                            new TextLineAttributes(lineAttr.get(2).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(2).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(2).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(2).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(2).getAsJsonObject().getAsJsonPrimitive("color").getAsInt()),
                            new TextLineAttributes(lineAttr.get(3).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(3).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(3).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(3).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(3).getAsJsonObject().getAsJsonPrimitive("color").getAsInt()),
                            new TextLineAttributes(lineAttr.get(4).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(4).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(4).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(4).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(4).getAsJsonObject().getAsJsonPrimitive("color").getAsInt()),
                            new TextLineAttributes(lineAttr.get(5).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(5).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(5).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(5).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(5).getAsJsonObject().getAsJsonPrimitive("color").getAsInt()),
                            new TextLineAttributes(lineAttr.get(6).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(6).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(6).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(6).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(6).getAsJsonObject().getAsJsonPrimitive("color").getAsInt()),
                            new TextLineAttributes(lineAttr.get(7).getAsJsonObject().getAsJsonPrimitive("seperator").getAsBoolean(),
                                    lineAttr.get(7).getAsJsonObject().getAsJsonPrimitive("flash").getAsBoolean(),
                                    lineAttr.get(7).getAsJsonObject().getAsJsonPrimitive("reverse").getAsBoolean(),
                                    lineAttr.get(7).getAsJsonObject().getAsJsonPrimitive("border").getAsBoolean(),
                                    lineAttr.get(7).getAsJsonObject().getAsJsonPrimitive("color").getAsInt())
                            );
                    
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
                    queue.add(page.build());
                }
                else if(frameType.equals("tod"))
                {
                    JsonObject todData = rootObj.getAsJsonObject("todData");
                    int timezone = todData.getAsJsonPrimitive("timezone").getAsInt();
                    int month = todData.getAsJsonPrimitive("month").getAsInt();
                    int dayOfMonth = todData.getAsJsonPrimitive("dayOfMonth").getAsInt();
                    int dayOfWeek = todData.getAsJsonPrimitive("dayOfWeek").getAsInt();
                    int hours = todData.getAsJsonPrimitive("hours").getAsInt();
                    int minutes = todData.getAsJsonPrimitive("minutes").getAsInt();
                    int seconds = todData.getAsJsonPrimitive("seconds").getAsInt();
                    int PM = todData.getAsJsonPrimitive("PM").getAsInt();
                    
                    TODBuilder todbuild = new TODBuilder();
                    todbuild.setOMCW(locomcw)
                            .setTimeZone(timezone)
                            .setMonth(month)
                            .setDayOfMonth(dayOfMonth)
                            .setDayOfWeek(dayOfWeek)
                            .setHours(hours)
                            .setMinutes(minutes)
                            .setSeconds(seconds)
                            .setPM(PM);
                    queue.add(todbuild.build());
                }
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
