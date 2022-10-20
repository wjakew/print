/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.print;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import usp.jakubwawak.ConsoleUI.ConsoleColors;
import usp.jakubwawak.ConsoleUI.ConsoleMenu;
import usp.jakubwawak.Maintanance.Configuration_Service;
import usp.jakubwawak.Maintanance.PrintLog;
import usp.jakubwawak.database.Database_Connector;

import java.io.Console;
import java.io.ObjectInputFilter;

@SpringBootApplication
public class PrintApplication {
	public static int debug = 0;

	public static String version = "v1.0.0";
	public static String build = "print-191022REV1";

	public static Database_Connector database;

	public static Configuration_Service cs;
	public static PrintLog log;

	/**
	 * Main app
	 * @param args
	 */
	public static void main(String[] args) {
		show_header();
		log = new PrintLog();
		cs = new Configuration_Service(log,build);
		if ( cs.configuration_file_exists ){
			cs.load_config();
			cs.show_config();
			run(args);
		}
		else{
			try{
				cs.user_load_config();
				cs.create_config("config_new.nami");
				cs.load_config();
				cs.show_config();
				run(args);
			}catch(Exception e){
				log.add("FATAL-ERROR","Fatal error: "+e.toString()+")");
			}

		}

	}

	/**
	 * Main app run function
	 * @param args
	 */
	public static void run(String[] args){
		database = new Database_Connector(cs,log);
		database.connect();
		if ( database.connected ){
			// setting current debug flag
			log.log_print_flag = database.get_debugFlag();
			if (debug == 1){
				System.out.println("Running tests...");
				Test test = new Test();
				try {
					test.run();
				}catch(Exception e){
					System.out.println("Tests failed ("+e.toString()+")");
				}
			}
			else{
				//runing app without tests
				ConsoleMenu cm = new ConsoleMenu(database,log,build,args);
				cm.run();
			}
		}
		else{
			// wrong database connection - error
			log.add("DB-ERROR-EXIT","Failed to load database connection!");
		}
	}

	/**
	 * Function for showing header data
	 */
	public static void show_header(){
		String header = "            _       _             _\n" +
				" _ __  _ __(_)_ __ | |_ ___ _ __ / \\   _ __  _ __\n" +
				"| '_ \\| '__| | '_ \\| __/ _ \\ '__/ _ \\ | '_ \\| '_ \\\n" +
				"| |_) | |  | | | | | ||  __/ | / ___ \\| |_) | |_) |\n" +
				"| .__/|_|  |_|_| |_|\\__\\___|_|/_/   \\_\\ .__/| .__/\n" +
				"|_|                                   |_|   |_| "+version+"/"+build;;
		System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT+header+ConsoleColors.RESET);
	}

}
