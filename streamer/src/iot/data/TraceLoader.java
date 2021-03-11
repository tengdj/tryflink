package iot.data;

public class TraceLoader extends TemporalSpatialDataLoader{
	
	public String trace_path = "";
	
	@Override
	public void emit(Event e) {
		if(out!=null) {
			out.println(e.toJson().toString());	
		}
	}
	
	public TraceLoader() {
	}
	
	/*
	 * TODO: 
	 * load the formated file and emit each record as an event (look into the Event class for definition)
	 * 
	 * */
	@Override
	public void loadFromFiles() {
		// some fake code to be replaced
		for(int i=0;i<limits;i++) {
			Event e = new Event();
			e.id = i+"";
			e.timestamp = i;
			e.coordinate = new Point(0.1, 0.1);
			emit(e);
		}
	}

	@Override
	public void initialize(String path) {
	}

}
