package bemutato;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Bemutato extends Application {

    @Override
    public void start(Stage primaryStage) {
      /*Létrehozhatunk több staget is*/
      /*Itt ha méretezem az ablaot a gomb ugyanakora marad*/
//      StackPane panel = new StackPane();
//      panel.getChildren().add(new Button("Első Button"));
//      Scene scene1 = new Scene(panel, 300, 50);
//      primaryStage.setScene(scene1);
//      primaryStage.setTitle("Első Stage");
//      primaryStage.show();
     
      /*Itt nem hoztunk létre stackpane-t, amire az első esetben pakoltuk a gombot.
        Így a gomb teljesen ki fogja tölteni a Scene területét, és ha méretezem az ablakot a gomb
        mérete is változik*/
//      Stage stage2 = new Stage();
//      stage2.setScene(new Scene(new Button("Második Button"), 300, 100));
//      stage2.setTitle("Második Stage");
//      stage2.show();    
      /*Bind: (bindelés): Szeretnénk ha a kör az átméretezéssel nem tűnne el pl a képernyőről, ha túl
        kicsire méretezzük, ahhoz, hogy dinamikusabb legyen hozzá kell kötni valamihez*/
//      Stage stage3 = new Stage();
//      Pane pane3 = new Pane();
//      Circle circle1 = new Circle();
      //kör sugara
//      circle1.setRadius(50);
      //kör középponta
      //circle1.setCenterY(70);
      //circle1.setCenterX(70);
      //A kör középpontját a pain szélességétől teszem függővé -> veszem a pain teljes szélességét
      //és osztom kettővel -> ebben az esetben középen lesz
//      circle1.centerXProperty().bind(pane3.widthProperty().divide(2));
//      circle1.centerYProperty().bind(pane3.heightProperty().divide(2));
      /*Előfordulhat, hogy több package is tartalmaz ugyanolyan nevű osztályt pl Color esetében is,
        a számunkra megfelelőt kell beimportálnunk, de előfordulhat, hogy két ugyanolyan nevű osztályt is beimportálunk
        és használunk, csak az útvonaluk más. Ilyenor kódban a teljes útvonalat kell használni.
        pl.:
      javafx.scene.paint.Color color = new javafx.scene.paint.Color();*/
//      Color color = new Color(0.7,0.1,0.1,1); //rgb színskála paraméterei szerint fogadja az érékeket (red green blue)
                                 //hexadecimális kódban van, 00 -> FF, ha mindegyik ugyanaz akor elég egy jegyűként megadni
                                 //pl FFFFFF ->fehér helyett FFF -> fehér; itt 0 és 1 között meg lehet adni a világosságát is
                                 //szinenként a kódok helyett, és hogy mennyire legyen látható átlátszó (oppacity vagy alfa érték)
//      Color color2 = color.brighter(); //világosít az eredeti szinen
      //circle1.setFill(color2); 
      //circle1.setFill(Color.WHITE); 
      /*Java fx-nek ccs szerű elemei vannak amivel tudjuk a design-t csinálni*/
      //CSS-hez hasonlóan is megadhatom a stílust: minden stílus elem elé kell a -fx-
//      circle1.setStyle("-fx-fill:green;-fx-stroke:red");
//      pane3.getChildren().add(circle1);
//      stage3.setScene(new Scene(pane3, 300, 300));
//      stage3.setTitle("Harmadik Stage");
//      stage3.show(); 
        /*Kép hozzáadása
        A képet mindg rá kell tenni egy imageView-ra*/
        StackPane pane = new StackPane();
        Image image = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQRAEEbZg02ARpB2uyLEdgMbHTF0VaD-NwpEGWEgIeauuceKXdn&usqp=CAU");
        ImageView imageView = new ImageView(image);
        //elemeket lehet forgatni is
        imageView.setRotate(10);
        pane.getChildren().add(imageView);
        //egy adott elemen belül hová tegyen egy másik elemet
        pane.setAlignment(imageView, Pos.TOP_LEFT);
        //méretezhetőség -> a ép szélességét kötöm az ablak szélességéhez
        imageView.fitWidthProperty().bind(pane.widthProperty());
       
        Scene scene2 = new Scene(pane, 600, 400);
        primaryStage.setScene(scene2);
        primaryStage.setTitle("Kép");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
