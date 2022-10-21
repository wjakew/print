/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.view.TonerPrinter_View;

@RestController
public class Data_Endpoint {

    @GetMapping("/data")
    public TonerPrinter_View get_toner_data(){
        TonerPrinter_View tpv = new TonerPrinter_View(PrintApplication.database);
        tpv.load_view();
        return tpv;
    }
}
