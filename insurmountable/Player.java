package insurmountable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Player implements GameConstants { 
	private int lastM;
	private int x, y;
	private int width, height;
	boolean dodgeRolling = false;
	boolean attacking = false;
	private long rollTimer, rollCooldown;
	private long attackTimer, attackCooldown;
	private int rollX, rollY;
	private int health = 5;

	boolean arrowUp, arrowDown, arrowLeft, arrowRight, keyZ, keyX;
    
    // player hitbox
    Rectangle playerHitbox; 
    Rectangle swordHitbox;    
    
    
    // player animations array
    private int currentAnimation = 0;
    private int currentFrame = 0;
    private ArrayList<BufferedImage>[] frames;
    
    // adjust when adding new animation sets
    private final int TOTAL_ANIMATIONS = 9;
    
    // parameter constructor
    public Player(int newX, int newY, String fileName) {
		x = newX;
		y = newY;

		loadSprites(fileName);
		width = this.frames[0].get(1).getWidth();
		height = this.frames[0].get(1).getHeight();
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
            		i++;
            		this.frames[j].add(ImageIO.read(new File(fileName+j+i+".png")));
            		System.out.println("File Success: " + fileName+j+i+".png");
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
		
		// draws player
		g.drawImage(this.frames[currentAnimation].get(currentFrame), this.x, this.y, null);
		g.setColor(Color.red);
		
		// player hitbox
		g.drawRect(this.x, this.y, width, height);
		if((lastM == 0) && attacking) {
			g.drawRect(this.x-60, this.y, 50,50);
			
		}
		if(lastM == 1 && attacking) {
			g.drawRect(this.x+60, this.y, 50,50);
		}
		if(lastM == 2 && attacking) {
			g.drawRect(this.x, this.y-60, 50,50);
		}
		if(lastM == 3 && attacking) {
			g.drawRect(this.x, this.y+60, 50,50);
		}

	}

//----------------------------------------    
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
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
		
		// player cannot move while rolling/attacking
		if(attacking) {
			if(lastM == 0) {
				swordHitbox = new Rectangle(this.x-30, this.y, 10,10); 
				currentAnimation = 6;
			}
			if(lastM == 1) {
				swordHitbox = new Rectangle(this.x+30, this.y, 10,10);
				currentAnimation = 5;
			}
			if(lastM == 2) {
				swordHitbox = new Rectangle(this.x, this.y-30, 10,10);
				currentAnimation = 7;
			}
			if(lastM == 3) {
				swordHitbox = new Rectangle(this.x, this.y+30, 10,10); 
				currentAnimation = 8;
			}
			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
			if((Time.since(attackTimer)) >= DODGE_TIME) {
				attacking = false;
				attackCooldown = Time.getTime();
			}
		}
		else if (dodgeRolling) {
			moveDistX = rollX;
			moveDistY = rollY;
			currentAnimation = 4;
			currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
			if((Time.since(rollTimer)) >= DODGE_TIME) {
				dodgeRolling = false;
				rollCooldown = Time.getTime();
			}
		} else {
			
			if (arrowLeft) {
				// cycles through movement frames using mod
				currentAnimation = 1;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistX -= RUN_SPEED;
				lastM = 0;
			}
			if (arrowRight) {
				currentAnimation = 2;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistX += RUN_SPEED;
				lastM = 1;
			}
			if (arrowUp) {
				currentAnimation = 3;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistY -= RUN_SPEED;
				lastM = 2;
			}
			if (arrowDown) {
				currentAnimation = 0;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistY += RUN_SPEED;
				lastM = 3;
			}
			
			// dodge roll intialize
			if (keyZ && rollCooldown == 0) {
				dodgeRolling = true;
				currentAnimation = 4;
				currentFrame = 0;
				rollTimer = Time.getTime();
				if (moveDistX != 0 && moveDistY != 0) {
					moveDistX /= 1.41;
					moveDistY /= 1.41;
				}
				rollX = Integer.signum(moveDistX) * DODGE_SPEED;
				rollY = Integer.signum(moveDistY) * DODGE_SPEED;
			}
			if(keyX && attackCooldown == 0) {
				attacking = true;				
				attackTimer = Time.getTime();				
			}
		}
		
		// normalizes the vectors to not have increased speed diagonally
		if (moveDistX != 0 && moveDistY != 0) {
			moveDistX /= 1.3;
			moveDistY /= 1.3;
		}
		
		// moves the player based on the keys held
		this.x += moveDistX;
		this.y += moveDistY;
		if(!((this.x + moveDistX)+width+13 < GAME_W)) {
			this.x = GAME_W-width-13;
		}
		if(!(this.x + moveDistX >= 0)){
			this.x = 0;
		}
		if(!((this.y + moveDistY)+height+40 < GAME_H)) {
			this.y = GAME_H-height-40;
		}
		if(!(this.y + moveDistX >= 0)){
			this.y = 0;
		}
		
		// remakes the player hitbox
		playerHitbox = new Rectangle(this.x, this.y, this.frames[0].get(1).getWidth(), this.frames[0].get(1).getHeight());
	}
	
	
	// getters and setters
	public int getHeath() {
    	return health;
    }
    public void setHealth(int newHealth) {
    	health = newHealth;
    }

}
