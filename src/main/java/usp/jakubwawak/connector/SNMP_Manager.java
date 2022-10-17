/**
 * by Jakub Wawak
 * kubawawak@gmail.com/j.wawak@usp.pl
 * all rights reserved
 */
package usp.jakubwawak.connector;

import org.apache.catalina.connector.Response;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SNMP_Manager {

    Snmp snmp;

    String ip_address;

    /**
     * Constructor
     * @param address
     */
    public SNMP_Manager(String address){
        ip_address = address;
    }

    /**
     Start the Snmp session. If you forget the listen() method you will not
     * get any answers because the communication is asynchronous
     * and the listen() method listens for answers.
     @throws IOException
     **/
    public void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp (transport);
        // Do not forget this line!
        transport.listen();
    }
    /**
    * This method returns a Target, which contains information about
    * where the data should be fetched and how.
     * @return Target
    **/
    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(ip_address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity (new OctetString("public"));
        target.setAddress (targetAddress);
        target.setRetries (2);
        target.setTimeout(1500);
        target.setVersion (SnmpConstants.version2c);
        return target;
    }

    /**
     *This method is capable of handling multiple OIDs
     * @param oids
     * @return ResponseEvent
     **/
    public ResponseEvent get (OID[] oids) throws IOException {
        PDU pdu = new PDU ();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType (PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if(event != null) {
            return event;
        }
        throw new RuntimeException ("GET timed out");
    }

    /**
     * Method which takes a single OID and returns the response from the agent as a String.
     * @param oid
     * @return String
     * @throws IOException
     */
    public String getAsString (OID oid) throws IOException {
        ResponseEvent event = get(new OID[] { oid });
        return event.getResponse().get
                (0).getVariable().toString();
    }
}
