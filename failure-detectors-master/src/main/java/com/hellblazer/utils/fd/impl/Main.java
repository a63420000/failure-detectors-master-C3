package com.hellblazer.utils.fd.impl;

public class Main extends Thread{
	
	public void run(){
		NIB nib = new NIB();
		int waitMin = 3;
		
		DistLoggerThread dlogger = new DistLoggerThread("localhost", nib);
		CPULoadingLoggerThread cllogger = new CPULoadingLoggerThread(16195,nib);
		CPULoadingCollectorThread clcollector = new CPULoadingCollectorThread(16195,nib);
		
		dlogger.start();
		cllogger.start();
		clcollector.start();
		
		
		//dlogger.setActive(false);
		//cllogger.setActive(false);
		//clcollector.setActive(false);
		
		//System.out.println("dlogger ceased, and nib..getDistance(localhost)="+nib.getDistance("localhost"));
		//System.out.println("CL logger ceased.. getLoadings(localhost)="+nib.getLoading("localhost"));
		
	}
	
	public static void main(String[] args) {
		//Main test = new Main();
		//test.start();
	}

}
