package utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains the method to create a connection statement
 */

public class DBQuery {

    private static Statement statement;

    /**
     * This method creates a create statement that is used to interact with database
     * @param conn
     * @throws SQLException
     */
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    /**
     * This method returns the created statement
     * @return statement
     */
    public static Statement getStatement() {
        return statement;
    }
}
