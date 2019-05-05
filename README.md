# IotDB

## Things to Do
* Implement two use cases
  1. Stream weather data (AOTData) - DONE
     * Implement AOTStreamer
     * Map input data to Event - DONE
     * Patition data using location - KeyBy(geohash) - DONE
     * Keep a global state corresponding to each geohash
     * Configurable window length
     * Update global state for each window
     * Generate alert if delta is greater than predefined threshold
  2. Stream driver data
     * TODO ...

## Datasets
* Chicago AOT dataset can be downloaded from url https://aot-file-browser.plenar.io/data-sets/chicago-complete
. download and decompress certain datasets into a folder and specify the folder path as aot-data-dir in streamer.properties file
* Climate dataset can be downloaded from url ftp://ftp.ncdc.noaa.gov/pub/data/ghcn/daily/download . firstly download the ghcn-stations.txt and ghcn-states.txt in a folder and specify the path of the folder as climate-meta-dir in streamer.properties. Then donwload the ghcn_all.tar.gz file and decompress it. The decompressed folder contains hundreds of thousands of txt files with station ids. specify the folder contain those txt files as climate-data-dir in streamer.properties.
* Chicago taxi trip dataset can be downloaded from url https://data.cityofchicago.org/Transportation/Taxi-Trips/wrvz-psew/data . click the export button to download the dataset as XML format. Then download the chicago street map dataset from url https://data.cityofchicago.org/Transportation/Street-Center-Lines/6imu-meau
 download as xml file. 
## Dependencies (UNIX)
* Java 8
* Maven 3
* Apache Flink 1.7.2

## Run Sample Job
The repository consists of two separate components. Before getting into the details, following are the configuration properties that need to be set appropriately.
### Configure Job
`streamer.properties` file contains configuration parameters. Following is a sample configuration file properties.
```bash
aot-data-dir=/Users/furqan/Workspace/iotdb/sample_data/chicago-complete.daily.2019-03-31/
# optional
#aot-data-file=
taxi-data-path=/Users/furqan/Workspace/iotdb/sample_data/taxi_data/
climate-data-path=/Users/furqan/Workspace/iotdb/sample_data/climate_data/
stream-host=127.0.0.1
stream-port=9000
```
### Data Streamer
As of writing, no public weather dataset was available for streaming. To get around this issue, we downloaded the dataset offline and streamed it over socket to IoTStreamer to mimic streaming IoT data processing. Following is the code to run sample `DataStreamer` using configured properties
```bash
java -cp target/streamer-0.0.1-SNAPSHOT-jar-with-dependencies.jar main.DataStreamer
```

### IoTStreamer
This contains core processing logic of the system. It can be submitted to flink as follows
```bash
./bin/flink run -c main.Main ~/Workspace/iotdb/IoTDB/streamer/target/streamer-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

## Code Structure
* `src/main/`
  * Contains tests and system driver code
* `src/iot/data/`
  * Contains code to handle different datasets
  * To mimic IoT data streaming, reads data from files and stream to socket
* `src/iot/common/`
  * Contains code common to data stream generation and core logic
* `src/iot/streamers/`
  * Contains logic to process streams of IoT data
* `src/iot/tools/`
  * Contains (adaptations of) third party codes to manage `geohashes`, work with `gps` data
  * Data processing utilities
