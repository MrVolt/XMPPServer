package mrvoltor.com.Model;

import java.util.LinkedList;

/**
 * Created by Олег Скидан on 01.09.2015.
 */
public class PacketQueue {

    //Actual storage is handled by a java.util.LinkedList
    LinkedList queue = new LinkedList();

    public synchronized void push(Packet packet){
        queue.add(packet);
        notifyAll();
    }

    public synchronized Packet pull(){
        try {
            while (queue.isEmpty()) {
                wait();
            }
        } catch (InterruptedException e){
            return null;
        }
        return (Packet)queue.remove(0);
    }
}

