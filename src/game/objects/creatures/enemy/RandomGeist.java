package game.objects.creatures.enemy;

import game.Spiel;
import game.objects.creatures.Pacman;

import java.awt.*;
import java.util.Random;

public class RandomGeist extends Geist {
    private final Random random;

    public RandomGeist(Spiel spiel, Pacman pacman, double centerX, double centerY, double radius, double speed, Color color) {
        super(spiel, pacman, centerX, centerY, radius, speed, color);
        random = new Random();
    }

    @Override
    protected void tickTarget() {
        if ((int) centerX == targetX && (int) centerY == targetY) {
            int nextTargetX = random.nextInt(spiel.getMap().getWidth());
            int nextTargetY = random.nextInt(spiel.getMap().getHeight());

            if (!(spiel.getMap().isFree(nextTargetX, nextTargetY))) {
                return;
            }

            targetX = nextTargetX;
            targetY = nextTargetY;
        }
    }
}
