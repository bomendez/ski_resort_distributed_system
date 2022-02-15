import Threads.ResortThread;
import Threads.SkierThread;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.model.SeasonsList;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MainClient {
    public static int numThreads;
    public static int numSkiers;
    public static int numLifts = 40;
    public static int numRuns = 10;
    public static String ipAddress = "http://localhost:8080/assignment1_war/";
    public static int SKI_DAY_MINS = 420;

    static CyclicBarrier barrier;

    private static void setNumThreads(int threads) {
        if (threads > 1024) {throw new IllegalArgumentException();}
        numThreads = threads;
    }

    private static void setNumSkiers(int skiers) {
        if (skiers > 100000) {throw new IllegalArgumentException();}
        numSkiers = skiers;
    }

    private static void setSkiLifts(int skiLifts) {
        if (skiLifts < 5 || skiLifts > 60) {throw new IllegalArgumentException();}
        numLifts = skiLifts;
    }

    private static void setNumRuns(int runs) {
        if (runs > 20) {throw new IllegalArgumentException();}
        numRuns = runs;
    }

    private static void setIpAddress(String ip) {
        ipAddress = ip;
    }

    public static void validateArgs(String[] args) {
        if (args.length < 1) {throw new IllegalArgumentException("no arguments provided");}
        if (args.length > 6) {throw new IllegalArgumentException("too many arguments provided");}
        if (args.length > 3) {
            try {
                setSkiLifts(Integer.parseInt(args[2]));
                setNumRuns(Integer.parseInt(args[3]));
                setIpAddress(args[4]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            try {
                setIpAddress(args[3]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        try {
            setNumThreads(Integer.parseInt(args[0]));
            setNumSkiers(Integer.parseInt(args[1]));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        validateArgs(args);
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(ipAddress);


        final long startPhase1 = System.currentTimeMillis();

        barrier = new CyclicBarrier(numThreads + 1);
        for (int i = 0; i < numThreads; i++) {
            // lambda runnable creation - interface only has a single method so lambda works fine
//            Threads.SkierThread skierThread = new Threads.SkierThread(counter, completed, ipAddress);
            ResortThread resortThread = new ResortThread(apiClient, numSkiers, barrier);
            new Thread(resortThread).start();
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        final long endPhase1 = System.currentTimeMillis();
        final long durationPhase1 = endPhase1 - startPhase1;
        System.out.printf("duration: %s milliseconds%n", durationPhase1);
    }

}
