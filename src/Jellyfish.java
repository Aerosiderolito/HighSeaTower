import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Jellyfish {

    private Image[] framesR;
    private Image[] framesL;
    private Image image;
    private double  fps = 8;	// Taux de rafraichissement de la jellyfish
    private double temps = 0;
	ImageView jelly = new ImageView();

    public Jellyfish(){

    	// Frames Right
    framesR = new Image[]{
        new Image("images/jellyfish1.png"),
        new Image("images/jellyfish2.png"),
        new Image("images/jellyfish3.png"),
        new Image("images/jellyfish4.png"),
        new Image("images/jellyfish5.png"),
        new Image("images/jellyfish6.png")
    };
    	// Frames Left
    framesL = new Image[]{
        new Image("images/jellyfish1g.png"),
        new Image("images/jellyfish2g.png"),
        new Image("images/jellyfish3g.png"),
        new Image("images/jellyfish4g.png"),
        new Image("images/jellyfish5g.png"),
        new Image("images/jellyfish6g.png")

    };

    image = framesR[0];

    }
    
    // Methode qui actualise l'image dépendament du framerate et du dernier mouvement de la méduse.
    public void actualize(double dt, boolean left, boolean right){

        temps += dt;
        int frame = (int) (temps * fps);
        if(left) {
            image = framesL[frame % 5];
        }
        else{
              image = framesR[frame % 5];
        }        
        jelly.setImage(image);
    }

    // Création d'une meduse;
    public Node createJelly() {
    	jelly.setImage(image);
    	jelly.setFitWidth(50);
    	jelly.setFitHeight(50);
    	jelly.setTranslateX(100);
    	jelly.setTranslateY(349);
    	return jelly;
    }
}

