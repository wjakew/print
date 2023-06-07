/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com / j.wawak@usp.pl
 */
package usp.jakubwawak.database;

import usp.jakubwawak.Maintanance.PrintLog;
import usp.jakubwawak.print.PrintApplication;

public class Database_ReconnectService implements Runnable{

    int time;

    /**
     * Constructor
     */
    public Database_ReconnectService(){
        time = 3600000;
    }

    /**
     * Function for reconnecting database
     */
    @Override
    public void run() {
        try{
            while(true){
                PrintApplication.database.nl.add("DATABASE-RECONNECT","Database reconnection service");
                if ( PrintApplication.cs.configuration_file_exists ){
                    PrintApplication.database = new Database_Connector(PrintApplication.cs,new PrintLog());
                    PrintApplication.database.connect();
                    if ( PrintApplication.database.connected)
                        PrintApplication.database.nl.add("DATABASE-RECONNECT","Database reconnected!");
                }
                else{
                    PrintApplication.database.nl.add("DATABASE-RECONNECT-FAILED","Configuration time didn't exist!");
                }
                Thread.sleep(time);
            }
        }catch(Exception ex){
            PrintApplication.database.nl.add("DATABASE-RECONNECT-FAILED","Failed to thread reconnect ("+ex.toString()+")");
        }
    }
}
