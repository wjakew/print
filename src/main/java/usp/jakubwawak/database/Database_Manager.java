/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Object for maintaining database content
 */
public class Database_Manager {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_Manager(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for adding printer data
     * @param printer_name
     * @param printer_localization
     * @param printer_model
     * @param printer_status
     * @return int
     */
    public int add_printer(String printer_name,String printer_ip,String printer_localization,String printer_model,String printer_status){
        String query = "INSERT INTO PRINTER (printer_name,printer_ip,printer_localization,printer_model,printer_status) " +
                "VALUES (?,?,?,?,?);";
        try{
            database.nl.add("PRINTER-ADD","Trying to add printer ("+printer_name+"/"+printer_ip);
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setString(1,printer_name);
            ppst.setString(2,printer_ip);
            ppst.setString(3,printer_localization);
            ppst.setString(4,printer_model);
            ppst.setString(5,printer_status);
            ppst.execute();
            database.nl.add("PRINTER-ADD","Added new printer to database ("+printer_name+")");
            return 1;
        }catch(SQLException e){
            database.nl.add("PRINTER-ADD-FAILED","Failed to add printer ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for getting all printer ids from database
     * @return ArrayList
     */
    public ArrayList<Integer> get_printer_ids(){
        ArrayList<Integer> data = new ArrayList<>();
        String query = "SELECT printer_id FROM PRINTER;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(rs.getInt("printer_id"));
            }
            database.nl.add("PRINTERID-LIST","Loaded "+data.size()+" printer_ids.");
        }catch(SQLException e){
            database.nl.add("PRINTERID-LIST-FAILED","Failed to get printer list ("+e.toString()+")");
        }
        return data;
    }

    /**
     * Function for getting printer ip data
     * @param printer_id
     * @return String
     */
    public String get_printer_ip(int printer_id){
        String query = "SELECT printer_ip from PRINTER WHERE printer_id = ?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,printer_id);
            ResultSet rs = ppst.executeQuery();
            if ( rs.next() ){
                return rs.getString("printer_ip");
            }
            return null;
        }catch(SQLException e){
            database.nl.add("PRINTERIP-FAILED",
                    "Failed to get printer ip ("+e.toString()+")");
            return null;
        }
    }

    /**
     * Function for getting oid information from database
     * @param element_id
     * @return String
     */
    public String get_element_oid(int element_id){
        String query = "SELECT element_oid FROM ELEMENT WHERE element_id = ?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,element_id);
            ResultSet rs = ppst.executeQuery();

            if ( rs.next() ){
                return rs.getString("element_oid");
            }
            return null;
        }catch(SQLException e){
            database.nl.add("ELEMENTOID-FAILED","Failed to get element oid ("+e.toString()+")");
            return null;
        }
    }

    /**
     * Function for adding element data to database
     * @param element_name
     * @param element_details
     * @param element_oid
     * @param element_datatype
     * @return
     */
    public int add_element(String element_name,String element_details,String element_oid,String element_datatype){
        String query = "INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype) " +
                "VALUES (?,?,?,?,?);";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setString(1,element_name);
            ppst.setObject(2, LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            ppst.setString(3,element_details);
            ppst.setString(4,element_oid);
            ppst.setString(5,element_datatype);
            ppst.execute();
            database.nl.add("ELEMENT-ADD","Added new element to database ("+element_name+")");
            return 1;
        }catch(SQLException e){
            database.nl.add("ELEMENT-ADD-FAILED","Failed to add element to database ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for listing elements
     * @return ArrayList
     */
    public ArrayList<String> list_elements(){
        ArrayList<String> data = new ArrayList<>();
        String query = "SELECT * FROM ELEMENT;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(rs.getInt("element_id")+": "+rs.getString("element_name"));
            }
        }catch(SQLException e){
            database.nl.add("ELEMENT-LIST-FAILED","Failed to list elements ("+e.toString()+")");
        }
        return data;
    }
}
