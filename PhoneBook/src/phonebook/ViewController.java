package phonebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ViewController implements Initializable {
   
    @FXML
    TableView table;
    @FXML
    TextField inputLastname;
    @FXML
    TextField inputFirstname;
    @FXML
    TextField inputEmail;
    @FXML
    Button addNewContactButton;
    @FXML
    StackPane menuPane;
    @FXML
    Pane contactPane;
    @FXML
    Pane exportPane;

    /*
    1.lépés - A kontaktoknak létrehozunk egy POJO-t, ez az adatbázis egy sorából egy objetumot csinál
    2.lépés - ObservableList létrehozása: ezzel tudunk majd a tableview-ra kiiratni, objektumokat tárol,
              amilyen objektumot teszünk bele, annak az osztályváltozói jelennek majd meg az oszlopokban.
              Kommunikál az adatbázissal is, amit itt létrehozunk új adatot, az bekerül az adatbázisba is és a tableview-ra is*/
    
    private final ObservableList<Person> data = 
            FXCollections.observableArrayList(
            new Person("Figler", "Renáta", "freni@teszt.hu"),
            new Person("Figler", "Anikó", "fani@teszt.hu"),
            new Person("Teszt", "Elek", "teszte@teszt.hu"));
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*a szerkesztőben is létrehozhatnánk oszlopokat, de így konfigurálhatóbb*/
        //létrehzunk egy oszlopot
        TableColumn lastNameCol = new TableColumn("Vezetéknév");
        //sose legyen kisebb mint 100
        lastNameCol.setMinWidth(100);
        //beállítjuk hozzá a cella típusát, sokféle cellát létre lehet hozni egy adott oszlophoz (pl textfield, label stb)
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //érték beállítás, mi jelenjen meg benne adatként--> beállítom az értéket, amiben egy Pojo-t adok át, 
        //  majd megmondom milyen típusú érték jön belőle (String itt)-> és átadom hogy pontosan mit keressen
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        
        TableColumn firstNameCol = new TableColumn("Keresztnév");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
        
        //összeötjük az oszlopainkat a tableView-val ->
        /*getColumns() -> érjük az összes oszlopát, ami jelenleg még nincs
          addAll -> hozzáadjuk azokat amiket szeretnénk
        */
        table.getColumns().addAll(lastNameCol, firstNameCol, emailCol);
        //Adatok hozzáadása -> observableList kell legyen
        table.setItems(data);
        
    }    
    
}
