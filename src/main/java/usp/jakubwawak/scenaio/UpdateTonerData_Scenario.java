/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.scenaio;

import com.vaadin.flow.component.notification.Notification;
import usp.jakubwawak.database.Database_Connector;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.database.Database_PrinterEvent;
import usp.jakubwawak.job_engine.PrinterJob_Engine;
import usp.jakubwawak.print.PrintApplication;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Object for creating scenario for Updating Toner data
 */
public class UpdateTonerData_Scenario {

    int printer_jobMAX_id;
    int printer_jobMIN_id;

    ArrayList<Integer> printer_ids;

    Database_Connector database;
    Database_Manager dm;

    /**
     * Constructor
     */
    public UpdateTonerData_Scenario(){
        this.database = PrintApplication.database;
        printer_jobMAX_id = 1;
        printer_jobMIN_id = 2;
        dm = new Database_Manager(database);
        printer_ids = dm.get_printer_ids();
        database.nl.add("SCENARIO","Running update toner data scenario");
    }

    /**
     * Constructor with given printer_ids collection
     * @param printer_ids
     */
    public UpdateTonerData_Scenario(ArrayList<Integer> printer_ids){
        this.database = PrintApplication.database;
        printer_jobMAX_id = 1;
        printer_jobMIN_id = 2;
        dm = new Database_Manager(database);
        this.printer_ids = printer_ids;
        database.nl.add("SCENARIO","Running update toner data scenario");
    }

    /**
     * Constructor with given printer_ids collection
     * @param printer_id
     */
    public UpdateTonerData_Scenario(int printer_id){
        this.database = PrintApplication.database;
        printer_jobMAX_id = 1;
        printer_jobMIN_id = 2;
        dm = new Database_Manager(database);
        this.printer_ids = new ArrayList<>();
        printer_ids.add(printer_id);
        database.nl.add("SCENARIO","Running update toner data scenario");
    }

    /**
     * Function for running scenario
     */
    public void run_scenario(){
        for(int current_printer_id : printer_ids){
            update_scenario(current_printer_id);
        }
    }

    /**
     * Function for updating scenario for printer
     */
    void update_scenario(int printer_id){
        try{
            System.out.println("----------------SCENARIO UPDATE PRINTER_ID: "+printer_id);
            PrinterJob_Engine pje_max = new PrinterJob_Engine(printer_id,printer_jobMAX_id);
            pje_max.run();
            PrinterJob_Engine pje_min = new PrinterJob_Engine(printer_id,printer_jobMIN_id);
            pje_min.run();
            ArrayList<Float> max_result = pje_max.get_float_result();
            ArrayList<Float> min_result = pje_min.get_float_result();

            System.out.println("max_result: "+max_result.get(0)+" "+max_result.get(1)+" "+max_result.get(2)+" "+max_result.get(3)+" ");
            System.out.println("min_result: "+min_result.get(0)+" "+min_result.get(1)+" "+min_result.get(2)+" "+min_result.get(3)+" ");
            //CMYB
            float cyan_value = 0,magenta_value = 0,yellow_value = 0,black_value = 0;
            //cyan data
            String cyan_data = "";
            try{
                cyan_value = calculate(max_result.get(0),min_result.get(0));
                if ( cyan_value != -69){
                    cyan_data = Float.toString(cyan_value);
                }
                else{
                    cyan_data = "NaN";
                }
            }catch(NullPointerException e){
                cyan_data = "NaN";
            }

            //magenta data
            String magenta_data = "";
            try {
                magenta_value = calculate(max_result.get(1),min_result.get(1));
                if ( magenta_value != -69){
                    magenta_data = Float.toString(magenta_value);
                }
                else{
                    magenta_data = "NaN";
                }
            }catch(NullPointerException e){
                magenta_data = "NaN";
            }

            //yellow data
            String yellow_data = "";
            try{
                yellow_value = calculate(max_result.get(2),min_result.get(2));
                if ( yellow_value != -69){
                    yellow_data = Float.toString(yellow_value);
                }
                else{
                    yellow_data = "NaN";
                }
            }catch(NullPointerException e){
                yellow_data = "NaN";
            }

            //black data
            String black_data = "";
            try{
                black_value = calculate(max_result.get(3),min_result.get(3));
                if ( black_value != -69){
                    black_data = Float.toString(black_value);
                }
                else{
                    yellow_data = "NaN";
                }
            }catch(NullPointerException e){
                black_data = "NaN";
            }

            Database_PrinterEvent dpe = new Database_PrinterEvent(database);
            String final_value = "C"+cyan_data + " M" + magenta_data + " Y" + yellow_data + " B" + black_data + " ";
            database.nl.add("SCENARIO-UPDATE","Updated printer (printer_id: "+printer_id+") with: "+final_value);
            dpe.add_toner_data(printer_id,final_value,cyan_value,magenta_value,yellow_value,black_value);
        }catch(Exception e){
            database.nl.add("SCENARIO-FAILED","Failed to update scenario ("+e.toString()+")");
        }
    }

    /**
     * Function for calculating toner %
     * @param max_result
     * @param min_result
     * @return Integer
     */
    float calculate(float max_result,float min_result){
        if ( max_result != -69 && min_result != -69){
            float result = min_result/max_result*100;
            return result;
        }
        else{
            return -69f;
        }
    }

}
