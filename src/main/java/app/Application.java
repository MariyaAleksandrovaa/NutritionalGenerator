package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Application {

	public static Connection con = null;
	static String driver = "com.mysql.cj.jdbc.Driver";

	public static void main(String[] args) throws SQLException {

		try {
			// Load the JDBC driver
			Class.forName(driver);
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nutri_db", "root", "mysql");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SpringApplication.run(Application.class, args);

	}

}
