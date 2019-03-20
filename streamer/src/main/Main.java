package main;

import iot.data.aot.AOTData;
import iot.data.taxi.ChicagoMap;
import iot.data.taxi.TaxiData;
import iot.tools.gps.*;
import iot.tools.utils.Util;

import java.text.ParseException;
import java.util.ArrayList;

public class Main {
	
	public static void main(String args[]) {
//		GeoHash gh = GeoHash.fromGeohashString("tengdejun");
//		System.out.println(gh.getBoundingBoxCenterPoint().getLatitude()+" "+gh.getBoundingBoxCenterPoint().getLongitude());
//		System.out.println(gh.toBase32());
		
//		gh = GeoHash.withCharacterPrecision(40.084591, 116.031330, 12);
//		System.out.println(gh.toBase32());
//		
		AOTData dt = new AOTData("data/aot");
		dt.loadFromFiles("data/aot/data.csv");
		dt.loadFromFiles("data/aot/data.csv.gz");
		
		
//		ClimateData cd = new ClimateData("climate");
//		cd.loadFolder("climate/daily");
		
//		Map st = new ChicagoMap();
//		st.loadFromFormatedData("data/chicago/formated");
//		ArrayList<Street> nav = st.navigate(new Point(-87.62076287,41.89833179), new Point(-87.90303966,41.97907082));
//		System.out.println(Map.genGeoJson(nav));
		
		
//		TaxiData td = new TaxiData();
//		td.loadFromFile("data/chicago/Taxi_Trips.csv");

		

//		System.out.println(Util.min_distance_point_to_segment(0,0, 2,0, 0, 4));
	}
	
}