package fdThreadTest;

public class EraseThread extends Thread {
	   private boolean active;
	   private String mask;


	   public EraseThread() {
	       this('*');
	   }

	   public EraseThread(char maskChar) {
	       active = true;
	       //mask = "\010" + maskChar;
	       mask = "x" + maskChar;
	   }

	   public void setActive(boolean active) {
	       this.active = active;
	   }

	   public boolean isActive() {
	       return active;
	   }

	   //重新定義 run 方法
	   public void run() {
	       
		   System.out.println("getting into run();");
	       
		   while(isActive()) {
	           
	    	   System.out.print(mask);
	           try {
	               // sleep 方法會丟出 InterruptedException 例外
	               Thread.currentThread().sleep(500);
	           }
	           catch(InterruptedException e) {
	               e.printStackTrace();
	           }
	           
	       } // end while
	   } // end run
	   
	}