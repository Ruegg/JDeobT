package bugpatch.master.utils;

import java.io.File;

import bugpatch.master.JDeobT;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Extractor {

	public static void extractZip(File zip){

		String parsedDestination = zip.getAbsolutePath().replaceAll(".zip", "");

	    try {
	         ZipFile zipFile = new ZipFile(zip);
	         zipFile.extractAll(parsedDestination);
	    } catch (ZipException e) {
	        e.printStackTrace();
	    }
			
	}
	
	public static void dissAssem(File JAD, File Dis){
		try
		{
		 Runtime rt = Runtime.getRuntime() ;
		 File DirToRun = new File(JDeobT.getFileLocation() + "bin");
		 Process p = rt.exec(JAD.getAbsolutePath() + " -dis " + Dis.getAbsolutePath(), null, DirToRun);
		 p.waitFor();
		}catch(Exception exc){}
	}
}
