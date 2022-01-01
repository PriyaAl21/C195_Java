package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import model.DataStorage;
import utilities.Crud;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import static controller.LogInScreen.name;
import model.Appointment;


/**
 * This class adds appointment to the database as well local datastorage in the application
 * @author Priya
 */

public class AddAppointmentScreen extends Crud implements Initializable {
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField typeField;
    public ComboBox<String> chooseContact;
    public TextField ApptIdField;
    public Button addAppointmentButton;
    public TextField customerIDField;
    public TextField userIDField;
    public DatePicker dateField;
    public ComboBox<String> chooseStartHours;
    public ComboBox<String> chooseStartMin;
    public ComboBox<String> chooseEndHours;
    public ComboBox<String> chooseEndMin;
    public Button cancel;

    /**
     * This method contains code that runs when the page loads
     * @param url
     * @param resourceBundle
     */
    @Override
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
        chooseContact.setItems(contactNames);

//hours
        ObservableList<String> hours = FXCollections.observableArrayList();

        for(int i = 5; i<23 && i>=5;i++){

            if(String.valueOf(i).length()==1){
                hours.add("0"+String.valueOf(i));

            }
            else{
                hours.add(String.valueOf(i));

            }
        }
        //hours.add("test");
        chooseStartHours.setItems(hours);
        chooseEndHours.setItems(hours);

//minutes
        ObservableList<String> minutes = FXCollections.observableArrayList();

        String[] minArray = {"00","15","30","45"} ;
            for(int i=0;i<minArray.length;i++){
                minutes.add(minArray[i]);

            }
           chooseStartMin.setItems(minutes);
            chooseEndMin.setItems(minutes);
    }

    /**
     * This method adds a new appointment to the database and datastorage lists
     * @param actionEvent
     * @return
     * @throws SQLException
     */
    public int OnAddAppointment(ActionEvent actionEvent) throws SQLException {

        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        String contact = chooseContact.getSelectionModel().getSelectedItem();
        String cusId = customerIDField.getText();
        String usId = userIDField.getText();

        LocalDate date = dateField.getValue();

        String startHours = chooseStartHours.getSelectionModel().getSelectedItem();
        String startMin = chooseStartMin.getSelectionModel().getSelectedItem();
        String endHours = chooseEndHours.getSelectionModel().getSelectedItem();
        String endMin = chooseEndMin.getSelectionModel().getSelectedItem();

        //check if fields are empty

        if(title.equals("")||description.equals("")||type.equals("")||contact.equals("")||cusId.equals("")||usId.equals("")
                || date.equals("")|| startHours.equals("")||startMin.equals("")||endHours.equals("")||endMin.equals("")){
            Alert empty = new Alert(Alert.AlertType.INFORMATION);
            empty.setTitle("Empty fields");
            empty.setContentText("Please fill all fields!");
            empty.showAndWait();
            return 1;
        }
        else{
            try{
                Integer.parseInt(cusId);
                Integer.parseInt(usId);
            }
            catch(NumberFormatException e){
                Alert empty = new Alert(Alert.AlertType.INFORMATION);
                empty.setTitle("Empty fields");
                empty.setContentText("Please enter a number for customer id and user id!");
                empty.showAndWait();
                return 1;
            }
        }

        int customerId = Integer.parseInt(cusId);
        int userId = Integer.parseInt(usId);




        // DateTimeFormatter parser = DateTimeFormatter.ofPattern("hh:mm:00");
        LocalTime localStartTime = LocalTime.parse(startHours + ":" + startMin + ":" + "00");
        LocalTime localEndTime = LocalTime.parse(endHours + ":" + endMin + ":" + "00");
        localStartTime.plusSeconds(00);
        localEndTime.plusSeconds(00);
        LocalDateTime startDateTime = LocalDateTime.of(date, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, localEndTime);




        //converting start time and end time to eastern time
        ZoneId Eastern = ZoneId.of("America/New_York");
        ZonedDateTime localStart = startDateTime.atZone(ZoneId.systemDefault());

        ZonedDateTime localEnd = endDateTime.atZone(ZoneId.systemDefault());

        ZonedDateTime startEastern = localStart.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endEastern = localEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

        LocalDateTime startDT = startEastern.toLocalDateTime();
        LocalDateTime endDT = endEastern.toLocalDateTime();
        LocalTime startEastTime = startDT.toLocalTime();
        LocalTime endEastTime = endDT.toLocalTime();

        //check if start time is between 8 am and 10 pm EST
        LocalTime lowerLimit = LocalTime.parse("08:00:00");
        LocalTime upperLimit = LocalTime.parse("22:00:00");

        //to check if start time is after end time

        if(startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime)){
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Start time can not be after or at End time!");
            noSelection.showAndWait();
            return -1;
        }

        //to check if date is today or after that
        if(LocalDate.now().compareTo(date)>0){
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Please choose a valid date!");
            noSelection.showAndWait();
            return -1;
        }
        //check if start time is between 8 am and 10 pm EST
        if (startEastTime.compareTo(lowerLimit) < 0) {
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Appointment times should be between 8.00 am and 10.00pm EST!");
            noSelection.showAndWait();
            return -1;
        } else if (startEastTime.compareTo(upperLimit) > 0) {
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Appointment times should be between 8.00 am and 10.00pm EST!");
            noSelection.showAndWait();
            return -1;
        }


        if (endEastTime.compareTo(lowerLimit) < 0) {
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Appointment times should be between 8.00 am and 10.00pm EST!");
            noSelection.showAndWait();
            return -1;

        } else if (endEastTime.compareTo(upperLimit) > 0) {
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Appointment times should be between 8.00 am and 10.00pm EST!");
            noSelection.showAndWait();
            return -1;
        }
        //

        LocalDateTime createDate = LocalDateTime.now();
        Timestamp lastUpdate = Timestamp.valueOf(createDate);
        String createdBy = name;



        //to check if appointments collide

        for (Appointment apt : DataStorage.getAllAppointments()) {
            LocalDateTime MS = startDateTime;
            LocalDateTime ME = endDateTime;
            LocalDateTime JS = apt.getStartDateNTime();
            LocalDateTime JE = apt.getEndDateNTime();
            if(customerId==apt.getCustomerId()) {
                if ((MS.isAfter(JS) || MS.isEqual(JS)) && MS.isBefore(JE)) {
                    Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                    noSelection.setTitle("Appointment collision");
                    noSelection.setContentText("There is an existing appointment in this time interval!\n"+"Please change times!");
                    noSelection.showAndWait();
                    return -1;
                } else if (ME.isAfter(JS) && (ME.isBefore(JE) || ME.isEqual(JE))) {
                    Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                    noSelection.setTitle("Appointment collision");
                    noSelection.setContentText("There is an existing appointment in this time interval!\n"+"Please change times!");
                    noSelection.showAndWait();
                    return -1;
                } else if ((MS.isBefore(JS) || MS.isEqual(JS)) && (ME.isAfter(JE) || ME.isEqual(JE))) {
                    Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                    noSelection.setTitle("Appointment collision");
                    noSelection.setContentText("There is an existing appointment in this time interval!\n"+"Please change times!");
                    noSelection.showAndWait();
                    return -1;
                }
            }
        }


        //utc date times
            //converting to UTC for storing in database
            System.out.println(localStart.toString());

            ZonedDateTime utcStart = localStart.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime utcEnd = localEnd.withZoneSameInstant(ZoneId.of("UTC"));

            LocalDateTime utcStartDateTime = utcStart.toLocalDateTime();
            LocalDateTime utcEndDateTime = utcEnd.toLocalDateTime();

            System.out.println(utcStartDateTime.toString());
            //adding to database

            ResultSet rs = Select("Select * from contacts where Contact_Name = " + '"' + contact + '"');
            while (rs.next()) {
                int contactId = Integer.parseInt(rs.getString("Contact_ID"));


                InsertUpdateDelete("Insert into appointments (Appointment_ID, Title,Description, Location, Type," +
                        " Start,End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                        "Values(null, " + '"' + title + '"' + "," + '"' + description + '"' + "," + '"' + location + '"' + "," + '"' + type + '"' + "," + '"' + utcStartDateTime + '"' + "," + '"' + utcEndDateTime + '"' + ","
                        + '"' + createDate + '"' + "," + '"' + createdBy + '"' + "," + '"' + lastUpdate + '"' + "," + '"' + createdBy + '"' + "," + '"' + customerId + '"' + "," + '"' + userId + '"' + ","
                        + '"' + contactId + '"' + ")");

                ResultSet rs1 =
                        Select("select appointments.Appointment_ID,  appointments.Title, appointments.Description ,appointments.Location,contacts.contact_Name,\n" +
                                "appointments.Type,appointments.Start,appointments.End,appointments.Customer_ID,appointments.User_ID\n" +
                                "from appointments \n" +
                                "join contacts\n" + " on appointments.Contact_ID = contacts.Contact_ID\n order by appointments.Appointment_ID DESC LIMIT 1");
                while (rs1.next()) {
                    Appointment.populate(rs1);
                }
                Stage stage = (Stage) cancel.getScene().getWindow();
                stage.close();


            }

                return 0;
    }

    /**
     * This method closes the page when the cancel button is clicked
     * @param actionEvent
     */

    public void OnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void OnChooseContact(ActionEvent actionEvent) {
    }


}
