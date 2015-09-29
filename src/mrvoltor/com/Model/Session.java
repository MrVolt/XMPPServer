package mrvoltor.com.Model;

import mrvoltor.com.Listeners.IStatusListener;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Олег Скидан on 31.08.2015.
 */
public class Session {

    public static final int DISCONNECTED = 1;
    public static final int CONNECTED = 2;
    public static final int STREAMING = 3;
    public static final int AUTHENTICATED = 4;
    int status;

    public Session(Socket socket) {
        setSocket(socket);
    }

    public Session() {
        setStatus(DISCONNECTED);
    }

    JabberID jid;
    public JabberID getJID() {
        return jid;
    }
    public void setJID(JabberID newID) {
        jid = newID;
    }

    String sid;
    public String getStreamID() {
        return sid;
    }
    public void setStreamID(String streamID) {
        sid = streamID;
    }

    Socket sock;
    public Socket getSocket() {
        return sock;
    }
    public void setSocket(Socket socket) {
        sock = socket;
        setStatus(CONNECTED);
    }

    Writer out;
    public Writer getWriter() throws IOException {
        if (out == null) {
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        }
        return out;
    }

    Reader in;
    public Reader getReader() throws IOException {
        if (in == null) {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        }
        return in;
    }

    LinkedList statusListeners = new LinkedList();
    public boolean addStatusListener(IStatusListener listener) {
        return statusListeners.add(listener);
    }
    public boolean removeStatusListener(IStatusListener listener) {
        return statusListeners.remove(listener);
    }

    public int getStatus() {
        return status;
    }

    public synchronized void setStatus(int newStatus) {
        status = newStatus;
        ListIterator iterator = statusListeners.listIterator();
        while (iterator.hasNext()) {
            IStatusListener listener = (IStatusListener) iterator.next();
            listener.notify(status);
        }
    }
}
