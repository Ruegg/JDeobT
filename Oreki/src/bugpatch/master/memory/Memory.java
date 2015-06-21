package bugpatch.master.memory;

import java.util.HashMap;

public class Memory {

	public HashMap<String, HashMap<String, String>> Items = new HashMap<String, HashMap<String, String>>();
	
	public Memory() {}
	
	public boolean hasMemory(String ITEM){
		if(Items.containsKey(ITEM)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean hasTestMemory(String ITEM){
		if(Items.containsKey(ITEM)){
			HashMap<String, String> Senses = Items.get(ITEM);
			if(Senses.containsKey("touch") || Senses.containsKey("taste")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public void putSenseToItem(String ITEM, String Sense, String Details){
		if(Items.containsKey(ITEM)){
			HashMap<String, String> Senses = Items.get(ITEM);
			Senses.put(Sense, Details);
			Items.put(ITEM, Senses);
		}else{
			HashMap<String,String> Senses = new HashMap<String, String>();
			Senses.put(Sense, Details);
			Items.put(ITEM, Senses);
		}
	}
	
	public void addMemory(String ITEM, HashMap<String, String> senses){
		Items.put(ITEM, senses);
	}
	
	public HashMap<String,String> getMemory(String ITEM){
		return Items.get(ITEM);
	}
	
	public String getSenseOfItem(String ITEM, String Sense){
		if(Items.containsKey(ITEM)){
			HashMap<String, String> Senses = Items.get(ITEM);
			if(Senses.containsKey(Sense)){
				return Senses.get(Sense);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public boolean hasSense(String ITEM, String Sense){
		if(Items.containsKey(ITEM)){
			HashMap<String, String> Senses = Items.get(ITEM);
			if(Senses.containsKey(Sense)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
