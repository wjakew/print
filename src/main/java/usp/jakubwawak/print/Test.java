/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.print;

import usp.jakubwawak.connector.SNMP_Connector;
import usp.jakubwawak.job_engine.PrinterJob;
import usp.jakubwawak.job_engine.PrinterJob_Engine;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;
import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerPrinter_View;

import java.io.IOException;

/**
 * Test class for SNMP Print application
 */
public class Test {

    /**
     * Function for running tests...
     */
    public void run() throws IOException {
        TonerPrinter_View tpv = new TonerPrinter_View(PrintApplication.database);
        tpv.load_view();
        System.out.print("Test");
    }
}
