/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextArea;
import org.apache.poi.ss.formula.functions.T;
import usp.jakubwawak.database.Database_Connector;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.database.Database_Warehouse;
import usp.jakubwawak.print.PrintApplication;

import java.util.ArrayList;

/**
 * Object for loading warehouse view data
 */
public class Warehouse_View {

    Database_Connector database;

    public ArrayList<Printer_View> printers;

    /**
     * Constructor
     * @param database
     */
    public Warehouse_View(Database_Connector database){
        this.database = database;
        printers = new ArrayList<>();
    }

    /**
     * Load view
     */
    public void load_view(){
        database.nl.add("WVVIEW","Loading printer/warehouse view...");
        printers = new ArrayList<>();
        Database_Manager dm = new Database_Manager(database);
        ArrayList<Integer> printer_ids = dm.get_printer_ids();
        for(int printer_id : printer_ids){
            Printer_View pv = new Printer_View(printer_id);
            pv.load_printer_details();
            pv.load_warehouse_data();
            printers.add(pv);
        }
        database.nl.add("WVVIEW","Loaded "+printers.size()+" elements");
    }
}
