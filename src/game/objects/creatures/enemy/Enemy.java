/* package game.objects.creatures.enemy;

import game.Game;
import game.objects.creatures.Creature;
import game.objects.creatures.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public abstract class Enemy extends Creature {
    protected Player player;

    protected int targetX;
    protected int targetY;

    public Enemy(Game game, Player player, double centerX, double centerY, double radius, double speed, Color color) {
        super(game, centerX, centerY, radius, speed, color);
        this.player = player;
        targetX = (int) centerX;
        targetY = (int) centerY;
    }

    private Node shortestDirectionTo(int goalX, int goalY) {
        Node startNode = new Node((int) centerX, (int) centerY, null, 0, goalX, goalY);

        Queue<Node> queue = new PriorityQueue<>(Node::compareTo);
        queue.add(startNode);

        Set<Node> visited = new HashSet<>();

        Node currentNode;
        while ((currentNode = queue.poll()) != null) {
            if (visited.contains(currentNode)) {
                continue;
            }

            if (currentNode.getX() == goalX && currentNode.getY() == goalY) {
                return currentNode.initialDirection();
            }

            visited.add(currentNode);
            queue.addAll(currentNode.neighbors(game.getMap(), goalX, goalY));
        }

        return null;
    }

    protected abstract void tickTarget();

    @Override
    public void tickPreferredDirection() {
        tickTarget();

        Node aim = shortestDirectionTo(targetX, targetY);
        if (aim != null) {
            preferredDirectionX = Integer.signum(aim.getX() - (int) centerX);
            preferredDirectionY = Integer.signum(aim.getY() - (int) centerY);
        }
    }

    private void tickPlayerCollision() {
        double dx = player.getCenterX() - centerX;
        double dy = player.getCenterY() - centerY;
        double r = player.getRadius() + radius;

        if (dx * dx + dy * dy < r * r) {
            game.lose();
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickPlayerCollision();
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        double centerXOnScreen = centerX * tileSize;
        double centerYOnScreen = centerY * tileSize;
        double radiusOnScreen = radius * tileSize;
        double sizeOnScreen = radiusOnScreen * 2.0;

        g.setColor(color);
        g.fill(new Rectangle2D.Double(centerXOnScreen - radiusOnScreen, centerYOnScreen - radiusOnScreen, sizeOnScreen, sizeOnScreen));

        renderEyes(g, centerXOnScreen, centerYOnScreen, radiusOnScreen, targetX + 0.5, targetY + 0.5);
    }
}*/

package game.objects.creatures.enemy;

import game.Game;
import game.objects.creatures.Creature;
import game.objects.creatures.Player;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public abstract class Enemy extends Creature {
    protected Player player;

    protected int targetX;
    protected int targetY;

    // Zustände für Power-Mode
    protected enum State {
        NORMAL,
        FRIGHTENED, // flieht, langsamer, kann gefressen werden
        EATEN       // unsichtbar, wartet auf Respawn
    }

    protected State state = State.NORMAL;

    protected final double normalSpeed;
    protected final double frightenedSpeed;

    // Fixpunkt für Flucht / Respawn (Startposition)
    protected final double spawnX;
    protected final double spawnY;

    protected long respawnTimeMs;

    public Enemy(Game game, Player player,
                 double centerX, double centerY,
                 double radius, double speed, Color color) {
        super(game, centerX, centerY, radius, speed, color);
        this.player = player;
        targetX = (int) centerX;
        targetY = (int) centerY;

        this.normalSpeed = speed;
        this.frightenedSpeed = speed * 0.5; // z. B. halb so schnell
        this.spawnX = centerX;
        this.spawnY = centerY;
    }

    private Node shortestDirectionTo(int goalX, int goalY) {
        Node startNode = new Node((int) centerX, (int) centerY, null, 0, goalX, goalY);

        Queue<Node> queue = new PriorityQueue<>(Node::compareTo);
        queue.add(startNode);

        Set<Node> visited = new HashSet<>();

        Node currentNode;
        while ((currentNode = queue.poll()) != null) {
            if (visited.contains(currentNode)) {
                continue;
            }

            if (currentNode.getX() == goalX && currentNode.getY() == goalY) {
                return currentNode.initialDirection();
            }

            visited.add(currentNode);
            queue.addAll(currentNode.neighbors(game.getMap(), goalX, goalY));
        }

        return null;
    }

    // Zielberechnung im Normalzustand ist je nach Enemy-Typ unterschiedlich
    protected abstract void tickTarget();

    @Override
    public void tickPreferredDirection() {
        if (state == State.FRIGHTENED) {
            // Im Angstzustand: Ziel ist Spawnpunkt (Flucht)
            Node aim = shortestDirectionTo((int) spawnX, (int) spawnY);
            if (aim != null) {
                preferredDirectionX = Integer.signum(aim.getX() - (int) centerX);
                preferredDirectionY = Integer.signum(aim.getY() - (int) centerY);
            }
        } else {
            // Normales Chasing/Random/Cutting-Target
            tickTarget();

            Node aim = shortestDirectionTo(targetX, targetY);
            if (aim != null) {
                preferredDirectionX = Integer.signum(aim.getX() - (int) centerX);
                preferredDirectionY = Integer.signum(aim.getY() - (int) centerY);
            }
        }
    }

    private void tickPlayerCollision() {
        double dx = player.getCenterX() - centerX;
        double dy = player.getCenterY() - centerY;
        double r = player.getRadius() + radius;

        if (dx * dx + dy * dy < r * r) {
            if (player.isPowerModeActive() && state == State.FRIGHTENED) {
                // Pacman darf verängstigten Geist fressen
                onEaten();
            } else if (state != State.EATEN) {
                // Normalfall: Spieler verliert (solange Geist nicht "tot")
                game.lose();
            }
        }
    }

    @Override
    public void tick() {
        if (state == State.EATEN) {
            // solange "tot": nicht bewegen, auf Respawn warten
            if (System.currentTimeMillis() >= respawnTimeMs) {
                // Respawn am Spawnpunkt
                centerX = spawnX;
                centerY = spawnY;

                // Falls PowerMode noch aktiv ist → gleich wieder FRIGHTENED
                if (player.isPowerModeActive()) {
                    state = State.FRIGHTENED;
                    speed = frightenedSpeed;
                } else {
                    state = State.NORMAL;
                    speed = normalSpeed;
                }
            }
            return;
        }

        super.tick();
        tickPlayerCollision();
    }

    // Wird vom Player aufgerufen, wenn PowerMode beginnt
    public void onPowerModeStart() {
        if (state == State.NORMAL) {
            state = State.FRIGHTENED;
            speed = frightenedSpeed;
        }
    }

    // Wird vom Player aufgerufen, wenn PowerMode endet
    public void onPowerModeEnd() {
        if (state == State.FRIGHTENED) {
            state = State.NORMAL;
            speed = normalSpeed;
        }
        // EATEN bleibt EATEN, Respawn läuft separat
    }

    protected void onEaten() {
        state = State.EATEN;
        respawnTimeMs = System.currentTimeMillis() + 3000; // 3 Sekunden weg
    }

    @Override
    public void reset() {
        super.reset();
        state = State.NORMAL;
        speed = normalSpeed;
    }

    @Override
    public void render(Graphics2D g, int tileSize) {
        if (state == State.EATEN) {
            // unsichtbar, solange "tot"
            return;
        }

        double centerXOnScreen = centerX * tileSize;
        double centerYOnScreen = centerY * tileSize;
        double radiusOnScreen = radius * tileSize;
        double sizeOnScreen = radiusOnScreen * 2.0;

        if (state == State.FRIGHTENED) {
            g.setColor(Color.CYAN);
        } else {
            g.setColor(color);
        }

        g.fill(new Rectangle2D.Double(
                centerXOnScreen - radiusOnScreen,
                centerYOnScreen - radiusOnScreen,
                sizeOnScreen,
                sizeOnScreen));


        renderEyes(g,
                centerXOnScreen,
                centerYOnScreen,
                radiusOnScreen);
    }
}



