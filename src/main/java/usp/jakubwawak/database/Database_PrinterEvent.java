/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Database manager for managing printer_events and printer snaps
 */
public class Database_PrinterEvent {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_PrinterEvent(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for adding toner data value
     * @param printer_id
     * @param toner_data_value
     * @return Integer
     */
    public int add_toner_data(int printer_id,String toner_data_value,float cyan_value,float magenta_value,float yellow_value,float black_value){
        String query = "INSERT INTO TONER_DATA (printer_id,toner_data_time,toner_data_cyan,toner_data_magenta,toner_data_yellow,toner_data_black,toner_data_value) VALUES (?,?,?,?,?,?,?);";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,printer_id);
            ppst.setObject(2, LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            ppst.setFloat(3,cyan_value);
            ppst.setFloat(4,magenta_value);
            ppst.setFloat(5,yellow_value);
            ppst.setFloat(6,black_value);
            ppst.setString(7,toner_data_value);
            ppst.execute();
            database.nl.add("TONERDATA-ADD","Added new toner data for printer_id:"+printer_id);
            return 1;
        }catch(SQLException e){
            database.nl.add("TONERDATA-ADD-FAILED","Failed to add toner data ("+e.toString()+")");
            return -1;
        }
    }
}
