package mrvoltor.com.Threads;

import mrvoltor.com.Listeners.PacketListener;
import mrvoltor.com.Model.Packet;
import mrvoltor.com.Model.PacketQueue;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Олег Скидан on 03.09.2015.
 */
public class QueueThread extends Thread {
    PacketQueue packetQueue;

    public QueueThread(PacketQueue queue) {
        packetQueue = queue;
    }
    HashMap packetListeners = new HashMap();
    public boolean addPacketListener(PacketListener listener, String element){
        if (listener == null || element == null){
            return false;
        }
        packetListeners.put(listener,element);
        return true;
    }
    public boolean removePacketListener(PacketListener listener){
        packetListeners.remove(listener);
        return true;
    }
    public void run(){
        for( Packet packet = packetQueue.pull(); packet != null;  packet = packetQueue.pull()) {
            try {
                synchronized(packetListeners){
                    Iterator iterator = packetListeners.keySet().iterator();
                    while (iterator.hasNext()){
                        PacketListener listener = (PacketListener)iterator.next();
                        String element = (String)packetListeners.get(listener);
                        //An empty string "" indicates match anything
                        if (element.equals(packet.getElement())
                                || element.length() == 0){
                            listener.notify(packet);
                        }
                    }
                }
            //Continue to process packets no matter what happens
            } catch (Exception ex){ }
        }
    }
}
