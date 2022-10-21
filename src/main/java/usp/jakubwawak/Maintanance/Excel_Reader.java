/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.Maintanance;

import java.io.File;
import java.io.FileInputStream;

/**
 * Object for reading excel files
 */
public class Excel_Reader {

    public String spreadsheet_path;
    public boolean file_exists;

    /**
     * Constructor
     * @param spreadsheet_path
     */
    public Excel_Reader(String spreadsheet_path){
        this.spreadsheet_path = spreadsheet_path;
        File file = new File(this.spreadsheet_path);
        file_exists = file.exists();
    }

    /**
     * Function for loading raw file data
     */
    void load_file(){

    }


}
