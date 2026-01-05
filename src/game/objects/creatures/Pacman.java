/* package spiel.objects.creatures;

import spiel.Spiel;
import spiel.objects.creatures.enemy.Geist;
import spiel.objects.tiles.Luft;
import spiel.objects.tiles.Dot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

public class Pacman extends Kreatur implements KeyListener {
    public Pacman(Spiel spiel, double centerX, double centerY, double radius, double speed) {
        super(spiel, centerX, centerY, radius, speed, Color.YELLOW);
    }

    private void tickDotCollision() {
        int x = (int) centerX;
        int y = (int) centerY;

        if (spiel.getMap().getTile(x, y) instanceof Dot dot) {
            double dx = dot.getCenterX() - centerX;
            double dy = dot.getCenterY() - centerY;
            double r = dot.getRadius() + radius;

            if (dx * dx + dy * dy < r * r) {
                spiel.getMap().setTile(x, y, new Luft(x, y));
                if (spiel.getMap().dotCount() == 0) {
                    spiel.win();
                }
            }
        }
    }

    @Override
    protected void tickPreferredDirection() {
        for (Geist enemy : spiel.getEnemies()) {
            enemy.tickPreferredDirection();
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickDotCollision();
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        double centerXOnScreen = centerX * tileSize;
        double centerYOnScreen = centerY * tileSize;
        double radiusOnScreen = radius * tileSize;
        double diameterOnScreen = radiusOnScreen * 2.0;

        g.setColor(color);
        g.fill(new Ellipse2D.Double(centerXOnScreen - radiusOnScreen, centerYOnScreen - radiusOnScreen, diameterOnScreen, diameterOnScreen));

        // Eyes
        Geist closestEnemy = null;
        double closestSqDistance = Double.MAX_VALUE;
        for (Geist enemy : spiel.getEnemies()) {
            double difX = enemy.centerX - centerX;
            double difY = enemy.centerY - centerY;
            double sqDistance = difX * difX + difY * difY;
            if (sqDistance < closestSqDistance) {
                closestEnemy = enemy;
                closestSqDistance = sqDistance;
            }
        }
        renderEyes(g, centerXOnScreen, centerYOnScreen, radiusOnScreen, closestEnemy.centerX, closestEnemy.centerY);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                preferredDirectionX = 0;
                preferredDirectionY = -1;
            }
            case KeyEvent.VK_A -> {
                preferredDirectionX = -1;
                preferredDirectionY = 0;
            }
            case KeyEvent.VK_S -> {
                preferredDirectionX = 0;
                preferredDirectionY = 1;
            }
            case KeyEvent.VK_D -> {
                preferredDirectionX = 1;
                preferredDirectionY = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }
}*/
/*
package spiel.objects.creatures;

import spiel.Spiel;
import spiel.objects.creatures.enemy.Geist;
import spiel.objects.tiles.Luft;
import spiel.objects.tiles.Dot;
import spiel.objects.tiles.PowerDot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

public class Pacman extends Kreatur implements KeyListener {

    // ------- NEU: Speed & Farben für Power-Mode -------
    private final double normalSpeed;
    private final double powerSpeed;

    private final Color normalColor = Color.YELLOW;
    private final Color powerColor = Color.ORANGE;
    // ---------------------------------------------------

    public Pacman(Spiel spiel, double centerX, double centerY, double radius, double speed) {
        super(spiel, centerX, centerY, radius, speed, Color.YELLOW);
        this.normalSpeed = speed;
        this.powerSpeed = speed * 1.5; // z. B. 50% schneller im Power-Mode
    }

    private void tickDotCollision() {
        int x = (int) centerX;
        int y = (int) centerY;

        if (spiel.getMap().getTile(x, y) instanceof Dot dot) {
            double dx = dot.getCenterX() - centerX;
            double dy = dot.getCenterY() - centerY;
            double r = dot.getRadius() + radius;

            if (dx * dx + dy * dy < r * r) {
                // Punkt entfernen
                spiel.getMap().setTile(x, y, new Luft(x, y));

                // NEU: Wenn PowerDot -> Power-Mode aktivieren
                if (dot instanceof PowerDot) {
                    spiel.activatePowerMode();
                }

                // Siegprüfung bleibt wie gehabt
                if (spiel.getMap().dotCount() == 0) {
                    spiel.win();
                }
            }
        }
    }

    // Geister berechnen pro Tick ihre Preferred Directions
    @Override
    protected void tickPreferredDirection() {
        for (Geist enemy : spiel.getEnemies()) {
            enemy.tickPreferredDirection();
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickDotCollision();
    }

    // ------- NEU: Hooks für Power-Mode -------

    public void onPowerModeStart() {
        this.speed = powerSpeed;
    }

    public void onPowerModeEnd() {
        this.speed = normalSpeed;
    }

    // ----------------------------------------

    @Override
    public void render(Graphics2D g, int tileSize) {
        double centerXOnScreen = centerX * tileSize;
        double centerYOnScreen = centerY * tileSize;
        double radiusOnScreen = radius * tileSize;
        double diameterOnScreen = radiusOnScreen * 2.0;

        // im Power-Mode andere Farbe
        Color currentColor = spiel.isPowerModeActive() ? powerColor : normalColor;
        g.setColor(currentColor);
        g.fill(new Ellipse2D.Double(centerXOnScreen - radiusOnScreen,
                centerYOnScreen - radiusOnScreen,
                diameterOnScreen,
                diameterOnScreen));

        // Eyes
        Geist closestEnemy = null;
        double closestSqDistance = Double.MAX_VALUE;
        for (Geist enemy : spiel.getEnemies()) {
            double difX = enemy.centerX - centerX;
            double difY = enemy.centerY - centerY;
            double sqDistance = difX * difX + difY * difY;
            if (sqDistance < closestSqDistance) {
                closestEnemy = enemy;
                closestSqDistance = sqDistance;
            }
        }
        if (closestEnemy != null) {
            renderEyes(g, centerXOnScreen, centerYOnScreen, radiusOnScreen,
                    closestEnemy.centerX, closestEnemy.centerY);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                preferredDirectionX = 0;
                preferredDirectionY = -1;
            }
            case KeyEvent.VK_A -> {
                preferredDirectionX = -1;
                preferredDirectionY = 0;
            }
            case KeyEvent.VK_S -> {
                preferredDirectionX = 0;
                preferredDirectionY = 1;
            }
            case KeyEvent.VK_D -> {
                preferredDirectionX = 1;
                preferredDirectionY = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }
}*/

package game.objects.creatures;

import game.Spiel;
import game.objects.creatures.enemy.Geist;
import game.objects.tiles.Luft;
import game.objects.tiles.Dot;
import game.objects.tiles.PowerDot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;

public class Pacman extends Kreatur implements KeyListener {

    // PowerMode-Einstellungen
    private final double normalSpeed;
    private final double powerSpeed;

    private final Color normalColor = Color.YELLOW;
    private final Color powerColor = Color.ORANGE;

    private boolean powerModeActive = false;
    private long powerModeEndTimeMs;

    public Pacman(Spiel spiel, double centerX, double centerY, double radius, double speed) {
        super(spiel, centerX, centerY, radius, speed, Color.YELLOW);
        this.normalSpeed = speed;
        this.powerSpeed = speed * 1.5; // z. B. 50% schneller im PowerMode
    }

    private void tickDotCollision() {
        int x = (int) centerX;
        int y = (int) centerY;

        if (spiel.getMap().getTile(x, y) instanceof Dot dot) {
            double dx = dot.getCenterX() - centerX;
            double dy = dot.getCenterY() - centerY;
            double r = dot.getRadius() + radius;

            if (dx * dx + dy * dy < r * r) {
                // Punkt entfernen
                spiel.getMap().setTile(x, y, new Luft(x, y));

                // Wenn es ein PowerDot ist → PowerMode aktivieren
                if (dot instanceof PowerDot) {
                    activatePowerMode();
                }

                // Alle Dots weg? Dann gewonnen
                if (spiel.getMap().dotCount() == 0) {
                    spiel.win();
                }
            }
        }
    }

    private void tickPowerMode() {
        if (powerModeActive && System.currentTimeMillis() > powerModeEndTimeMs) {
            deactivatePowerMode();
        }
    }

    private void activatePowerMode() {
        powerModeActive = true;
        powerModeEndTimeMs = System.currentTimeMillis() + 5000; // 5 Sekunden

        // Pacman schneller machen
        this.speed = powerSpeed;

        // Alle Enemies in FRIGHTENED versetzen
        for (Geist geist : spiel.getEnemies()) {
            geist.onPowerModeStart();
        }
    }

    private void deactivatePowerMode() {
        powerModeActive = false;
        this.speed = normalSpeed;

        // Alle Enemies aus FRIGHTENED zurückholen
        for (Geist geist : spiel.getEnemies()) {
            geist.onPowerModeEnd();
        }
    }

    public boolean isPowerModeActive() {
        return powerModeActive;
    }

    @Override
    protected void tickPreferredDirection() {
        // Deine bisherige Logik: Pacman stößt das Pathfinding der Enemies an
        for (Geist geist : spiel.getEnemies()) {
            geist.tickPreferredDirection();
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickDotCollision();
        tickPowerMode(); // PowerMode-Timer hier handlen
    }

    @Override
    public void reset() {
        super.reset();
        powerModeActive = false;
        speed = normalSpeed;
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        double centerXOnScreen = centerX * tileSize;
        double centerYOnScreen = centerY * tileSize;
        double radiusOnScreen = radius * tileSize;
        double diameterOnScreen = radiusOnScreen * 2.0;

        // Farbe je nach PowerMode
        Color currentColor = powerModeActive ? powerColor : normalColor;
        g.setColor(currentColor);
        g.fill(new Ellipse2D.Double(
                centerXOnScreen - radiusOnScreen,
                centerYOnScreen - radiusOnScreen,
                diameterOnScreen,
                diameterOnScreen));

        // Eyes
        renderEyes(g,
                centerXOnScreen,
                centerYOnScreen,
                radiusOnScreen);

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ignore
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                preferredDirectionX = 0;
                preferredDirectionY = -1;
            }
            case KeyEvent.VK_LEFT -> {
                preferredDirectionX = -1;
                preferredDirectionY = 0;
            }
            case KeyEvent.VK_DOWN -> {
                preferredDirectionX = 0;
                preferredDirectionY = 1;
            }
            case KeyEvent.VK_RIGHT -> {
                preferredDirectionX = 1;
                preferredDirectionY = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // ignore
    }
}



