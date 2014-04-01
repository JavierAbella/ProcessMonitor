/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author javier.abella
 */
public class NoSuchTypeOfProcessException extends Exception{

    public NoSuchTypeOfProcessException() {
    }

    public NoSuchTypeOfProcessException(String message) {
        super(message);
    }

    public NoSuchTypeOfProcessException(Throwable cause) {
        super(cause);
    }

    public NoSuchTypeOfProcessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
