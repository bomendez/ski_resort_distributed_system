package Utilities;

public class RequestLog {
    public static int numSuccessfulRequests;
    public static int numUnsuccessfulRequests;

    private RequestLog () {}

    public static int getNumSuccessfulRequests() {return numSuccessfulRequests;}

    public static int getNumUnsuccessfulRequests() {return numUnsuccessfulRequests;}

    public static void logSuccess() {numSuccessfulRequests++;}

    public static void logFailure() {numUnsuccessfulRequests++;}
}
