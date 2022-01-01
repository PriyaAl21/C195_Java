package utilities;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * This is an abstract class which contains methods for reading and updating the database
 */
public abstract class Crud {

    public  Crud() {

    }

    /**
     * This method retrieves information from database and returns it
     * @param insertStatement
     * @return rs
     * @throws SQLException
     */
    public ResultSet Select(String insertStatement) throws SQLException {
        Connection conn = JDBC.getConnection();
        DBQuery.setStatement(conn);
        Statement st = DBQuery.getStatement();
        st.execute(insertStatement);
        ResultSet rs = st.getResultSet();
        //ObservableList<ResultSet> allResults = FXCollections.observableArrayList();
        return rs;
    }
    /**
     * This method updates tables in database
     * @param insertStatement
     * @throws SQLException
     */

    public void InsertUpdateDelete(String insertStatement) throws SQLException {
        Connection conn = JDBC.getConnection();
        DBQuery.setStatement(conn);
        Statement st = DBQuery.getStatement();
        st.execute(insertStatement);


    }

}
