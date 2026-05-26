package ASimulatorSystem;

import java.sql.*;  
import javax.swing.JOptionPane;

public class Conn{
    Connection c;
    Statement s;
    public Conn(){  
        try{  
            Class.forName("com.mysql.cj.jdbc.Driver");
            String host = getSetting("ATM_DB_HOST", "atm.db.host", "localhost");
            String port = getSetting("ATM_DB_PORT", "atm.db.port", "3306");
            String database = getSetting("ATM_DB_NAME", "atm.db.name", "bankmanagementsystem");
            String user = getSetting("ATM_DB_USER", "atm.db.user", "root");
            String password = getSetting("ATM_DB_PASSWORD", "atm.db.password", "");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database
                    + "?createDatabaseIfNotExist=true"
                    + "&useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=Asia/Kolkata";
            c = DriverManager.getConnection(url, user, password);
            s =c.createStatement(); 
            initializeDatabase();
        }catch(Exception e){ 
            JOptionPane.showMessageDialog(null,
                    "Database connection failed.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Database connection failed", e);
        }  
    }

    private String getSetting(String envName, String propertyName, String defaultValue) {
        String value = System.getProperty(propertyName);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(envName);
        }
        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }
        return value;
    }

    private void initializeDatabase() throws SQLException {
        s.executeUpdate("CREATE TABLE IF NOT EXISTS signup ("
                + "formno VARCHAR(20) PRIMARY KEY,"
                + "name VARCHAR(100),"
                + "fname VARCHAR(100),"
                + "dob VARCHAR(50),"
                + "gender VARCHAR(20),"
                + "email VARCHAR(100),"
                + "marital VARCHAR(30),"
                + "address VARCHAR(255),"
                + "city VARCHAR(100),"
                + "pincode VARCHAR(20),"
                + "state VARCHAR(100))");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS signup2 ("
                + "formno VARCHAR(20) PRIMARY KEY,"
                + "religion VARCHAR(50),"
                + "category VARCHAR(50),"
                + "income VARCHAR(50),"
                + "education VARCHAR(100),"
                + "occupation VARCHAR(100),"
                + "pan VARCHAR(50),"
                + "aadhar VARCHAR(50),"
                + "scitizen VARCHAR(10),"
                + "eaccount VARCHAR(10))");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS signup3 ("
                + "formno VARCHAR(20) PRIMARY KEY,"
                + "atype VARCHAR(100),"
                + "cardno VARCHAR(20),"
                + "pin VARCHAR(10),"
                + "facility VARCHAR(255))");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS login ("
                + "formno VARCHAR(20),"
                + "cardno VARCHAR(20),"
                + "pin VARCHAR(10))");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS bank ("
                + "pin VARCHAR(10),"
                + "date VARCHAR(100),"
                + "mode VARCHAR(30),"
                + "amount VARCHAR(20))");
        s.executeUpdate("CREATE TABLE IF NOT EXISTS atm_session ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                + "pin VARCHAR(10),"
                + "login_time TIMESTAMP,"
                + "logout_time TIMESTAMP)");
    }
}  
