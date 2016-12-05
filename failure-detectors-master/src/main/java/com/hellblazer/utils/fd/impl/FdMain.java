package com.hellblazer.utils.fd.impl;


import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FdMain {

	public static EndNode node1 = new EndNode("192.168.10.137", 5554);
	public static EndNode node2 = new EndNode("192.168.10.138", 5555);
	public static EndNode node3 = new EndNode("192.168.10.139", 5556);
	public static EndNode node4 = new EndNode("192.168.10.140", 5557);
	public static Map<Integer,EndNode> EndnodeList = new ConcurrentHashMap<Integer,EndNode>();
	public static Map<Integer,Integer> SendportList = new ConcurrentHashMap<Integer,Integer>();
        public static DatagramChannel channel;
	public static int sdflag = 0;
	public static int count = 0;
	
	public static List<String> tempResult = new ArrayList<String>();
	
	public static void main(String[] args) {
	 
		System.out.println("Hello World");
		//int count = 0;
		EndNode.gossipList.put(5554, 0);
		EndNode.gossipList.put(5555, 0);
		EndNode.gossipList.put(5556, 0);
		EndNode.gossipList.put(5557, 0);
		EndnodeList.put(5554,node1);
		EndnodeList.put(5555,node2);
		EndnodeList.put(5556,node3);
		EndnodeList.put(5557,node4);
		SendportList.put(5500, 0);
		SendportList.put(5501, 1);
		SendportList.put(5502, 2);
		SendportList.put(5503, 3);
		

		multiThreadTestMain test = new multiThreadTestMain(tempResult);
		test.start();
		//System.out.println(tempResult);
		
		
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				EndNode.suspectMtx[i][j] = 0;
			}
		}
		for(int i=0; i<4; i++)
		{
			EndNode.fctime[i] = 0;
			EndNode.fcflag[i] = 0;
		}

		for(int i=0; i<100; i++)
		{
			EndNode.ft[i] = 0;
			//EndNode.ff[i] = 0;
			EndNode.mc[i] = 0;
		}		

		try {
                        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
			Thread.sleep(10000);
			channel = DatagramChannel.open();
			DatagramSocket rvsocket = channel.socket();
			SocketAddress addr = new InetSocketAddress(5556);
			rvsocket.bind(addr);
			channel.configureBlocking(false);
		} catch (Exception e) { e.printStackTrace(); }

		while(true)
		{
			try {
/*
				String[] cmd1 = {"sh", "-c", "ps aux | grep karaf | grep Rl+"};
				Process pl = Runtime.getRuntime().exec(cmd1);
				pl.waitFor();
				String l = "";
				BufferedReader p_in = new BufferedReader(new InputStreamReader(pl.getInputStream()));
				l = p_in.readLine();
				
				String[] cmd2 = {"sh", "-c", "ps aux | grep karaf | grep Sl+"};
				Process pl2 = Runtime.getRuntime().exec(cmd2);
				pl2.waitFor();
				String l2 = "";
				BufferedReader p_in2 = new BufferedReader(new InputStreamReader(pl2.getInputStream()));
				l2 = p_in2.readLine();
				
				if((l.contains("java")) || (l2.contains("java")))
				{
*/
					sdflag = 0;
					count++;
					System.out.println("Round: "+count);
					int hc = EndNode.gossipList.get(5556);
					hc++;
					EndNode.gossipList.put(5556, hc);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556)+1)%EndNode.aliveList.size())).run(tempResult);
					//EndNode.gossipList.put(5556, hc);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556)+2)%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					sdflag = 1;
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					EndnodeList.get(EndNode.aliveList.get((EndNode.aliveList.indexOf(5556))%EndNode.aliveList.size())).run(tempResult);
					Thread.sleep(100);
					
/*
				}
				else
				{
					System.out.println("---ODL Karaf Dead---");
					//Runtime.getRuntime().exec("/home/skysky/karaf");
					//System.out.println("---Restart ODL Karaf---");
				}
				p_in.close();
				p_in2.close();
*/
				//for(int i=0; i<4; i++)
				//{
				//	for(int j=0 ;j<4; j++)
				//	{
				//		System.out.println(EndNode.suspectMtx[i][j]);
				//	}
				//}
			} catch (Exception e) { e.printStackTrace(); }
		}
					
	}
	 
}

class ShutdownThread extends Thread {
	public ShutdownThread() {
		super();		
	}
	
	public void run() {
		for(int i=0; i<EndNode.index; i++)
		{
			System.out.println("Detected Time "+(i+1)+": "+EndNode.ft[i]);
		}
		for(int i=0; i<EndNode.index; i++)
		{
			System.out.println("Mistakes "+(i+1)+": "+EndNode.mc[i]);
		}
		System.out.println("Current Time: "+System.currentTimeMillis());		
	}
		
}
