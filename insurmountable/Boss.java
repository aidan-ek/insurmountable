package insurmountable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Boss implements GameConstants {
	private int width, height, x, y;
	private int health = 100;
	
	// timer for antimations
	private long animationStartTime = 0;
	
	// boss hitbox
    Rectangle bossHitbox;    
    
    // boss animations arrays
    private int currentAnimation = 0;
    private int currentFrame = 0;
    private ArrayList<BufferedImage>[] frames;
	
	// parameter constructor
    public Boss(int newX, int newY, String fileName) {
    	
    	// CURRENTLY MISSING BOSS SPRITES FOR PLACEHOLDER
		loadSprites(fileName);
		x = newX;
		y = newY;
	}
    
    
    // loads the boss sprites in the folder until failure. (catches missing file as it will always throw this)
    private void loadSprites(String fileName){
        this.frames = new ArrayList[1];
        for(int j=0;j<frames.length; j++) {
        	System.out.println(j);
        	this.frames[j] = new ArrayList<BufferedImage>();
        	try {
        		int i = 0;
            	while(true) {
            		i++;
            		this.frames[j].add(ImageIO.read(new File(fileName+j+i+".png")));
            		System.out.println("File Success: " + fileName+j+i+".png");
            	}
            }
            catch(Exception e) {
            	if (("" + e).equals("javax.imageio.IIOException: Can't read input file!")) {
            		System.out.println("All boss images loaded successfully.");
            	} else {
            		System.out.println("Error Loading Images: " + e);
            	}
            	 
            }
        }
        
    }
    
    // draws hitbox and sprite
 	public void draw(Graphics g) {
 		
 		// draws boss
 		g.drawImage(this.frames[currentAnimation].get(currentFrame), this.x, this.y, null);
 		
 		// boss hitbox
 		g.setColor(Color.red);
 		g.drawRect(this.x, this.y, this.frames[currentAnimation].get(currentFrame).getWidth(), this.frames[currentAnimation].get(currentFrame).getHeight());
 	

 	}
 	
 	public void update() {
 		
 		// uses timers to slow down boss animations (this just a test of idle animation)
 		if (animationStartTime == 0) {
 			animationStartTime = Time.getTime();
 			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
 		} else if (Time.since(animationStartTime) >= 100) {
 			animationStartTime = 0;
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
