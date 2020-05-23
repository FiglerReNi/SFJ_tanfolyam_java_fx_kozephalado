package phonebook;
/*Ez azért kell, mert a SimpleStringProperty típusú adat kompatibilis a tableView elemmel, amiben az adatokat meg akarjuk jeleníteni,
  ez képes kommunikálni az adattáblával is*/
import javafx.beans.property.SimpleStringProperty;

public class Person {
    
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty email;
    
    public Person(){
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
    }
    
    public Person(String fname, String lname, String email){
        this.firstName = new SimpleStringProperty(fname);
        this.lastName = new SimpleStringProperty(lname);
        this.email = new SimpleStringProperty(email);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fname){
        firstName.set(fname);
    }
    
    public String getLastName() {
        return lastName.get();
    }
    
    public void setLastName(String lname){
        firstName.set(lname);
    }

    public String getEmail() {
        return email.get();
    }
    
    public void setEmail(String email){
        firstName.set(email);
    }
}
