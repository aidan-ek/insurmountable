package insurmountable;

/**
 * [Player.java]
 * Create user's player
 * @author Mohammad/Aiden
 * Date June 08, 2022
 */


//Import required
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

class Player extends Fighters { 

	//Declare variables
	private int lastM;
	boolean dodgeRolling = false;
	boolean invulnerable = false;
	boolean attacking = false;
	private long rollTimer, rollCooldown;
	private long attackTimer, attackCooldown;
	private int rollX, rollY;
	private int combo = 0;
	private long knockTimer, knockStart;
	private int knockbackDir = 0; //0 is neutral. 1, 2, 3, 4 are UP, RIGHT, LEFT, DOWN
	boolean arrowUp, arrowDown, arrowLeft, arrowRight, keyZ, keyX;
	
	private final int TOTAL_ANIMATIONS = 10;
        
    // parameter constructor
    public Player(int newX, int newY, String fileName) {
		super(newX, newY);
		
		loadSprites(fileName);
		setWidth(this.frames[1].get(0).getWidth());
		setHeight(this.frames[1].get(0).getHeight());
		setHealth(PLAYER_MAXHP);	
		hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());
		attackHitbox = new Rectangle();

	}
//----------------------------------------        
 // loads the player sprites in the folder until failure. (catches missing file as it will always throw this)
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
            		System.out.println("All player images loaded successfully.");
            	} else {
            		System.out.println("Error Loading Images: " + e);
            	}
            	 
            }
        } 
    }

	// draws hitbox and sprite
	public void draw(Graphics g) {
		
		int xOffset = 0;
		int yOffset = 0;
		switch (currentAnimation) {
			case 0: xOffset = -20; break;
			case 3: xOffset = -5; yOffset = -10; break;
			case 4: xOffset = -20; yOffset = -10; break;
		}
		
		// draws player
		System.out.println(currentAnimation + ", " + currentFrame);
		g.drawImage(this.frames[currentAnimation].get(currentFrame), getX() + xOffset, getY() + yOffset, null);
		g.setColor(Color.red);
		

	}


// Player movement   
	public void move() {
		// adds up the inputs to see the total offset of the player in a frame
		int moveDistX = 0;
		int moveDistY = 0;
		
		// checks if roll is still on cooldown
		if (Time.since(rollCooldown) >= DODGE_CD) {
			rollCooldown = 0;
		}
		// checks if attack is still on cooldown
		if(Time.since(attackCooldown) >= ATTACK_CD) {
			attackCooldown = 0;
		}
		
		//Display knockback
		if (currentAnimation == 9) {
			if (knockbackDir > 0) {
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				System.out.println(currentFrame);
				switch (knockbackDir) {
					
					case 4: moveDistY += KNOCKBACK_SPEED; break;
					case 3: moveDistX -= KNOCKBACK_SPEED; break;
					case 2: moveDistX += KNOCKBACK_SPEED; break;
					case 1: moveDistY -= KNOCKBACK_SPEED; break;
					
					default: moveDistY = 0; moveDistX = 0;
				}
				if((Time.since(knockStart)) >= knockTimer) {
					knockbackDir = 0;
					knockTimer = 0;
					knockStart = 0;
					currentAnimation = 0;
					currentFrame = 0;
				}
			} else {
				currentAnimation = 0;
			}
			
		}	
		// player cannot move while rolling/attacking
		else if(attacking) {
			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
			//Time for attack
			if((Time.since(attackTimer)) >= ATTACK_TIME) {
				attacking = false;
				attackCooldown = Time.getTime();
			}
		}
		else if (dodgeRolling) {
			moveDistX = rollX;
			moveDistY = rollY;
			currentAnimation = 4;
			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
			//Time for dodge
			if((Time.since(rollTimer)) >= DODGE_TIME) {
				dodgeRolling = false;
				rollCooldown = Time.getTime();
			}
		} else {
			// cycles through movement frames using mod and change distance
			if (arrowDown) {
				currentAnimation = 0;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistY += RUN_SPEED;
				lastM = 0;
			}
			if (arrowLeft) {
				currentAnimation = 1;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistX -= RUN_SPEED;
				lastM = 1;
			}
			if (arrowRight) {
				currentAnimation = 2;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistX += RUN_SPEED;
				lastM = 2;
			}
			if (arrowUp) {
				currentAnimation = 3;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistY -= RUN_SPEED;
				lastM = 3;
			}
			
			// resets animation if no keys are pressed
			if(!arrowDown && !arrowUp && !arrowRight && !arrowLeft && currentAnimation == frames[currentAnimation].size() && currentAnimation != 9) {
				currentAnimation = lastM;
				currentFrame = 0;
			}
			
			// dodge roll and attack
			if (keyX && rollCooldown == 0) {
				dodgeRolling = true;
				currentAnimation = 4;
				currentFrame = 0;
				rollTimer = Time.getTime();
				if (moveDistX != 0 && moveDistY != 0) {
					moveDistX /= 1.41;
					moveDistY /= 1.41;
				}
				//Roll speeds
				rollX = Integer.signum(moveDistX) * DODGE_SPEED;
				rollY = Integer.signum(moveDistY) * DODGE_SPEED;
			} else if(keyZ && attackCooldown == 0) {
				attacking = true;				
				attackTimer = Time.getTime();	
				//Create hitboxes for attacking 
				if(lastM == 0) {
					attackHitbox = new Rectangle(getX(), getY()+60, 50, 50); 
					currentAnimation = 8;
				}
				if(lastM == 1) {
					attackHitbox = new Rectangle(getX()-60, getY(), 50, 50); 
					currentAnimation = 6;
				}
				if(lastM == 2) {
					attackHitbox = new Rectangle(getX()+60, getY(), 50, 50);
					currentAnimation = 5;
				}
				if(lastM == 3) {
					attackHitbox = new Rectangle(getX(), getY()-60, 50, 50);
					currentAnimation = 7;
				}
			}
		}
		
		// normalizes the vectors to not have increased speed diagonally
		if (moveDistX != 0 && moveDistY != 0) {
			moveDistX /= 1.3;
			moveDistY /= 1.3;
		}
		
		// moves the player based on the keys held
		setX(getX() +moveDistX);
		setY(getY() +moveDistY);
		
		//Create borders 
		if(!((getX() + moveDistX)+getWidth()+13 < GAME_W)) {
			setX(GAME_W-getWidth()-13);
		}
		if(!(getX() + moveDistX >= 0)){
			setX(0);
		}
		if(!((getY() + moveDistY)+getHeight()+40 < GAME_H-50)) {
			setY(GAME_H-getHeight()-40-50);
		}
		if(!(getY() + moveDistY >= 150)){
			setY(150);
		}
		
		//updates the player hitbox
		hitbox = new Rectangle(getX(), getY(), getWidth(), getHeight());

	}
	
	//Create combo method 
	public void comboAdd() {
		if(combo < MAX_COMBO) {
			combo += 1;
		}		
	}
	
	//Create combo reset method
	public void comboReset() {
		combo = 0;
	}
	
	//Create touched boss method
	public void touchedBoss() {
		if (dodgeRolling) {
			rollX = 0;
			rollY = 0;
			rollTimer = 0;
			hurt(0, lastM+1, 100);
		} else {
			hurt(1, lastM+1, 100);
		}
		

	}
	
	// overload hurt method to either just apply damage or knockback the player
	public void hurt(int damage) {
		setHealth(getHealth() - damage);
		currentAnimation = 9;
		combo = 0;
	}
	public void hurt(int damage, int knockDirection, long knockTime) { //for knockdirection: 1, 2, 3, 4 are UP, RIGHT, LEFT, DOWN
		setHealth(getHealth() - damage);
		currentAnimation = 9;
		knockTimer = knockTime;
		knockStart = Time.getTime();
		knockbackDir = knockDirection;
		combo = 0;
		arrowUp = false;
		arrowDown = false;
		arrowLeft = false;
		arrowRight = false;
	}
	
	// getters and setters
	public long getRollCooldown() {
    	return Time.since(rollCooldown);
    }
	public int getCombo() {
		return combo;
	}
	
   
}
