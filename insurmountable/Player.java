package insurmountable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Player implements GameConstants {
	private int x, y;
	private int width, height;
	private int xSpeed, ySpeed;
	boolean arrowUp, arrowDown, arrowLeft, arrowRight;
	int spriteX = GAME_W/2;
    int spriteY = GAME_H/2;
    
    
    // Time Tracking. Should probably move this to its own file and make it public so other ones can use
    static long startTime, currentTime, elapsedTime;
    
    // player hitbox
    Rectangle playerHitbox;    
    
    // player animations array
    private int currentAnimation = 4;
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
		if (arrowLeft) {
			// cycles through movement frames using mod
			currentAnimation = 1;
			currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
			moveDistX -= RUN_SPEED;
		}
		if (arrowRight) {
			currentAnimation = 2;
			currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
			moveDistX += RUN_SPEED;
		}
		if (arrowUp) {
			currentAnimation = 3;
			currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
			moveDistY -= RUN_SPEED;
		}
		if (arrowDown) {
			currentAnimation = 0;
			currentFrame = (currentFrame + 1)%frames[currentAnimation].length;
			moveDistY += RUN_SPEED;
		}
		
		// moves the player based on the keys held
		this.x += moveDistX;
		this.y += moveDistY;
		
		System.out.println(moveDistX);
		
		// remakes the player hitbox
		playerHitbox = new Rectangle(this.x, this.y, this.frames[0][1].getWidth(), this.frames[0][1].getHeight());
	}
//----------------------------------------        
	public void timeUpdate() {
		currentTime = System.currentTimeMillis();
	}
	public void update() {
	}
}
