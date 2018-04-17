package listeners;

import collidables.Collidable;
import game.GameLevel;
import geometry.Counter;
import sprites.Fire;

/**
 * EnemyRemover class.
 * a EnemyRemover is in charge of removing enemies from the game, as well as keeping count
 * of the number of enemies that remain.
 * Created by Eilon.
 */
public class EnemyRemover implements HitListener {
    private GameLevel game;
    private Counter remainingEnemies;

    /**
     * Constructor.
     *
     * @param game          game level.
     * @param removedEnemies geometry.Counter of enemies.
     */
    public EnemyRemover(GameLevel game, Counter removedEnemies) {
        this.game = game;
        this.remainingEnemies = removedEnemies;
    }

    /**
     * This method whenever there is a hit checks the number of hits on the block
     * if the number is 1 which means the block shoould be removed.
     *
     * @param beingHit the block that was hit.
     * @param bullet   the ball.
     */
    public void hitEvent(Collidable beingHit, Fire bullet) {
            this.remainingEnemies.decrease(1);
            beingHit.removeFromGame(this.game);
    }
}