package iot.common;

public interface TemporalSpatialData {

	
	public void initialize(String path);
	public void loadFromFiles(String path);
	public default void emit(Event e) {
		
		System.out.println(e.toJson().toString());
	};

}
