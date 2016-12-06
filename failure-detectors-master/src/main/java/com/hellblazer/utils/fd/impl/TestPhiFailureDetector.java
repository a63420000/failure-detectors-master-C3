package com.hellblazer.utils.fd.impl;

import com.hellblazer.utils.fd.FailureDetector;
import com.hellblazer.utils.fd.FailureDetectorFactory;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;

import com.hellblazer.utils.fd.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/////-------------
import java.util.Arrays;
/////------------ Array.sort();

public class TestPhiFailureDetector {
	
	FailureDetectorFactory PhiFDfac;
	
	FailureDetector PhiFD;
	public double[] Testarr=new double[1000];
	public long now,now2;
    
	public TestPhiFailureDetector(){
		
		PhiFDfac = new PhiFailureDetectorFactory(1, 1000, 1000, 1000, 1000, false);
		PhiFD = PhiFDfac.create(); //phiFD is FailureDetector .
		
		//double convictionThreshold,
        //int windowSize
        //long expectedSampleInterval,
        //int initialSamples,
        //double minimumInterval
		//boolean useMedian
		
	}
	
	public void run(){
		
		now= System.currentTimeMillis();
		
		double  timeout,average;
		long temp;
		double expectedInterval;
		
		PhiFD.record(now,777);
		
		try{
			
			Thread.sleep(4000);
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		now2 = System.currentTimeMillis();
		
		PhiFD.record(now2,777);
		
		expectedInterval=PhiFD.getExpectedInterArrivalTime();
		
		Testarr=PhiFD.getInterArrivalTime();
		
		average=PhiFD.getAverageInterArrivalTime();
		
		timeout= average+Testarr[999]-PhiFD.getExpectedInterArrivalTime();
		
		//Testarr[999] is another window to calculate the next heartbeat inter-arrival time.
		
		System.out.println("yeeee 0: "+ Testarr[0]+"   yeeee 999: "+ Testarr[999]);
		System.out.println("average : "+average+"ms");
		System.out.println("timeout : "+timeout);
		
		//System.out.println( "Test --->>>"+PhiFD.shouldConvict(now2) );
		
	}
	
	public static void main(String[] args){
		TestPhiFailureDetector test= new TestPhiFailureDetector();
		test.run();
		
		
	}
	
		
}
