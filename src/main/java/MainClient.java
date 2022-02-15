import Threads.ResortThread;
import io.swagger.client.ApiClient;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Math.floor;

public class MainClient {
    public static int numThreads;
    public static int numSkiers;
    public static int numLifts = 40;
    public static int numRuns = 10;
    public static String ipAddress = "http://localhost:8080/assignment1_war/";
    public static int SKI_DAY_MINS = 420;

    static CyclicBarrier barrier;

    /**
     * sets numThreads to threads / 4
     * @param threads the number of threads
     *               must be less than 1024
     */
    private static void setNumThreads(int threads) {
        if (threads > 1024) {throw new IllegalArgumentException();}
        numThreads = threads;
        System.out.println("numThreads " + numThreads);
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

    public static void processArgs(String[] args) {
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

    /**
     * accepts arguments in two formats
     * @param args
     * format 1, three arguments:  numThreads numSkiers ipAddress
     * format 2, five arguments:   numThreads numSkiers numLifts numRuns ipAddress
     */
    public static void main(String[] args) {
        processArgs(args);
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(ipAddress);


        /**
         * Phase 1
         */
        final long startPhase1 = System.currentTimeMillis();

        int numThreadsPhase1 = (int) Math.floor(numThreads/4);
        barrier = new CyclicBarrier(numThreadsPhase1+ 1);

        int skierIdStart = 0;
        int skierIdStop = numSkiers/numThreadsPhase1;
        int startDayPhase1 = 0;
        int endDayPhase1 = 90;
        double numCalls = floor(numRuns*0.2)*(numSkiers/numThreadsPhase1);

        for (int i = 0; i < numThreadsPhase1; i++) {
            System.out.println("Thread " + i + " start: " + skierIdStart + " end: " + skierIdStop);
            ResortThread resortThread = new ResortThread(i, apiClient, barrier, skierIdStart,
                    skierIdStop, startDayPhase1, endDayPhase1, numThreadsPhase1, numSkiers, numRuns, numCalls, numLifts);
            new Thread(resortThread).start();
            skierIdStart = skierIdStop + 1;
            skierIdStop += numSkiers/numThreadsPhase1;
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

        /**
         * Phase 2: how to check for 20%
         */
    }

}
