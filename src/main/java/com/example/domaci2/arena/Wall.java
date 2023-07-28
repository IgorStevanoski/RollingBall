package com.example.domaci2.arena;

import javafx.geometry.Bounds;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

public class Wall extends Box {

    public Wall(double width, double height, double depth, Material material, Translate position){
        super(width, height, depth);

        super.setMaterial(material);

        super.getTransforms().addAll( position );
    }

    public boolean handleCollision (Ball ball) {
        Bounds ballBounds = ball.getBoundsInParent ( );
        double ballMaxX      = ballBounds.getMaxX ( );
        double ballMinX      = ballBounds.getMinX ( );
        double ballMaxZ      = ballBounds.getMaxZ ( );
        double ballMinZ      = ballBounds.getMinZ ( );

        double ballRadius = ballBounds.getWidth() / 2;
        double ballX = ballBounds.getCenterX ( );
        double ballZ = ballBounds.getCenterZ ( );

        Bounds wallBounds = super.getBoundsInParent ( );
        double wallMaxX      = wallBounds.getMaxX ( );
        double wallMinX      = wallBounds.getMinX ( );
        double wallMaxZ      = wallBounds.getMaxZ ( );
        double wallMinZ      = wallBounds.getMinZ ( );

        double wallX = wallBounds.getCenterX ( );
        double wallZ = wallBounds.getCenterZ ( );

        if ((ballMaxX >= wallMinX && ballMaxX <= wallMaxX
                && (Math.abs(ballZ - wallZ) < ballRadius + this.getDepth() / 2))
                && (Math.abs(ballMaxX - wallMinX) < Math.abs(ballMaxZ - wallMinZ))
                && (Math.abs(ballMaxX - wallMinX) < Math.abs(ballMinZ - wallMaxZ))
        ) {
            ball.changeDirection( -1, 1); return true;
        }else if ((ballMinX >= wallMinX && ballMinX <= wallMaxX
                && (Math.abs(ballZ - wallZ) < ballRadius + this.getDepth() / 2))
                && (Math.abs(ballMinX - wallMaxX) < Math.abs(ballMaxZ - wallMinZ))
                && (Math.abs(ballMinX - wallMaxX) < Math.abs(ballMinZ - wallMaxZ))
        )  {
            ball.changeDirection( -1, 1); return true;
        }else if ((ballMaxZ >= wallMinZ && ballMaxZ <= wallMaxZ
                && (Math.abs(ballX - wallX) < ballRadius + this.getWidth() / 2))
                && (Math.abs(ballMaxZ - wallMinZ) < Math.abs(ballMaxX - wallMinX))
                && (Math.abs(ballMaxZ - wallMinZ) < Math.abs(ballMinX - wallMaxX))
        ) {
            ball.changeDirection( 1, -1); return true;
        } else if ((ballMinZ >= wallMinZ && ballMinZ <= wallMaxZ
                && (Math.abs(ballX - wallX) < ballRadius + this.getWidth() / 2))
                && (Math.abs(ballMinZ - wallMaxZ) < Math.abs(ballMaxX - wallMinX))
                && (Math.abs(ballMinZ - wallMaxZ) < Math.abs(ballMinX - wallMaxX))
        )  {
            ball.changeDirection( 1, -1); return true;
        }

        return false;
    }
}
