import animation.Animation;
import animation.AnimationRunner;
import animation.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;
import game.GameFlow;
import highscores.HighScoresAnimation;
import highscores.HighScoresTable;
import menu.Menu;
import menu.MenuAnimation;
import menu.Task;

import java.io.File;
/**
 * Created by Eilon on 23/06/2017.
 */
public class Ass7Game {
    /**
     * main.
     * main for space invaders game.
     * @param args arguments from user.
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Space Invaders", 800, 600);
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        AnimationRunner ar = new AnimationRunner(gui, framesPerSecond, sleeper);
        KeyboardSensor ks = gui.getKeyboardSensor();
        DialogManager dialog = gui.getDialogManager();
        //High score table.
        HighScoresTable highScoresTable = null;
        File highscoreFile = new File("highscores");
        if (highscoreFile.exists()) {
            highScoresTable = HighScoresTable.loadFromFile(highscoreFile);
        } else {
            highScoresTable = new HighScoresTable(5);
        }
        Animation highscore = new KeyPressStoppableAnimation(ks,
                KeyboardSensor.SPACE_KEY, new HighScoresAnimation(highScoresTable));
        HighScoresTable hst = highScoresTable;
        Menu<Task<Void>> menu = new MenuAnimation(ks);
        Task<Void> gameTask = new Task<Void>() {
            @Override
            public Void run() {
                GameFlow game = new GameFlow(ar, ks, "images/enemy.png", highscoreFile, dialog, hst);
                game.runLevels();
                return null;
            }
        };
        Task<Void> highscoreTask = new Task<Void>() {
            @Override
            public Void run() {
                ar.run(highscore);
                return null;
            }
        };
        Task<Void> exitTask = new Task<Void>() {
            @Override
            public Void run() {
                gui.close();
                System.exit(1);
                return null;
            }
        };
        menu.addSelection("s", "Start Game", gameTask);
        menu.addSelection("h", "High Scores", highscoreTask);
        menu.addSelection("q", "Exit", exitTask);
        while (true) {
            ar.run(menu);
            Task<Void> t = menu.getStatus();
            if (t != null) {
                t.run();
            }
        }
    }

}
