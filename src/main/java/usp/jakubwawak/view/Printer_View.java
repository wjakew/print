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

/**
 * Function for creating printer view
 */
public class Printer_View {

    public int printer_id;

    public String printer_name;
    public String printer_model;
    public String printer_ip;

    public String printer_localization;

    LocalDateTime last_update;

    public float last_cyan_data, last_magenta_data, last_yellow_data, last_black_data;

    /**
     * Constructor
     *
     * @param printer_id
     */
    public Printer_View(int printer_id) {
        this.printer_id = printer_id;
        load_printer_details();
        load_last_toner_data();
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
}
