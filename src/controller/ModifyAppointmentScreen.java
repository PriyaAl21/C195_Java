package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.DataStorage;
import utilities.Crud;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

import static controller.LogInScreen.name;

public class ModifyAppointmentScreen extends Crud implements Initializable {
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField typeField;
    public ComboBox<String> chooseContact;
    public TextField AptIdField;
    public Button modifyAppointmentButton;
    public TextField startDateTimeField;
    public TextField endDateTimeField;
    public TextField customerIDField;
    public TextField userIDField;
    public DatePicker dateField;
    public ComboBox<String> chooseStartHours;
    public ComboBox<String> chooseStartMin;
    public ComboBox<String> chooseEndHours;
    public ComboBox<String> chooseEndMin;
    public Button cancel;


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

    public void fillDetails(Appointment apt){
        AptIdField.setText(String.valueOf(apt.getAptId()));
        titleField.setText(apt.getTitle());
        descriptionField.setText(apt.getDescription());
        locationField.setText(apt.getLocation());
        typeField.setText(apt.getType());
        chooseContact.setPromptText(apt.getContact());
        customerIDField.setText(String.valueOf(apt.getCustomerId()));
        userIDField.setText(String.valueOf(apt.getUserId()));
        dateField.setPromptText(apt.getStartDateNTime().toLocalDate().toString());
        chooseStartHours.setPromptText(String.valueOf(apt.getStartDateNTime().toLocalTime().getHour()));
        chooseStartMin.setPromptText(String.valueOf(apt.getStartDateNTime().toLocalTime().getMinute()));
        chooseEndHours.setPromptText(String.valueOf(apt.getEndDateNTime().toLocalTime().getHour()));
        chooseEndMin.setPromptText(String.valueOf(apt.getEndDateNTime().toLocalTime().getMinute()));;
    }
    public void OnChooseContact(ActionEvent actionEvent) {
    }

    public int OnModifyAppointment(ActionEvent actionEvent) throws SQLException {

        int aptId = Integer.parseInt(AptIdField.getText());
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        String contact;
        String cusId = customerIDField.getText();
        String usId = userIDField.getText();

        //check if fields are empty

        if(title.equals("")||description.equals("")||location.equals("")||type.equals("")||cusId.equals("")||usId.equals("")){
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


        if(chooseContact.getSelectionModel().getSelectedItem()==null){
            contact = chooseContact.getPromptText();
        }
        else{
           contact = chooseContact.getSelectionModel().getSelectedItem();
        }


        LocalDate date;
        if(dateField.getValue()==null){
            date = LocalDate.parse(dateField.getPromptText());
        }
        else{
            date = dateField.getValue();
        }

        String startHours;

        if(chooseStartHours.getSelectionModel().getSelectedItem()==null){
            if(chooseStartHours.getPromptText().length() == 1){
                String hour = "0" + chooseStartHours.getPromptText();
                startHours = hour;
            }
            else{startHours = chooseStartHours.getPromptText();}
        }
        else{
            startHours = chooseStartHours.getSelectionModel().getSelectedItem();
        }


        String startMin;

        if(chooseStartMin.getSelectionModel().getSelectedItem()==null){
            if(chooseStartMin.getPromptText().equals("0")){
                String min = chooseStartMin.getPromptText() + "0";
                startMin = min;
            }
            else{startMin = chooseStartMin.getPromptText();}
        }
        else{
            startMin = chooseStartMin.getSelectionModel().getSelectedItem();
        }

        String endHours;

        if(chooseEndHours.getSelectionModel().getSelectedItem()==null){
            if(chooseEndHours.getPromptText().length() == 1){
                String hour = "0" + chooseEndHours.getPromptText();
                endHours = hour;
            }
            else{endHours = chooseEndHours.getPromptText();}
        }
        else{
            endHours = chooseEndHours.getSelectionModel().getSelectedItem();
        }

        String endMin;

        if(chooseEndMin.getSelectionModel().getSelectedItem()==null){
            if(chooseEndMin.getPromptText().equals("0")){
                String min = chooseEndMin.getPromptText() + "0";
                endMin = min;
            }
            else{endMin = chooseEndMin.getPromptText();}
        }
        else{
            endMin = chooseEndMin.getSelectionModel().getSelectedItem();
        }


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

        //to check if start time is after now
        System.out.println(LocalDateTime.now().isAfter(startDateTime));
        if(LocalDateTime.now().isAfter(startDateTime)){
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Start date & time should be in the future!");
            noSelection.showAndWait();
            return -1;
        }

        //to check if start time is after end time

        if(startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime)){
            Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
            noSelection.setTitle("Appointment time");
            noSelection.setContentText("Start time can not be after or at End time!");
            noSelection.showAndWait();
            return -1;
        }



        //check if start time is between 8 am and 10 pm EST
        LocalTime lowerLimit = LocalTime.parse("08:00:00");
        LocalTime upperLimit = LocalTime.parse("22:00:00");


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

        LocalDateTime createDate = LocalDateTime.now();
        Timestamp lastUpdate = Timestamp.valueOf(createDate);
        String createdBy = name;


        //to check if appointments collide

        for (Appointment apt : DataStorage.getAllAppointments()) {
            LocalDateTime MS = startDateTime;
            LocalDateTime ME = endDateTime;
            LocalDateTime JS = apt.getStartDateNTime();
            LocalDateTime JE = apt.getEndDateNTime();

            if(apt.getAptId()!= aptId) {
                if (customerId == apt.getCustomerId()) {
                    if ((MS.isAfter(JS) || MS.isEqual(JS)) && MS.isBefore(JE)) {
                        Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                        noSelection.setTitle("Appointment collision");
                        noSelection.setContentText("There is an existing appointment in this time interval!\n" + "Please change times!");
                        noSelection.showAndWait();
                        return -1;
                    } else if (ME.isAfter(JS) && (ME.isBefore(JE) || ME.isEqual(JE))) {
                        Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                        noSelection.setTitle("Appointment collision");
                        noSelection.setContentText("There is an existing appointment in this time interval!\n" + "Please change times!");
                        noSelection.showAndWait();
                        return -1;
                    } else if ((MS.isBefore(JS) || MS.isEqual(JS)) && (ME.isAfter(JE) || ME.isEqual(JE))) {
                        Alert noSelection = new Alert(Alert.AlertType.INFORMATION);
                        noSelection.setTitle("Appointment collision");
                        noSelection.setContentText("There is an existing appointment in this time interval!\n" + "Please change times!");
                        noSelection.showAndWait();
                        return -1;
                    }
                }
            }
        }
        //utc date times
        //converting to UTC for storing in database

        ZonedDateTime utcStart = localStart.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = localEnd.withZoneSameInstant(ZoneId.of("UTC"));

        LocalDateTime utcStartDateTime = utcStart.toLocalDateTime();
        LocalDateTime utcEndDateTime = utcEnd.toLocalDateTime();

        ResultSet rs = Select("Select * from contacts where Contact_Name = " + '"' + contact + '"');
        while (rs.next()) {
            int contactId = Integer.parseInt(rs.getString("Contact_ID"));

            InsertUpdateDelete("UPDATE appointments SET Title = " + '"' + title + '"' +
                    " ,Description = " + '"' + description + '"' + " ,Location = " + '"' + location + '"' + " ,Type = " + '"' + type + '"' +
                    " ,Start = " + '"' + utcStartDateTime + '"' + " ,End = " + '"' + utcEndDateTime + '"' + " ,Customer_ID = " + '"' + customerId + '"' + " ,User_ID = " + '"' + userId + '"' + ",Contact_ID = " + '"' + contactId + '"' +
                    " WHERE Appointment_ID = " + aptId);


            Appointment modified = new Appointment(aptId,title,description,location, contact,type,
                    startDateTime,  endDateTime,customerId, userId);
            DataStorage.modifyAppointment(modified);

        }    Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();
            return 0;

    }

    public void OnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

}
