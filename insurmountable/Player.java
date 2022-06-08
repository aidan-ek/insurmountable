//example for a game object
package insurmountable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;

class Player implements GameConstants {
	private int x, y;
	private BufferedImage sprite;
	private int width, height;
	private int xSpeed, ySpeed;
	boolean arrowUp, arrowDown, arrowLeft, arrowRight;

	public Player(int x, int y, String fileName) {
		this.x = x;
		this.y = y;
		loadSprite(fileName);
		this.width = this.sprite.getWidth();
		this.height = this.sprite.getHeight();
		this.xSpeed = 0;
		this.ySpeed = 0;
	}

//----------------------------------------        
	public void loadSprite(String fileName) {
		try {
			this.sprite = ImageIO.read(new File(fileName));
		} catch (Exception e) {
			System.out.println("error loading sprite");
		}
		;
	}

	public void draw(Graphics g) {
		g.drawImage(this.sprite, this.x, this.y, null);
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
			moveDistX -= RUN_SPEED;
		} else if (arrowRight) {
			moveDistX += RUN_SPEED;
		}
		if (arrowUp) {
			moveDistY -= RUN_SPEED;
		} else if (arrowDown) {
			moveDistY += RUN_SPEED;
		}
		
		this.x += moveDistX;
		this.y += moveDistY;
	}
//----------------------------------------        

	public void update() {
	}
}
