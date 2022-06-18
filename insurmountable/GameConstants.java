package insurmountable;

/**
 * [GameConstants.java]
 * Interface to hold all game constants
 * @author Mohammad/Aiden
 * Date June 08, 2022
 */

interface GameConstants {
    //width and height of the start window
    //width and height of the game window
    static final int GAME_W = 1024;
    static final int GAME_H = 576;
    
    //player constants
    static final int RUN_SPEED = 8;
    static final int DODGE_SPEED = 15;
    static final long ATTACK_CD = 500;
    static final long DODGE_CD = 1200;
    static final long DODGE_TIME = 300;
    static final long ATTACK_TIME = 150;
    static final int PLAYER_MAXHP = 8;
    static final int MAX_COMBO = 10;
    static final int FRAME_TIME = 40;
    static final int KNOCKBACK_SPEED = 10;
    
    // boss constants
    static final int BOSS_MAXHP = 200;
    
    final long IDLE_DELAY = 5;
    final long ATTACK_DELAY = 600;
    // ------------------------
    final long CLEAVE_WINDUP = 900;
    final long CLEAVE_INDICATETIME = 400;
    final long CLEAVE_AFTERHIT = 500;
    final int CLEAVE_W = 800;
    final int CLEAVE_H = 400;
    // ------------------------
    final long COMBO_SMALL_WINDUP = 300;
    final long COMBO_SMALL_INDICATETIME = 300;
    final long COMBO_SMALL_AFTERHIT = 1000;
    final long COMBO_LARGE_WINDUP = 500;
    final long COMBO_LARGE_INDICATETIME = 300;
    final long COMBO_LARGE_AFTERHIT = 1000;
    final int COMBO_SMALL_W = 200;
    final int COMBO_SMALL_H = 100;
    final int COMBO_LARGE_W = 400;
    final int COMBO_LARGE_H = 200;
    // ------------------------
    final long RAY_WINDUP = 300;
    final long RAY_INDICATETIME = 700;
    final long RAY_ENDTIME = 4000;
    final int RAY_W = 150;
    final int RAY_H = 1000;
}