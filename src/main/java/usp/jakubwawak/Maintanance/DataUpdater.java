/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.Maintanance;


import com.vaadin.flow.component.UI;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;

import java.util.TimerTask;

/**
 * Object for updating data on application and reloading the page
 */
public class DataUpdater extends TimerTask {

    /**
     * Function for reloading page data
     */
    public void run(){
        UpdateTonerData_Scenario utds = new UpdateTonerData_Scenario();
        utds.run_scenario();
        UI.getCurrent().getPage().reload();
    }
}
