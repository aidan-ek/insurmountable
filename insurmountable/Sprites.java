package insurmountable;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Sprites implements GameConstants{
    private final int LEFT = 1;
    private final int RIGHT = 2;
    private final int UP = 3;
    private final int DOWN = 0;
    
    private int x;
    private int y;
    private BufferedImage[][] frames;
    private int row;
    private int col;
//------------------------------------------------------------------------------    
    Sprites(int x, int y, String picName, int rows, int columns){
        this.x = x;
        this.y = y;        
        frames = new BufferedImage[rows][columns];
        try {
            for (int row=0; row<rows; row++){
                for (int col=0; col<columns; col++){
                    frames[row][col] = ImageIO.read(new File(picName+row+col+".png"));
                }
            }
        } catch (IOException ex){}
        row = 0;
        col = 0;
    }
//------------------------------------------------------------------------------    
    public void draw(Graphics g){
        g.drawImage(this.frames[this.row][this.col], this.x, this.y, null);        
    }
    
    public void moveLeft(){
        this.row = LEFT;
        this.col = (this.col + 1)%frames[row].length;
        this.x -= Const.STEP;
    }
    public void moveRight(){
        this.row = RIGHT;
        this.col = (this.col + 1)%frames[row].length;
        this.x += Const.STEP;
    }
    public void moveUp(){
        this.row = UP;
        this.col = (this.col + 1)%frames[row].length;
        this.y -= Const.STEP;
    }
    public void moveDown(){
        this.row = DOWN;
        this.col = (this.col + 1)%frames[row].length;
        this.y += Const.STEP;
    }    
}