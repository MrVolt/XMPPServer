package mrvoltor.com.Threads;

import mrvoltor.com.Handlers.JabberInputHandler;
import mrvoltor.com.Model.PacketQueue;
import mrvoltor.com.Model.Session;
import org.apache.log4j.Logger;

/**
 * Created by Олег Скидан on 26.09.2015.
 */
public class ProcessThread extends Thread {
    final static Logger logger = Logger.getRootLogger();
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
            logger.error(ex.getMessage());
        }
    }
}
