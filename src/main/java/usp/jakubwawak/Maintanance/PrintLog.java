/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.Maintanance;

import java.util.ArrayList;

/**
 * Object for maintaining log data on database and file
 */
public class PrintLog {

    public ArrayList<String> log_history;

    /**
     * Constructor
     */
    public PrintLog(){
        this.log_history = new ArrayList<>();
    }

    /**
     * Function for adding log data
     * @param log_code
     * @param log_desc
     */
    public void add(String log_code,String log_desc){
        log_history.add(log_code+": "+log_desc);
    }

}
