package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.DataStorage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import utilities.Crud;

public class AppointmentScreen extends Crud implements Initializable {
    public TableView aptTable;
    public TableColumn aptIdCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startDateTimeCol;
    public TableColumn endDateTimeCol;
    public TableColumn customerIDCol;
    public TableColumn userIdCol;
    public Button addAppointment;
    public Button modifyAppointment;
    public Button deleteAppointment;
    public RadioButton viewWeek;
    public ToggleGroup weekMonth;
    public RadioButton viewMonth;
    public RadioButton viewAll;
    public Button viewCustomers;
    public Button exitButton;
    public Button typeReport;
    public Button contactReport;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ResultSet rs = null;
        try {
            rs = Select("select appointments.Appointment_ID,  appointments.Title, appointments.Description ,appointments.Location,contacts.contact_Name,\n" +
                    "appointments.Type,appointments.Start,appointments.End,appointments.Customer_ID,appointments.User_ID\n" +
                    "from appointments \n" +
                    "join contacts\n" +" on appointments.Contact_ID = contacts.Contact_ID\n order by appointments.Appointment_ID ");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                Appointment.populate(rs);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        aptIdCol.setCellValueFactory(new PropertyValueFactory<>("aptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateNTime"));
        endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateNTime"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        aptTable.setItems(DataStorage.getAllAppointments());

    }



    public void OnAddAppointment(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Customers");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void OnModifyAppointment(ActionEvent actionEvent) {
    }

    public void OnDeleteAppointment(ActionEvent actionEvent) {
    }

    public void OnViewWeek(ActionEvent actionEvent) {
        ObservableList<Appointment> weeklyApts = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        //Month month = today.getMonth();
        DayOfWeek day = today.getDayOfWeek();
        System.out.println(day.toString());
        int year = today.getYear();
        int n=0;
        switch(day){
            case SUNDAY:
                n= 6;
                break;
            case MONDAY:
                n=5;
                break;
            case TUESDAY:
                n=4;
                break;
            case WEDNESDAY:
                n=3;
                break;
            case THURSDAY:
                n=2;
                break;
            case FRIDAY:
                n=1;
                break;
            case SATURDAY:
                n=0;
                break;
        }

        ObservableList<LocalDate> dates = FXCollections.observableArrayList();
        LocalDate tomw ;
        for(int i=1; i<=n; i++){
            System.out.println("day- "+n);
            tomw = today.plusDays(i);
            System.out.println(tomw);
           dates.add(tomw);
        }
        for(Appointment apt : DataStorage.getAllAppointments()){
          if(dates.contains(apt.getStartDateNTime().toLocalDate())){
              weeklyApts.add(apt);
          }
        }
        aptTable.setItems(weeklyApts);

    }

    public void OnViewMonth(ActionEvent actionEvent) {
        ObservableList<Appointment> monthlyApts = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        Month month = today.getMonth();
        int year = today.getYear();
        for(Appointment apt : DataStorage.getAllAppointments()){
            if(apt.getStartDateNTime().getYear()==year){
                if(apt.getStartDateNTime().getMonth()==month){
                    monthlyApts.add(apt);
                }
            }
        }
        aptTable.setItems(monthlyApts);
    }

    public void OnViewAll(ActionEvent actionEvent) {
        aptTable.setItems(DataStorage.getAllAppointments());

    }

    public void OnViewCustomers(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Customers");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnExit(ActionEvent actionEvent) {
    }


    public void onTypeReport(ActionEvent actionEvent) {
    }

    public void onContactReport(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentTypeScreen.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();
    }
}
