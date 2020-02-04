package ohdm.storage;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import ohdm.bean.Classification;
import ohdm.bean.ExternalSystem;
import ohdm.bean.Sensor;
import ohdm.bean.User;

public class DatabaseManager {
  
    private ArrayList<Sensor> sensorDataList;
    private ConnectionDb database = new ConnectionDb("jdbc:postgresql://localhost:5432/postgis_ohdm", "marta", "0000"); 
    private Table table = new Table(database);
    private UserInfo userInfo = new UserInfo(database);
    private Dht22 dht = new Dht22(database);
    private Ppd42 ppd = new Ppd42(database);
      
    /** Constructor
     * 
     * @param sensorDataList  holds the complete list of parsed data from the csv files.
     */
    public DatabaseManager(ArrayList<Sensor> sensorDataList) {
        this.sensorDataList = sensorDataList;
    }
    
    /** Methode to create all sensor relevant in the ohdm postgis database.
     * @throws SQLException     thrown if an error occurs during the table creation.
     */
    public void createTables() throws SQLException {
        System.out.println("Creating not existing tables...");
        table.createFineDustTable();
        table.createTemperatureTable();
        table.createImportedSensorTable();
    }

    /** Depending on the .csv column sensor type data gets inserted to the appropriate table.
     * 
     * @throws SQLException     is thrown if an sql insertion fails.
     * @throws ParseException   is thrown if a parsing error occurs.
     */
    public void insertSensorIntoDatabase() throws SQLException, ParseException {
        int typeId = 1; // 1 is the classification for points (location) in ohdm and applies for all sensors.
                
        System.out.println("Inserting sensor data...");
        Classification clazz = new Classification("sensor", "temperature");
        ExternalSystem dataSource = new ExternalSystem("luftdaten", "archive.luftdaten.info");
        User user = new User(1, "LuftdatenImporter");     
        long extSystemId = userInfo.addDataSource(dataSource);
        long userId = userInfo.addUser(user, extSystemId);
     
        // Here is the section to add sensors.
        for (int i = 0; i < sensorDataList.size(); ++i) {
            if (sensorDataList.get(i).getSensorType().contains("DHT")) {  
                dht.addDhtData(sensorDataList.get(i), clazz, typeId, userId);
            }
            if (sensorDataList.get(i).getSensorType().contains("PPD")) {
                ppd.addPpdData(sensorDataList.get(i), clazz, typeId, userId);
            }
        }
    }
}

