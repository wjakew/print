/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.endpoints;

import usp.jakubwawak.view.Printer_View;
import usp.jakubwawak.view.TonerSnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Object for
 */
public class Printer_View_Simple {
    public int printer_id;

    public String printer_name;
    public String printer_model;
    public String printer_ip;

    public String printer_localization;

    public String printer_serialnumber;

    LocalDateTime last_update;

    public float last_cyan_data, last_magenta_data, last_yellow_data, last_black_data;

    public ArrayList<TonerSnapshot> snapshots_list;

    /**
     * Constructor
     * @param og_obj
     */
    public Printer_View_Simple(Printer_View og_obj){
        printer_id = og_obj.printer_id;
        printer_name = og_obj.printer_name;
        printer_model = og_obj.printer_model;
        printer_ip = og_obj.printer_ip;
        printer_localization = og_obj.printer_localization;
        printer_serialnumber = og_obj.printer_serialnumber;
        last_update = og_obj.last_update;
        last_cyan_data = og_obj.last_cyan_data;
        last_magenta_data = og_obj.last_magenta_data;
        last_yellow_data = og_obj.last_yellow_data;
        last_black_data = og_obj.last_black_data;
        snapshots_list = og_obj.snapshots_list;
    }
}
