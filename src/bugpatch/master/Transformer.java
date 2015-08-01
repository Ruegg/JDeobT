package bugpatch.master;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public interface Transformer {

	public void initialRun();
	
	public String decrypt(String s);
	
	public HashMap<Integer,String> findStrings(File JAD) throws IOException;	
	
	public void saveData(File jdeobt, HashMap<Integer, String> Decrypted) throws IOException;

	public boolean runComplete(File JAD, File SAVE, boolean Messages) throws IOException;
}
