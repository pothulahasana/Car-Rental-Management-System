import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/cardb";
    private static final String USER = "root";
    private static final String PASSWORD = "Dob@28032007"; // put your MySQL password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // load driver

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("✅ Database Connected Successfully");
            return con;

        } catch (Exception e) {
            System.out.println("❌ Database Connection Failed");
            e.printStackTrace();   // VERY IMPORTANT
            return null;
        }
    }
}
