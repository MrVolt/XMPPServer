package mrvoltor.com;

import mrvoltor.com.Handlers.CloseStreamHandler;
import mrvoltor.com.Handlers.DeliveryHandler;
import mrvoltor.com.Handlers.OpenStreamHandler;
import mrvoltor.com.Model.PacketQueue;
import mrvoltor.com.Model.Session;
import mrvoltor.com.Model.SessionIndex;
import mrvoltor.com.Threads.ProcessThread;
import mrvoltor.com.Threads.QueueThread;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //Need to create config file
    //Hardcoded info should be in configuration files
    final static public int JABBER_PORT = 5222;
    final static public String SERVER_NAME = "127.0.0.1";

    final static Logger logger = Logger.getRootLogger();

    static SessionIndex index = new SessionIndex();
    public static SessionIndex getUserIndex() {
        return index;
    }

    static public void main(String [] args) throws IOException {
        BasicConfigurator.configure();

        //The shared PacketQueue
        PacketQueue packetQueue = new PacketQueue();

        //Creating and starting QueueThread
        QueueThread qThread = new QueueThread(packetQueue);

        //Daemon threads don't keep an application running;
        //We rely on the main thread to do that
        qThread.setDaemon(true);

        //Register the packet handler classes with the QueueThread
        qThread.addPacketListener(new OpenStreamHandler(index),
                "stream:stream");
        qThread.addPacketListener(new CloseStreamHandler(index),
                "/stream:stream");
        qThread.addPacketListener(new DeliveryHandler(index),
                "");
        qThread.start();
        ServerSocket serverSocket;

        try {
            //Begin listening on Jabber port
            serverSocket = new ServerSocket(JABBER_PORT);
        } catch (IOException ex){
            //If port not available, server shuts down
            logger.error(ex.getMessage());
            return;
        }
        while (true){
            try {
                //Accept new connections forever
                Socket newSock = serverSocket.accept();
                Session session = new Session(newSock);

                //Create and start a thread to handle new connection
                ProcessThread processor = new ProcessThread(packetQueue,
                        session);
                processor.start();
            } catch (IOException ie){
                logger.error(ie.getMessage());
            }
        }
    }
}
