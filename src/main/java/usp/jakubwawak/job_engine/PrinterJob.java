/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.job_engine;

import usp.jakubwawak.print.PrintApplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * PrinterJob database object
 */
public class PrinterJob {

    int printer_job_id;
    String printer_job_name;
    int element1_id,element2_id,element3_id,element4_id;
    public ArrayList<Integer> element_id_list;
    boolean error;

    /**
     * Constructor
     * @param printer_job_id
     */
    public PrinterJob (int printer_job_id){
        this.printer_job_id = printer_job_id;
        element_id_list = new ArrayList<>();
        load_object(); // loading object data from database
    }

    /**
     * Loading object from database
     */
    void load_object(){
        String query = "SELECT * FROM PRINTER_JOB WHERE printer_job_id = ?;";
        try{
            PreparedStatement ppst = PrintApplication.database.con.prepareStatement(query);
            ppst.setInt(1,printer_job_id);
            ResultSet rs = ppst.executeQuery();
            if ( rs.next() ){
                printer_job_name = rs.getString("printer_job_name");
                element1_id = rs.getInt("element1_id");
                element2_id = rs.getInt("element2_id");
                element3_id = rs.getInt("element3_id");
                element4_id = rs.getInt("element4_id");
                element_id_list.add(element1_id);
                element_id_list.add(element2_id);
                element_id_list.add(element3_id);
                element_id_list.add(element4_id);
            }
        }catch(SQLException e){
            PrintApplication.database.nl.add("PRINTJOB-LOAD-FAILED",
                    "Failed to load print job ("+e.toString()+")");
            error = true;
        }
    }
}
