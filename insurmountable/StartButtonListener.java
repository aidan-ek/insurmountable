//this listener is associated with the start button on the start screen
package insurmountable;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class StartButtonListener implements ActionListener {  
    JFrame targetFrame;
    StartButtonListener(JFrame target) { 
        targetFrame = target;
    }
    public void actionPerformed(ActionEvent event)  {  
        System.out.println("Starting new Game");
        targetFrame.dispose();            //dispose of the start screen
        new GameFrameOOP();               //create a new frame - the game frame
    }
}