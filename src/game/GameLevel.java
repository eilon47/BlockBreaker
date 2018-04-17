package game;

import animation.Animation;
import animation.KeyPressStoppableAnimation;
import animation.CountdownAnimation;
import animation.PauseScreen;
import animation.AnimationRunner;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collidables.Collidable;
import collidables.GameEnvironment;
import geometry.Counter;
import geometry.Point;
import listeners.BlockRemover;
import listeners.EnemyRemover;
import listeners.PaddleTracking;
import listeners.ScoreTrackingListener;
import sprites.Sprite;
import sprites.EnemyCollection;
import sprites.Fire;
import sprites.Paddle;
import sprites.SpriteCollection;
import sprites.Shield;
import sprites.ScoreIndicator;
import sprites.LiveIndicator;
import sprites.IndicatorsBlock;
import sprites.FireOnScreenCollection;
import sprites.BoundaryBlock;
import sprites.Spaceship;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;


/**
 * Created by Eilon.
 * ID: 308576933.
 * game.GameLevel class, initialize the game by the level information it gets.
 */
public class GameLevel implements Animation {
    //Members
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Paddle gamePaddle;
    private Counter enemiesCounter;
    private Counter scoreCounter;
    private Counter livesCounter;
    private Counter paddleCounter;
    private AnimationRunner runner;
    private KeyboardSensor keyboard;
    private List<Shield> shields;
    private boolean running;
    private LevelInformation levelInfo;
    private ScoreIndicator scoreIndicator;
    private LiveIndicator liveIndicator;
    private EnemyCollection ememies;
    private String pathToEnemyImg;
    private boolean playerShot;
    private boolean enemyShot;
    private long playerLastShotTime;
    private long enemyLastShotTime;
    private FireOnScreenCollection fireCollection;
    private Spaceship spaceship;

    /**
     * game.GameLevel constructor.
     *
     * @param levelInfo      information about the level.
     * @param ks             keyboard sensor.
     * @param ar             animation runner.
     * @param score          score indicator.
     * @param lives          lives indicator.
     * @param pathToEnemyImg path to enemy's image;
     */
    public GameLevel(LevelInformation levelInfo, KeyboardSensor ks, AnimationRunner ar,
                     ScoreIndicator score, LiveIndicator lives, String pathToEnemyImg) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.levelInfo = levelInfo;
        this.keyboard = ks;
        this.runner = ar;
        this.scoreIndicator = score;
        this.liveIndicator = lives;
        this.livesCounter = liveIndicator.getCounter();
        this.scoreCounter = this.scoreIndicator.getCounter();
        this.addSprite(this.levelInfo.getBackground());
        this.pathToEnemyImg = pathToEnemyImg;
        this.paddleCounter = new Counter(0);
        fireCollection = new FireOnScreenCollection();
        this.playerShot = false;
        this.enemyShot = false;
    }

    /**
     * addCollidable.
     * adds a collidables.Collidable object to the list.
     *
     * @param c colliadable object.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * addSprite.
     * adds a sprites.Sprite object to the list.
     *
     * @param s sprite object.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * getGuiHeight.
     *
     * @return screen's height.
     */
    public int getGuiHeight() {
        return 600;
    }

    /**
     * getGuiwidth.
     *
     * @return screen's width.
     */
    public int getGuiWidth() {
        return 800;
    }

    /**
     * createPaddle.
     * The method gets the speead and the width of the paddle from the game level
     * information and creates new paddle, the it adds it to the game.
     *
     * @param speed speed of the paddle.
     * @param width width of the paddle.
     * @return paddle.
     */
    public Paddle createPaddle(int speed, int width) {
        Paddle paddle = new Paddle(this, this.keyboard, width,
                (0.025 * this.getGuiHeight()), Color.ORANGE, speed);
        paddle.addToGame(this);
        this.paddleCounter.increase(1);
        return paddle;
    }

    /**
     * setBlockAsBoundaries.
     * creates the blocks for the boundaries.
     *
     * @param lim1      upper left point of the screen.
     * @param lim2      upper right point of the screen.
     * @param guiWidth  screen's width.
     * @param guiHeight screen's height.
     * @param color     color of the blocks.
     * @return returns the blocks in array.
     */
    public BoundaryBlock[] setBlockAsBoundaries(Point lim1, Point lim2, int guiWidth,
                                                int guiHeight, Color color) {
        BoundaryBlock[] blocksArr = new BoundaryBlock[3];
        blocksArr[0] = new BoundaryBlock(lim1, 20, guiHeight, color);
        blocksArr[1] = new BoundaryBlock(lim1, guiWidth, 20, color);
        blocksArr[2] = new BoundaryBlock(lim2, 20, guiHeight, color);
        for (BoundaryBlock b : blocksArr) {
            b.setNumberOnBlock(0);
            b.addToGame(this);
        }
        return blocksArr;
    }

    /**
     * initialize.
     * Initialize a new game: creates the listeners and the counters, the boundaries and the
     * death region.
     */
    public void initialize() {
        //Counters and listeners.
        this.enemiesCounter = new Counter(this.levelInfo.numOfEnemies());
        ScoreTrackingListener scoreTrack = new ScoreTrackingListener(scoreCounter);
        BoundaryBlock indiBlock = new BoundaryBlock(0, 0, this.getGuiWidth(), 20, Color.GRAY);
        IndicatorsBlock indicatorsBlock = new IndicatorsBlock(indiBlock, liveIndicator,
                scoreIndicator, this.levelInfo.levelName());
        BlockRemover br = new BlockRemover(this);
        this.shields = new ArrayList<>();
        shields.add(new Shield(new Point(70, 470), Color.cyan, this));
        shields.add(new Shield(new Point(320, 470), Color.cyan, this));
        shields.add(new Shield(new Point(570, 470), Color.cyan, this));
        for (Shield shield : shields) {
            shield.addHitListener(br);
            shield.addToGame(this);
        }
        this.sprites.addSprite(scoreIndicator);
        this.sprites.addSprite(liveIndicator);
        EnemyRemover enemyRemover = new EnemyRemover(this, this.enemiesCounter);
        //Sets borders.
        Point limPoint1 = new Point(0, 20);
        Point limPoint2 = new Point(0, this.getGuiHeight() - 20);
        Point limPoint3 = new Point(this.getGuiWidth() - 20, 40);
        //Creates blocks for boundaries.
        BoundaryBlock[] blocksAsBoundaries = setBlockAsBoundaries(limPoint1, limPoint3,
                this.getGuiWidth(), this.getGuiHeight(), Color.GRAY);
        this.sprites.addSprite(indicatorsBlock);
        this.ememies = new EnemyCollection(this.levelInfo.numOfEnemies(), this.levelInfo.enemiesStartAt(),
                levelInfo.enemiesSpeed(), this, this.pathToEnemyImg, this.environment);
        this.addSprite(this.ememies);
        this.ememies.addHitListenerToEnemy(enemyRemover);
        this.ememies.addHitListenerToEnemy(scoreTrack);
        this.ememies.addToGame(this);
    }

    /**
     * Sets the sprites and does the countdown animation, then runs it until the user
     * looses life or ends the level.
     */
    public void playOneTurn() {
        this.setSpritesToGame();
        this.running = true;
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        while (!this.shouldStop()) {
            this.runner.run(this);
        }
        this.endPlayOneTurn();
        return;
    }

    /**
     * setSpritesToGame.
     * Creates the spaceship and the game paddle and adds them to the game.
     */
    public void setSpritesToGame() {
        //Creates paddle.
        this.gamePaddle = createPaddle(this.levelInfo.paddleSpeed(), this.levelInfo.paddleWidth());
        PaddleTracking pt = new PaddleTracking(this.paddleCounter);
        this.gamePaddle.addHitListener(pt);
        this.spaceship = new Spaceship(this.gamePaddle, levelInfo.spaceshipPath());
        this.sprites.addSprite(spaceship);

    }

    /**
     * endPlayOneTurn.
     * After we finish our one turn we need to remove all the old sprites, and checks if the
     * player deserve extra 100 points after removing all the blocks.
     */
    public void endPlayOneTurn() {
        if (this.enemiesCounter.getValue() == 0) {
            this.scoreCounter.increase(100);
        }
        this.removeSprite(this.gamePaddle);
        this.removeCollidable(this.gamePaddle);
        this.removeCollidable(this.spaceship);
        this.removeSprite(this.spaceship);
        this.fireCollection.removeAllFromGame(this);
        this.removeSprite(fireCollection);
        if (this.enemiesCounter.getValue() != 0) {
            this.ememies.setOriginalLocation();
            this.ememies.setSpeedToAll(this.levelInfo.enemiesSpeed());
        }

    }

    /**
     * doOneFrame.
     * Draws all the sprites in the game, checks if the player finished to
     * remove all the enemies and if he lose one life.
     * Runs the animation.PauseScreen if the player press "p".
     *
     * @param dt 1 / framespersecond
     * @param d  draw surface.
     */
    public void doOneFrame(DrawSurface d, double dt) {
        //Draws all the sprites on list.
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard,
                    KeyboardSensor.SPACE_KEY, new PauseScreen(this.sprites)));
        }
        if (this.canShoot()) {
            if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
                fireCollection.addFire(this.gamePaddle.fire(this.environment), this);
                this.playerShot = true;
                this.playerLastShotTime = System.currentTimeMillis();
            }
        }
        if (this.canEnemyShoot()) {
            Fire f = this.ememies.randomFire();
            if (f != null) {
                fireCollection.addFire(f, this);
                this.enemyShot = true;
            }
            this.enemyLastShotTime = System.currentTimeMillis();
        }
        if (this.enemiesCounter.getValue() == 0) {
            this.running = false;
        }
        if (this.paddleCounter.getValue() == 0) {
            liveIndicator.getCounter().decrease(1);
            this.running = false;
        }
        if (this.ememies.getBiggestY() > this.shieldsLowestY()) {
            liveIndicator.getCounter().decrease(1);
            this.running = false;
        }
    }

    /**
     * shouldStop.
     *
     * @return true or false.
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * removeCollidable.
     * removes a given collidable from the game.
     *
     * @param c collidable.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * removeSprite.
     * removes a given sprite from the sprite collection.
     *
     * @param s sprite.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * getNumOfBlocks.
     *
     * @return returns how many blocks left in the game.
     */
    public int getNumOfenemies() {
        return this.enemiesCounter.getValue();
    }

    /**
     * getFireCollection.
     *
     * @return fire on screen.
     */
    public FireOnScreenCollection getFireCollection() {
        return this.fireCollection;
    }

    /**
     * canShoot.
     * checks if the player can shoot.
     *
     * @return boolean.
     */
    public boolean canShoot() {
        if (!this.playerShot) {
            return true;
        }
        if (this.playerLastShotTime + 350 < System.currentTimeMillis()) {
            this.playerShot = false;
            return true;
        }
        return false;
    }

    /**
     * canEnemyShoot.
     * checks if the enemy can shoot.
     *
     * @return boolean.
     */
    public boolean canEnemyShoot() {
        if (!this.enemyShot) {
            return true;
        }
        if (this.enemyLastShotTime + 500 < System.currentTimeMillis()) {
            this.enemyShot = false;
            return true;
        }
        return false;
    }

    /**
     * shieldLowestY.
     *
     * @return lowest y value in the shiles.
     */
    public double shieldsLowestY() {
        Set<Double> lowY = new HashSet<Double>();
        for (Shield s : shields) {
            lowY.add(s.getMinY());
        }
        return Collections.min(lowY);
    }
}