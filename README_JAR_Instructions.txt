Execution Command
	java -jar BHS.jar -g graphSize -min minAsynchTime -max maxAsynchTime -hb homeBaseLocation - bh blackHoleLocation -d
	
-g : graph size flag allows user to specify desired ring size
-min : minimum asynch time for edge traversal
-max : maximum asynch time for edge traversal

-algo : determines the algorithm to run on ring input either an int or string [0|1|2] or [WCCW|OAT|OTS]

-d : debug flag which if set prints ring state at each iteration

-hb : lets user specify homebase index within bounds of graphSize
-bh : lets user specify blackhole index within bounds of graphSize

Example: 
	java -jar BHS.jar -g 25 -hb 0 - bh 10 -d
Generates a ring size of 25 nodes with homebase located at node 0, and black hole at node 10, with debug flag set, so it will print the state of the ring at each stage