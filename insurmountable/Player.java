package insurmountable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Player implements GameConstants {
	final int UP = 0;
	final int LEFT = 1; 
	final int RIGHT = 2; 
	final int DOWN = 3; 
	private int lastM;
	private int x, y;
	private int width, height;
	private int xSpeed, ySpeed;
	boolean dodgeRolling = false;
	boolean attacking = false;
	private long rollTimer, rollCooldown;
	private long attackTimer, attackCooldown;
	private int rollX, rollY;
	boolean arrowUp, arrowDown, arrowLeft, arrowRight, keyZ, keyX;
	int spriteX = GAME_W/2;
    int spriteY = GAME_H/2;
    
    
    // Time Tracking. Should probably move this to its own file and make it public so other ones can use
    static long startTime, currentTime, elapsedTime;
    
    // player hitbox
    Rectangle playerHitbox; 
    Rectangle swordHitbox;    
    
    
    // player animations array
    private int currentAnimation = 9;
    private int currentFrame = 3;
    private BufferedImage[][] frames = new BufferedImage[currentAnimation][currentFrame];
    
    // parameter constructor
    public Player(int newX, int newY, String fileName) {
		x = newX;
		y = newY;

		loadSprite(fileName);
		width = this.frames[0][1].getWidth();
		height = this.frames[0][1].getHeight();
		xSpeed = 0;
		ySpeed = 0;
	}
//----------------------------------------        
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
			System.out.println("error loading Player sprite");
		}
	}

	// draws hitbox and sprite
	public void draw(Graphics g) {
		
		// draws player
		g.drawImage(this.frames[currentAnimation][currentFrame], this.x, this.y, null);
		g.setColor(Color.red);
		
		// player hitbox
		g.drawRect(this.x, this.y, this.frames[0][1].getWidth(), this.frames[0][1].getHeight());
		if((lastM == LEFT) && attacking) {
			g.drawRect(this.x-60, this.y, 50,50);
			
		}
		if(lastM == RIGHT && attacking) {
			g.drawRect(this.x+60, this.y, 50,50);
		}
		if(lastM == UP && attacking) {
			g.drawRect(this.x, this.y-60, 50,50);
		}
		if(lastM == DOWN && attacking) {
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

	public void setXspeed(int speed) {
		this.xSpeed = speed;
	}

	public void setYspeed(int speed) {
		this.ySpeed = speed;
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
			if(lastM == RIGHT) {
				swordHitbox = new Rectangle(this.x+30, this.y, 10,10);
				currentAnimation = 5;
			}
			if(lastM == LEFT) {
				swordHitbox = new Rectangle(this.x-30, this.y, 10,10); 
				currentAnimation = 6;
			}
			if(lastM == UP) {
				swordHitbox = new Rectangle(this.x, this.y-30, 10,10);
				currentAnimation = 7;
			}
			if(lastM == DOWN) {
				swordHitbox = new Rectangle(this.x, this.y+30, 10,10); 
				currentAnimation = 8;
			}
			currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
			if((Time.since(attackTimer)) >= DODGE_TIME) {
				attacking = false;
				attackCooldown = Time.getTime();
			}
		}
		else if (dodgeRolling) {
			moveDistX = rollX;
			moveDistY = rollY;
			currentAnimation = 4;
			currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
			if((Time.since(rollTimer)) >= DODGE_TIME) {
				dodgeRolling = false;
				rollCooldown = Time.getTime();
			}
		} else {
			
			if (arrowLeft) {
				// cycles through movement frames using mod
				currentAnimation = 1;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
				moveDistX -= RUN_SPEED;
				lastM = LEFT;
			}
			if (arrowRight) {
				currentAnimation = 2;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
				moveDistX += RUN_SPEED;
				lastM = RIGHT;
			}
			if (arrowUp) {
				currentAnimation = 3;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
				moveDistY -= RUN_SPEED;
				lastM = UP;
			}
			if (arrowDown) {
				currentAnimation = 0;
				currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
				moveDistY += RUN_SPEED;
				lastM = DOWN;
			}
			
			// dodge roll intialize
			if (keyZ && rollCooldown == 0) {
				dodgeRolling = true;
				currentAnimation = 4;
				currentFrame = 0;
				rollTimer = Time.currentTime;
				if (moveDistX != 0 && moveDistY != 0) {
					moveDistX /= 1.41;
					moveDistY /= 1.41;
				}
				rollX = Integer.signum(moveDistX) * DODGE_SPEED;
				rollY = Integer.signum(moveDistY) * DODGE_SPEED;
			}
			if(keyX && attackCooldown == 0) {
				attacking = true;				
				attackTimer = Time.currentTime;				
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
		
		
		
		System.out.println(moveDistX);
		
		// remakes the player hitbox
		playerHitbox = new Rectangle(this.x, this.y, this.frames[0][1].getWidth(), this.frames[0][1].getHeight());
	}

}
