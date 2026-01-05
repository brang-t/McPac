package game.objects.tiles;

import game.objects.Spielobjekt;

public abstract class Tile extends Spielobjekt {
    protected final int x;
    protected final int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
