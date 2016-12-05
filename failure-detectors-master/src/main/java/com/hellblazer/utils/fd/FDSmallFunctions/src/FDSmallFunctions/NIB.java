package FDSmallFunctions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NIB {
	
	private String localhostIP;
	public double aDistance;
	public double loading;
	public Map<String,Double> distances = new HashMap<String,Double>();
	public Map<String,Double> loadings = new HashMap<String,Double>();
	public List<String> activeControllers = new ArrayList<String>();
	public Map<String,Double> switchLoadings = new HashMap<String,Double>();
	
	public NIB(){
		aDistance = 0;
		loading = 0;
	}
	
	
	public double getSwitchLoading(String switchID){
		return switchLoadings.get(switchID);
	}
	public void setSwitchLoading(String switchID, double loading){
		switchLoadings.put(switchID, loading);
	}
	public void setLocalIP(String ip){
		localhostIP = ip;
	}
	public String getLocalIP(){
		return localhostIP;
	}
	public double getDistance(String ip){
		return distances.get(ip);
	}
	public void setDistance(String ip, double dist){
		distances.put(ip, dist);
	}
	public Map<String,Double> printDistances(){
		return distances;
	}
	public double getLoading(String ip){
		return loadings.get(ip);
	}
	public void setLoading(String ip, double load){
		if(ip.equalsIgnoreCase("localhost"))
			loadings.put(getLocalIP(), load);
		else
			loadings.put(ip, load);
	}
	public Map<String,Double> printLoadings(){
		return loadings;
	}

	public int hasController(String ctrl){
		int index = -1;
		for(int i=0; i<activeControllers.size(); i++){
			if(ctrl.equalsIgnoreCase(activeControllers.get(i)))
				index = i;
		}
		return index;
	}
	public boolean enactiveController(String ctrl){
		if(hasController(ctrl)<0){
			activeControllers.add(ctrl);
			return true;
		}
		else{
			return false;
		}
	}
	public boolean deactiveController(String ctrl){
		int index = hasController(ctrl);
		if(index<0){
			System.out.println("There is no such controller to delete..");
			return false;
		}
		else{
			activeControllers.remove(index);
			return true;
		}
	}
	public List<String> getActiveControllerList(){
		return activeControllers;
	}


}

//for (Map.Entry<String, Double> entry : distances.entrySet())
//	System.out.println("controllerLoadings " + entry.getKey() + "/" + entry.getValue());
//
