/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.connector;

import org.snmp4j.smi.OID;
import usp.jakubwawak.print.PrintApplication;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Object for connecting to SNMP Server
 */
public class SNMP_Connector {

    public String ip_address;
    public ArrayList<String> raw_data;
    public SNMP_Manager client;
    String server_address;


    /**
     * Constructor
     * @param ip_address
     */
    public SNMP_Connector(String ip_address){
        this.ip_address = ip_address;
        server_address = "udp:"+this.ip_address+"/161";
    }

    public void connect() throws IOException {
        PrintApplication.database.nl.add("SNMPCONNECTOR","Connector object starting for "+ip_address);
        client = new SNMP_Manager(server_address);
        client.start();
    }

    /**
     * Function for showing parameter data
     * @param snmp_oid
     */
    public void show_parameter(String snmp_oid) throws IOException {
        System.out.println(client.getAsString(new OID(snmp_oid)));
    }

    /**
     * Function for getting parameter result from snmp connection
     * @param snmp_oid
     * @return String
     */
    public String get_parameter(String snmp_oid){
        try{
            PrintApplication.database.nl.add("SNMPCONNECTOR","Loading parameter "
                    +snmp_oid+"="+client.getAsString(new OID(snmp_oid)));
            return client.getAsString(new OID(snmp_oid));
        }
        catch(Exception e){
            return null;
        }
    }

}
