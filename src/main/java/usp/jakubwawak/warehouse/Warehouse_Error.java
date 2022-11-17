package usp.jakubwawak.warehouse;

/**
 * Object for storing warehouse errors
 */
public class Warehouse_Error {

    String printer_name;
    String error_data;

    /**
     * Constructor
     * @param printer_name
     * @param error_data
     */
    public Warehouse_Error(String printer_name,String error_data){
        this.printer_name = printer_name;
        this.error_data =error_data;
    }

    public String getPrinter_name(){
        return printer_name;
    }

    public String getError_data(){
        return error_data;
    }
}
