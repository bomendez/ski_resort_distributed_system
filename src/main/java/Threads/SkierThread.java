package Threads;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import Utilities.RequestLog;

public class SkierThread implements Runnable {
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
    public static CountDownLatch completed;
    public static Integer skierID;
    public static Integer waitTime;
    public static LiftRide body;
    public static SkiersApi apiInstance;
    public final Integer resortID = 30;
    public final String seasonID = "20";
    public final String dayID = "10";
    public static int attempts = 0;
    public final int MAX_RETRY = 5;

    public static List<String> RESORTS = new ArrayList<>();
    public static List<String> SEASON = new ArrayList<>();

    public SkierThread(Integer id, ApiClient client, Integer skierIdStart, Integer skierIdStop,
                       Integer start, Integer end, Integer threadCount, Integer skierCount, Integer runCount,
                       double callCount, Integer liftCount, CountDownLatch latch) {
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
        completed = latch;
    }

    public void apiCall() {
        while(attempts < MAX_RETRY) {
            try {
                apiInstance.writeNewLiftRide(body, resortID, seasonID, dayID, skierID);
                RequestLog.logSuccess();
                break;
            } catch (ApiException e) {
                System.err.println("Exception when calling SkiersApi#writeNewLiftRide");
                e.printStackTrace();
                attempts++;
            }
        }
        if (attempts == 6) {
            RequestLog.logFailure();
        }
    }

    public void run() {



        skierID = ThreadLocalRandom.current().nextInt(skierIdBegin, skierIdEnd);
        waitTime = Integer.valueOf((int) Math.floor(Math.random()*(endTime - startTime + 1) + startTime));
        body = new LiftRide();
        body.setLiftID(numLifts);
        body.setTime(timeValue);
        body.setWaitTime(waitTime);

        apiInstance = new SkiersApi(apiClient);
        for (int i=0; i < numCalls; i++) {
            apiCall();
        }
        completed.countDown();
    }
}
