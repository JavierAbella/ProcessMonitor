package process;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.w3c.dom.Element;
import protocol.Protocol;

/**
 *
 * @author javier.abella
 * 
 */
public abstract class ProcessType {
   private boolean ok;
   private List<String> errors;
   private List<String> fatalErrors;
   private List<String> lastErrors;
   private List<String> lastFatalErrors;
   private List<String> filesError;
   private long numberFilesToProcess; // Pasar al ProcessTypeOne
   private String name;
   private String description;
   private boolean alert;
   private boolean alertFatal;
   private boolean primeraAletra;
   
   
   public abstract ProcessType createProcess(Element elementProcess);
   public abstract void refreshOk(Protocol protocol) throws ExecutionException;
   public abstract void refreshElementsWithError(Protocol protocol) throws ExecutionException;

    public ProcessType() {
        ok=false;
        errors = new ArrayList<>();
        fatalErrors = new ArrayList<>();
        alert = false;
        alertFatal = false;
        filesError = new ArrayList<>();
        primeraAletra = false;
    }
    
    public boolean isOk() {
        return ok;
    }

    protected void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getFatalErrors() {
        return fatalErrors;
    }
    
    
    
    public void clearErrors(){
        errors.clear();
    }
    
     public void clearFatalErrors(){
        fatalErrors.clear();
    }

    protected void addError(String errors) {
        lastErrors=new ArrayList<>(this.errors);
        this.errors.add(errors);
        if(lastErrors.size()<this.errors.size()){
            alert=true;
        }else{
            alert=false;
        }
    }
    
    protected void addFatalError(String errors) {
        lastFatalErrors = new ArrayList<>(this.fatalErrors);
        if(!fatalErrors.contains(errors)){
            this.fatalErrors.add(errors);
            if(lastFatalErrors.size()<this.fatalErrors.size()){
                primeraAletra = true;
            }else{
                if (primeraAletra){
                    alertFatal=true;
                    primeraAletra=false; 
                }else{
                    primeraAletra=false;
                }
        }
        }else{
            if (primeraAletra){
                    alertFatal=true;
                    primeraAletra=false;
                }else{
                    primeraAletra=false;
                }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    public boolean showAlert() {
        return alert;
    }
    
    public boolean showFatalAlert() {
        return alertFatal;
    }

    public void addFilesError(String file) {
        filesError.add(file);
    }

    public void clearFilesError() {
        this.filesError.clear();
    }

    public List<String> getFilesError() {
        return filesError;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isAlertFatal() {
        return alertFatal;
    }

    public void setAlertFatal(boolean alertFatal) {
        this.alertFatal = alertFatal;
    }

    public long getNumberFilesToProcess() {
        return numberFilesToProcess;
    }

    public void setNumberFilesToProcess(long numberFilesToProcess) {
        this.numberFilesToProcess = numberFilesToProcess;
    }



    
    
   
   
}
