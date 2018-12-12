package main;

import java.util.Arrays;
import java.util.List;

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
		
		String[] algorithms = {"WCCW", "OAT", "OTS", "GROUP"};
		String algorithm = algorithms[1]; // Select an algorithm
		int graphSize = 25;
		
		// min and max values for rng delay in traveling edge - to simulate asynch agents
		int minTravelTime = 1;
		int maxTravelTime = 3;
		
		// home base and black hole location, if set to -1 they will be randomly generated
		int homeBaseLocation = 2;
		homeBaseLocation = -1;
		int blackHoleLocation = 4;
		blackHoleLocation = -1;
		
		boolean debug = false;
		
		if(args.length >= 5) {
			List<String> input = Arrays.asList(args);
			
			graphSize = input.indexOf("-g") > 0 ? Integer.parseInt(args[input.indexOf("-g")+1]) : graphSize;
			minTravelTime = input.indexOf("-min") > 0 ? Integer.parseInt(args[input.indexOf("-min")+1]) : minTravelTime;
			maxTravelTime = input.indexOf("-max") > 0 ? Integer.parseInt(args[input.indexOf("-max")+1]) : maxTravelTime;
			homeBaseLocation = input.indexOf("-hb") > 0 ? Integer.parseInt(args[input.indexOf("-hb")+1]) : homeBaseLocation;
			blackHoleLocation = input.indexOf("-bh") > 0 ? Integer.parseInt(args[input.indexOf("-bh")+1]) : blackHoleLocation;
			if(input.indexOf("-algo") > 0) {
				try {
					algorithm = args[Integer.parseInt(args[input.indexOf("-algo")+1])];
				} catch (Exception e) {
					if(Arrays.asList(algorithms).contains(args[input.indexOf("-algo")+1])) {
						algorithm = args[input.indexOf("-algo")+1];
					}
				}
			}
			
			debug = input.contains("-d");
		} 
		
		System.out.println("*************** Program Start ***************");
		//for(int i =0;i<100;i++) {
		int i = 0;
		switch (algorithm) {
		case "CautiousWalk":
			// Cautious Walk Algorithm
			try {
				WorstCaseCW walk = new WorstCaseCW(graphSize, homeBaseLocation, blackHoleLocation, minTravelTime, maxTravelTime, debug, i);
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
				AlgorithmOAT aot = new AlgorithmOAT(graphSize, homeBaseLocation, blackHoleLocation, minTravelTime, maxTravelTime, debug, i);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		case "OTS":
			// Algorithm 3 : Algorithm OPTTEAMSIZE
			try {
				AlgorithmOTS ots = new AlgorithmOTS(graphSize, homeBaseLocation, blackHoleLocation, minTravelTime, maxTravelTime, debug, i);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		}
//		System.out.println("done:" + i + "/99");
//		}
		System.out.println("************ Program Terminate ************");
	}
	
	public static void help() {
		System.out.println("Implementation of Black Hole Search in Rings");
		System.out.println("java -jar BHS.jar -g graphSize -min minAsynchTime -max maxAsynchTime -hb homeBaseLocation - bh blackHoleLocation -d");
		System.out.println("java -jar BHS.jar -g 25 -min 1 -max 2 -hb 0 - bh 10 -d");
		System.out.println("algorithmChoice : int|string - [0|1|2] or [CautiousWalk|OAT|OTS]");
		System.out.println("graphSize : int - size of ring");
		System.out.println("minAsynchTime : int - min wait time for simulated asynch");
		System.out.println("maxAsynchTime : int - max wait time for simulated asynch");
		System.out.println("homeBaseLocation : int - homebase index within bounds of graphSize");
		System.out.println("blackHoleLocation : int - blackhole index within bounds of graphSize");
		System.out.println("debug : boolean - [true|false] print ring state at each iteration");
	}
}

