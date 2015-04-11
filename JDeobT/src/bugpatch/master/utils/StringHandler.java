package bugpatch.master.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;

import bugpatch.master.JDeobT;

public class StringHandler {

	public static void runComplete(File f){
		try {
			findDecryptionChars(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] splt = JDeobT.Decryption.split(" ");
		
		if(!JDeobT.Decryption.equals("") && splt.length == 5){
		System.out.println("JDeobT !! Decryption found >> " + JDeobT.Decryption);

		try {
			findStrings(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		runDeobf();
		}else{
			JDeobT.skipSave = true;
		}
		
	}
	
	public static void findDecryptionChars(File f) throws FileNotFoundException, IOException{
		System.out.println("-------------------------");
		System.out.println("  Finding ByteCode Decrypt");
		System.out.println("-------------------------");
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
		    	   
		   		   System.out.println("TableSwitch Skip found " + skp);
		    	   LineSkip = Integer.parseInt("" + skp);
		    	   LineSkip = LineSkip+1;
		    	   System.out.println("JDeobT is Line Skipping " + LineSkip);
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
		    			
		    			if(JDeobT.Decryption.equals("")){
		    				JDeobT.Decryption = substr;
		    			}else{
		    				JDeobT.Decryption = JDeobT.Decryption + " " + substr;
		    			}
		    			
		    			
		    			DecryptProgress = DecryptProgress+1;
		    			LineSkip = 1;
		    			System.out.println("JDeobT is Line Skipping 1");
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
		    			
		    			if(JDeobT.Decryption.equals("")){
		    				JDeobT.Decryption = substr;
		    			}else{
		    				JDeobT.Decryption = JDeobT.Decryption + " " + substr;
		    			}
		    			TSFound = false;
		    			DecryptProgress = 0;
		    		}
		    	}else{
		    		TSFound = false;
		    	}
		    }
		}else{
			System.out.println("Skipped Line.");
			LineSkip = LineSkip-1;
		}
	   }
	  }
	}
	
	public static void findStrings(File f) throws IOException{
		System.out.println("\n-------------------------");
		System.out.println("  Finding Encrypted Strings");
		System.out.println("-------------------------");
		int curLine = 0;
		int StringsFound = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	curLine++;
		    	if(line.contains("<String ")){
		    		String[] s = line.split("String ");
		    		String ss = s[1];
		    		ss = ss.replaceAll(">", "");
		    		ss = ss.substring(1);
		    		ss = ss.substring(0, ss.length()-1);
		    		StringsFound++;
		    		ss = StringEscapeUtils.unescapeJava(ss);
		    		System.out.println(ss + " found on line " + curLine);
		    		JDeobT.StringvsLine.put(curLine, ss);
		    	}
		    }
		}
		System.out.println(StringsFound + " Strings have been found");
	}
	
	public static void runDeobf(){
		System.out.println("\n-------------------------");
		System.out.println(" Running String Deobfuscator");
		System.out.println("-------------------------");
		
		String[] splt = JDeobT.Decryption.split(" ");
		int rg1 = Integer.parseInt(splt[0]);
		int rg2 = Integer.parseInt(splt[1]);
		int rg3 = Integer.parseInt(splt[2]);
		int rg4 = Integer.parseInt(splt[3]);
		int rg5 = Integer.parseInt(splt[4]);
		JDeobT.decryption = new char[] {(char) rg1, (char) rg2, (char) rg3, (char) rg4, (char) rg5};
		
		LinkedHashMap<Integer, String> FutureEdits = JDeobT.StringvsLine;
		for(Entry<Integer, String> e : FutureEdits.entrySet()){
			String s = e.getValue();
			String ss = decrypt(s.toCharArray());
			JDeobT.StringvsLine.put(e.getKey(), ss);
			System.out.println(e.getValue() + " was decrypted to " + ss);
		}
	}
	
	private static String decrypt(char[] arr) {
        char key[] = JDeobT.decryption;
        for (int i = 0; i < arr.length; i++) {
            arr[i] ^= key[i % 5];
        }
        return new String(arr);
    }
	
	public static void saveData(File f) throws FileNotFoundException, UnsupportedEncodingException{
		if(!JDeobT.skipSave){
		System.out.println("\n-------------------------");
		System.out.println("     Saving Data");
		System.out.println("-------------------------");
		PrintWriter writer = new PrintWriter(f, "UTF-8");
		for(Entry<Integer,String> e : JDeobT.StringvsLine.entrySet()){
			writer.println("'" + e.getValue() + "' on line " + e.getKey());
			System.out.println("Writing " + e.getValue() + " to " + f.getName());
		}
		writer.close();
	}
  }
}
