// class for the keyboard listener - detects key actions and runs the corresponding code
package insurmountable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class PlayerKeyListener implements KeyListener,GameConstants {
    //reference to items effected by keyboard actions
    private Player player;
   
    PlayerKeyListener(Player p) {
        player = p;
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT){
            player.arrowLeft = true;
        } else if (keyCode == KeyEvent.VK_RIGHT){
        	player.arrowRight = true;
        } else if (keyCode == KeyEvent.VK_UP){
        	player.arrowUp = true;
        } else if (keyCode == KeyEvent.VK_DOWN){
        	player.arrowDown = true;
        } else if (keyCode == KeyEvent.VK_Z){
        	player.keyZ = true;
        } else if (keyCode == KeyEvent.VK_X) {
        	player.keyX = true;
        }
        
        
    }   
    
    public void keyReleased(KeyEvent e) {
    	 int keyCode = e.getKeyCode();
         if (keyCode == KeyEvent.VK_LEFT){
             player.arrowLeft = false;
         } else if (keyCode == KeyEvent.VK_RIGHT){
         	player.arrowRight = false;
         } else if (keyCode == KeyEvent.VK_UP){
         	player.arrowUp = false;
         } else if (keyCode == KeyEvent.VK_DOWN){
         	player.arrowDown = false;
         } else if (keyCode == KeyEvent.VK_Z){
         	player.keyZ = false;
         } else if (keyCode == KeyEvent.VK_X) {
         	player.keyX = false;
         }

         

    }
}

