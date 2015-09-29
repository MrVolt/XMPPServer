package mrvoltor.com.Handlers;

import mrvoltor.com.Model.Packet;
import mrvoltor.com.Model.PacketQueue;
import mrvoltor.com.Model.Session;
import mrvoltor.com.Model.StreamingCharFactory;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

public class JabberInputHandler extends DefaultHandler {

    PacketQueue packetQ;
    Session session;

    public JabberInputHandler(PacketQueue packetQueue) {
        packetQ = packetQueue;
    }

    public void process(Session session)
            throws IOException, SAXException {
        //Directly create a Xerces SAXParser
        SAXParser parser = new SAXParser();
        //Content handler for the SAX parser
        parser.setContentHandler(this);
        //Handle streaming XML
        parser.setReaderFactory(new StreamingCharFactory());
        //Save the session
        this.session = session;
        //Start the SAX parser parsing
        parser.parse(new InputSource(session.getReader()));
    }

    Packet packet;
    int depth = 0;

    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException
    {
        switch (depth++){
            case 0:
                if (qName.equals("stream:stream")){
                    Packet openPacket = new Packet(null,qName,namespaceURI,atts);
                    openPacket.setSession(session);
                    packetQ.push(openPacket);
                    return;
                }
                throw new SAXException("Root element must be <stream:stream>");
            case 1:
                packet = new Packet(null,qName,namespaceURI,atts);
                packet.setSession(session);
                break;
            default:
                Packet child = new Packet(packet,qName,namespaceURI,atts);
                packet = child;
        }
    }

    public void characters(char[] ch,
                           int start,
                           int length)
            throws SAXException{
        if (depth > 1){
            packet.getChildren().add(new String(ch,start,length));
        }
    }

    public void endElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName)
            throws SAXException {
        switch (--depth) {
            case 0:
                Packet closePacket = new Packet("/stream:stream");
                closePacket.setSession(session);
                packetQ.push(closePacket);
                break;
            case 1:
                packetQ.push(packet);
                break;
            default:
                packet = packet.getParent();
        }
    }
}