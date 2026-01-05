package game.objects.creatures.enemy;

import game.Spiel;
import game.objects.creatures.Pacman;

import java.awt.*;

public class VerfolgerGeist extends Geist {
    public VerfolgerGeist(Spiel spiel, Pacman pacman, double centerX, double centerY, double radius, double speed, Color color) {
        super(spiel, pacman, centerX, centerY, radius, speed, color);
    }

    @Override
    protected void tickTarget() {
        targetX = (int) pacman.getCenterX();
        targetY = (int) pacman.getCenterY();
    }
}
