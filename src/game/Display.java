package game;

import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {
    private final Spiel spiel;

    public Display(Spiel spiel) {
        super();
        this.spiel = spiel;
        spiel.add(this);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        spiel.render(g2);
    }
}
