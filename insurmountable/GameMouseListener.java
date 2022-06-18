//class for the mouse listener - detects mouse movement & clicks and runs the corresponding methods 
package insurmountable;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class GameMouseListener implements MouseListener {
	GameMouseListener() {

	}

	public void mouseClicked(MouseEvent e) {
		// If the mouse is clicked within the box of the start, set menu to true
		if (StartScreen.menu == false) {
			if (((e.getX() > 371) && (e.getX() < (620))) && ((e.getY() > 341) && (e.getY() < (390)))) {
				StartScreen.menu = true;
			}
			if(((e.getX() > 399) && (e.getX() < (596))) && ((e.getY() > 409) && (e.getY() < (444)))) {
				StartScreen.info = true;
			}
			if(StartScreen.info) {
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
