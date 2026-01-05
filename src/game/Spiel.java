/*package spiel;

import spiel.objects.creatures.Pacman;
import spiel.objects.creatures.enemy.VerfolgerGeist;
import spiel.objects.creatures.enemy.CuttingGeist;
import spiel.objects.creatures.enemy.Geist;
import spiel.objects.creatures.enemy.RandomGeist;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Spiel extends JFrame {
    private final Display display;
    private final Karte map;
    private final Pacman pacman;

    private boolean won;

    private final Geist[] enemies;

    public Spiel() {
        super("Spiel");

        display = new Display(this);
        map = new Karte(40);
        pacman = new Pacman(this, 13.5, 10.5, 0.375, 0.07);
        addKeyListener(pacman);

        enemies = new Geist[]{
                new VerfolgerGeist(this, pacman, 12.5, 8.5, 0.375, 0.06, Color.RED),
                new CuttingGeist(this, pacman, 13.5, 8.5, 0.375, 0.065, Color.GREEN),
                new RandomGeist(this, pacman, 14.5, 8.5, 0.375, 0.07, Color.MAGENTA)
        };

        setSize(1096, 759);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        startGameLoop();
    }

    private void startGameLoop() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            tick();
            display.repaint();
        }, 0L, 1000L / 60L, TimeUnit.MILLISECONDS);
    }

    private void reset() {
        won = false;
        map.reset();
        pacman.reset();
        for (Geist enemy : enemies) {
            enemy.reset();
        }
    }

    public void win() {
        won = true;
    }

    public void lose() {
        JOptionPane.showMessageDialog(null, "Spiel Over!");
        reset();
    }

    private void tick() {
        if (won) {
            JOptionPane.showMessageDialog(null, "You Won!");
            reset();
        }
        pacman.tick();
        for (Geist enemy : enemies) {
            enemy.tick();
        }
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int tileSize = map.getTileSize();

        map.render(g2, tileSize);
        pacman.render(g2, tileSize);
        for (Geist enemy : enemies) {
            enemy.render(g2, tileSize);
        }
    }

    public Karte getMap() {
        return map;
    }

    public Geist[] getEnemies() {
        return enemies;
    }

    public static void main(String[] args) {
        new Spiel();
    }
}*/

/* package spiel;

import spiel.objects.creatures.Pacman;
import spiel.objects.creatures.enemy.VerfolgerGeist;
import spiel.objects.creatures.enemy.CuttingGeist;
import spiel.objects.creatures.enemy.Geist;
import spiel.objects.creatures.enemy.RandomGeist;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Spiel extends JFrame {
    private final Display display;
    private final Karte map;
    private final Pacman pacman;

    private boolean won;

    private final Geist[] enemies;

    // ------- NEU: Power-Mode-Zustand + Timer -------
    private boolean powerModeActive = false;
    private long powerModeEndTimeMs;
    // ------------------------------------------------

    public Spiel() {
        super("Spiel");

        display = new Display(this);
        map = new Karte(40);
        pacman = new Pacman(this, 13.5, 10.5, 0.375, 0.07);
        addKeyListener(pacman);

        enemies = new Geist[]{
                new VerfolgerGeist(this, pacman, 12.5, 8.5, 0.375, 0.06, Color.RED),
                new CuttingGeist(this, pacman, 13.5, 8.5, 0.375, 0.065, Color.GREEN),
                new RandomGeist(this, pacman, 14.5, 8.5, 0.375, 0.07, Color.MAGENTA)
        };

        setSize(1096, 759);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        startGameLoop();
    }

    private void startGameLoop() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            tick();
            display.repaint();
        }, 0L, 1000L / 60L, TimeUnit.MILLISECONDS);
    }

    private void reset() {
        won = false;
        powerModeActive = false; // Power-Mode zurücksetzen
        map.reset();
        pacman.reset();
        for (Geist enemy : enemies) {
            enemy.reset();
        }
    }

    public void win() {
        won = true;
    }

    public void lose() {
        JOptionPane.showMessageDialog(null, "Spiel Over!");
        reset();
    }

    // ------- NEU: Power-Mode aktivieren / abfragen -------

    // wird vom Pacman aufgerufen, wenn ein PowerDot gefressen wurde
    public void activatePowerMode() {
        powerModeActive = true;
        powerModeEndTimeMs = System.currentTimeMillis() + 5000; // 5 Sekunden Dauer

        pacman.onPowerModeStart();
        for (Geist enemy : enemies) {
            enemy.onPowerModeStart();
        }
    }

    public boolean isPowerModeActive() {
        return powerModeActive;
    }

    // ----------------------------------------------------

    private void tick() {
        if (won) {
            JOptionPane.showMessageDialog(null, "You Won!");
            reset();
        }

        // Ablauf des Power-Modes prüfen
        if (powerModeActive && System.currentTimeMillis() > powerModeEndTimeMs) {
            powerModeActive = false;
            pacman.onPowerModeEnd();
            for (Geist enemy : enemies) {
                enemy.onPowerModeEnd();
            }
        }

        pacman.tick();
        for (Geist enemy : enemies) {
            enemy.tick();
        }
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int tileSize = map.getTileSize();

        map.render(g2, tileSize);
        pacman.render(g2, tileSize);
        for (Geist enemy : enemies) {
            enemy.render(g2, tileSize);
        }
    }

    public Karte getMap() {
        return map;
    }

    public Geist[] getEnemies() {
        return enemies;
    }

    public static void main(String[] args) {
        new Spiel();
    }
}
*/
package game;

import game.objects.creatures.Pacman;
import game.objects.creatures.enemy.*;
import game.objects.creatures.enemy.Geist;
import game.objects.creatures.enemy.RandomGeist;


import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Spiel extends JFrame {
    private final Display display;
    private final Karte map;
    private final Pacman pacman;

    private boolean won;

    private final Geist[] enemies;

    ImageIcon icon = new ImageIcon("src/img/Game_Logo_hbrs.png");

    public Spiel() {
        super("Spiel");



        // 1) Karte auswählen, bevor Map/Pacman/Enemies angelegt werden
        // 1) Karte wählen

        int selectedMapIndex = chooseMap();           // NEU: Auswahl beim Start
        map = new Karte(40, selectedMapIndex);      // NEU: mapIndex an Karte

        display = new Display(this);
        // map = new Karte(40);
        pacman = new Pacman(this, 13.5, 10.5, 0.375, 0.07);
        addKeyListener(pacman);

        enemies = new Geist[]{
                new VerfolgerGeist(this, pacman, 12.5, 8.5, 0.375, 0.06, Color.RED),
                new RandomGeist(this, pacman, 13.5, 8.5, 0.375, 0.065, Color.GREEN),
        };

        setSize(1096, 759);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);



        startGameLoop();
    }

    private void startGameLoop() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            tick();
            display.repaint();
        }, 0L, 1000L / 60L, TimeUnit.MILLISECONDS);
    }

    private void reset() {
        won = false;
        map.reset();
        pacman.reset();
        for (Geist geist : enemies) {
            geist.reset();
        }
    }

    public void win() {
        won = true;
    }

    public void lose() {
        JOptionPane.showMessageDialog(
                null,
                "Spiel Over!",
                "You Lost!",
                JOptionPane.PLAIN_MESSAGE,
                icon
        );
        reset();
    }

    private int chooseMap() {
        String[] options = {
                "Karte 3 - Hochschule Bonn-Rhein-Sieg Special",
                "Karte 2 – Embedded Systems Special",
                "Karte 1 – Klassisch"
                // später einfach mehr hinzufügen
        };

        int choice = JOptionPane.showOptionDialog(
                null,
                "Bitte wähle eine Karte:",
                "Kartenwahl",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                icon,
                options,
                options[0]
        );

        // Wenn der Spieler ESC drückt oder das Fenster schließt:
        if (choice < 0) {
            choice = 0; // Standard: erste Karte
        }

        return choice;
    }


    private void tick() {
        if (won) {
            JOptionPane.showMessageDialog(null, "You Won!");
            reset();
        }

        // Spiel kennt den PowerMode nicht – nur Ticks durchreichen
        pacman.tick();
        for (Geist geist : enemies) {
            geist.tick();
        }
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int tileSize = map.getTileSize();

        map.render(g2, tileSize);
        pacman.render(g2, tileSize);
        for (Geist geist : enemies) {
            geist.render(g2, tileSize);
        }
    }

    public Karte getMap() {
        return map;
    }

    public Geist[] getEnemies() {
        return enemies;
    }

    static void main(String[] args) {
        new Spiel();
    }

}