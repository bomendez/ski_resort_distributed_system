package ClientPart2;

import Utilities.RequestLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticsGenerator {

    public StatisticsGenerator() {}

    public void createCSV() {
        List<ApiPerformance> apiPerformanceList = RequestLog.getApiPerformanceList();
        String toWrite = getApiInCSVFormat(apiPerformanceList);
        writeToFile(toWrite,"ClientPart2" + String.valueOf(LocalDateTime.now()) + ".csv");
    }

    private String formatNode(ApiPerformance apiNode) {
        String apiString;
        String startTime;
        String requestType;
        String latency;
        String responseCode;

        startTime = "\"" + String.valueOf(apiNode.getStartTime()) + "\"";
        requestType = "\""+ apiNode.getRequestType() + "\"";
        latency = "\"" + String.valueOf(apiNode.getLatency()) + "\"";
        responseCode = "\"" + String.valueOf(apiNode.getResponseCode()) + "\"";

        apiString = String.join(",", Arrays.asList(startTime, requestType, latency, responseCode));
        return apiString;
    }

    private String buildCSVData(List<String> listOfRowData){
        StringBuilder sb = new StringBuilder();
        for(String todoItem: listOfRowData){
            sb.append(todoItem);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private String getApiInCSVFormat(List<ApiPerformance> listOfNodes){
        List<String> csvRowStrings = new ArrayList<>();
        for(ApiPerformance apiNode: listOfNodes) {
            csvRowStrings.add(formatNode(apiNode));
        }
        String result = buildCSVData(csvRowStrings);
        return result;
    }

    public static void writeToFile(String content, String path) {
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(file));
            bw.append("\"" + "startTime" + "\"");
            bw.append(",");
            bw.append("\"" + "requestTime" + "\"");
            bw.append(",");
            bw.append("\"" + "requestType" + "\"");
            bw.append(",");
            bw.append("\"" + "latency" + "\"");
            bw.append("\n");
            bw.write(content);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
