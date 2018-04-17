package sprites;

import biuoop.DrawSurface;
import game.GameLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * FireOnScreenCollection.
 * Holds all the fires that still on screen.
 * Created by Eilon.
 */
public class FireOnScreenCollection implements Sprite {
    private List<Fire> fires;

    /**
     * Constructor.
     */
    public FireOnScreenCollection() {
        this.fires = new ArrayList<Fire>();
    }

    /**
     * draw the sprite to the screen.
     *
     * @param d surface.
     */
    public void drawOn(DrawSurface d) {
        for (Fire f: fires) {
            f.drawOn(d);
        }
    }


    /**
     * notify the sprite that time has passed.
     *
     * @param dt 1/framesPerSecond
     */
    public void timePassed(double dt) {
        for (Fire f: fires) {
            f.moveOneStep(dt);
        }
    }

    /**
     * adds fire to the game.
     * @param f fire.
     * @param g game.
     */
    public void addFire(Fire f, GameLevel g) {
        this.fires.add(f);
        g.addSprite(f);
    }
    /**
     * removes all fires from the game.
     * @param g game.
     */
    public void removeAllFromGame(GameLevel g) {
        for (Fire f: fires) {
            f.removeFromGame(g);
        }
        this.fires.clear();
    }
    /**
     * remove fire from the game.
     * @param f fire.
     * @param g game.
     */
    public void removeOneFire(GameLevel g, Fire f) {
        this.fires.remove(f);
        g.removeSprite(f);
    }


}
