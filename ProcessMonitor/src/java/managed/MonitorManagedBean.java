/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managed;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import monitor.Monitor;
import org.apache.log4j.Logger;
import process.Host;
import process.HostList;

/**
 *
 * @author fredycat
 */
@ManagedBean
@ApplicationScoped
public class MonitorManagedBean{
    static Logger log = Logger.getLogger(MonitorManagedBean.class);

    private Monitor monitor;
    /**
     * Inicializamos todos los recursos para que estén disponibles
     */
    @PostConstruct
    private void init(){
        log.info("Inicializamos los recursos...");
        log.info("Creamos el monitor...");
        monitor = new Monitor();
        log.info("Monitor Creado");
        log.info("Conectamos con todos los host...");
        monitor.startBackground();
        log.info("Hosts conectados");
        log.info("Comenzamos la monitorización...");
        monitor.launch();
        log.info("Monitorización comenzada");
    }
    
    public List<Host> getHostList(){
        return monitor.getHostList().getHostList();
    }
    
    
    
    /**
     * Cerramos conexiones y demás elementos antes de terminar la aplicación
     */
    @PreDestroy
    private void stop(){
        log.info("Se ha mandado la instrucción de parado del servicio de monotorización");
        log.info("Comenzando a liberar todas las conexiones con los Hosts...");
        monitor.stop();
        log.info("Conexiones liberadas");
    }
    
}
