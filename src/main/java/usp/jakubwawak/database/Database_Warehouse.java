/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import usp.jakubwawak.warehouse.Warehouse_Element;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Object for maintaining warehouse data
 */
public class Database_Warehouse {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_Warehouse(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for loading category of template object
     * @param template_name
     * @return String
     */
    String get_template_category(String template_name){
        String query = "SELECT warehouse_element_template_category from WAREHOUSE_ELEMENT_TEMPLATE where warehouse_element_template_name = ?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setString(1,template_name);
            ResultSet rs  = ppst.executeQuery();
            if (rs.next()){
                return rs.getString("warehouse_element_template_category");
            }
            return null;
        }catch(SQLException e){
            database.nl.add("WAREHOUSE-TCATEGORY-FAILED","Failed to get category of template ("+e.toString()+")");
            return null;
        }
    }
    /**
     * Function for adding element
     * @param printer_id
     * @param warehouse_element_name
     * @param warehouse_element_category
     * @return Integer
     */
    int add_element(int printer_id,String warehouse_element_name,String warehouse_element_category){
        String query = "INSERT INTO WAREHOUSE_ELEMENT (printer_id,warehouse_element_name,warehouse_element_time,warehouse_element_category)" +
                " VALUES (?,?,?,?);";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,printer_id);
            ppst.setString(2,warehouse_element_name);
            ppst.setObject(3, LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            ppst.setString(4,warehouse_element_category);
            ppst.execute();
            database.nl.add("WAREHOUSE-ADD-ELEMENT","Added new element ("+warehouse_element_name+"/"+warehouse_element_category+") to printer_id: "+printer_id);
            return 1;
        }catch(SQLException e){
            database.nl.add("WAREHOUSE-ADD-FAILED","Failed to add element to warehouse ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for removing element from warehouse
     * @param printer_name
     * @param warehouse_element_name
     * @return Integer
     */
    public int remove_element(String printer_name,String warehouse_element_name){
        Database_Manager dm = new Database_Manager(database);
        int printer_id = dm.get_printer_id(printer_name);
        String query = "DELETE FROM WAREHOUSE_ELEMENT WHERE printer_id = ? and warehouse_element_name = ?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,printer_id);
            ppst.setString(2,warehouse_element_name);
            ppst.execute();
            database.nl.add("WAREHOUSE-REMOVE-ELEMENT","Removed element ("+warehouse_element_name+") from printer_id: "+printer_id);
            return 1;
        }catch(SQLException e){
            database.nl.add("WAREHOUSE-REMOVE-FAILED","Failed to remove element from warehouse ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for checking stock of element for given printer
     * @param printer_id
     * @param warehouse_element_name
     * @return
     */
    public int check_stock(int printer_id,String warehouse_element_name){
        String query = "SELECT warehouse_element_id FROM WAREHOUSE_ELEMENT WHERE printer_id = ? and warehouse_element_name = ?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,printer_id);
            ppst.setString(2,warehouse_element_name);
            ResultSet rs = ppst.executeQuery();
            if ( rs.next() ){
                return 1;
            }
            return 0;
        }catch(SQLException e){
            database.nl.add("WAREHOUSE-STOCK-FAILED","Failed to check stock ("+e.toString()+")");
            return -1;
        }
    }


    /**
     * Function for checking element
     * @param printer_name
     * @param warehouse_element_name
     * @return Integer
     */
    public int check_element(String printer_name, String warehouse_element_name){
        Database_Manager dm = new Database_Manager(database);
        int printer_id = dm.get_printer_id(printer_name);
        String query = "SELECT warehouse_element_id FROM WAREHOUSE_ELEMENT WHERE printer_id = ? and warehouse_element_name = ?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,printer_id);
            ppst.setString(2,warehouse_element_name);
            ResultSet rs = ppst.executeQuery();
            if ( rs.next() ){
                return 1;
            }
            return 0;
        }catch(SQLException e){
            database.nl.add("WAREHOUSE-REMOVE-FAILED","Failed to remove element from warehouse ("+e.toString()+")");
            return -1;
        }
    }

    /**
     * Function for adding element
     * @param printer_name
     * @param warehouse_element_template_name
     * @return
     */
    public int add_element(String printer_name,String warehouse_element_template_name){
        Database_Manager dm = new Database_Manager(database);
        int printer_id = dm.get_printer_id(printer_name);
        String template_category = get_template_category(warehouse_element_template_name);
        return add_element(printer_id,warehouse_element_template_name,template_category);
    }

    /**
     * Function for loading warehouse element data
     * @return ArrayList
     */
    public ArrayList<Warehouse_Element> load_warehouse(){
        String query = "SELECT * FROM WAREHOUSE_ELEMENT;";
        ArrayList<Warehouse_Element> data = new ArrayList<>();
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(new Warehouse_Element(rs));
            }
            database.nl.add("WAREHOUSE-LIST","Loaded "+data.size()+" warehouse elements");
        }catch(SQLException e){
            database.nl.add("WAREHOUSE-LIST-FAILED","Failed to list warehouse elements ("+e.toString()+")");
        }
        return data;
    }

    /**
     * Function for listing warehouse templates
     * @return ArrayList
     */
    public ArrayList<String> list_warehouse_templates(){
        ArrayList<String> data = new ArrayList<>();
        String query = "SELECT warehouse_element_template_name FROM WAREHOUSE_ELEMENT_TEMPLATE;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(rs.getString("warehouse_element_template_name"));
            }

        }catch(SQLException e){
            database.nl.add("WAREHOUSE-TEMP-FAILED","Failed to list warehouse templates ("+e.toString()+")");
        }
        return data;
    }
}
