/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.database;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object for exporting data from .papp file
 */
public class Database_Exporter {
    /**
     * .papp file stores data with elements and printers for quick database export
     */

    Database_Connector database;

    /**
     * .papp file schema
     *
     * $papp printapp application configuration dump file
     * $by JakubWawak 2022
     * $
     *
     * !printer_name!printer_ip!printer_localization!printer_model!printer_status!
     * #element_name#element_time#element_details#element_oid#element_datatype
     */

    /**
     * Constructor
     * @param database
     */
    public Database_Exporter(Database_Connector database){
        this.database = database;
    }

}
