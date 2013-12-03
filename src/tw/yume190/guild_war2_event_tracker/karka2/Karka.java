package tw.yume190.guild_war2_event_tracker.karka2;

import java.util.HashMap;

public class Karka {
	
	HashMap<String, Integer> stateMap = new HashMap<String, Integer>();
	
	String mission1 = "Recapture the settlement from crazed wildlife.";
	String mission2 = "Defend the repair worker.";
	String mission3 = "Clear the karka and their eggs to rebuild Camp Karka.";
	String mission4 = "Reclaim the settlement by burning out the karka nests.";
	
	Karka(){
		setUpStateMap();
	}

	private void setUpStateMap() {
		stateMap.put("Active",0);
		stateMap.put("Inactive",1);
		stateMap.put("Success",2);
		stateMap.put("Fail",3);
		stateMap.put("Warmup",4);
		stateMap.put("Preparation",5);
	}
	
	public int getSuccess(int campState){
		int result = 0;
		switch(campState){
		case 2:
		case 4:
			result = 1;
		case 1:
		case 3:
		case 5:
		case 0:
		default:
			break;	
		}
		return result;
	}
}
