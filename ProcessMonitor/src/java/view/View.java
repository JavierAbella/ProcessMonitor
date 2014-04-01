/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;



import process.HostList;

/**
 *
 * @author javier.abella
 */
public interface View {
    
    public abstract void print(HostList hostList);
    public abstract void exit();
}
