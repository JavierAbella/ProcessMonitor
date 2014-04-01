/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitor;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import process.Host;
import process.HostList;
import process.ProcessType;
import util.Util;
import util.XmlParser;
import view.TestView;
import view.View;
import view.graphicview.MainGraphicView;

/**
 * @author javier.abella
 * 
 * Esta clase Main lanza el proceso que monotoriza todos los procesos identificados
 */
public class Monitor {
    static Logger log = Logger.getLogger(Monitor.class);

    private HostList hostList;
    private XmlParser xmlParser;
    private View view;
    private boolean run;
    private boolean firstTime;
    
    public HostList getHostList() {
        return hostList;
    }
    
    
    /**
     * 1. Inicializa y crea todos los procesos de monotorizacion
     */
    public Monitor(){
        log.debug("Creando la clase principal \"Monitor\"");
        /************* INICIALIZACIONES ************************/
        run = false;
        firstTime = true;
        /*******************************************************/
        log.debug("Registramos los protocolos y los procesos");
        try {
            Class.forName("protocol.concreteprotocol.ProtocolSSH");
            Class.forName("process.concreteprocess.ProcessTypeOne");
            try {
                Util util = new Util();
                log.debug("Creamos el XML Parser desde la fuente "+util.getProcessProperties());
                xmlParser = new XmlParser(util.getProcessProperties());
                hostList = new HostList();
                log.debug("Registramos los hosts, tal y como estan definidos en el XML");
                hostList.setHostList(xmlParser.getHostList());
            } catch (ParserConfigurationException ex) {
                log.error("Error parseando el XML de configuración",ex);
            } catch (SAXException ex) {
                log.error("Error SAX en el XML de configuración",ex);
            } catch (IOException ex) {
                log.error("Erroe de entrada/salida en el XMl de configuración");
            }
        } catch (ClassNotFoundException ex) {
            log.error("Error creando la clase Monitor, no se puede registrar un protocolo o proceso",ex);
        }
    }
    
    /**
     * 2. Coordina y conecta todos los hosts
     */
    public void start(){
        log.debug("Conectamos los hosts e inicializamos la vista asignada");
        run = true;
        for(Host host:hostList.getHostList()){
            try {
                host.connect();
                Date date = new Date();
                host.setMilis(date.getTime());
            } catch (ConnectException ex) {
                log.error("Error conectando al host: "+host.getIp(),ex);
            }
         }
        view = new TestView(this);
        log.debug("Conexiones realizadas y vista inicializada");
   }
    
    /**
     * 2. Coordina y conecta todos los hosts. Este método se una cuando no es necesario crear la vista
     */
    public void startBackground(){
        log.debug("Conectamos los hosts sin inicializar ninguna vista, modo Background");
        run = true;
        for(Host host:hostList.getHostList()){
            try {
                host.connect();
                Date date = new Date();
                host.setMilis(date.getTime());
            } catch (ConnectException ex) {
                log.error("Error conectando al host: "+host.getIp(),ex);
            }
         }
        log.debug("Conexiones realizadas");
   }
        
    /**
     * 3. Para los procesos 
     */
    public void stop(){
        log.debug("Liberamos las conexiones con los Hosts...");
        for(Host host:hostList.getHostList()){
            try {
                host.disconnect();
            } catch (ConnectException ex) {
                log.error("Error liberando la conexión con el host: "+host.getIp(),ex);
            }
         }
        log.debug("Conexiones liberadas");
    }
    
    public void launch(){
        log.debug("Creamos otro hilo que se encargará de realizar la monitorización en segundo plano");
        new Thread(new Runnable() {
            public void run() {
                while (run){
                    try {
                        refresh();
                        Thread.sleep(1000L);
                    } catch (InterruptedException ex) {
                        log.error(ex);
                    }
                }
            }
        }).start();
    }
    
    private void refresh(){
        for(Host host:getHostList().getHostList()){
            Date date = new Date();
            Long milisDate = date.getTime();
                if((host.getMilis()+host.getTemp())<milisDate || firstTime){
                    host.setMilis(milisDate);
                    if(!host.isConnected()){
                        try {
                            host.connect();
                        } catch (ConnectException ex) {
                            log.debug("No se ha podido volver a conectar al host: "+host.getIp());
                        }
                    }else{
                        for(ProcessType process:host.getProcessList()){
                            try {
                                process.refreshOk(host.getProtocol());
                                process.refreshElementsWithError(host.getProtocol());
                            } catch (ExecutionException ex) {
                                log.error(ex);
                                host.setConnected(false);
                            }
                        }
                    }
                }
         }
        firstTime=false;
        if(view!=null){
            view.print(getHostList());
        }
        
    }
}
