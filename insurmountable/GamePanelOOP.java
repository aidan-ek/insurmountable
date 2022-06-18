//class for the the game area - this is where all the drawing of the screen occurs
package insurmountable;

import java.awt.Graphics;
import java.io.File;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class GamePanelOOP extends JPanel implements GameConstants {

	// declare game objects
	private Player player;
	private Boss boss;
	private GUI gui;
	private EndScreen endScreen;
	private StartScreen startScreen;
	boolean bossHit = false;
	private Thread gameThread;
	
	private BufferedImage background;


	GamePanelOOP() {

		// game object initialization
		player = new Player(GAME_W / 2, GAME_H / 2, "src/images/Player/sprite");
		startScreen = new StartScreen("src/images/GUI/");
		endScreen = new EndScreen("src/images/GUI/");
		boss = new Boss(GAME_W / 2, 50, "src/images/Boss/Boss");
		gui = new GUI(player, boss);
		
		try {
    		background = ImageIO.read(new File("src/images/background.png"));
        }
        catch(Exception e) {
        	System.out.println("Error loading background: " + e);
        }

		// attach key and mouse listeners to the game panel
		PlayerKeyListener keyListener = new PlayerKeyListener(player);
		this.addKeyListener(keyListener);
		GameMouseListener m = new GameMouseListener();
		this.addMouseListener(m);

		// JPanel Stuff
		this.setFocusable(true); // make this window the active window
		this.requestFocusInWindow();

		// start the game loop in a separate thread (allows simple frame rate control)
		// alternately delete this code and just call repaint() at the end of
		// paintComponent()

		// Create thread for the start screen
		Thread t = new Thread(new Runnable() {
			public void run() {
				menu();
				// Create thread for gameloop
				gameThread = new Thread(new Runnable() {
					public void run() {
						gameLoop();
					}
				}); // start the gameLoop
				gameThread.start();
			}
		});
		t.start();
		
	}
	
	// Start main method to create game frame
	public static void main(String[] args) {
		new GameFrameOOP();
	}

	// Create menu method
	public void menu() {
		// Start while loop for menu
		while (!StartScreen.menu) {
			// As long as they dont click start, they will be in the menu
			this.repaint();
		}
		
	}

	// the main gameloop - this is where the game state is updated
	public void gameLoop() {
		while (true) {
			Time.update();
			// System.out.println(player.currentTime);

			// update the gameplay
			player.move();
			boss.update(player);
			updateCollides(player, boss);
			gui.update(player, boss);
			EndScreen.gameEnd = checkDeath(player, boss);
			
			// repaint the window
			this.repaint();

			// wait for the frame to be seen by the user before refreshing
			try {
				Thread.sleep(FRAME_TIME);
			} catch (Exception exc) {
				System.out.println("Thread Error");
			}
		}
	}
	
	public boolean checkDeath(Player p, Boss b) {
		if(p.getHealth() <= 0) {
			return true;
		}else if(b.getHealth() <= 0) {
			return true;
		}else {
			return false;
		}
		
	}
	
	// updates all hitbox collisions
	public void updateCollides(Player p, Boss b) {
		
		// damages and pushes player when they touch the boss
		if(p.hitbox.intersects(b.hitbox) && !p.invulnerable) {
			p.touchedBoss();
		}
		
		// put all attack collision checkers here
		if (!p.dodgeRolling && !p.invulnerable) {
			
			
			// cleave damage
			if(p.hitbox.intersects(b.attackHitbox) && b.getAnimation() == 1) {
				p.hurt(2, 4, 300);
			}
			
			// combo damage
			if(p.hitbox.intersects(b.attackHitbox) && b.getAnimation() == 2) {
				p.hurt(1, 4, 150);
			}
			
			// combo damage
			if(p.hitbox.intersects(b.attackHitbox) && b.getAnimation() == 3) {
				p.hurt(1, 4, 150);
			}
			
		}
		
		// Check if player attack hit
		if (p.attacking) {
			if (p.attackHitbox.intersects(b.hitbox)) {
				if (!bossHit) {
					bossHit = true;
					p.comboAdd();
					boss.hurt(p.getCombo());	
				}
			}
		} else {
			bossHit = false;
		}
		
		// prevents repeated hits every frame. always keep at end of updateCollides
		if(p.hitbox.intersects(b.hitbox) || p.hitbox.intersects(b.attackHitbox)) {
			p.invulnerable = true;
		} else {
			p.invulnerable = false;
		}
	}

	// paintComponnent runs every time the window gets refreshed
	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g); // required
		setDoubleBuffered(true);

		// While they dont click start display the menu
		if (!StartScreen.menu && !StartScreen.info) {
			g.setColor(Color.white);
			g.fillRect(0, 0, GAME_W, GAME_H);
			g.drawImage(startScreen.start, -40, -40, null);

			// When they enter game display game
		}else if(StartScreen.info) {
			g.drawImage(startScreen.moves, 0, 0, null);
		} 
		else if (StartScreen.menu && !EndScreen.gameEnd) {
			g.drawImage(background, 0, 0, null);
			boss.draw(g);
			gui.draw(g);
			player.draw(g);
			
		} else if (EndScreen.gameEnd) {
			if (boss.getHealth() <= 0) {
				g.setColor(Color.white);
				g.fillRect(0, 0, GAME_W, GAME_H);
				g.drawImage(endScreen.playerWon, 0, 0, null);
				gameThread.stop();
			}
			else {
				g.setColor(Color.white);
				g.fillRect(0, 0, GAME_W, GAME_H);
				g.drawImage(endScreen.playerLost, 0, 0, null);
				gameThread.stop();
			}
		}
	}
}
