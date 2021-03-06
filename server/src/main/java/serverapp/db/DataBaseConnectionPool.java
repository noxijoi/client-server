package serverapp.db;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(DataBaseConnectionPool.class);
    private static final String DB_PROPERTIES_FILE = "db";

    private static DataBaseConnectionPool instance;

    private static BlockingQueue<Connection> connectionPool;

    private static final Lock locker = new ReentrantLock();

    private static int DEFAULT_POOL_SIZE = 10;

    public static DataBaseConnectionPool getInstance(){
        if(instance == null){
            try{
                locker.lock();
                if(instance == null){
                    instance = new DataBaseConnectionPool();
                    init();
                }
            } finally {
                locker.unlock();
            }
        }
        return instance;
    }

    private DataBaseConnectionPool() {
    }

    private static void init(){
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_PROPERTIES_FILE);

        final String URL = resourceBundle.getString("db.host");
        final String USER = resourceBundle.getString("db.login");
        final String PASSWORD = resourceBundle.getString("db.password");
        final String CHARACTER_ENCODIND = resourceBundle.getString("db.characterEncoding");
        final String USE_UNICODE = resourceBundle.getString("db.useUnicode");
        final String USER_LEGACY_DATETIME_CODE =resourceBundle.getString("db.useLegacyDatetimeCode");
        final String useJDBCCompliantTimezoneShift = resourceBundle.getString("db.useJDBCCompliantTimezoneShift");
        final String SERVER_TIMEZONE = resourceBundle.getString("db.serverTimezone");


        Properties properties = new Properties();
        properties.put("user", USER);
        properties.put("password", PASSWORD);
        properties.put("characterEncoding", CHARACTER_ENCODIND);
        properties.put("useUnicode", USE_UNICODE);
        properties.put("useLegacyDatetimeCode", USER_LEGACY_DATETIME_CODE);
        properties.put("useJDBCCompliantTimezoneShift", useJDBCCompliantTimezoneShift);
        properties.put("serverTimezone", SERVER_TIMEZONE);

        connectionPool = new ArrayBlockingQueue<>(DEFAULT_POOL_SIZE);

        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            Connection connection = null;
            try{
                connection = DriverManager.getConnection(URL, properties);
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
            try {
                connectionPool.put(connection);
            } catch (InterruptedException e) {
                LOGGER.warn(e.getMessage());
            }
        }
        LOGGER.info("Connection pool is inited");
    }


    public static void shutDown(){
        for (Connection c : connectionPool) {
            try {
                c.close();
                LOGGER.info("close connection in connection pool");
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
            }

        }
    }

    private static void releaseConnection(Connection connection) {
        try{
            connectionPool.put(connection);
            LOGGER.info("put connection to connection pool");
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try{
            connection = connectionPool.take();
            LOGGER.info("Take connection from connection pool");
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
        return connection;
    }
}



