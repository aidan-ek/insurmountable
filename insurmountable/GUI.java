package insurmountable;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;


public class GUI implements GameConstants {
	
	private static BufferedImage hitpointFull = loadSprite("hitpoint_full");
	private static BufferedImage hitpointEmpty = loadSprite("hitpoint_empty");
	private static BufferedImage comboText = loadSprite("combo");
	private static BufferedImage[] comboSprites = new BufferedImage[11];
	private static int hitpointWidth = hitpointFull.getWidth();
	private Boss boss;
	private Player player;
	
	// the size of the boss health bar
	static final int BOSSBAR_H = 20;
	static final int BOSSBAR_W = 700;
	static final int DODGEBAR_H = 10;
	static final int DODGEBAR_W = 50;
	static final int DODGEBAR_EDGEOFFSET = 500;
	// how far the health bar will be from edge of screen
	static final int BOSSBAR_EDGEOFFSET = 50;

	public GUI(Player newPlayer, Boss newBoss) {
		boss = newBoss;
		player = newPlayer;
		
		// loads combo text
		for(int i=0; i<comboSprites.length; i++) {
			comboSprites[i] = loadSprite("ComboNums/" + i);
		}
	}
	
	private static BufferedImage loadSprite(String fileName){
        try {
			return ImageIO.read(new File("src/images/GUI/" + fileName + ".png"));
		} catch (IOException e) {
			System.out.println("Failed to load GUI file: " + e);
		}
        return null;
    }
	
	public void draw(Graphics g) {
		
		// draws full hitpoints
		int drawPosX = 10;
		for (int i=0; i<player.getHealth(); i++) {
			g.drawImage(hitpointFull, drawPosX, 10, null);
			drawPosX += (hitpointWidth + 5);
		}
		// draws empty hitpoints
		for (int i=0; i<PLAYER_MAXHP-player.getHealth(); i++) {
			g.drawImage(hitpointEmpty, drawPosX, 10, null);
			drawPosX += (hitpointWidth + 5);
		}
		
		// draws boss healthbar
		int barWidth = BOSSBAR_W * boss.getHealth() / BOSS_MAXHP;
		g.setColor(Color.GRAY);
		g.fillRect(GAME_W - BOSSBAR_W - BOSSBAR_EDGEOFFSET, GAME_H - BOSSBAR_H - BOSSBAR_EDGEOFFSET, BOSSBAR_W, BOSSBAR_H);
		g.setColor(Color.RED);
		if (boss.getHealth() > 0) {
			g.fillRect(GAME_W - barWidth - BOSSBAR_EDGEOFFSET, GAME_H - BOSSBAR_H - BOSSBAR_EDGEOFFSET, barWidth, BOSSBAR_H);
		}
		
		// player dodge timer
		int dodgeBar = (int)(DODGEBAR_W * player.getRollCooldown() / DODGE_CD);
		
		g.setColor(Color.GREEN);
		if (player.getRollCooldown() < DODGE_CD) {
			g.fillRect(player.getX()+dodgeBar/2, player.getY()+player.getHeight(), (DODGEBAR_W) - dodgeBar, DODGEBAR_H);
		}
		
		// draws combo multiplier
		if (player.getCombo() > 0) {
			g.drawImage(comboText, 50, 100, null);
			g.drawImage(comboSprites[player.getCombo()], 150, 100, 50 + (3*player.getCombo()), 50 + (3*player.getCombo()), null);
		}

	}
	
	public void update(Player newPlayer, Boss newBoss) {
		boss = newBoss;
		player = newPlayer;
	}
}
