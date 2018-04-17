package listeners;

import collidables.Collidable;
import geometry.Counter;
import sprites.Fire;

/**
 * ScoreTrackingListener.
 * This class implements HitListener so whenever there is a hit with a block
 * in the game its increase the score counter with few points.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * Constructor.
     *
     * @param scoreCounter score counter.
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
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
        this.currentScore.increase(100);
    }
}