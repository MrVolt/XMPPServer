package mrvoltor.com.Handlers;

import mrvoltor.com.Model.JabberID;
import mrvoltor.com.Listeners.IPacketListener;
import mrvoltor.com.Model.Packet;
import mrvoltor.com.Model.Session;
import mrvoltor.com.Model.SessionIndex;
import org.apache.log4j.Logger;

/**
 * Created by Олег Скидан on 03.09.2015.
 */
public class OpenStreamHandler implements IPacketListener {
    final static Logger logger = Logger.getRootLogger();
    static int streamID = 0;
    SessionIndex sessionIndex;

    public OpenStreamHandler(SessionIndex index) {
        sessionIndex = index;
    }

    public void notify(Packet packet){
        try {
            Session session = packet.getSession();
            String from = packet.getFrom();

            if (from == null){
                session.getSocket().close();
                return;
            }

            session.setJID(new JabberID(from));
            session.setStatus(Session.STREAMING);
            session.setStreamID(Integer.toHexString(streamID++));
            sessionIndex.addSession(session);
            packet.setTo(packet.getFrom());
            packet.setFrom(""/*Server.SERVER_NAME*/);
            packet.setID(session.getStreamID());
            packet.writeXML();

        } catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
