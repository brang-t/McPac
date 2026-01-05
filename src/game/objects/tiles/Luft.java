package game.objects.tiles;

import java.awt.*;

public class Luft extends Tile {
    public Luft(int x, int y) {
        super(x, y);
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        // Luft is invisible
    }
}
