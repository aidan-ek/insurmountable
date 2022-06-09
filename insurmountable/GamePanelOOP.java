//class for the the game area - this is where all the drawing of the screen occurs
package insurmountable;

import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;

class GamePanelOOP extends JPanel implements GameConstants{
    
    //declare game objects
    Player player;
    
    GamePanelOOP() {
        //game object initialization
        player = new Player(GAME_W/2, GAME_H/2, "src/images/sprite");
        
        //attach key and mouse listeners to the game panel
        PlayerKeyListener keyListener = new PlayerKeyListener(player);
        this.addKeyListener(keyListener);
        GameMouseListener mouseListener = new GameMouseListener(player);
        this.addMouseListener(mouseListener);
        
        //JPanel Stuff
        this.setFocusable(true);      //make this window the active window
        this.requestFocusInWindow(); 
        
        //start the game loop in a separate thread (allows simple frame rate control)
        //alternately delete this code and just call repaint() at the end of paintComponent()
        Thread t = new Thread(new Runnable() { public void run() {gameLoop(); }}); //start the gameLoop 
        t.start();
    }
    
    //the main gameloop - this is where the game state is updated
    public  void gameLoop() { 
        while(true){
            player.timeUpdate();
            //update the gameplay
            player.move();
            System.out.println(player.getY());
            //repaint the window
            this.repaint();            

            //wait for the frame to be seen by the user before refreshing
            try{ Thread.sleep(FRAME_TIME);} 
            catch (Exception exc){System.out.println("Thread Error");}
        }    
    }
    
    // paintComponnent runs every time the window gets refreshed
    public void paintComponent(Graphics g) {   
        super.paintComponent(g);          //required
        setDoubleBuffered(true); 
        
        //screen is being refreshed - draw all objects
        g.setColor(Color.white);
        g.fillRect(0, 0, GAME_W, GAME_H);
        player.draw(g);     
    }   
}

