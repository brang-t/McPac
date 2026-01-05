package game.objects.tiles;

import java.awt.*;

public class Wand extends Tile {
    public Wand(int x, int y) {
        super(x, y);
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        Color hellblau = new Color(74, 174, 246); // RGB für "LightBlue"
        g.setColor(hellblau);
        g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
    }
}
