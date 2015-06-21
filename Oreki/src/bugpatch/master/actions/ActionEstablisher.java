package bugpatch.master.actions;

import java.util.HashMap;
import java.util.List;

public class ActionEstablisher {

	public HashMap<String, Integer> Touch = new HashMap<String, Integer>();
	public HashMap<String, Integer> Taste = new HashMap<String, Integer>();
	
	
	public ActionEstablisher() {}
	
	public int weighTouch(List<String> details){
		int Weight = 0;
		for(String detail : details){
			int AddingTouch = Touch.get(detail);
			Weight = Weight+AddingTouch;
		}
		return Weight;
	}
	
	public int weighTaste(List<String> details){
		int Weight = 0;
		for(String detail : details){
			int AddingTaste = Taste.get(detail);
			Weight = Weight+AddingTaste;
		}
		return Weight;
	}
	
	public void loadActions(){
		Touch.put("burn", -2);
		Touch.put("trans", 0);
		Touch.put("normal", 1);
		Touch.put("soft", 1);
		Touch.put("hard", 1);
		Touch.put("false", 0);
		
		Taste.put("burn", -2);
		Taste.put("sweet", 2);
		Taste.put("hard", -1);
		Taste.put("false", 0);
	}
}
