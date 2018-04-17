package listeners;

import collidables.Collidable;
import sprites.Fire;

/**
 * Class For HitListener interface.
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     *
     * @param beingHit the block that was hit.
     * @param bullet   the bullet.
     */
    void hitEvent(Collidable beingHit, Fire bullet);
}