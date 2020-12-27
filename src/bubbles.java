import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class bubbles {
    private int w = 350;
    private int h = 480;
    private Color colorBubbles = Color.rgb(0, 0, 255, 0.4);
    private Color colorBackground = Color.rgb(0,9,145);
    private int xEnsemble;
    private int yInitiale = 480;
    private ArrayList<ArrayList<Double>> bubbleGroup = new ArrayList<>();
    private ArrayList<Double> b1 = new ArrayList<>();
    private ArrayList<Double> b2 = new ArrayList<>();
    private ArrayList<Double> b3 = new ArrayList<>();
    private ArrayList<Double> b4 = new ArrayList<>();
    private ArrayList<Double> b5 = new ArrayList<>();

    /**
     * constructeur de la classe
     */

    public bubbles(){ 
        Random aleatoire = new Random();

        xEnsemble = aleatoire.nextInt(350); // on defini un X pour l'ensemble des bulles
        // On defini une taille pour chaque bulle
        b1.add((double) (aleatoire.nextInt(41-10)+10)); // taille de la premiere bulle
        b2.add((double) (aleatoire.nextInt(41-10)+10)); // taille de la deuxieme bulle
        b3.add((double) (aleatoire.nextInt(41-10)+10)); // taille de la troisieme bulle
        b4.add((double) (aleatoire.nextInt(41-10)+10)); // taille de la quatrieme bulle
        b5.add((double) (aleatoire.nextInt(41-10)+10)); // taille de la cinquieme bulle

        // On defini une vitesse pour chaque bulle

        b1.add((double) (aleatoire.nextInt(450-350)+350));// vitesse de la premiere bulle
        b2.add((double) (aleatoire.nextInt(450-350)+350));// vitesse de la deuxieme bulle
        b3.add((double) (aleatoire.nextInt(450-350)+350));// vitesse de la troisieme bulle
        b4.add((double) (aleatoire.nextInt(450-350)+350));// vitesse de la quatrieme bulle
        b5.add((double) (aleatoire.nextInt(450-350)+350));// vitesse de la cinquieme bulle

        // On defini un Coordonne x par rapport a xEnsemble pour chaque bulle

        b1.add((double)Math.abs(xEnsemble-20)+20);
        b2.add((double)Math.abs(xEnsemble-20)+20);
        b3.add((double)Math.abs(xEnsemble-20)+20);
        b4.add((double)Math.abs(xEnsemble-20)+20);
        b5.add((double)Math.abs(xEnsemble-20)+20);


        // On defini un Coordonne y origine a l'exterieur de l'ecran

        b1.add(500.0);
        b2.add(500.0);
        b3.add(500.0);
        b4.add(500.0);
        b5.add(500.0);

        // On met les arrayList dans un seul groupe

        bubbleGroup.add(b1);
        bubbleGroup.add(b2);
        bubbleGroup.add(b3);
        bubbleGroup.add(b4);
        bubbleGroup.add(b5);
    }

    public ArrayList<ArrayList<Double>> getGroup() {

        return this.bubbleGroup;
    }
    /**
     * Fonction pour mettre a jour la coordonnee Y
     * */
    public boolean actualize(double dt){

        double bSpeed; // vitesse de la bulle
        double bPosY = 0; // position courant de la bulle en Y
        double nextY; // prochaine X


        for(int i = 0; i<bubbleGroup.size(); i++){

            bPosY = (double) bubbleGroup.get(i).get(3); // position courante en Y
            
            bSpeed =  bubbleGroup.get(i).get(1); // Vitesse courante

            double decreaceY =  ((bSpeed * dt)/3);
            //System.out.println(nextY);
            bPosY -= decreaceY;

            bubbleGroup.get(i).set(3,bPosY);
        }
        if(bPosY < -10.0){
            return true;
        }
        else{
            return false;
        }



    }
    /**
     * Dessiner l'ensemble des bulles
     * */
    public void draw(GraphicsContext fondo) {
        // double xAffiche = x - windowUp
        fondo.setFill(colorBackground);
        fondo.fillRect(0,0, w, h);
        fondo.setFill(colorBubbles);

        for (int i = 0; i < bubbleGroup.size(); i++) {
            double size = bubbleGroup.get(i).get(0);// taille
            double yPos = bubbleGroup.get(i).get(3); // position en y
            double xPos = bubbleGroup.get(i).get(2); // position en X
            fondo.fillOval(xPos, yPos, size, size);

        }

    }

    public int getyInitiale() {
        return this.yInitiale;
    }
}
