package bugpatch.master.items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ItemEstablisher {

	public ItemEstablisher(){ }
	
	public HashMap<String, HashMap<String, String>> Items = new HashMap<String, HashMap<String, String>>();
	
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
	
	public void loadItems() throws FileNotFoundException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(new File("./Resources/ITEMS.oreki")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(line.contains(".")){
		    		String[] LS = line.split("\\.");
		    		String ItemName = LS[0];
		    		if(LS.length == 3){
		    			if(LS[1].equals("attr")){
		    				int ColLoc = line.indexOf(":");
		    				int SenseRemove = line.length() - ColLoc;
		    				String Sense = LS[2];
		    				Sense = Sense.substring(0, Sense.length()-SenseRemove);
		    				String[] SC = line.split(":");
		    				String Details = SC[1];
		    				Details = Details.substring(1, Details.length());
		    				if(Items.containsKey(ItemName)){
		    					HashMap<String, String> Attributes = Items.get(ItemName);
		    					Attributes.put(Sense, Details);
		    					Items.put(ItemName, Attributes);
		    				}else{
		    					HashMap<String, String> Attributes = new HashMap<String, String>();
		    					Attributes.put(Sense, Details);
		    					Items.put(ItemName, Attributes);
		    				}
		    			}
		    		}
		    	}
		    }
		}
	}
}
