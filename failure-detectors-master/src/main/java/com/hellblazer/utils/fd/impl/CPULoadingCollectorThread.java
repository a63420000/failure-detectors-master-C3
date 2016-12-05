package com.hellblazer.utils.fd.impl;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class CPULoadingCollectorThread extends Thread{
	// indication of liveness
	private boolean active;
	private int port;
	private InetAddress remoteIP;
	private String remoteIPString;
	NIB nib;

	public CPULoadingCollectorThread(int listenPort, NIB networkInformationBase){
		System.out.println("clcollector constructed..");
		active = true;
		port = listenPort;
		remoteIP = null;
		remoteIPString = "";
		nib = networkInformationBase;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isActive() {
		return active;
	}

	public void run() {
		
		// Declaration
    	final int SIZE = 4096;
    	byte buffer[] = new byte[SIZE];
    	DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    	DatagramSocket socket = null;
    	double loading = 0.0;
    	
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) { e.printStackTrace(); }
		///////while is here
	    while(isActive()){

	    	try {
	    		Thread.currentThread();
	    		Thread.sleep(3000);
				socket.receive(packet);
			} catch (IOException e) { 
				e.printStackTrace(); 
			} catch (InterruptedException e){
				e.printStackTrace();
			}
	    	/********************************************
	    	 * The following code has not yet been tested.
	    	 ********************************************/
	    	remoteIP = packet.getAddress();
	    	remoteIPString = remoteIP.getHostAddress();
	    	String content = (new String (packet.getData()));
	    	String[] data = content.split("/");
	    	//System.out.println("data[0]="+data[0]);
	    	//System.out.println("data[1]="+data[1]);
	    	//System.out.println("Here print the packet content:\n"+(new String(packet.getData()))+"\n----End of packet----");
	    	loading = Double.parseDouble(data[1]);
	    	nib.setLoading(remoteIPString, loading);
	    	System.out.println(remoteIPString+" "+nib.getLoading(remoteIPString));

		}
	    
        socket.close();
	}


}
