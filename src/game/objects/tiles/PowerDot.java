package game.objects.tiles;

import java.awt.*;

public class PowerDot extends Dot {

    private static final double RADIUS = 0.25;

    public PowerDot(int x, int y) {
        super(x, y, RADIUS);
    }

    @Override
    protected Color getColor() {
        return Color.RED;
    }
}