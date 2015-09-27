package mrvoltor.com.Model;

/**
 * Created by Олег Скидан on 31.08.2015.
 */
public class JabberID {
    String user;

    public JabberID(String user, String domain, String resource) {
        setUser(user);
        setDomain(domain);
        setResource(resource);
    }

    public JabberID(String jid) {
        setJID(jid);
    }

    public String toString() {
        StringBuffer jid = new StringBuffer();
        if (user != null) {
            jid.append(user);
            jid.append("@");
        }
        jid.append(domain);
        if (resource != null) {
            jid.append("/");
            jid.append(resource);
        }
        return jid.toString();
    }

    public String getUser() { return user; }
    public void setUser(String name){ user = name; }

    String domain;
    public String getServer() { return domain; }
    public void setServer(String name){ domain = name; }

    public String getDomain() { return domain; }
    public void setDomain(String name){ domain = name; }

    String resource;
    public String getResource() { return resource; }
    public void setResource(String value){ resource = value; }

    public boolean equalsDomain(String domain) {
        if (this.domain == null ^ domain == null) {
            return false;
        }
        return this.domain.equalsIgnoreCase(domain);
    }
    public boolean equalsDomain(JabberID testJid){
        return equalsDomain(testJid.domain);
    }
    public boolean equalsUser(String user){
        if ( this.user == null ^ user == null ) {
            return false;
        }
        return this.user.equalsIgnoreCase(user);
    }

    public boolean equalsUser(JabberID testJid){
        return equalsUser(testJid.user);
    }

    public boolean equalsResource(JabberID test){
        return equalsResource(test.resource);
    }

    public boolean equalsResource(String test){
        if ( resource == null ^ test == null ) {
            return false;
        }
        return resource.equalsIgnoreCase(test);
    }

    public boolean equalsUser(String user, String resource){
        return equalsUser(user) && resource.equals(resource);
    }

    public boolean equals(JabberID jid){
        return equalsUser(jid) && equalsDomain(jid) && equalsResource(jid);
    }

    public boolean equals(String jid){
        return equals(new JabberID(jid));
    }

    public void setJID(String jid){
        if (jid == null){
            user = null;
            domain = null;
            resource = null;
            return;
        }
        int atLoc = jid.indexOf("@");
        if (atLoc == -1){
            user = null;
        } else {
            user = jid.substring(0, atLoc).toLowerCase();
            jid = jid.substring(atLoc + 1);
        }
        atLoc = jid.indexOf("/");
        if (atLoc == -1) {
            resource = null;
            domain = jid.toLowerCase();
        } else {
            domain = jid.substring(0, atLoc).toLowerCase();
            resource = jid.substring(atLoc + 1).toLowerCase();
        }
    }
}
