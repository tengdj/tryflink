package main;

import iot.data.TraceLoader;

public class DataLoader{

	public static void test_load_trace() {
		TraceLoader loader = new TraceLoader();
		loader.setPath("/path/to/trace/file");
		loader.setLimits(10);
		loader.start();
		try {
			loader.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * an entry point to start a client to load and send data
	 * */
    public static void main(String args[]){
    	test_load_trace();	
    }
}
