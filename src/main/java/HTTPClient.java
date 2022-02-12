import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.*;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class HTTPClient {
    private static String url = "http://localhost:8080/assignment1_war/skiers/";
    private static int count = 0;
    private static int SKIER_ID = 1;
    private static List<String> RESORTS = new ArrayList<>();
    private static List<String> SEASON = new ArrayList<>();
    private static int numThreads;
    private static int numSkiers;
    private static int numLifts = 40;
    private static int numRuns = 10;
    private static String ipAddress;
    private static int SKI_DAY_MINS = 420;

    public HTTPClient() throws IllegalArgumentException {
    }

    private void setNumThreads(int threads) {
        if (threads > 1024) {throw new IllegalArgumentException();}
        this.numThreads = threads;
    }

    private void setNumSkiers(int skiers) {
        if (skiers > 100000) {throw new IllegalArgumentException();}
        this.numSkiers = skiers;
    }

    private void setSkiLifts(int skiLifts) {
        if (skiLifts < 5 || skiLifts > 60) {throw new IllegalArgumentException();}
        this.numLifts = skiLifts;
    }

    private void setNumRuns(int runs) {
        if (runs > 20) {throw new IllegalArgumentException();}
        this.numRuns = runs;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    synchronized public void inc() {
        count++;
    }

    public int getVal() {
        return this.count;
    }

    public static void main(String[] args) throws InterruptedException {
//        ApiClient apiClient = new ApiClient();
//        apiClient.setBasePath("http://localhost:8080/assignment1_war/");


//        SkiersApi apiInstance = new SkiersApi(apiClient);
//        try {
//            RESORTS.add("1");
//            SEASON.add("2");
//            SkierVertical result = apiInstance.getSkierResortTotals(SKIER_ID, RESORTS, SEASON);
//            System.out.println(result);
//        } catch (ApiException e) {
//            System.err.println("Exception when calling ResortsApi#getResorts");
//            e.printStackTrace();
//        }



//        if (args.length <= 1) {throw new IllegalArgumentException("no arguments provided");}

        final HTTPClient counter = new HTTPClient();
        CountDownLatch  completed = new CountDownLatch(numThreads);

        final long start = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            // lambda runnable creation - interface only has a single method so lambda works fine
            SkierThread skierThread = new SkierThread(counter, completed, url);
            new Thread(skierThread).start();
        }

        final long end = System.currentTimeMillis();
        System.out.printf("duration:%s%n milliseconds", end - start);
        completed.await();
        System.out.println("Value should be equal to " + numThreads + " It is: " + counter.getVal());


    }
}
