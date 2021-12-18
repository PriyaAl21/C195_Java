package utilities;
import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import utilities.JDBC;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public abstract class Crud {

    public  Crud() {

    }

    public ResultSet Select(String insertStatement) throws SQLException {
        Connection conn = JDBC.getConnection();
        DBQuery.setStatement(conn);
        Statement st = DBQuery.getStatement();
        st.execute(insertStatement);
        ResultSet rs = st.getResultSet();
        //ObservableList<ResultSet> allResults = FXCollections.observableArrayList();
        return rs;
    }

    public void InsertUpdateDelete(String insertStatement) throws SQLException {
        Connection conn = JDBC.getConnection();
        DBQuery.setStatement(conn);
        Statement st = DBQuery.getStatement();
        st.execute(insertStatement);
        System.out.println("added");

    }

}
