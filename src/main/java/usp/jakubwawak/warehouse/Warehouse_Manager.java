/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.warehouse;

import usp.jakubwawak.database.Database_Connector;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.Warehouse_View;

import java.util.ArrayList;

/**
 * Object for managing supplies on warehouse
 */
public class Warehouse_Manager {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Warehouse_Manager(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for preparing raport data
     * @return ArrayList
     */
    public ArrayList<Warehouse_Error> prepare_raport(){
        Warehouse_View wv = new Warehouse_View(database);
        ArrayList<Warehouse_Error> view = new ArrayList<>();
        wv.load_view();

        for(Printer_View pv : wv.printers){
            if ( !pv.get_warehouse_supplay_empty().equals("") ){
                view.add(new Warehouse_Error(pv.printer_name, "No supplies: "+pv.get_warehouse_supplay_empty()));
            }
        }
        return view;
    }
}
