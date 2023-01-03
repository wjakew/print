/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.view;


import usp.jakubwawak.print.PrintApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Function for creating printer view
 */
public class Printer_View {

    public int printer_id;

    public String printer_name;
    public String printer_model;
    public String printer_ip;

    public String printer_localization;

    public String printer_serialnumber;

    LocalDateTime last_update;

    public float last_cyan_data, last_magenta_data, last_yellow_data, last_black_data;
    public int cyan_toner_status,magenta_toner_status,yellow_toner_status,black_toner_status;
    public int waste_catrige;

    /**
     * Constructor
     *
     * @param printer_id
     */
    public Printer_View(int printer_id) {
        this.printer_id = printer_id;
        last_cyan_data = 0; last_magenta_data = 0; last_yellow_data = 0; last_black_data = 0;
        load_printer_details();
        load_last_toner_data();
    }

    /**
     * Function for loading supply data on warehouse
     * @return String
     */
    public String get_warehouse_status(){
        String toner_data = "";
        if ( cyan_toner_status == 1 ){
            toner_data = toner_data + "CYAN ";
        }
        if ( magenta_toner_status == 1){
            toner_data = toner_data + "MAGENTA ";
        }
        if ( yellow_toner_status == 1){
            toner_data = toner_data + "YELLOW ";
        }
        if ( black_toner_status == 1){
            toner_data = toner_data + "BLACK ";
        }
        if ( waste_catrige == 1){
            toner_data = toner_data + "WASTE CONTAINER";
        }
        if (toner_data.equals("")){
            toner_data = "EMPTY";
        }
        PrintApplication.database.nl.add("WAREHOUSE-STATUS-LOADER","Status: "+toner_data);
        return toner_data;
    }

    /**
     * Function for loading empty data on warehouse
     * @return String
     */
    public String get_warehouse_supplay_empty(){
        String toner_data = "";
        if ( cyan_toner_status == 0 ){
            toner_data = toner_data + "CYAN ";
        }
        if ( magenta_toner_status == 0){
            toner_data = toner_data + "MAGENTA ";
        }
        if ( yellow_toner_status == 0){
            toner_data = toner_data + "YELLOW ";
        }
        if ( black_toner_status == 0){
            toner_data = toner_data + "BLACK ";
        }
        if ( waste_catrige == 0){
            toner_data = toner_data + "WASTE CONTAINER";
        }
        if (toner_data.equals("")){
            toner_data = "";
        }
        return toner_data;
    }

    public int getID(){
        return printer_id;
    }

    public String getName(){
        return printer_name;
    }

    public String getIP(){
        return printer_ip;
    }

    public String getLastUpdate(){
        return last_update.toString();
    }

    public String getPrinter_serialnumber(){return printer_serialnumber;};

    public String getCyan(){
        if ( last_cyan_data == -69f){
            return "X";
        }
        return Integer.toString((int) last_cyan_data) +"%";
    }

    public String getLocalization(){
        if ( printer_localization == null || printer_localization.equals("")){
            return "brak";
        }
        else{
            return printer_localization;
        }
    }

    public String getMagenta(){
        if ( last_magenta_data == -69f){
            return "X";
        }
        return Integer.toString((int) last_magenta_data) +"%";
    }

    public String getYellow(){
        if ( last_yellow_data == -69f){
            return "X";
        }
        return Integer.toString((int) last_yellow_data) +"%";
    }

    public String getBlack(){
        if ( last_black_data == -69f){
            return "X";
        }
        return Integer.toString((int) last_black_data) +"%";
    }

    public void load_warehouse_data(){
        String query = "SELECT * FROM WAREHOUSE_ELEMENT WHERE printer_id = ?;";
        try{
            PreparedStatement ppst = PrintApplication.database.con.prepareStatement(query);
            ppst.setInt(1,printer_id);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                switch(rs.getString("warehouse_element_name")){
                    case "CYAN TONER":
                        cyan_toner_status = 1;
                        break;
                    case "MAGENTA TONER":
                        magenta_toner_status = 1;
                        break;
                    case "YELLOW TONER":
                        yellow_toner_status = 1;
                        break;
                    case "BLACK TONER":
                        black_toner_status = 1;
                        break;
                    case "WASTE CONTAINER":
                        waste_catrige = 1;
                        break;
                }
            }
        }catch(SQLException e){
            PrintApplication.database.nl.add("PRINTER-WAREHOUSE-FAILED","Failed to add elements from warehouse to printer data("+e.toString());
        }
    }

    /**
     * Function for loading printer details
     */
    void load_printer_details() {
        String query = "SELECT * FROM PRINTER WHERE printer_id = ?;";
        try {
            PreparedStatement ppst = PrintApplication.database.con.prepareStatement(query);
            ppst.setInt(1, printer_id);
            ResultSet rs = ppst.executeQuery();
            if (rs.next()) {
                printer_name = rs.getString("printer_name");
                printer_model = rs.getString("printer_model");
                printer_ip = rs.getString("printer_ip");
                printer_localization = rs.getString("printer_localization");
                printer_serialnumber = rs.getString("printer_serialnumber");
                PrintApplication.database.nl.add("VIEW-DETAILS","Loaded details for printer "+printer_name);
            } else {
                printer_name = "empty";
                printer_model = "empty";
                printer_ip = "empty";
            }
        } catch (SQLException e) {
            PrintApplication.database.nl.add("PRINTERVIEW-FAILED", "Failed to show printer details (" + e.toString() + ")");
        }
    }

    /**
     * Function for loading last toner snap
     */
    void load_last_toner_data() {
        String query = "SELECT * FROM TONER_DATA WHERE printer_id = ?;";
        try {
            PreparedStatement ppst = PrintApplication.database.con.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ppst.setInt(1, printer_id);
            ResultSet rs = ppst.executeQuery();
            if ( rs.next() ){
                rs.last();
                last_cyan_data = rs.getFloat("toner_data_cyan");
                last_magenta_data = rs.getFloat("toner_data_magenta");
                last_yellow_data = rs.getFloat("toner_data_yellow");
                last_black_data = rs.getFloat("toner_data_black");
                last_update = rs.getObject("toner_data_time",LocalDateTime.class);
                PrintApplication.database.nl.add("LASTTONER","Loaded last toner entry from "+last_update.toString());
            }
            else{
                PrintApplication.database.nl.add("LASTTONER-EMPTY","Cannot find toner data for printer ("+printer_name+")");
            }

        } catch (SQLException e) {
            PrintApplication.database.nl.add("LASTTONER-FAILED","Failed to load last toner data ("+e.toString()+")");
        }
    }

    /**
     * Function for loading printer logs
     * @return String
     */
    public String get_printer_logs(){
        String logs = "";
        String query = "SELECT * FROM APPLOG;";
        try{
            PreparedStatement ppst = PrintApplication.database.con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                String log = rs.getString("applog_desc");
                if (log.contains("id:"+printer_id)){
                    logs = logs + log + "\n";
                }
            }
        }catch(SQLException e){
            PrintApplication.database.nl.add("PRINTER-LOG-GET-FAILED","Failed to get printer log data ("+e.toString());
        }
        return logs;
    }
}
