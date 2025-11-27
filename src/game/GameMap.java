package game;

import game.objects.GameObject;
import game.objects.tiles.*;

import java.awt.*;

public class GameMap extends GameObject {
    private static final int[][] DEFAULT_MAP = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 3, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 3, 1},
            {1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1},
            {1, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 0, 2, 3, 2, 0, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 2, 2, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 2, 2, 2, 1},
            {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1},
            {1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1},
            {1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1},
            {1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1},
            {1, 3, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 3, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private static final int[][] HBRS_MAP = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 1, 1, 2, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 3, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 2, 2, 1, 1, 1, 2, 1, 1, 1, 2, 2, 2, 1, 1, 2, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 2, 1, 2, 2, 2, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 3, 2, 2, 1, 2, 2, 1, 2, 2, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 0, 0, 1, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 1},
            {1, 2, 1, 2, 2, 1, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 2, 1, 1, 2, 2, 1},
            {1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };



    private static final int[][][] MAPS = {
            DEFAULT_MAP,
            HBRS_MAP
            // später einfach mehr Maps hinzufügen
    };


    private final int tileSize;
    private final Tile[][] tiles;
    private final int mapIndex; // <- neu: welche der MAPS benutzen wir?


    public GameMap(int tileSize, int mapIndex) {
        this.tileSize = tileSize;
        this.mapIndex = mapIndex;

        int[][] layout = MAPS[mapIndex];
        tiles = new Tile[layout.length][layout[0].length];

        reset();
    }


    public void reset() {
        int[][] layout = MAPS[mapIndex];

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                tiles[y][x] = switch (layout[y][x]) {
                    case 1 -> new Block(x, y);
                    case 2 -> new RegularDot(x, y);
                    case 3 -> new PowerDot(x, y);
                    default -> new Air(x, y);
                };
            }
        }
    }


    @Override
    public void render(Graphics2D g, int tileSize) {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.render(g, tileSize);
            }
        }
    }

    public int dotCount() {
        int sum = 0;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile instanceof Dot) {
                    sum++;
                }
            }
        }
        return sum;
    }

    public boolean isFree(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            return false;
        }

        return !(tiles[y][x] instanceof Block);
    }

    public int getWidth() {
        return tiles[0].length;
    }

    public int getHeight() {
        return tiles.length;
    }

    public int getTileSize() {
        return tileSize;
    }

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public void setTile(int x, int y, Tile tile) {
        tiles[y][x] = tile;
    }
}
