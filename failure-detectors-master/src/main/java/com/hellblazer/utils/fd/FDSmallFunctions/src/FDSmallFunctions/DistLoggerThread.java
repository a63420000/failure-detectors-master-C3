package FDSmallFunctions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DistLoggerThread extends Thread{
	
	public String pingIP;
	private boolean active;
	NIB nib;
	
	public DistLoggerThread(String ip, NIB n){
		active = true;
		pingIP = ip;
		nib = n;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public void run(){
		while(isActive()){

			// [notation needed]
			Runtime r = Runtime.getRuntime();

	        // ping --> parse --> store to NIB
			try {
				// [notation needed]
	        	Process p = r.exec("ping -c 1 "+pingIP);
				BufferedReader tempBuf = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String aTempLine;
		        if ((aTempLine = tempBuf.readLine()) != null) { // retrieved "PING 8.8.8.8 (8.8.8.8) 56(84) bytes of data."
		        	if ((aTempLine = tempBuf.readLine()) != null) { // retrieved "64 bytes from 8.8.8.8: icmp_seq=1 ttl=128 time=6.14 ms"
				        String[] tempA = aTempLine.split("time=");
				        if(tempA.length!=2){
				        	System.out.println("Not ping-able..");
				        }
				        String[] tempB = tempA[1].split(" ");
				        String dist = tempB[0];
				        // System.out.println("delay(in ms)="+dist);
				        nib.setDistance(pingIP, Double.parseDouble(dist));
			        }
			        else{
			        	System.out.println("Ping not responding..");
			        }
		        }
		        tempBuf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

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
