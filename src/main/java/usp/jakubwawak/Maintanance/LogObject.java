/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.Maintanance;

/**
 * Object for storing log data
 */
public class LogObject {

    public int applog_id;
    public String applog_code;
    public String applog_desc;
    public String applog_time;

    /**
     * Constructor
     * @param applog_id
     * @param applog_code
     * @param applog_desc
     * @param applog_time
     */
    public LogObject(int applog_id,String applog_code,String applog_desc,String applog_time){
        this.applog_id = applog_id;
        this.applog_code = applog_code;
        this.applog_desc = applog_desc;
        this.applog_time = applog_time;
    }

    public int getID(){
        return applog_id;
    }

    public String get_code(){
        return applog_code;
    }

    public String get_desc(){
        return applog_desc;
    }

    public String get_time(){
        return applog_time;
    }

    /**
     * Check if log data is correct to view in scope
     * @return boolean
     */
    public boolean validate(){
        return !applog_code.equals("VIEW-DETAILS") && !applog_code.equals("SNMPCONNECTOR")
                && !applog_code.equals("CONNECTION") && !applog_code.equals("DEBUG-FLAG")
                && !applog_code.equals("WVVIEW") && !applog_code.equals("PRINTERID-LIST")
                && !applog_code.equals("LASTTONER") && !applog_code.equals("TPVIEW")
                && !applog_code.equals("SCENARIO-UPDATE");
    }
}
