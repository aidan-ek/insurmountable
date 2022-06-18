package insurmountable;


/**
 * [GameMouseListener.java]
 * class for the mouse listener
 * @author Mohammad/Aiden
 * Date June 08, 2022
 */

//Import Required
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class GameMouseListener implements MouseListener {

	// reference to items effected by keyboard actions
	private Player player;
	private StartScreen startScreen;
	private EndScreen endScreen;

	GameMouseListener() {

	}

	public void mouseClicked(MouseEvent e) {
		// If the mouse is clicked within the box of the start, set menu to true
		if (StartScreen.menu == false) {
			if (((e.getX() > 371) && (e.getX() < (620))) && ((e.getY() > 341) && (e.getY() < (390)))) {
				StartScreen.menu = true;
			}
			//If while in menu they click the moves, set info to true
			if(((e.getX() > 399) && (e.getX() < (596))) && ((e.getY() > 409) && (e.getY() < (444)))) {
				StartScreen.info = true;
			}
      
			//If in moves they click the close button, send back to menu
			if(startScreen.info) {

				if(((e.getX() > 57) && (e.getX() < (132))) && ((e.getY() > 42) && (e.getY() < (72)))) {
					StartScreen.info = false;
				}
			}
		}
 
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
