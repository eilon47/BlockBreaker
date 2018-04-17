package listeners;

import collidables.Collidable;
import geometry.Counter;
import sprites.Fire;

/**
 * PaddleTracking.
 * tracks the number if it still on screen.
 * Created by Eilon.
 */
public class PaddleTracking implements HitListener {
    private Counter paddle;

    /**
     * Constructor.
     *
     * @param paddle paddle counter.
     */
    public PaddleTracking(Counter paddle) {
        this.paddle = paddle;
    }

    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     * This method in this class raises the score by the number of hits on block.
     *
     * @param beingHit the block that was hit.
     * @param hitter   the ball.
     */
    public void hitEvent(Collidable beingHit, Fire hitter) {
        this.paddle.decrease(1);
    }
}
