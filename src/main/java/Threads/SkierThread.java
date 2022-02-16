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

    public void run() {
        Integer resortID = 30;
        String seasonID = "20";
        String dayID = "10";
        Integer skierID = ThreadLocalRandom.current().nextInt(skierIdBegin, skierIdEnd);
        Integer waitTime = Integer.valueOf((int) Math.floor(Math.random()*(endTime - startTime + 1) + startTime));
        LiftRide body = new LiftRide();
        body.setLiftID(numLifts);
        body.setTime(timeValue);
        body.setWaitTime(waitTime);

        SkiersApi apiInstance = new SkiersApi(apiClient);
        for (int i=0; i < numCalls; i++) {
        try {
            System.out.println("thread started " + Thread.currentThread().getId() + ":" + i);

            /**
             * This call throwing ApiException
             */
            apiInstance.writeNewLiftRide(body, resortID, seasonID, dayID, skierID);
            RequestLog.logSuccess();
        } catch (ApiException e) {
            System.err.println("Exception when calling SkiersApi#writeNewLiftRide");
            e.printStackTrace();
        }
        }
        completed.countDown();
    }
}
