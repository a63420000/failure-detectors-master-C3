package com.hellblazer.utils.fd.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class multiThreadTestMain extends Thread{

	public static String Ctko;
	public static double highestStdDev;
	public static double lowestStdDev;
	public static double highestDist;
	public static double lowestDist;
	public static double highLoad;
	public static double lowLoad;
	//mapping
	public static List<String> mapping =  new ArrayList<String>();
	// Results
	public static List<Double> eval_BasicGA = new ArrayList<Double>();
	public static List<Long> time_BasicGA = new ArrayList<Long>();
	public static List<String> sol_BasicGA = new ArrayList<String>();
	public static List<Double> dist_BasicGA = new ArrayList<Double>();
	public static List<Double> stdev_BasicGA = new ArrayList<Double>();

	public static List<Double> eval_BruteForce = new ArrayList<Double>();
	public static List<Long> time_BruteForce = new ArrayList<Long>();
	public static List<String> sol_BruteForce = new ArrayList<String>();
	public static List<Double> dist_BruteForce = new ArrayList<Double>();
	public static List<Double> stdev_BruteForce = new ArrayList<Double>();

	public static List<Double> eval_CustmGA = new ArrayList<Double>();
	public static List<Long> time_CustmGA = new ArrayList<Long>();
	public static List<String> sol_CustmGA = new ArrayList<String>();
	public static List<Double> dist_CustmGA = new ArrayList<Double>();
	public static List<Double> stdev_CustmGA = new ArrayList<Double>();

	public static List<Double> eval_FCFM = new ArrayList<Double>();
	public static List<Long> time_FCFM = new ArrayList<Long>();
	public static List<String> sol_FCFM = new ArrayList<String>();
	public static List<Double> dist_FCFM = new ArrayList<Double>();
	public static List<Double> stdev_FCFM = new ArrayList<Double>();

	public static List<Double> eval_NSNC = new ArrayList<Double>();
	public static List<Long> time_NSNC = new ArrayList<Long>();
	public static List<String> sol_NSNC = new ArrayList<String>();
	public static List<Double> dist_NSNC = new ArrayList<Double>();
	public static List<Double> stdev_NSNC = new ArrayList<Double>();

	public static List<Double> eval_SVVR = new ArrayList<Double>();
	public static List<Long> time_SVVR = new ArrayList<Long>();
	public static List<String> sol_SVVR = new ArrayList<String>();
	public static List<Double> dist_SVVR = new ArrayList<Double>();
	public static List<Double> stdev_SVVR = new ArrayList<Double>();

	public static List<Double> eval_PPF = new ArrayList<Double>();
	public static List<Long> time_PPF = new ArrayList<Long>();
	public static List<String> sol_PPF = new ArrayList<String>();
	public static List<Double> dist_PPF = new ArrayList<Double>();
	public static List<Double> stdev_PPF = new ArrayList<Double>();

	// switch related
	public static List<String> switchList = new ArrayList<String>(); // e.g.
																		// 00:00:00:00:00:00:00:01
	public static List<Double> SwLoadContributions = new ArrayList<Double>(); // cpu
																				// utilization
																				// e.g.
																				// 0.8
	public static Map<String, Integer> invertedSwitchList = new HashMap<String, Integer>();

	// sw-con related
	public static List<String> aChrome = new ArrayList<String>(); // e.g.
																	// 192.168.17.146
	public static Map<String, String> aSwConMapping = new HashMap<String, String>(); // e.g.
																						// <"00:00:00:00:00:00:00:01","192.168.17.146">
	public static List<Map<String, String>> aSetOfMapping = new ArrayList<Map<String, String>>(); // e.g.
																									// Map<String,
																									// String>
	public static List<Double> evaluations = new ArrayList<Double>(); // e.g.
																		// 1.2345

	// controller related
	public static List<String> controllerList = new ArrayList<String>();
	public static Map<String, Integer> invertedControllerList = new HashMap<String, Integer>(); // e.g.
																								// <192.168.17.146,
																								// 0>
																								// or
																								// <192.168.17.147,
																								// 1>
	public static Map<String, Double> controllerLoadings = new HashMap<String, Double>();
	public static Map<String, Double> switchControllerDistances = new HashMap<String, Double>();

	public static List<String> AChrome = new ArrayList<String>();
	public static List<String> BChrome = new ArrayList<String>();

	public static Random rand = new Random();
	
	public static List<String> test = new ArrayList<String>();
	public int first=0;
	public static List<String> temp = new ArrayList<String>();
	public multiThreadTestMain(List<String> temp){
		this.temp=temp;
	}
	
	public void run(){
		
	 while(true){
		
		highestStdDev = 0.5;
		lowestStdDev = 0.0;
		highestDist = 13.0;
		lowestDist = 3.0;
		highLoad = 0.7;
		lowLoad = 0.4;

		int switchNumber = 5;
		int ctrlNumber = 3;

		int custmInitSetSize = 50;
		double mutationRate = 0.4;
		int maxMutation = 3000;
		int maxGeneration = 10;
		
		for (int i = 0; i < 1; i++) {

			List<String> mapping1 = new ArrayList<String>();
			List<String> mapping2 = new ArrayList<String>();
			List<String> mapping3 = new ArrayList<String>();
			List<String> mapping4 = new ArrayList<String>();
			List<String> mapping5 = new ArrayList<String>();
			List<String> mapping6 = new ArrayList<String>();
			List<String> mapping7 = new ArrayList<String>();

			int initialSetSize = 50;
			System.out.println("*** The " + i + "-th round ***");
			// System.out.println("=====================================");
			generateRandomSwitches(switchNumber, 2, 40);
			generateRandomControllers(ctrlNumber, 3, 13, 50, 60);
			//printSwitchesInfo();
			// printControllersInfo();
			// System.out.println("=====================================\n");
			
			// System.out.println("=====================================");
			long timeInit0 = System.currentTimeMillis();
			generateInitialChromeSet(switchNumber, initialSetSize, ctrlNumber,30); // original genetic algorithm
			long timeInit1 = System.currentTimeMillis();
			// System.out.println("-- generateInitialChromeSet time = "+(timeInit1-timeInit0)+" ms");
			// System.out.println(" Initial chrome set size = "+aSetOfMapping.size());
			// System.out.println("=====================================\n");
			
			// System.out.println("=====================================");
			
			
			
			long time1 = System.currentTimeMillis();
			mapping1 = generateBestChromeTraditional();
			long time2 = System.currentTimeMillis();
			eval_BasicGA.add(evaluateChrome(mapping1));
			time_BasicGA.add((time2 - time1));
			sol_BasicGA.addAll(mapping1);
			///////
			System.out.println(first);
			if(first==0){
				for(int x=0;x<mapping1.size();x++){
					temp.add(mapping1.get(x));
					first++;
				}
			}else{
				for(int x=0;x<mapping1.size();x++)
					temp.set(x,mapping1.get(x));
			}
			///////
			dist_BasicGA.add(calculateAvgControllerSwitchDistance(mapping1));
			stdev_BasicGA.add(calculateAvgControllerLoadingVariance(mapping1));
		
			resetAllControllersSwitches();
			// System.out.println("");
		} // end for

		System.out.println("======== [[ Start our little statistics ]] ================");

		// System.out.println("[Basic GA] evaluations: "+eval_BasicGA);
		// System.out.println("[Basic GA] time: "+time_BasicGA);
		// System.out.println("[Basic GA] solution: "+sol_BasicGA);
		System.out.println("[Basic GA] Average E="+ averageDouble(eval_BasicGA));
		
		System.out.println("[Basic GA] Average exe time="+ averageLong(time_BasicGA));
		
		System.out.println("[Basic GA] Average distance="+ averageDouble(dist_BasicGA));
		
		System.out.println("[Basic GA] Average std dev="+ averageDouble(stdev_BasicGA));
		// System.out.println("=================");
		// System.out.println("[Brute-Force] evaluations: "+eval_BruteForce);
		// System.out.println("[Brute-Force] time: "+time_BruteForce);
		// System.out.println("[Brute-Force] solution: "+sol_BruteForce);
		// System.out.println("[Brute-Force] Average E="+averageDouble(eval_BruteForce));
		// System.out.println("[Brute-Force] Average exe time="+averageLong(time_BruteForce));
		// System.out.println("[Brute-Force]returnResult Average distance="+averageDouble(dist_BruteForce));
		// System.out.println("[Brute-Force] Average std dev="+averageDouble(stdev_BruteForce));
		System.out.println("=================");
		// System.out.println("[Customized GA] evaluations: "+eval_CustmGA);
		// System.out.println("[Customized GA] time: "+time_CustmGA);
		// System.out.println("[Customized GA] solution: "+sol_CustmGA);
		// System.out.println("[Customized GA] Average E="+averageDouble(eval_CustmGA));
		// System.out.println("[Customized GA] Average exe time: "+averageLong(time_CustmGA));
		// System.out.println("[Customized GA] Average distance="+averageDouble(dist_CustmGA));
		// System.out.println("[Customized GA] Average std dev="+averageDouble(stdev_CustmGA));
		// System.out.println("=================");
		/*
		
		System.out.println("[FCFM] evaluations: " + eval_FCFM);
		System.out.println("[FCFM] time: " + time_FCFM);
		System.out.println("[FCFM] solution: " + sol_FCFM);
		System.out.println("[FCFM] Average E=" + averageDouble(eval_FCFM));
		System.out.println("[FCFM] Average exe time=" + averageLong(time_FCFM));
		System.out.println("[FCFM] Average distance="+ averageDouble(dist_FCFM));
		System.out.println("[FCFM] Average std dev="+ averageDouble(stdev_FCFM));
		
		*/
		// System.out.println("=================");
		// System.out.println("[NSNC] Average E="+averageDouble(eval_NSNC));
		// System.out.println("[NSNC] Average exe time="+averageLong(time_NSNC));
		// System.out.println("[NSNC] Average distance="+averageDouble(dist_NSNC));
		// System.out.println("[NSNC] Average std dev="+averageDouble(stdev_NSNC));
		// System.out.println("=================");
		// System.out.println("[SVVR] Average E="+averageDouble(eval_SVVR));
		// System.out.println("[SVVR] Average exe time="+averageLong(time_SVVR));
		// System.out.println("[SVVR] Average distance="+averageDouble(dist_SVVR));
		// System.out.println("[SVVR] Average std dev="+averageDouble(stdev_SVVR));
		// System.out.println("=================");
		// System.out.println("[PPF] Average E="+averageDouble(eval_PPF));
		// System.out.println("[PPF] Average exe time="+averageLong(time_PPF));
		// System.out.println("[PPF] Average distance="+averageDouble(dist_PPF));
		// System.out.println("[PPF] Average std dev="+averageDouble(stdev_PPF));

		System.out.println("======== [[ End our little statistics ]] ================");
		try{

			Thread.sleep(5000);		
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
	  }//while true
	
	}//end run
	
	public static void main() {
		//multiThreadTestMain temp= new multiThreadTestMain();
		//temp.run(test);
	} // end main

	/**
	 * 
	 * @param switchNumber
	 *            Assign the number of switches to create
	 * @param switchLoadFrom
	 *            Assign the lowest possible switch load contribution in system
	 *            (in percentage)
	 * @param switchLoadTo
	 *            Assign the highest possible switch load contribution in system
	 *            (in percentage)
	 */
	
	public static List returnResult(){
		return mapping;
	}
	
	public static void setResult(List result){
		List<String> mapping= new ArrayList<String>();
		mapping=result;
	}
	
	public static void generateRandomSwitches(int switchNumber,
			int switchLoadFrom, int switchLoadTo) {
		int n = switchNumber;
		double randLoadContri = 0.0;
		int switchloadLow = switchLoadFrom; // in percentage
		int switchloadHigh = switchLoadTo; // in percentage

		for (int i = 0; i < n; i++) {
			String s = null;
			if (i < 10)
				s = "00:00:00:00:00:00:00:0" + i;
			else if (i < 100)
				s = "00:00:00:00:00:00:00:" + i;
			else if (i < 1000)
				s = "00:00:00:00:00:00:0" + (int) (i / 1000) + ":" + (i % 1000);
			else
				System.out.println("[ERROR] switch number out of bound..");
			switchList.add(s);
			invertedSwitchList.put(s, i);
			randLoadContri = (randInt(switchloadLow, switchloadHigh) / 100.0);
			SwLoadContributions.add(i, randLoadContri);
		}
	}

	/**
	 * 
	 * @param controllerNumber
	 * @param distanceFrom
	 *            Assign the shortest distance in system (in ms)
	 * @param distanceTo
	 *            Assign the longest distance in system (in ms)
	 * @param loadingFrom
	 *            Assign the lowest possible controller loading in system (in
	 *            percentage)
	 * @param loadingTo
	 *            Assign the highest possible controller loading in system (in
	 *            percentage)
	 */
	public static void generateRandomControllers(int controllerNumber,
			int distanceFrom, int distanceTo, int loadingFrom, int loadingTo) {
		int n = controllerNumber;
		double randCtrlDist = 0.0;
		int DistLow = distanceFrom; // in ms
		int DistHigh = distanceTo; // in ms
		double randLoadings = 0.0;
		int loadingLow = loadingFrom; // in percentage
		int loadingHigh = loadingTo; // in percentage

		for (int i = 0; i < n; i++) {
			String s = null;
			if (i < 1000)
				s = "192.168.10." + (i+138);
			else
				System.out.println("[ERROR] controller number out of bound..");
			
			controllerList.add(s);
			invertedControllerList.put(s, i);
			randCtrlDist = randInt(DistLow, DistHigh);// raondom set dist to thoes three controllers
			switchControllerDistances.put(s, randCtrlDist);
			randLoadings = (randInt(loadingLow, loadingHigh) / 100.0);
			controllerLoadings.put(s, randLoadings);
		}
	}

	// Printing functions
	public static double calculateAvgControllerSwitchDistance(List<String> mapping) {
		double[] m = new double[mapping.size()];
		for (int i = 0; i < mapping.size(); i++) {
			double dist = switchControllerDistances.get(mapping.get(i));
			// System.out.println(dist);
			m[i] = dist;
		}
		return getMean(m);//get those three controllers' mean ex 20 30 40  mean = 30
	}

	public static void printAvgControllerSwitchDistance(List<String> mapping) {
		System.out.println("Mean = "
				+ calculateAvgControllerSwitchDistance(mapping));
	}

	public static double calculateAvgControllerLoadingVariance(
			List<String> mapping) {
		double[] load = new double[controllerList.size()];
		// double[] load = new double[mapping.size()];
		// System.out.println("Controller loadings after migration:");
		for (int i = 0; i < load.length; i++) {
			load[i] = controllerLoadings.get(controllerList.get(i));
			// System.out.println("Retrieved ControllerLoadings["+i+"]: "+load[i]);
		}
		for (int i = 0; i < mapping.size(); i++) {
			int controllerIndex = invertedControllerList.get(mapping.get(i));
			double addLoad = SwLoadContributions.get(i);
			load[controllerIndex] = load[controllerIndex] + addLoad;
		}
		for (int i = 0; i < load.length; i++) {
			// System.out.println(load[i]);
		}
		// System.out.println("Standard Deviation = "+getStdDev(load,load.length));
		return getStdDev(load, load.length);
	}

	public static void printControllerLoadingVariance(List<String> mapping) {
		System.out.println("Standard Deviation = "
				+ calculateAvgControllerLoadingVariance(mapping));
	}

	public static void printChromeSet(List<Map<String, String>> m) {
		for (int i = 0; i < m.size(); i++)
			System.out.println("printed chromeSet[" + i + "]: " + m.get(i));
	}

	
	public static void printSwitchesInfo() {
		System.out.println("=== Switch Information ===");
		for (int i = 0; i < switchList.size(); i++)
			System.out.println("switchList[" + i + "]=\"" + switchList.get(i)+ "\"");
		
		for (Map.Entry<String, Integer> entry : invertedSwitchList.entrySet())
			System.out.println("invertedSwitchList <\"" + entry.getKey()+ "\", " + entry.getValue() + ">");
		
		for (int i = 0; i < SwLoadContributions.size(); i++)
			System.out.println("SwLoadContributions[" + i + "]:"+ SwLoadContributions.get(i));
		
		System.out.println("");
	}

	public static void printControllersInfo() {
		System.out.println("=== Controller Information ===");
		for (int i = 0; i < controllerList.size(); i++)
			System.out.println("controllerList[" + i + "]:"
					+ controllerList.get(i));
		for (Map.Entry<String, Double> entry : controllerLoadings.entrySet())
			System.out.println("controllerLoadings " + entry.getKey() + "/"
					+ entry.getValue());
		for (Map.Entry<String, Integer> entry : invertedControllerList
				.entrySet())
			System.out.println("invertedControllerList <\"" + entry.getKey()
					+ "\", " + entry.getValue() + ">");
		for (Map.Entry<String, Double> entry : switchControllerDistances
				.entrySet())
			System.out.println("switchControllerDistances <\"" + entry.getKey()
					+ "\", " + entry.getValue() + ">");

		System.out.println("");
	}

	// Utility functions
	public static double getSwitchControllerDistances(String ctrl) {
		return switchControllerDistances.get(ctrl);
	}

	public static double getMean(double[] a) {
		int n = a.length;
		double mean = 0.0;
		double sum = 0.0;
		for (int i = 0; i < n; i++) {
			sum = sum + a[i];
		}
		mean = sum / n;
		return mean;
	}

	public static double getStdDev(double[] loadings, int NumController) {
		int n = NumController;
		double mean = 0;
		double sum = 0;
		double varSum = 0;
		double stdDev = 0;

		for (int i = 0; i < n; i++) {
			sum = sum + loadings[i];
		}
		mean = sum / n;

		for (int i = 0; i < n; i++) {
			varSum = varSum + Math.pow((loadings[i] - mean), 2);
		}
		stdDev = Math.pow((varSum / (n - 1)), 0.5); // Be careful of 1/2==0
													// instead of 0.5
		return stdDev;
	}

	public static double evaluateChrome(List<String> chrome) {
		double e = 0.0;
		double newLoading = 0;
		double sumDist = 0;
		int count = 0;
		int c2 = 0;
		// Map<String, Double> controllerNewLoadingsMap = new HashMap<String,
		// Double>(); // e.g. <192.168.17.146, 0.56>
		double[] controllerNewLoadingsArray = new double[controllerLoadings
				.size()];

		// System.out.println("[E1] Trim out original loadings..");
		for (int i = 0; i < controllerList.size(); i++) {
			controllerNewLoadingsArray[i] = controllerLoadings
					.get(controllerList.get(i));
			invertedControllerList.put(controllerList.get(i), i);
		}
		// for (int i=0; i<controllerList.size(); i++)
		// System.out.println("[E1] controllerNewLoadingsArray["+i+"]="+controllerNewLoadingsArray[i]);

		// Update controller loadings
		for (int i = 0; i < chrome.size(); i++) {
			// System.out.println("[DEBUG] invertedControllerList.get("+chrome.get(i)+")="+invertedControllerList.get(chrome.get(i)));
			// System.out.println("[DEBUG] i="+i);
			newLoading = SwLoadContributions.get(i)
					+ controllerNewLoadingsArray[invertedControllerList
							.get(chrome.get(i))];
			controllerNewLoadingsArray[invertedControllerList
					.get(chrome.get(i))] = newLoading;
		}
		// for (int i=0; i<controllerList.size(); i++)
		// System.out.println("[E2] controllerNewLoadingsArray["+i+"]="+controllerNewLoadingsArray[i]);

		for (String value : chrome) {
			// newLoading = SwLoadContributions.get(count) +
			// controllerLoadings.get(value);
			// System.out.println("[D] newLoading for "+value+" = "+SwLoadContributions.get(count)
			// +" + "+ controllerLoadings.get(value));
			// controllerNewLoadingsMap.put(value, newLoading);
			sumDist = sumDist + getSwitchControllerDistances(value);
			// System.out.println("[E3] sumDist = "+(sumDist-getSwitchControllerDistances(value))+" + "+getSwitchControllerDistances(value));
			count++;
		}

		// System.out.println("[Evaluate] Convert controllerNewLoadingsMap to controllerNewLoadingsArray..");
		// for (Map.Entry<String, Double> entry :
		// controllerNewLoadingsMap.entrySet()){
		// System.out.println(entry.getKey() + "/" + entry.getValue());
		// controllerNewLoadingsArray[c2] = entry.getValue();
		// c2++;
		// }
		// System.out.println("[Evaluate] finally c2="+c2+"..");

		e = ((sumDist / count) - lowestDist)
				/ (highestDist - lowestDist)
				+ (getStdDev(controllerNewLoadingsArray,
						controllerLoadings.size()) - lowestStdDev)
				/ (highestStdDev - lowestStdDev);
		// System.out.println("[Evaluate] Result: "+e+" = ( ("+sumDist+"/"+count+") - "+lowestDist+")/("+highestDist+" - "+lowestDist+") + ("+getStdDev(controllerNewLoadingsArray,controllerLoadings.size())+" - "+lowestStdDev+")/("+highestStdDev+" - "+lowestStdDev+")");

		return e;
	}

	public static List<Double> evaluateChromeSet(
			List<Map<String, String>> mapSet) {
		List<Double> e = new ArrayList<Double>();
		for (int i = 0; i < mapSet.size(); i++) {
			Map<String, String> m = new HashMap<String, String>();
			List<String> t = new ArrayList<String>();
			m = mapSet.get(i);
			t = convertSwitchControllerFromMapToList(m);
			double eValue = evaluateChrome(t);
			e.add(eValue);
		}
		if (e.size() != mapSet.size()) {
			System.out.println("[ERROR] evaluateChromeSet error!!");
		}
		return e;
	}

	public static List<Map<String, String>> copyChromeSet(
			List<Map<String, String>> inputSet) {
		List<Map<String, String>> aSet = new ArrayList<Map<String, String>>();
		for (int i = 0; i < inputSet.size(); i++) {
			Map<String, String> m = inputSet.get(i);
			aSet.add(m);
		}
		return aSet;
	}

	public static int randInt(int min, int max) {

		Random rand = new Random();
		// Note: range of nextInt is exclusive e.g. nextInt(2)=0 or 1, so add 1
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static List<String> convertSwitchControllerFromMapToList(
			Map<String, String> map) {
		List<String> t = new ArrayList<String>();
		for (int i = 0; i < map.size(); i++) {
			t.add(null);
		}
		for (int i = 0; i < map.size(); i++) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				t.set(invertedSwitchList.get(entry.getKey()), entry.getValue()); // don't
																					// use
																					// add,
																					// use
																					// set
			}
		}
		return t;
	}

	public static Map<String, String> convertSwConMappingFromListToMap(
			List<String> chrome) {
		Map<String, String> output = new HashMap<String, String>();
		String controllerIP = null;
		String switchID = null;
		for (int i = 0; i < chrome.size(); i++) {
			switchID = switchList.get(i);
			controllerIP = chrome.get(i);
			output.put(switchID, controllerIP);
		}
		return output;
	}

	public static List<Map<String, String>> selectBestChromes(
			List<Map<String, String>> chromeSet, List<Double> eval, int number) {
		List<Map<String, String>> output = new ArrayList<>();
		if (number != 0) {
			int[] bestChromesIndexes = new int[number];
			double temp = 2;

			// This should be replace by sorting O(nlogn) and(plus) retrieving
			// the best few O(n), not two for loop O(n^2)
			for (int i = 0; i < number; i++) {
				temp = 2;
				for (int j = 0; j < chromeSet.size(); j++) {
					if (eval.get(j) < temp) {
						temp = eval.get(j);
						bestChromesIndexes[i] = j;
					}
				}
			}

			// Retrieve
			for (int i = 0; i < number; i++) {
				output.add(chromeSet.get(bestChromesIndexes[i]));
			}
			return output;
		}
		return output;

	}

	public static void resetAllControllersSwitches() {
		// switch related
		switchList = new ArrayList<String>();
		SwLoadContributions = new ArrayList<Double>();
		invertedSwitchList = new HashMap<String, Integer>();

		// sw-con related
		aChrome = new ArrayList<String>();
		aSwConMapping = new HashMap<String, String>();
		aSetOfMapping = new ArrayList<Map<String, String>>();
		evaluations = new ArrayList<Double>();

		// controller related
		controllerList = new ArrayList<String>();
		invertedControllerList = new HashMap<String, Integer>();
		controllerLoadings = new HashMap<String, Double>();
		switchControllerDistances = new HashMap<String, Double>();

		AChrome = new ArrayList<String>();
		BChrome = new ArrayList<String>();
		rand = new Random();
		test = new ArrayList<String>();
	}

	public static double averageDouble(List<Double> e) {
		double sum = 0.0;
		for (int i = 0; i < e.size(); i++) {
			sum = sum + e.get(i);
		}
		return (sum / e.size());
	}

	public static long averageLong(List<Long> t) {
		long sum = 0;
		for (int i = 0; i < t.size(); i++) {
			sum = sum + t.get(i);
		}
		return (sum / t.size());
	}

	// Initial set "aSetOfMapping" generating
	public static void generateInitialChromeSet(int switchNumber,
			int InitialSetSize, int controllerNumber, int maxRenew) {

		int bucketA = 0;
		int bucketB = 0;
		int bucketC = 0;
		int th = InitialSetSize;
		int nCtrl = controllerNumber;
		int nGene = switchNumber;
		String newController = null;
		double sumDistance = 0;
		double stdDev = 0;
		int renewControllerCounter = 0;
		int maxRenewing = maxRenew;
		double[] controllerNewLoadingsArray = new double[controllerNumber];
		double[] controllerNewLoadingsArrayBackup = new double[controllerNumber];
		// System.out.println("predefined values: nCtrl="+nCtrl+" nGene="+nGene);

		// Duplicate controller loadings info
		for (int i = 0; i < nCtrl; i++) {
			controllerNewLoadingsArray[i] = controllerLoadings.get(controllerList.get(i));//ip
			controllerNewLoadingsArrayBackup[i] = controllerLoadings.get(controllerList.get(i));
		}
		// System.out.println("Trim controller loadings");
		 //for (int i=0; i<nCtrl; i++)
			// System.out.println("controllerNewLoadingsArray["+i+"]="+controllerNewLoadingsArray[i]);
	
		// Go through all switches, randomly choose a controller
		while ((bucketA + bucketB + bucketC) < th) {
			
			// Notice: using global variable to store temp chrome is not
			// recommended for fear that it may cause memory reference problem
			Map<String, String> aTempChrome = new HashMap<String, String>();

			// System.out.println("\n[BUCKET1] buckets A:"+bucketA+", B:"+bucketB+", C: "+bucketC+"..");
			// resume controller loadings
			for (int i = 0; i < nCtrl; i++) {
				controllerNewLoadingsArray[i] = controllerNewLoadingsArrayBackup[i];
			}
			
			// generate a chrome
			for (int i = 0; i < nGene; i++) {
				
				// get a new controller
				newController = controllerList.get(randInt(0, nCtrl - 1));
				// System.out.println("[BUCKET2] newCOntroller="+newController);

				// renew controller when it's not a good controller
				// System.out.println("[BUCKET3] controllerLoadings.get(newController)+SwLoadContributions.get(i)= "+controllerLoadings.get(newController)+"+"+SwLoadContributions.get(i));
				// System.out.println("[BUCKET3] controllerNewLoadingsArray[invertedControllerList.get(newController)]+SwLoadContributions.get(i)= "+controllerNewLoadingsArray[invertedControllerList.get(newController)]+"+"+SwLoadContributions.get(i));
				
				// if (
				// controllerLoadings.get(newController)+SwLoadContributions.get(i)
				// > 1.0){
				
				if (controllerNewLoadingsArray[invertedControllerList
						.get(newController)] + SwLoadContributions.get(i) > 1.0) {
					
					renewControllerCounter = 0;
					
					while (renewControllerCounter < maxRenewing
							|| (controllerLoadings.get(newController)
									+ SwLoadContributions.get(i) > 1.0)) {
						
						newController = controllerList.get(randInt(0, nCtrl - 1));
						renewControllerCounter++;
						//System.out.println(renewControllerCounter);
						// System.out.println("[BUCKET3] controller renewed "+renewControllerCounter+"-th times :"+
						// newController);
					}
					// System.out.println("[BUCKET3] controller renewed "+renewControllerCounter+" times :"+
					// newController);
				}
				
				// aSwConMapping.put(switchList.get(i), newController);
				aTempChrome.put(switchList.get(i), newController);
			
				// System.out.println("[BUCKET4] aSwConMapping.put("+switchList.get(i)+", "+newController+");");
				sumDistance = sumDistance
						+ switchControllerDistances.get(newController);
				controllerNewLoadingsArray[invertedControllerList
						.get(newController)] = controllerNewLoadingsArray[invertedControllerList
						.get(newController)] + SwLoadContributions.get(i);
				// System.out.println("[BUCKET4] controllerNewLoadingsArray[] now updated as follows:");
			}
			
			// Test
			// System.out.println("Test size of a mapping (should be 5):"+aTempChrome.size());
			// System.out.println("Test size of mapping set(should be adaptive)(before)"+aSetOfMapping.size());

			// store the chrome
			// aSetOfMapping.add(aSwConMapping);
			aSetOfMapping.add(aTempChrome);
			// System.out.println("Test size of mapping set(should be adaptive)(after)"+aSetOfMapping.size());

			stdDev = getStdDev(controllerNewLoadingsArray, nCtrl);
			// for (int i=0; i<nCtrl; i++)
			// System.out.println("[BUCKET4] newControllers' controllerNewLoadingsArray["+i+"]: "+controllerNewLoadingsArray[i]);
			// for (Map.Entry<String, String> entry010 :
			// aSwConMapping.entrySet())
			// System.out.println("[BUCKET5] aSwConMapping " + entry010.getKey()
			// + "/" + entry010.getValue());

			// throw it into one of the box
			// System.out.println("Record bucket info");
			if ((sumDistance / nGene) < (highestDist) && bucketA < ((th) / 3)) {
				bucketA++;
			} else if (stdDev < ((highestStdDev - lowestStdDev) / 2.0)
					&& bucketB < ((th) / 3)) {
				bucketB++;
			} else if ((bucketA + bucketB + bucketC) <= th) {
				bucketC++;
			}
					
			//System.out.println( "bA "+(bucketA)+" bB " + bucketB+" bC " + bucketC+ " th " + th);
			/*
			try{
			Thread.currentThread().sleep(1000);
			}catch(InterruptedException ex)
			{ ex.printStackTrace();}
			*/
		} // end while
		// System.out.println("initial set generated..");

		// for(int i=0; i<aSetOfMapping.size(); i++){
		// System.out.println("aSetOfMapping.get(i)"+aSetOfMapping.get(i));
		// }

		// evaluate all mappings
		// System.out.println("Evaluate chromes one my one. Map set size="+aSetOfMapping.size());
		for (int i = 0; i < aSetOfMapping.size(); i++) {
			// passing reference
			Map<String, String> m = aSetOfMapping.get(i);
			evaluations.add(evaluateChrome(convertSwitchControllerFromMapToList(m)));
		}

		// Print evaluation
		// for(int i=0; i<evaluations.size(); i++){
		// System.out.println("evaluations["+i+"]="+evaluations.get(i));
		// }

		// System.out.println("The end..");

	}// end generateInitialChromeSet

	public static List<String> generateBestChromeTraditional() {

		System.out.println("[BasicGA] starting..");
		int indexOne = -1;
		int indexTwo = -1;
		double temp = 2;

		for (int i = 0; i < aSetOfMapping.size(); i++) {
			// Select the best
			if (evaluations.get(i) < temp) {
				temp = evaluations.get(i);
				indexOne = i;
			}
		}
		temp = 2;
		for (int i = 0; i < aSetOfMapping.size(); i++) {
			// Select the second best
			if (evaluations.get(i) < temp && (i != indexOne)) {
				temp = evaluations.get(i);
				indexTwo = i;
			}
		}
		System.out.println("Two best chromes are selected.");
		System.out.println("1-th chrome = " + indexOne + ", e = "
				+ evaluations.get(indexOne) + " ..");
		System.out.println("2-nd chrome = " + indexTwo + ", e = "
				+ evaluations.get(indexTwo) + " ..");

		// System.out.println("== Start testing cross-over ==");
		// // Test ten million times to get statistical data
		// List<String> testChromeA = new ArrayList<String>();
		// List<String> testChromeB = new ArrayList<String>();
		// List<String> testChromeC = new ArrayList<String>();
		// testChromeA =
		// convertSwitchControllerFromMapToList(aSetOfMapping.get(indexOne));
		// testChromeB =
		// convertSwitchControllerFromMapToList(aSetOfMapping.get(indexTwo));
		// int goodCross=0;
		// int badCross=0;
		// int runs=1000;
		// for(int i=0; i<runs; i++){
		// testChromeC = crossOver(testChromeA, testChromeB);
		// if( (evaluateChrome(testChromeC)<evaluateChrome(testChromeA)) &&
		// (evaluateChrome(testChromeC)<evaluateChrome(testChromeB)) ){
		// goodCross++;
		// }else{
		// badCross++;
		// }
		// }

		List<String> tempA = new ArrayList<String>();
		List<String> tempB = new ArrayList<String>();
		List<String> tempC = new ArrayList<String>();
		List<String> tempD = new ArrayList<String>();
		tempA = convertSwitchControllerFromMapToList(aSetOfMapping
				.get(indexOne));
		tempB = convertSwitchControllerFromMapToList(aSetOfMapping
				.get(indexTwo));
		tempC = crossOver(tempA, tempB);
		double eA = evaluateChrome(tempA);
		double eB = evaluateChrome(tempB);
		double eC = evaluateChrome(tempC);
		double eD = 0.0;
		int count = 0;
		while (((eC > eA) || (eC > eB)) && (count < 300)) {
			if (eA > eB) {
				tempC = mutate(tempA, 0.4);
			} else {
				tempC = mutate(tempB, 0.4);
			}
			eC = evaluateChrome(tempC);
			count++;
		}
		// The enhanced crossover
		int count2 = 0;
		tempD = tempC;
		double eD1 = 2.0, eD2 = 2.0;
		while (count2 < 10000) {
			tempD = mutate(tempD, 0.7);
			eD2 = evaluateChrome(tempD);
			if (eD2 < eD1) {
				eD1 = eD2;
			}
			count2++;
		}
		if (eD1 < eC) {
			// System.out.println("A better result (after "+count2+" runs) eD1="+eD1);
			// globalE3.add(eD1);
		}

		return tempC;
	} // end generateBestChromeTraditional

	// GA utility
	public static List<String> mutate(List<String> chrome, double mutationRate) {
		List<String> ch = new ArrayList<String>();
		Random r = new Random();
		if ((mutationRate < 0.0) || (mutationRate > 1.0)) {
			System.out.println("[ERROR] mutationRate should be in [0,1]!! ");
			ch = chrome;
		} else {
			for (int i = 0; i < chrome.size(); i++) {
				boolean toDo = r.nextDouble() < mutationRate; // need further
																// testing
				if (toDo) {
					int ctrlID = (randInt(1, controllerList.size()) - 1);
					String ctrlIP = controllerList.get(ctrlID);
					ch.add(ctrlIP);
				} else {
					ch.add(chrome.get(i));
				}

			}
		}
		return ch;
	}

	public static List<String> crossOver(List<String> chromeA,
			List<String> chromeB) {
		List<String> chrome = new ArrayList<String>();
		int c = 0;
		for (int i = 0; i < chromeA.size(); i++) {
			c = randInt(0, 1);
			if (c == 0) {
				chrome.add(chromeA.get(i));
			} else if (c == 1) {
				chrome.add(chromeB.get(i));
			}
		}
		// System.out.println("Sizes: chromeA="+chromeA.size()+" chromeB="+chromeB.size()+" chromeC="+chrome.size()+" ..");
		if ((chromeA.size() == chrome.size())
				&& (chromeB.size() == chrome.size())) {
			// System.out.println("cross-over successful..");
		} else {
			System.out.println("ERROR: Cross-over error!!");
		}
		return chrome;
	}

	public static List<Map<String, String>> crossoverAndMutation(
			List<Map<String, String>> inputList, int maxRound, double rate) {
		List<Map<String, String>> returningList = new ArrayList<Map<String, String>>();
		// System.out.println("inputList size="+inputList.size());
		int sizeOfList = inputList.size();
		int maxMutation = maxRound;
		double mutationRate = rate;
		int mutaCount; // note count while mutating

		for (int i = 0; i < (sizeOfList - 1); i++) {
			for (int j = (i + 1); j < sizeOfList; j++) {
				// System.out.println("[c&m] cross two chromes: i="+i+" j="+j);
				List<String> a = new ArrayList<String>();
				List<String> b = new ArrayList<String>();
				List<String> c = new ArrayList<String>();
				a = convertSwitchControllerFromMapToList(inputList.get(i));
				b = convertSwitchControllerFromMapToList(inputList.get(j));
				c = crossOver(a, b);
				// System.out.println("Evaluation: a="+evaluateChrome(a)+", b="+evaluateChrome(b)+", c="+evaluateChrome(c));

				mutaCount = 0;
				while ((evaluateChrome(c) > evaluateChrome(a))
						&& (evaluateChrome(c) > evaluateChrome(b))
						&& (mutaCount < maxMutation)) {

					if (evaluateChrome(a) > evaluateChrome(b))
						c = mutate(a, mutationRate);
					else
						c = mutate(b, mutationRate);

					if (evaluateChrome(c) > evaluateChrome(a)
							|| evaluateChrome(c) > evaluateChrome(b))
						break;
					else
						mutaCount++;

				}
				// System.out.println("[c&m] mutations="+mutaCount);
				returningList.add(convertSwConMappingFromListToMap(c));

			} // end for j
		} // end for i

		return returningList;
	}

	public static int chooseBestSelectionSize(int size) {
		if (size < 6) {
			System.out.println("[WARNING] Selection size too small (<5) !!");
			return 1;
		} else {
			int i = 1;
			// from equation: max k, so that k+C(k,2) <= n
			while (i * (i + 1) <= (size * 2)) {
				i++;
			}
			return (i - 1);
		}

	}

	public static List<Integer> pickNRandom(List<Integer> list, int n) {
		List<Integer> copy = new ArrayList<Integer>(list);
		Collections.shuffle(copy);
		return copy.subList(0, n);
	}

	public static List<Map<String, String>> tournamentSelection(
			List<Map<String, String>> inputList) {
		List<Map<String, String>> returningList = new ArrayList<Map<String, String>>();
		List<List<String>> tempList = new ArrayList<List<String>>();
		List<Double> eval = new ArrayList<Double>();
		List<Integer> IDs = new ArrayList<Integer>();
		int sizeA = chooseBestSelectionSize(inputList.size());
		int sizeB = inputList.size() / 2;
		// first of all, convert inputs from Maps to Lists
		for (int i = 0; i < inputList.size(); i++) {
			tempList.add(convertSwitchControllerFromMapToList(inputList.get(i)));
			eval.add(evaluateChrome(tempList.get(i)));
			IDs.add(i);
		}
		// do selection
		for (int i = 0; i < sizeA; i++) {
			double eValue = 2.0;
			int bestIndex = -1;
			List<Integer> tempIDs = pickNRandom(IDs, sizeB);
			// retrieve the best one
			for (int j = 0; j < sizeB; j++) {
				int index = tempIDs.get(j);
				if (eval.get(index) < eValue)
					bestIndex = j;
			}
			returningList.add(inputList.get(bestIndex));
		}
		return returningList;
	}

	public static List<String> testGA(int switches, int controllers,
			int initialChromeSetSize, int numberOfGeneration, int maxMutation,
			double mutationRate) {

		System.out.println(" === Start G.A. testing ===");
		int numGen = numberOfGeneration;
		System.out.println("numGeneration=" + numGen);
		int switchNumber = switches;
		int controllerNumber = controllers;
		int initSize = initialChromeSetSize;
		double bestValue = Double.MAX_VALUE;
		List<String> bestChrome = new ArrayList<String>();
		List<Map<String, String>> initChromeSet = new ArrayList<Map<String, String>>(
				aSetOfMapping);
		List<Map<String, String>> tempChromeSet = new ArrayList<Map<String, String>>(
				initChromeSet);

		// Each run simulate a generation ( including a complete cycle of
		// selection, cross-over and mutation )
		for (int i = 0; i < numGen; i++) {

			List<Map<String, String>> tournamentSet = new ArrayList<Map<String, String>>();
			List<Map<String, String>> cnmSet = new ArrayList<Map<String, String>>();
			List<Map<String, String>> supplementSet = new ArrayList<Map<String, String>>();
			List<Map<String, String>> newChromeSet = new ArrayList<Map<String, String>>();
			int totalSize = initChromeSet.size();
			int tSize = 0;
			int cSize = 0;
			int sSize = 0;

			// Compute "tournament set", "cross&mutation set", and
			// "supplement set", respectively
			tournamentSet = tournamentSelection(tempChromeSet);
			tSize = tournamentSet.size();
			cnmSet = crossoverAndMutation(tournamentSet, maxMutation,
					mutationRate);
			cSize = cnmSet.size();
			sSize = totalSize - tSize - cSize;
			supplementSet = selectBestChromes(tournamentSet,
					evaluateChromeSet(tournamentSet), sSize);

			// Gather all the above sets, output a chrome set
			for (int j = 0; j < tournamentSet.size(); j++) {
				newChromeSet.add(tournamentSet.get(j));
			}
			for (int j = 0; j < cnmSet.size(); j++) {
				newChromeSet.add(cnmSet.get(j));
			}
			for (int j = 0; j < supplementSet.size(); j++) {
				newChromeSet.add(supplementSet.get(j));
			}

			tempChromeSet = newChromeSet;

		}

		// bestChrome <- select the best one from tempChromeSet
		List<Double> eValues = evaluateChromeSet(tempChromeSet);
		for (int i = 0; i < tempChromeSet.size(); i++) {
			if (eValues.get(i) < bestValue) {
				bestValue = eValues.get(i);
				bestChrome = convertSwitchControllerFromMapToList(tempChromeSet
						.get(i));
			}
		}

		// System.out.println("The g.a. has run "+numGen+" generations..");
		// System.out.println("The G.A. yield the following result:");
		// System.out.println("The best fitness value: "+bestValue);
		// for(int i=0; i<switchNumber; i++){
		// System.out.println("The corresponding chrome["+i+"]= "+bestChrome.get(i));
		// }
		// System.out.println(" === Finish G.A. testing === ");

		return bestChrome;
	}

	// BruteForce solution
	public static List<String> initializeChromeWithAllTheSameGene(String con,
			int size) {
		List<String> chrome = new ArrayList<String>();
		for (int i = 0; i < size; i++)
			chrome.add(con);
		if (chrome.size() != size)
			System.out.println("[Error] chrome size unmatched..");
		return chrome;
	}

	public static List<String> increaseChromeIndexByOne2(List<String> ch,
			int controllersize) {
		List<String> result = new ArrayList<>();
		Iterator<String> iter = ch.iterator();
		String first = iter.next();
		int controllerid = invertedControllerList.get(first) + 1;
		if (controllerid < controllersize) // no carry occurs for the first gene
		{
			result.add(controllerList.get(controllerid));
			for (; iter.hasNext();) {
				String element = iter.next();
				result.add(element);
			}
		} else // carry occurs for the first gene
		{
			result.add(controllerList.get(0));
			boolean ca = true;
			for (; iter.hasNext();) {
				String element = iter.next();
				if (ca == true) {
					int newIndex = invertedControllerList.get(element) + 1;
					if (newIndex < controllersize) {
						result.add(controllerList.get(newIndex));
						ca = false;
					} else {
						result.add(controllerList.get(0));
						ca = true;
					}

				} else {
					result.add(element);
				}
			}
		}

		return result;

	}

	public static List<String> increaseChromeIndexByOne(List<String> ch) {

		String oldCon = ch.get(ch.size() - 1); // get the last switch's
												// assignment
		int newConIndex = 0;
		if ((invertedControllerList.get(oldCon) + 1) >= controllerList.size()) {
			newConIndex = (invertedControllerList.get(oldCon) + 1)
					% (controllerList.size());
		} else {
			newConIndex = (invertedControllerList.get(oldCon) + 1);
		}
		String newCon = controllerList.get(newConIndex);

		return ch;
	}

	public static List<String> bruteForceSolution(int switches, int controllers) {
		// System.out.println("=== Start testing brute-force ===");
		int switchNumber = switches;
		int controllerNumber = controllers;
		boolean breakWhile = false;
		double eValue = 0.0;
		int counter = 0;
		// If you need to record the chromes, use the following declaration. it
		// works
		// List<List<String>> maps = new ArrayList<List<String>>();
		// List<Double> e = new ArrayList<Double>(); // evaluations

		// Start
		double bestE = Double.MAX_VALUE; // bestE's initially assigned to be
											// very large
		String lastController = controllerList.get(controllerList.size() - 1); // the
																				// last
																				// controller
																				// to
																				// iterate

		List<String> bestChrome = null;
		List<String> previousChrome = null;
		while (true) {
			List<String> ch = new ArrayList<String>(); // chromes
			if (counter == 0) {
				ch = initializeChromeWithAllTheSameGene(controllerList.get(0),
						switchNumber);
			} else {
				ch = increaseChromeIndexByOne2(previousChrome, controllerNumber); // increase
																					// the
																					// previous
																					// chrome
																					// by
																					// one
			}
			previousChrome = new ArrayList<String>(ch);
			eValue = evaluateChrome(ch);
			if (bestE > eValue) {
				// System.out.println("A better fitness: " +
				// Double.toString(eValue));
				// System.out.println("A better chrome:  " + ch);
				bestE = eValue;
				bestChrome = ch;
			}
			counter++;

			// check weather to break
			breakWhile = true;
			for (int i = 0; i < ch.size(); i++) {
				if (ch.get(i).equalsIgnoreCase(lastController) != true) {
					breakWhile = false;
				}
			}
			if (breakWhile)
				break;

		}
		// System.out.println("the best value:"+bestE);
		// globalE1.add(bestE);
		return bestChrome;
	}

	// FCFM
	public static String getControllerOfLeastLoading(
			Map<String, Double> controllerLoadings, int ranking) {

		String name = null;
		Set<String> checkedControllerSet = new HashSet<>();

		if ((ranking < 1) || (ranking > (controllerList.size() + 1)))
			System.out
					.println("[ERROR] getControllerLoadings index out of bound!(rank="
							+ ranking + ")");

		for (int i = 0; i < controllerList.size(); i++) {

			double least = 1.0;
			for (Map.Entry<String, Double> entry : controllerLoadings
					.entrySet()) {

				if (checkedControllerSet.contains(entry.getKey())) {
					continue;
				}

				if (entry.getValue() < least) {
					least = entry.getValue();
					name = entry.getKey();
				}
			}

			checkedControllerSet.add(name);

			if (i == (ranking - 1))
				break;

		}

		return name;
	}

	public static boolean FCFMIsGoodAssignment(
			double switchLoadingContribution, double controllerCurrentLoading,
			double loadCeiling, double beta) {
		if ((switchLoadingContribution + controllerCurrentLoading) <= (loadCeiling))
			return true;
		else
			return false;
	}

	public static List<String> testFCFM() {
		// System.out.println("=== Start testing FCFM ===");
		int numSwitch = switchList.size();
		int numCtrl = controllerList.size();
		double swCost = 0.0;
		double beta = 0.7;

		List<String> sws = new ArrayList<String>(switchList);
		List<Double> swLoads = new ArrayList<Double>(SwLoadContributions);
		Map<String, Integer> invertedSws = new HashMap<String, Integer>(
				invertedSwitchList);

		List<String> ctrls = new ArrayList<String>(controllerList);
		Map<String, Integer> invertedCtrl = new HashMap<String, Integer>(
				invertedControllerList);
		Map<String, Double> ctrlLoads = new HashMap<String, Double>(
				controllerLoadings);
		Map<String, Double> dists = new HashMap<String, Double>(
				switchControllerDistances);

		List<List<Double>> swCostList = new ArrayList<List<Double>>();
		List<String> outputMapping = new ArrayList<String>();
		Map<String, Double> dynamicControllerLoadings = new HashMap<String, Double>(
				controllerLoadings);
		Map<String, Double> controllerLoadCeiling = new HashMap<String, Double>();
		// System.out.println("Controller loadings: "+controllerLoadings);

		// Calculate controller loading upper bound for this time
		for (Map.Entry<String, Double> entry : controllerLoadings.entrySet()) {
			controllerLoadCeiling.put(entry.getKey(),
					(entry.getValue() + entry.getValue() * beta));
		}
		// System.out.println("Controller load ceilings: "+controllerLoadCeiling);

		// Do switch migrations (each run assigns a switch to a controller)
		for (int i = 0; i < numSwitch; i++) {
			List<Double> costList = new ArrayList<Double>();

			// Calculate "switch cost" for switch i (current switch)
			for (int j = 0; j < numCtrl; j++) {
				double distij = dists.get(ctrls.get(j));
				double pktinij = swLoads.get(i);
				double cost = distij * pktinij;
				costList.add(cost);
			}

			// Sort all the above costs
			List<Double> copiedList = new ArrayList<>(costList);
			Collections.sort(copiedList);

			// Preparation
			boolean[] checkArray = new boolean[costList.size()];
			boolean switchBeMigrated = false;
			for (int j = 0; j < costList.size(); j++) {
				checkArray[j] = true;
			}

			// Do a migration (each run tries to migrate switch i to controller
			// j)
			for (int j = 0; j < copiedList.size(); j++) {
				boolean toNextSwitch = false;

				// get cost(switch i, controller j)
				double value = copiedList.get(j);

				// search for "cost" in the original List, and retrieve
				// controller ID of such "cost"
				for (int k = 0; k < costList.size(); k++) {
					if ((checkArray[k] == true) && (value == costList.get(k))) {
						checkArray[k] = false;
						String ctrlID = ctrls.get(k);
						String swID = sws.get(i);

						if (FCFMIsGoodAssignment(swLoads.get(i),
								dynamicControllerLoadings.get(ctrlID),
								controllerLoadCeiling.get(ctrlID), 0.7)) {
							// assign switch to controller
							outputMapping.add(ctrlID);
							switchBeMigrated = true;
							toNextSwitch = true;
							double newLoad = dynamicControllerLoadings
									.get(ctrlID) + swLoads.get(i);
							dynamicControllerLoadings.put(ctrlID, (newLoad));
							// System.out.println("Adding switch loading "+swLoads.get(i)+" to controller "+ctrlID);
							// System.out.println("dynamic controller loadings update:"+dynamicControllerLoadings+"\n");
							break;
						}
					}
				}

				// if migrated, do the next switch
				if (toNextSwitch)
					break;
			}

			// if there is not a good controller to migrate, migrate the switch
			// i to the least loaded controller
			if (!switchBeMigrated) {
				// get the least loaded controller
				String ctrlID = getControllerOfLeastLoading(ctrlLoads, 1);
				outputMapping.add(ctrlID);
			}

			// System.out.println("A switch migration is perfomred for switch["+i+"]..");

		} // end for

		// System.out.println("FCFM's output: "+outputMapping);
		// System.out.println("FCFM's evaluation value="+evaluateChrome(outputMapping));
		// System.out.println("=== Finish testing FCFM ===");
		return outputMapping;
	}

	// NSNC
	public static double getMaxFromMap(Map<String, Double> m) {
		double max = -1.0;
		for (Map.Entry<String, Double> entry : m.entrySet()) {
			if (entry.getValue() > max)
				max = entry.getValue();
		}
		return max;
	}

	public static double calculateNSNCScore(String con, String sw, double w1,
			double w2) {
		double maxLoad = getMaxFromMap(controllerLoadings);
		double maxDist = getMaxFromMap(switchControllerDistances);
		double swLoad = SwLoadContributions.get(invertedSwitchList.get(sw));
		double swConDist = switchControllerDistances.get(con);
		double score = w1 * (swLoad / maxLoad) + w2 * (swConDist / maxDist);
		return score;
	}

	public static List<String> testNSNC() {

		List<String> output = new ArrayList<String>();
		Map<String, String> aMapping = new HashMap<String, String>();
		List<Double> swLoad = new ArrayList<Double>(SwLoadContributions);
		List<Integer> orderedSwitch = new ArrayList<Integer>();

		// Sort switch loadings
		Collections.sort(swLoad);

		// Construct "invertedSortedSwitchList" that helps to find the switch by
		// its loading
		for (int i = 0; i < swLoad.size(); i++) {
			for (int j = 0; j < SwLoadContributions.size(); j++) {
				if (SwLoadContributions.get(j) == swLoad.get(i))
					orderedSwitch.add(j);
			}
		}
		if (orderedSwitch.size() != switchList.size()) {
			System.out
					.println("[ERROR] Sorting switch loadings error: size not match..!");
		}

		// Each round yield a switch-controller mapping
		for (int i = 0; i < orderedSwitch.size(); i++) {
			int bestControllerIndex = -1;
			double bestScore = Double.MAX_VALUE;
			String switchID = switchList.get(orderedSwitch.get(i));
			// Each round checks a controller (an switch-controller assignment)
			for (int j = 0; j < controllerList.size(); j++) {
				double cScore = calculateNSNCScore(controllerList.get(j),
						switchID, 0.5, 0.5);
				if (cScore < bestScore) {
					bestScore = cScore;
					bestControllerIndex = j;
				}
			}
			aMapping.put(switchID, controllerList.get(bestControllerIndex));
		}
		if (aMapping.size() != switchList.size()) {
			System.out
					.println("[ERROR] Constructing mapping error: size not match..!");
		}

		// transform the format from Map<> to List<>
		output = convertSwitchControllerFromMapToList(aMapping);
		return output;
	}

	// SVVR
	public static List<String> testSVVR() {
		List<String> output = new ArrayList<String>();
		Map<String, Double> activeConLoads = new HashMap<String, Double>(
				controllerLoadings);
		System.out.println("activeConLoads=" + activeConLoads);
		for (int i = 0; i < switchList.size(); i++) {
			double minLoading = Double.MAX_VALUE;
			String targetCtrl = null;
			for (Map.Entry<String, Double> entry : activeConLoads.entrySet()) {
				if (entry.getValue() < minLoading) {
					minLoading = entry.getValue();
					targetCtrl = entry.getKey();
					System.out.println("Got minLoad=" + minLoading
							+ " targetCon=" + targetCtrl);
				}
			}
			System.out.println("activeControllerLoading(" + targetCtrl + "): "
					+ activeConLoads.get(targetCtrl));
			System.out.println("switch load distribution[" + i + "]= "
					+ SwLoadContributions.get(i));
			double newload = activeConLoads.get(targetCtrl)
					+ SwLoadContributions.get(i);
			activeConLoads.put(targetCtrl, newload);
			output.add(targetCtrl);
		}

		return output;
	}

	// PPF
	public static List<String> testPPF() {
		List<String> output = new ArrayList<String>();
		int i = 0;
		int NumCon = controllerList.size() / 2;
		if (NumCon < 1)
			NumCon = 1;
		System.out.println("NumCon=" + NumCon);
		Map<String, Double> copiedCtrlDist = new HashMap<String, Double>(
				switchControllerDistances);
		System.out.println("switch con dist:" + copiedCtrlDist);
		List<String> tkoControllers = new ArrayList<String>();

		for (int j = 0; j < NumCon; j++) {
			double minDist = Double.MAX_VALUE;
			String selectedCon = null;
			for (Map.Entry<String, Double> entry : copiedCtrlDist.entrySet()) {
				if (entry.getValue() < minDist) {
					minDist = entry.getValue();
					selectedCon = entry.getKey();
				}
			}
			tkoControllers.add(selectedCon);
		}
		System.out.println("tkoControllers=" + tkoControllers);

		for (int k = 0; k < switchList.size(); k++) {
			int indexToGet = (k % tkoControllers.size());
			// System.out.println("indexToGet="+indexToGet);
			String conToAdd = tkoControllers.get(indexToGet);
			// System.out.println("conToAdd="+conToAdd);
			output.add(conToAdd);
		}
		System.out.println("OUTPUT=" + output);
		return output;
	}

	// Other tests
	public static void testFunctionA() {
		/*******
		 * This function enables you to test the ArrayList feature by
		 * reassigning List objects
		 ******/
		double bestE = Double.MAX_VALUE;
		int round = 1000;
		System.out.println("[INFO] Start functional test..");
		List<String> tempA = null;
		List<String> previousChrome = null;
		System.out.println("Initialing tempA:" + tempA);
		System.out.println("Initialing previousChrome:" + previousChrome);
		int counter = 0;
		double eValue = 0.0;

		while (true) {
			List<String> ch = new ArrayList<String>(); // chromes
			System.out.println("In " + counter
					+ "-th round, \nA new list created: ch=" + ch);
			if (counter == 0) {
				ch = initializeChromeWithAllTheSameGene(controllerList.get(0),
						10);
				System.out.println("*c=0, ch be assigned:" + ch);
			} else {
				ch = increaseChromeIndexByOne2(previousChrome,
						controllerList.size());
				System.out.println("*c=" + counter
						+ ", previousChrome be checked:" + previousChrome);
				System.out.println("*c=" + counter + ", ch be assigned:" + ch);
			}
			previousChrome = new ArrayList<String>(ch);
			System.out.println("c=" + counter + ", pCh be assigned:"
					+ previousChrome);
			eValue = evaluateChrome(ch);
			if (bestE > eValue) {
				bestE = eValue;
				tempA = ch;
				System.out.println("A better fitness: "
						+ Double.toString(eValue));
				System.out.println("A better chrome: " + tempA);
			}
			counter++;
			if (counter == round)
				break;
		}
		System.out.println("[INFO] Finish functional test..");
	}

}

// Thread.currentThread();
// try { Thread.sleep(1000); } catch (InterruptedException e) {
// e.printStackTrace(); }

// System.out.println("-- [Test Rand functionality] --");
// System.out.println("generate numbers betwen 0 and 1");
// int randCount=0;
// int rand_k=0;
// int rand_limit=2000000;
// for (int i=0; i<rand_limit; i++){
// //System.out.println("rand(0): "+(rand.nextInt(3+1)));
// rand_k=randInt(0,1);
// if(rand_k==0)
// randCount++;
// }
// System.out.println("0 count="+randCount+" 1 count="+(rand_limit-randCount));
// System.out.println("-- [Finish test random number] --");
//
// System.out.println("-- [Test chrome evaluation functionality] --");

// switchList.add("00:00:00:00:00:00:00:01");
// switchList.add("00:00:00:00:00:00:00:02");
// switchList.add("00:00:00:00:00:00:00:03");
// switchList.add("00:00:00:00:00:00:00:04");
// switchList.add("00:00:00:00:00:00:00:05");
// for(int i=0;i<5;i++)
// System.out.println("switchList["+i+"]:"+ switchList.get(i));
//
// invertedSwitchList.put("00:00:00:00:00:00:00:01", 0);
// invertedSwitchList.put("00:00:00:00:00:00:00:02", 1);
// invertedSwitchList.put("00:00:00:00:00:00:00:03", 2);
// invertedSwitchList.put("00:00:00:00:00:00:00:04", 3);
// invertedSwitchList.put("00:00:00:00:00:00:00:05", 4);
// for (Map.Entry<String, Integer> entry000 : invertedSwitchList.entrySet())
// System.out.println("invertedwitchList " + entry000.getKey() + "/" +
// entry000.getValue());
//
// SwLoadContributions.add(0, 0.2);
// SwLoadContributions.add(1, 0.1);
// SwLoadContributions.add(2, 0.2);
// SwLoadContributions.add(3, 0.1);
// SwLoadContributions.add(4, 0.3);
// for(int i=0;i<5;i++)
// System.out.println("swLoadContributions["+i+"]:"+
// SwLoadContributions.get(i));
//
// switchControllerDistances.put("192.168.17.144", 4.0);
// switchControllerDistances.put("192.168.17.145", 7.0);
// switchControllerDistances.put("192.168.17.146", 10.0);
// for (Map.Entry<String, Double> entry001 :
// switchControllerDistances.entrySet())
// System.out.println("switchControllerDistances " + entry001.getKey() + "/" +
// entry001.getValue());
//
// controllerLoadings.put("192.168.17.144", 0.5);
// controllerLoadings.put("192.168.17.145", 0.2);
// controllerLoadings.put("192.168.17.146", 0.7);
// for (Map.Entry<String, Double> entry002 : controllerLoadings.entrySet())
// System.out.println("controllerLoadings " + entry002.getKey() + "/" +
// entry002.getValue());
//
// controllerList.add("192.168.17.144");
// controllerList.add("192.168.17.145");
// controllerList.add("192.168.17.146");
// for(int i=0;i<3;i++)
// System.out.println("controllerList["+i+"]:"+ controllerList.get(i));
//
// invertedControllerList.put("192.168.17.144", 0);
// invertedControllerList.put("192.168.17.145", 1);
// invertedControllerList.put("192.168.17.146", 2);
// for (Map.Entry<String, Integer> entry003 : invertedControllerList.entrySet())
// System.out.println("invertedControllerList " + entry003.getKey() + "/" +
// entry003.getValue());
//
// System.out.println("========");
// System.out.println("+++Now Let's begin testing sample_1");
//
// List<String> c = new ArrayList<String>();
// c.add("192.168.17.144");
// c.add("192.168.17.144");
// c.add("192.168.17.145");
// c.add("192.168.17.145");
// c.add("192.168.17.146");
// for(int i=0;i<5;i++)
// System.out.println("a chrome["+i+"]:" + c.get(i));
// System.out.println("e1=" + evaluateChrome(c));
// System.out.println("========");
//
// System.out.println("========");
// System.out.println("+++Now Let's begin testing sample_2");
// List<String> c2 = new ArrayList<String>();
// c2.add("192.168.17.144");
// c2.add("192.168.17.144");
// c2.add("192.168.17.145");
// c2.add("192.168.17.145");
// c2.add("192.168.17.145");
// for(int i=0;i<5;i++)
// System.out.println("a chrome["+i+"]:" + c2.get(i));
// System.out.println("e2=" + evaluateChrome(c2));
// System.out.println("========");
//
// System.out.println("-- [Finish chrome evaluation test] --");
//
// System.out.println("-- [Test initial set generation] --");
