package listeners;

import collidables.Collidable;
import game.GameLevel;
import sprites.Fire;

/**
 * FireRemover class.
 * FireRemover class implements HitListener interface.
 * FireRemover is in charge of removing Fires from the game, as well as keeping count
 * of the number of Fires that remain.
 * Created by Eilon.
 */
public class FireRemover implements HitListener {
    private GameLevel game;


    /**
     * Constructor.
     *
     * @param game  gamelevel.
     */
    public FireRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Fire that's doing the hitting.
     * FireRemover is a block whenever Fire hits it , the Fire is gone.
     *
     * @param beingHit the block that was hit.
     * @param bullet bullet.
     */
    public void hitEvent(Collidable beingHit, Fire bullet) {
        bullet.removeFromGame(this.game);
    }
}