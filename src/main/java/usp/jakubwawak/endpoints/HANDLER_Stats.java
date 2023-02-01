/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerPrinter_View;

import java.util.ArrayList;

/**
 * Object for creating REST endpoint for getting current printer stats
 */
@RestController
public class HANDLER_Stats {

    @GetMapping("/stats")
    public ArrayList<Printer_View_Simple> printer_stats(){
        TonerPrinter_View tpv = new TonerPrinter_View(PrintApplication.database);
        tpv.load_view();
        ArrayList<Printer_View_Simple> data = new ArrayList<>();
        for(Printer_View printer : tpv.list_view){
            data.add(new Printer_View_Simple(printer));
        }
        PrintApplication.database.nl.add("REST-STATS","Requested stats endpoint");
        return data;
    }
}
