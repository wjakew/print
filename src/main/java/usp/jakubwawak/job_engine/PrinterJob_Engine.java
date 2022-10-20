/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.job_engine;


import usp.jakubwawak.connector.SNMP_Connector;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.print.PrintApplication;

import java.util.ArrayList;

/**
 * Object for running printer job
 */
public class PrinterJob_Engine {

    int printer_job_id;
    int printer_id;

    PrinterJob printerjob_obj;

    public ArrayList<String> result; // field to store engine print job result

    /**
     * Constructor
     * @param printer_job_id
     */
    public PrinterJob_Engine(int printer_job_id,int printer_id){
        this.printer_job_id = printer_job_id;
        this.printer_id = printer_id;
        result = new ArrayList<>();
    }

    public PrinterJob_Engine(String printer_job_name,int printer_id){

    }

    /**
     * Function for running printer job
     */
    public void run(){
        printerjob_obj = new PrinterJob(printer_job_id);
        if (printerjob_obj.error){
            // no error on the object
            try{
                // load printer ip
                Database_Manager dm = new Database_Manager(PrintApplication.database);
                String printer_ip = dm.get_printer_ip(printer_id);
                SNMP_Connector connector = new SNMP_Connector(printer_ip);
                connector.connect(); // loaded connection to printer

                for(int element_id : printerjob_obj.element_id_list){
                    if ( element_id != -1 ){
                        String element_oid = dm.get_element_oid(element_id);
                        result.add(connector.get_parameter(element_oid));
                    }
                    else{
                        result.add("-1");
                    }
                }
            }catch(Exception e){
                PrintApplication.database.nl.add("ENGINEPRINTJOB-FAILED"
                        ,"Failed to run print job ("+e.toString()+")");
            }
        }
        else{
            PrintApplication.database.nl.add("PRINTERJOB-ERROR"
                    ,"Failed to run print job!");
        }
    }

    /**
     * Function for showing result data
     */
    public void show_result(){
        System.out.println("PrinterJob: "+printerjob_obj.printer_job_name);
        System.out.print("ElementIDs: ");
        for(int element_id : printerjob_obj.element_id_list){
            System.out.print(element_id+" ");
        }
        System.out.print("\n");
        System.out.print("Results: ");
        for(String result_element : result){
            System.out.print(result_element+ " ");
        }
    }
}
