/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package usp.jakubwawak.Maintanance;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Object for creating, storing and maintaning configuration files
 */
public class Configuration_Service {

    public String configuration_path;
    public ArrayList<String> raw_data;

    public String printapp_raw_database; //field for storing database ip for the database
    public String printapp_raw_database_user; //field for storing database user data
    public String printapp_raw_database_password; // field for storing database password

    public String printappklip_raw_database_name;
    public ArrayList<Integer> blacklist;
    public String build;
    public boolean configuration_file_exists;
    PrintLog nl;

    /**
     * raw file:
     * $configuration file for nami app / nami service
     * printapp_raw_database%XXXXXXXXXXXXX
     * printapp_raw_database_user%XXXXXXXXXXXXXXXXXX
     * printapp_raw_database_password%XXXXXXXXXXXXXX
     * $by Jakub Wawak 2022 / j.wawak@usp.pl / kubawawak@gmail.com
     */

    /**
     * Constructor
     */
    public Configuration_Service(PrintLog nl,String build){
        this.build = build;
        Path currentRelativePath = Paths.get("");
        this.nl = nl;
        configuration_path = "config_new.nami";
        String current_service_path = currentRelativePath.toAbsolutePath().toString();
        File dir = new File(current_service_path);
        File[] directoryListening = dir.listFiles();
        if ( directoryListening != null ){
            for(File child : directoryListening){
                if ( child.getAbsolutePath().contains(".nami") ){
                    configuration_path = child.getAbsolutePath();
                    nl.add("CONFIG-FILE-FOUND","Found configuration file: "+configuration_path);
                    configuration_file_exists = true;
                    break;
                }
            }
        }
        else{
            nl.add("CONFIG-FILE-NOTFOUND","Failed to found configuration file!");
            configuration_file_exists = false;
            configuration_path = "null";
        }
        this.printapp_raw_database_user = "";
        this.printapp_raw_database_password = "";
        blacklist = new ArrayList<>();
    }

    /**
     * Constructor with parameters for creating configuration files
     * @param printapp_raw_database
     * @param printapp_raw_database_user
     * @param printapp_raw_database_password
     * @param nl
     */
    public Configuration_Service(String printapp_raw_database,String printapp_raw_database_user,String printapp_raw_database_password,PrintLog nl){
        this.printapp_raw_database = printapp_raw_database;
        this.printapp_raw_database_user = printapp_raw_database_user;
        this.printapp_raw_database_password = printapp_raw_database_password;
        this.nl = nl;
    }

    /**
     * Function for loading configuration file
     */
    public void load_config(){

        /**
         * raw file:
         * $configuration file for nami app / nami service
         * printapp_raw_database%XXXXXXXXXXXXX
         * printapp_raw_database_user%XXXXXXXXXXXXXXXXXX
         * printapp_raw_database_password%XXXXXXXXXXXXXX
         * printapp_raw_database_name%XXXXXXXXXXXXXXXXX
         * $by Jakub Wawak 2022 / j.wawak@usp.pl / kubawawak@gmail.com
         */
        try{
            FileReader fr = new FileReader(configuration_path );
            BufferedReader bfr = new BufferedReader(fr);
            String line;
            while((line = bfr.readLine()) != null){
                if ( line.contains("printapp_raw_database%")){
                    printapp_raw_database = line.split("%")[1];
                }
                else if (line.contains("printapp_raw_database_user%")){
                    printapp_raw_database_user = line.split("%")[1];
                }
                else if (line.contains("printapp_raw_database_password")){
                    printapp_raw_database_password = line.split("%")[1];
                }
                else if (line.contains("printapp_raw_database_name")){
                    printappklip_raw_database_name = line.split("%")[1];
                }
            }
            nl.add("CONFIG-LOADED","Configuration file loaded");
        }catch(Exception e){
            System.out.println("Error! Failed to load lines ("+e.toString()+")");
            nl.add("CONFIG-FAILED","Error! Failed to load lines ("+e.toString()+")");
        }
    }

    /**
     * Function for scanning system
     */
    public void user_load_config() throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.print("printapp_database_ip?");
        String user_raw_database = sc.nextLine();
        System.out.print("printapp_database_user?");
        String user_raw_login = sc.nextLine();
        System.out.print("printapp_database_name?");
        String user_raw_database_name = sc.nextLine();
        char[] ch;
        try{
            Console cnsl = System.console();
            ch = cnsl.readPassword("printapp_database_password?");
        }catch(Exception e){
            System.out.print("printapp_database_password?");
            String user_raw_password = sc.nextLine();
            ch = user_raw_password.toCharArray();
        }
        if ( !user_raw_database.equals("") && !user_raw_login.equals("") && !String.copyValueOf(ch).equals("") && !user_raw_database_name.equals("") ){
            printapp_raw_database = user_raw_database;
            printapp_raw_database_user = user_raw_login;
            printapp_raw_database_password = String.copyValueOf(ch);
            printappklip_raw_database_name = user_raw_database_name;
            create_config(configuration_path);
        }
    }

    /**
     * Function for creating config file
     */
    public void create_config(String config_name) throws IOException {
        if ( config_name.equals(configuration_path)){
            config_name = configuration_path;
        }
        else {
            config_name = config_name.replaceAll(" ", "") + ".nami";
        }
        FileWriter fw = new FileWriter(config_name);
        fw.write("$configuration file for nami app / nami service - "+build+"\n");
        fw.write("printapp_raw_database%"+printapp_raw_database+"\n");
        fw.write("printapp_raw_database_user%"+printapp_raw_database_user+"\n");
        fw.write("printapp_raw_database_password%"+printapp_raw_database_password+"\n");
        fw.write("printapp_raw_database_name%"+printappklip_raw_database_name+"\n");
        fw.write("\n$by Jakub Wawak 2022 / j.wawak@usp.pl / kubawawak@gmail.com\n");
        fw.close();
        nl.add("CONFIG-SUCCESS","Configuration file created and saved");
    }

    /**
     * Function for showing configuration file
     */
    public void show_config(){
        System.out.println("Current configuration:");
        System.out.println("printapp_raw_database: "+printapp_raw_database);
        System.out.println("printapp_raw_database_name: "+printappklip_raw_database_name);
        System.out.println("printapp_raw_database_user: "+printapp_raw_database_user);
        System.out.println("printapp_raw_database_password: "+printapp_raw_database_password);
    }


    /**
     * Function for checking if object is empty
     * @return boolean
     */
    public boolean empty(){
        return printapp_raw_database.equals("") && printapp_raw_database_user.equals("") && printapp_raw_database_password.equals("") && printappklip_raw_database_name.equals("");
    }
}
