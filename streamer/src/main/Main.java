package main;


public class Main {
	
	
	
    /*
     * main entry for tests
     */
	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		Test.transposeClimateData("D:/climate/transposed", "D:/climate/ghcnd_all/ghcnd_all", "D:/climate/", 20);
		long end = System.currentTimeMillis();
		System.out.println("takes "+((end-start)/1000.0)+" seconds");
		//Test.test_load_climatedata();
		//Test.test_streaming_aotdata();
		//Test.test_geohash();
	}
}
