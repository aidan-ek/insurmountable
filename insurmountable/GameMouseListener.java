//cum
//class for the mouse listener - detects mouse movement & clicks and runs the corresponding methods 
package insurmountable;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class GameMouseListener implements MouseListener {
    //reference to items effected by keyboard actions
    private Player player;
    
    GameMouseListener(Player p) {
        player = p;
    } 
    
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked!!");
        System.out.println("X:"+e.getX() + " y:"+e.getY());
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}

