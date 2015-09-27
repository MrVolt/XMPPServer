package mrvoltor.com.Threads;

import mrvoltor.com.Handlers.JabberInputHandler;
import mrvoltor.com.Model.PacketQueue;
import mrvoltor.com.Model.Session;

/**
 * Created by Олег Скидан on 26.09.2015.
 */
public class ProcessThread extends Thread {
    Session session;
    PacketQueue packetQueue;

    public ProcessThread(PacketQueue queue, Session session) {
        packetQueue = queue;
        this.session = session;
    }

    public void run() {
        try {
            JabberInputHandler handler = new JabberInputHandler(packetQueue);
            handler.process(session);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
