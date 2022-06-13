//class for the the game area - this is where all the drawing of the screen occurs
package insurmountable;

import java.awt.Graphics;
import java.io.File;
import java.awt.Color;
import javax.swing.JPanel;

class GamePanelOOP extends JPanel implements GameConstants{
	
	
    //declare game objects
    private Player player;
    private StartScreen startScreen;
        
    GamePanelOOP() {
    	
    	
        //game object initialization
        player = new Player(GAME_W/2, GAME_H/2, "src/images/sprite");
        startScreen = new StartScreen("src/images/startscreen.png");
        //attach key and mouse listeners to the game panel
        PlayerKeyListener keyListener = new PlayerKeyListener(player);
        this.addKeyListener(keyListener);
        GameMouseListener m = new GameMouseListener();
        this.addMouseListener(m);
        
                
        //JPanel Stuff
        this.setFocusable(true);      //make this window the active window
        this.requestFocusInWindow(); 
                      
        // PUT START MENU HERE
             
        //start the game loop in a separate thread (allows simple frame rate control)
        //alternately delete this code and just call repaint() at the end of paintComponent()
        
        //Create thread for the start screen
        Thread t = new Thread(new Runnable() { public void run() {menu(); }}); 
        t.start(); 
        
    }
    
    //Start main method to create game frame
    public static void main(String[] args) { 
    	new  GameFrameOOP();
    } 
    
    //Create menu method
    public void menu() {
    	//Start while loop for menu
    	while(!startScreen.menu) {
    		//As long as they dont click start, they will be in the menu
    		this.repaint();
    	}
    	
    	//Create thread for gameloop
    	Thread t = new Thread(new Runnable() { public void run() {gameLoop(); }}); //start the gameLoop 
        t.start();
    }
    //the main gameloop - this is where the game state is updated
    public  void gameLoop() { 
        while(true){
            player.timeUpdate();
            //System.out.println(player.currentTime);
            //update the gameplay
            player.move();
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
        
        //While they dont click start display the menu
        if(!startScreen.menu){
        g.setColor(Color.white);
        g.fillRect(0, 0, GAME_W, GAME_H);
        g.drawImage(startScreen.start, -40,-40, null);
        
        //When they enter game display game
        }else if(startScreen.menu) {       
        g.setColor(Color.white);
        g.fillRect(0, 0, GAME_W, GAME_H);
        player.draw(g);    
    }
    }   
}

