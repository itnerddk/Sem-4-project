package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameKeys;
import org.sdu.sem4.g7.common.data.Level;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

public class PlayerControlSystem implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, Level world) {
        for (Entity entity : world.getEntities(Player.class)) {
            // Controls
            Player player = (Player) entity;

            
            
            // Forward and backward
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                player.setSpeed(
                    lerp(
                        player.getSpeed(),
                        Math.min(player.getSpeed() + player.getAcceleration(), player.getMaxSpeed()),
                        0.7
                    )
                );
            } else if (gameData.getKeys().isDown(GameKeys.DOWN)) {
                player.setSpeed(
                    lerp(
                        player.getSpeed(),
                        Math.max(player.getSpeed() - player.getDeacceleration(), -(player.getMaxSpeed()/2)),
                        0.5
                    )
                );
            } else {
                // Drag of 1
                player.setSpeed(lerp(player.getSpeed(), 0, 0.1));
            }

            // Rotate
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - player.getRotationSpeed());
            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + player.getRotationSpeed());
            }

            

            double changeX = Math.cos(Math.toRadians(player.getRotation() - 90));
            double changeY = Math.sin(Math.toRadians(player.getRotation() - 90));

            
            // Set position
            player.setPosition(player.getPosition().getX() + (changeX * player.getSpeed()), player.getPosition().getY() + (changeY * player.getSpeed()));
        }
    }

    double lerp(double a, double b, double f)
    {
        return a * (1.0 - f) + (b * f);
    }
    
}
