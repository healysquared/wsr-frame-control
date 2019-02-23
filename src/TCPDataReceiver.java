
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.stream.Collectors;

public class TCPDataReceiver implements Runnable {

    private final Queue<DataFrame[]> queue;
    private final OMCWBuilder omcw;

    public TCPDataReceiver(Queue<DataFrame[]> queue, OMCWBuilder omcw) {
        this.queue = queue;
        this.omcw = omcw;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6868);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Big brother is listening...");
        BufferedReader reader;
        Socket socket;

        while (true) {
            try {
                //Thread is blocked until accept new connection
                socket = serverSocket.accept();
                socket.setSoTimeout(1000);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String data = reader.lines().collect(Collectors.joining());
                processJSON(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processJSON(String data) {
        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(data).getAsJsonObject();
        String frameType = rootObj.getAsJsonPrimitive("frameType").getAsString();

        JsonObject jomcw = rootObj.getAsJsonObject("OMCW");
        OMCW locomcw;

        if (jomcw != null) {
            locomcw = new OMCWBuilder()
                    .setRegionSeparatorEnabled(jomcw.getAsJsonPrimitive("regionSeparator").getAsBoolean())
                    .setTopSolid(jomcw.getAsJsonPrimitive("topSolid").getAsBoolean())
                    .setBottomSolid(jomcw.getAsJsonPrimitive("bottomSolid").getAsBoolean())
                    .setTopPage(jomcw.getAsJsonPrimitive("topPage").getAsInt())
                    .setLdlPage(jomcw.getAsJsonPrimitive("ldlPage").getAsInt())
                    .build();
            omcw.setRegionSeparatorEnabled(jomcw.getAsJsonPrimitive("regionSeparator").getAsBoolean())
                    .setTopSolid(jomcw.getAsJsonPrimitive("topSolid").getAsBoolean())
                    .setBottomSolid(jomcw.getAsJsonPrimitive("bottomSolid").getAsBoolean())
                    .setTopPage(jomcw.getAsJsonPrimitive("topPage").getAsInt())
                    .setLdlPage(jomcw.getAsJsonPrimitive("ldlPage").getAsInt());
        } else {
            throw new NullPointerException("No OMCW in JSON!");
        }

        //Process page json
        if (frameType.equals("Page")) {
            JsonObject pageData = rootObj.getAsJsonObject("pageData");

            PageBuilder page = new PageBuilder(pageData.getAsJsonPrimitive("pageNumber").getAsInt());
            page.setOMCW(locomcw);

            if (pageData.has("pageAttributes")) {
                JsonObject pageAttr = pageData.getAsJsonObject("pageAttributes");
                page.setPageAttributes(new PageAttributes(pageAttr.getAsJsonPrimitive("freeze").getAsBoolean(),
                        pageAttr.getAsJsonPrimitive("advisory").getAsBoolean(),
                        pageAttr.getAsJsonPrimitive("warning").getAsBoolean(),
                        pageAttr.getAsJsonPrimitive("flip").getAsBoolean(),
                        pageAttr.getAsJsonPrimitive("roll").getAsBoolean(),
                        pageAttr.getAsJsonPrimitive("chain").getAsBoolean()));
            } else {
                page.setPageAttributes(new PageAttributes());
            }

            if (pageData.has("lineAttributes")) {
                JsonArray lineAttr = pageData.getAsJsonArray("lineAttributes");
                page.setLineAttributes(
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
            } else {
                page.setLineAttributes(
                        new TextLineAttributes(),
                        new TextLineAttributes(),
                        new TextLineAttributes(),
                        new TextLineAttributes(),
                        new TextLineAttributes(),
                        new TextLineAttributes(),
                        new TextLineAttributes(),
                        new TextLineAttributes()
                );
            }

            //Iterate through lines
            JsonArray lines = pageData.getAsJsonArray("lines");
            for (int i = 0; i < lines.size(); i++) {
                JsonObject row = lines.get(i).getAsJsonObject();
                int rowNumber = row.getAsJsonPrimitive("rowNumber").getAsInt();
                String textData = row.getAsJsonPrimitive("textData").getAsString();
                int height = row.getAsJsonPrimitive("height").getAsInt();
                int width = row.getAsJsonPrimitive("width").getAsInt();
                page.addLine(rowNumber, textData, height, width);
                System.out.println("Row:" + rowNumber);
            }
            queue.add(page.build());
        } else if (frameType.equals("tod")) {
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

}
