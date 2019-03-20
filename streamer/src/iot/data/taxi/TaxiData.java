package iot.data.taxi;

import iot.common.Event;

public class TaxiData implements iot.common.TemporalSpatialData {

	@Override
	public void initialize(String path) {
		
	}
	double max = Double.MIN_VALUE;

	
	//taxi trip is more complicated, since it is a trajectory instead of a single events	
	void emit(Trip t) {
		if(t.trip_length>max) {
			max = t.trip_length;
			t.print();
		}
	}
	
	@Override
	public void emit(Event e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public TaxiData() {
		
	}
	
	@Override
	public void loadFromFiles(String path) {
		
		iot.tools.utils.FileBatchReader fb = new iot.tools.utils.FileBatchReader(path);
		int count = 0;
		boolean header = true;
		while(!fb.eof) {
			
			for(String s:fb.lines) {
				if(count++>100) {
					return;
				}
				if(header) {
					header = false;
					continue;
				}
				String cols[] = s.split(",");
				if(cols.length!=24) {
					continue;
				}
				Trip t;
				try {
					t = new Trip(cols);
				} catch (Exception e) {
					//System.out.println(s);
					continue;
				}

				emit(t);

			}
			
			fb.nextBatch();
			
		}
	}



}
