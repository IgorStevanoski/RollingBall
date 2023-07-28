package com.example.domaci2;

import com.example.domaci2.arena.*;
import com.example.domaci2.timer.Timer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
	private static final double WINDOW_WIDTH  = 800;
	private static final double WINDOW_HEIGHT = 800;
	
	private static final double PODIUM_WIDTH  = 2000;
	private static final double PODIUM_HEIGHT = 10;
	private static final double PODIUM_DEPTH  = 2000;
	
	private static final double CAMERA_FAR_CLIP = 100000;
	private static final double CAMERA_Z        = -5000;
	private static final double CAMERA_X_ANGLE  = -45;
	
	private static final double BALL_RADIUS = 50;
	
	private static final double BALL_DAMP = 0.999;
	private static final double ARENA_DAMP = 0.995;

	private static final double MAX_ANGLE_OFFSET = 30;
	private static final double MAX_ACCELERATION = 400;
	
	private static final int    NUMBER_OF_HOLES = 4;
	private static final double HOLE_RADIUS     = 2 * Main.BALL_RADIUS;
	private static final double HOLE_HEIGHT     = PODIUM_HEIGHT;

	private static final double WALL_WIDTH = PODIUM_WIDTH / 2;
	private static final double WALL_HEIGHT = 100;
	private static final double WALL_DEPTH = 10;
	private static final double LIFE_POINT_RADIUS = 10;
	private static final double VECTOR_LINE_LENGTH = WINDOW_WIDTH / 10;
	private static final int MAX_LIFE_POINT = 5;
	private static final int POINTS_HOLE = 10;
	private static final int POINTS_COIN = 5;
	private static final int NUM_OF_LEVELS = 3;
	private static final double TIME_LIMIT = 60;

	private Group root;
	private Ball  ball;
	private Arena arena;
	private Hole hole;
	private Camera defaultCamera;
	private Camera birdViewCamera;
	private Scene scene;

	private Box podium;
	private Timer timer;
	private List<Wall> walls = new ArrayList<Wall>();
	private int cameraActive = 1;
	private Translate cameraDistance;
	private Translate cameraDistance2D;
	private Translate ballPosition;
	private Rotate cameraRotateY;
	private Rotate cameraRotateX;
	private double previousX = 0;
	private double previousY = 0;

	private SubScene subSceneMain;
	private SubScene subScene2D;
	private SubScene selectScene2D;
	private Group rootSubSceneMain;
	private Group rootSubScene2D;
	private Group rootSelectScene2D;

	private Circle lifepoints[] = new Circle[MAX_LIFE_POINT];
	private int remainingLifepoints = MAX_LIFE_POINT;
	private Text pointsText;
	private int points;
	private List<Coin> coins = new ArrayList<Coin>();
	private List<RoundWall> roundWalls = new ArrayList<RoundWall>();
	private List<BounceWall> bounceWalls = new ArrayList<BounceWall>();
	private List<BadHole> badHoles = new ArrayList<BadHole>();
	private Lamp lamp;
	private PointLight light;
	private boolean lightOn;
	private Line vectorLine;
	private int roundWallCnt = 0;
	private int wallHitCounter = 0;
	private Group startButton;
	private Path leftChoice;
	private Path rightChoice;
	private int  levelChosen = 0;
	private int  ballChosen = 0;
	private Translate ballPickPosition;
	private Rectangle timerBar;
	private Scale timerScale = new Scale(1, 0);
	private Text timerText;
	private double timeRemaining = TIME_LIMIT;

	@Override
	public void start ( Stage stage ) throws IOException {
		this.root = new Group ( );
		this.rootSubSceneMain = new Group ( );
		this.rootSubScene2D = new Group ( );
		this.rootSelectScene2D = new Group ( );
		this.startButton = new Group ( );

		remainingLifepoints = MAX_LIFE_POINT;
		points = 0;
		pointsText = new Text( WINDOW_WIDTH - 50, 30, String.valueOf( points ));
		pointsText.setFont( new Font(30));
		pointsText.setFill( Color.RED );
		rootSubScene2D.getChildren().add( pointsText );
		vectorLine = new Line( 0, 0, VECTOR_LINE_LENGTH, VECTOR_LINE_LENGTH );
		vectorLine.setStroke( Color.RED );

		this.scene = new Scene (
				this.root,
				Main.WINDOW_WIDTH,
				Main.WINDOW_HEIGHT,
				true,
				SceneAntialiasing.BALANCED
		);

		this.subSceneMain = new SubScene(
				this.rootSubSceneMain,
				Main.WINDOW_WIDTH,
				Main.WINDOW_HEIGHT,
				true,
				SceneAntialiasing.BALANCED
		);
		this.subScene2D = new SubScene(
				this.rootSubScene2D,
				Main.WINDOW_WIDTH,
				Main.WINDOW_HEIGHT
		);
		this.selectScene2D = new SubScene(
				this.rootSelectScene2D,
				Main.WINDOW_WIDTH,
				Main.WINDOW_HEIGHT
		);

		this.ballPosition = new Translate (
				- ( Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS ),
				- ( Main.BALL_RADIUS + Main.PODIUM_HEIGHT / 2 ),
				Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS
		);
		if ( levelChosen == 1 ) {
			ballPosition.setZ( 0 );
		} else if ( levelChosen == 2 ) {
			ballPosition.setZ( 0 );
			ballPosition.setX( 0 );
		}
		this.ball = MapPrepare.addBall ( Main.BALL_RADIUS, ballPosition, ballChosen);
		this.podium = MapPrepare.addPodium( PODIUM_WIDTH, PODIUM_HEIGHT, PODIUM_DEPTH, new PhongMaterial( Color.BLUE) );
		this.hole = MapPrepare.addHole( PODIUM_WIDTH, PODIUM_DEPTH, HOLE_HEIGHT, HOLE_RADIUS, levelChosen);
		this.lamp = MapPrepare.addLamp( BALL_RADIUS * 2 );
		this.rightChoice = MapPrepare.addChoiceRight( WINDOW_WIDTH, WINDOW_HEIGHT);
		this.leftChoice	= MapPrepare.addChoiceLeft( WINDOW_WIDTH, WINDOW_HEIGHT);

		MapPrepare.addVector( WINDOW_WIDTH / 5, rootSubScene2D, vectorLine);
		MapPrepare.addStartButton( WINDOW_WIDTH, WINDOW_HEIGHT, startButton);
		MapPrepare.addWalls( WALL_WIDTH, WALL_HEIGHT, WALL_DEPTH, PODIUM_WIDTH, PODIUM_DEPTH, walls, levelChosen);
		MapPrepare.addBounceWalls( BALL_RADIUS * 3, WALL_HEIGHT, PODIUM_WIDTH, PODIUM_DEPTH, bounceWalls, levelChosen);
		MapPrepare.addRoundWalls( BALL_RADIUS, WALL_HEIGHT * 2, PODIUM_WIDTH, PODIUM_DEPTH, roundWalls, levelChosen);
		MapPrepare.addBadHoles( PODIUM_WIDTH, PODIUM_DEPTH, HOLE_HEIGHT, HOLE_RADIUS, badHoles, levelChosen);
		MapPrepare.addLifePoints( MAX_LIFE_POINT, LIFE_POINT_RADIUS, rootSubScene2D, lifepoints);
		MapPrepare.addCoins( BALL_RADIUS, PODIUM_WIDTH, PODIUM_DEPTH, PODIUM_HEIGHT, coins, levelChosen);

		this.addBallSelect();
		this.addTimerScale( timerScale, rootSubScene2D );

		this.cameraDistance = new Translate( 0, 0, CAMERA_Z);
		this.cameraDistance2D = new Translate( 0, 0, -4800 );
		this.cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
		this.cameraRotateX = new Rotate(0, Rotate.X_AXIS);
		this.defaultCamera = MapPrepare.addDefaultCamera ( CAMERA_FAR_CLIP, CAMERA_X_ANGLE,
				cameraDistance, cameraRotateY, cameraRotateX, rootSubSceneMain);
		this.birdViewCamera = MapPrepare.addBirdViewCamera( CAMERA_FAR_CLIP, ballPosition);
		subSceneMain.setCamera ( this.defaultCamera );
		cameraActive = 1;

		light = new PointLight( Color.WHITE );
		light.getTransforms().add( new Translate( 0, -1200, 0));

		this.arena = new Arena ( );
		this.arena.getChildren ( ).add ( podium );
		this.arena.getChildren ( ).add ( this.ball );
		this.arena.getChildren ( ).add ( this.hole );
		this.arena.getChildren ( ).add ( this.lamp );
		this.arena.getChildren ( ).add ( this.light );
		this.arena.getChildren ( ).addAll ( this.walls );
		this.arena.getChildren ( ).addAll ( this.roundWalls );
		this.arena.getChildren ( ).addAll ( this.bounceWalls );
		this.arena.getChildren ( ).addAll ( this.badHoles );
		this.arena.getChildren ( ).addAll ( this.coins );

		light.getScope().add( arena );
		lightOn = true;

		this.rootSubSceneMain.getChildren ( ).add ( this.arena );
		this.rootSelectScene2D.getChildren ( ).add ( this.startButton);
		this.rootSelectScene2D.getChildren ( ).add ( this.rightChoice);
		this.rootSelectScene2D.getChildren ( ).add ( this.leftChoice);

		this.rootSelectScene2D.getTransforms().add(
			new Translate( 0, WINDOW_HEIGHT / 2 * 3 / 4)
		);

		root.getChildren().add( subSceneMain );
		root.getChildren().add( selectScene2D );

		Image image = new Image(Main.class.getClassLoader().getResourceAsStream("background.jpg"));
		scene.setFill( new ImagePattern(image));

		startButton.addEventHandler( MouseEvent.ANY, this::handleStartButton);
		rightChoice.addEventHandler( MouseEvent.ANY, event -> this.handleChangeLevelButton( event, 1 ));
		leftChoice.addEventHandler( MouseEvent.ANY, event -> this.handleChangeLevelButton( event, NUM_OF_LEVELS - 1 ));

		scene.addEventHandler ( MouseEvent.ANY, this::handleMouseEvent);
		scene.addEventHandler ( ScrollEvent.ANY, this::handleScrollEvent);

		stage.setTitle ( "Rolling Ball" );
		stage.setScene ( scene );
		stage.show ( );
	}

	private void changeArena (int change) {
		levelChosen = (levelChosen + change) % NUM_OF_LEVELS;
		//System.out.println( "level chosen = " + levelChosen);

		if ( levelChosen == 0 ) {
			ballPosition.setX(- ( Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS ));
			ballPosition.setZ( Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS);
		} else if ( levelChosen == 1 ) {
			ballPosition.setZ( 0 );
		} else if ( levelChosen == 2 ) {
			ballPosition.setZ( 0 );
			ballPosition.setX( 0 );
		}

		arena.getChildren().removeAll( walls );
		arena.getChildren().removeAll( roundWalls );
		arena.getChildren().removeAll( bounceWalls );
		arena.getChildren().removeAll( badHoles );
		arena.getChildren().removeAll( coins );
		arena.getChildren().remove( hole );

		this.walls = new ArrayList<>();
		this.roundWalls = new ArrayList<>();
		this.bounceWalls = new ArrayList<>();
		this.coins = new ArrayList<>();
		this.badHoles = new ArrayList<>();

		MapPrepare.addWalls( WALL_WIDTH, WALL_HEIGHT, WALL_DEPTH, PODIUM_WIDTH, PODIUM_DEPTH, walls, levelChosen);
		MapPrepare.addRoundWalls( BALL_RADIUS, WALL_HEIGHT * 2, PODIUM_WIDTH, PODIUM_DEPTH, roundWalls, levelChosen);
		MapPrepare.addCoins( BALL_RADIUS, PODIUM_WIDTH, PODIUM_DEPTH, PODIUM_HEIGHT, coins, levelChosen);
		MapPrepare.addBounceWalls( BALL_RADIUS * 3, WALL_HEIGHT, PODIUM_WIDTH, PODIUM_DEPTH, bounceWalls, levelChosen);
		MapPrepare.addBadHoles( PODIUM_WIDTH, PODIUM_DEPTH, HOLE_HEIGHT, HOLE_RADIUS, badHoles, levelChosen);
		this.hole = MapPrepare.addHole( PODIUM_WIDTH, PODIUM_DEPTH, HOLE_HEIGHT, HOLE_RADIUS, levelChosen);

		arena.getChildren().addAll( walls );
		arena.getChildren().addAll( roundWalls );
		arena.getChildren().addAll( bounceWalls );
		arena.getChildren().addAll( badHoles );
		arena.getChildren().addAll( coins );
		arena.getChildren().add( hole );

		cameraRotateY.setAngle(0);
		cameraRotateX.setAngle(0);
		cameraDistance.setZ(CAMERA_Z);
		cameraDistance2D.setZ(CAMERA_Z / 2);
	}

	private void addBallSelect () {
		Circle ball0 = new Circle( BALL_RADIUS / 2, Color.RED );
		ball0.getTransforms().add(
				new Translate( WINDOW_WIDTH / 4, BALL_RADIUS - WINDOW_HEIGHT / 2 * 3 / 4)
		);
		Circle ball1 = new Circle( BALL_RADIUS / 2, Color.LIGHTGREEN );
		ball1.getTransforms().add(
				new Translate( WINDOW_WIDTH / 2, BALL_RADIUS - WINDOW_HEIGHT / 2 * 3 / 4)
		);
		Circle ball2 = new Circle( BALL_RADIUS / 2, Color.ALICEBLUE );
		ball2.getTransforms().add(
				new Translate( WINDOW_WIDTH * 3 / 4, BALL_RADIUS - WINDOW_HEIGHT / 2 * 3 / 4)
		);
		Circle ballPick = new Circle( BALL_RADIUS / 2 + 6, Color.YELLOW );
		this.ballPickPosition = new Translate( WINDOW_WIDTH / 4, BALL_RADIUS - WINDOW_HEIGHT / 2 * 3 / 4);
		ballPick.getTransforms().add( ballPickPosition );

		ball0.setStroke( Color.BLACK );
		ball1.setStroke( Color.BLACK );
		ball2.setStroke( Color.BLACK );

		ball0.addEventHandler( MouseEvent.ANY, event -> this.handleBalLPick( event, 0));
		ball1.addEventHandler( MouseEvent.ANY, event -> this.handleBalLPick( event, 1));
		ball2.addEventHandler( MouseEvent.ANY, event -> this.handleBalLPick( event, 2));

		rootSelectScene2D.getChildren().add( ballPick );
		rootSelectScene2D.getChildren().add( ball0 );
		rootSelectScene2D.getChildren().add( ball1 );
		rootSelectScene2D.getChildren().add( ball2 );
	}

	private void handleBalLPick( MouseEvent event, int chosen ) {
		if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
			this.ballChosen = chosen;
			ballPickPosition.setX( WINDOW_WIDTH * (chosen + 1) / 4 );

			this.arena.getChildren().remove( this.ball );

			this.ball = MapPrepare.addBall ( Main.BALL_RADIUS, ballPosition, chosen);

			this.arena.getChildren().add( ball );
		}
	}

	private void startGame () {
		root.getChildren().remove( selectScene2D );

		root.getChildren().add( subScene2D );

		this.addTimer();

		scene.addEventHandler ( KeyEvent.ANY, event -> this.arena.handleKeyEvent ( event, Main.MAX_ANGLE_OFFSET ) );
		scene.addEventHandler ( KeyEvent.ANY, this::handleKeyEvent);
	}

	private void addTimer () {
		this.timer = new Timer (
				deltaSeconds -> {
					this.arena.update( ARENA_DAMP );
					updateVectorLine();
					updateTimerBar( deltaSeconds );

					if ( Main.this.ball != null && timeRemaining > 0) {

						boolean wallHit = false;
						Point3D ballSpeed = this.ball.getSpeed();
						if ( wallHitCounter <= 0) {
							for (Wall w : this.walls) {
								if ( w.handleCollision(this.ball) ) {
									wallHitCounter = 10;
									break;
								}
							}
						} else wallHitCounter--;
						//provera da li se desio sudar sa zidom da ne bi loptica prolazila kroz zidove na obodu terena
						if (	ballSpeed.getX() != ball.getSpeed().getX() ||
								ballSpeed.getY() != ball.getSpeed().getY() ||
								ballSpeed.getZ() != ball.getSpeed().getZ()
						) {
							wallHit = true;
						}

						for (Coin c: this.coins) {
							if (c.handleCollision( this.ball )) {
								this.arena.getChildren().remove(c);
								coins.remove(c);
								points += POINTS_COIN;
								pointsText.setText( String.valueOf(points) );
								break;
							}
						}
						if ( roundWallCnt <= 0) {
							for (RoundWall r : this.roundWalls) {
								if (r.handleCollision(this.ball)) {
									roundWallCnt = 20;
									break;
								}
							}
						} else roundWallCnt--;

						for (BounceWall b: this.bounceWalls) {
							if (b.handleCollision(this.ball))
								break;
						}

						boolean isInBadHole = false;

						for (BadHole h: this.badHoles) {
							if (h.handleCollision(this.ball)) {
								isInBadHole = true;
								break;
							}
						}

						boolean outOfArena = Main.this.ball.update (
								deltaSeconds,
								Main.PODIUM_DEPTH / 2,
								-Main.PODIUM_DEPTH / 2,
								-Main.PODIUM_WIDTH / 2,
								Main.PODIUM_WIDTH / 2,
								this.arena.getXAngle ( ),
								this.arena.getZAngle ( ),
								Main.MAX_ANGLE_OFFSET,
								Main.MAX_ACCELERATION * ( 1 + ballChosen * 1. / 2 ),
								Main.BALL_DAMP
						);

						boolean isInHole = this.hole.handleCollision ( this.ball );

						if ( (outOfArena || isInHole || isInBadHole) && !wallHit ) {
							handleBallDrop( isInHole, isInBadHole);
						}
					}
				}
		);
		timer.start ( );
	}

	private void addTimerScale(Scale scale, Group root) {
		timerBar = new Rectangle(10, Main.WINDOW_HEIGHT, Color.GRAY);
		root.getChildren().add(timerBar);

		timerBar.getTransforms().addAll(
				new Translate( Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT ),
				new Rotate(180),
				scale
		);

		timerText = new Text(15, 15, "Time remaining: " + (int) timeRemaining);
		timerText.setFont(new Font(20));
		timerText.setFill( Color.GRAY );
		root.getChildren().add( timerText );
		timerText.getTransforms().add(
				new Translate( Main.WINDOW_WIDTH - Main.WINDOW_WIDTH / 4,
						Main.WINDOW_HEIGHT - 30)
		);
	}

	private void updateTimerBar(double deltaSeconds) {
		if (timeRemaining > 0){
			timeRemaining -= deltaSeconds;
			timerScale.setY(timeRemaining / TIME_LIMIT);
			timerText.setText( "Time remaining: " + (int) (timeRemaining + 1));
		} else {
			Text textEnd = new Text( WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, "Kraj igre!");
			textEnd.setFont( new Font(50) );
			Bounds textBounds = textEnd.getBoundsInParent();
			textEnd.getTransforms().add(
					new Translate( (textBounds.getMinX() - textBounds.getMaxX()) / 2, 0 )
			);
			textEnd.setFill( Color.RED );
			rootSubScene2D.getChildren().add( textEnd );
		}
	}

	private void updateVectorLine () {
		this.vectorLine.setEndY( (arena.getXAngle() / MAX_ANGLE_OFFSET ) * VECTOR_LINE_LENGTH );
		this.vectorLine.setEndX( (arena.getZAngle() / MAX_ANGLE_OFFSET ) * VECTOR_LINE_LENGTH );
	}

	private void handleBallDrop( boolean isInHole, boolean isInBadHole ) {
		this.rootSubScene2D.getChildren().remove(lifepoints [--remainingLifepoints]);
		lifepoints [remainingLifepoints] = null;
		this.arena.getChildren ( ).remove ( this.ball );
		Main.this.ball = null;
		if ( isInHole ) {
			points += POINTS_HOLE;
			pointsText.setText( String.valueOf(points) );
		} else if ( isInBadHole ) {
			points -= POINTS_HOLE;
			pointsText.setText( String.valueOf(points) );
		}
		if ( remainingLifepoints > 0 ) {
			this.ballPosition.setX( - ( Main.PODIUM_WIDTH / 2 - 2 * Main.BALL_RADIUS ) );
			if ( levelChosen == 0 ) this.ballPosition.setZ( Main.PODIUM_DEPTH / 2 - 2 * Main.BALL_RADIUS );
			else if ( levelChosen == 1 ) ballPosition.setZ( 0 );
			else if ( levelChosen == 2 ) {
				ballPosition.setZ(0);
				ballPosition.setX(0);
			}
			this.ball = MapPrepare.addBall ( Main.BALL_RADIUS, ballPosition, ballChosen);
			this.arena.getChildren().add( this.ball );
			this.arena.resetRotate();
		} else {
			Text textEnd = new Text( WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, "Kraj igre!");
			textEnd.setFont( new Font(50) );
			Bounds textBounds = textEnd.getBoundsInParent();
			textEnd.getTransforms().add(
					new Translate( (textBounds.getMinX() - textBounds.getMaxX()) / 2, 0 )
			);
			textEnd.setFill( Color.RED );
			rootSubScene2D.getChildren().add( textEnd );
		}
	}

	private void handleKeyEvent ( KeyEvent event ) {
		if ( event.getEventType().equals( KeyEvent.KEY_PRESSED)){
			if (event.getCode().equals(KeyCode.DIGIT1) || event.getCode().equals(KeyCode.NUMPAD1)) {
				this.subSceneMain.setCamera( this.defaultCamera );
				cameraActive = 1;
			} else if (event.getCode().equals(KeyCode.DIGIT2) || event.getCode().equals(KeyCode.NUMPAD2)) {
				this.subSceneMain.setCamera( this.birdViewCamera );
				cameraActive = 2;
			} else if (event.getCode().equals(KeyCode.SPACE)) {
				if (cameraActive == 1){
					cameraRotateY.setAngle(0);
					cameraRotateX.setAngle(0);
					cameraDistance.setZ(CAMERA_Z);
					cameraDistance2D.setZ(CAMERA_Z / 2);
				}
			} else if (event.getCode().equals(KeyCode.DIGIT0) || event.getCode().equals(KeyCode.NUMPAD0)) {
				if ( lightOn ) {
					this.arena.getChildren ( ).remove ( this.light );
				} else {
					this.arena.getChildren ( ).add ( this.light );
				}
				this.lamp.changeLight( lightOn );
				lightOn = !lightOn;
			}
		}
	}

	private void handleScrollEvent (ScrollEvent event){
		if (cameraActive == 1) {
			if (event.getDeltaY() < 0) {
				cameraDistance.setZ(cameraDistance.getZ() - 20);
				cameraDistance2D.setZ(cameraDistance2D.getZ() - 20);
			} else {
				cameraDistance.setZ(cameraDistance.getZ() + 20);
				cameraDistance2D.setZ(cameraDistance2D.getZ() + 20);
			}
		}
	}

	private void handleStartButton (MouseEvent event) {
		if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
				startGame();
		}
	}

	private void handleChangeLevelButton (MouseEvent event, int change) {
		if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
				changeArena( change );
		}
	}

	private void handleMouseEvent (MouseEvent event){
		if (cameraActive == 1){
			if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)){
				if (event.isPrimaryButtonDown()){
					if( previousX == 0 ){
						previousX = event.getSceneX();
					}
					if( previousY == 0 ){
						previousY = event.getSceneX();
					}

					double x = event.getSceneX();
					double dx = x - previousX;
					previousX = x;

					double y = event.getSceneY();
					double dy = y - previousY;
					previousY = y;

					int signX = dx >= 0 ? 1 : -1;
					int signY = dy >= 0 ? -1 : 1;

					cameraRotateY.setAngle( cameraRotateY.getAngle() + signX);
					cameraRotateX.setAngle( Utilities.clamp(cameraRotateX.getAngle() + signY * 1. / 2, - 45, 45));
				}
			}
		}
	}

	public static void main ( String[] args ) {
		launch ( );
	}
}