package main;

import iot.data.TraceLoader;

public class DataLoader{

	public static void test_load_taxidata() {
		TraceLoader td = new TraceLoader();
		td.loadFromFiles("data/chicago/Taxi_Trips.csv");
	}
	/*
	 * an entry point to start a client to load and send data
	 * */
    public static void main(String args[]){
    	test_load_taxidata();	
    }
}
