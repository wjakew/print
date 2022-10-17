/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.print;

import usp.jakubwawak.connector.SNMP_Connector;

import java.io.IOException;

/**
 * Test class for SNMP Print application
 */
public class Test {

    /**
     * Function for running tests...
     */
    public void run() throws IOException {
        SNMP_Connector connector = new SNMP_Connector("10.10.24.77");
        connector.connect();
        connector.show_parameter(".1.3.6.1.2.1.43.11.1.1.9.1.2");
    }
}
