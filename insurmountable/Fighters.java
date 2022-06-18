package insurmountable;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

abstract class Fighters implements GameConstants {
	private int x, y;
	private int width, height;
	int currentAnimation = 0;
	int currentFrame = 0;
	int TOTAL_ANIMATIONS;
    ArrayList<BufferedImage>[] frames;
    Rectangle hitbox;    
    Rectangle attackHitbox;
    private int health;
    public Fighters(int newX, int newY){
    	this.x = newX;
    	this.y = newY;
    }
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
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
	public int getHealth() {
    	return health;
    }
    public void setHealth(int newHealth) {
    	health = newHealth;
    }
    public void hurt(int damage) {
 	}

}
