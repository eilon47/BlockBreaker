package listeners;

import collidables.Collidable;
import game.GameLevel;
import sprites.Fire;

/**
 * BlockRemover class.
 * a BlockRemover is in charge of removing blocks from the game, as well as keeping count
 * of the number of blocks that remain.
 * Created by Eilon.
 */
public class BlockRemover implements HitListener {
    private GameLevel game;

    /**
     * Constructor.
     *
     * @param game          game level.
     */
    public BlockRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     *
     * @param beingHit the block that was hit.
     * @param bullet   the bullet.
     */
    public void hitEvent(Collidable beingHit, Fire bullet) {
        beingHit.removeFromGame(this.game);
    }

}
