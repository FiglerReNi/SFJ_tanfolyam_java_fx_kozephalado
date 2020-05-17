package bemutato;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
      StackPane panel = new StackPane();
      panel.getChildren().add(new Button("Első Button"));
      Scene scene1 = new Scene(panel, 300, 50);
      primaryStage.setScene(scene1);
      primaryStage.setTitle("Első Stage");
      primaryStage.show();
      
      /*Itt nem hoztunk létre stackpane-t, amire az első esetben pakoltuk a gombot.
        Így a gomb teljesen ki fogja tölteni a Scene területét, és ha méretezem az ablakot a gomb
        mérete is változik*/
      Stage stage2 = new Stage();
      stage2.setScene(new Scene(new Button("Második Button"), 300, 100));
      stage2.setTitle("Második Stage");
      stage2.show();
      
      /*Bind: (bindelés): Szeretnénk ha a kör az átméretezéssel nem tűnne el pl a képernyőről, ha túl
        kicsire méretezzük, ahhoz, hogy dinamikusabb legyen hozzá kell kötni valamihez*/
      Stage stage3 = new Stage();
      Pane pane3 = new Pane();
      Circle circle1 = new Circle();
      //kör sugara
      circle1.setRadius(50);
      //kör középponta
      //circle1.setCenterY(70);
      //circle1.setCenterX(70);
      //A kör középpontját a pain szélességétől teszem függővé -> veszem a pain teljes szélességét
      //és osztom kettővel -> ebben az esetben középen lesz
      circle1.centerXProperty().bind(pane3.widthProperty().divide(2));
      circle1.centerYProperty().bind(pane3.heightProperty().divide(2));
      circle1.setFill(Color.WHITE);     
      pane3.getChildren().add(circle1);
      stage3.setScene(new Scene(pane3, 300, 300));
      stage3.setTitle("Harmadik Stage");
      stage3.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
