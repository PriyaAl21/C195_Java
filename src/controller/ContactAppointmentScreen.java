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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * This class displays the appointments under each contact names and the info about it
 * @author Priya
 */
public class ContactAppointmentScreen extends Crud implements Initializable {
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

    /**
     * This method has code that runs when the page is laoding
     * @param url
     * @param resourceBundle
     */
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

    /**
     * This method closes the screen when the close button is clicked
     * @param actionEvent
     */
    public void OnClose(ActionEvent actionEvent) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    /**
     * This method displays the appointment details of an appointment for the selected contact name
     * @param actionEvent
     * @throws SQLException
     */
    public void OnContactCombo(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> aptTypes = FXCollections.observableArrayList();
        String contact = (String) contactCombo.getSelectionModel().getSelectedItem();
        ResultSet rs =
                Select("select appointments.Appointment_ID,  appointments.Title, appointments.Description,\n" +
                        "appointments.Type,appointments.Start,appointments.End,appointments.Customer_ID\n" +
                        "from appointments \n" +
                        "join contacts\n" +" on appointments.Contact_ID = contacts.Contact_ID\n " +
                        "where contacts.Contact_Name = "+ '"'+contact+'"'+" order by appointments.Appointment_ID");
        while (rs.next()) {
            int aptId = Integer.parseInt(rs.getString("Appointment_ID"));
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String type = rs.getString("Type");

            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            ZonedDateTime ldtZoned = start.atZone(ZoneId.systemDefault());
            LocalDateTime startDateNTime = ldtZoned.toLocalDateTime();

            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            ZonedDateTime ldtZoned1 = end.atZone(ZoneId.systemDefault());
            LocalDateTime endDateNTime = ldtZoned1.toLocalDateTime();

            int customerId = (rs.getInt("Customer_ID"));

            Appointment newApt = new Appointment(aptId,title,description,type,startDateNTime,endDateNTime,customerId);
            aptTypes.add(newApt);
        }

       for (Appointment apt:aptTypes){
           System.out.println(apt.getAptId());
        };
            aptIdCol.setCellValueFactory(new PropertyValueFactory<>("aptId"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateNTime"));
            endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateNTime"));
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

            aptTable.setItems(aptTypes);


    }


}
