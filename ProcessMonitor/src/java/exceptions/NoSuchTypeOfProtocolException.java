/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author javier.abella
 */
public class NoSuchTypeOfProtocolException extends Exception{

    public NoSuchTypeOfProtocolException() {
    }

    public NoSuchTypeOfProtocolException(String message) {
        super(message);
    }

    public NoSuchTypeOfProtocolException(Throwable cause) {
        super(cause);
    }

    public NoSuchTypeOfProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
