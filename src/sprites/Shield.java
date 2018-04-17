package sprites;

import biuoop.DrawSurface;
import game.GameLevel;
import geometry.Point;
import listeners.HitListener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Shield class.
 * Makes shields for the game.
 * eveny shield made from many blocks.
 * Created by Eilon.
 */
public class Shield implements Sprite {
    private List<Block> blocks;
    private Point startAt;
    private Color color;

    /**
     * Constructor.
     * @param startAt blocks start at.
     * @param color color for the blocks.
     * @param g game level.
     */
    public Shield(Point startAt, Color color, GameLevel g) {
        this.color = color;

        this.startAt = startAt;
        int width = 7;
        int height = 7;
        this.blocks = new ArrayList<>();
        double x = startAt.getX();
        double y = startAt.getY();
        for (int i = 0; i < 75; i++) {
            Block b = new Block(x, y, width, height, this.color);
            b.setGameLevel(g);
            blocks.add(b);
            x = x + width;
            if (i == 24 || i == 49) {
                y = y + height;
                x = startAt.getX();
            }
        }
    }
    /**
     * draw the sprite to the screen.
     *
     * @param d surface.
     */
    public void drawOn(DrawSurface d) {
        for (Block b: blocks) {
            b.drawOn(d);
        }
    }

    /**
     * notify the sprite that time has passed.
     * @param dt 1/framesPerSecond
     */
    public void timePassed(double dt) { }

    /**
     * add hit listener to each block.
     * @param hl hit listener.
     */
    public void addHitListener(HitListener hl) {
        for (Block b: blocks) {
            b.addHitListener(hl);
        }
    }

    /**
     * adds each block to the game.
     * @param g game level.
     */
    public void addToGame(GameLevel g) {
        for (Block b: blocks) {
            b.addToGame(g);
        }
    }

    /**
     * returns the lowest y value of the blocks.
     * @return lowest y value.
     */
    public double getMinY() {
        if (this.blocks.isEmpty()) { return 600; }
        double y = this.blocks.get(0).getCollisionRectangle().getUpperLeft().getY();
        for (Block b: blocks) {
            if (y > b.getCollisionRectangle().getUpperLeft().getY()) {
                y = b.getCollisionRectangle().getUpperLeft().getY();
            }
        }
        return y;
    }
}
