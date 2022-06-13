//example for a game object
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
    int rows = 4;
    int columns = 3;
    static long startTime, currentTime, elapsedTime;
    Rectangle playerBoundingBox;    
    BufferedImage[][] frames = new BufferedImage[rows][columns];
    BufferedImage start;
    public Player(int x, int y, String fileName) {
		this.x = x;
		this.y = y;
		loadSprite(fileName);
		this.width = this.frames[0][1].getWidth();
		this.height = this.frames[0][1].getHeight();
		this.xSpeed = 0;
		this.ySpeed = 0;
	}
//----------------------------------------        
	public void loadSprite(String fileName) {
		
		try {
			for (int row=0; row<rows; row++){
	            for (int col=0; col<columns; col++){
	                frames[row][col] = ImageIO.read(new File(fileName+row+col+".png"));
	            }
	        }
			rows = 0;
			columns = 1;
		} catch (Exception e) {
			System.out.println("error loading sprite");
		}
	}


	public void draw(Graphics g) {
		
		g.drawImage(this.frames[this.rows][this.columns], this.x, this.y, null);
		g.setColor(Color.red);
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

//----------------------------------------    
	public void move() {
		int moveDistX = 0;
		int moveDistY = 0;
		if (arrowLeft) {
			this.rows = 1;
			this.columns = (this.columns + 1)%frames[rows].length;
			moveDistX -= RUN_SPEED;
		} else if (arrowRight) {
			this.rows = 2;
			this.columns = (this.columns + 1)%frames[rows].length;
			moveDistX += RUN_SPEED;
		}
		if (arrowUp) {
			this.rows = 3;
			this.columns = (this.columns + 1)%frames[rows].length;
			moveDistY -= RUN_SPEED;
		} else if (arrowDown) {
			this.rows = 0;
			this.columns = (this.columns + 1)%frames[rows].length;
			moveDistY += RUN_SPEED;
		}
		
		this.x += moveDistX;
		this.y += moveDistY;
		playerBoundingBox = new Rectangle(this.x, this.y, this.frames[0][1].getWidth(), this.frames[0][1].getHeight());
	}
//----------------------------------------        
	public void timeUpdate() {
		currentTime = System.currentTimeMillis();
	}
	public void update() {
	}
}
