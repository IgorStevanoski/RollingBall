package com.example.domaci2;

import com.example.domaci2.arena.*;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.List;

public class MapPrepare {

    //OBJECTS -------------------------------------------------------------------------------------
    public static Box addPodium (double width, double height, double depth, Material material) {
        Box podium = new Box (
                width,
                height,
                depth
        );
        podium.setMaterial ( material );

        return podium;
    }

    public static Ball addBall ( double radius, Translate position, int chosen ){
        Material ballMaterial;

        if ( chosen == 1 ) ballMaterial = new PhongMaterial ( Color.LIGHTGREEN );
        else if ( chosen == 2 ) ballMaterial = new PhongMaterial ( Color.ALICEBLUE );
        else ballMaterial = new PhongMaterial ( Color.RED );

        return new Ball ( radius, ballMaterial, position );
    }

    public static Hole addHole ( double width, double depth, double height, double radius, int level) {
        double x = ( width / 2 - 2 * radius );
        double z = - ( depth / 2 - 2 * radius );

        Translate holePosition = new Translate ( x, -30, z );
        if ( level == 1) holePosition = new Translate( 0, -30, 0);
        Material holeMaterial = new PhongMaterial ( Color.YELLOW );

        return new Hole (
                radius,
                height,
                holeMaterial,
                holePosition
        );
    }

    public static void addBadHoles ( double width, double depth, double height, double radius, List<BadHole> holes, int level) {
        Material material = new PhongMaterial( Color.BLACK);

        BadHole hole1;
        BadHole hole2;

        Translate hole1Position;
        Translate hole2Position;

        if ( level == 1 ) {
            hole1Position = new Translate( -width / 4,-30, 0 );
            hole1 = new BadHole( radius, height, material, hole1Position);
            holes.add( hole1 );
        } else if ( level == 2 ) {
            hole1Position = new Translate(  width / 8, -30, depth * 3 / 8 - (radius + 20));
            hole1 = new BadHole( radius, height, material, hole1Position);

            hole2Position = new Translate( width / 8,-30, -depth * 2 / 8 - radius);
            hole2 = new BadHole( radius * 2, height, material, hole2Position);

            holes.add( hole1 );
            holes.add( hole2 );
        }

    }

    public static Lamp addLamp ( double width ) {
        PhongMaterial material = new PhongMaterial( );
        Image image = new Image( Main.class.getClassLoader().getResourceAsStream("selfIllumination.png"));
        material.setSelfIlluminationMap( image );
        material.setSpecularColor( Color.WHITE );
        material.setSpecularPower( 1 );

        Translate position = new Translate( 0, -1200, 0);
        return new Lamp( width, width, width, material, position);
    }

    public static void addWalls ( double width, double height, double depth, double podWidth, double podDepth, List<Wall> walls, int level ) {
        Translate wall1position = new Translate( 0,  - height / 2, podDepth / 2 - depth );
        Wall wall1 = new Wall( width, height, depth, new PhongMaterial( Color.RED ), wall1position );

        Translate wall2position = new Translate( 0,  - height / 2,  - (podDepth / 2 - depth ) );
        Wall wall2 = new Wall( width, height, depth, new PhongMaterial( Color.RED ), wall2position );

        Translate wall3position = new Translate( podWidth / 2 - depth ,  - height / 2, 0 );
        Wall wall3 = new Wall( depth, height, width, new PhongMaterial( Color.RED ), wall3position );

        Translate wall4position = new Translate(  - (podWidth / 2 - depth),  - height / 2, 0 );
        Wall wall4 = new Wall( depth, height, width, new PhongMaterial( Color.RED ), wall4position );

        Translate wall5position;
        Translate wall6position;
        Translate wall7position;
        Wall wall5;
        Wall wall6;
        Wall wall7;

        if ( level == 1 ) {
            wall1position = new Translate( 0,  - height / 2, podDepth / 2 - depth );
            wall1 = new Wall( width / 2, height, depth, new PhongMaterial( Color.RED ), wall1position );

            wall2position = new Translate( 0,  - height / 2,  - (podDepth / 2 - depth ) );
            wall2 = new Wall( width / 2, height, depth, new PhongMaterial( Color.RED ), wall2position );

            wall3 = new Wall( depth, height, width / 3, new PhongMaterial( Color.RED ), wall3position );

            wall5position = new Translate( 0,  - height / 2, podDepth / 8 - depth );
            wall5 = new Wall( width, height, depth, new PhongMaterial( Color.RED ), wall5position );

            wall6position = new Translate( 0,  - height / 2,  - (podDepth / 8 - depth ) );
            wall6 = new Wall( width, height, depth, new PhongMaterial( Color.RED ), wall6position );

            walls.add( wall5 );
            walls.add( wall6 );

        } else if ( level == 2 ) {
            wall1position = new Translate( 0,  - height / 2, podDepth / 2 - depth );
            wall1 = new Wall( width * 3 / 2, height, depth, new PhongMaterial( Color.RED ), wall1position );

            wall2position = new Translate( - width / 2,  - height / 2,  - (podDepth / 2 - depth ) );
            wall2 = new Wall( width, height, depth, new PhongMaterial( Color.RED ), wall2position );

            wall3position = new Translate( podWidth / 8 - depth ,  - height / 2, 0 );
            wall3 = new Wall( depth, height, width * 3 / 4, new PhongMaterial( Color.RED ), wall3position );

            wall4position = new Translate(  - (podWidth / 8 - depth),  - height / 2, 0 );
            wall4 = new Wall( depth, height, width * 3 / 4, new PhongMaterial( Color.RED ), wall4position );

            wall5position = new Translate( 0,  - height / 2, podDepth  * 3 / 8 - depth );
            wall5 = new Wall( width, height, depth, new PhongMaterial( Color.RED ), wall5position );

            wall6position = new Translate( podWidth / 2,  - height / 2, 0 );
            wall6 = new Wall( depth, height, width, new PhongMaterial( Color.RED ), wall6position );

            wall7position = new Translate( -podWidth / 2,  - height / 2, 0 );
            wall7 = new Wall( depth, height, width * 2, new PhongMaterial( Color.RED ), wall7position );

            walls.add( wall5 );
            walls.add( wall6 );
            walls.add( wall7 );
        }

        walls.add( wall1 );
        walls.add( wall2 );
        walls.add( wall3 );
        walls.add( wall4 );
    }

    public static void addRoundWalls ( double radius, double height, double podWidth, double podDepth, List<RoundWall> walls, int level) {
        PhongMaterial material = new PhongMaterial();
        Image image = new Image( Main.class.getClassLoader().getResourceAsStream( "obstacle.jpg" ));
        material.setDiffuseMap( image );

        Translate wall1position = new Translate( podWidth / 4,  - height / 2, podDepth / 4 );
        RoundWall roundWall1 = new RoundWall( radius, height, material, wall1position );

        Translate wall2position = new Translate( podWidth / 4,  - height / 2, -podDepth / 4 );
        RoundWall roundWall2 = new RoundWall( radius, height, material, wall2position );

        Translate wall3position = new Translate( -podWidth / 4,  - height / 2, podDepth / 4 );
        RoundWall roundWall3 = new RoundWall( radius, height, material, wall3position );

        Translate wall4position = new Translate(  -podWidth / 4, - height / 2, -podDepth / 4 );
        RoundWall roundWall4 = new RoundWall( radius, height, material, wall4position );

        Translate wall5position;
        Translate wall6position;

        RoundWall roundWall5;
        RoundWall roundWall6;

        if ( level == 1 ) {
            wall1position = new Translate( podWidth / 4,  - height / 2, podDepth / 8 );
            roundWall1 = new RoundWall( radius, height, material, wall1position );

            wall2position = new Translate( podWidth / 4,  - height / 2, -podDepth / 8 );
            roundWall2 = new RoundWall( radius, height, material, wall2position );

            wall3position = new Translate( -podWidth / 4,  - height / 2, podDepth / 8 );
            roundWall3 = new RoundWall( radius, height, material, wall3position );

            wall4position = new Translate(  -podWidth / 4, - height / 2, -podDepth / 8 );
            roundWall4 = new RoundWall( radius, height, material, wall4position );

            wall5position = new Translate( 0,  - height / 2, podDepth / 4 );
            roundWall5 = new RoundWall( radius, height, material, wall5position );

            wall6position = new Translate(  0, - height / 2, -podDepth / 4 );
            roundWall6 = new RoundWall( radius, height, material, wall6position );

            walls.add( roundWall5 );
            walls.add( roundWall6 );
        } else if ( level == 2 ) {
            wall1position = new Translate( -podWidth * 5 / 16,  - height / 2, -podDepth / 4);
            roundWall1 = new RoundWall( radius, height, material, wall1position );

            wall2position = new Translate( podWidth / 8,  - height / 2, -podDepth / 2 * 1 / 3 );
            roundWall2 = new RoundWall( radius, height, material, wall2position );

            wall3position = new Translate( podWidth * 7 / 16,  - height / 2, 0 );
            roundWall3 = new RoundWall( radius * 2, height, material, wall3position );

            wall4position = new Translate(  -podWidth / 8, - height / 2, podDepth / 2 / 3 );
            roundWall4 = new RoundWall( radius, height, material, wall4position );
        }

        walls.add( roundWall1 );
        walls.add( roundWall2 );
        walls.add( roundWall3 );
        walls.add( roundWall4 );
    }

    public static void addBounceWalls ( double width, double height, double podWidth, double podDepth, List<BounceWall> walls, int level) {
        Translate wall1position = new Translate( 0,  - height / 2, 0 );
        BounceWall wall1 = new BounceWall( width, height, width, new PhongMaterial( Color.WHITE ), wall1position);

        Translate wall2position;
        BounceWall wall2;

        if ( level == 1 ) {
            wall1position = new Translate( podWidth / 2 - width / 2,  - height / 2, width * 2  );
            wall1 = new BounceWall( width, height, width, new PhongMaterial( Color.WHITE ), wall1position);

            wall2position = new Translate( podWidth / 2 - width / 2,  - height / 2, -width * 2 );
            wall2 = new BounceWall( width, height, width, new PhongMaterial( Color.WHITE ), wall2position);

            walls.add( wall2 );
        } else if ( level == 2 ) {
            wall1position = new Translate( - podWidth / 4 - width / 2,  - height / 2, podDepth * 3 / 8 - width / 2 - 10);
            wall1 = new BounceWall( width, height, width, new PhongMaterial( Color.WHITE ), wall1position);

            wall2position = new Translate( podWidth * 3 / 8 - width * 3 / 2,  - height / 2, - podDepth / 2 + width / 2);
            wall2 = new BounceWall( width, height, width, new PhongMaterial( Color.WHITE ), wall2position);

            walls.add( wall2 );
        }

        walls.add( wall1 );
    }

    public static void addCoins (double radius, double podium_width, double podium_depth,
                                 double podium_height, List<Coin> coins, int level) {
        Material material = new PhongMaterial( Color.GOLD );

        Translate coin1translate = new Translate( podium_width / 4, -(radius + podium_height / 2), 0);
        Coin coin1 = new Coin( radius, 5, material, coin1translate);

        Translate coin2translate = new Translate( -podium_width / 4, -(radius + podium_height / 2), 0);
        Coin coin2 = new Coin( radius, 5, material, coin2translate);

        Translate coin3translate = new Translate( 0, -(radius + podium_height / 2), podium_depth / 4);
        Coin coin3 = new Coin( radius, 5, material, coin3translate);

        Translate coin4translate = new Translate( 0, -(radius + podium_height / 2), -podium_depth / 4);
        Coin coin4 = new Coin( radius, 5, material, coin4translate);

        if ( level == 1 ) {
            coin1translate = new Translate( podium_width * 3 / 8, -(radius + podium_height / 2), 0);
            coin1 = new Coin( radius, 5, material, coin1translate);

            coin2translate = new Translate( -podium_width * 3 / 8, -(radius + podium_height / 2), 0);
            coin2 = new Coin( radius, 5, material, coin2translate);

            coin3translate = new Translate( 0, -(radius + podium_height / 2), podium_depth * 3 / 8);
            coin3 = new Coin( radius, 5, material, coin3translate);

            coin4translate = new Translate( 0, -(radius + podium_height / 2), -podium_depth * 3 / 8);
            coin4 = new Coin( radius, 5, material, coin4translate);
        } else if ( level == 2 ) {
            coin2translate = new Translate( -podium_width * 5 / 16, -(radius + podium_height / 2), -podium_depth * 3 / 8);
            coin2 = new Coin( radius, 5, material, coin2translate);

            coin3translate = new Translate( podium_width * 7 / 16, -(radius + podium_height / 2), podium_depth * 7 / 16);
            coin3 = new Coin( radius, 5, material, coin3translate);

            coin4translate = new Translate( -podium_width * 5 / 16, -(radius + podium_height / 2), 0);
            coin4 = new Coin( radius, 5, material, coin4translate);


        }

        coins.add( coin1 );
        coins.add( coin2 );
        coins.add( coin3 );
        coins.add( coin4 );
    };

    //CAMERA --------------------------------------------------------------------------------------
    public static Camera addDefaultCamera ( double far_clip, double camera_x_angle, Translate cameraDistance,
                                            Rotate cameraRotateY, Rotate cameraRotateX, Group root ) {
        Camera camera = new PerspectiveCamera( true );
        camera.setFarClip ( far_clip );
        camera.getTransforms ( ).addAll (
                cameraRotateY,
                cameraRotateX,
                new Rotate( camera_x_angle, Rotate.X_AXIS ),
                cameraDistance
        );
        root.getChildren ( ).add ( camera );

        return camera;
    }

    public static Camera addBirdViewCamera ( double far_clip, Translate position ) {
        Camera camera = new PerspectiveCamera( true );
        camera.setFarClip ( far_clip );
        camera.getTransforms().addAll(
                new Translate( 0, -2000, 0),
                position,
                new Rotate( -90, Rotate.X_AXIS)
        );

        return camera;
    }
    //2D ------------------------------------------------------------------------------------------

    public static void addLifePoints( int max_life, double radius, Group root, Circle lives[]) {
        for (int i = 0; i < max_life; i++) {
            Translate lifePosition = new Translate(
                    2 * radius + 3 * i * radius,
                    2 * radius
            );
            Circle life = new Circle(radius, Color.RED);
            life.getTransforms().add(lifePosition);
            root.getChildren().addAll(life);
            lives[i] = life;
        }
    }

    public static Group addVector( double width, Group root, Line line ) {
        Group vector = new Group();

        Rectangle squareBig = new Rectangle( width, width, Color.RED);
        Rectangle squareSmall = new Rectangle( width - 4, width - 4, Color.GREEN);
        /*Line line = new Line( 0, 0, width / 2, width / 2);
        line.setStroke( Color.RED );*/

        squareSmall.getTransforms().add( new Translate( 2, 2) );
        line.getTransforms().add( new Translate( width / 2, width / 2) );

        vector.getChildren().addAll( squareBig, squareSmall, line );
        vector.getTransforms().add( new Translate( 0, width * 4));
        root.getChildren().add( vector );

        return vector;
    }

    public static void addStartButton(double width, double height, Group root) {
        Rectangle startRectangle = new Rectangle( width / 5, height / 10, Color.YELLOW );
        startRectangle.getTransforms().add(
                new Translate( width * 4 / 10, height * 9 / 20)
        );
        startRectangle.setStroke( Color.RED );
        startRectangle.setStrokeWidth( 3 );
        Text startText = new Text( width / 2 - width / 10 + 10, height / 2 + 20, "START");
        startText.setFont( new Font(50));
        startText.setFill( Color.RED );

        root.getChildren().addAll( startRectangle, startText);
    }

    public static Path addChoiceRight(double width, double height) {
        Path choice = new Path (
                new MoveTo(0, 0),
                new LineTo( width / 10, height / 20),
                new LineTo( 0, height / 10),
                new ClosePath()
        );

        choice.getTransforms().add(
                 new Translate( width * 8 / 10, height / 2 - height / 20 )
        );

        choice.setFill( Color.YELLOW );
        choice.setStrokeWidth( 3 );
        choice.setStroke(Color.RED);

        return choice;
    }

    public static Path addChoiceLeft(double width, double height) {
        Path choice = new Path (
                new MoveTo(0, 0),
                new LineTo(  - width / 10, height / 20),
                new LineTo( 0, height / 10),
                new ClosePath()
        );

        choice.getTransforms().add(
                 new Translate( width * 2 / 10, height / 2 - height / 20 )
        );

        choice.setFill( Color.YELLOW );
        choice.setStrokeWidth( 3 );
        choice.setStroke(Color.RED);

        return choice;
    }
}
