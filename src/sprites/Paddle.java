package sprites;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import collidables.Collidable;
import collidables.GameEnvironment;
import game.GameLevel;
import geometry.Point;
import geometry.Rectangle;
import listeners.HitListener;
import listeners.HitNotifier;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Eilon.
 * ID: 308576933.
 * Class for sprites.Paddle.
 * sprites.Paddle class implements sprites.Sprite and collidables.Collidable interfaces.
 */
public class Paddle implements Collidable, Sprite, HitNotifier {
    //Members.
    private KeyboardSensor keyboard;
    private Rectangle movingBlock;
    private Point upperLeft;
    private Point centerPaddlePoint;
    private double width;
    private double height;
    private Color color;
    private double screenWidth;
    private int speed;
    private List<HitListener> hitListenerList;
    private GameLevel game;

    /**
     * Cunstroctor for sprites.Paddle.
     *
     * @param speed          speed of the paddle.
     * @param keyboardSensor keyboard from the game.
     * @param g              game.
     * @param width          width of the paddle.
     * @param height         height of the paddle.
     * @param color          color of paddle.
     */

    public Paddle(GameLevel g, KeyboardSensor keyboardSensor, double width, double height, Color color, int speed) {
        this.keyboard = keyboardSensor;
        this.screenWidth = g.getGuiWidth() - 20;
        this.centerPaddlePoint = new Point(g.getGuiWidth() / 2, g.getGuiHeight() - 30);
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.upperLeft = new Point(centerPaddlePoint.getX() - (width / 2),
                centerPaddlePoint.getY() - (height / 2));
        this.updateCenter();
        this.movingBlock = new Rectangle(this.upperLeft, this.width, this.height);
        this.color = color;
        this.hitListenerList = new ArrayList<>();
    }

    /**
     * addToGame.
     * Add this paddle to the game.
     *
     * @param g game
     */
    public void addToGame(GameLevel g) {
        this.game = g;
        this.game.addSprite(this);
        this.game.addCollidable(this);
    }

    /**
     * getPaddleWidth.
     *
     * @return paddle's width.
     */
    public double getPaddleWidth() {
        return this.width;
    }

    /**
     * getPaddleHeight.
     *
     * @return paddle's height.
     */
    public double getPaddleHeight() {
        return this.height;
    }

    /**
     * getCenterPaddlePoint.
     *
     * @return paddle's center point.
     */
    public Point getCenterPaddlePoint() {
        return this.centerPaddlePoint;
    }

    /**
     * getUpperLeft.
     *
     * @return paddle's upper left point.
     */
    public Point getUpperLeft() {
        return this.upperLeft;
    }

    /**
     * moveLeft.
     * moves the paddle to the left.
     * @param dt 1/framesPerSecond
     */
    public void moveLeft(double dt) {
        this.upperLeft = new Point(this.upperLeft.getX() - (this.speed * dt), this.upperLeft.getY());
        if (this.upperLeft.getX() <= 20) {
            this.upperLeft = new Point(20, this.upperLeft.getY());
        }
        this.updateCenter();
        this.movingBlock = new Rectangle(this.upperLeft, this.getPaddleWidth(), this.getPaddleHeight());
    }

    /**
     * moveRight.
     * moves the paddle to the right.
     * @param dt 1/framesPerSecond
     */
    public void moveRight(double dt) {
        this.upperLeft = new Point(this.upperLeft.getX() + (this.speed * dt), this.upperLeft.getY());
        if (this.upperLeft.getX() >= this.screenWidth - this.getPaddleWidth()) {
            this.upperLeft = new Point(this.screenWidth - this.getPaddleWidth(), this.upperLeft.getY());
        }
        this.updateCenter();
        this.movingBlock = new Rectangle(this.upperLeft, this.getPaddleWidth(), this.getPaddleHeight());
    }

    /**
     * TimePassed.
     * Implementation of sprites.Sprite interface method.
     * Calls to moveLeft or moveRight.
     * @param dt 1/framesPerSecond
     */
    public void timePassed(double dt) {
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft(dt);
        }
        if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight(dt);
        }
    }

    /**
     * Draws the paddle on the given surface.
     * Implementation of sprites.Sprite interface method.
     *
     * @param d where to draw the ball.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillRectangle((int) this.movingBlock.getUpperLeft().getX(), (int) this.movingBlock.getUpperLeft().getY(),
                (int) this.movingBlock.getWidth(), (int) this.movingBlock.getHeight());
        d.setColor(Color.BLACK);
        d.drawRectangle((int) this.movingBlock.getUpperLeft().getX(), (int) this.movingBlock.getUpperLeft().getY(),
                (int) this.movingBlock.getWidth(), (int) this.movingBlock.getHeight());

    }

    /**
     * getCollisionRectangle.
     * Implementation of collidable interface method.
     * returns the shape of the paddle.
     *
     * @return the shape of the paddle.
     */
    public Rectangle getCollisionRectangle() {
        return this.movingBlock;
    }

    /**
     * hit method.
     * Implementation of collidable interface method.
     * The method gets collision point on the paddle and the velocity of the object that
     * hit the paddle. the method checks where the collision point is and returns the appropriate
     * velocity to the object.
     *
     * @param bullet          the bullet which hits the paddle.
     * @param collisionPoint  the collisioin point of the object with the block.
     * @param currentVelocity the velocity of the object that hit the block.
     */
    public void hit(Fire bullet, Point collisionPoint, Velocity currentVelocity) {
        double y = this.movingBlock.getBoundaries().getHighY();
        double x1 = this.movingBlock.getBoundaries().getLeftX();
        double x2 = this.movingBlock.getBoundaries().getRightX();
        if (collisionPoint.getY() == y && x1 <= collisionPoint.getX() && collisionPoint.getX() <= x2) {
            this.notifyHit(bullet);
            this.removeFromGame(this.game);
            bullet.removeFromGame(this.game);
        }
    }

    /**
     * generateRandomColor.
     * The method get random numbers from 0 - 255 for red, green and blue.
     *
     * @return random color.
     */
    public Color generateRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        //If two of the three are equals try again recursively, unwanted varieties.
        if (r != g && g != b && r != b) {
            return new Color(r, g, b);
        } else {
            return generateRandomColor();
        }
    }
    /**
     * removeFromGame.
     * Removes the block from the given game.
     *
     * @param g game.
     */
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
        g.removeCollidable(this);
    }

    /**
     * adds hit listener.
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
     * fire.
     * releases fire from the paddle.
     * @param environment environment.
     * @return fire.
     */
    public Fire fire(GameEnvironment environment) {
        Velocity v = Velocity.fromAngleAndSpeed(0 , 350);
        return new Fire(this.centerPaddlePoint , 2, Color.RED, v, environment, this.game, "sprites.Paddle");

    }

    /**
     * update center of the paddle.
     */
    public void updateCenter() {
        this.centerPaddlePoint = new Point(this.upperLeft.getX() + (width / 2), this.upperLeft.getY() - 5);
    }
}