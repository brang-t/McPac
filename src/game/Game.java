/*package game;

import game.objects.creatures.Player;
import game.objects.creatures.enemy.ChasingEnemy;
import game.objects.creatures.enemy.CuttingEnemy;
import game.objects.creatures.enemy.Enemy;
import game.objects.creatures.enemy.RandomEnemy;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {
    private final Display display;
    private final GameMap map;
    private final Player player;

    private boolean won;

    private final Enemy[] enemies;

    public Game() {
        super("Game");

        display = new Display(this);
        map = new GameMap(40);
        player = new Player(this, 13.5, 10.5, 0.375, 0.07);
        addKeyListener(player);

        enemies = new Enemy[]{
                new ChasingEnemy(this, player, 12.5, 8.5, 0.375, 0.06, Color.RED),
                new CuttingEnemy(this, player, 13.5, 8.5, 0.375, 0.065, Color.GREEN),
                new RandomEnemy(this, player, 14.5, 8.5, 0.375, 0.07, Color.MAGENTA)
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
        player.reset();
        for (Enemy enemy : enemies) {
            enemy.reset();
        }
    }

    public void win() {
        won = true;
    }

    public void lose() {
        JOptionPane.showMessageDialog(null, "Game Over!");
        reset();
    }

    private void tick() {
        if (won) {
            JOptionPane.showMessageDialog(null, "You Won!");
            reset();
        }
        player.tick();
        for (Enemy enemy : enemies) {
            enemy.tick();
        }
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int tileSize = map.getTileSize();

        map.render(g2, tileSize);
        player.render(g2, tileSize);
        for (Enemy enemy : enemies) {
            enemy.render(g2, tileSize);
        }
    }

    public GameMap getMap() {
        return map;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public static void main(String[] args) {
        new Game();
    }
}*/

/* package game;

import game.objects.creatures.Player;
import game.objects.creatures.enemy.ChasingEnemy;
import game.objects.creatures.enemy.CuttingEnemy;
import game.objects.creatures.enemy.Enemy;
import game.objects.creatures.enemy.RandomEnemy;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {
    private final Display display;
    private final GameMap map;
    private final Player player;

    private boolean won;

    private final Enemy[] enemies;

    // ------- NEU: Power-Mode-Zustand + Timer -------
    private boolean powerModeActive = false;
    private long powerModeEndTimeMs;
    // ------------------------------------------------

    public Game() {
        super("Game");

        display = new Display(this);
        map = new GameMap(40);
        player = new Player(this, 13.5, 10.5, 0.375, 0.07);
        addKeyListener(player);

        enemies = new Enemy[]{
                new ChasingEnemy(this, player, 12.5, 8.5, 0.375, 0.06, Color.RED),
                new CuttingEnemy(this, player, 13.5, 8.5, 0.375, 0.065, Color.GREEN),
                new RandomEnemy(this, player, 14.5, 8.5, 0.375, 0.07, Color.MAGENTA)
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
        player.reset();
        for (Enemy enemy : enemies) {
            enemy.reset();
        }
    }

    public void win() {
        won = true;
    }

    public void lose() {
        JOptionPane.showMessageDialog(null, "Game Over!");
        reset();
    }

    // ------- NEU: Power-Mode aktivieren / abfragen -------

    // wird vom Player aufgerufen, wenn ein PowerDot gefressen wurde
    public void activatePowerMode() {
        powerModeActive = true;
        powerModeEndTimeMs = System.currentTimeMillis() + 5000; // 5 Sekunden Dauer

        player.onPowerModeStart();
        for (Enemy enemy : enemies) {
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
            player.onPowerModeEnd();
            for (Enemy enemy : enemies) {
                enemy.onPowerModeEnd();
            }
        }

        player.tick();
        for (Enemy enemy : enemies) {
            enemy.tick();
        }
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int tileSize = map.getTileSize();

        map.render(g2, tileSize);
        player.render(g2, tileSize);
        for (Enemy enemy : enemies) {
            enemy.render(g2, tileSize);
        }
    }

    public GameMap getMap() {
        return map;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public static void main(String[] args) {
        new Game();
    }
}
*/
package game;

import game.objects.creatures.Player;
import game.objects.creatures.enemy.ChasingEnemy;
import game.objects.creatures.enemy.CuttingEnemy;
import game.objects.creatures.enemy.Enemy;
import game.objects.creatures.enemy.RandomEnemy;


import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {
    private final Display display;
    private final GameMap map;
    private final Player player;

    private boolean won;

    private final Enemy[] enemies;

    ImageIcon icon = new ImageIcon("src/img/Game_Logo_hbrs.png");

    public Game() {
        super("Game");



        // 1) Karte auswählen, bevor Map/Player/Enemies angelegt werden
        // 1) Karte wählen

        int selectedMapIndex = chooseMap();           // NEU: Auswahl beim Start
        map = new GameMap(40, selectedMapIndex);      // NEU: mapIndex an GameMap

        display = new Display(this);
        // map = new GameMap(40);
        player = new Player(this, 13.5, 10.5, 0.375, 0.07);
        addKeyListener(player);

        enemies = new Enemy[]{
                new ChasingEnemy(this, player, 12.5, 8.5, 0.375, 0.06, Color.RED),
                new RandomEnemy(this, player, 13.5, 8.5, 0.375, 0.065, Color.GREEN),
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
        player.reset();
        for (Enemy enemy : enemies) {
            enemy.reset();
        }
    }

    public void win() {
        won = true;
    }

    public void lose() {
        JOptionPane.showMessageDialog(
                null,
                "Game Over!",
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

        // Game kennt den PowerMode nicht – nur Ticks durchreichen
        player.tick();
        for (Enemy enemy : enemies) {
            enemy.tick();
        }
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        int tileSize = map.getTileSize();

        map.render(g2, tileSize);
        player.render(g2, tileSize);
        for (Enemy enemy : enemies) {
            enemy.render(g2, tileSize);
        }
    }

    public GameMap getMap() {
        return map;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    static void main(String[] args) {
        new Game();
    }

}