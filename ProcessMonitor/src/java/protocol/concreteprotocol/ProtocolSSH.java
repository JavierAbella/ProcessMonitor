/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.concreteprotocol;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import protocol.Protocol;
import protocol.ProtocolFactory;

/**
 *
 * @author javier.abella
 */
public class ProtocolSSH extends Protocol{

    private static final String TYPE = "ssh";
    
    static
	{
	    ProtocolFactory.getInstance().registerProtocol(TYPE, new ProtocolSSH());
	}
    
    private JSch jsch;
    private Session session;
    
    
    @Override
    public void connect(String IP, int port, String user, String pass) throws ConnectException{
        try {
            jsch=new JSch();
            session=jsch.getSession(user, IP, port);
            session.setPassword(pass);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException ex) {
            throw new ConnectException(ex.getLocalizedMessage());
        }
    }

    @Override
    public void disconnect() {
         session.disconnect();
    }

    @Override
    public String executeQuery(String query) throws ExecutionException{
        String result="";
        ChannelExec channel=null;
        try{
            if (!session.isConnected()){
                throw new ExecutionException(new Exception("La sesión ha sido cerrada"));
            }
            channel=(ChannelExec) session.openChannel("exec");
            BufferedReader in=new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.setCommand(query);
            channel.connect();
            String msg;
            
            while((msg=in.readLine())!=null){
                result+=msg+"\n";
            }
            
            // Le quitamos el último salto de linea
            if(result.length()>0){
                result = result.substring(0,result.length()-1);
            }
            
        }catch(Exception e){
            throw new ExecutionException(e);
        }finally{
            if(channel!=null){
                channel.disconnect();
            }
        }
        
        return result;
    }

    @Override
    public Protocol createProtocol() {
        return new ProtocolSSH();
    }
    
}
