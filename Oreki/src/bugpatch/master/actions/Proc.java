package bugpatch.master.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import bugpatch.master.Oreki;

public class Proc {

	public Oreki oreki;
	
	public HashMap<String, Integer> Similars = new HashMap<String, Integer>(); //ItemName, Similars
	
	public boolean ESims = false;
	
	public Proc() {}
	public void process(String ITEM){
		
		runSimilars(ITEM);
		
		oreki.mem.putSenseToItem(ITEM, "sight", oreki.IE.getSenseOfItem(ITEM, "sight"));
		oreki.mem.putSenseToItem(ITEM, "smell", oreki.IE.getSenseOfItem(ITEM, "smell"));
		oreki.mem.putSenseToItem(ITEM, "hear", oreki.IE.getSenseOfItem(ITEM, "hear"));
		
		if(oreki.mem.hasTestMemory(ITEM)){
			int TouchWeight = 0;
			int TasteWeight = 0;
			
			oreki.in.recognizableObject();
			
			if(oreki.mem.hasSense(ITEM, "touch") && oreki.mem.hasSense(ITEM, "taste")){
				
				String[] TouchDetails = oreki.mem.getSenseOfItem(ITEM, "touch").split(",");
				String[] TasteDetails = oreki.mem.getSenseOfItem(ITEM, "taste").split(",");
				
				List<String> Touch = Arrays.asList(TouchDetails);
				List<String> Taste = Arrays.asList(TasteDetails);
				
				TouchWeight = oreki.AE.weighTouch(Touch);
				TasteWeight = oreki.AE.weighTaste(Taste);
			}else{
				if(oreki.mem.hasSense(ITEM, "touch")){
					String[] TouchDetails = oreki.mem.getSenseOfItem(ITEM, "touch").split(",");
					List<String> Touch = Arrays.asList(TouchDetails);
					TouchWeight = oreki.AE.weighTouch(Touch);
					
					TasteWeight = weighSimilarTaste();
				}else if(oreki.mem.hasSense(ITEM, "taste")){
					String[] TasteDetails = oreki.mem.getSenseOfItem(ITEM, "taste").split(",");
					List<String> Taste = Arrays.asList(TasteDetails);
					TasteWeight = oreki.AE.weighTaste(Taste);
					
					TouchWeight = weighSimilarTouch();
				}
			}
			
			if(TouchWeight < -1 && TasteWeight < -1){
			oreki.in.noAction("touch and taste", ITEM);	
			}else{
				if(TouchWeight > -1){
					touchItem(ITEM);
				}else{
					oreki.in.noAction("touch", ITEM);
				}
			
				if(TasteWeight > -1){
					tasteItem(ITEM);
				}else{
					oreki.in.noAction("taste", ITEM);
				}
			}
		} else{
			
			oreki.in.firstSeen();
			
			int TouchWeight = 0;
			int TasteWeight = 0;
			
			if(ESims){
				TouchWeight = weighSimilarTouch();
				TasteWeight = weighSimilarTaste();
			}else{
				TouchWeight = 1;//Force to test
				TasteWeight = 1;
			}
			
			if(TouchWeight < -1 && TasteWeight < -1){
				oreki.in.noAction("touch and taste", ITEM);	
				}else{
					if(TouchWeight > -1){
						touchItem(ITEM);
					}else{
						oreki.in.noAction("touch", ITEM);
					}
				
					if(TasteWeight > -1){
						tasteItem(ITEM);
					}else{
						oreki.in.noAction("taste", ITEM);
					}
				}
		}
	}
	
	public void touchItem(String ITEM){
		String details = oreki.IE.getSenseOfItem(ITEM, "touch");
		String[] detailsSplit = details.split(",");
		List<String> SenseDetails = Arrays.asList(detailsSplit);
		int Weight = 0;
		for(String d : SenseDetails){//for sense details
			Weight = Weight+oreki.AE.Touch.get(d);
		}
		oreki.mem.putSenseToItem(ITEM, "touch", details);
		if(Weight < 0){
			oreki.in.action("touched", ITEM, -1);
		}else{
			oreki.in.action("touched", ITEM, 1);
		}
	}
	
	public void tasteItem(String ITEM){
		String details = oreki.IE.getSenseOfItem(ITEM, "taste");
		String[] detailsSplit = details.split(",");
		List<String> SenseDetails = Arrays.asList(detailsSplit);
		int Weight = 0;
		for(String d : SenseDetails){
			Weight = Weight+oreki.AE.Taste.get(d);
		}
		oreki.mem.putSenseToItem(ITEM, "taste", details);
		if(Weight < 0){
			oreki.in.action("tasted", ITEM, -1);
		}else{
			oreki.in.action("tasted", ITEM, 1);
		}
	}
	
	public int weighSimilarTaste(){
		if(ESims){
			int curTasteWeight = 0;
			for(Entry<String, Integer> e : Similars.entrySet()){//For each similar item
				String curItem = e.getKey();
				if(oreki.mem.hasSense(curItem, "taste")){
				String TasteDetails = oreki.mem.getSenseOfItem(curItem, "taste");
				String[] TasteSplit = TasteDetails.split(",");
				List<String> Taste = Arrays.asList(TasteSplit);
				for(String detail: Taste){//For each of the items taste details
					int AddingTaste = oreki.AE.Taste.get(detail);
					curTasteWeight = curTasteWeight+AddingTaste;
				}
			  }
			}
			return curTasteWeight;
			}else{
				return 1;
			}
	}
	
	public int weighSimilarTouch(){
		if(ESims){
			int curTouchWeight = 0;
			for(Entry<String, Integer> e : Similars.entrySet()){//For each similar item
				String curItem = e.getKey();
				if(oreki.mem.hasSense(curItem, "touch")){
				String TouchDetails = oreki.mem.getSenseOfItem(curItem, "touch");
				String[] TouchSplit = TouchDetails.split(",");
				List<String> Touch = Arrays.asList(TouchSplit);
				for(String detail: Touch){//For each of the items taste details
					int AddingTouch = oreki.AE.Touch.get(detail);
					curTouchWeight = curTouchWeight+AddingTouch;
				}
			   }
			}
			return curTouchWeight;
			}else{
				return 1;
			}
	}
	public void runSimilars(String ITEM){
		Similars.clear();
		
		HashMap<String, String> CurItemSDs = new HashMap<String, String>();
		CurItemSDs.put("sight", oreki.IE.getSenseOfItem(ITEM, "sight"));
		CurItemSDs.put("smell", oreki.IE.getSenseOfItem(ITEM, "smell"));
		CurItemSDs.put("hear", oreki.IE.getSenseOfItem(ITEM, "hear"));
		
		
		
		///IF HAS AT LEAST 3 SIMILARITIES PER ITEM THEN USE IT THO DONT ELSE
		for(Entry<String, HashMap<String, String>> e : oreki.mem.Items.entrySet()){ //For each Item in memory (name, senses)
			String ITEMNAME = e.getKey();
			Similars.put(ITEMNAME, 0);
			HashMap<String, String> SenseDetails = e.getValue(); //senses
			for(Entry<String,String> es : SenseDetails.entrySet()){//For (sense, details)
				String curSense = es.getKey();
				String curDetails = oreki.mem.getSenseOfItem(ITEMNAME, curSense);
				String[] Details = curDetails.split(",");
				List<String> curArray = Arrays.asList(Details);
				if(CurItemSDs.containsKey(curSense)){
					List<String> itemArray = Arrays.asList(CurItemSDs.get(curSense).split(","));
					for(String detail : itemArray){
						if(curArray.contains(detail)){
							Similars.put(ITEMNAME, Similars.get(ITEMNAME)+1);
						}
					}
				}
			}
		}
		
			double Uses = 0.25*Similars.size();
			int similarsUse = (int) Math.ceil(Uses);
			Similars = getHighestValues(Similars, similarsUse);
			
			ArrayList<String> RemovingKeys = new ArrayList<String>();
			for(Entry<String, Integer> e : Similars.entrySet()){
				if(e.getValue() < 3){
					RemovingKeys.add(e.getKey());
				}
			}
			for(String removing : RemovingKeys){
				Similars.remove(removing);
			}
			
		if(Similars.size() > 0){
			ESims = true;
		}else{
			ESims = false;
		}
	}

	public HashMap<String, Integer> getHighestValues(HashMap<String, Integer> values, int amount){
		HashMap<String, Integer> copy = values;
		HashMap<String, Integer> top = new HashMap<String, Integer>();
		for(int x = 1; x != amount+1; x++){
				Entry<String,Integer> maxEntry = null;
			for(Entry<String,Integer> entry : copy.entrySet()) {
			    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
			        maxEntry = entry;
			    }
			}
			copy.remove(maxEntry);
			top.put(maxEntry.getKey(), maxEntry.getValue());
		}
		return top;
	}
	
}
