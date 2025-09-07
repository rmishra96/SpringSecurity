package com.springsecurity.UtilityPackage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class RateLimiterSlide {

    private static final Map<String, Deque> rateLimiters = new HashMap<>();
    //how may request you are going to allow
    private static final int Threshold=3;
    private static final int PERIOD_IN_SECONDs=1;


    public boolean request(String userId){
        Deque<Long> deque = rateLimiters.get(userId);
        long currentTime = System.currentTimeMillis();

        ///  first chec if this is the first request. if that is the case the deque won't be initialized
        if(deque==null){
            deque = new ArrayDeque<>();
            deque.addLast(currentTime);
            rateLimiters.put(userId, deque);
            return true;
        }

        // keep on removing the timestamps that are no long the t to t-1s window
        while(!deque.isEmpty() && currentTime -deque.getFirst() > PERIOD_IN_SECONDs){
            deque.removeFirst();
        }
        if(deque.size()>=Threshold){
            return false;
        }
        deque.addLast(currentTime);
        return true;

    }

    public static void main(String[] args) throws InterruptedException {
        RateLimiterSlide limiter =new RateLimiterSlide();
        System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
        System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
        System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
        System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
        Thread.sleep(1000);
        System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
        System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));

    }
}
