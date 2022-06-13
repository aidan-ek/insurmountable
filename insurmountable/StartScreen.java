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
	BufferedImage start;
	
	//Create constructor
	public StartScreen(String fileName) {
		loadSprite(fileName);
	}
	
	//Start load sprite method
	public void loadSprite(String fileName) {
		try {
			start = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			System.out.println("Couldn't Load Menu");
		}
	}	
}
