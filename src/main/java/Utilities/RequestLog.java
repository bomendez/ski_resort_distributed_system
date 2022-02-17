package Utilities;

import java.util.Collections;

public class RequestLog {
    public static int numSuccessfulRequests;
    public static int numUnsuccessfulRequests;
    public static int numRequests;
    // TODO: add JS object with key=Thread, values=start time, request type (ie POST), latency, response code

    private RequestLog() {}

    public static int getNumSuccessfulRequests() {return numSuccessfulRequests;}

    public static int getNumUnsuccessfulRequests() {return numUnsuccessfulRequests;}

    public static int getNumRequests() {return numRequests;}

    public static void logSuccess() {numSuccessfulRequests++;}

    public static void logFailure() {numUnsuccessfulRequests++;}

    public static void incRequestCount() {numRequests++;}
}
