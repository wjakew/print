/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com / j.wawak@usp.pl
 */
package usp.jakubwawak.Maintanance;


import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;

/**
 * Object for semi toner update
 */
public class AutoTonerUpdater implements Runnable{

    @Override
    public void run() {
        while(true){
            try{
                UpdateTonerData_Scenario utds = new UpdateTonerData_Scenario();
                utds.run_scenario();
                PrintApplication.database.nl.add("AUTO-TONER-UPDATE","Toner update finish");
                Thread.sleep(3600000);
            }catch(Exception e){
                PrintApplication.database.nl.add("AUTO-TONER-UPDATE-FAILED","Failed to update toner data ("+e.toString()+")");
            }
        }

    }

}
