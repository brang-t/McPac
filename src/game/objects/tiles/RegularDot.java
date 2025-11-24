package game.objects.tiles;

import java.awt.*;

public class RegularDot extends Dot {

    private static final double RADIUS = 0.125;

    public RegularDot(int x, int y) {
        super(x, y, RADIUS);
    }

    @Override
    protected Color getColor() {
        return Color.WHITE;
    }
}