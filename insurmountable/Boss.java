package insurmountable;

import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Boss extends Fighters {

	
	// timer for antimations
	private long animationStartTime = 0;
	
    Rectangle indicatorHitbox;
    

    private BufferedImage attackImage = null;
    private int attackImageX = 0, attackImageY = 0;
    private BufferedImage cleave;
    private BufferedImage comboSmall;
    private BufferedImage comboLarge;
    private BufferedImage ray;
    private long rayTimer = 0;
    private Player player;
    
    // adjust when adding new animation sets
    private final int TOTAL_ANIMATIONS = 4;
	
	// parameter constructor
    public Boss(int newX, int newY, String fileName) {
    	super(newX, newY);
		loadSprites(fileName);

		setWidth(this.frames[0].get(1).getWidth());
		setHeight(this.frames[0].get(1).getHeight());
		setX(newX - (getWidth() / 2));
		hitbox = new Rectangle(getX(), getY(), this.frames[0].get(1).getWidth(), this.frames[0].get(1).getHeight());
		attackHitbox = new Rectangle();
		indicatorHitbox = new Rectangle();
		setHealth(BOSS_MAXHP);
		
		// load attack images
		cleave = loadAttack("cleave");
		comboSmall = loadAttack("comboSmall");
		comboLarge = loadAttack("comboLarge");
		ray = loadAttack("ray");
		
		setX(newX - (getWidth() / 2));
		setY(newY);
		
		
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
 		

 		// draws the indicator area and boss attack
 		g.setColor(Color.RED);
 	 	g2d.fill(indicatorHitbox);
 	 	g2d.drawImage(attackImage, attackImageX, attackImageY, null);	
 	 	
 		// draw boss
 	 	g.drawImage(this.frames[currentAnimation].get(currentFrame), getX(), getY(), null);
 	 	g2d.draw(hitbox);

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
 		animationStartTime = 0;
 		currentAnimation = ThreadLocalRandom.current().nextInt(1, TOTAL_ANIMATIONS);
 		currentFrame = 0;
 	}
 	
 	
 	// plays idle animation
 	public void idle() {
 		if (animationStartTime == 0) {
 			animationStartTime = Time.getTime();
 		} 
 		if (Time.since(animationStartTime)%IDLE_DELAY+1 >= IDLE_DELAY) {
 			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
 		}
 		if (Time.since(animationStartTime) >= ATTACK_DELAY) {
 			randomAttack();
 		} 
 	}
 	
 	// starts cleave attack
 	public void cleaveAttack() {
 		if (animationStartTime == 0) { // windup
 			animationStartTime = Time.getTime();

 			attackHitbox = new Rectangle();
 			indicatorHitbox = new Rectangle(getX() + getWidth()/2 - CLEAVE_W/2, getY(), CLEAVE_W, CLEAVE_H);
 		} else { //indicator
 			// resets attack hitbox so it doesnt linger after hitting
 			attackHitbox = new Rectangle();
      
 			if (currentFrame == 0 && Time.since(animationStartTime) >= (CLEAVE_WINDUP + ThreadLocalRandom.current().nextInt(-400, 401))) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				
 			}  // actual hit frames
 			if (currentFrame == 1 && Time.since(animationStartTime) >= CLEAVE_INDICATETIME) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = indicatorHitbox;
 				indicatorHitbox = new Rectangle();
 				setAttackImage(cleave, attackHitbox.x, attackHitbox.y);
 			} // after hit frames
      
 			if (currentFrame == 2 && Time.since(animationStartTime) >= CLEAVE_AFTERHIT) {
 				animationStartTime = Time.getTime();
 				currentFrame = 0;
 				currentAnimation = 0;
 				attackHitbox = new Rectangle();
 				setAttackImage(null, 0, 0);
 			}
 		}
 	}
 	
 	public void comboAttack() {
 		if (animationStartTime == 0) { // windup first hit
 			animationStartTime = Time.getTime();
 			attackHitbox = new Rectangle();
 			indicatorHitbox = new Rectangle(player.getX() - (COMBO_SMALL_W-player.getWidth())/2, player.getY()-COMBO_SMALL_H/2, COMBO_SMALL_W, COMBO_SMALL_H);
 		} else { 
 			// resets attack hitbox so it doesnt linger after hitting
 			attackHitbox = new Rectangle();
 			// first hit indicator
 			if (currentFrame == 0 && Time.since(animationStartTime) >= COMBO_SMALL_WINDUP) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				
 			}  
 			// first hit frames
 			if (currentFrame == 1 && Time.since(animationStartTime) >= COMBO_SMALL_INDICATETIME) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = indicatorHitbox;
 				indicatorHitbox = new Rectangle();
 				setAttackImage(comboSmall, attackHitbox.x, attackHitbox.y);
 			}
 			// second hit windup
 			if (currentFrame == 2 && Time.since(animationStartTime) >= COMBO_SMALL_AFTERHIT) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = new Rectangle();
 				setAttackImage(null, 0, 0);
 				indicatorHitbox = new Rectangle(player.getX() - (COMBO_SMALL_W-player.getWidth())/2, player.getY()-COMBO_SMALL_H/2, COMBO_SMALL_W, COMBO_SMALL_H);
 			}  
 			// second hit indicator
 			if (currentFrame == 3 && Time.since(animationStartTime) >= COMBO_SMALL_WINDUP) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 			} 
 			// second hit frames
 			if (currentFrame == 4 && Time.since(animationStartTime) >= COMBO_SMALL_INDICATETIME) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = indicatorHitbox;
 				indicatorHitbox = new Rectangle();
 				setAttackImage(comboSmall, attackHitbox.x, attackHitbox.y);
 			}
 			// large third hit windup
 			if (currentFrame == 5 && Time.since(animationStartTime) >= COMBO_SMALL_AFTERHIT) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = new Rectangle();
 				setAttackImage(null, 0, 0);
 				indicatorHitbox = new Rectangle(player.getX() - (COMBO_LARGE_W-player.getWidth())/2, player.getY()-COMBO_LARGE_H/2, COMBO_LARGE_W, COMBO_LARGE_H);
 			}  
 			// third hit indicator
 			if (currentFrame == 6 && Time.since(animationStartTime) >= COMBO_LARGE_WINDUP) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 			} 
 			// third hit frames
 			if (currentFrame == 7 && Time.since(animationStartTime) >= COMBO_LARGE_INDICATETIME) {
 				animationStartTime = Time.getTime();
 				currentFrame++;
 				attackHitbox = indicatorHitbox;
 				indicatorHitbox = new Rectangle();
 				setAttackImage(comboLarge, attackHitbox.x, attackHitbox.y);
 			}
 			// end of attack
 			if (currentFrame == 8 && Time.since(animationStartTime) >= COMBO_LARGE_AFTERHIT) {
 				animationStartTime = 0;
 				currentFrame = 0;
 				attackHitbox = new Rectangle();
 				setAttackImage(null, 0, 0);
 				currentAnimation = 0;
 			}  
 		}
 	}
 	
 	public void rayAttack() {
 		if (animationStartTime == 0) { // windup
 			animationStartTime = Time.getTime();
 			rayTimer = Time.getTime();

 			attackHitbox = new Rectangle();
 			
 		} else {
 			// resets attack hitbox so it doesnt linger after hitting
 			attackHitbox = new Rectangle();
 			
 			// loops between currentFrame 0 and 1 until the attack timer ends
 			if (currentFrame == 0 && Time.since(rayTimer) >= (RAY_WINDUP)) {
 				rayTimer = Time.getTime();
 				currentFrame = 1;
 				indicatorHitbox = new Rectangle(player.getX() + ThreadLocalRandom.current().nextInt(-100, 101), getY(), RAY_W, RAY_H);
 				
 			}  
 			// actual hit frames
 			if (currentFrame == 1 && Time.since(rayTimer) >= RAY_INDICATETIME) {
 				rayTimer = Time.getTime();
 				currentFrame = 2;
 				attackHitbox = indicatorHitbox;
 				indicatorHitbox = new Rectangle();
 				setAttackImage(ray, attackHitbox.x, attackHitbox.y);
 			}
 			// actual hit frames
 			if (currentFrame == 2 && Time.since(rayTimer) >= 200) {
 				rayTimer = Time.getTime();
 				currentFrame = 0;
 				setAttackImage(null, 0, 0);
 			}
 			
 			// ends the attack
 			if (Time.since(animationStartTime) >= RAY_ENDTIME) {
 				animationStartTime = 0;
 				currentFrame = 0;
 				currentAnimation = 0;
 				attackHitbox = new Rectangle();
 				setAttackImage(null, 0, 0);
 				indicatorHitbox = new Rectangle();
 			}
 		}
 	}
 	
 	public void hurt(int damage) {
 		setHealth(getHealth() - damage);
 	}
 	
 	public void setAttackImage(BufferedImage image, int x, int y) {
 		attackImage = image;
 		attackImageX = x;
 		attackImageY = y;
 	}

    public int getAnimation() {
    	return currentAnimation;
    }
}
