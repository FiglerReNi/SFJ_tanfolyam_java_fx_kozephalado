package phonebook;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DB {
    //final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    //nincsen server a gépen, és sehol ebben a példában, így belső útvonalat adunk meg
    //sampleDB -> adatbázis neve
    //create true -> ha nincs még ilyen létrehozza
    final String URL = "jdbc:derby:sampleDB;create=true";
    //final String USERNAME = "";
    //final String PASSWORD = "";
    
    private Connection conn = null;
    private Statement createStatement = null;
    private DatabaseMetaData dbmd = null;
    
    public DB() {
        //Ez a híd a kód és az adatbázis között       
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("A híd létrejött.");
        } catch (SQLException ex) {
            System.out.println("Valami baj van a connection létrehozásakor.");
            System.out.println("" + ex);
        }

        /*Statement: ezzel mondjuk meg, hogy adatot küldünk vagy kérünk
       Kétféle adatot nyerhetünk ki:
            - metaadatok: az adatbázisról adnak információt pl hány adattábla van
            - konkrét adatok*/
        //Ez a teherautó a hídon
        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van a createStatement létrehozásakor.");
                System.out.println("" + ex);
            }
        }

        //Üres e az adatbázis? (Első futtatáskor)
        //Lekérjük az adatbázis metaadatait        
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adatbázis leírás létrehozásakor.");
            System.out.println("" + ex);
        }

        //Az adatbázis információk ebben a resultsetben jönnek vissza, táblázatszerűen tárolja
        //az adatokat.
        /*Shema: APP
          Adattábla neve: USERS
         */
        //megnézzük, hogy adott tábla létezik-e az adatbázisban, ha nem létrehozzuk
        try {
            ResultSet rs = dbmd.getTables(null, "APP", "CONTACTS", null);
            //ha rs1-nek van következő értéke: rs.next()
            if (!rs.next()) {
               createStatement.execute("create table contacts(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),lastname varchar(20), firstname varchar(20), email varchar(30))");        
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattábla létrehozásakor.");
            System.out.println("" + ex);
        }
    }  
    
    public ArrayList<Person> getAllContacts(){
        String sql = "select * from contacts";
        ArrayList<Person> users = null;
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            users = new ArrayList<Person>();
             while (rs.next()) {
                 Person actualPerson = new Person(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"), rs.getString("email"));
                 users.add(actualPerson);
             }
        } catch (SQLException ex) {
              System.out.println("Valami baj van a user kiolvasásakor");
              System.out.println("" + ex);
        }
        return users;
    }
    
    public void addContact(Person person){
         try {
             //mivel id-t nem akarunk beállítani, mert automatikus, ezért meg kell adni a mezőket amiknek értéket adunk
            String sql = "insert into contacts (lastname, firstname, email) values(?,?,?)";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, person.getLastName());
            pstm.setString(2, person.getFirstName());
            pstm.setString(3, person.getEmail());
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a contact hozzáadásakor");
            System.out.println("" + ex);
        }
    }
    
    public void updateContact(Person person) {
        try {
            String sql = "update contacts set lastname = ?, firstname = ?, email = ? where id = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, person.getLastName());
            pstm.setString(2, person.getFirstName());
            pstm.setString(3, person.getEmail());
            pstm.setInt(4, Integer.parseInt(person.getId()));
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a contact frissítéseor");
            System.out.println("" + ex);
        }
    }

    public void removeContact(Person person) {
        try {
            String sql = "delete from contacts where id = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, Integer.parseInt(person.getId()));
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a contact törlésekor");
            System.out.println("" + ex);
        }
    }
}
