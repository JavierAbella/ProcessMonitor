/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author javier.abella
 */
public abstract class Protocol {
    public abstract void connect(String IP, int port, String user, String pass) throws ConnectException;
    public abstract void disconnect() throws ConnectException;
    public abstract String executeQuery(String query) throws ExecutionException;
    public abstract Protocol createProtocol();
}
