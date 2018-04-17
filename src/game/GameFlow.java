package game;

import animation.Animation;
import animation.AnimationRunner;
import animation.EndScreen;
import animation.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Counter;
import geometry.Point;
import highscores.HighScoresAnimation;
import highscores.HighScoresTable;
import highscores.ScoreInfo;
import sprites.LiveIndicator;
import sprites.ScoreIndicator;
import sprites.Sprite;

import java.awt.Color;
import java.io.File;
import java.io.IOException;



/**
 * GameFlow.
 * Runs few levels one after the other until the game ends.
 * Created by Eilon.
 */
public class GameFlow {
    //Members.
    private AnimationRunner animationRunner;
    private KeyboardSensor keyboardSensor;
    private DialogManager dialog;
    private String pathToImgEnemy;
    private HighScoresTable hst;
    private File filename;

    /**
     * Constructor.
     * @param dialog dialog manager.
     * @param hst highscore table.
     * @param filename name of file.
     * @param ar Animation runner.
     * @param ks Keyboard sensor.
     * @param pathToImgEnemy path to the enemy image.
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, String pathToImgEnemy,
                    File filename, DialogManager dialog, HighScoresTable hst) {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.hst = hst;
        this.filename = filename;
        this.dialog = dialog;
        this.pathToImgEnemy = pathToImgEnemy;

    }

    /**
     * runLevels.
     * The method gets list of levels and runs them one after the other until
     * the list ends. Then it shows the end screen.
     *
     */
    public void runLevels() {

        //Initialize the score and lives.
        Counter scoreCounter = new Counter(0);
        Counter liveCounter = new Counter(3);
        ScoreIndicator scoreIndicator = new ScoreIndicator(scoreCounter);
        LiveIndicator liveIndicator = new LiveIndicator(liveCounter);
        //Runs all the levels.
        int i = 1;
        while (true) {
            GameLevel level = new GameLevel(this.level((10 * i), "Battle no. " + Integer.toString(i)) ,
                    this.keyboardSensor, this.animationRunner, scoreIndicator, liveIndicator, pathToImgEnemy);
            level.initialize();
            while (level.getNumOfenemies() > 0 && liveCounter.getValue() > 0) {
                level.playOneTurn();

            }
            //Checks if the player won or lose.
            if (liveCounter.getValue() == 0) {
                break;
            }
            i++;
        }
        //Runs the end screen.
        animationRunner.run(new EndScreen(this.keyboardSensor, scoreCounter));
        int possibleRank = this.hst.getRank(scoreCounter.getValue());
        if (possibleRank < this.hst.size()) {
            String name = this.dialog.showQuestionDialog("Name", "What is your name?", "");
            hst.add(new ScoreInfo(name, scoreCounter.getValue()));
            try {
                hst.save(this.filename);
            } catch (IOException e) {
                System.out.println("Could not save file");
            }
        }
        Animation highScoreKeyAnimation = new KeyPressStoppableAnimation(this.keyboardSensor,
                KeyboardSensor.SPACE_KEY, new HighScoresAnimation(this.hst));

        animationRunner.run(highScoreKeyAnimation);

    }

    /**
     * retuns new level information.
     * @param speed speed to the enemies.
     * @param name level name.
     * @return level information.
     */
    public LevelInformation level(int speed, String name) {
        return new LevelInformation() {
            /**
             * paddleSpeed.
             * Sets the speed of the paddle in this level.
             *
             * @return speed of paddle.
             */
            public int paddleSpeed() {
                return 300;
            }

            /**
             * paddleWidth.
             * Sets the width of the paddle in this level.
             *
             * @return width of the paddle.
             */
            public int paddleWidth() {
                return 100;
            }

            /**
             * levelName.
             * the level name will be displayed at the top of the screen.
             *
             * @return string of the level's name.
             */
            public String levelName() {
                return name;
            }

            /**
             * getBackground.
             * The method creates a sprite that is the background of this level.
             * Each level has different background.
             *
             * @return a sprite with the background of the level
             */
            public Sprite getBackground() {
                return new Sprite() {
                    @Override
                    public void drawOn(DrawSurface d) {
                        d.setColor(Color.BLACK);
                        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
                    }

                    @Override
                    public void timePassed(double dt) {

                    }
                };
            }

            /**
             * blocks.
             * The Blocks that make up this level, each block contains
             * its size, color and location.
             *
             * @return list of blocks.
             */
            public Point enemiesStartAt() {
                return new Point(80, 60);
            }
            public int enemiesSpeed() { return speed; }
            /**
             * numberOfBlockToRemove.
             * Number of levels that should be removed
             * before the level is considered to be "cleared".
             *
             * @return number of blocks.
             */
            public int numOfEnemies() {
                return 50;
            }

            public String spaceshipPath() {
                return "images/spaceship.jpg";
            }

        };
    }
}