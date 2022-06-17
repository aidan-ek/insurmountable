package insurmountable;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Boss implements GameConstants {
	private int width, height, x, y;
	private int health = BOSS_MAXHP;
	private Player player;
	
	// timer for antimations
	private long animationStartTime = 0;
	
	// boss hitbox
    Rectangle hitbox;    
    Rectangle attackHitbox;
    
    // boss animations arrays
    private int currentAnimation = 0;
    private int currentFrame = 0;
    private ArrayList<BufferedImage>[] frames;
    private BufferedImage attackImage = null;
    private BufferedImage cleave;
    private BufferedImage comboSmall;
    private BufferedImage comboLarge;
    
    // adjust when adding new animation sets
    private final int TOTAL_ANIMATIONS = 2;
    
    // boss attack constants
    private long IDLE_DELAY = 100;
    // ------------------------
    private long CLEAVE_WINDUP = 900;
    private long CLEAVE_INDICATETIME = 400;
    private long CLEAVE_AFTERHIT = 500;
    private int CLEAVE_W = 800;
    private int CLEAVE_H = 400;
    // ------------------------
    private long COMBO_SMALL_WINDUP = 300;
    private long COMBO_SMALL_INDICATETIME = 300;
    private long COMBO_LARGE_WINDUP = 800;
    private long COMBO_LARGE_AFTERHIT = 1000;
    private int COMBO_SMALL_W = 200;
    private int COMBO_SMALL_H = 100;
    private int COMBO_LARGE_W = 400;
    private int COMBO_LARGE_H = 200;
    
    

    
	
	// parameter constructor
    public Boss(int newX, int newY, String fileName) {
    	
    	// CURRENTLY MISSING BOSS SPRITES FOR PLACEHOLDER
		loadSprites(fileName);
		cleave = loadAttack("cleave");
		comboSmall = loadAttack("comboSmall");
		comboLarge = loadAttack("comboLarge");
		width = this.frames[0].get(1).getWidth();
		height = this.frames[0].get(1).getHeight();
		x = newX - (width / 2);
		y = newY;
		hitbox = new Rectangle(this.x, this.y, this.frames[0].get(1).getWidth(), this.frames[0].get(1).getHeight());
		attackHitbox = new Rectangle();
		
		
	}
    
    private BufferedImage loadAttack(String fileName){
    	try {
    		return ImageIO.read(new File("src/images/Boss/Attacks/" + fileName + ".png"));
        }
        catch(Exception e) {
        	System.out.println("Error loading boss attack: " + e);
        	return null;
        }
        
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
            		this.frames[j].add(ImageIO.read(new File(fileName+j+i+".png")));
            		System.out.println("File Success: " + fileName+j+i+".png");
            		i++;
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
 		
 		
 		Graphics2D g2d = (Graphics2D) g;
 		// draws boss
 		g.drawImage(this.frames[currentAnimation].get(currentFrame), this.x, this.y, null);
 		
 		// boss hitbox
 		g.setColor(Color.red);
 		g.drawRect(this.x, this.y, this.frames[0].get(1).getWidth(), this.frames[0].get(1).getHeight());
 		
 		// draws boss attack and hitbox
 		g2d.draw(attackHitbox);
 	 	g.drawImage(attackImage, attackHitbox.x, attackHitbox.y, null);	
 		
 	

 	}
 	
 	public void update(Player p) {
 		player = p;
 		switch (currentAnimation) {
 			case 0: idle(); break;
 			case 1: cleaveAttack(); break;
 			case 2: comboAttack(); break;
 			case 3: rayAttack(); break;
 			default: idle();
 		}
 		
 		
 	}
 	
 	// sets the current attack to a random one
 	public void randomAttack() {
 		animationStartTime = Time.getTime();
 		currentAnimation = ThreadLocalRandom.current().nextInt(1, TOTAL_ANIMATIONS);
 		currentFrame = 0;
 	}
 	
 	
 	// plays idle animation
 	public void idle() {
 		if (animationStartTime == 0) {
 			animationStartTime = Time.getTime();
 			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
 		} else if (Time.since(animationStartTime) >= IDLE_DELAY) {
 			animationStartTime = 0;
 		}
 	}
 	
 	// starts cleave attack
 	public void cleaveAttack() {
 		if (animationStartTime == 0) { // windup
 			animationStartTime = Time.getTime();
 			currentFrame = 1;
 			attackHitbox = new Rectangle(this.x, this.y, 0, 0);
 		} else { //indicator
 			if (currentFrame == 0 && Time.since(animationStartTime) >= (CLEAVE_WINDUP + ThreadLocalRandom.current().nextInt(-400, 401))) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 			}  // actual hit frames
 			if (currentFrame == 1 && Time.since(animationStartTime) >= CLEAVE_INDICATETIME) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = new Rectangle(this.x + width/2 - CLEAVE_W/2, this.y, CLEAVE_W, CLEAVE_H);
 				attackImage = cleave;
 			} // after hit frames
 			if (currentFrame == 2 && Time.since(animationStartTime) >= CLEAVE_AFTERHIT) {
 				animationStartTime = Time.getTime();
 				currentFrame = 0;
 				currentAnimation = 0;
 				attackHitbox = new Rectangle();
 				attackImage = null;
 			}
 		}
 	}
 	
 	public void comboAttack() {
 		if (animationStartTime == 0) { // windup first hit
 			animationStartTime = Time.getTime();
 			currentFrame = 1;
 			attackHitbox = new Rectangle(this.x, this.y, 0, 0);
 		} else { 
 			// first hit indicator
 			if (currentFrame == 0 && Time.since(animationStartTime) >= COMBO_SMALL_WINDUP) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 			}  
 			// first hit frames
 			if (currentFrame == 1 && Time.since(animationStartTime) >= COMBO_SMALL_INDICATETIME) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = new Rectangle(player.getX() + width/2 - COMBO_SMALL_W/2, player.getY()-COMBO_SMALL_H/2, COMBO_SMALL_W, COMBO_SMALL_H);
 				attackImage = comboSmall;
 			} 
 			// second hit windup
 			if (currentFrame == 2 && Time.since(animationStartTime) >= COMBO_SMALL_WINDUP) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 			}  
 			
 			if (currentFrame == 2 && Time.since(animationStartTime) >= CLEAVE_AFTERHIT) {
 				animationStartTime = Time.getTime();
 				currentFrame = 0;
 				currentAnimation = 0;
 				attackHitbox = new Rectangle();
 				attackImage = null;
 			}
 		}
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
    public int getAnimation() {
    	return currentAnimation;
    }
}
