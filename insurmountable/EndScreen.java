package insurmountable;

//Import required
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//Start class
public class EndScreen {
	//Declare methods
	static boolean gameEnd = false;
	BufferedImage playerWon;
	BufferedImage playerLost;
	
	//Create constructor
	public EndScreen(String fileName) {
		loadSprite(fileName);
	}
	
	//Start load sprite method
	public void loadSprite(String fileName) {
		try {
			playerWon = ImageIO.read(new File(fileName + "won" + ".png"));
			playerLost = ImageIO.read(new File(fileName+ "lost" + ".png"));
		} catch (IOException e) {
			System.out.println("Couldn't Load Menu");
		}
	}	
}