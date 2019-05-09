package main;


public class Main {
    /*
     * main entry for tests
     */
	public static void main(String args[]) {
		if(args.length<5) {
			System.out.println("usage: java -jar streamer.jar output_dir input_dir meta_dir thread_number buffer_size");
			return;
		}
		iot.tools.utils.ClimateDataTransposer.transposeClimateData(args[0],args[1],args[2],Integer.parseInt(args[3]),Long.parseLong(args[4]));

		//Test.test_load_climatedata_local();
		//Test.test_streaming_aotdata();
		//Test.test_geohash();
	}
}
