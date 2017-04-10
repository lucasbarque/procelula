package estacio.br.com.procelula.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionManager {
    private static String url = "jdbc:mysql://a2plcpnl0778.prod.iad2.secureserver.net/celulas";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String username = "celulas";
    private static String password = "C#{gKW6}ZdFW";
    private static Connection con;

    public static Connection getConnection() throws SQLException {
        try {
            //TODO adicionar mais tentativas de conexao para minimizar erros
            Class.forName(driverName);
            DriverManager.setLoginTimeout(1);
            String dbConnectionString = url+"?user="+username+"&password="+password;
            Properties properties = new Properties();
            properties.put("connectTimeout", "2000");
            con = DriverManager.getConnection(dbConnectionString, properties);
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        return con;
    }
}