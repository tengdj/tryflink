package io.streamers;

import java.util.Random;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.json.JSONObject;

import iot.common.Event;

public class BaseStreamer extends Thread{
	public static int default_port = 9010;
	int port = default_port;
	String host = "127.0.0.1";

	public void setPort(int port) {
		this.port = port;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	static Random rand = new Random();

	public void run() {
		
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
        //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //env.getConfig().setAutoWatermarkInterval(1000L);
		DataStream<String> text = env.socketTextStream(host, port, "\n");

		// parse the data, group it, window it, and aggregate the counts
//		SingleOutputStreamOperator<Tuple2<String,Long>> processed = text.flatMap(new SimpleMapper())
//				.keyBy(0)
//				.timeWindow(Time.seconds(5))
//				.reduce(new SimpleReducer());
		
		DataStream<Tuple2<String, Long>> processed = text.map(new SameMapper())
					.keyBy(e->e.geohash)
					.process(new ItemCounter());
		
		processed.print();
		try {
			env.execute("socket streamer");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class SimpleMapper implements FlatMapFunction<String, Tuple2<String,Long>> {
		private static final long serialVersionUID = 1L;
		@Override
		public void flatMap(String value, Collector<Tuple2<String,Long>> out) {
			JSONObject obj = new JSONObject(value);
			//out.collect(new WordWithCount(obj.toString(),1L));
			out.collect(new Tuple2<String,Long>(obj.getString("geohash").substring(0,4),1L));
		}
	}
	
	public static class SameMapper extends RichMapFunction<String, Event> {
		private static final long serialVersionUID = 1L;
		@Override
		public Event map(String arg0) throws Exception {
			Event e = new Event(arg0);
			e.geohash = "word"+rand.nextInt(5);
			return e;
		}
	}
	
	public static class SimpleReducer implements ReduceFunction<Tuple2<String,Long>> {
		private static final long serialVersionUID = 1L;
		@Override
		public Tuple2<String,Long> reduce(Tuple2<String,Long> a, Tuple2<String,Long> b) {
			return new Tuple2<String,Long>(a.f0, a.f1 + b.f1);
		}
	}
	
	public static class ItemCounter extends KeyedProcessFunction<String, Event, Tuple2<String, Long>> {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ValueState<Long> total;

        @Override
        public void open(Configuration conf) {
            // register state handle
            total = getRuntimeContext().getState(
                    new ValueStateDescriptor<>("total", Types.LONG));
        }

        @Override
        public void processElement(
        		Event val,
                Context ctx,
                Collector<Tuple2<String, Long>> out) throws Exception {

            // look up start time of the last shift
            if(total.value()==null) {
            	total.update(0L);
            }
            Long curval = total.value();            
            total.update(++curval);
            if (curval%1000==0) {
                out.collect(Tuple2.of(ctx.getCurrentKey(),curval));
            }
        }
    }
	
}
