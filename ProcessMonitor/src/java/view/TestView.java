/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import monitor.Monitor;
import process.Host;
import process.HostList;
import process.ProcessType;
import util.Util;

/**
 *
 * @author javier.abella
 */
public class TestView implements View{

    protected Monitor monitor;
    
    
    public TestView(Monitor monitor) {
        this.monitor=monitor;
    }
    
    @Override
    public synchronized void print(HostList hostList) {
        Util util = new Util();
        for(Host host:hostList.getHostList()){
            System.out.println("");
            System.out.println("************************************************************");
            System.out.println("******************* HOST: "+host.getIp()+" ***********************");
            System.out.println("************************************************************");
            System.out.println("Estado: "+host.isConnected());
            
            for(ProcessType process:host.getProcessList()){
                System.out.println("  ----------- Proceso: "+process.getName()+" -------------");
                System.out.println("    -> OK - "+process.isOk());
                System.out.println("    -> ERRORES - "+process.getErrors());
                System.out.println("    -> ERRORES FATALES - "+process.getFatalErrors());
                System.out.println("    -> ARCHIVOS:");
                for (String archivo:process.getFilesError()){
                    System.out.println("         -"+archivo);
                }
                System.out.println("  --------------------------------------------------------");
                
                if(process.showAlert()){
                    util.alert();
                    process.setAlert(false);
                }
                if(process.showFatalAlert()){
                    util.alertFatal();
                    process.setAlertFatal(false);
                }
                
            }
            System.out.println("************************************************************");
            System.out.println("************************************************************");
        }
    }

    @Override
    public void exit() {
        monitor.stop();
    }

    

    
}
