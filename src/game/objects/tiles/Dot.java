package game.objects.tiles;

import java.awt.*;
        import java.awt.geom.Ellipse2D;

public abstract class Dot extends Tile {

    protected final double radius;

    protected Dot(int x, int y, double radius) {
        super(x, y);
        this.radius = radius;
    }

    // Pacman braucht das:
    public double getCenterX() {
        return x + 0.5;
    }

    public double getCenterY() {
        return y + 0.5;
    }

    // Pacman braucht das auch:
    public double getRadius() {
        return radius;
    }

    // Unterklassen bestimmen die Farbe:
    protected abstract Color getColor();

    @Override
    public void render(Graphics2D g, int tileSize) {
        double centerXOnScreen = getCenterX() * tileSize;
        double centerYOnScreen = getCenterY() * tileSize;

        double radiusOnScreen = radius * tileSize;
        double diameterOnScreen = radiusOnScreen * 2.0;

        g.setColor(getColor());
        g.fill(new Ellipse2D.Double(
                centerXOnScreen - radiusOnScreen,
                centerYOnScreen - radiusOnScreen,
                diameterOnScreen,
                diameterOnScreen
        ));
    }
}