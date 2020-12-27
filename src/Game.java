import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Classe permetant de derouler le jeu
 * */

public class Game extends HighSeaTower {

    // parametres pour teste si la jelly va a droite ou a gauche
    boolean left = true;
    boolean right = true;
    Point2D playerVelocity = new Point2D(0, 0);

    // pour verifier si le jouer peut sauter
    private boolean canJump = true;
    private ArrayList<Integer> platParams = new ArrayList<Integer>();   // ArrayList contenant des parametres des plateformes
	Jellyfish meduz = new Jellyfish();                              // creation d'un nouvel personnage

    // ArrayList qui contient les plateformes en forme de nodes
    protected ArrayList<Node> platArray = new ArrayList<>();

    // ArrayList qui contient des bulles
    private ArrayList<bubbles> bulles = new ArrayList<>();
    
    // boolean qui test si on touche le sol
	private boolean touchFloor;                                

    /**
     *Constructeur du jeu
     * */
	
    public Game(){

        initContent();                      // Appel a la methode pour initialiser le contenu

        for(int i = 0; i<2; i++){           // creation des bulles
            bulles.add(new bubbles());
        } 
    }
    
    /**
     * Permet de mettre a jour le jeu
     * */
    public void actualize(double dt){

        // Mettre a jour les bulles

        boolean nuevo = false;

        for(bubbles b: bulles){
            nuevo = b.actualize(dt);
        }
        if (nuevo == true){

            for(int i = 0; i<2; i++){
                bulles.remove(0);
            }
            for(int i = 0; i<2; i++){
                bulles.add(new bubbles());
            }
        }
        playerCoordinateY = player.getTranslateY()+climbedPixels;
        meduz.actualize(dt, left, right);

    }
    /**
     * Permet de dessiner dans le jeu
     * */
    public void draw(GraphicsContext fondo, boolean deb){

        // on dessine chaque bulle

        for(int i =0; i<bulles.size(); i++){

            bulles.get(i).draw(fondo); // draw all the group

        }
        // Affichage des parametres si debug est vrai
        if (deb) {
            fondo.setFill(Color.WHITE);
            fondo.setFont(Font.font("Comic Sans MS", 12));
            fondo.fillText("Position = (" + (int) player.getTranslateX() + ", " +  player.getTranslateY() + ")", 5, 15);
            fondo.fillText("v = ("+"0 "+","+ 600+ ")", 5, 30); 		// On ne respecte pas la gravité, sorry.
            fondo.fillText("a = ( "+"0 "+","+ 1200+ ")", 5, 45);	// Einstein disliked this comment ↑↑↑↑↑.
            fondo.fillText("Touche le sol: " + touchFloor , 5, 60);		// Svp pas trop de pénalité hahah.			(please)
            fondo.setFill(Color.rgb(221, 69, 69,0.3));
            fondo.fillRect((double) player.getTranslateX(), player.getTranslateY()-climbedPixels,50,50);
        }
        
        fondo.setFill(Color.WHITE);
        fondo.setFont(Font.font("Comic Sans MS", 20));

        // permet de calculer un score (qui se verra dans la console)

        if (pScore != (int)Math.abs(player.getTranslateY()-399)) {
        	touchFloor = false;
        } else {
        	touchFloor = true;
        }
        // metres parcourus
        fondo.fillText(((int)Math.abs(player.getTranslateY()-399)+"m"), 150, 60);
    	pScore = (int)Math.abs(player.getTranslateY()-399);

    }
    /**
     * Permet d'initialiser le contenu dans le jeu
     * */
    public void initContent() {								
    	player = meduz.createJelly(); 
    	gameRoot.getChildren().add(meduz.createJelly());
    	platGen();
        }   
    
    /**
     * Permet de creer des nouvelles plateformes
     * */
    public void platGen() {
        for(int i = 0; i < 5; i++) {
            platforms newPlat = new platforms();
            int platPos = 400-100*i;
            platParams.add(platPos);
            platArray.add(newPlat.makePlatform(platPos));
            gameRoot.getChildren().add(platArray.get(i));
        }
    }

    /**
     * Permet d'obtenir des parametres sur les plateformes
     * */

    public ArrayList<Integer> getPlat(){
        return platParams;
    }

    /**
     * Permet de modifier les plateformes
     * */
    public void modifyPlatArray(int a){

        platforms newPlat = new platforms();
        platArray.add(newPlat.makePlatform(a));
        platArray.remove(newPlat.makePlatform(a-1));
        gameRoot.getChildren().add(platArray.get(platArray.size()-1));

    }
    
    /**
     * Importante pour deplacer le personage en X
     * */
    void movePlayerX(int value) {
        boolean movingRight = value > 0;
        
        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platArray) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.getTranslateX() + 40 == platform.getTranslateX()) {
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateX() == platform.getTranslateX() + 40) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }
    /**
     * Importante pour deplacer le personage en Y & gérer les collisions sur l'axe 
     * */
    void movePlayerY(int value) {
        boolean movingDown = value > 0; 

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platArray) {
         /**
          * Check le ID des plateformes pour decider quoi faire
          * */
            	if (platform.getId() == "yellow") {
            		if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                        if (movingDown) {
                            if (player.getTranslateY() +50 == platform.getTranslateY()){
                            	player.setTranslateY(player.getTranslateY()-1);
                                canJump = true;
                                return;
                            }
                        }
            		}
            	}
            		
            		
            	if (platform.getId() == "green" ) {
            		if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                        if (movingDown) {
                        	canJump = true;
                            if (player.getTranslateY() +50 == platform.getTranslateY()){
                            	jumpPlayer();
                                return;
                            }
                        }
                    }
            	}
            	
            	if (platform.getId() == "red"   ) {
            		if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                        if (movingDown) {
                        	if (player.getTranslateY() +50 == platform.getTranslateY()){
                            	player.setTranslateY(player.getTranslateY()-1);
                                canJump = true;
                                return;
                            }
                        }
                        else {
                            if (player.getTranslateY() == platform.getTranslateY()) {
                                return;
                            }
                        }
                    }
            	}
            	
            	if (platform.getId() == "orange") {
            		if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                        if (movingDown) {
                            if (player.getTranslateY() +50 == platform.getTranslateY()){
                            	player.setTranslateY(player.getTranslateY()-1);
                                canJump = true;
                                return;
                            }
                        }
                    }
            	}
                
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
    
/**
 * Saut de la jelly
 * */
    void jumpPlayer() {
        if (canJump) {
          	Music("boing.wav");
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }   
}
