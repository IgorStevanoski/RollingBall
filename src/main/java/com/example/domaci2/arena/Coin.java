package com.example.domaci2.arena;

import com.example.domaci2.timer.Timer;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.paint.Material;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Coin extends Cylinder {
    private Rotate rotation;
    private Translate upndown;

    public Coin (double radius, double height, Material material, Translate position ) {
        super ( radius, height );

        rotation = new Rotate( 0, Rotate.Y_AXIS );
        upndown = new Translate( 0, 0, 0 );

        super.setMaterial ( material );

        super.getTransforms ( ).addAll (
                position,
                rotation,
                upndown,
                new Rotate( 90 , Rotate.Z_AXIS)
        );

        Timeline animation = new Timeline(
                new KeyFrame( Duration.seconds(0), new KeyValue( rotation.angleProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame( Duration.seconds(4), new KeyValue( rotation.angleProperty(), 360, Interpolator.LINEAR))
        );
        animation.setCycleCount( Animation.INDEFINITE );
        animation.setAutoReverse( false );
        animation.play();

        Timeline animation2 = new Timeline(
                new KeyFrame( Duration.seconds(0), new KeyValue( upndown.yProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame( Duration.seconds(2), new KeyValue( upndown.yProperty(), -radius, Interpolator.LINEAR))
        );
        animation2.setCycleCount( Animation.INDEFINITE );
        animation2.setAutoReverse( true );
        animation2.play();
    }

    public boolean handleCollision ( Sphere ball ) {
        Bounds ballBounds = ball.getBoundsInParent ( );

        double ballX = ballBounds.getCenterX ( );
        double ballZ = ballBounds.getCenterZ ( );
        double ballRadius = ball.getRadius();

        Bounds holeBounds = super.getBoundsInParent ( );
        double holeX      = holeBounds.getCenterX ( );
        double holeZ      = holeBounds.getCenterZ ( );
        double holeRadius = super.getRadius ( );

        double dx = holeX - ballX;
        double dz = holeZ - ballZ;

        double distance = dx * dx + dz * dz;

        boolean isInHole = distance < (holeRadius + ballRadius) * (holeRadius + ballRadius);

        return isInHole;
    }

}
