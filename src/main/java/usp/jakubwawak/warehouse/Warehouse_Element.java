/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.warehouse;

import oshi.jna.platform.mac.SystemB;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Object for representing warehouse element data
 */
public class Warehouse_Element {

    public int warehouse_element_id;
    public int printer_id;
    public String warehouse_element_name;
    public String warehouse_element_time;
    public String warehouse_element_category;

    /**
     * Constructor
     */
    public Warehouse_Element(ResultSet to_add){
        try {
            warehouse_element_id = to_add.getInt("warehouse_element_id");
            printer_id = to_add.getInt("printer_id");
            warehouse_element_name = to_add.getString("warehouse_element_name");
            warehouse_element_time = to_add.getObject("warehouse_element_time",LocalDateTime.class).toString();
            warehouse_element_category = to_add.getString("warehouse_element_category");
        }catch(SQLException e){
            PrintApplication.database.nl.add("WAREHOUSE-ELEMENT-FAILED","Failed to load data to object ("+e.toString()+")");
        }
    }

    public int getWarehouse_element_id(){
        return warehouse_element_id;
    }

    public String getPrinterName(){
        Database_Manager dm = new Database_Manager(PrintApplication.database);
        return dm.get_printer_name(printer_id);
    }

    public String getWarehouse_element_name(){
        return warehouse_element_name;
    }

    public String getWarehouse_element_time(){
        return warehouse_element_time;
    }

    public String getWarehouse_element_category(){
        return warehouse_element_category;
    }


}
