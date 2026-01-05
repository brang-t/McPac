package game.objects.creatures.enemy;

import game.Spiel;
import game.objects.creatures.Pacman;

import java.awt.*;

public class CuttingGeist extends Geist {
    public CuttingGeist(Spiel spiel, Pacman pacman, double centerX, double centerY, double radius, double speed, Color color) {
        super(spiel, pacman, centerX, centerY, radius, speed, color);
    }

    @Override
    protected void tickTarget() {
        targetX = (int) pacman.getCenterX();
        targetY = (int) pacman.getCenterY();
        int vx = pacman.getMovingDirectionX();
        int vy = pacman.getMovingDirectionY();

        if (vx != 0) {
            while (spiel.getMap().isFree(targetX + vx, targetY)) {
                targetX += vx;
            }
        } else if (vy != 0) {
            while (spiel.getMap().isFree(targetX, targetY + vy)) {
                targetY += vy;
            }
        }
    }
}
