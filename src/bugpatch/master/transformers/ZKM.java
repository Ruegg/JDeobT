 package bugpatch.master.transformers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;

import bugpatch.master.Transformer;

public class ZKM implements Transformer{

	public String Decryption = "";
	public char[] decryption;
	public boolean Messages = false;
	
	@Override
	public void initialRun() {
		
	}

	public void findDecryptionChars(File f) throws FileNotFoundException, IOException{
		out("Finding Bytecode Decryption");
		int LineSkip = 0;
		boolean TSFound = false;
		int DecryptProgress = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    if(LineSkip == 0){
		    if(!TSFound){
		       if(line.contains("tableswitch")){
		    	   String[] rgs = line.split(":");
		    	   String sl = rgs[1];
		    	   int a = sl.lastIndexOf(" ");
		    	   String skp = sl.substring(a+1);		    	   
		   		   out("TableSwitch Skip found " + skp);
		    	   LineSkip = Integer.parseInt("" + skp);
		    	   LineSkip = LineSkip+1;
		    	   out("JDeobT is Line Skipping " + LineSkip);
		    	   TSFound  = true;
		       }
		    }else{
		    	if(line.contains("bipush") | line.contains("iconst")){
		    		if(DecryptProgress != 4){   			
		    			String substr = "";		    			
		    			if(line.contains("bipush")){
		    				int pl = line.lastIndexOf(" ");
			    			pl = pl +1;
			    			int numChars = line.length() - pl;
			    			substr = line.substring(line.length() - numChars);
		    			}else if(line.contains("iconst")){
		    				String s = line.replaceAll(" ", "");
		    				String[] rgs = s.split("_");
		    				substr = rgs[1];
		    			}		    			
		    			if(Decryption.equals("")){
		    				Decryption = substr;
		    			}else{
		    				Decryption = Decryption + " " + substr;
		    			}    			
		    			DecryptProgress = DecryptProgress+1;
		    			LineSkip = 1;
		    			out("JDeobT is Line Skipping 1");
		    		}else{		    			
		    			String substr = "";		    			
		    			if(line.contains("bipush")){
		    				int pl = line.lastIndexOf(" ");
			    			pl = pl +1;
			    			int numChars = line.length() - pl;
			    			substr = line.substring(line.length() - numChars);
		    			}else if(line.contains("iconst")){
		    				String s = line.replaceAll(" ", "");
		    				String[] rgs = s.split("_");
		    				substr = rgs[1];
		    			}	    			
		    			if(Decryption.equals("")){
		    				Decryption = substr;
		    			}else{
		    				Decryption = Decryption + " " + substr;
		    			}
		    			TSFound = false;
		    			DecryptProgress = 0;
		    		}
		    	}else{
		    		TSFound = false;
		    	}
		    }
		}else{
			out("Skipped Line.");
			LineSkip = LineSkip-1;
		}
	   }
	  }
		if(Decryption.contains(" ")){
		if(Decryption.split(" ").length == 5){
			String[] spltDecrypt = Decryption.split(" ");
			int p1 = Integer.parseInt(spltDecrypt[0]);
			int p2 = Integer.parseInt(spltDecrypt[1]);
			int p3 = Integer.parseInt(spltDecrypt[2]);
			int p4 = Integer.parseInt(spltDecrypt[3]);
			int p5 = Integer.parseInt(spltDecrypt[4]);
			decryption = new char[] {(char) p1, (char) p2, (char) p3, (char) p4, (char) p5};
		}
	  }
	}
	
	@Override
	 public String decrypt(String str) {
        char key[] = decryption;
        char arr[] = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            arr[i] ^= key[i % 5];
        }
        return new String(arr);
    }

	@Override
	public HashMap<Integer, String> findStrings(File JAD) throws IOException {
		HashMap<Integer,String> StringList = new HashMap<Integer,String>();
		int curLine = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(JAD))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	curLine++;
		    	if(line.contains("<String ")){
		    		String[] s = line.split("String ");
		    		String ss = s[1];
		    		ss = ss.replaceAll(">", "");
		    		ss = ss.substring(1);
		    		ss = ss.substring(0, ss.length()-1);
		    		ss = StringEscapeUtils.unescapeJava(ss);
		    		StringList.put(curLine, ss);
		    		out(ss + " found on line " + curLine);
		    	}
		    }
		}
		out("Total of " + StringList.size() + " lines found in " + JAD.getName());
		return StringList;
	}

	@Override
	public void saveData(File jdeobt, HashMap<Integer, String> Decrypted) throws IOException{
		PrintWriter writer = new PrintWriter(jdeobt, "UTF-8");
		for(Entry<Integer,String> e : Decrypted.entrySet()){
			writer.println("'" + e.getValue() + "' on line " + e.getKey());
			out("Writing " + e.getValue() + " to " + jdeobt.getName());
		}
		writer.close();
	}

	@Override
	public boolean runComplete(File JAD, File SAVE, boolean Messages)
			throws IOException {
		Decryption = "";
		decryption = null;
		this.Messages = Messages;
		findDecryptionChars(JAD);
		String[] spltDecrypt = Decryption.split(" ");
		if(spltDecrypt.length == 5){
			HashMap<Integer, String> StringList = findStrings(JAD);
			HashMap<Integer, String> Decrypted = new HashMap<Integer, String>();
			
			if(StringList.size() > 0){
				out("Decrypting Strings");
				for(Entry<Integer,String> e : StringList.entrySet()){
					Decrypted.put(e.getKey(), decrypt(e.getValue()));
				}
				
				out("Saving Data");
				saveData(SAVE, Decrypted);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public void out(String msg){
		if(Messages){
			System.out.println(msg);
		}
	}

}
