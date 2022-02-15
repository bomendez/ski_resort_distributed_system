package Threads;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.SkierVertical;
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

public class SkierThread implements Runnable {
    public static ApiClient apiClient;
    public static Integer skierID;
    public static List<String> RESORTS = new ArrayList<>();
    public static List<String> SEASON = new ArrayList<>();

//    public Threads.SkierThread(HTTPClient client, CountDownLatch latch, String urlParam) {
//        this.counter = client;
//        this.completed = latch;
//        this.url = urlParam;
//    }

    public SkierThread(ApiClient client, Integer numSkiers) {
        apiClient = client;
        skierID = numSkiers;
    }

    public void run() {
        SkiersApi apiInstance = new SkiersApi(apiClient);
        try {
            RESORTS.add("1");
            SEASON.add("2");
            SkierVertical result = apiInstance.getSkierResortTotals(skierID, RESORTS, SEASON);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ResortsApi#getResorts");
            e.printStackTrace();
        }
    }
}
