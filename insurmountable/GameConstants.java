package insurmountable;

interface GameConstants {
    //width and height of the start window
    //width and height of the game window
    static final int GAME_W = 1024;
    static final int GAME_H = 576;
    //game-specific constants
    static final int GROUND = 565;
    static final int RUN_SPEED = 8;
    static final int DODGE_SPEED = 15;
    static final long ATTACK_CD = 500;
    static final long DODGE_CD = 2000;
    static final long DODGE_TIME = 400;
    static final long ATTACK_TIME = 150;
    static final int PLAYER_MAXHP = 5;
    static final int BOSS_MAXHP = 10;
    static final int MAX_COMBO = 10;

    static final int GRAVITY = 2;
    static final int FRAME_TIME = 30;
}