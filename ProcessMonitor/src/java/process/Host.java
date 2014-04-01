/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;


import exceptions.NoSuchTypeOfProtocolException;
import java.net.ConnectException;
import java.util.List;
import protocol.Protocol;
import protocol.ProtocolFactory;

/**
 *
 * @author javier.abella
 */
public class Host {
    private String ip;
    private String port;
    private String user;
    private String pass;
    private Protocol protocol;
    private Long temp;
    private Long milis;
    
    private boolean connected = false;
    
    private List<ProcessType> processList;
    

    public Host() {
        this.milis = 0L;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public void setProtocol(String protocol) throws NoSuchTypeOfProtocolException {
        this.protocol = ProtocolFactory.getInstance().createProtocol(protocol);
    }

    public Protocol getProtocol() {
        return protocol;
    }
    
    

    public Long getTemp() {
        return temp;
    }

    public void setTemp(Long temp) {
        this.temp = temp;
    }

    public List<ProcessType> getProcessList() {
        return processList;
    }

    public void setProcessList(List<ProcessType> processList) {
        this.processList = processList;
    }
    
    public void connect() throws ConnectException{
        protocol.connect(ip, Integer.parseInt(port), user, pass);
        connected=true;
    }
    
    public void disconnect() throws ConnectException{
        protocol.disconnect();
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Long getMilis() {
        return milis;
    }

    public void setMilis(Long milis) {
        this.milis = milis;
    }
    
    
    
}
