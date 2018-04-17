package sprites;

import biuoop.DrawSurface;
import collidables.Collidable;
import collidables.GameEnvironment;
import game.GameLevel;
import geometry.Point;
import geometry.Rectangle;
import listeners.HitListener;
import listeners.HitNotifier;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Enemy class for one enemy.
 * Created by Eilon.
 */
public class Enemy implements Collidable, Sprite, HitNotifier {
    private Velocity speed;
    private Block backBlock;
    private Point curPoint;
    private int width;
    private int height;
    private String imgPath;
    private Image img;
    private List<HitListener> hitListenerList;
    private GameLevel currentLevel;

    /**
     * Constructor.
     * @param imgPath path to image.
     * @param block block.
     * @param speedDx speed.
     * @param game game level.
     */
    public Enemy(String imgPath, Block block, int speedDx, GameLevel game) {
        this.speed = new Velocity(speedDx, 0);
        this.backBlock = block;
        this.curPoint = block.getCollisionRectangle().getUpperLeft();
        this.imgPath = imgPath;
        this.currentLevel = game;
        this.hitListenerList = new ArrayList<>();
        this.height = (int) this.backBlock.getCollisionRectangle().getHeight();
        this.width = (int) this.backBlock.getCollisionRectangle().getWidth();

    }
    /**
     * getCollisionRectangle.
     *
     * @return Return the "collision shape" of the object.
     */
    public Rectangle getCollisionRectangle() { return this.backBlock.getCollisionRectangle(); }

    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     *
     * @param bullet          the bullet that hits the collidable.
     * @param collisionPoint  the point the collision happens
     * @param currentVelocity given velocity of the object is collided with.
     */
    public void hit(Fire bullet, Point collisionPoint, Velocity currentVelocity) {
        double y = this.backBlock.getCollisionRectangle().getBoundaries().getLowY();
        double x1 = this.backBlock.getCollisionRectangle().getBoundaries().getLeftX();
        double x2 = this.backBlock.getCollisionRectangle().getBoundaries().getRightX();
        if (collisionPoint.getY() == y && x1 <= collisionPoint.getX() && collisionPoint.getX() <= x2) {
            this.notifyHit(bullet);
            this.removeFromGame(this.currentLevel);
            bullet.removeFromGame(this.currentLevel);
        } else {
            bullet.removeFromGame(this.currentLevel);
        }
    }
    /**
     * removeFromGame.
     * Removes the enemy from the given game.
     *
     * @param g game.
     */
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
        g.removeCollidable(this);
    }
    /**
     * addHitListener.
     * Add listeners.HitListener to the list so whenever there is a hit the hit listener know about it.
     *
     * @param hl listeners.HitListener.
     */
    public void addHitListener(HitListener hl) {
        this.hitListenerList.add(hl);
    }

    /**
     * removeHitListener.
     * Remove listener from the list.
     *
     * @param hl listeners.HitListener.
     */
    public void removeHitListener(HitListener hl) {
        this.hitListenerList.remove(hl);
    }
    /**
     * notifyHit.
     * The method notify all the hit listeners about the hit with the ball(hitter)
     *
     * @param bullet the bullet that hits the blocks
     */
    private void notifyHit(Fire bullet) {
        // Make a copy of the hitListeners before iterating over them.
        java.util.List<HitListener> listeners = new ArrayList<HitListener>(this.hitListenerList);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, bullet);
        }
    }
    /**
     * draw the sprite to the screen.
     *
     * @param d surface.
     */
    public void drawOn(DrawSurface d) {
        int x = (int) this.curPoint.getX();
        int y = (int) this.curPoint.getY();
        if (this.img != null) {
            d.drawImage(x, y, this.img);
        } else {
            try {
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(this.imgPath);
                this.img = ImageIO.read(is);
                d.drawImage(x, y, this.img);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * notify the sprite that time has passed.
     * @param dt 1/framesPerSecond
     */
    public void timePassed(double dt) {
        this.moveOneStep(dt);
    }

    /**
     * moves the enemy one step.
     * @param dt 1/ frames per second.
     */
    public void moveOneStep(double dt) {
        Velocity v = new Velocity(this.speed.getDx() * dt, this.speed.getDy() * dt);
        this.curPoint = v.applyToPoint(this.curPoint);
        this.backBlock = new Block(this.curPoint, width,
                height, this.backBlock.getColor(), this.backBlock.getColor());
    }

    /**
     * fire- shoots fire from enemy.
     * @param environment game environment.
     * @return fire.
     */
    public Fire fire(GameEnvironment environment) {
        Velocity v = Velocity.fromAngleAndSpeed(180 , 150);
        Point midLowPoint = new Point(this.getCollisionRectangle().getBoundaries().getLeftX()
                + (this.getCollisionRectangle().getWidth() / 2),
                this.getCollisionRectangle().getBoundaries().getLowY() + 10);
        return new Fire(midLowPoint, 5, Color.RED, v, environment, this.currentLevel, "Enemy");
    }

    /**
     * sets the speed of the enemy.
     * @param dx dx.
     */
    public void setSpeed(double dx) {
        this.speed = new Velocity(dx, 0);
    }

    /**
     * get speed.
     * @return speed of the enemy.
     */
    public Velocity getSpeed() {
        return this.speed;
    }

    /**
     * moves the enemy one row down.
     */
    public void moveDown() {
        this.curPoint = new Point(this.curPoint.getX(), this.curPoint.getY() + height);
        this.backBlock = new Block(this.curPoint , width,
                height, this.backBlock.getColor(), this.backBlock.getColor());
    }

    /**
     * adds the enemy to game.
     * @param g game level.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * sets the enemy in the given location.
     * @param p point.
     */
    public void setLocation(Point p) {
        this.backBlock = new Block(p, this.backBlock.getCollisionRectangle().getWidth(),
                this.backBlock.getCollisionRectangle().getHeight(), this.backBlock.getColor());
        this.curPoint = p;
    }
}
