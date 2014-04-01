/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process.concreteprocess;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import process.ProcessFactory;
import process.ProcessType;
import protocol.Protocol;

/**
 *
 * @author javier.abella
 */
public class ProcessTypeOne extends ProcessType{
    
    private static Logger log = Logger.getLogger(ProcessTypeOne.class);

    private static final int TYPE = 1;
    private String sentenceIsOk;
    private String sentenceIsOk2;
    private String sentencegetErrors;
    private String sentenceGetNumberFiles;
    private long milisUltimoLog;
    private List<String> files;
    
    
    
    static
	{
	    ProcessFactory.getInstance().registerProcess(TYPE, new ProcessTypeOne());
	}

    
    @Override
    public ProcessType createProcess(Element elementProcess) {
        ProcessTypeOne instance = new ProcessTypeOne();
        instance.sentenceIsOk = elementProcess.getElementsByTagName("sentenceIsOk").item(0).getTextContent();
        instance.setName(elementProcess.getElementsByTagName("name").item(0).getTextContent());
        instance.setDescription(elementProcess.getElementsByTagName("description").item(0).getTextContent());
        instance.sentenceIsOk2 = elementProcess.getElementsByTagName("sentenceIsOk2").item(0).getTextContent();
        instance.sentencegetErrors = elementProcess.getElementsByTagName("sentencegetErrors").item(0).getTextContent();
        instance.sentenceGetNumberFiles = elementProcess.getElementsByTagName("sentenceGetNumberFiles").item(0).getTextContent();
        instance.milisUltimoLog = (Long.parseLong(elementProcess.getElementsByTagName("milisUltimoLog").item(0).getTextContent()));
        
        Node filesNode = elementProcess.getElementsByTagName("files").item(0);
        instance.files = new ArrayList<>();
        
        // Recuperamos las sentencias que nos muestran los ficheros incorrectos
        for(int i=0; i<filesNode.getChildNodes().getLength();i++){
            if(filesNode.getChildNodes().item(i).getNodeType()==Node.ELEMENT_NODE){
                instance.files.add(filesNode.getChildNodes().item(i).getTextContent());
            }
        }

        return instance;
    }

    @Override
    public void refreshOk(Protocol protocol) throws ExecutionException {
        try {
            SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
            Date hoy = new Date();
            hoy.setTime(hoy.getTime() - milisUltimoLog);
            boolean ok=false;
            String result = protocol.executeQuery(sentenceIsOk);
            //System.out.println("result: "+result);
            if(!sentenceIsOk2.isEmpty()){
                String result2 = protocol.executeQuery(sentenceIsOk2);
                if(!result2.isEmpty()){
                    Date fechaLog = sfd.parse(result2);
                    ok = result.equals("OK");
                    ok = ok && hoy.before(fechaLog);
//                    System.out.println("hoy: "+sfd.format(hoy));
//                    System.out.println("log: "+sfd.format(fechaLog));
                }
            }else{
                ok = result.equals("OK");
            }
            setOk(ok);
        } catch (ParseException ex) {
            log.error("Error parseando la fecha",ex);
        }
    }

    @Override
    public void refreshElementsWithError(Protocol protocol) throws ExecutionException{
        setNumberFilesToProcess(Long.parseLong(protocol.executeQuery(sentenceGetNumberFiles)));
        if(isOk()){
            clearErrors();
            clearFatalErrors();
                String errores = protocol.executeQuery(sentencegetErrors);
                if(!errores.equals("") && !errores.equals(" ")){
                    addError(errores);
                }
                clearFilesError();
                
                for(int i=0; i<files.size();i++){
                    String[] archivos = protocol.executeQuery(files.get(i)).split("\n");
                    for(String archivo:archivos){
                        if(!archivo.isEmpty() && archivo.length()>3){
                             addFilesError(archivo);
                        }
                       
                    }
                    
                }
        }else{
            clearErrors();
            addFatalError("El servicio está caido, no existe el PID definido de este proceso o está atascado");
        }
    }
    
}
