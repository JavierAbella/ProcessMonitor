/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author javier.abella
 */
public class Util {
    public void alert(){
        playSound(Constants.SONIDO_BEEP);
    }

    public void alertFatal() {
        playSound(Constants.SONIDO_ALERTA);
    }

    private synchronized void playSound(final String file) {
        new Thread(new Runnable() {
        // The wrapper thread is unnecessary, unless it blocks on the
        // Clip finishing; see comments.
          public void run() {
            try {        
            BufferedInputStream bis = new BufferedInputStream(this.getClass().getResourceAsStream("/util/sound/" + file));
            Player player = new Player(bis);
            player.play();
            } catch (JavaLayerException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }).start();
    }
     
     public InputStream getProcessProperties(){
         return this.getClass().getResourceAsStream(Constants.FICHERO_PROPIEDADES);
     }
            
    
}