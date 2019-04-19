package serverapp.db;


import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class DataBasePoolTest {
    private DataBaseConnectionPool connectionPool;
    @Test
    public void create() throws SQLException{
        connectionPool = DataBaseConnectionPool.getInstance();
        Assert.assertTrue(connectionPool.getConnection().isValid(100));
    }
    public void getConnection() {
    }

    public void releaseConnection() {
    }
}
