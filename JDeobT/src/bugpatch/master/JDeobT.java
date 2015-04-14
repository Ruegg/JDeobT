package bugpatch.master;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import bugpatch.master.transformers.Allatori;
import bugpatch.master.transformers.ZKM;
import bugpatch.master.utils.Extractor;

public class JDeobT {

	
	public static void main(String[] args) throws IOException{
		File binDir = new File(getFileLocation() + "bin");
		if(binDir.exists()){
			FileUtils.deleteDirectory(binDir);
		}
		new File(getFileLocation() + "bin").mkdirs();
		
		getInput();
	}
	
	public static void getInput(){
		Scanner in = new Scanner(System.in);
		System.out.println("Command: ");
		String commandExecuted = in.nextLine();
		String[] command = commandExecuted.split(" ");
		
		try {
			commandExecuted(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getInput();
	}
	
	public static void execHelp(){
		System.out.println("USAGE:");
		System.out.println("target.jar -import -transformer");
		System.out.println("import           <jar|class|jad>");
		System.out.println("transformer      <allatori|zkm>");
		System.out.println("Example C:/Users/Steve/Magic.jar -jar -zkm");
	}
	
	public static void execBadArgs(){
		System.out.println("Bad arguments!! Try using help");
	}
	
	public static void commandExecuted(String[] args) throws IOException{
		File binDir = new File(getFileLocation() + "bin");
		if(binDir.exists()){
			FileUtils.deleteDirectory(binDir);
		}
		new File(getFileLocation() + "bin").mkdirs();
		
		if(args.length == 0){
			execHelp();
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				execHelp();
			}else if(args[0].equalsIgnoreCase("v")){
				System.out.println("JDeobT V. 1.1.2");
				System.out.println("Dv. MasterBugPatch");
				System.out.println("© ByteStructure 2015");
			}else{
				execBadArgs();
			}
		}else if(args.length == 2){
			execBadArgs();
		}else if(args.length == 3){
			String targetLocation = args[0];
			String importion = args[1];
			String transformer = args[2];
			transformer = transformer.substring(1);
			
			if(!targetLocation.contains("/") && !targetLocation.contains("\\")){
				targetLocation = getFileLocation() + targetLocation;
			}
			
			File target = new File(targetLocation);
			Transformer t = null;
			if(getTransformer(transformer) != null){
				t = getTransformer(transformer);
				if(target.exists()){
					if(importion.equalsIgnoreCase("-jar")){
						
						String targetName = target.getName();
						
						FileUtils.copyFile(target, new File(getFileLocation() + "bin/" + targetName));
						
						File copiedJar = new File(getFileLocation() + "bin/" + targetName);
						
						String[] fns = copiedJar.getName().split("\\.");
						String noExtName = fns[fns.length-2];
						
						copiedJar.renameTo(new File(getFileLocation() + "bin/" + noExtName + ".zip"));
						
						File zippedTarget = new File(getFileLocation() + "bin/" + noExtName + ".zip");
						
						System.out.println("JDeobT extracting > " + noExtName);
						System.out.println("Extract Time varies...");
						Extractor.extractZip(zippedTarget);
						System.out.println("Extracted!");
						
						System.out.println("Deleting temporary zip");
						zippedTarget.delete();
						
						Collection<File> classFiles = FileUtils.listFiles(new File(getFileLocation() + "bin/" + noExtName), null, true);
						
						System.out.println("Iterating through class files");
						for(File classFile : classFiles){
							String classFileName = classFile.getName();
							if(classFileName.endsWith(".class")){
								
							String SaveFL = classFile.getAbsolutePath().replaceAll("\\\\" + noExtName + "\\\\", "\\\\" + noExtName + "-Assem" + "\\\\");
							SaveFL = SaveFL.substring(0, SaveFL.length() - 6);
							SaveFL = SaveFL + ".jad";
							
							String JADExp = getFileLocation() + "bin/" + classFileName;
							JADExp = JADExp.substring(0, JADExp.length() - 6);
							JADExp = JADExp + ".jad";
							System.out.println("Export location " + JADExp);
							File clss = new File(JADExp);
							
							if(clss.exists()){
								System.out.println("Disassembled already found, removing.");
								clss.delete();							
								
							}
							
							File JAD = new File(getFileLocation() + "jad.exe");
							System.out.println("Dissasembling " + classFileName);
							Extractor.dissAssem(JAD, classFile);
					
							System.out.println(classFileName + " has been disassembled with JAD");
							
							File Dissed = new File(clss.getAbsolutePath());
							String DissedName = clss.getName();
							System.out.println("Passing " + DissedName + " to deobfuscation");
							
							if(Dissed.exists()){
							FileUtils.copyFile(Dissed, new File(SaveFL));
							System.out.println("Writing " + DissedName);
							Dissed.delete();
							}else{
								System.out.println("An unknown deletion occured to " + DissedName);
							}
						  }
						}
						
						System.out.println("JDeobT finished disassembling " + noExtName + " with JAD");
						System.out.println("Removing temporary classes");
						FileUtils.deleteDirectory(new File(getFileLocation() + "bin/" + noExtName));
						
						System.out.println("Beggining deobfuscation");
						Collection<File> cfassem = FileUtils.listFiles(new File(getFileLocation() + "bin/" + noExtName + "-Assem"), null, true);
						
						for(File Dissed : cfassem){
							if(Dissed.getName().endsWith(".jad")){
								
							String SaveFL = Dissed.getAbsolutePath().replaceAll("\\\\" + noExtName + "-Assem" + "\\\\", "\\\\" + noExtName + "-Deob" + "\\\\");
							SaveFL = SaveFL.substring(0, SaveFL.length() - 4);
							SaveFL = SaveFL + ".jdbt";
							
							String[] temp = SaveFL.split("\\\\");
							String Temp = temp[temp.length-1];
							
							String DirToCreate = SaveFL.replaceAll(Temp, "");
							new File(DirToCreate).mkdirs();
							
							t.runComplete(Dissed, new File(SaveFL), true);
							
							}
						}
						
						System.out.println("String Deobfuscation finished");
						
						System.out.println("Removing temporary jar");
						copiedJar.delete();
						
						System.out.println("Removing temporary disassembles");
						FileUtils.deleteDirectory(new File(getFileLocation() + "bin/" + noExtName + "-Assem"));
						
						System.out.println("JDeobT !! Deobfuscation finished!");
						System.out.println("Please move the files from the /bin/ folder as they get removed on new deobfuscations");
						
						
					}else if(importion.equalsIgnoreCase("-class")){
						
						File JAD = new File(getFileLocation() + "jad.exe");
						
						if(JAD.isFile()){
							String targetName = target.getName();
							
							String JADExp = getFileLocation() + "bin";
							JADExp = JADExp.substring(0, JADExp.length() - 6);
							JADExp = JADExp + ".jad";
							
							File clss = new File(JADExp);
							
							if(clss.exists()){
								clss.delete();
								System.out.println("Disassembled JAD already found, removing to continue.");
							}
							
							Extractor.dissAssem(JAD, target);
							System.out.println(targetName + " has been disassembled with JAD");
							
							File newDis = new File(clss.getAbsolutePath());
							
							System.out.println("Passing " + targetName + " to deobfuscation");
							File SaveFL = new File(getFileLocation() + target.getName() + ".jdbt");
							
							t.runComplete(newDis, SaveFL, true);
							
							newDis.delete();
							System.out.println("JDeobT !! Deobfuscation finished!");
							System.out.println("Please move the files from the /bin/ folder as they get removed on new deobfuscations");
						}else{
							System.out.println("Deobfucation from jars or classes requires JAD for disassembly");
						}
					}else if(importion.equalsIgnoreCase("-jad")){
						
						File SaveFL = new File(getFileLocation() + target.getName() + ".jdbt");
						
						t.runComplete(target, SaveFL, true);
						
						System.out.println("JDeobT !! Deobfuscation finished!");
						System.out.println("Please move the files from the /bin/ folder as they get removed on new deobfuscations");
					}else{
						System.out.println("!! Importion type non-existant");
					}
				}else{
					System.out.println("!! Target cannot be found");
					System.out.println("Location checked: " + targetLocation);
				}
			}else{
				System.out.println("!! Invalid transformer");
			}
		}
	}
	
	
	public static Transformer getTransformer(String t){
		if(t.equalsIgnoreCase("allatori")){
			return new Allatori();
		}else if(t.equalsIgnoreCase("zkm")){
			return new ZKM();
		}else{
			return null;
		}
	}
	
/*	public static void main(String[] args) throws IOException{
		new File(getFileLocation() + "bin").mkdirs();
		if(args.length == 0 | args.length == 1){
			
			Transformer t = new Allatori();
			System.out.println("Arguments are invalid");
			System.out.println("JDeobT.jar -type C:/magic.jar");
			System.out.println("-TYPE");
			System.out.println("  -assem");
			System.out.println("  -class");
			System.out.println("  -jar");
			System.out.println(getFileLocation());
		}else if(args.length == 2){
			
			String loc = args[1];
			if(!loc.contains("/") && !loc.contains("\\")){
				loc = getFileLocation() + args[1];
			}
			
			File target = new File(loc);
			if(target.isFile()){
				if(args[0].equalsIgnoreCase("-assem")){
					
					StringHandler.runComplete(target);
					File SaveFL = new File(getFileLocation() + target.getName() + ".jdbt");
					
					try {
						StringHandler.saveData(SaveFL);
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						System.out.println(e.getLocalizedMessage());
						System.out.println("JDeobT threw an error!");
					}
					
					clearAll();
					
					System.out.println("JDeobT !! Deobfuscation finished!");
					System.out.println("Please move the files from the /bin/ folder as they get removed on new deobfuscations");
					
				}else if(args[0].equalsIgnoreCase("-class")){
					
					File JAD = new File(getFileLocation() + "jad.exe");
					
					if(JAD.isFile()){
						String targetName = target.getName();
						
						String JADExp = getFileLocation() + "bin";
						JADExp = JADExp.substring(0, JADExp.length() - 6);
						JADExp = JADExp + ".jad";
						
						File clss = new File(JADExp);
						
						if(clss.exists()){
							clss.delete();
							System.out.println("Disassembled JAD already found, removing to continue.");
						}
						
						Extractor.dissAssem(JAD, target);
						System.out.println(targetName + " has been disassembled with JAD");
						
						File newDis = new File(clss.getAbsolutePath());
						StringHandler.runComplete(newDis);
						
						System.out.println("Passing " + targetName + " to deobfuscation");
						File SaveFL = new File(getFileLocation() + target.getName() + ".jdbt");
						
						try {
							StringHandler.saveData(SaveFL);
						} catch (FileNotFoundException | UnsupportedEncodingException e) {
							System.out.println(e.getLocalizedMessage());
							System.out.println("JDeobT threw an error!");
						}
						
						newDis.delete();
						
						clearAll();
						
						System.out.println("JDeobT !! Deobfuscation finished!");
						System.out.println("Please move the files from the /bin/ folder as they get removed on new deobfuscations");
					}else{
						System.out.println("Deobfucation from jars or classes requires JAD for disassembly");
					}
				}else if(args[0].equalsIgnoreCase("-jar")){
					
					String targetName = target.getName();
					
					FileUtils.copyFile(target, new File(getFileLocation() + "bin/" + targetName));
					
					File copiedJar = new File(getFileLocation() + "bin/" + targetName);
					
					String[] fns = copiedJar.getName().split("\\.");
					String noExtName = fns[fns.length-2];
					
					copiedJar.renameTo(new File(getFileLocation() + "bin/" + noExtName + ".zip"));
					
					File zippedTarget = new File(getFileLocation() + "bin/" + noExtName + ".zip");
					
					System.out.println("JDeobT extracting > " + noExtName);
					System.out.println("Extract Time varies...");
					Extractor.extractZip(zippedTarget);
					
					zippedTarget.delete();
					
					Collection<File> classFiles = FileUtils.listFiles(new File(getFileLocation() + "bin/" + noExtName), null, true);
					
					for(File classFile : classFiles){
						String classFileName = classFile.getName();
						if(classFileName.endsWith(".class")){
							
						String SaveFL = classFile.getAbsolutePath().replaceAll("\\\\" + noExtName + "\\\\", "\\\\" + noExtName + "-Assem" + "\\\\");
						SaveFL = SaveFL.substring(0, SaveFL.length() - 6);
						SaveFL = SaveFL + ".jad";
						
						File JAD = new File(getFileLocation() + "jad.exe");
						Extractor.dissAssem(JAD, classFile);
						
						String JADExp = getFileLocation() + "bin/" + classFileName;
						JADExp = JADExp.substring(0, JADExp.length() - 6);
						JADExp = JADExp + ".jad";
						
						File clss = new File(JADExp);
						
						
						System.out.println(classFileName + " has been disassembled with JAD");
						
						File Dissed = new File(clss.getAbsolutePath());
						String DissedName = clss.getName();
						System.out.println("Passing " + DissedName + " to deobfuscation");
						
						if(Dissed.exists()){
						FileUtils.copyFile(Dissed, new File(SaveFL));
						System.out.println("Writing " + DissedName);
						Dissed.delete();
						}else{
							System.out.println("An unknown deletion occured to " + DissedName);
						}
					  }
					}
					
					System.out.println("JDeobT finished disassembling " + noExtName + " with JAD");
					System.out.println("Removing temporary classes");
					FileUtils.deleteDirectory(new File(getFileLocation() + "bin/" + noExtName));
					
					System.out.println("Beggining deobfuscation");
					Collection<File> cfassem = FileUtils.listFiles(new File(getFileLocation() + "bin/" + noExtName + "-Assem"), null, true);
					
					for(File Dissed : cfassem){
						if(Dissed.getName().endsWith(".jad")){
							
						String SaveFL = Dissed.getAbsolutePath().replaceAll("\\\\" + noExtName + "-Assem" + "\\\\", "\\\\" + noExtName + "-Deob" + "\\\\");
						SaveFL = SaveFL.substring(0, SaveFL.length() - 4);
						SaveFL = SaveFL + ".jdbt";
						
						String[] temp = SaveFL.split("\\\\");
						String Temp = temp[temp.length-1];
						
						String DirToCreate = SaveFL.replaceAll(Temp, "");
						new File(DirToCreate).mkdirs();
						
						StringHandler.runComplete(Dissed);				
				
						StringHandler.saveData(new File(SaveFL));
						
						clearAll();
						}
					}
					
					System.out.println("String Deobfuscation finished");
					
					System.out.println("Removing temporary jar");
					copiedJar.delete();
					
					System.out.println("Removing temporary disassembles");
					FileUtils.deleteDirectory(new File(getFileLocation() + "bin/" + noExtName + "-Assem"));
					
					System.out.println("JDeobT !! Deobfuscation finished!");
					System.out.println("Please move the files from the /bin/ folder as they get removed on new deobfuscations");
					
					
				}else{
					System.out.println("Invalid type given");
				}
			}else{
				System.out.println("Invalid File!");
			}
		}
	} */
	
	public static String getFileLocation(){
		String r = "";
		r = JDeobT.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String[] splt = r.split("/");
		int arg = splt.length-1;
		String ThisFile = splt[arg];
		r = r.replaceAll(ThisFile, "");
		r = r.substring(1);
		return r;
	}
	
}
