package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.DataStorage;
import utilities.Crud;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class renders the report showing appointments based on type
 * @author Priya
 */


public class AppointmentTypeScreen extends Crud {
    public Button Jan;
    public Button feb;
    public Button march;
    public Button may;
    public Button june;
    public Button july;
    public Button oct;
    public Button sep;
    public Button nov;
    public Button Aug;
    public Button april;
    public Button dec;
    public TableView typeTable;
    public TableColumn aptTypeCol;
    public TableColumn countCol;
    public Button close;

    /**
     * This class creates an AppointmentType object
     */
    public static class AppointmentType{
        public String type;
        public int count;
        public AppointmentType(String type, int count) {
            this.type = type;
            this.count = count;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    /**
     * This method gets the number of appointments based on type and for a particular month
     * @param month
     * @throws SQLException
     */
    public void getCount(int month) throws SQLException {
        String type;
        int count;
        ObservableList<AppointmentType>aptTypeItems = FXCollections.observableArrayList();

        ResultSet rs = Select("Select Type, count(*) from Appointments where EXTRACT(month FROM Start) = "+month+ " group by Type");
        while (rs.next()) {
            type = rs.getString("Type");
            count = Integer.parseInt(rs.getString("count(*)"));
            AppointmentType aptType = new AppointmentType(type, count);
            aptTypeItems.add(aptType);
        }
        aptTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("Count"));
        typeTable.setItems(aptTypeItems);
    }


    /**
     * This method gets the number of appointments that belong to each type for January
     * @param actionEvent
     * @throws SQLException
     */
    public void onJan(ActionEvent actionEvent) throws SQLException {
        getCount(1);

    }

    public void onFeb(ActionEvent actionEvent) throws SQLException {
        getCount(2);
    }

    public void onMarch(ActionEvent actionEvent) throws SQLException {
        getCount(3);
    }

    public void onMay(ActionEvent actionEvent) throws SQLException {
        getCount(5);
    }

    public void onJune(ActionEvent actionEvent) throws SQLException {
        getCount(6);
    }

    public void onJuly(ActionEvent actionEvent) throws SQLException {
        getCount(7);
    }

    public void onOct(ActionEvent actionEvent) throws SQLException {
         getCount(10);
    }

    public void onSep(ActionEvent actionEvent) throws SQLException {
         getCount(9);
    }

    public void onNov(ActionEvent actionEvent) throws SQLException {
         getCount(11);
    }

    public void onAug(ActionEvent actionEvent) throws SQLException {
         getCount(8);
    }

    public void onApril(ActionEvent actionEvent) throws SQLException {
         getCount(4);
    }

    public void onDec(ActionEvent actionEvent) throws SQLException {
         getCount(12);
    }

    /**
     * This method closes the screen on clicking the close button
     * @param actionEvent
     */
    public void OnClose(ActionEvent actionEvent) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
