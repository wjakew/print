package usp.jakubwawak.print;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PrintApplication {
	public static int debug = 1;

	public static String version = "v1.0.0";
	public static String build = "print-171022REV1";

	public static void main(String[] args) {
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
			SpringApplication.run(PrintApplication.class, args);
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
	}

}
