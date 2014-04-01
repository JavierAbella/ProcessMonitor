/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;


import exceptions.NoSuchTypeOfProtocolException;
import java.util.HashMap;
import protocol.concreteprotocol.ProtocolSSH;


/**
 * Factoria de creación de Procesos. Es Singleton
 * @author javier.abella
 */
public class ProtocolFactory {
    
    private static ProtocolFactory instance;
    
    private ProtocolFactory(){
        
    }
    
    public static ProtocolFactory getInstance(){
        if (instance == null){
            synchronized(ProtocolFactory.class){
                if (instance == null){
                    instance = new ProtocolFactory();
                }
            }            
        }

        return instance;
    }
    
    private HashMap protocolMap = new HashMap();
    
    public void registerProtocol (String protocolID, Protocol protocol){
        protocolMap.put(protocolID, protocol);
    }
    
    public Protocol createProtocol(String tipo) throws NoSuchTypeOfProtocolException{
        if(protocolMap.get(tipo)==null){
            throw new NoSuchTypeOfProtocolException();
        }
        Protocol protocol = (Protocol)protocolMap.get(tipo);
        return protocol.createProtocol(); 
    }

    
    
}
