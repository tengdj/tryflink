package main;

import iot.streamers.BaseStreamer;

public class DataStreamer {
	
	public static void test_streaming() {
		BaseStreamer streamer = new BaseStreamer();
		streamer.start();
		try {
			streamer.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /*
     * A simple Java server to create data stream from
     * specified file. Note that this is for testing
     * purposes only. Ideally, the data stream should
     * come from third party API, Kafka etc.
     */
    public static void main(String args[]) {
    	test_streaming();
    }
}
