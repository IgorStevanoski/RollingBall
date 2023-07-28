package com.example.domaci2.arena;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Material;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class RoundWall extends Cylinder {
    public RoundWall (double radius, double height, Material material, Translate position ) {
        super ( radius, height );

        super.setMaterial ( material );

        super.getTransforms ( ).add ( position );
    }

    public boolean handleCollision ( Ball ball ) {
        Bounds ballBounds = ball.getBoundsInParent ( );

        double ballX = ballBounds.getCenterX ( );
        double ballZ = ballBounds.getCenterZ ( );
        double ballRadius = ball.getRadius();
        Point3D ballSpeed = ball.getSpeed();

        double lastX = ballX - ballSpeed.getX();
        double lastZ = ballZ - ballSpeed.getZ();

        Bounds wallBounds = super.getBoundsInParent ( );
        double wallX      = wallBounds.getCenterX ( );
        double wallZ      = wallBounds.getCenterZ ( );
        double wallRadius = super.getRadius ( );

        double dx = wallX - ballX;
        double dz = wallZ - ballZ;

        double distance = dx * dx + dz * dz;

        boolean isInHole = distance < (wallRadius + ballRadius) * (wallRadius + ballRadius);

        Point3D normalVector = new Point3D( dx, 0, dz);

        if (isInHole) {

            normalVector = normalVector.normalize();
            Point3D newSpeed = ballSpeed.subtract(normalVector.multiply(ballSpeed.dotProduct(normalVector)).multiply(2));

            ball.setSpeed( newSpeed );
        }

        return isInHole;
    }

}

/*    Bounds ballBounds = ball.getBoundsInParent ( );

    double ballX = ballBounds.getCenterX ( );
    double ballZ = ballBounds.getCenterZ ( );
    double ballRadius = ball.getRadius();
    Point3D ballSpeed = ball.getSpeed();

    double lastX = ballX - ballSpeed.getX();
    double lastZ = ballZ - ballSpeed.getZ();

    Bounds wallBounds = super.getBoundsInParent ( );
    double wallX      = wallBounds.getCenterX ( );
    double wallZ      = wallBounds.getCenterZ ( );
    double wallRadius = super.getRadius ( );

    double dx = wallX - ballX;
    double dz = wallZ - ballZ;

    double distance = dx * dx + dz * dz;

    boolean isInHole = distance < (wallRadius + ballRadius) * (wallRadius + ballRadius);

        if (isInHole) {

                Point2D pointStart = new Point2D( wallRadius, 0);
                Point2D pointFrom  = new Point2D( ballX - wallX, ballZ - wallZ);
                double angle = pointStart.angle(pointFrom);

                double cos = Math.cos(Math.toDegrees(angle));
                double sin = Math.sin(Math.toDegrees(angle));

                double newX = lastX * cos + lastZ * sin;
                double newZ = -lastX * sin + lastZ * cos;

                newZ = -newZ;
                cos = Math.cos(Math.toDegrees(-angle));
                sin = Math.sin(Math.toDegrees(-angle));

                newX = newX * cos + newZ * sin;
                newZ = -newX * sin + newZ * cos;

                Point3D newSpeed = new Point3D( newX, 0, newZ );
                ball.setSpeed( newSpeed );
                }

                return isInHole;
                }*/