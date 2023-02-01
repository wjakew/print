/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import usp.jakubwawak.Maintanance.Configuration_Service;
import usp.jakubwawak.Maintanance.LogObject;
import usp.jakubwawak.Maintanance.PrintLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
    public Database_Connector(Configuration_Service cs,PrintLog nl){
        this.cs = cs;
        this.nl = nl;
    }


    /**
     * Function for connecting service to local database
     */
    public void connect(){
        if ( !cs.empty() ){
            nl.add("DATABASE-CONNECTOR","Trying to connect to database with user: "+cs.printapp_raw_database_user);
            String login_data = "jdbc:mysql://"+cs.printapp_raw_database+"/"+cs.printappklip_raw_database_name+"?"
                    + "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&" +
                    "user="+cs.printapp_raw_database_user+"&password="+cs.printapp_raw_database_password;

            try{
                con = DriverManager.getConnection(login_data);
                run_time = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
                nl.add("CONNECTION","Connected succesfully");
                nl.add("CONNECTION",login_data.substring(0,login_data.length()-25)+"...*END*");
                connected = true;
            }catch(SQLException e){
                connected = false;
                nl.add("ERROR-DB01","Failed to connect to database ("+e.toString()+")");
            }
        }
        else{
            nl.add("DATABASE-CONFIGURATION-EMPTY","Failed to connect, configuration file is empty.");
        }
    }

    /**
     * Function for loading instance name
     * @return String
     */
    public String get_instance_name(){
        String query = "SELECT printapp_instance_name from HEALTH;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();

            if ( rs.next() ){
                return rs.getString("printapp_instance_name");
            }
            return "";
        }catch(SQLException e){
            nl.add("INSTANCE-NAME-GET","Failed to get instance name ("+e.toString()+")");
            return "";
        }
    }

    /**
     * Function for adding log object data
     * @return ArrayList
     */
    public ArrayList<LogObject> load_log(){
        String query = "SELECT * FROM APPLOG;";
        ArrayList<LogObject> log_array = new ArrayList<>();
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                LogObject log = new LogObject(rs.getInt("applog_id"),rs.getString("applog_code")
                        ,rs.getString("applog_desc"),rs.getObject("applog_time",LocalDateTime.class).toString());
                if ( log.validate() ){
                    log_array.add(log);
                }
            }
            Collections.reverse(log_array);
        }catch(SQLException e){
            nl.add("LOG-LOAD-FAILED","Failed to list log data ("+e.toString()+")");
        }
        return log_array;
    }

    /**
     * Function for updating instance name
     * @param instance_name
     * @return Integer
     */
    public int update_instance_name(String instance_name){
        String query = "UPDATE HEALTH SET printapp_instance_name = ?;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.setString(1,instance_name);
            ppst.execute();
            nl.add("INSTANCE-UPDATE","Updated instance to ("+instance_name+")");
            return 1;
        }catch(SQLException e){
            nl.add("INSTANCE-UPDATE-FAILED","Failed to update instance ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for getting update time from database
     * @return Integer
     */
    public int get_update_time(){
        String query = "SEKECT printapp_update_time FROM HEALTH;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            if ( rs.next() ){
                return rs.getInt("printapp_update_time");
            }
        }catch(SQLException e){
            nl.add("GETTIME-FAILED","Failed to update update time from database ("+e.toString()+")");
        }
        return -1;
    }

    /**
     * Function for getting debug data from database
     * @return Integer
     */
    public int get_debugFlag(){
        String query = "SELECT printapp_debug_flag FROM HEALTH;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            if ( rs.next() ){
                nl.add("DEBUG-FLAG","Debug flag set to: "+rs.getInt("printapp_debug_flag"));
                return rs.getInt("printapp_debug_flag");
            }
            return -2;
        }catch(SQLException e){
            nl.add("DEBUG-FLAG-ERROR","Failed to get debug flag ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for disabling log printing
     * @return Integer
     */
    public int disable_logPrinting(){
        String query = "UPDATE HEALTH SET printapp_debug_flag = 0;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.executeQuery();
            nl.add("DEBUGFLAG-UPDATE","Updated debug flag to: 0");
            return 1;
        }catch(SQLException e){
            nl.add("DEBUGFLAG-FAILED","Failed to update debug flag to 0 ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for enable log printing
     * @return Integer
     */
    public int enable_logPrinting(){
        String query = "UPDATE HEALTH SET printapp_debug_flag = 1;";
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.executeQuery();
            nl.add("DEBUGFLAG-UPDATE","Updated debug flag to: 1");
            return 1;
        }catch(SQLException e){
            nl.add("DEBUGFLAG-FAILED","Failed to update debug flag to 1 ("+e.toString()+")");
            return -1;
        }
    }
}
