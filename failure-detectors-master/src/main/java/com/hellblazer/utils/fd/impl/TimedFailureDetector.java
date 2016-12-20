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

/**
 * 
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 * 
 */
public class TimedFailureDetector implements FailureDetector {
    private volatile long last = -1;
    private final long    maxInterval;

    public TimedFailureDetector(long maxInterval) {
        this.maxInterval = maxInterval;
    }

    @Override
    public void record(long now, long delay) {
        last = now;
    }

    @Override
    public boolean shouldConvict(long now) {
        if (last < 0) {
            return false;
        }
        return now - last > maxInterval;
    }

	@Override
	public boolean shouldConvictByDesign(long now) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setExpectedInterArrivalTime(double expected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordTimeout(double Timeout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordDelta(double delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getInterArrivalTime(double[] temp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getAverageInterArrivalTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getExpectedInterArrivalTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getInterArrivalTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDelta() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxarr() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAverage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean shouldConvictPhi(long now, int BigPhi) {
		// TODO Auto-generated method stub
		return false;
	}

}
