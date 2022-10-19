/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.Maintanance;

import usp.jakubwawak.ConsoleUI.ConsoleColors;
import usp.jakubwawak.print.PrintApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;

/**
 * Object for maintaining log data on database and file
 */
public class PrintLog {

    public ArrayList<String> log_history;
    public int log_print_flag = 1;

    /**
     * Constructor
     */
    public PrintLog(){
        this.log_history = new ArrayList<>();
    }

    /**
     * Function for adding log data
     * @param log_code
     * @param log_desc
     */
    public void add(String log_code,String log_desc){
        log_history.add(log_code+": "+log_desc);
        if ( log_print_flag == 1){
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+log_code+ConsoleColors.RED_BOLD_BRIGHT+": "+log_desc+ConsoleColors.RESET);
        }
        try{
            String query = "INSERT INTO APPLOG (applog_time,applog_code,applog_desc) VALUES (?,?,?);";
            PreparedStatement ppst = PrintApplication.database.con.prepareStatement(query);
            ppst.setObject(1, LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            ppst.setString(2,log_code);
            ppst.setString(3,log_desc);
            ppst.execute();
        }catch(Exception e){
            log_history.add("DATABASE-LOG-ADD-FAILED: Failed to add log to database ("+e.toString()+")");
        }
    }

    /**
     * Function for dumping data to file
     */
    public void dumpToFile(){
        try{
            Date date = new Date();
            String current  = date.toString();
            current = current.replaceAll(" ", "");
            current = current.replaceAll("-","");
            current = current.replaceAll(":","");
            File file = new File("printapplog-"+current+".log");
            add("DUMP-EXIT","Saving to file!");
            if( file.exists() ){
                Writer output = new BufferedWriter(new FileWriter("printapplog-"+current+".log", true));
                for(String line : log_history){
                    output.append(line+"\n");
                }
                output.close();
            }
            else{
                FileWriter fw = new FileWriter("papplog"+current+".log");
                for ( String line : log_history){
                    fw.write(line+"\n");
                }
                fw.close();
            }
        }catch(Exception e){
            add("DUMP-FAILED","Failed to dump log data to file ("+e.toString()+")");
        }
    }

}
