package main;

import algorithmGroup.AlgorithmGroup;
import cautiousWalk.CautiousWalk;

public class MainLoop {
	public static void main(String[] args) {
		String algorithm = "GROUP";
		int graphSize = 9;
		
		System.out.println("*************** Program Start ***************");
		
		switch (algorithm) {
		case "CautiousWalk":
			// Cautious Walk Algorithm
			try {
				CautiousWalk walk = new CautiousWalk(graphSize);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		case "GROUP":
			// Algorithm 1 : Algorithm GROUP
			try {
				if(graphSize % 4 != 1) { System.out.println("Invalid size, n = 4q + 1"); System.exit(0); }
				AlgorithmGroup group = new AlgorithmGroup(graphSize);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		}
		
		System.out.println("************ Program Terminate ************");
	}
}
