package game;


import geometry.Point;
import sprites.Sprite;

/**
 *Class for LevelInformation interface.
 */
public interface LevelInformation {

    /**
     * paddleSpeed.
     * Sets the speed of the paddle in this level.
     *
     * @return speed of paddle.
     */
    int paddleSpeed();

    /**
     * paddleWidth.
     * Sets the width of the paddle in this level.
     *
     * @return width of the paddle.
     */
    int paddleWidth();

    /**
     * levelName.
     * the level name will be displayed at the top of the screen.
     *
     * @return string of the level's name.
     */
    String levelName();

    /**
     * getBackground.
     * The method creates a sprite that is the background of this level.
     * Each level has different background.
     *
     * @return a sprite with the background of the level
     */
    Sprite getBackground();

    /**
     * blocks.
     * The Blocks that make up this level, each block contains
     * its size, color and location.
     *
     * @return list of blocks.
     */
    Point enemiesStartAt();

    /**
     * numberOfBlockToRemove.
     * Number of levels that should be removed
     * before the level is considered to be "cleared".
     *
     * @return number of blocks.
     */
    int numOfEnemies();

    /**
     * enemiesSpeed.
     * initialize the enemies speed.
     * @return speed.
     */
    int enemiesSpeed();

    /**
     * path to the spaceship image.
     * @return path.
     */
    String spaceshipPath();
}