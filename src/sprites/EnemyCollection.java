package sprites;

import biuoop.DrawSurface;
import collidables.Collidable;
import collidables.GameEnvironment;
import game.GameLevel;
import geometry.Counter;
import geometry.Point;
import listeners.HitListener;

import java.util.Set;
import java.util.HashSet;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * EnemyCollection class for holding many enemies.
 * Created by Eilon.
 */
public class EnemyCollection implements Sprite, HitListener {
    private Counter numOfEnemies;
    private int leftX;
    private int rightX;
    private List<Enemy> enemies;
    private List<Point> startLocations;
    private GameEnvironment game;

    /**
     * Constructor.
     * also creates the enemies.
     * @param numOfEnemies number of enemies.
     * @param upperLeft upper left point.
     * @param speed speed of each enemy.
     * @param game game level.
     * @param pathToImg path to image.
     * @param gameEnvironment environment.
     */
    public EnemyCollection(int numOfEnemies, Point upperLeft, int speed, GameLevel game,
                           String pathToImg, GameEnvironment gameEnvironment) {
        this.numOfEnemies = new Counter(numOfEnemies);
        double x = upperLeft.getX();
        double y = upperLeft.getY();
        this.startLocations = new ArrayList<>();
        this.game = gameEnvironment;
        this.enemies = new ArrayList<Enemy>();
        for (int i = 0; i < numOfEnemies; i++) {
            Point p = new Point(x, y);
            this.startLocations.add(p);
            Block b = new Block(p, 40, 30, Color.BLACK);
            Enemy e = new Enemy(pathToImg, b, speed, game);
            e.addHitListener(this);
            enemies.add(e);
            x = x + 50;
            if (i % 10 == 9) {
                y = y + 40;
                x = upperLeft.getX();
            }
        }
        this.checkLeftAndRightXs();

    }

    /**
     * sets the speed of the enemies.
     * @param dx dx.
     */
    public void setSpeedToAll(double dx) {
        for (int i = 0; i < this.numOfEnemies.getValue(); i++) {
            this.enemies.get(i).setSpeed(dx);
        }
    }

    /**
     * removes one enemy from the collection.
     * @param e enemy.
     */
    public void removeEnemy(Enemy e) {
        this.enemies.remove(e);
        this.numOfEnemies.decrease(1);
    }

    /**
     * checks which is the leftest and rightest x's in the collection.
     */
    public void checkLeftAndRightXs() {
        this.leftX = (int) this.enemies.get(0).getCollisionRectangle().getBoundaries().getLeftX();
        this.rightX = (int) (this.enemies.get(0).getCollisionRectangle().getBoundaries().getRightX());
        for (Enemy e : enemies) {
            if (e.getCollisionRectangle().getBoundaries().getLeftX() < this.leftX) {
                this.leftX = (int) e.getCollisionRectangle().getBoundaries().getLeftX();
            }
            if (e.getCollisionRectangle().getBoundaries().getRightX() > this.rightX) {
                this.rightX = (int) e.getCollisionRectangle().getBoundaries().getRightX();
            }
        }
    }

    /**
     * draw the sprite to the screen.
     *
     * @param d surface.
     */
    public void drawOn(DrawSurface d) {
        for (Enemy e : this.enemies) {
            e.drawOn(d);
        }
    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt 1/framesPerSecond
     */
    public void timePassed(double dt) {
        this.moveOneStep(dt);
    }

    /**
     * moves each enemy in the collection, if it touches the borders it goes on line down and change direction.
     * @param dt 1 / frames per second.
     */
    public void moveOneStep(double dt) {
        this.checkLeftAndRightXs();
        if (this.leftX <= 20) {
            for (Enemy e : enemies) {
                e.moveDown();
                e.setSpeed(-(e.getSpeed().getDx() * 1.1));
            }

        }
        if (this.rightX >= 780) {
            for (Enemy e : enemies) {
                e.moveDown();

                e.setSpeed(-(e.getSpeed().getDx() * 1.1));

            }

        }
        for (Enemy e : enemies) {
            e.moveOneStep(dt);
        }
    }

    /**
     * adds hit listener to each enemy in the collection.
     * @param hl hit listener.
     */
    public void addHitListenerToEnemy(HitListener hl) {
        for (Enemy e : enemies) {
            e.addHitListener(hl);
        }
    }

    /**
     * removes the collection from the game.
     * @param g game level.
     */
    public void removeFromGame(GameLevel g) {
        for (Enemy e : enemies) {
            e.removeFromGame(g);
        }
        this.startLocations.clear();
    }

    /**
     * adds the collection to the game.
     * @param g game level.
     */
    public void addToGame(GameLevel g) {
        for (Enemy e : enemies) {
            e.addToGame(g);
        }
    }
    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     *
     * @param beingHit the block that was hit.
     * @param bullet   the bullet.
     */
    public void hitEvent(Collidable beingHit, Fire bullet) {
        int index = this.enemies.indexOf((Enemy) beingHit);
        this.removeEnemy((Enemy) beingHit);
        this.startLocations.remove(index);
    }

    /**
     * choose random enemy from the lowest line and tells it to shoot.
     * @return fire.
     */
    public Fire randomFire() {
        Random random = new Random();
        List<Enemy> lowestLine = this.lowestLine();
        if (lowestLine.isEmpty()) { return null; }
        int i = random.nextInt(lowestLine.size());
        return lowestLine.get(i).fire(this.game);
    }

    /**
     * gets the lowest line of enemies.
     * @return list of enemies.
     */
    public List<Enemy> lowestLine() {
        List<Enemy> low = new ArrayList<>();
        for (Double x : this.xOfEachCol()) {
            List<Enemy> colList = new ArrayList<>();
            for (Enemy e : enemies) {
                if (e.getCollisionRectangle().getUpperLeft().getX() == x) {
                    colList.add(e);
                }
            }
            low.add(this.lowestEnemyInCol(colList));
        }
        return low;
    }

    /**
     * checks how many cols there are and returns each col x.
     * @return set of x's.
     */
    public Set<Double> xOfEachCol() {
        Set<Double> xOfCols = new HashSet<>();
        for (Enemy e : enemies) {
            Double ex = e.getCollisionRectangle().getUpperLeft().getX();
            if (!xOfCols.contains(ex)) {
                xOfCols.add(ex);
            }
        }
        return xOfCols;
    }

    /**
     * returns the enemy the lowest in each column.
     * @param col list of enemies.
     * @return enemy.
     */
    public Enemy lowestEnemyInCol(List<Enemy> col) {
        if (col.isEmpty()) {
            return null;
        }
        Enemy enemy = col.get(0);
        for (Enemy e : col) {
            if (e.getCollisionRectangle().getBoundaries().getLowY()
                    > enemy.getCollisionRectangle().getBoundaries().getLowY()) {
                enemy = e;
            }
        }
        return enemy;
    }

    /**
     * sets the collection in the origianl start position.
     */
    public void setOriginalLocation() {
        for (int i = 0; i < this.enemies.size(); i++) {
            Point originalPoint = this.startLocations.get(i);
            this.enemies.get(i).setLocation(originalPoint);
        }
    }

    /**
     * returns the the y of the lowest enemy.
     * @return lowest y position of enemy.
     */
    public double getBiggestY() {
        double y = 0;
        List<Enemy> enemyLowest = this.lowestLine();
        if (enemyLowest.size() > 0) {
            y = enemyLowest.get(0).getCollisionRectangle().getBoundaries().getLowY();
            for (Enemy e: enemyLowest) {
                if (e.getCollisionRectangle().getBoundaries().getLowY() > y) {
                    y = e.getCollisionRectangle().getBoundaries().getLowY();
                }
            }
            return y;
        }
        return y;
    }
}
