package mrvoltor.com.Threads;

import mrvoltor.com.Listeners.IPacketListener;
import mrvoltor.com.Model.Packet;
import mrvoltor.com.Model.PacketQueue;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Олег Скидан on 03.09.2015.
 */
public class QueueThread extends Thread {
    final static Logger logger = Logger.getRootLogger();
    PacketQueue packetQueue;

    public QueueThread(PacketQueue queue) {
        packetQueue = queue;
    }
    HashMap packetListeners = new HashMap();
    public boolean addPacketListener(IPacketListener listener, String element){
        if (listener == null || element == null){
            return false;
        }
        packetListeners.put(listener,element);
        return true;
    }
    public boolean removePacketListener(IPacketListener listener){
        packetListeners.remove(listener);
        return true;
    }
    public void run(){
        for( Packet packet = packetQueue.pull(); packet != null;  packet = packetQueue.pull()) {
            try {
                synchronized(packetListeners){
                    Iterator iterator = packetListeners.keySet().iterator();
                    while (iterator.hasNext()){
                        IPacketListener listener = (IPacketListener)iterator.next();
                        String element = (String)packetListeners.get(listener);
                        //An empty string "" indicates match anything
                        if (element.equals(packet.getElement())
                                || element.length() == 0){
                            listener.notify(packet);
                        }
                    }
                }
            //Continue to process packets no matter what happens
            } catch (Exception ex){
                logger.error(ex.getMessage());
            }
        }
    }
}
