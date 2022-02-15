package Threads;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.model.ResortSkiers;
import io.swagger.client.model.SeasonsList;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResortThread implements Runnable {
    // TODO: add id
    public static ApiClient apiClient;
    public static Integer skierID;
    public static CyclicBarrier synk;

    public ResortThread(ApiClient client, Integer numSkiers, CyclicBarrier barrier) {
        apiClient = client;
        skierID = numSkiers;
        synk = barrier;
    }

    public void run() {
        ResortsApi apiInstance = new ResortsApi(apiClient);
        Integer resortID = 56; // Integer | ID of the resort of interest
        try {
            SeasonsList result = apiInstance.getResortSeasons(resortID);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ResortsApi#getResortSeasons");
            e.printStackTrace();
        }

//        ResortsApi apiInstance = new ResortsApi();
//        Integer resortID = 56; // Integer | ID of the resort of interest
//        Integer seasonID = 56; // Integer | ID of the resort of interest
//        Integer dayID = 56; // Integer | ID of the resort of interest
//        try {
//            ResortSkiers result = apiInstance.getResortSkiersDay(resortID, seasonID, dayID);
//            System.out.println(result);
//        } catch (ApiException e) {
//            System.err.println("Exception when calling ResortsApi#getResortSkiersDay");
//            e.printStackTrace();
//        }
        try {
            // TO DO insert code to wait on the CyclicBarrier
            System.out.println("Thread waiting at barrier");
            synk.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(ResortThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
