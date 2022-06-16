package insurmountable;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Boss implements GameConstants {
	private int width, height, x, y;
	private int health = BOSS_MAXHP;
	
	// timer for antimations
	private long animationStartTime = 0;
	
	// boss hitbox
    Rectangle hitbox;    
    
    // boss animations arrays
    private int currentAnimation = 0;
    private int currentFrame = 0;
    private ArrayList<BufferedImage>[] frames;
    
    // adjust when adding new animation sets
    private final int TOTAL_ANIMATIONS = 1;
    
    // boss attack constants
    private long IDLE_DELAY = 100;
    private long CLEAVE_WINDUP = 1000;
    
	
	// parameter constructor
    public Boss(int newX, int newY, String fileName) {
    	
    	// CURRENTLY MISSING BOSS SPRITES FOR PLACEHOLDER
		loadSprites(fileName);
		x = newX - (this.frames[0].get(1).getWidth() / 2);
		y = newY;
		hitbox = new Rectangle(this.x, this.y, this.frames[0].get(1).getWidth(), this.frames[0].get(1).getHeight());
	}
    
    
    // loads the boss sprites in the folder until failure. (catches missing file as it will always throw this)
    private void loadSprites(String fileName){
        this.frames = new ArrayList[TOTAL_ANIMATIONS];
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
 		g.drawRect(this.x, this.y, this.frames[0].get(1).getWidth(), this.frames[0].get(1).getHeight());
 	

 	}
 	
 	public void update() {
 		
 		switch (currentAnimation) {
 			case 0: idle(); break;
 			case 1: cleaveAttack(); break;
 			case 2: comboAttack(); break;
 			case 3: rayAttack(); break;
 			default: idle();
 		}
 		
 		
 	}
 	
 	public void randomAttack() {
 		animationStartTime = Time.getTime();
 		currentAnimation = ThreadLocalRandom.current().nextInt(1, TOTAL_ANIMATIONS + 1);
 		currentFrame = 0;
 	}
 	
 	public void idle() {
 		if (animationStartTime == 0) {
 			animationStartTime = Time.getTime();
 			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
 		} else if (Time.since(animationStartTime) >= IDLE_DELAY) {
 			animationStartTime = 0;
 		}
 	}
 	
 	public void cleaveAttack() {
 		if (animationStartTime == 0) {
 			animationStartTime = Time.getTime();
 			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
 		} else {
 			if (currentFrame == 0 && Time.since(animationStartTime) >= CLEAVE_WINDUP) {
 				animationStartTime = 0;
 				currentFrame = 1;
 			}
 		}
 	}
 	
 	public void comboAttack() {
 		
 	}
 	
 	public void rayAttack() {
 		
 	}
 	
 	public void hurt(int damage) {
 		health -= damage;
 	}
    
    // getters and setters
    public int getHealth() {
    	return health;
    }
    public void setHealth(int newHealth) {
    	health = newHealth;
    }
    public int getX() {
    	return x;
    }
    public void setX(int newX) {
    	x = newX;
    }
    public int getY() {
    	return y;
    }
    public void setY(int newY) {
    	y = newY;
    }
}
