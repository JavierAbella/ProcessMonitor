/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managed;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.icefaces.ace.component.dialog.Dialog;
import process.ProcessType;

/**
 *
 * @author fredycat
 */
@ManagedBean
@ViewScoped
public class MainViewSBean{
    private static Logger log = Logger.getLogger(MainViewSBean.class);
    @ManagedProperty (value = "#{monitorManagedBean}")
    MonitorManagedBean monitorManagedBean;
    
    private ProcessType process;
    
    private Dialog typeOneDialog;

    public MainViewSBean() {
        process=null;
    }

    
    public MonitorManagedBean getMonitorManagedBean() {
        return monitorManagedBean;
    }

    public void setMonitorManagedBean(MonitorManagedBean monitorManagedBean) {
        this.monitorManagedBean = monitorManagedBean;
    }
    
    public void setProcessToViewFiles (ProcessType process){
        log.debug("Se ha seleccionado el proceso: "+process.getName());
        this.process = process;
        typeOneDialog.setVisible(true);
    }
    
    public void closeTypeOneDialog(){
        typeOneDialog.setVisible(false);
        
    }

    public ProcessType getProcess() {
        return process;
    }

    public Dialog getTypeOneDialog() {
        return typeOneDialog;
    }

    public void setTypeOneDialog(Dialog typeOneDialog) {
        this.typeOneDialog = typeOneDialog;
    }

    public String getDescription(){
        if (process!=null){
            return process.getDescription().replace("\\n", "<br/>");
        }
        return "";
    }
    
    
    
}
