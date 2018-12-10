package main;

import algorithmGroup.AlgorithmGroup;
import algorithmOAT.AlgorithmOAT;
import algorithmOTS.AlgorithmOTS;
import worstCaseCW.WorstCaseCW;

public class MainLoop {
	public static void main(String[] args) {
		if(args.length == 1 && args[0].equals("-h")) {
			help();
			System.exit(0);
		}
		
		String[] algorithms = {"CautiousWalk", "GROUP", "OAT", "OTS"};
		String algorithm = algorithms[0]; // Select an algorithm
		int graphSize = 9;
		graphSize = 5005;
		
		// min and max values for rng delay in traveling edge - to simulate asynch agents
		int minTravelTime = 1;
		int maxTravelTime = 3;
		
		// home base and black hole location, if set to -1 they will be randomly generated
		int homeBaseLocation = 2;
		homeBaseLocation = -1;
		int blackHoleLocation = 4;
		blackHoleLocation = -1;
		
		boolean debug = false;
		
		if(args.length >= 6) {
			graphSize = Integer.parseInt(args[1]);
			minTravelTime = Integer.parseInt(args[2]);
			maxTravelTime = Integer.parseInt(args[3]);
			homeBaseLocation = Integer.parseInt(args[4]);
			blackHoleLocation = Integer.parseInt(args[5]);
			try {
				algorithm = algorithms[Integer.parseInt(args[0])];
			} catch (Exception e) {
				algorithm = args[0];
			}
			if(args.length == 7) {
				debug = Boolean.parseBoolean(args[6]);
			}
		} 
		
		System.out.println("*************** Program Start ***************");
		for(int i =0;i<100;i++) {
		
		switch (algorithm) {
		case "CautiousWalk":
			// Cautious Walk Algorithm
			try {
				WorstCaseCW walk = new WorstCaseCW(graphSize, homeBaseLocation, blackHoleLocation, minTravelTime, maxTravelTime, debug);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		case "GROUP":
			// Algorithm 1 : Algorithm GROUP
			try {
				if(graphSize % 4 != 1) { System.out.println("Invalid size, n = 4q + 1"); System.exit(0); }
				AlgorithmGroup group = new AlgorithmGroup(graphSize);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		case "OAT":
			// Algorithm 2 : Algorithm OPTAVGTIME
			try {
				AlgorithmOAT aot = new AlgorithmOAT(graphSize, homeBaseLocation, blackHoleLocation, minTravelTime, maxTravelTime, debug);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		case "OTS":
			// Algorithm 3 : Algorithm OPTTEAMSIZE
			try {
				AlgorithmOTS ots = new AlgorithmOTS(graphSize, homeBaseLocation, blackHoleLocation, minTravelTime, maxTravelTime, debug);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		}
		System.out.println("done:" + i + "/99");
		}
		System.out.println("************ Program Terminate ************");
	}
	
	public static void help() {
		System.out.println("Implementation of Black Hole Search in Rings");
		System.out.println("algorithmChoice graphSize minAsynchTime maxAsynchTime homeBaseLocation blackHoleLocation debug");
		System.out.println("algorithmChoice : int|string - [0|1|2|3] or [CautiousWalk|GROUP|OAT|OTS]");
		System.out.println("graphSize : int - size of ring");
		System.out.println("minAsynchTime : int - min wait time for simulated asynch");
		System.out.println("maxAsynchTime : int - max wait time for simulated asynch");
		System.out.println("homeBaseLocation : int - homebase index within bounds of graphSize");
		System.out.println("blackHoleLocation : int - blackhole index within bounds of graphSize");
		System.out.println("debug : boolean - [true|false] print ring state at each iteration");
	}
}

