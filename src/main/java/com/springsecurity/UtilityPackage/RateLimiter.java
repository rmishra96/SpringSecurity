package com.springsecurity.UtilityPackage;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

    private  static final Map<String,FixedWindow> hashmap=new HashMap<String,FixedWindow>();
    private static final int Threshold=5;
    private static final int PERIOD_IN_SECONDS=5;


    static class FixedWindow{
        public int count;
        public long timestamp;

        public FixedWindow(int count, long timestamp){
            this.count = count;
            this.timestamp =  timestamp;
        }
    }

    public boolean request(String userid){
        FixedWindow fixedWindow=hashmap.get(userid);
        long currentTime=System.currentTimeMillis();
        if(fixedWindow== null ||currentTime-fixedWindow.timestamp>PERIOD_IN_SECONDS ){
            hashmap.put(userid,new FixedWindow(1,currentTime));
            return true;
        }else {
            if(fixedWindow.count>Threshold){
                fixedWindow.count++;
                hashmap.put(userid,fixedWindow);
                return true;
            }else {
                return false;
            }
        }
    }


        public static void main(String[] args) throws InterruptedException {
            RateLimiter limiter = new RateLimiter();
            System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
            System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
            System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
            System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
            Thread.sleep(1000);
            System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));
            System.out.println("Sent to API Gateway Server? " + (limiter.request("1234") ? "Yes" : "No"));

        }
    }

