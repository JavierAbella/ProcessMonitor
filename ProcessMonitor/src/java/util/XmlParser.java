/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import exceptions.NoSuchTypeOfProcessException;
import exceptions.NoSuchTypeOfProtocolException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import process.Host;
import process.ProcessFactory;
import process.ProcessType;

/**
 *
 * @author javier.abella
 */
public class XmlParser {
    private Document doc;
    
    public XmlParser(InputStream is) throws ParserConfigurationException, SAXException, IOException{ 
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	doc = dBuilder.parse(is);
	doc.getDocumentElement().normalize();
    }
    
    //TODO hacer comprobación de que se han introducido todos los parámetros o si no lanzar una excepción
    public List<Host> getHostList(){
        List<Host> hostList=new ArrayList<>();
        Element root = doc.getDocumentElement();
        NodeList nList = root.getChildNodes();
        
        // Recorremos nodos nivel 1 -> Hosts
        for(int i=0;i<nList.getLength();i++){
           if(nList.item(i).getNodeType() == Node.ELEMENT_NODE){
               try {
                   Element element = (Element) nList.item(i);
                   Host host = new Host();
                   host.setIp(element.getAttribute("ip"));
                   host.setPort(element.getAttribute("port"));
                   host.setUser(element.getAttribute("user"));
                   host.setPass(element.getAttribute("pass"));
                   host.setProtocol((element.getAttribute("protocol")));
                   host.setTemp(Long.parseLong(element.getAttribute("temp")));
                   List <ProcessType> processList = new ArrayList<>();
                   host.setProcessList(processList);
                   
                   // Recorremos nodos nivel 2 -> Process
                   for(int j=0;j<nList.item(i).getChildNodes().getLength();j++){
                       if(nList.item(i).getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE){
                            try {
                                Element elementProcess = (Element) nList.item(i).getChildNodes().item(j);
                                int type = Integer.parseInt(elementProcess.getElementsByTagName("type").item(0).getTextContent());
                                ProcessType process = ProcessFactory.getInstance().createProcess(type, elementProcess);
                                host.getProcessList().add(process);
                            } catch (NoSuchTypeOfProcessException ex) {
                               // TODO
                               // Marcar este proceso como no contemplado en el sistema
                            }
                       }
                   }
                   hostList.add(host);
               } catch (NoSuchTypeOfProtocolException ex) {
                   Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
               }
        }
        }
        return hostList;
    }
    
}
