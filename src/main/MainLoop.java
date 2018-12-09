package main;

import algorithmGroup.AlgorithmGroup;
import algorithmOAT.AlgorithmOAT;
import algorithmOTS.AlgorithmOTS;
import worstCaseCW.WorstCaseCW;

public class MainLoop {
	public static void main(String[] args) {
		String[] algorithms = {"CautiousWalk", "GROUP", "OAT", "OTS"};
		String algorithm = algorithms[3];
		int graphSize = 9;
		
		// min and max values for rng delay in traveling edge - to simulate asynch agents
		int minTravelTime = 0;
		int maxTravelTime = 3;
		
		System.out.println("*************** Program Start ***************");
		
		switch (algorithm) {
		case "CautiousWalk":
			// Cautious Walk Algorithm
			try {
				WorstCaseCW walk = new WorstCaseCW(graphSize);
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
				AlgorithmOAT aot = new AlgorithmOAT(graphSize);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		case "OTS":
			// Algorithm 3 : Algorithm OPTTEAMSIZE
			try {
				AlgorithmOTS ots = new AlgorithmOTS(graphSize);
			} catch (Exception e) { e.printStackTrace(); }
			break;
		}
		
		System.out.println("************ Program Terminate ************");
	}
}
