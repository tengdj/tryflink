package main;

import java.util.ArrayList;

import iot.common.Point;
import iot.data.aot.AOTData;
import iot.data.climate.ClimateData;
import iot.data.taxi.ChicagoMap;
import iot.data.taxi.TaxiData;
import iot.streamers.AOTStreamer;
import iot.streamers.ChicagoTaxiStreamer;
import iot.tools.geohash.GeoHash;
import iot.tools.gps.Map;
import iot.tools.gps.Street;

public class Test {

	public static void test_geohash() {
		GeoHash gh = GeoHash.fromGeohashString("tengdejun");
		System.out.println(gh.toBase32()+" = "+gh.getBoundingBoxCenterPoint().getLatitude()+","+gh.getBoundingBoxCenterPoint().getLongitude());
		GeoHash reversed_gh = GeoHash.withCharacterPrecision(8.88888888,88.888888888, 12);
		System.out.println(reversed_gh.getBoundingBoxCenterPoint().getLatitude()+","+reversed_gh.getBoundingBoxCenterPoint().getLongitude()+" = "+reversed_gh.toBase32());
	}
	
	
	public static void test_streaming_aotdata() {
		AOTData at = new AOTData("data/aotdata/");
		at.setPath("data/aotdata/data.csv.gz");
		at.start();
		
		AOTStreamer as = new AOTStreamer();
		as.start();
	}

	public static void test_streaming_taxidata() {
		TaxiData td = new TaxiData("data/chicago/formated");
		td.setPort(9000);
		td.setPath("data/chicago/Taxi_Trips.csv");
		td.limits = 1000;
		td.start();
		
		ChicagoTaxiStreamer st = new ChicagoTaxiStreamer();
		st.start();
	}
	
	public static void test_load_aotdata() {
		AOTData dt = new AOTData("data/aotdata");
		dt.loadFromFiles("data/aotdata/data.csv");
	}
	
	public static void test_load_climatedata() {
		ClimateData cd = new ClimateData("data/climate");
		cd.loadFromFiles("data/climate/daily");		
	}
	
	public static void test_load_chicagomap() {
		ChicagoMap st = new ChicagoMap();
		st.loadFromCsv("data/chicago/transportation.csv");
		st.dumpTo("data/chicago/formated");
		st.clear();
		st.loadFromFormatedData("data/chicago/formated");
	}
	
	public static void test_navigate() {
		ChicagoMap st = new ChicagoMap();
		st.loadFromFormatedData("data/chicago/formated");
		ArrayList<Street> nav = st.navigate(new Point(-87.62076287,41.89833179), new Point(-87.90303966,41.97907082));
		System.out.println(Map.genGeoJson(nav).toString(1));	
		st.clear();
		nav.clear();
	}
	
	public static void test_load_taxidata() {
		TaxiData td = new TaxiData("data/chicago/formated");
		td.loadFromFiles("data/chicago/Taxi_Trips.csv");
	}
	
}
