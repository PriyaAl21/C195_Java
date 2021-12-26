package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
            //System.out.println(String.valueOf(i));
            if(String.valueOf(i).length()==1){
                hours.add("0"+String.valueOf(i));
                // System.out.println("0"+String.valueOf(i));
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

    public void OnModifyAppointment(ActionEvent actionEvent) throws SQLException {

        int aptId = Integer.parseInt(AptIdField.getText());
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        String contact;
        if(chooseContact.getSelectionModel().getSelectedItem()==null){
            contact = chooseContact.getPromptText();
        }
        else{
           contact = chooseContact.getSelectionModel().getSelectedItem();
        }
        int customerId = Integer.parseInt(customerIDField.getText());
        int userId = Integer.parseInt(userIDField.getText());

        LocalDate date;
        if(dateField.getValue()==null){
            date = LocalDate.parse(dateField.getPromptText());
        }
        else{
            date = dateField.getValue();
        }

        String startHours;

        if(chooseStartHours.getSelectionModel().getSelectedItem()==null){
            startHours = chooseStartHours.getPromptText();
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
            endHours = chooseEndHours.getPromptText();
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
        System.out.println(startMin);
        LocalTime localStartTime = LocalTime.parse(startHours + ":" + startMin + ":" + "00");
        LocalTime localEndTime = LocalTime.parse(endHours + ":" + endMin + ":" + "00");
        localStartTime.plusSeconds(00);
        localEndTime.plusSeconds(00);
        LocalDateTime startDateTime = LocalDateTime.of(date, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, localEndTime);

        //converting start time and end time to eastern time
        ZoneId Eastern = ZoneId.of("America/New_York");
        ZonedDateTime localStart = startDateTime.atZone(ZoneId.systemDefault());
        System.out.println(localStart.toLocalTime().toString());
        ZonedDateTime localEnd = endDateTime.atZone(ZoneId.systemDefault());
        System.out.println(localEnd.toLocalTime().toString());
        ZonedDateTime startEastern = localStart.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endEastern = localEnd.withZoneSameInstant(ZoneId.of("America/New_York"));
//        ZonedDateTime startDT = startDateTime.atZone(Eastern);
//        ZonedDateTime endDT = endDateTime.atZone(Eastern);
        LocalDateTime startDT = startEastern.toLocalDateTime();
        LocalDateTime endDT = endEastern.toLocalDateTime();
        LocalTime startEastTime = startDT.toLocalTime();
        LocalTime endEastTime = endDT.toLocalTime();
        System.out.println(startEastTime.toString());
        System.out.println(endEastTime.toString());
        //check if start time is between 8 am and 10 pm EST
        LocalTime lowerLimit = LocalTime.parse("08:00:00");
        LocalTime upperLimit = LocalTime.parse("22:00:00");

        if (startEastTime.compareTo(lowerLimit) < 1) {
            System.out.println("too early!");
        } else if (startEastTime.compareTo(upperLimit) > 1) {
            System.out.println("late!");
        }


        if (endEastTime.compareTo(lowerLimit) < 1) {
            System.out.println("too early!");
        } else if (endEastTime.compareTo(upperLimit) > 1) {
            System.out.println("late!");
        }
        //

        LocalDateTime createDate = LocalDateTime.now();
        Timestamp lastUpdate = Timestamp.valueOf(createDate);
        String createdBy = name;

        //to check if appointments collide

//        for (Appointment apt : DataStorage.getAllAppointments()) {
//
//        }


        ResultSet rs = Select("Select * from contacts where Contact_Name = " + '"' + contact + '"');
        while (rs.next()) {
            int contactId = Integer.parseInt(rs.getString("Contact_ID"));

            InsertUpdateDelete("UPDATE appointments SET Title = " + '"' + title + '"' +
                    " ,Description = " + '"' + description + '"' + " ,Location = " + '"' + location + '"' + " ,Type = " + '"' + type + '"' +
                    " ,Start = " + '"' + startDateTime + '"' + " ,End = " + '"' + endDateTime + '"' + " ,Customer_ID = " + '"' + customerId + '"' + " ,User_ID = " + '"' + userId + '"' + ",Contact_ID = " + '"' + contactId + '"' +
                    " WHERE Appointment_ID = " + aptId);
            ResultSet rs1 =
                    Select("select appointments.Appointment_ID,  appointments.Title, appointments.Description ,appointments.Location,contacts.contact_Name,\n" +
                            "appointments.Type,appointments.Start,appointments.End,appointments.Customer_ID,appointments.User_ID\n" +
                            "from appointments \n" +
                            "join contacts\n" + " on appointments.Contact_ID = contacts.Contact_ID\n order by appointments.Appointment_ID DESC LIMIT 1");
            while (rs1.next()) {
                for (Appointment apt : DataStorage.getAllAppointments()) {
                    int id = Integer.parseInt(rs1.getString("Appointment_ID"));
                    if (apt.getAptId() == id) {
                        DataStorage.getAllAppointments().remove(apt);
                        Appointment.populate(rs1);

                    }
                }

            }

        }    Stage stage = (Stage) cancel.getScene().getWindow();
            stage.close();

    }

    public void OnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    //int apptId, String title, String description, String location, String contact, String type,
    //                       LocalDateTime startDateNTime, LocalDateTime endDateNTime, int customerId, int userId
}
