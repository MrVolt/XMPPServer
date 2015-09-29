package mrvoltor.com.Handlers;

import mrvoltor.com.Listeners.IPacketListener;
import mrvoltor.com.Model.Packet;
import mrvoltor.com.Model.Session;
import mrvoltor.com.Model.SessionIndex;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Олег Скидан on 03.09.2015.
 */
public class DeliveryHandler implements IPacketListener {
    final static Logger logger = Logger.getRootLogger();
    SessionIndex sessionIndex;

    public DeliveryHandler(SessionIndex index) throws IOException {
        sessionIndex = index;
    }

    public void notify(Packet packet) {
        String recipient = packet.getTo();
        //Messages sent to the server are ignored
        if (recipient.equalsIgnoreCase(""/*Server.SERVER_NAME*/)) return;

        try {
            Session session = sessionIndex.getSession(recipient);
            if (session != null) {
                //Deliver the packet
                packet.writeXML(session.getWriter());
                //Recipients that are not online are dropped
            } else {
                return;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}

