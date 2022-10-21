/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import com.vaadin.flow.component.html.Pre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Object for maintaining database job data
 */
public class Database_Job_Manager {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_Job_Manager(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for listing print jobs
     * @return ArrayList
     */
    public ArrayList<String> list_printjobs(){
        String query = "SELECT * FROM PRINTER_JOB;";
        ArrayList<String> data = new ArrayList<>();
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ResultSet rs = ppst.executeQuery();
            while(rs.next()){
                data.add(rs.getInt("printer_job_id")+": "+rs.getString("printer_job_name"));
            }
        }catch(SQLException e){
            database.nl.add("PRINTJOBS-LIST-FAILED","Failed to list printjobs ("+e.toString()+")");
        }
        return data;
    }

    /**
     * Function for getting print job id
     * @param print_job_name
     * @return Integer
     */
    public int get_print_job_id(String print_job_name){
        String query = "SELECT printer_job_id FROM PRINTER_JOB WHERE printer_job_name=?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setString(1,print_job_name);
            ResultSet rs = ppst.executeQuery(query);
            if ( rs.next() ){
                return rs.getInt("printer_job_id");
            }
            return -1;
        }catch(SQLException e){
            database.nl.add("GETPRINTJOBID-FAILED","Failed to get print job id ("+e.toString()+")");
            return -2;
        }
    }

    /**
     * Function for creating print job
     * @param printer_job_name
     * @param element1_id
     * @param element2_id
     * @param element3_id
     * @param element4_id
     * @return Integer
     */
    public int create_print_job(String printer_job_name,int element1_id,
                                int element2_id,int element3_id,int element4_id){
        String query = "INSERT INTO PRINTER_JOB (printer_job_name,element1_id,element2_id,element3_id,element4_id) " +
                "VALUES (?,?,?,?,?);";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setString(1,printer_job_name);
            ppst.setInt(2,element1_id);
            ppst.setInt(3,element2_id);
            ppst.setInt(4,element3_id);
            ppst.setInt(5,element4_id);
            ppst.execute();
            database.nl.add("PRINTERJOB-ADD","Added new printer job ("+printer_job_name+")");
            return 1;
        }catch(SQLException e){
            database.nl.add("PRINTERJOB-ADD-FAILED","Failed to add new printer job ("+e.toString()+")");
            return -1;
        }
    }
}
