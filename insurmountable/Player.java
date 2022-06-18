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
	boolean invulnerable = false;
	boolean attacking = false;
	private long rollTimer, rollCooldown;
	private long attackTimer, attackCooldown;
	private int rollX, rollY;
	private int combo = 0;
	private int health = PLAYER_MAXHP;
	private long knockTimer, knockStart;
	private int knockbackDir = 0; //0 is neutral. 1, 2, 3, 4 are UP, RIGHT, LEFT, DOWN

	boolean arrowUp, arrowDown, arrowLeft, arrowRight, keyZ, keyX;
    
    // player hitbox
    Rectangle hitbox; 
    Rectangle attackHitbox;    
    
    
    // player animations array
    private int currentAnimation = 0;
    private int currentFrame = 0;
    private ArrayList<BufferedImage>[] frames;
    
    // adjust when adding new animation sets
    private final int TOTAL_ANIMATIONS = 10;
    
    // parameter constructor
    public Player(int newX, int newY, String fileName) {
		x = newX;
		y = newY;

		loadSprites(fileName);
		
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
		
		// draws player
		g.drawImage(this.frames[currentAnimation].get(currentFrame), this.x, this.y, null);
		g.setColor(Color.red);
		
		// player hitbox
		g.drawRect(this.x, this.y, width, height);
		if(lastM == 0 && attacking) {
			g.drawRect(this.x, this.y+60, width, 50);
		}
		if((lastM == 1) && attacking) {
			g.drawRect(this.x-60, this.y, 50, height);
			
		}
		if(lastM == 2 && attacking) {
			g.drawRect(this.x+60, this.y, 50, height);
		}
		if(lastM == 3 && attacking) {
			g.drawRect(this.x, this.y-60, width,50);
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
			if((Time.since(rollTimer)) >= DODGE_TIME) {
				dodgeRolling = false;
				rollCooldown = Time.getTime();
			}
		} else {
			if (arrowDown) {
				currentAnimation = 0;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].size();
				moveDistY += RUN_SPEED;
				lastM = 0;
			}
			if (arrowLeft) {
				// cycles through movement frames using mod
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
			if(!arrowDown && !arrowUp && !arrowRight && !arrowLeft) {
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
				rollX = Integer.signum(moveDistX) * DODGE_SPEED;
				rollY = Integer.signum(moveDistY) * DODGE_SPEED;
			} else if(keyZ && attackCooldown == 0) {
				attacking = true;				
				attackTimer = Time.getTime();		
				if(lastM == 0) {
					attackHitbox = new Rectangle(this.x, this.y+60, 50,50); 
					currentAnimation = 8;
				}
				if(lastM == 1) {
					attackHitbox = new Rectangle(this.x-60, this.y, 50,50); 
					currentAnimation = 6;
				}
				if(lastM == 2) {
					attackHitbox = new Rectangle(this.x+60, this.y, 50,50);
					currentAnimation = 5;
				}
				if(lastM == 3) {
					attackHitbox = new Rectangle(this.x, this.y-60, 50,50);
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
		this.x += moveDistX;
		this.y += moveDistY;
		
		//Borders 
		if(!((this.x + moveDistX)+width+13 < GAME_W)) {
			this.x = GAME_W-width-13;
		}
		if(!(this.x + moveDistX >= 0)){
			this.x = 0;
		}
		if(!((this.y + moveDistY)+height+40 < GAME_H-50)) {
			this.y = GAME_H-height-40-50;
		}
		if(!(this.y + moveDistY >= 100)){
			this.y = 100;
		}
		
		// remakes the player hitbox
		if(lastM == 1 || lastM == 2) {
		width = this.frames[1].get(0).getWidth();
		height = this.frames[1].get(0).getHeight();
		}else if(lastM == 0) {
		width = this.frames[0].get(0).getWidth();
		height = this.frames[0].get(0).getHeight();
		}else if(lastM == 3) {
		width = this.frames[3].get(0).getWidth();
		height = this.frames[3].get(0).getHeight();
		}
		hitbox = new Rectangle(this.x, this.y, width, height);
	}
	
	public void comboAdd() {
		if(combo < MAX_COMBO) {
			combo += 1;
		}		
	}
	public void comboReset() {
		combo = 0;
	}
	public void touchedBoss() {
		hurt(1, lastM+1, 100);
	}
	
	// overload hurt method to either just apply damage or knockback the player
	public void hurt(int damage) {
		health -= damage;
		currentAnimation = 9;
		combo = 0;
	}
	public void hurt(int damage, int knockDirection, long knockTime) { //for knockdirection: 1, 2, 3, 4 are UP, RIGHT, LEFT, DOWN
		health -= damage;
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
	public int getHealth() {
    	return health;
    }
    public void setHealth(int newHealth) {
    	health = newHealth;
    }
   
}
