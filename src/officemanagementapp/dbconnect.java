
package DBapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnect {
	
	public Connection conn;
	
	public dbconnect() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=12345"); // change based on user and password
			System.out.println("Connected to dbsales Successfully!");
		} catch (SQLException e) {
			System.out.println("An error happened connecting to dbsales");
			System.out.println(e.getMessage());
		}
	}

	public void disconnect() {
		try {
			conn.close();
			System.out.println("Closing connection to the DB");
		} catch (Exception e) {
	        System.out.println("An error happened closing the connection to the DB");
			System.out.println(e.getMessage());			
		}
	}
	public static void main(String[] args) {
	    dbconnect db = new dbconnect();
	}

}

