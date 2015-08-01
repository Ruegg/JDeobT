package bugpatch.master.transformers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;

import bugpatch.master.JDeobT;
import bugpatch.master.Transformer;

public class Allatori implements Transformer{

	boolean Messages = false;
	
	@Override
	public void initialRun() {
		
		
	}

	@Override
	public String decrypt(String s) {
		  int i = s.length();
	        char[] a = new char[i];
	        int i0 = i - 1;
	        while(true)
	        {
	            if (i0 >= 0)
	            {
	                int i1 = s.charAt(i0);
	                int i2 = i0 + -1;
	                int i3 = (char)(i1 ^ 105);
	                a[i0] = (char)i3;
	                if (i2 >= 0)
	                {
	                    i0 = i2 + -1;
	                    int i4 = s.charAt(i2);
	                    int i5 = (char)(i4 ^ 59);
	                    a[i2] = (char)i5;
	                    continue;
	                }
	            }
	            return new String(a);
	        }
	}

	@Override
	public HashMap<Integer, String> findStrings(File JAD) throws IOException{
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

	public void out(String msg){
		if(Messages){
			System.out.println(msg);
		}
	}
	
	@Override
	public boolean runComplete(File JAD, File SAVE, boolean Messages) throws IOException {
		Messages = true;
		
		out("Finding Strings");
		HashMap<Integer, String> Strings = findStrings(JAD);
		HashMap<Integer, String> Decrypted = new HashMap<Integer, String>();
		
		if(Strings.size() > 0){
			
			out("Decrypting Strings");
			for(Entry<Integer,String> e : Strings.entrySet()){
				Decrypted.put(e.getKey(), decrypt(e.getValue()));
			}
			
			out("Saving Data");
			saveData(SAVE, Decrypted);
			
			return true;
		}else{
			return false;
		}
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
	
}
