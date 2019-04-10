package main;

/* Java imports */
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;


public class StreamerConfig {

    public static Properties config;

    public static String get(String prop){
	return config.getProperty(prop);
    }
    
    static{
	InputStream inputStream = null;
	try {
	    config = new Properties();
	    inputStream =
	    	StreamerConfig.class.getClassLoader().getResourceAsStream("streamer.properties");
	    config.load(inputStream);

	    if (!config.stringPropertyNames().contains("aot-data-file")){
		config.setProperty("aot-data-file",
				   config.getProperty("aot-data-dir") + "/" + "data.csv.gz");
	    }
	    
	    inputStream.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(-1);
	} finally{
	    try{
		inputStream.close();
	    }catch(Exception e){
		System.out.println("No stream to close");
	    }
	}
    }

}
