/** (C) Copyright 2010 Hal Hildebrand, All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.hellblazer.utils.fd.impl;

import com.hellblazer.utils.fd.FailureDetector;
import com.hellblazer.utils.windows.MultiWindow;
import com.hellblazer.utils.windows.RunningAverage;
import com.hellblazer.utils.windows.SampledWindow;
import com.hellblazer.utils.windows.Window;

import java.util.Arrays;

/**
 * An adaptive accural failure detector based on the paper:
 * "A New Adaptive Accrual Failure Detector for Dependable Distributed Systems"
 * by Benjamin Satzger, Andreas Pietzowski, Wolfgang Trumler, Theo Ungerer
 * 
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand/a>
 * 
 */
public class AdaptiveFailureDetector extends MultiWindow implements
        FailureDetector {

    private double       last        = -1.0;
    private final double minInterval;
    private final double scale;
    private final double threshold;
    private double       sumOfDelays = 0.0;
    private double delta;
	private SampledWindow       window;
	
	public double Expected;
	public double timeout;
	public double maxarr;
	public double average;
    /**
     * 
     * @param convictionThreshold
     *            - the level of certainty that must be met before conviction.
     *            This value must be <= 1.0
     * @param windowSize
     *            - the number of samples in the window
     * @param scale
     *            - a scale factor to accomidate the real world
     * @param expectedSampleInterval
     *            - the expected sample interval, used to prime the detector
     * @param initialSamples
     *            - the number of initial samples to prime the detector
     * @param minimumInterval
     *            - the minimum inter arival interval
     */
    
   
    
    public AdaptiveFailureDetector(double convictionThreshold, int windowSize,
                                   double scale, long expectedSampleInterval,
                                   int initialSamples, double minimumInterval) {
        super(windowSize, 2);
        if (convictionThreshold > 1.0) {
            throw new IllegalArgumentException(
                                               String.format("Conviction threshold %s must be <= 1.0",
                                                             convictionThreshold));
        }
        threshold = convictionThreshold;
        minInterval = minimumInterval;
        this.scale = scale;

        window = new RunningAverage(windowSize);
        
        setExpectedInterArrivalTime(expectedSampleInterval);
        
        long now = System.currentTimeMillis();
        last = now - initialSamples * expectedSampleInterval;
        for (int i = 0; i < initialSamples; i++) {
            record((long) (last + expectedSampleInterval), 0L);
        }
        last = -1.0;
    }

    @Override
    public synchronized void record(long timeStamp, long delay) {
        if (last >= 0.0) {
            double sample = timeStamp - last;
           // if (sample > minInterval) {
                sumOfDelays += delay;
                if (count == samples.length) {
                    double[] removed = removeFirst();
                    sumOfDelays -= removed[1];
                }
                addLast(sample, delay);
                
                window.sample(sample);
           // }
          //  else
           // {
           // 	FdMain.mloss++;
           // 	return;
          //  }
        }
        double averageDelay = count == 0 ? 0.0 : sumOfDelays / count;
        last = timeStamp + averageDelay;
    }
    
    

	

	
    @Override
	public void setExpectedInterArrivalTime(double expected){
    	Expected=expected;
    }
    @Override
	public void recordTimeout(double timeout) {
		this.timeout=timeout;
	}
    public void recordDelta(double delta){
    	this.delta=delta;
    }
    public void recordAverage(double average){
    	this.average=average;
    }
    public void recordMaxarr(double maxarr){
    	this.maxarr= maxarr;
    }
    
    @Override
	public double getTimeout() {
		// TODO Auto-generated method stub
		return this.timeout;
	}
    public double getDelta(){
    	return delta;
    }
    public double getAverage(){
    	return this.average;
    }
    public double getMaxarr(){
    	return this.maxarr;
    }
    @Override
	public double getExpectedInterArrivalTime(){
    	return this.Expected;
    }
    
    public synchronized boolean shouldConvictPhi(long now,int BigPhi) {
        if (last < 0) {
            return false;
        }
        
        average = getAverageInterArrivalTime();
        
        double delta = now - last;
        double timeout = average * BigPhi * 2.3025;
        
        recordDelta(delta);
        recordAverage(average);
        recordTimeout(timeout);
        
        /*
        if (convict) {
            System.out.println(String.format("delta: %s, count: %s, size: %s, probability: %s, window: %s",
                                             delta, countLessThanEqualTo,
                                             count, probability, sorted));
        }*/
        boolean shouldConvict = delta > timeout;
        
        return shouldConvict;
    }
    
    @Override
	public boolean shouldConvictByDesign(long now) {
		
    	
    	if (last < 0) {
            return false;
        }
    	
    	double delta = (now - last - sumOfDelays / count) * scale;
    	
    	double average;
        double[] temparr = new double[1000];
        double timeout;
        
        double expectedInterArrivalTime = getExpectedInterArrivalTime();
        
        recordDelta(delta);
        
        temparr = getInterArrivalTime();
        average = getAverageInterArrivalTime();
        
        timeout = average + temparr[998] - expectedInterArrivalTime;
        
        recordAverage(average);
        recordTimeout(timeout);
        recordMaxarr(temparr[998]);
        
      
        boolean shouldConvict = delta > timeout;
        
        temparr = null;
        
		return shouldConvict;
	}
    
    @Override
    public synchronized boolean shouldConvict(long now) {
        if (last < 0) {
            return false;
        }

        double delta = (now - last - sumOfDelays / count) * scale;
        double countLessThanEqualTo = countLessThanEqualTo(delta);
        double probability = countLessThanEqualTo / count;
        boolean convict = probability >= threshold;
        recordDelta(delta);
        /*
        if (convict) {
            System.out.println(String.format("delta: %s, count: %s, size: %s, probability: %s, window: %s",
                                             delta, countLessThanEqualTo,
                                             count, probability, sorted));
        }*/
        return convict;
    }

    /**
     * @param delta
     * @return
     */
    private double countLessThanEqualTo(double delta) {
        int deltaCount = 0;
        for (int i = 0; i < count; i++) {

            if (samples[(i + head) % samples.length][0] <= delta) {
                deltaCount++;
            }
        }
        return deltaCount;
    }

	@Override
	
	public double getAverageInterArrivalTime(){
    	
    	double sum =0;
    	
    	int i;
    	
    	for(i=0;i<window.size();i++){
    		
    		sum+=window.getWindowElement(i);
    		
    	}
    	
    	return sum/1000;
    	
    }
	

	@Override
	public double[] getInterArrivalTime(){
    	
    	double[] arr = new double[1000];
    	int i;
    	
    	for(i=0;i<1000;i++){
    		arr[i]=window.getWindowElement(i);
    	}
    	
    	Arrays.sort(arr);
    	
    	return arr;
    }

	


 
	@Override
	public void getInterArrivalTime(double[] temp) {
		// TODO Auto-generated method stub
		
	}
}