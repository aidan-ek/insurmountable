package insurmountable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Boss implements GameConstants {
	private int width, height, x, y;
	private int health = 100;
	
	// boss hitbox
    Rectangle bossHitbox;    
    
    // player animations array
    private int currentAnimation = 4;
    private int currentFrame = 3;
    private BufferedImage[][] frames = new BufferedImage[currentAnimation][currentFrame];
	
	// parameter constructor
    public Boss(int newX, int newY, String fileName) {
    	
    	// CURRENTLY MISSING BOSS SPRITES FOR PLACEHOLDER
		loadSprite(fileName);
		x = newX;
		y = newY;
	}
    
    
    // loads the boss sprites
    public void loadSprite(String fileName) {
		
		try {
			// loads all sprite animation files
			for (int row=0; row<currentAnimation; row++){
	            for (int col=0; col<currentFrame; col++){
	                frames[row][col] = ImageIO.read(new File(fileName+row+col+".png"));
	            }
	        }
			// sets the sprite to its first frame of the first animation
			currentAnimation = 0;
			currentFrame = 1;
		} catch (Exception e) {
			System.out.println("error loading Boss sprite");
		}
	}
    
    // getters and setters
    public int getHeath() {
    	return health;
    }
    public void setHealth(int newHealth) {
    	health = newHealth;
    }
}
