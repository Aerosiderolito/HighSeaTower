import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;
/**
 * Cette classe s'ocupe de l'affichage aleatoire des bulles
 * */

public class platforms {

    /**
     * Ce methode s'ocupe de la creation des rectangles
     * */
    public Node makePlatform(int y) {

        // couleurs des plateformes

        Color red, yellow, orange, green, couleurChoisi;
        red =  Color.rgb(184, 15, 36);
        yellow = Color.rgb(230, 221, 58);
        green =  Color.LIGHTGREEN;
        orange = Color.rgb(230, 134, 58);

        double randomTest;                               // probabilite aleatoire

            // decidons le type de platteforme;
            Random aleatoire = new Random();

            //Taille aleatoire entre 175 et 80px
            int largeur = (int) (175*Math.random());
            while(largeur<80){
                largeur = (int) (175*Math.random());
            }

            // position aleatoire dans l'ecran sans depaser la largeur
            int x = aleatoire.nextInt(350-largeur);

            // algorithme de l'enonce
            String id;
            randomTest = Math.random();
            if(randomTest>0.05){

                if(randomTest>0.1){
                    if(randomTest>0.2){
                        // choisisons une plateforme orange
                        couleurChoisi = orange;
                        id = "orange";
                    }
                    else{
                        // choisisons une plateforme verte
                        couleurChoisi = green;
                        id = "green";
                    }
                }
                else{
                    // choisisons une plateforme jaune
                    couleurChoisi = yellow;
                    id = "yellow";
                }
            }
            else{
                // choisisons une plateforme rouge
                couleurChoisi = red;
                id = "red";
            }

            // Creation de la plateforme avec ses proprietes
            Rectangle entity = new Rectangle(largeur, 10);
            entity.setTranslateX(x);
            entity.setTranslateY(y);
            entity.setFill(couleurChoisi);
            entity.setId(id);
            return entity;

    }




}