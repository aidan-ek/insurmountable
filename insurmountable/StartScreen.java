package insurmountable;

//Import required
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//Start class
public class StartScreen {
	//Declare methods
	static boolean menu = false;
	static boolean info = false;
	BufferedImage start;
	BufferedImage moves;
	
	//Create constructor
	public StartScreen(String fileName) {
		loadSprite(fileName);
	}
	
	//Start load sprite method
	public void loadSprite(String fileName) {
		try { 
			//Add the images of the start screen and the moves screen
			start = ImageIO.read(new File(fileName + "startscreen.png"));
			moves = ImageIO.read(new File(fileName + "infoscreen.png"));
		} catch (IOException e) {
			System.out.println("Couldn't Load Menu");
		}
	}	
}
