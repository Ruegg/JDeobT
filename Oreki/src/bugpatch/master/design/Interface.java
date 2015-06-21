package bugpatch.master.design;

public class Interface {

	public Interface () {}
	
	public void out(String out){
		System.out.println(out);
	}
	
	public void noAction(String sense, String object){
		int r = 1 + (int)(Math.random() * ((4 - 1) + 1));
		if(r == 1){
			out("He decides its best to not " + sense + " the " + object + ".");
		}else if(r == 2){
			out("Oreki knows best not to " + sense + " the " + object + " from last time.");
		}else if(r == 3){
			out("He doesn't want to " + sense + " the " + object + " this time");
		}else if(r == 4){
			out("Oreki won't " + sense + " the " + object + " this time");
		}
	}
	public void action(String sense, String object, int outcome){
		int r = 1 + (int)(Math.random() * ((2 - 1) + 1));
		if(outcome == 1){
			if(r == 1){
			out("He " + sense + " the " + object + ", everything went okay.");
			}else if(r == 2){
				out("He " + sense + " it and is fine");
			}
		}else if(outcome == -1){
			if(r == 1){
				out("He " + sense + " the " + object + ", it went very badly!");
				}else if(r == 2){
					out("He " + sense + " it and it hurt him!");
				}
		}
	}
	
	public void firstSeen(){
		int r = 1 + (int)(Math.random() * ((3 - 1) + 1));
		if(r == 1){
			out("Oreki has never seen this object");
		}else if(r == 2){
			out("Oreki doesn't know of this");
		}else if(r == 3){
			out("This is Oreki's first time coming across this");
		}
	}
	
	public void recognizableObject(){
		int r = 1 + (int)(Math.random() * ((3 - 1) + 1));
		if(r == 1){
			out("Oreki remembers seeing this object before");
		}else if(r == 2){
			out("This object seems familiar to Oreki");
		}else if(r == 3){
			out("Oreki has seen this before");
		}
	}
}
