/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.view;

import usp.jakubwawak.database.Database_Connector;
import usp.jakubwawak.database.Database_Manager;

import java.util.ArrayList;

/**
 * Object for creating view
 */
public class TonerPrinter_View {

    Database_Connector database;
    public ArrayList<Printer_View> list_view;

    /**
     * Constructor
     * @param database
     */
    public TonerPrinter_View(Database_Connector database){
        this.database = database;
        list_view = new ArrayList<>();
    }

    /**
     * Function for loading view data
     */
    public void load_view(){
        database.nl.add("TPVIEW","Loading printer/toner view...");
        Database_Manager dm = new Database_Manager(database);
        ArrayList<Integer> printer_ids = dm.get_printer_ids();
        for(int printer_id : printer_ids){
            list_view.add(new Printer_View(printer_id));
        }
        database.nl.add("TPVIEW","Loaded "+list_view.size()+" elements");
    }
}
