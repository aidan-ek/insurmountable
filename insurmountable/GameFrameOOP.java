//this template can be used as reference or a starting point
package insurmountable;


/**
 * [GameFrameOOP.java]
 * Necessary JFrame class
 * @author Mohammad/Aiden
 * Date June 08, 2022
 */

//Import Required
import javax.swing.JFrame;
//import javax.swing.JPanel;
//import java.awt.Toolkit;

class GameFrameOOP extends JFrame implements GameConstants{ 
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	GameFrameOOP() { 
    	
        super("Game Screen - Use arrows, a, d, mouse");  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GAME_W, GAME_H);
        this.setLocationRelativeTo(null); //start the frame in the center of the screen
        // this.setSize(Toolkit.getDefaultToolkit().getScreenSize()); //set the frame to full screen         
        // this.setUndecorated(true);     //set to true to remove title bar
        
        //add the panel to the frame
        this.add(new GamePanelOOP());
        this.setFocusable(false);         //focus on the game panel
        this.setVisible(true);   
        
    } 
}




