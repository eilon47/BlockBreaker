package animation;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Counter;


import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;

import java.io.IOException;
import java.io.InputStream;

/**
 * EndScreen class.
 * Animation for the game when it's over.
 * Created by Eilon.
 */
public class EndScreen implements Animation {
    //Members.
    private KeyboardSensor keyboard;
    private boolean stop;
    private Counter score;
    private Image img;

    /**
     * Constructor.
     *
     * @param k         keyboard.
     * @param score     the score the player got.
     */
    public EndScreen(KeyboardSensor k, Counter score) {
        this.keyboard = k;
        this.stop = false;
        this.score = score;
        this.img = null;
    }

    /**
     * doOneFrame.
     *
     * @param dt 1 / framespersecond
     * @param d draw surface.
     */
    public void doOneFrame(DrawSurface d, double dt) {
        String scoreString = "Your score is: " + Integer.toString(this.score.getValue());
        int width = d.getWidth();
        int height = d.getHeight();

        if (this.img == null) {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("screens_images/theend.jpg");
            try {
                this.img = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        d.drawImage(0, 0, this.img);
        d.setColor(Color.white);
        d.drawText((int) (width * 0.3), (int) (height * 0.8), "press space to continue", 32);
        d.drawText((int) (width * 0.25), (int) (height * 0.6), scoreString, 48);
        if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            this.stop = true;
        }
    }

    /**
     * shouldStop.
     *
     * @return true or false.
     */
    public boolean shouldStop() {
        return this.stop;
    }
}