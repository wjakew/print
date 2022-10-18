/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import usp.jakubwawak.Maintanance.Configuration_Service;
import usp.jakubwawak.Maintanance.PrintLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Object for connecting to database
 */
public class Database_Connector {

    public boolean connected;
    LocalDateTime run_time;
    public Connection con;
    public PrintLog nl;
    public Configuration_Service cs;

    /**
     *
     */
    public Database_Connector(Configuration_Service cs){
        this.cs = cs;
    }


    /**
     * Function for connecting service to local database
     */
    public void connect(){
        if ( !cs.empty() ){
            nl.add("Trying to connect to database with user: "+cs.printapp_raw_database_user,"DATABASE-CONNECTOR");
            String login_data = "jdbc:mysql://"+cs.printapp_raw_database+"/"+cs.printappklip_raw_database_name+"?"
                    + "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&" +
                    "user="+cs.printapp_raw_database_user+"&password="+cs.printapp_raw_database_password;

            try{
                con = DriverManager.getConnection(login_data);
                run_time = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
                nl.add("Connected succesfully","CONNECTION");
                nl.add(login_data.substring(0,login_data.length()-25)+"...*END*","CONNECTION");
                connected = true;
            }catch(SQLException e){
                connected = false;
                nl.add("Failed to connect to database ("+e.toString()+")","ERROR-DB01");
            }
        }
        else{
            nl.add("Failed to connect, configuration file is empty.","DATABASE-CONFIGURATION-EMPTY");
        }
    }


}
