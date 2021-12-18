package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Appointment;
import utilities.Crud;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentTypeScreen extends Crud implements Initializable {
    public TableView aptTable;
    public TableColumn aptIdCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn typeCol;
    public TableColumn startDateTimeCol;
    public TableColumn endDateTimeCol;
    public TableColumn customerIDCol;
    public Button close;
    public ComboBox contactCombo;

    public void initialize(URL url, ResourceBundle resourceBundle) {
// contact names in combo box
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try {
            ResultSet rs = (ResultSet) Select("select * from contacts");
            while (rs.next()) {
                contactNames.add(rs.getString("Contact_Name"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        contactCombo.setItems(contactNames);
    }

    public void OnClose(ActionEvent actionEvent) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    public void OnContactCombo(ActionEvent actionEvent) throws SQLException {
        String contact = (String) contactCombo.getSelectionModel().getSelectedItem();
        ResultSet rs1 =
                Select("select appointments.Appointment_ID,  appointments.Title, appointments.Description,\n" +
                        "appointments.Type,appointments.Start,appointments.End,appointments.Customer_ID\n" +
                        "from appointments \n" +
                        "join contacts\n" +" on appointments.Contact_ID = contacts.Contact_ID\n " +
                        "where contacts.Contact_Name = "+ contact+"order by appointments.Appointment_ID");
        while (rs1.next()) {
            Appointment.populate(rs1);
        }
    }
}
