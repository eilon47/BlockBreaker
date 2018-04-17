package sprites;

import biuoop.DrawSurface;
import collidables.Collidable;
import collidables.CollisionInfo;
import collidables.GameEnvironment;
import game.GameLevel;
import geometry.Line;
import geometry.Point;

import java.awt.Color;

/**
 * Class for sprites.Fire.
 * @author Eilon Bashari.
 */
public class Fire implements Sprite {
    private Point point;
    private int length;
    private Color color;
    private Velocity v;
    private GameEnvironment game;
    private GameLevel gameLevel;
    private String shooter;

    /**
     * Constructor.
     * @param start start point
     * @param length length
     * @param color color.
     * @param v velocity.
     * @param game environment.
     * @param g game level.
     * @param shooter who shot this.
     */
    public Fire(Point start, int length, Color color, Velocity v, GameEnvironment game, GameLevel g, String shooter) {
        this.point = start;
        this.length = length;
        this.color = color;
        this.v = v;
        this.game = game;
        this.gameLevel = g;
        this.shooter = shooter;
    }

    /**
     * draw the sprite to the screen.
     *
     * @param d surface.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(this.color);
        d.fillCircle((int) this.point.getX(), (int) this.point.getY(), this.length);
    }

    /**
     * notify the sprite that time has passed.
     * @param dt 1/framesPerSecond
     */
    public void timePassed(double dt) {
        this.moveOneStep(dt);
    }

    /**
     * moves the bullet one step.
     * @param dt 1\ frames per second.
     */
    public void moveOneStep(double dt) {
        //Line made by the center of the ball and it's velocity.
        Velocity newV = new Velocity((this.v.getDx() * dt), (this.v.getDy() * dt));
        Line trajectory = new Line(this.point, newV.applyToPoint(this.point));
        //Gets if there are any collisions.
        CollisionInfo collisionInfo = this.game.getClosestCollision(trajectory);
        if (collisionInfo == null) {
            //If there are no collisions the balls moves to it's next step.
            this.point = newV.applyToPoint(this.point);
        } else {
            if (this.shooter(collisionInfo.collisionObject())) {
                this.removeFromGame(this.gameLevel);
                return;
            }
            collisionInfo.collisionObject().hit(this, collisionInfo.collisionPoint(), this.v);
        }
        if (this.point.getY() > 600 || this.point.getY() <= 50) {
            this.gameLevel.getFireCollection().removeOneFire(this.gameLevel, this);
            this.removeFromGame(gameLevel);
        }
    }
    /**
     * removeFromGame.
     * the method removes this ball from the given game.
     *
     * @param g game.
     */
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
    }

    /**
     * checks if the collidable is the same type of the shooter.
     * @param c collidable.
     * @return boolean.
     */
    public boolean shooter(Collidable c) {
        String clss = c.getClass().toString();
        return clss.contains(this.shooter);
    }

}
