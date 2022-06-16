//class for the mouse listener - detects mouse movement & clicks and runs the corresponding methods 
package insurmountable;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class GameMouseListener implements MouseListener, GameConstants {
	// reference to items effected by keyboard actions
	private Player player;
	private StartScreen startScreen;
	private EndScreen endScreen;

	GameMouseListener() {

	}

	public void mouseClicked(MouseEvent e) {
		// If the mouse is clicked within the box of the start, set menu to true
		if (startScreen.menu == false) {
			if (((e.getX() > 371) && (e.getX() < (620))) && ((e.getY() > 341) && (e.getY() < (390)))) {
				startScreen.menu = true;
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
