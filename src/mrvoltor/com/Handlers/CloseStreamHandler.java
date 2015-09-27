package mrvoltor.com.Handlers;

import mrvoltor.com.Listeners.PacketListener;
import mrvoltor.com.Model.Packet;
import mrvoltor.com.Model.Session;
import mrvoltor.com.Model.SessionIndex;

/**
 * Created by Олег Скидан on 03.09.2015.
 */
public class CloseStreamHandler implements PacketListener {
    SessionIndex sessionIndex;
    public CloseStreamHandler(SessionIndex index) {
        sessionIndex = index;
    }
    public void notify(Packet packet){
        try {
            packet.writeXML();
            Session session = packet.getSession();
            session.getSocket().close();
            sessionIndex.removeSession(session);
        } catch (Exception ex){
            sessionIndex.removeSession(packet.getSession());
        }
    }
}
