package phonebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ViewController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="FXML elemek">
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
    @FXML
    TextField inputExport;
    @FXML
    Button exportButton;
    @FXML
    AnchorPane anchor;  
    @FXML
    SplitPane mainSplit; 
    
    DB db = new DB();
//</editor-fold>

    private final String MENU_CONTACTS = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_EXPORT = "Export";
    private final String MENU_EXIT = "Kilépés";
    /*
    1.lépés - A kontaktoknak létrehozunk egy POJO-t, ez az adatbázis egy sorából egy objetumot csinál
    2.lépés - ObservableList létrehozása: ezzel tudunk majd a tableview-ra kiiratni, objektumokat tárol,
              amilyen objektumot teszünk bele, annak az osztályváltozói jelennek majd meg az oszlopokban.
              Kommunikál az adatbázissal is, amit itt létrehozunk új adatot, az bekerül az adatbázisba is és a tableview-ra is*/
    private final ObservableList<Person> data
            = FXCollections.observableArrayList(
                    //amíg nincs táblázatunk ezekkel az adatokkal dolgozunk
//                    new Person("Figler", "Renáta", "freni@teszt.hu"),
//                    new Person("Figler", "Anikó", "fani@teszt.hu"),
//                    new Person("Teszt", "Elek", "teszte@teszt.hu")
            );
    /*Új contact felvitele gombhoz esemény
      A viewban is meg kell adni az onActionnál*/
    @FXML
    private void addContact(ActionEvent event){
        String email = inputEmail.getText();
        //e-mail validálás
        if(email.length() > 3 && email.contains("@") && email.contains(".")){
            //a beírt adatokat elmentjük
        Person newPerson = new Person(inputLastname.getText(), inputFirstname.getText(), email);
        data.add(newPerson);
        db.addContact(newPerson);
        //ürtjuk a mezőket
        inputLastname.clear();
        inputFirstname.clear();
        inputEmail.clear();
        }else{
            alert("Adj meg egy valódi e-mail címet!");
        }   
    }
    
    @FXML
    private void exportList(ActionEvent event) {
        String fileName = inputExport.getText();
        //kiszedjük a whitespaceket
        fileName = fileName.replaceAll("\\s+", "");
        //írt e be a felhasználó file nevet
        if (fileName != null && !fileName.equals("")) {
            PdfGeneration pdfCreator = new PdfGeneration(fileName, data);
        }else{
            alert("Adj meg egy file nevet!");
        }
    }
    
    public void alert(String text){
        //letiltjuk a főablakot
        mainSplit.setDisable(true);
        //láthatóságot visszavesszük
        mainSplit.setOpacity(0.4);
        //popup létrehozása
        //kiírandó szöveg
        Label label = new Label(text);
        //ok gomb
        Button alertButton = new Button("OK");
        //maga az ablak (vertikális box)
        //egymás alá beteszi az átadott elemeket
        VBox vbox = new VBox(label, alertButton);
        //középre zárt
        vbox.setAlignment(Pos.CENTER);
        //gomb működése
        alertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //visszaállítjuk a láthatóságát a programnak
                mainSplit.setDisable(false);
                mainSplit.setOpacity(1);
                vbox.setVisible(false);
            }
        });
        //hozzákötjük a programunkhoz a vboxot -> az anchorhoz adjuk
        anchor.getChildren().add(vbox);
        //ha nem pozicionáljuk bal felül jelenik meg, ezért megmondjuk, hogy mennyire legyen a felső és bal széltól
        anchor.setTopAnchor(vbox, 300.0);
        anchor.setLeftAnchor(vbox, 250.0);
    }
    
    private void setTableData() {
        /*a szerkesztőben is létrehozhatnánk oszlopokat, de így konfigurálhatóbb*/
        //létrehzunk egy oszlopot
        TableColumn lastNameCol = new TableColumn("Vezetéknév");
        //sose legyen kisebb mint 100
        lastNameCol.setMinWidth(130);
        //beállítjuk hozzá a cella típusát, sokféle cellát létre lehet hozni egy adott oszlophoz (pl textfield, label stb)
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //érték beállítás, mi jelenjen meg benne adatként--> beállítom az értéket, amiben egy Pojo-t adok át, 
        //  majd megmondom milyen típusú érték jön belőle (String itt)-> és átadom hogy pontosan mit keressen
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));

        /*Le kell kezelnnk, hogy mi történjen, ha változás történik egy cellában --> hiszen ezek szerkeszthetőek.
          setOnCommit() -> amikor egy cellába belelépünk majd elhagyjuk dönthetünk, hogy mi történjen a változással
                elcommitoljuk vagy nem érdekel minket. Ha érdekel paraméterben kell megadni hogy mire figyeljen -->
                Ehhez kell egy eseménykezelő és meg kell adnunk mi történjen
                t -> az esemény neve, amit a handle megkap objektumként
                getTableView()->getItems() --> érjük az egész tábla összes adatát és ezen belül kérjük a változtatás pozicióját
                és ennek kérjük a sorát --> ezt kasztoljuk az elején Person objektummá
                majd beállítjuk az új értéket
            Az event olyan objektum ami tartalmazni fogja a régi és az új értéket is, de ezt csak a handle functionon belül 
            látjuk, utána már nem lesz elérhető, célszerú ha adatbázis oldalon gondoskodunk arról, hogy az előző adat meg-
            maradjon, ha fontos */
      lastNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                actualPerson.setLastName(t.getNewValue());
                db.updateContact(actualPerson);
            }
        }
        );

        TableColumn firstNameCol = new TableColumn("Keresztnév");
        firstNameCol.setMinWidth(130);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));

        firstNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                actualPerson.setFirstName(t.getNewValue());
                   db.updateContact(actualPerson);
            }
        }
        );

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(250);
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));

        emailCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Person, String> t) {
                Person actualPerson = (Person) t.getTableView().getItems().get(t.getTablePosition().getRow());
                actualPerson.setEmail(t.getNewValue());
                db.updateContact(actualPerson);
            }
        }
        );

        TableColumn removeCol = new TableColumn("Törlés");
        removeCol.setMinWidth(100);
        
        Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory =
                new Callback<TableColumn<Person, String>, TableCell<Person, String>>(){
        @Override
        public TableCell call(final TableColumn<Person, String> param){
            final TableCell<Person, String> cell = new TableCell<Person, String>(){
                final Button btn = new Button("Törlés");
                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                        setText(null);
                    }else{
                        btn.setOnAction((event) -> {
                            Person person = getTableView().getItems().get(getIndex());
                            data.remove(person);
                            db.removeContact(person);
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        }       
    };
    removeCol.setCellFactory(cellFactory);
        //összekötjük az oszlopainkat a tableView-val ->
        /*getColumns() -> kérjük az összes oszlopát, ami jelenleg még nincs
          addAll -> hozzáadjuk azokat amiket szeretnénk
         */
        table.getColumns().addAll(lastNameCol, firstNameCol, emailCol, removeCol);
        //adatbázisból kivesszük az adatokat
        data.addAll(db.getAllContacts());
        //Adatok hozzáadása -> observableList kell legyen
        table.setItems(data);
    }

    private void setMenuData() {
        /*
        TreeView: az elemeket fa szerkezetben jeleníti meg, csak úgy hozhatjuk létre ha át is adunk neki egy elemet
            ez lesz a fő, a belépő pont
        TreeItem: elemek a fa szerkezetben*/
        TreeItem<String> treeItemRoot1 = new TreeItem<>("Menü");
        TreeView<String> treeView = new TreeView<>(treeItemRoot1);
        //A menüt nem szeretnénk kiiratni --> tehát ezt elrejtjük
        treeView.setShowRoot(false);
        //Amit a menüben szeretnék
        TreeItem<String> nodeItemA = new TreeItem<>(MENU_CONTACTS);
        TreeItem<String> nodeItemB = new TreeItem<>(MENU_EXIT);
        //Így alapértelmezetten le lesz nyitva a menü
       // nodeItemA.setExpanded(true);
        //Képe az almenükhöz
        Node listaNode = new ImageView(new Image(getClass().getResourceAsStream("/nyil1.png")));
        Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/nyil2.png")));
        //Almenük a kontakton belül
        TreeItem<String> nodeItemA1 = new TreeItem<>(MENU_LIST, listaNode);
        TreeItem<String> nodeItemA2 = new TreeItem<>(MENU_EXPORT, exportNode);
        //Kontaktokra rá kell tenni ezt a kettőt
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        //Kérjük az alap itemet, amit már rátettünk az ágra, kérjük az összes gyerekét ha van és rátesszük az újakat
        treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);
        //Rátesszük a menuPane-re, hogy felületen is lássuk
        menuPane.getChildren().add(treeView);
        //Hozzáadunk a treeView hoz egy listener-t --> a Kontaktokra csak egyszer elljen kattintani, hogy lenyíljon
        //Azt figyeli, hogy melyik menüt választjuk ki
        treeView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener() {
            //fogadja a megfigyelt objektumot, a régi és az új értékét
            //amire rákattintottunk az lesz a selectedItem
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                String selectedMenu;
                selectedMenu = selectedItem.getValue(); //ez visszaadja a menü nevét amire attintva lett
                //Mi történjen kattintáskor
                if (null != selectedMenu) {
                    switch (selectedMenu) {
                        case MENU_CONTACTS:
                            //a try-catch azért kell hogyha nem lennének almenük, akkor a catch-be futna
                        try {
                                if(selectedItem.isExpanded()){
                                    selectedItem.setExpanded(false);
                                }else{
                                    selectedItem.setExpanded(true);
                                }  
                        } catch (Exception ex) {
                            System.out.print(ex);
                        }
                           break;
                        case MENU_LIST: 
                            /*alapból mindig az látszódik ami alul van a szerkeztőben, a hierarchiában*/
                           contactPane.setVisible(true);
                           exportPane.setVisible(false);
                           break;   
                        case MENU_EXPORT:  
                           contactPane.setVisible(false);
                           exportPane.setVisible(true);
                           break;                        
                        case MENU_EXIT:
                            //program bezárás
                            System.exit(0);
                            break;
                    }
                }
            }
        });
    }

//    private void pdfGeneration(String filename, String text){
//        //ez maga a pdf file
//        Document document = new Document();
//        try{
//            //üres dokumentum jön létre    //hová mentem
//            PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf")); 
//            //megnyitom az üres doksit
//            document.open();
//            //kép a pdf-re -> ehhez létre kell hozni egy Image-t, de a pdf-nél hibát fog adni, mert használtunk már
//            //egy mási osztályból Imaget-t a nyilaknál, de ide egy másik osztályból kell ->
//            //vagy teljes útvonalat használunk
//            //vagy külön osztályba tesszük ezt
//            com.itextpdf.text.Image image1 = com.itextpdf.text.Image.getInstance(getClass().getResource("/pdfkep.jpeg"));
//        }catch(Exception e){            
//        }
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTableData();
        setMenuData();
        //Ez itt nem szerencsés, mert így minden egyes indításkor generálódik pdf, a fájlba írás általában úgy
        //történik, hogy ha már létezik a fájl akkor felülírja
//        PdfGeneration pdfCreator = new PdfGeneration("filename", "text");
    }
}


/*Event handler: eseményezelő, egy akció bekövetkezteor triggerel vmit (vagyis valami történni fog). Ezt elindítja valami -> egy esemény
  Listener: hasonló az előzőhöz, de ez egy változásra figyel folyamatosan és az indítja el*/
/*PDF generálás segédek:
   -itext 5.5.5  letöltése //ha olyan programhoz használjuk amiért pénzt kérünk a fizetős változatát kell használni
   -itextpdf-5.5.5.jar fájl bementése a főkönyvtárba (PhoneBook)
   -Libraries-> Add Jar -> iválaszt -> hozzáad*/
/*A projet dist mappájában van a .jar file amit használni kell és a lib mappa, hogy más gépeken futtatható legyen a program, ezt kell csak
  átadni, ilyenkor a pdf-et e mellé a fájl mellé fogja generálni, ha nem állítunk be más lehetőséget.*/