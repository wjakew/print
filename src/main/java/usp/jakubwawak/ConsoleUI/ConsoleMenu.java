/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.ConsoleUI;

import org.springframework.boot.SpringApplication;
import usp.jakubwawak.Maintanance.PrintLog;
import usp.jakubwawak.database.Database_Connector;
import usp.jakubwawak.database.Database_Job_Manager;
import usp.jakubwawak.database.Database_Manager;
import usp.jakubwawak.job_engine.PrinterJob_Engine;
import usp.jakubwawak.print.PrintApplication;
import usp.jakubwawak.scenaio.UpdateTonerData_Scenario;

import java.util.Scanner;

/**
 * Object for maintaing menu options
 */
public class ConsoleMenu {

    Database_Connector database;
    PrintLog log;
    String build;

    String [] args;
    Scanner sc;
    boolean flag_run;

    /**
     * Constructor
     * @param database
     * @param log
     * @param build
     */
    public ConsoleMenu(Database_Connector database, PrintLog log,String build,String[] args){
        this.database = database;
        this.args = args;
        this.log = log;
        this.build = build;
        flag_run = true;
        sc = new Scanner(System.in);
    }

    /**
     * Run menu - console menu
     */
    public void run(){
        while(flag_run){
            System.out.print(ConsoleColors.GREEN_BOLD_BRIGHT+"printapp>"+ConsoleColors.RESET);
            String input = sc.nextLine();
            int size = input.split(" ").length;
            System.out.print(ConsoleColors.BLUE_BOLD_BRIGHT);
            for(String word : input.split(" ")){
                switch(word){
                    case "exit":
                    {
                        log.add("EXIT","Service will exit");
                        System.out.println("Bye!");
                        log.dumpToFile();
                        System.exit(0);
                    }
                    case "info":
                    {
                        System.out.println("build: "+build);
                        System.out.println("by Jakub Wawak 2022");
                        break;
                    }
                    case "printeradd":
                    {
                        printer_menu(input);
                        break;
                    }
                    case "elementadd":
                    {
                        element_menu(input);
                        break;
                    }
                    case "job":
                    {
                        job_menu(input);
                        break;
                    }
                    case "instance":
                    {
                        Scanner sc = new Scanner(System.in);
                        System.out.print("New instance name:");
                        String instance_name = sc.nextLine();
                        if ( !instance_name.equals("") ){
                            database.update_instance_name(instance_name);
                        }
                        else{
                            System.out.println("Blank");
                        }
                        break;
                    }
                    case "updatetoner":
                    {
                        System.out.println("Toner update start");
                        UpdateTonerData_Scenario utds = new UpdateTonerData_Scenario();
                        utds.run_scenario();
                        System.out.println("Toner update finish");
                        break;
                    }
                }
            }
            System.out.print(ConsoleColors.RESET);
        }
    }

    /**
     * Function for creating job functionality
     * @param user_input
     */
    public void job_menu(String user_input){
        Database_Job_Manager djm = new Database_Job_Manager(database);
        Database_Manager dm = new Database_Manager(database);
        Scanner sc = new Scanner(System.in);
        for(String word: user_input.split(" ")){
            switch(word){
                case "add":
                {
                    System.out.println("Element list:");
                    for(String name : dm.list_elements()){
                        System.out.println(name);
                    }
                    System.out.println("--------------");
                    System.out.println("For setting empty element set id to -1");
                    if ( user_input.split(" ").length == 3 ){
                        String job_name = user_input.split(" ")[2];
                        System.out.print("element1_id: "); String element1_id = sc.nextLine();
                        System.out.print("element2_id: "); String element2_id = sc.nextLine();
                        System.out.print("element3_id: "); String element3_id = sc.nextLine();
                        System.out.print("element4_id: "); String element4_id = sc.nextLine();

                        try{
                            int ans = djm.create_print_job(job_name,Integer.parseInt(element1_id),
                                    Integer.parseInt(element2_id),Integer.parseInt(element3_id),
                                    Integer.parseInt(element4_id));
                            if ( ans == 1 ){
                                System.out.println("New printer job added!");
                            }
                        }catch(Exception e){
                            System.out.println("Wont element_id");
                        }
                    }
                    else{
                        System.out.println("Wrong command usage.");
                    }
                    break;
                }
                case "list":
                {
                    for(String line : djm.list_printjobs()){
                        System.out.println(line);
                    }
                    break;
                }
                case "runp":
                {
                    if ( user_input.split(" ").length == 4 ){
                        try{
                            PrinterJob_Engine pre = new PrinterJob_Engine(user_input.split(" ")[2]
                                    ,Integer.parseInt(user_input.split(" ")[3]));
                            pre.run();
                            pre.show_result();
                        }catch(Exception e){
                            System.out.println("Error! ("+e.toString()+")");
                        }
                    }
                    else{
                        System.out.println("Wrong command usage.");
                    }
                    break;
                }
            }

        }

    }

    /**
     * Function for creating element functionality in the menu
     * @param user_input
     */
    public void element_menu(String user_input){
        Database_Manager dm = new Database_Manager(database);
        Scanner sc = new Scanner(System.in);
        for(String word: user_input.split(" ")){
            switch(word){
                case "elementadd":
                {
                    System.out.print("element_name: ");String element_name = sc.nextLine();
                    System.out.print("element_details: ");String element_details = sc.nextLine();
                    System.out.print("element_oid: ");String element_oid = sc.nextLine();
                    System.out.print("element_datatype (1-integer,2-text): ");String element_datatype = sc.nextLine();
                    try{
                        int type = Integer.parseInt(element_datatype);
                        switch(type){
                            case 1:
                            {
                                element_datatype = "integer";
                                break;
                            }
                            case 2:
                            {
                                element_datatype = "text";
                                break;
                            }
                        }
                        int ans = dm.add_element(element_name,element_details,element_oid,element_datatype);
                        if ( ans == 1 ){
                            System.out.println("Element added!");
                        }
                        else{
                            System.out.println("Failed to add element! Check app log");
                        }
                    }catch(Exception e){
                        System.out.println("Wrong value");
                    }
                }
            }
        }
    }

    /**
     * Function for creating printer functionality in the menu
     * @param user_input
     */
    public void printer_menu(String user_input){
        Database_Manager dm = new Database_Manager(database);
        Scanner sc = new Scanner(System.in);
        for(String word: user_input.split(" ")){
            switch(word){
                case "printeradd":
                {
                    System.out.print("printer_name: ");String printer_name = sc.nextLine();
                    System.out.print("printer_ip: ");String printer_ip = sc.nextLine();
                    System.out.print("printer_localization: ");String printer_localization = sc.nextLine();
                    System.out.print("printer_model: ");String printer_model = sc.nextLine();
                    System.out.print("status: (1-active,2-error,3-disabled):");
                    try{
                        int status = Integer.parseInt(sc.nextLine());
                        String printer_status = "";
                        switch(status){
                            case 1:
                                printer_status = "active";
                                break;
                            case 2:
                                printer_status = "error";
                                break;
                            case 3:
                                printer_status = "disabled";
                                break;
                            default:
                                printer_status = "active";
                                break;
                        }
                        int ans = dm.add_printer(printer_name,printer_ip,printer_localization,printer_model,printer_status);
                        if( ans == 1 ){
                            System.out.println("Added new printer!");
                        }
                        else{
                            System.out.println("Failed to add printer. Check log data.");
                        }
                    }catch(Exception e){
                        System.out.println("Wrong value");
                    }
                    break;
                }
            }
        }
    }
}
