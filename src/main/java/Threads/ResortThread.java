package Threads;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.model.ResortIDSeasonsBody;
import java.util.concurrent.ThreadLocalRandom;

public class ResortThread implements Runnable {
    public static int threadId;
    public static ApiClient apiClient;
    public static Integer skierIdBegin;
    public static Integer skierIdEnd;
    public static Integer startTime;
    public static Integer endTime;
    public static Integer numThreads;
    public static Integer numSkiers;
    public static Integer numRuns;
    public static double numCalls;
    public static Integer numLifts;
    public static Integer timeValue;

    public ResortThread(Integer id, ApiClient client, Integer skierIdStart, Integer skierIdStop,
                        Integer start, Integer end, Integer threadCount, Integer skierCount, Integer runCount,
                        double callCount, Integer liftCount) {
        threadId = id;
        apiClient = client;
        skierIdBegin = skierIdStart;
        skierIdEnd = skierIdStop;
        startTime = start;
        endTime = end;
        numThreads = threadCount;
        numSkiers = skierCount;
        numRuns = runCount;
        numCalls = callCount;
        numLifts = ThreadLocalRandom.current().nextInt(liftCount);
        timeValue = ThreadLocalRandom.current().nextInt(start, end);
    }

    public void run() {
        Integer resortID = ThreadLocalRandom.current().nextInt(skierIdBegin, skierIdEnd);
        Integer waitTime = (int) Math.floor(Math.random()*(endTime - startTime + 1) + startTime);

        ResortsApi apiInstance = new ResortsApi(apiClient);
        ResortIDSeasonsBody body = new ResortIDSeasonsBody(); // ResortIDSeasonsBody | Specify new Season value
        body.setYear("1991");

        System.out.println("resort Id " + resortID);
        for (int i=0; i < numCalls; i++) {
            try {
                apiInstance.addSeason(body, resortID);
            } catch (ApiException e) {
                System.err.println("Exception when calling ResortsApi#addSeason");
                e.printStackTrace();
            }
        }
    }
}
