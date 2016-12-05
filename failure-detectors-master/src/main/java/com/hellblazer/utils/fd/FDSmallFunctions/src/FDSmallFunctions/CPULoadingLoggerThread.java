package FDSmallFunctions;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;


public class CPULoadingLoggerThread extends Thread{

	
	public double loading;
	private boolean active;
	NIB nib;
	public int port;
	
	public CPULoadingLoggerThread(int pushToPort, NIB n){
		loading = 0.0;
		active = true;
		port = pushToPort;
		nib = n;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public double getAverageCPULoading(){
		double loading = 0.0;
		try { 
			Runtime r = Runtime.getRuntime();
	    	Process p = r.exec("top -b -n 1");
	    	BufferedReader tempBuf = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	String aLine=null;
	
	    	if ((aLine = tempBuf.readLine()) != null) { // retrieved "top - 11:38:37 up 20 days, 10:37,  4 users,  load average: 0.71, 0.75, 0.36"
	        	String[] tempA = aLine.split("  ");
	        	//System.out.println("yee  "+tempA[3]);
	        	String[] tempB = tempA[3].split(" ");
	        	String[] tempC = tempB[2].split(",");
	        	loading = Double.parseDouble(tempC[0]); 
	        }
	        else{
	        	System.out.println("Read top command Error: no data retrieved..");
	        }
		}
        catch (IOException e) {
			e.printStackTrace();
		}
		return loading;
	}
	public double getLiveCPULoading(String filePath){
		String fp = filePath;
		double loading = 0.0;
		if(filePath.equalsIgnoreCase("default")){
			fp = "/home/skysky/customTop.sh";
		}
		try {

			ProcessBuilder pb = new ProcessBuilder("sh", fp);
	    	pb.directory(new File("/home/skysky/"));
			BufferedReader tempBuf = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
			String aLine=null;
			
			// retrieved "top - 11:38:37 up 20 days, 10:37,  4 users,  load average: 0.71, 0.75, 0.36"
			if((aLine = tempBuf.readLine()) == null){
				System.out.println("Error!");
			}

			int count = 0;
			double sum = 0.0;
			while (((aLine = tempBuf.readLine()) != null) && (count<10)){  
				// System.out.println("aLine1="+aLine);
	        	String[] tempA = aLine.split(" ");
	        	double unNicedProcLoading = 0.0;
	        	double nicedProcLoading = 0.0;
	        	for(int i=0; i<10; i++){
		        	if(tempA[i].equalsIgnoreCase("us,")){
		        		System.out.println("tempA[i-1]:"+tempA[i-1]);
		        		if(tempA[i-1].contains(":")){
		        			String[] temp = tempA[i-1].split(":");
		        			unNicedProcLoading = Double.parseDouble(temp[1]);
		        			for(int j=0;j<30;j++){System.out.println("**"+j+j+j+j+j+"*****************************************");}
		        		}
		        		else{
		        			unNicedProcLoading = Double.parseDouble(tempA[i-1]);	
		        		}
		        		sum = sum + unNicedProcLoading;
		        	}
		        	if(tempA[i].equalsIgnoreCase("ni,")){
		        		nicedProcLoading = Double.parseDouble(tempA[i-1]);
		        		sum = sum + nicedProcLoading;
		        	}
		        	
	        	}
	        	// System.out.println("Iteration "+count+", sum="+sum+", unNicedProcLoading="+unNicedProcLoading+", nicedProcLoading="+nicedProcLoading );
				count++;
			}
			loading = sum/count;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loading;
	}
	
	public void infoPusher(List<String> IPs, int port, double loading){
		// System.out.println("[Check] IP list is as follows:");
		// for(int i=0; i<IPs.size(); i++)
		// 	System.out.println("[Check] The "+i+"th ip is: "+IPs.get(i));

		try {
			DatagramSocket socket = null;
			InetAddress IP = null;
			socket = new DatagramSocket();
			
			for(int i=0; i<IPs.size(); i++){
				String msg = "CPULoading/"+loading;
				// System.out.println("[Check] Message to send: "+msg);
				byte buffer[] = msg.getBytes();
				IP = InetAddress.getByName(IPs.get(i));
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, IP, port); 
				// System.out.println("[Check] Packet made as:");
				// System.out.println("[Check] ip="+IPs.get(i));
				// System.out.println("[Check] port="+port);
				// System.out.println("[Check] packet content:"+packet.getData());
				// System.out.println("[Check] Finish packet check.");
				socket.send(packet);
				// System.out.println("[Check] Packet sent..");
			} 
			socket.close();
		}
		catch (SocketException e1) { e1.printStackTrace(); }
		catch (UnknownHostException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public void run(){
		while(isActive()){

			// Store loading locally
			System.out.println("average loading="+getAverageCPULoading()); // get the average cpu utilization in recent one minute 
			//System.out.println("current loading="+getLiveCPULoading("default")); // get live cpu loading from default file path
			nib.setLoading("localhost", getAverageCPULoading());
			
			// Push the loading information to all the other controllers
			// System.out.println("Active controllers: "+nib.getActiveControllerList());
			///////////////////////////
			//enactivecontroller , ex put the number of controllers' IP
			nib.enactiveController("192.168.145.130");//localhost
			nib.setLoading("192.168.145.130", getAverageCPULoading());
			
			nib.enactiveController("192.168.145.131");//controller2
			nib.setLoading("192.168.145.131", 0);
			
			//nib.enactiveController("192.168.145.132");//controller3
			//nib.setLoading("192.168.145.132", 0);
			
			//nib.enactiveController("192.168.145.133");//controller4
			//nib.setLoading("192.168.145.133", 0);
			
			///////////////////////////
			List<String> iplist = nib.getActiveControllerList();
			infoPusher(iplist, port, getAverageCPULoading());
			// System.out.println("System waiting..");
			
			// stop for a while
	        try {
				Thread.currentThread();
				Thread.sleep(3000);
			} 
			catch (InterruptedException e) { 
				e.printStackTrace(); 
			}
	        
		}
	}


}
