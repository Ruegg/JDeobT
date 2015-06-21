package bugpatch.master;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import bugpatch.master.actions.ActionEstablisher;
import bugpatch.master.actions.Proc;
import bugpatch.master.design.Interface;
import bugpatch.master.items.ItemEstablisher;
import bugpatch.master.memory.Memory;

public class Oreki {

	public static ActionEstablisher AE;
	public static ItemEstablisher IE;
	public static Memory mem;
	public static Proc proc;
	public static Interface in;
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		AE = new ActionEstablisher();
		IE = new ItemEstablisher();
		proc = new Proc();
		mem = new Memory();
		in = new Interface();
		
		AE.loadActions();
		IE.loadItems();
		
		System.out.println("Welcome to The Oreki Project BETA v.02, Developed by MasterBugPatch");
		inputM();
	}
	
	public static void inputM(){
		String s;
		Scanner in = new Scanner(System.in);
		System.out.println("Proc | ");
		s = in.nextLine();
		String[] IS = s.split(" ");
		if(IS.length == 2){
			if(IS[0].equalsIgnoreCase("spawn")){
				String item = IS[1];
				proc.process(item);
			}else{
				outC("Invalid command");
			}
		}else{
			outC("Help");
			outC("  spawn <item> - Spawn an item to The System");
		}
		inputM();
	}
	
	public static void outC(String out){
		System.out.println(out);
	}
}
