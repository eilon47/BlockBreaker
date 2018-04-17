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
import java.io.IOException;
import java.io.InputStream;

/**
 * Spaceship class.
 * uses paddles to draw the spaceship.
 * Created by Eilon.
 */
public class Spaceship implements Sprite, Collidable, HitNotifier {
    private Paddle paddle;
    private Image img;

    /**
     * constructor.
     * @param paddle paddle the spaceshit wraps.
     * @param imgPath path to spaceship's image.
     */
    public Spaceship(Paddle paddle, String imgPath) {
        this.paddle = paddle;
        this.img = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(imgPath);
            this.img = ImageIO.read(is);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    /**
     * TimePassed.
     * Implementation of Sprite interface method.
     * Calls to moveLeft or moveRight.
     * @param dt 1/framesPerSecond
     */
    public void timePassed(double dt) {
        this.paddle.timePassed(dt);
    }

    /**
     * Draws the spaceship on the given surface.
     * Implementation of Sprite interface method.
     *
     * @param d where to draw the ball.
     */
    public void drawOn(DrawSurface d) {
        if (this.img != null) {
            int x = (int) this.paddle.getUpperLeft().getX();
            int y = (int) this.paddle.getUpperLeft().getY();
            d.drawImage(x, y, this.img);
        }

    }

    /**
     * getCollisionRectangle.
     * Implementation of collidable interface method.
     * returns the shape of the paddle.
     *
     * @return the shape of the paddle.
     */
    public Rectangle getCollisionRectangle() {
        return this.paddle.getCollisionRectangle();
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
       this.paddle.hit(bullet, collisionPoint, currentVelocity);
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
     * adds hit listener to the paddle.
     * @param hl listeners.HitListener.
     */
    public void addHitListener(HitListener hl) {
        this.paddle.addHitListener(hl);
    }

    /**
     * removeHitListener.
     * Remove listener from the list.
     *
     * @param hl listeners.HitListener.
     */
    public void removeHitListener(HitListener hl) {
        this.paddle.removeHitListener(hl);
    }

    /**
     * tells the paddle to shoot.
     * @param environment environment.
     * @return fire.
     */
    public Fire fire(GameEnvironment environment) {
        return this.paddle.fire(environment);
    }
}
