import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/***
 * Auteurs: Simo Hakim et Cesar Rodriguez
 *	20096040 /  20117371
 * Programme principal permetant l'execution du jeu
 * */

public class HighSeaTower extends Application {

    // Parametres du jeu
    public int w = 350;
    public int h = 480;
    public double temps;

    // Parametres de l'ecran
    protected Pane racine = new Pane();
    public static Pane gameRoot = new Pane();
    public Node player;

    // Parametres de temps
    private double deltaTime;
    private double startTime;
    private static double vitesseInitiale = 50*1e-9;
    private static double vitesseFinale = vitesseInitiale;
    private static double acceleration = 2;
    public static int multiplicator = 1;
    public static  HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private boolean running = true;
    boolean test = false;
    protected static double climbedPixels;
    double playerCoordinateY;
    private boolean debug = false;
    Game game;
    double pScore;

/**
 * Methode utilis�e pour la lecture des touches du clavier
 * */
        
    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
/**
 * Recommence un nouveau jeu en remettant les param�tres a z�ro et commencant une nouvelle instance de Game
 * **/

    private void restart() {

    	vitesseInitiale = 50*1e-9;
		vitesseFinale = vitesseInitiale;
		playerCoordinateY = 0;
		deltaTime = 0;
		startTime = 0;
		debug = false;
		racine.getChildren().remove(gameRoot);
		gameRoot = new Pane();
		racine.getChildren().add(gameRoot);
		game = new Game();
		Music("gameover.wav");
		gameRoot.setLayoutY(0);
		test = false;
    };
    
    public void Music(String path){

        try{
            AudioInputStream audioInputStream =AudioSystem.getAudioInputStream(this.getClass().getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start( );
        }
        catch(Exception ex)
        {
            System.out.println(".wav file not found"); // Affiche un message de fichier non trouve
        }

    }
    @Override
    public void start(Stage stage) throws Exception {

        // parametres de la fenetre
        stage.setTitle("High Sea Tower");
        stage.setWidth(350);
        stage.setHeight(480);
        stage.setResizable(false);

        Scene scene = new Scene(racine);
        
        /**
         * Event T -> Debug
         * Event Escape -> Jeu arret�
         * Anything else -> Mouvements
         **/
        
        scene.setOnKeyPressed(event -> {
        	if(event.getCode() == KeyCode.T) {
        		debug = !debug;
        		test = !test;
        		System.out.println("debug enabled :  " + debug);

        		} else if (event.getCode() == KeyCode.ESCAPE) {
            		stage.close();
            		System.out.println("Thanks for playing :) ");
        		} else {
        			keys.put(event.getCode(), true);
        			}

        });
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        // Initialisation d'un nouveau jeu et des Panes correspondants
        game = new Game();
        Canvas lienzo = new Canvas(350, 480);
        GraphicsContext fondo = lienzo.getGraphicsContext2D();
        racine.getChildren().add(lienzo);
        racine.getChildren().add(gameRoot);

        
        // Notre timer d'animation qui gere le defilement du Pane gameRoot contenant les plateformes et la jellyfish
        AnimationTimer timer = new AnimationTimer() {
            private long tFinal = 0;

            @Override
            public void handle(long l) { 

                // Permet de fixer le temps courant
                if (tFinal == 0){
                    tFinal = l;
                    temps = tFinal;  // @@@
                    return;
                }
                if(startTime == 0) {
                    startTime = l;
                    return;
                }
                deltaTime = l - startTime;
                
                vitesseFinale = multiplicator*(vitesseInitiale + acceleration*1e-9*deltaTime*1e-9);
                climbedPixels = (deltaTime*(vitesseFinale+vitesseInitiale))/2 ;
                gameRoot.setLayoutY(climbedPixels);


          
                /**
                 * Si le joueur tombe en sous de la dimension de la fenetre, on recommence un nouveau jeu
                 * **/
                
                if(game.playerCoordinateY > 450) {
                	System.out.println("New game !");
        			restart();
                }

                if(running) {
                    /**
                     * lit les touches du clavier pour determines les evenements dans le programme
                     * */
                    
              	  if (isPressed(KeyCode.UP) || isPressed(KeyCode.SPACE) || isPressed(KeyCode.Z) || isPressed(KeyCode.W)) {
                        game.jumpPlayer();
                    }
                    if (isPressed(KeyCode.LEFT) || isPressed(KeyCode.Q) || isPressed(KeyCode.A)) {
                        if(game.player.getTranslateX() > 0) {
                            game.movePlayerX(-5);
                            game.left = true;
                            game.right = false; 
                        } else if(game.player.getTranslateX() <= 0) {
                            game.movePlayerX(50);	// Rebond sur mur
                          	Music("boing.wav");

                            game.left = false;
                            game.right = true; 
                                                
                        }
                    }
                    
                    if (isPressed(KeyCode.RIGHT) || isPressed(KeyCode.D)) {
                        if(game.player.getTranslateX() < 300) {
                      	game.movePlayerX(5);
                          game.left = false;
                          game.right = true; 
                        } else if (game.player.getTranslateX() >= 300)  {
                      	game.movePlayerX(-50);	// Rebond sur mur      
                      	Music("boing.wav");
                          game.left = true;
                          game.right = false; 
                        }
                    }

                    if (game.playerVelocity.getY() < 10) {
                        game.playerVelocity = game.playerVelocity.add(0, 1);
                    }
                    game.movePlayerY((int)game.playerVelocity.getY()); 
                }
                
                /**
                 * Determine en temps reel si on devrait commencer a faire des nouvelles platformes
                 * */

                ArrayList<Integer> tabPlat = game.getPlat(); // Contient des parametres sur les plateformes // # dans game
                // si Y > a la derniere plateforme

                if(game.player.getTranslateY()< tabPlat.get(3)){

                    // on fait des nouvelles
                    for(int i = 0; i< 5; i++) {
                        int newH;
                        if(i == 0){

                            newH = tabPlat.get(4) - 100;

                        }
                        else {

                            newH = tabPlat.get(4) - (i += 100);

                        }
                        tabPlat.set(4, newH);

                        // On appelle une methode pour effacer et reconstruire des plateformes
                        game.modifyPlatArray(newH);

                    }
                }
                  
                  double dt = (l - tFinal)*1e-9;
                  temps =  dt;
                  // appel a diverses methodes
                  fondo.clearRect(0,0,w,h);
                  game.actualize(dt);
                  game.draw(fondo, debug);
                  tFinal = l; 
              }
              
          };
          
          stage.setScene(scene);
          stage.show();
          timer.start();
      }
           
}

